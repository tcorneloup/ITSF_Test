package Recruitement.ITSF.Repository;

import Recruitement.ITSF.Entity.OptionEntity;
import Recruitement.ITSF.Entity.SubscriptionEntity;
import Recruitement.ITSF.Service.Enum.OptionType;
import Recruitement.ITSF.Service.Enum.SubscriptionType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class SubscriptionRepositoryTest {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Test
    void findAllSubscriptions_shouldReturnEmptyListWhenNoSubscriptionsExist() {
        List<SubscriptionEntity> lSubscriptionsEntityList = subscriptionRepository.findAllSubscriptions();

        assertNotNull(lSubscriptionsEntityList);
        assertTrue(lSubscriptionsEntityList.isEmpty());
    }

    @Test
    void findAllSubscriptions_shouldReturnSubscriptionsWithoutOptions() {
        SubscriptionEntity lSubscriptionList = new SubscriptionEntity();
        lSubscriptionList.setClientId(1L);
        lSubscriptionList.setType(SubscriptionType.MOBILE);
        lSubscriptionList.setSubscriptionDateStart(LocalDateTime.now());
        subscriptionRepository.save(lSubscriptionList);

        List<SubscriptionEntity> lSubscriptionsEntityList = subscriptionRepository.findAllSubscriptions();

        assertEquals(1, lSubscriptionsEntityList.size());
        assertEquals(SubscriptionType.MOBILE, lSubscriptionsEntityList.getFirst().getType());
        assertTrue(lSubscriptionsEntityList.getFirst().getOptionsList().isEmpty());
    }

    @Test
    void findAllSubscriptions_shouldReturnSubscriptionsWithOption() {
        OptionEntity lOptionEntity = new OptionEntity();
        lOptionEntity.setName(OptionType.NETFLIX);
        lOptionEntity.setOptionSubDateStart(LocalDateTime.now());

        SubscriptionEntity lSubscriptionEntity = new SubscriptionEntity();
        lSubscriptionEntity.setClientId(2L);
        lSubscriptionEntity.setType(SubscriptionType.FIBER);
        lSubscriptionEntity.setSubscriptionDateStart(LocalDateTime.now());
        lSubscriptionEntity.setOptionsList(new ArrayList<>(List.of(lOptionEntity)));
        subscriptionRepository.save(lSubscriptionEntity);

        List<SubscriptionEntity> lSubscriptionsEntityList = subscriptionRepository.findAllSubscriptions();

        assertEquals(1, lSubscriptionsEntityList.size());
        assertEquals(1, lSubscriptionsEntityList.getFirst().getOptionsList().size());
        assertEquals(OptionType.NETFLIX, lSubscriptionsEntityList.getFirst().getOptionsList().getFirst().getName());
    }

    @Test
    void findAllSubscriptions_shouldNotReturnDuplicatesWhenSubscriptionHasMultipleOptions() {
        OptionEntity lOptionEntity1 = new OptionEntity();
        lOptionEntity1.setName(OptionType.NETFLIX);
        lOptionEntity1.setOptionSubDateStart(LocalDateTime.now());

        OptionEntity lOptionEntity2 = new OptionEntity();
        lOptionEntity2.setName(OptionType.MUSIC);
        lOptionEntity2.setOptionSubDateStart(LocalDateTime.now());

        SubscriptionEntity lSubscriptionEntity = new SubscriptionEntity();
        lSubscriptionEntity.setClientId(3L);
        lSubscriptionEntity.setType(SubscriptionType.FIBER);
        lSubscriptionEntity.setSubscriptionDateStart(LocalDateTime.now());
        lSubscriptionEntity.setOptionsList(new ArrayList<>(List.of(lOptionEntity1, lOptionEntity2)));
        subscriptionRepository.save(lSubscriptionEntity);

        List<SubscriptionEntity> lSubscriptionsEntityList = subscriptionRepository.findAllSubscriptions();

        assertEquals(1, lSubscriptionsEntityList.size());
        assertEquals(2, lSubscriptionsEntityList.getFirst().getOptionsList().size());
    }

    @Test
    void saveSubscriptionEntity_shouldPersistAndGenerateId() {
        SubscriptionEntity lSubscriptionEntity = new SubscriptionEntity();
        lSubscriptionEntity.setClientId(42L);
        lSubscriptionEntity.setType(SubscriptionType.FIX);
        lSubscriptionEntity.setSubscriptionDateStart(LocalDateTime.now());

        SubscriptionEntity lSavedEntity = subscriptionRepository.save(lSubscriptionEntity);

        assertNotNull(lSavedEntity.getId());
        assertEquals(42L, lSavedEntity.getClientId());
    }

    @Test
    void findById_shouldReturnEmptySubscriptionEntityWhenIdDoesNotExist() {
        Optional<SubscriptionEntity> lSubscriptionEntity = subscriptionRepository.findById(999L);

        assertTrue(lSubscriptionEntity.isEmpty());
    }

    @Test
    void findById_shouldReturnEntityWhenIdExists() {
        SubscriptionEntity lSubscriptionEntity = new SubscriptionEntity();
        lSubscriptionEntity.setClientId(10L);
        lSubscriptionEntity.setType(SubscriptionType.MOBILE);
        lSubscriptionEntity.setSubscriptionDateStart(LocalDateTime.now());
        SubscriptionEntity lSavedSubscriptionEntity = subscriptionRepository.save(lSubscriptionEntity);

        Optional<SubscriptionEntity> lOptionalSubscriptionEntity = subscriptionRepository.findById(lSavedSubscriptionEntity.getId());

        assertTrue(lOptionalSubscriptionEntity.isPresent());
        assertEquals(10L, lOptionalSubscriptionEntity.get().getClientId());
    }
}

