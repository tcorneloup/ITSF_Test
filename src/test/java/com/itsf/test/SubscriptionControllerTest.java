package com.itsf.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itsf.test.dto.AddOptionRequest;
import com.itsf.test.dto.CreateSubscriptionRequest;
import com.itsf.test.dto.SubscriptionDto;
import com.itsf.test.enums.OptionName;
import com.itsf.test.enums.SubscriptionType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class SubscriptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String BASE_URL = "/api/subscriptions";

    // -------- Helper Methods --------

    private SubscriptionDto createSubscription(SubscriptionType type) throws Exception {
        CreateSubscriptionRequest request = new CreateSubscriptionRequest();
        request.setType(type);
        request.setSubscriptionDate(LocalDate.now());
        request.setClientId(1L);

        MvcResult result = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        return objectMapper.readValue(result.getResponse().getContentAsString(), SubscriptionDto.class);
    }

    private void addOption(Long subscriptionId, OptionName optionName) throws Exception {
        AddOptionRequest request = new AddOptionRequest();
        request.setName(optionName);
        request.setSubscriptionDate(LocalDate.now());

        mockMvc.perform(post(BASE_URL + "/" + subscriptionId + "/options")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    // -------- Create Subscription Tests --------

    @Test
    void createSubscription_shouldReturn201() throws Exception {
        CreateSubscriptionRequest request = new CreateSubscriptionRequest();
        request.setType(SubscriptionType.MOBILE);
        request.setSubscriptionDate(LocalDate.of(2024, 1, 15));
        request.setClientId(42L);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.type").value("MOBILE"))
                .andExpect(jsonPath("$.clientId").value(42))
                .andExpect(jsonPath("$.options").isEmpty());
    }

    // -------- Get All Subscriptions Tests --------

    @Test
    void getAllSubscriptions_shouldReturnEmptyList() throws Exception {
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getAllSubscriptions_shouldReturnCreatedSubscriptions() throws Exception {
        createSubscription(SubscriptionType.MOBILE);
        createSubscription(SubscriptionType.FIBER);

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    // -------- Add Option Rules Tests --------

    @Test
    void addRoaming_toMobileSubscription_shouldSucceed() throws Exception {
        SubscriptionDto sub = createSubscription(SubscriptionType.MOBILE);
        addOption(sub.getId(), OptionName.ROAMING);

        mockMvc.perform(get(BASE_URL))
                .andExpect(jsonPath("$[0].options", hasSize(1)))
                .andExpect(jsonPath("$[0].options[0].name").value("ROAMING"));
    }

    @Test
    void addRoaming_toFiberSubscription_shouldFail() throws Exception {
        SubscriptionDto sub = createSubscription(SubscriptionType.FIBER);

        AddOptionRequest request = new AddOptionRequest();
        request.setName(OptionName.ROAMING);
        request.setSubscriptionDate(LocalDate.now());

        mockMvc.perform(post(BASE_URL + "/" + sub.getId() + "/options")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("ROAMING")));
    }

    @Test
    void addRoaming_toFixSubscription_shouldFail() throws Exception {
        SubscriptionDto sub = createSubscription(SubscriptionType.FIX);

        AddOptionRequest request = new AddOptionRequest();
        request.setName(OptionName.ROAMING);
        request.setSubscriptionDate(LocalDate.now());

        mockMvc.perform(post(BASE_URL + "/" + sub.getId() + "/options")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addNetflix_toFiberSubscription_shouldSucceed() throws Exception {
        SubscriptionDto sub = createSubscription(SubscriptionType.FIBER);
        addOption(sub.getId(), OptionName.NETFLIX);

        mockMvc.perform(get(BASE_URL))
                .andExpect(jsonPath("$[0].options[0].name").value("NETFLIX"));
    }

    @Test
    void addNetflix_toMobileSubscription_shouldFail() throws Exception {
        SubscriptionDto sub = createSubscription(SubscriptionType.MOBILE);

        AddOptionRequest request = new AddOptionRequest();
        request.setName(OptionName.NETFLIX);
        request.setSubscriptionDate(LocalDate.now());

        mockMvc.perform(post(BASE_URL + "/" + sub.getId() + "/options")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("NETFLIX")));
    }

    @Test
    void addHd_withNetflixAlreadyPresent_shouldSucceed() throws Exception {
        SubscriptionDto sub = createSubscription(SubscriptionType.FIBER);
        addOption(sub.getId(), OptionName.NETFLIX);
        addOption(sub.getId(), OptionName.HD);

        mockMvc.perform(get(BASE_URL))
                .andExpect(jsonPath("$[0].options", hasSize(2)));
    }

    @Test
    void addHd_withoutNetflix_shouldFail() throws Exception {
        SubscriptionDto sub = createSubscription(SubscriptionType.FIBER);

        AddOptionRequest request = new AddOptionRequest();
        request.setName(OptionName.HD);
        request.setSubscriptionDate(LocalDate.now());

        mockMvc.perform(post(BASE_URL + "/" + sub.getId() + "/options")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("NETFLIX")));
    }

    @Test
    void addMusic_toMobileSubscription_shouldSucceed() throws Exception {
        SubscriptionDto sub = createSubscription(SubscriptionType.MOBILE);
        addOption(sub.getId(), OptionName.MUSIC);

        mockMvc.perform(get(BASE_URL))
                .andExpect(jsonPath("$[0].options[0].name").value("MUSIC"));
    }

    @Test
    void addMusic_toFiberSubscription_shouldSucceed() throws Exception {
        SubscriptionDto sub = createSubscription(SubscriptionType.FIBER);
        addOption(sub.getId(), OptionName.MUSIC);

        mockMvc.perform(get(BASE_URL))
                .andExpect(jsonPath("$[0].options[0].name").value("MUSIC"));
    }

    @Test
    void addMusic_toFixSubscription_shouldFail() throws Exception {
        SubscriptionDto sub = createSubscription(SubscriptionType.FIX);

        AddOptionRequest request = new AddOptionRequest();
        request.setName(OptionName.MUSIC);
        request.setSubscriptionDate(LocalDate.now());

        mockMvc.perform(post(BASE_URL + "/" + sub.getId() + "/options")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addDuplicateOption_shouldFail() throws Exception {
        SubscriptionDto sub = createSubscription(SubscriptionType.MOBILE);
        addOption(sub.getId(), OptionName.ROAMING);

        AddOptionRequest request = new AddOptionRequest();
        request.setName(OptionName.ROAMING);
        request.setSubscriptionDate(LocalDate.now());

        mockMvc.perform(post(BASE_URL + "/" + sub.getId() + "/options")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("already has option")));
    }

    @Test
    void addOption_toNonExistentSubscription_shouldReturn404() throws Exception {
        AddOptionRequest request = new AddOptionRequest();
        request.setName(OptionName.ROAMING);
        request.setSubscriptionDate(LocalDate.now());

        mockMvc.perform(post(BASE_URL + "/999/options")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }
}
