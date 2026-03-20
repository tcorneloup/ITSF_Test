package Recruitement.ITSF.Controller;

import Recruitement.ITSF.Bean.Subscription;
import Recruitement.ITSF.Entity.OptionEntity;
import Recruitement.ITSF.Entity.SubscriptionEntity;
import Recruitement.ITSF.Error.ErrorMessageImpl;
import Recruitement.ITSF.Error.ErrorMessageUtils;
import Recruitement.ITSF.Exception.CustomClassException;
import Recruitement.ITSF.Service.Enum.OptionType;
import Recruitement.ITSF.Service.Enum.SubscriptionType;
import Recruitement.ITSF.Service.SubscriptionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.Collections;

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
        // Arrange
        when(subscriptionService.findAllSubscriptionsWithOptions()).thenReturn(Collections.emptyList());

        // Act + Assert
        mockMvc.perform(get("/api/ITSF/subscription"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void findAllSubscriptions_shouldReturn200WithSubscriptions() throws Exception {
        // Arrange
        Subscription lSubscription = new Subscription();
        lSubscription.setClientId(1L);
        lSubscription.setType(SubscriptionType.FIX);

        when(subscriptionService.findAllSubscriptionsWithOptions())
                .thenReturn(Collections.singletonList(lSubscription));

        // Act + Assert
        mockMvc.perform(get("/api/ITSF/subscription"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void newSubscription_shouldReturnError400WhenClientIdIsNull() throws Exception {
        // Arrange
        Subscription lSubscriptionRequest = new Subscription();
        lSubscriptionRequest.setClientId(null);
        lSubscriptionRequest.setType(SubscriptionType.MOBILE);

        // Act + Assert
        mockMvc.perform(post("/api/ITSF/subscription/newSubscription")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lSubscriptionRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void newSubscription_shouldReturnError400WhenOptionIsNull() throws Exception {
        Subscription lSubscriptionRequest = new Subscription();
        lSubscriptionRequest.setId(1L);
        lSubscriptionRequest.setType(null);

        mockMvc.perform(post("/api/ITSF/subscription/newSubscription")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lSubscriptionRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void newSubscription_shouldReturn200WhenSuccess() throws Exception {
        // Arrange
        Subscription lSubscription = new Subscription();
        lSubscription.setClientId(42L);
        lSubscription.setType(SubscriptionType.MOBILE);

        SubscriptionEntity lSavedSubscriptionEntity = new SubscriptionEntity();
        lSavedSubscriptionEntity.setId(1L);
        lSavedSubscriptionEntity.setClientId(42L);
        lSavedSubscriptionEntity.setType(SubscriptionType.MOBILE);

        when(subscriptionService.addNewSubscription(any(SubscriptionEntity.class)))
                .thenReturn(lSavedSubscriptionEntity);

        // Act + Assert
        mockMvc.perform(post("/api/ITSF/subscription/newSubscription")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lSubscription)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.clientId").value(42))
                .andExpect(jsonPath("$.type").value("MOBILE"));
    }

    @Test
    void addOptionToExistingSubscription_shouldReturn400WhenOptionTypeIsNull() throws Exception {
        mockMvc.perform(post("/api/ITSF/subscription/1/null"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addOptionToExistingSubscription_shouldReturn400WhenClientIdIsNull() throws Exception {
        mockMvc.perform(post("/api/ITSF/subscription/null/NETFLIX"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addOptionToExistingSubscription_shouldReturn400WhenSubscriptionNotFound() throws Exception {

        when(errorMessageUtils.buildErrorMessage("subscription.isNull"))
                .thenReturn(new ErrorMessageImpl("subscription.isNull", "The subscription was not registered."));
        when(subscriptionService.getSubscriptionById(99L))
                .thenThrow(new CustomClassException("subscription.isNull"));

        mockMvc.perform(post("/api/ITSF/subscription/99/NETFLIX"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("The subscription was not registered."));
    }

    @Test
    void addOptionToExistingSubscription_shouldReturn200WhenSuccess() throws Exception {

        OptionEntity lOptionEntity = new OptionEntity();
        lOptionEntity.setId(1L);
        lOptionEntity.setName(OptionType.NETFLIX);

        SubscriptionEntity lSubscriptionEntity = new SubscriptionEntity();
        lSubscriptionEntity.setId(1L);
        lSubscriptionEntity.setClientId(42L);
        lSubscriptionEntity.setType(SubscriptionType.FIBER);
        lSubscriptionEntity.setOptionsList(Collections.singletonList(lOptionEntity));

        SubscriptionEntity lUpdatedSubscriptionEntity = new SubscriptionEntity();
        lUpdatedSubscriptionEntity.setId(1L);
        lUpdatedSubscriptionEntity.setClientId(42L);
        lUpdatedSubscriptionEntity.setType(SubscriptionType.FIBER);
        lUpdatedSubscriptionEntity.setOptionsList(Collections.singletonList(lOptionEntity));

        when(subscriptionService.getSubscriptionById(1L)).thenReturn(lSubscriptionEntity);
        when(subscriptionService.addOptionToExistingSubscription(lSubscriptionEntity, OptionType.ROAMING))
                .thenReturn(lUpdatedSubscriptionEntity);

        // Act + Assert
        mockMvc.perform(post("/api/ITSF/subscription/1/ROAMING"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.clientId").value(42))
                .andExpect(jsonPath("$.type").value("FIBER"))
                .andExpect(jsonPath("$.optionsList").isArray());
    }
}