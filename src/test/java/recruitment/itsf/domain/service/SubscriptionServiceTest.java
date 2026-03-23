package recruitment.itsf.domain.service;

import recruitment.itsf.domain.exception.DomainException;
import recruitment.itsf.domain.repository.SubscriptionRepository;
import recruitment.itsf.domain.model.Option;
import recruitment.itsf.domain.model.OptionType;
import recruitment.itsf.domain.model.Subscription;
import recruitment.itsf.domain.model.SubscriptionType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
    void getSubscriptionById_shouldThrowDomainExceptionWhenNotFound() {
        when(subscriptionRepository.findById(any())).thenReturn(Optional.empty());

        DomainException exception = assertThrows(DomainException.class,
                () -> subscriptionService.getSubscriptionById(999L));

        assertEquals("subscription.isNull", exception.getMessage());
    }

    @Test
    void findAllSubscriptionsWithOptions_shouldReturnSubscriptionsIfThereAreSome() {
        Subscription lSubscription = new Subscription();
        lSubscription.setClientId(1L);
        lSubscription.setType(SubscriptionType.FIBER);
        lSubscription.setSubscriptionDateStart(LocalDateTime.now());

        Option lOption = new Option();
        lOption.setOptionType(OptionType.NETFLIX);
        lSubscription.setOptionList(new java.util.ArrayList<>(List.of(lOption)));

        when(subscriptionRepository.findAllSubscriptionsWithOptions())
                .thenReturn(List.of(lSubscription));

        List<Subscription> lResult = subscriptionService.findAllSubscriptionsWithOptions();

        assertEquals(1, lResult.size());
        assertNotNull(lResult.getFirst());
        verify(subscriptionRepository).findAllSubscriptionsWithOptions();
    }

    @Test
    void findAllSubscriptionsWithOptions_shouldReturnEmptyListIfThereIsNoSubscriptions() {
        when(subscriptionRepository.findAllSubscriptionsWithOptions())
                .thenReturn(Collections.emptyList());

        List<Subscription> lResult = subscriptionService.findAllSubscriptionsWithOptions();

        assertNotNull(lResult);
        assertTrue(lResult.isEmpty());
        verify(subscriptionRepository).findAllSubscriptionsWithOptions();
    }

    @Test
    void addNewSubscription_shouldThrowDomainExceptionWhenClientIdIsNull() {
        Subscription lSubscription = new Subscription();
        lSubscription.setClientId(null);
        lSubscription.setType(SubscriptionType.MOBILE);

        DomainException e = assertThrows(DomainException.class,
                () -> subscriptionService.addNewSubscription(lSubscription));

        assertEquals("subscription.clientId.required", e.getErrorCode());
    }

    @Test
    void addNewSubscription_shouldThrowDomainExceptionWhenSubTypeIsNull() {
        Subscription lSubscription = new Subscription();
        lSubscription.setClientId(1L);
        lSubscription.setType(null);

        DomainException e = assertThrows(DomainException.class,
                () -> subscriptionService.addNewSubscription(lSubscription));

        assertEquals("subscription.type.required", e.getErrorCode());
    }

    @Test
    void addNewSubscription_shouldReturnSubscriptionWhenValid() {
        Subscription lSubscription = new Subscription();
        lSubscription.setClientId(1L);
        lSubscription.setType(SubscriptionType.MOBILE);

        Subscription lSavedSub = mock(Subscription.class);
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(lSavedSub);

        Subscription lNewSub = subscriptionService.addNewSubscription(lSubscription);

        assertSame(lSavedSub, lNewSub);
        verify(subscriptionRepository).save(any(Subscription.class));
    }

    @Test
    void addOptionToExistingSubscription_shouldThrowDomainExceptionWhenOptionTypeIsNull() {

        Subscription lSubscription = new Subscription();
        lSubscription.setClientId(1L);
        lSubscription.setType(SubscriptionType.MOBILE);

        DomainException e = assertThrows(DomainException.class,
                () -> subscriptionService.addOptionToExistingSubscription(lSubscription, null));

        assertEquals("option.isNull", e.getErrorCode());
    }

    @Test
    void addOptionToExistingSubscription_shouldReturnSubscriptionWhenOptionTypeIsValid() {
        Subscription lSubscription = new Subscription();
        lSubscription.setClientId(1L);
        lSubscription.setType(SubscriptionType.FIBER);

        Subscription lSavedSub = mock(Subscription.class);
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(lSavedSub);

        Subscription lNewSub = subscriptionService.addOptionToExistingSubscription(lSubscription, OptionType.NETFLIX);

        assertSame(lSavedSub, lNewSub);
        verify(subscriptionRepository).save(any(Subscription.class));
    }

    @Test
    void manageOptions_shouldThrowDomainExceptionWhenOptionTypeIsNull() {
        Subscription lSubscription = new Subscription();
        lSubscription.setClientId(1L);
        lSubscription.setType(SubscriptionType.MOBILE);

        Option lOption = new Option();
        lOption.setOptionType(null);

        lSubscription.setOptionList(new java.util.ArrayList<>());
        lSubscription.getOptionsList().add(lOption);

        DomainException e = assertThrows(DomainException.class,
                () -> subscriptionService.addNewSubscription(lSubscription));

        assertEquals("option.isNull", e.getErrorCode());
    }

    @Test
    void manageOptions_shouldThrowDomainExceptionWhenOptionAlreadyExists() {
        Subscription lSubscription = new Subscription();
        lSubscription.setClientId(1L);
        lSubscription.setType(SubscriptionType.MOBILE);

        Option lOption1 = new Option();
        lOption1.setOptionType(OptionType.ROAMING);

        Option lOption2 = new Option();
        lOption2.setOptionType(OptionType.ROAMING);

        lSubscription.setOptionList(new java.util.ArrayList<>());
        lSubscription.getOptionsList().add(lOption1);
        lSubscription.getOptionsList().add(lOption2);

        DomainException e = assertThrows(DomainException.class,
                () -> subscriptionService.addNewSubscription(lSubscription));

        assertEquals("option.already.exists", e.getErrorCode());
    }

    @Test
    void manageOptions_shouldThrowDomainExceptionWhenRoamingWithoutMobile() {
        Subscription lSubscription = new Subscription();
        lSubscription.setClientId(1L);
        lSubscription.setType(SubscriptionType.FIX);

        Option lOption = new Option();
        lOption.setOptionType(OptionType.ROAMING);

        lSubscription.setOptionList(new java.util.ArrayList<>());
        lSubscription.getOptionsList().add(lOption);

        DomainException e = assertThrows(DomainException.class,
                () -> subscriptionService.addNewSubscription(lSubscription));

        assertEquals("option.roaming.not.allowed", e.getErrorCode());
    }

    @Test
    void manageOptions_shouldThrowDomainExceptionWhenNetflixWithoutFiber() {
        Subscription lSubscription = new Subscription();
        lSubscription.setClientId(1L);
        lSubscription.setType(SubscriptionType.MOBILE);

        Option lOption = new Option();
        lOption.setOptionType(OptionType.NETFLIX);

        lSubscription.setOptionList(new java.util.ArrayList<>());
        lSubscription.getOptionsList().add(lOption);

        DomainException e = assertThrows(DomainException.class,
                () -> subscriptionService.addNewSubscription(lSubscription));

        assertEquals("option.netflix.not.allowed", e.getErrorCode());
    }

    @Test
    void manageOptions_shouldThrowDomainExceptionWhenHDWithoutNetflix() {
        Subscription lSubscription = new Subscription();
        lSubscription.setClientId(1L);
        lSubscription.setType(SubscriptionType.MOBILE);

        Option lOption = new Option();
        lOption.setOptionType(OptionType.ROAMING);

        Option lOption2 = new Option();
        lOption2.setOptionType(OptionType.HD);

        lSubscription.setOptionList(new java.util.ArrayList<>());
        lSubscription.getOptionsList().add(lOption);
        lSubscription.getOptionsList().add(lOption2);

        DomainException e = assertThrows(DomainException.class,
                () -> subscriptionService.addNewSubscription(lSubscription));

        assertEquals("option.hd.requires.netflix", e.getErrorCode());
    }

    @Test
    void manageOptions_shouldThrowDomainExceptionWhenMusicWithFix() {
        Subscription lSubscription = new Subscription();
        lSubscription.setClientId(1L);
        lSubscription.setType(SubscriptionType.FIX);

        Option lOption = new Option();
        lOption.setOptionType(OptionType.MUSIC);

        lSubscription.setOptionList(new java.util.ArrayList<>());
        lSubscription.getOptionsList().add(lOption);

        DomainException e = assertThrows(DomainException.class,
                () -> subscriptionService.addNewSubscription(lSubscription));

        assertEquals("option.music.not.allowed", e.getErrorCode());
    }

    @Test
    void manageOptions_shouldSaveWhenRoamingAndMusicWithMobile() {
        Subscription lSubscription = new Subscription();
        lSubscription.setClientId(1L);
        lSubscription.setType(SubscriptionType.MOBILE);

        Option lOption = new Option();
        lOption.setOptionType(OptionType.MUSIC);

        Option lOption2 = new Option();
        lOption2.setOptionType(OptionType.ROAMING);

        lSubscription.setOptionList(new java.util.ArrayList<>());
        lSubscription.getOptionsList().add(lOption);
        lSubscription.getOptionsList().add(lOption2);

        Subscription lSavedSub = mock(Subscription.class);
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(lSavedSub);

        Subscription lNewSub = subscriptionService.addNewSubscription(lSubscription);

        assertSame(lSavedSub, lNewSub);
        verify(subscriptionRepository).save(any(Subscription.class));
    }

    @Test
    void manageOptions_shouldSaveWhenNetflixAndMusicAndHDWithFiber() {
        Subscription lSubscription = new Subscription();
        lSubscription.setClientId(1L);
        lSubscription.setType(SubscriptionType.FIBER);

        Option lOption = new Option();
        lOption.setOptionType(OptionType.NETFLIX);

        Option lOption2 = new Option();
        lOption2.setOptionType(OptionType.MUSIC);

        Option lOption3 = new Option();
        lOption3.setOptionType(OptionType.HD);

        lSubscription.setOptionList(new java.util.ArrayList<>());
        lSubscription.getOptionsList().add(lOption);
        lSubscription.getOptionsList().add(lOption2);
        lSubscription.getOptionsList().add(lOption3);

        Subscription lSavedSub = mock(Subscription.class);
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(lSavedSub);

        Subscription lNewSub = subscriptionService.addNewSubscription(lSubscription);

        assertSame(lSavedSub, lNewSub);
        verify(subscriptionRepository).save(any(Subscription.class));
    }
}
