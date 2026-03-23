package recruitment.itsf.domain.service;

import recruitment.itsf.domain.exception.DomainException;
import recruitment.itsf.domain.repository.SubscriptionRepository;
import recruitment.itsf.domain.model.Option;
import recruitment.itsf.domain.model.OptionType;
import recruitment.itsf.domain.model.Subscription;
import recruitment.itsf.domain.model.SubscriptionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SubscriptionService {
    
    private static final Logger log = LoggerFactory.getLogger(SubscriptionService.class);

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Transactional
    public Subscription addNewSubscription(Subscription newSubscription) {

        if (newSubscription.getClientId() == null) {
            log.warn(ErrorCodes.SUBSCRIPTION_CLIENT_ID_REQUIRED);
            throw new DomainException(ErrorCodes.SUBSCRIPTION_CLIENT_ID_REQUIRED);
        }

        if (newSubscription.getType() == null) {
            log.warn(ErrorCodes.SUBSCRIPTION_TYPE_REQUIRED);
            throw new DomainException(ErrorCodes.SUBSCRIPTION_TYPE_REQUIRED);
        }
        newSubscription.setSubscriptionDateStart(LocalDateTime.now());
        
        List<Option> lOptionsList = newSubscription.getOptionsList();
        for (Option option : lOptionsList) {
            manageOptions(newSubscription, option);
        }

        return subscriptionRepository.save(newSubscription);
    }

    @Transactional
    public Subscription addOptionToExistingSubscription(Subscription subscriptionToAddNewOption, OptionType optionTypeToAdd) {

        Option newOption = new Option();
        newOption.setOptionType(optionTypeToAdd);
        newOption.setOptionSubDateStart(LocalDateTime.now());
        
        manageOptions(subscriptionToAddNewOption, newOption);

        subscriptionToAddNewOption.getOptionsList().add(newOption);
        
        return subscriptionRepository.save(subscriptionToAddNewOption);
    }
    
    @Transactional(readOnly = true)
    public Subscription getSubscriptionById(Long pId) {
        return subscriptionRepository.findById(pId)
                .orElseThrow(() -> new DomainException(ErrorCodes.SUBSCRIPTION_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<Subscription> findAllSubscriptionsWithOptions() {
        return subscriptionRepository.findAllSubscriptionsWithOptions();
    }
    
    private void manageOptions(Subscription subscriptionToManage, Option optionToManage) {
        OptionType lOptionType = optionToManage.getOptionType();

        if (optionToManage.getOptionSubDateStart() == null) {
            optionToManage.setOptionSubDateStart(LocalDateTime.now());
        }

        if (lOptionType == null) {
            log.warn(ErrorCodes.OPTION_TYPE_NULL);
            throw new DomainException(ErrorCodes.OPTION_TYPE_NULL);
        }

        boolean optionAlreadyExistsInThisSubscription = subscriptionToManage.getOptionsList().stream()
                .filter(option -> option != optionToManage)
                .anyMatch(option -> option.getOptionType() == lOptionType);

        if (optionAlreadyExistsInThisSubscription) {
            log.warn("Option {} is already added for this subscription {}", lOptionType, subscriptionToManage.getId());
            throw new DomainException(ErrorCodes.OPTION_ALREADY_EXISTS);
        }

        checkOptionToAddInTheSubscription(subscriptionToManage, lOptionType);
    }

    private void checkOptionToAddInTheSubscription(Subscription subscriptionToManage, OptionType lOptionType) {
        switch (lOptionType) {
            case ROAMING -> {
                if (subscriptionToManage.getType() != (SubscriptionType.MOBILE)) {
                    log.warn(ErrorCodes.OPTION_ROAMING_NOT_ALLOWED);
                    throw new DomainException(ErrorCodes.OPTION_ROAMING_NOT_ALLOWED);
                }
            }
            case NETFLIX -> {
                if (subscriptionToManage.getType() != (SubscriptionType.FIBER)) {
                    log.warn(ErrorCodes.OPTION_NETFLIX_NOT_ALLOWED);
                    throw new DomainException(ErrorCodes.OPTION_NETFLIX_NOT_ALLOWED);
                }
            }
            case HD -> {
                boolean isNetflixAdded = subscriptionToManage.getOptionsList().stream()
                        .anyMatch(option -> option.getOptionType() == OptionType.NETFLIX);

                if (!isNetflixAdded) {
                    log.warn(ErrorCodes.OPTION_HD_REQUIRES_NETFLIX);
                    throw new DomainException(ErrorCodes.OPTION_HD_REQUIRES_NETFLIX);
                }
            }
            case MUSIC -> {
                if (subscriptionToManage.getType() == SubscriptionType.FIX) {
                    log.warn(ErrorCodes.OPTION_MUSIC_NOT_ALLOWED);
                    throw new DomainException(ErrorCodes.OPTION_MUSIC_NOT_ALLOWED);
                }
            }
        }
    }

}