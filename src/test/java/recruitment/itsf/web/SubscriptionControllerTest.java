package recruitment.itsf.web;

import recruitment.itsf.domain.exception.DomainException;
import recruitment.itsf.domain.model.Option;
import recruitment.itsf.domain.model.OptionType;
import recruitment.itsf.domain.model.Subscription;
import recruitment.itsf.domain.model.SubscriptionType;
import recruitment.itsf.domain.service.SubscriptionService;
import recruitment.itsf.web.controller.SubscriptionController;
import recruitment.itsf.web.dto.SubscriptionRequest;
import recruitment.itsf.web.error.ErrorMessageImpl;
import recruitment.itsf.web.error.ErrorMessageUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SubscriptionController.class)
class SubscriptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SubscriptionService subscriptionService;

    @MockitoBean
    private ErrorMessageUtils errorMessageUtils;

    @Test
    void findAllSubscriptions_shouldReturn200WithEmptyList() throws Exception {
        when(subscriptionService.findAllSubscriptionsWithOptions()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/itsf/subscription"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void findAllSubscriptions_shouldReturn200WithSubscriptions() throws Exception {
        Subscription lSubscription = new Subscription();
        lSubscription.setId(1L);
        lSubscription.setClientId(1L);
        lSubscription.setType(SubscriptionType.FIX);
        lSubscription.setSubscriptionDateStart(LocalDateTime.now());

        when(subscriptionService.findAllSubscriptionsWithOptions())
                .thenReturn(Collections.singletonList(lSubscription));

        mockMvc.perform(get("/api/itsf/subscription"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].clientId").value(1))
                .andExpect(jsonPath("$[0].type").value("FIX"));
    }

    @Test
    void newSubscription_shouldReturnError400WhenClientIdIsNull() throws Exception {
        SubscriptionRequest lRequest = new SubscriptionRequest(null, SubscriptionType.MOBILE, null);

        mockMvc.perform(post("/api/itsf/subscription")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void newSubscription_shouldReturnError400WhenTypeIsNull() throws Exception {
        SubscriptionRequest lRequest = new SubscriptionRequest(1L, null, null);

        mockMvc.perform(post("/api/itsf/subscription")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void newSubscription_shouldReturn200WhenSuccess() throws Exception {
        Subscription lSavedSubscription = new Subscription();
        lSavedSubscription.setId(1L);
        lSavedSubscription.setClientId(42L);
        lSavedSubscription.setType(SubscriptionType.MOBILE);
        lSavedSubscription.setSubscriptionDateStart(LocalDateTime.now());

        when(subscriptionService.addNewSubscription(any(Subscription.class)))
                .thenReturn(lSavedSubscription);

        SubscriptionRequest lRequest = new SubscriptionRequest(42L, SubscriptionType.MOBILE, null);

        mockMvc.perform(post("/api/itsf/subscription")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.clientId").value(42))
                .andExpect(jsonPath("$.type").value("MOBILE"));
    }


    @Test
    void addOptionToExistingSubscription_shouldReturn400WhenOptionTypeIsInvalid() throws Exception {
        mockMvc.perform(post("/api/itsf/subscription/1/option/INVALID_OPTION"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addOptionToExistingSubscription_shouldReturn400WhenIdIsInvalid() throws Exception {
        mockMvc.perform(post("/api/itsf/subscription/null//option/NETFLIX"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addOptionToExistingSubscription_shouldReturn400WhenSubscriptionNotFound() throws Exception {
        when(errorMessageUtils.buildErrorMessage("subscription.isNull"))
                .thenReturn(new ErrorMessageImpl("subscription.isNull", "The subscription was not registered."));
        when(subscriptionService.getSubscriptionById(99L))
                .thenThrow(new DomainException("subscription.isNull"));

        mockMvc.perform(post("/api/itsf/subscription/99/option/NETFLIX"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("The subscription was not registered."));
    }

    @Test
    void addOptionToExistingSubscription_shouldReturn200WhenSuccess() throws Exception {
        Option lOption = new Option();
        lOption.setId(1L);
        lOption.setOptionType(OptionType.NETFLIX);
        lOption.setOptionSubDateStart(LocalDateTime.now());

        Subscription lSubscription = new Subscription();
        lSubscription.setId(1L);
        lSubscription.setClientId(42L);
        lSubscription.setType(SubscriptionType.FIBER);
        lSubscription.setSubscriptionDateStart(LocalDateTime.now());
        lSubscription.setOptionList(List.of(lOption));

        Subscription lUpdatedSubscription = new Subscription();
        lUpdatedSubscription.setId(1L);
        lUpdatedSubscription.setClientId(42L);
        lUpdatedSubscription.setType(SubscriptionType.FIBER);
        lUpdatedSubscription.setSubscriptionDateStart(LocalDateTime.now());
        lUpdatedSubscription.setOptionList(List.of(lOption));

        when(subscriptionService.getSubscriptionById(1L)).thenReturn(lSubscription);
        when(subscriptionService.addOptionToExistingSubscription(lSubscription, OptionType.ROAMING))
                .thenReturn(lUpdatedSubscription);

        mockMvc.perform(post("/api/itsf/subscription/1/option/ROAMING"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.clientId").value(42))
                .andExpect(jsonPath("$.type").value("FIBER"))
                .andExpect(jsonPath("$.options").isArray());
    }
}