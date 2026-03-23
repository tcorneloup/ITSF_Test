package recruitment.itsf.web;

import recruitment.itsf.domain.model.Option;
import recruitment.itsf.domain.model.OptionType;
import recruitment.itsf.domain.model.Subscription;
import recruitment.itsf.domain.model.SubscriptionType;
import recruitment.itsf.web.dto.OptionRequest;
import recruitment.itsf.web.dto.OptionResponse;
import recruitment.itsf.web.dto.SubscriptionRequest;
import recruitment.itsf.web.dto.SubscriptionResponse;
import recruitment.itsf.web.mapper.SubscriptionDtoMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SubscriptionDtoMapperTest {

    private static final LocalDateTime DATE = LocalDateTime.of(2026, 3, 10, 12, 30);

    @Test
    void subscriptionRequestToDomain_shouldReturnNullWhenRequestIsNull() {
        assertNull(SubscriptionDtoMapper.subscriptionRequestToDomain(null));
    }

    @Test
    void subscriptionRequestToDomain_shouldMapFieldsCorrectly() {
        SubscriptionRequest request = new SubscriptionRequest(42L, SubscriptionType.MOBILE, null);

        Subscription result = SubscriptionDtoMapper.subscriptionRequestToDomain(request);

        assertNotNull(result);
        assertEquals(42L, result.getClientId());
        assertEquals(SubscriptionType.MOBILE, result.getType());
        assertNull(result.getId());
        assertNull(result.getSubscriptionDateStart());
        assertTrue(result.getOptionsList().isEmpty());
    }

    @Test
    void subscriptionRequestToDomain_shouldMapOptionsCorrectly() {
        OptionRequest optionRequest = new OptionRequest(OptionType.NETFLIX, DATE);
        SubscriptionRequest request = new SubscriptionRequest(42L, SubscriptionType.FIBER, List.of(optionRequest));

        Subscription result = SubscriptionDtoMapper.subscriptionRequestToDomain(request);

        assertEquals(1, result.getOptionsList().size());
        Option option = result.getOptionsList().getFirst();
        assertEquals(OptionType.NETFLIX, option.getOptionType());
        assertEquals(DATE, option.getOptionSubDateStart());
    }

    @Test
    void subscriptionRequestToDomain_shouldHandleEmptyOptionsList() {
        SubscriptionRequest request = new SubscriptionRequest(42L, SubscriptionType.FIX, Collections.emptyList());

        Subscription result = SubscriptionDtoMapper.subscriptionRequestToDomain(request);

        assertNotNull(result);
        assertTrue(result.getOptionsList().isEmpty());
    }

    @Test
    void optionRequestToDomain_shouldReturnNullWhenRequestIsNull() {
        assertNull(SubscriptionDtoMapper.optionRequestToDomain(null));
    }

    @Test
    void optionRequestToDomain_shouldMapFieldsCorrectly() {
        OptionRequest request = new OptionRequest(OptionType.ROAMING, DATE);

        Option result = SubscriptionDtoMapper.optionRequestToDomain(request);

        assertNotNull(result);
        assertEquals(OptionType.ROAMING, result.getOptionType());
        assertEquals(DATE, result.getOptionSubDateStart());
        assertNull(result.getId());
    }

    @Test
    void subscriptionDomainToResponse_shouldReturnNullWhenSubscriptionIsNull() {
        assertNull(SubscriptionDtoMapper.subscriptionDomainToResponse(null));
    }

    @Test
    void subscriptionDomainToResponse_shouldMapAllFieldsCorrectly() {
        Subscription subscription = new Subscription();
        subscription.setId(1L);
        subscription.setClientId(42L);
        subscription.setType(SubscriptionType.MOBILE);
        subscription.setSubscriptionDateStart(DATE);

        SubscriptionResponse result = SubscriptionDtoMapper.subscriptionDomainToResponse(subscription);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals(42L, result.clientId());
        assertEquals(SubscriptionType.MOBILE, result.type());
        assertEquals(DATE, result.subscriptionDateStart());
        assertTrue(result.options().isEmpty());
    }

    @Test
    void subscriptionDomainToResponse_shouldMapOptionsCorrectly() {
        Option option = new Option();
        option.setId(10L);
        option.setOptionType(OptionType.HD);
        option.setOptionSubDateStart(DATE);

        Subscription subscription = new Subscription();
        subscription.setId(1L);
        subscription.setClientId(42L);
        subscription.setType(SubscriptionType.FIBER);
        subscription.setSubscriptionDateStart(DATE);
        subscription.setOptionList(List.of(option));

        SubscriptionResponse result = SubscriptionDtoMapper.subscriptionDomainToResponse(subscription);

        assertEquals(1, result.options().size());
        OptionResponse optionResponse = result.options().getFirst();
        assertEquals(10L, optionResponse.id());
        assertEquals(OptionType.HD, optionResponse.optionType());
        assertEquals(DATE, optionResponse.optionSubDateStart());
    }

    @Test
    void subscriptionDomainToResponse_shouldHandleNullOptionsList() {
        Subscription subscription = new Subscription();
        subscription.setId(1L);
        subscription.setClientId(42L);
        subscription.setType(SubscriptionType.FIX);
        subscription.setOptionList(null);

        SubscriptionResponse result = SubscriptionDtoMapper.subscriptionDomainToResponse(subscription);

        assertNotNull(result);
        assertTrue(result.options().isEmpty());
    }

    @Test
    void optionDomainToResponse_shouldReturnNullWhenOptionIsNull() {
        assertNull(SubscriptionDtoMapper.optionDomainToResponse(null));
    }

    @Test
    void optionDomainToResponse_shouldMapAllFieldsCorrectly() {
        Option option = new Option();
        option.setId(5L);
        option.setOptionType(OptionType.MUSIC);
        option.setOptionSubDateStart(DATE);

        OptionResponse result = SubscriptionDtoMapper.optionDomainToResponse(option);

        assertNotNull(result);
        assertEquals(5L, result.id());
        assertEquals(OptionType.MUSIC, result.optionType());
        assertEquals(DATE, result.optionSubDateStart());
    }
}

