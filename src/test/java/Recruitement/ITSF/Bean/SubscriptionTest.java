package Recruitement.ITSF.Bean;

import Recruitement.ITSF.Entity.OptionEntity;
import Recruitement.ITSF.Entity.SubscriptionEntity;
import Recruitement.ITSF.Service.Enum.OptionType;
import Recruitement.ITSF.Service.Enum.SubscriptionType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SubscriptionTest {

    @Test
    void fromSubscription_ShouldReturnNullIfSubscriptionEntityIsNull() {
        assertNull(Subscription.fromSubscription(null));
    }

    @Test
    void fromSubscription_ShouldReturnSubscriptionBeanIfSubscriptionEntityIsNotNull() {

        LocalDateTime lDate = LocalDateTime.of(2026, 3, 10, 12, 30);

        SubscriptionEntity lSubscriptionEntity = new SubscriptionEntity();
        lSubscriptionEntity.setId(1L);
        lSubscriptionEntity.setClientId(2L);
        lSubscriptionEntity.setType(SubscriptionType.FIBER);
        lSubscriptionEntity.setSubscriptionDateStart(lDate);
        lSubscriptionEntity.setOptionsList(List.of(new OptionEntity()));

        Subscription lSubscription = Subscription.fromSubscription(lSubscriptionEntity);

        assertNotNull(lSubscription);
        assertEquals(1L, lSubscription.getId());
        assertEquals(2L, lSubscription.getClientId());
        assertEquals(SubscriptionType.FIBER, lSubscription.getType());
        assertEquals(lDate, lSubscription.getSubscriptionDateStart());

    }

    @Test
    void toSubscription_shouldReturnSubscriptionEntity() {

        LocalDateTime lDate = LocalDateTime.of(2026, 3, 10, 12, 30);

        Subscription lSubscription = new Subscription();
        lSubscription.setId(1L);
        lSubscription.setType(SubscriptionType.FIBER);
        lSubscription.setSubscriptionDateStart(lDate);

        SubscriptionEntity lSubscriptionEntity = lSubscription.toSubscriptionEntity();

        assertNotNull(lSubscriptionEntity);
        assertEquals(1L, lSubscriptionEntity.getId());
        assertEquals(SubscriptionType.FIBER, lSubscriptionEntity.getType());
        assertEquals(lDate, lSubscriptionEntity.getSubscriptionDateStart());
    }

    @Test
    void toSubscription_shouldReturnSubscriptionEntityWithOptionIfOptionIsNotNull() {

        LocalDateTime lDate = LocalDateTime.of(2026, 3, 10, 12, 30);

        Option lOption = new Option();
        lOption.setId(1L);
        lOption.setName(OptionType.NETFLIX);
        lOption.setOptionSubDateStart(lDate);

        Subscription lSubscription = new Subscription();
        lSubscription.setId(1L);
        lSubscription.setType(SubscriptionType.FIBER);
        lSubscription.setSubscriptionDateStart(lDate);
        lSubscription.setOptionsList(List.of(lOption));

        SubscriptionEntity lSubscriptionEntity = lSubscription.toSubscriptionEntity();
        OptionEntity lOptionEntity = lSubscriptionEntity.getOptionsList().getFirst();

        assertNotNull(lSubscriptionEntity);
        assertEquals(1L, lSubscriptionEntity.getId());
        assertEquals(SubscriptionType.FIBER, lSubscriptionEntity.getType());
        assertEquals(lDate, lSubscriptionEntity.getSubscriptionDateStart());

        assertEquals(lOption.getId(), lOptionEntity.getId());
        assertEquals(lOption.getName(), lOptionEntity.getName());
        assertEquals(lOption.getOptionSubDateStart(), lOptionEntity.getOptionSubDateStart());
    }

    @Test
    void toSubscriptionEntity_shouldNotSetOptionsWhenOptionsListIsNull() {

        Subscription lSubscription = new Subscription();
        lSubscription.setClientId(42L);
        lSubscription.setType(SubscriptionType.MOBILE);
        lSubscription.setOptionsList(null);

        SubscriptionEntity lSubscriptionEntity = lSubscription.toSubscriptionEntity();

        assertNotNull(lSubscriptionEntity);
        assertTrue(lSubscriptionEntity.getOptionsList().isEmpty());
    }
}
