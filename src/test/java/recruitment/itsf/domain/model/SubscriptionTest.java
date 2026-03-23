package recruitment.itsf.domain.model;

import recruitment.itsf.infra.entity.OptionEntity;
import recruitment.itsf.infra.entity.SubscriptionEntity;
import recruitment.itsf.infra.mapper.SubscriptionEntityMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SubscriptionTest {

    @Test
    void subscriptionEntityToDomain_ShouldReturnNullIfSubscriptionEntityIsNull() {
        assertNull(SubscriptionEntityMapper.subscriptionEntitytoDomain(null));
    }

    @Test
    void subscriptionEntityToDomain_ShouldReturnSubscriptionWhenEntityIsNotNull() {
        LocalDateTime lDate = LocalDateTime.of(2026, 3, 10, 12, 30);

        OptionEntity lOptionEntity = new OptionEntity();
        lOptionEntity.setOptionType(OptionType.NETFLIX);
        lOptionEntity.setOptionSubDateStart(lDate);

        SubscriptionEntity lSubscriptionEntity = new SubscriptionEntity();
        lSubscriptionEntity.setId(1L);
        lSubscriptionEntity.setClientId(2L);
        lSubscriptionEntity.setType(SubscriptionType.FIBER);
        lSubscriptionEntity.setSubscriptionDateStart(lDate);
        lSubscriptionEntity.setOptionsList(List.of(lOptionEntity));

        Subscription lSubscription = SubscriptionEntityMapper.subscriptionEntitytoDomain(lSubscriptionEntity);

        assertNotNull(lSubscription);
        assertEquals(1L, lSubscription.getId());
        assertEquals(2L, lSubscription.getClientId());
        assertEquals(SubscriptionType.FIBER, lSubscription.getType());
        assertEquals(lDate, lSubscription.getSubscriptionDateStart());
        assertEquals(1, lSubscription.getOptionsList().size());
        assertEquals(OptionType.NETFLIX, lSubscription.getOptionsList().getFirst().getOptionType());
    }

    @Test
    void subscriptionDomainToEntity_ShouldReturnNullIfSubscriptionIsNull() {
        assertNull(SubscriptionEntityMapper.subscriptionDomaintoEntity(null));
    }

    @Test
    void subscriptionDomainToEntity_ShouldReturnSubscriptionEntity() {
        LocalDateTime lDate = LocalDateTime.of(2026, 3, 10, 12, 30);

        Subscription lSubscription = new Subscription();
        lSubscription.setId(1L);
        lSubscription.setClientId(2L);
        lSubscription.setType(SubscriptionType.FIBER);
        lSubscription.setSubscriptionDateStart(lDate);

        SubscriptionEntity lSubscriptionEntity = SubscriptionEntityMapper.subscriptionDomaintoEntity(lSubscription);

        assertNotNull(lSubscriptionEntity);
        assertEquals(1L, lSubscriptionEntity.getId());
        assertEquals(2L, lSubscriptionEntity.getClientId());
        assertEquals(SubscriptionType.FIBER, lSubscriptionEntity.getType());
        assertEquals(lDate, lSubscriptionEntity.getSubscriptionDateStart());
    }

    @Test
    void subscriptionDomainToEntity_ShouldReturnEntityWithOptions() {
        LocalDateTime lDate = LocalDateTime.of(2026, 3, 10, 12, 30);

        Option lOption = new Option();
        lOption.setId(1L);
        lOption.setOptionType(OptionType.NETFLIX);
        lOption.setOptionSubDateStart(lDate);

        Subscription lSubscription = new Subscription();
        lSubscription.setId(1L);
        lSubscription.setType(SubscriptionType.FIBER);
        lSubscription.setSubscriptionDateStart(lDate);
        lSubscription.setOptionList(List.of(lOption));

        SubscriptionEntity lSubscriptionEntity = SubscriptionEntityMapper.subscriptionDomaintoEntity(lSubscription);

        assertNotNull(lSubscriptionEntity);
        assertEquals(1, lSubscriptionEntity.getOptionsList().size());
        assertEquals(OptionType.NETFLIX, lSubscriptionEntity.getOptionsList().getFirst().getOptionType());
    }

    @Test
    void subscriptionDomainToEntity_ShouldNotSetOptionsWhenOptionsListIsNull() {
        Subscription lSubscription = new Subscription();
        lSubscription.setClientId(42L);
        lSubscription.setType(SubscriptionType.MOBILE);
        lSubscription.setOptionList(null);

        SubscriptionEntity lSubscriptionEntity = SubscriptionEntityMapper.subscriptionDomaintoEntity(lSubscription);

        assertNotNull(lSubscriptionEntity);
        assertTrue(lSubscriptionEntity.getOptionsList().isEmpty());
    }
}
