package Recruitement.ITSF.Service;

import Recruitement.ITSF.Bean.Subscription;
import Recruitement.ITSF.Entity.OptionEntity;
import Recruitement.ITSF.Entity.SubscriptionEntity;
import Recruitement.ITSF.Exception.CustomClassException;
import Recruitement.ITSF.Repository.SubscriptionRepository;
import Recruitement.ITSF.Service.Enum.OptionType;
import Recruitement.ITSF.Service.Enum.SubscriptionType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

    @Mock
	private SubscriptionRepository subscriptionRepository;

	@InjectMocks
	private SubscriptionService subscriptionService;

    @Test
    void getSubscriptionById_shouldThrowCustomExceptionWhenIdIsNull() {
        // Arrange
        Long id = null;

        // Act
        CustomClassException exception = assertThrows(CustomClassException.class,
                () -> subscriptionService.getSubscriptionById(id));

        // Assert
        assertEquals("subscription.isNull", exception.getMessage());
    }

    @Test
    void findAllSubscriptionsWithOptions_shouldReturnBeansIfThereAreSomeSubscriptions() {
        // Arrange
        SubscriptionEntity lSubscriptionEntity = new SubscriptionEntity();
        lSubscriptionEntity.setClientId(1L);
        lSubscriptionEntity.setType(SubscriptionType.FIBER);
        lSubscriptionEntity.setSubscriptionDateStart(LocalDateTime.now());

        OptionEntity lOption = new OptionEntity();
        lOption.setName(OptionType.NETFLIX);
        lSubscriptionEntity.setOptionsList(new java.util.ArrayList<>(List.of(lOption)));

        when(subscriptionRepository.findAllSubscriptions()).thenReturn(List.of(lSubscriptionEntity));

        // Act
        List<Subscription> lResult = subscriptionService.findAllSubscriptionsWithOptions();

        // Assert
        assertEquals(1, lResult.size());
        assertNotNull(lResult.get(0));
        verify(subscriptionRepository).findAllSubscriptions();
    }

    @Test
    void findAllSubscriptionsWithOptions_shouldReturnEmptyListIfThereIsNoSubscriptions() {
        // Arrange
        when(subscriptionRepository.findAllSubscriptions()).thenReturn(Collections.emptyList());

        // Act
        List<Subscription> lResult = subscriptionService.findAllSubscriptionsWithOptions();

        // Assert
        assertNotNull(lResult);
        assertTrue(lResult.isEmpty());
        verify(subscriptionRepository).findAllSubscriptions();
    }

	@Test
	void addNewSubscription_shouldThrowCustomExceptionWhenOptionsClientIdIsNull() {
        //Arrange
		SubscriptionEntity lSubscriptionEntity = new SubscriptionEntity();
        lSubscriptionEntity.setClientId(null);
        lSubscriptionEntity.setType(SubscriptionType.MOBILE);

		//Act
		CustomClassException e = assertThrows(CustomClassException.class,
				() -> subscriptionService.addNewSubscription(lSubscriptionEntity));

		//Assert
		assertEquals("subscription.clientId.required", e.getErrorCode());
	}

	@Test
	void addNewSubscription_shouldThrowCustomExceptionWhenSubTypeIsEmpty() {
        // Arrange
		SubscriptionEntity lSubscriptionEntity = new SubscriptionEntity();
        lSubscriptionEntity.setClientId(1L);
        lSubscriptionEntity.setType(null);
        lSubscriptionEntity.setOptionsList(List.of(new OptionEntity()));

        // Act
		CustomClassException e = assertThrows(CustomClassException.class,
				() -> subscriptionService.addNewSubscription(lSubscriptionEntity));

        // Assert
		assertEquals("subscription.type.required", e.getErrorCode());
	}

    @Test
    void addNewSubscription_shouldReturnSubscriptionWhenSubTypeAndClientIdAreNotNull() {
        // Arrange
        SubscriptionEntity lSubscriptionEntity = new SubscriptionEntity();
        lSubscriptionEntity.setClientId(1L);
        lSubscriptionEntity.setType(SubscriptionType.MOBILE);

		SubscriptionEntity lSavedSub = mock(SubscriptionEntity.class);
		when(subscriptionRepository.save(any(SubscriptionEntity.class))).thenReturn(lSavedSub);

        // Act
		SubscriptionEntity lNewSub = subscriptionService.addNewSubscription(lSubscriptionEntity);

        // Assert
		assertSame(lSavedSub, lNewSub);
		verify(subscriptionRepository).save(any(SubscriptionEntity.class));
    }

    @Test
    void addOptionToExistingSubscription_shouldThrowCustomExceptionWhenOptionsTypeIsNull() {
        // Arrange
        SubscriptionEntity lSubscriptionEntity = new SubscriptionEntity();
        lSubscriptionEntity.setClientId(1L);
        lSubscriptionEntity.setType(SubscriptionType.MOBILE);

        OptionEntity lOption = new OptionEntity();
        lOption.setName(null);

        // Act
        CustomClassException e = assertThrows(CustomClassException.class,
                () -> subscriptionService.addOptionToExistingSubscription(lSubscriptionEntity, null));

        // Assert
        assertEquals("option.isNull", e.getErrorCode());
    }

    @Test
    void addOptionToExistingSubscription_shouldReturnSubscriptionWhenOptionTypeIsNotNull() {
        // Arrange
        SubscriptionEntity lSubscriptionEntity = new SubscriptionEntity();
        lSubscriptionEntity.setClientId(1L);
        lSubscriptionEntity.setType(SubscriptionType.FIBER);

        SubscriptionEntity lSavedSub = mock(SubscriptionEntity.class);
        when(subscriptionRepository.save(any(SubscriptionEntity.class))).thenReturn(lSavedSub);

        // Act
        SubscriptionEntity lNewSub = subscriptionService.addOptionToExistingSubscription(lSubscriptionEntity, OptionType.NETFLIX);

        // Assert
        assertSame(lSavedSub, lNewSub);
        verify(subscriptionRepository).save(any(SubscriptionEntity.class));
    }

    @Test
    void manageOptions_shouldThrowCustomExceptionWhenOptionsTypeIsNull() {
        // Arrange
        SubscriptionEntity lSubscriptionEntity = new SubscriptionEntity();
        lSubscriptionEntity.setClientId(1L);
        lSubscriptionEntity.setType(SubscriptionType.MOBILE);

        OptionEntity lOption = new OptionEntity();
        lOption.setName(null);

        lSubscriptionEntity.setOptionsList(new java.util.ArrayList<>());
        lSubscriptionEntity.getOptionsList().add(lOption);

        // Act
        CustomClassException e = assertThrows(CustomClassException.class,
                () -> subscriptionService.addNewSubscription(lSubscriptionEntity));

        // Assert
        assertEquals("option.isNull", e.getErrorCode());
    }

    @Test
    void manageOptions_shouldThrowCustomExceptionWhenOptionsAlreadyExists() {
        // Arrange
        SubscriptionEntity lSubscriptionEntity = new SubscriptionEntity();
        lSubscriptionEntity.setClientId(1L);
        lSubscriptionEntity.setType(SubscriptionType.MOBILE);

        OptionEntity lOption = new OptionEntity();
        lOption.setName(OptionType.NETFLIX);

        OptionEntity lOption2 = new OptionEntity();
        lOption2.setName(OptionType.NETFLIX);

        lSubscriptionEntity.setOptionsList(new java.util.ArrayList<>());
        lSubscriptionEntity.getOptionsList().add(lOption);
        lSubscriptionEntity.getOptionsList().add(lOption2);

        // Act
        CustomClassException e = assertThrows(CustomClassException.class,
                () -> subscriptionService.addNewSubscription(lSubscriptionEntity));

        // Assert
        assertEquals("option.already.exists", e.getErrorCode());
    }

    @Test
    void manageOptions_shouldThrowCustomExceptionWhenRoamingWithoutMobile() {
        // Arrange
        SubscriptionEntity lSubscriptionEntity = new SubscriptionEntity();
        lSubscriptionEntity.setClientId(1L);
        lSubscriptionEntity.setType(SubscriptionType.FIX);

        OptionEntity lOption = new OptionEntity();
        lOption.setName(OptionType.ROAMING);

        lSubscriptionEntity.setOptionsList(new java.util.ArrayList<>());
        lSubscriptionEntity.getOptionsList().add(lOption);

        // Act
        CustomClassException e = assertThrows(CustomClassException.class,
                () -> subscriptionService.addNewSubscription(lSubscriptionEntity));

        // Assert
        assertEquals("option.roaming.not.allowed", e.getErrorCode());
    }

    @Test
    void manageOptions_shouldThrowCustomExceptionWhenNetflixWithoutFiber() {
        // Arrange
        SubscriptionEntity lSubscriptionEntity = new SubscriptionEntity();
        lSubscriptionEntity.setClientId(1L);
        lSubscriptionEntity.setType(SubscriptionType.MOBILE);

        OptionEntity lOption = new OptionEntity();
        lOption.setName(OptionType.NETFLIX);

        lSubscriptionEntity.setOptionsList(new java.util.ArrayList<>());
        lSubscriptionEntity.getOptionsList().add(lOption);

        // Act
        CustomClassException e = assertThrows(CustomClassException.class,
                () -> subscriptionService.addNewSubscription(lSubscriptionEntity));

        // Assert
        assertEquals("option.netflix.not.allowed", e.getErrorCode());
    }

    @Test
    void manageOptions_shouldThrowCustomExceptionWhenHDWithoutNetflix() {
        // Arrange
        SubscriptionEntity lSubscriptionEntity = new SubscriptionEntity();
        lSubscriptionEntity.setClientId(1L);
        lSubscriptionEntity.setType(SubscriptionType.MOBILE);

        OptionEntity lOption = new OptionEntity();
        lOption.setName(OptionType.ROAMING);

        OptionEntity lOption2 = new OptionEntity();
        lOption2.setName(OptionType.HD);

        lSubscriptionEntity.setOptionsList(new java.util.ArrayList<>());
        lSubscriptionEntity.getOptionsList().add(lOption);
        lSubscriptionEntity.getOptionsList().add(lOption2);

        // Act
        CustomClassException e = assertThrows(CustomClassException.class,
                () -> subscriptionService.addNewSubscription(lSubscriptionEntity));

        // Assert
        assertEquals("option.hd.requires.netflix", e.getErrorCode());
    }

    @Test
    void manageOptions_shouldThrowCustomExceptionWhenMusicWithFix() {
        // Arrange
        SubscriptionEntity lSubscriptionEntity = new SubscriptionEntity();
        lSubscriptionEntity.setClientId(1L);
        lSubscriptionEntity.setType(SubscriptionType.FIX);

        OptionEntity lOption = new OptionEntity();
        lOption.setName(OptionType.MUSIC);

        lSubscriptionEntity.setOptionsList(new java.util.ArrayList<>());
        lSubscriptionEntity.getOptionsList().add(lOption);

        // Act
        CustomClassException e = assertThrows(CustomClassException.class,
                () -> subscriptionService.addNewSubscription(lSubscriptionEntity));

        // Assert
        assertEquals("option.music.not.allowed", e.getErrorCode());
    }

    @Test
    void manageOptions_shouldSaveWhenOptionsRoamingAndMusicWithMobile() {
        // Arrange
        SubscriptionEntity lSubscriptionEntity = new SubscriptionEntity();
        lSubscriptionEntity.setClientId(1L);
        lSubscriptionEntity.setType(SubscriptionType.MOBILE);

        OptionEntity lOption = new OptionEntity();
        lOption.setName(OptionType.MUSIC);

        OptionEntity lOption2 = new OptionEntity();
        lOption2.setName(OptionType.ROAMING);

        lSubscriptionEntity.setOptionsList(new java.util.ArrayList<>());
        lSubscriptionEntity.getOptionsList().add(lOption);
        lSubscriptionEntity.getOptionsList().add(lOption2);

        SubscriptionEntity lSavedSub = mock(SubscriptionEntity.class);
        when(subscriptionRepository.save(any(SubscriptionEntity.class))).thenReturn(lSavedSub);

        // Act
        SubscriptionEntity lNewSub = subscriptionService.addNewSubscription(lSubscriptionEntity);

        // Assert
        assertSame(lSavedSub, lNewSub);
        verify(subscriptionRepository).save(any(SubscriptionEntity.class));
    }

    @Test
    void manageOptions_shouldSaveWhenOptionsNetflixAndMusicAndHDWithFiber() {
        // Arrange
        SubscriptionEntity lSubscriptionEntity = new SubscriptionEntity();
        lSubscriptionEntity.setClientId(1L);
        lSubscriptionEntity.setType(SubscriptionType.FIBER);

        OptionEntity lOption = new OptionEntity();
        lOption.setName(OptionType.NETFLIX);

        OptionEntity lOption2 = new OptionEntity();
        lOption2.setName(OptionType.MUSIC);

        OptionEntity lOption3 = new OptionEntity();
        lOption3.setName(OptionType.HD);

        lSubscriptionEntity.setOptionsList(new java.util.ArrayList<>());
        lSubscriptionEntity.getOptionsList().add(lOption);
        lSubscriptionEntity.getOptionsList().add(lOption2);
        lSubscriptionEntity.getOptionsList().add(lOption3);

        SubscriptionEntity lSavedSub = mock(SubscriptionEntity.class);
        when(subscriptionRepository.save(any(SubscriptionEntity.class))).thenReturn(lSavedSub);

        // Act
        SubscriptionEntity lNewSub = subscriptionService.addNewSubscription(lSubscriptionEntity);

        // Assert
        assertSame(lSavedSub, lNewSub);
        verify(subscriptionRepository).save(any(SubscriptionEntity.class));
    }

}
