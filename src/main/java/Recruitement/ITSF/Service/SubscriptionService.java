package Recruitement.ITSF.Service;

import Recruitement.ITSF.Service.Enum.OptionType;
import Recruitement.ITSF.Service.Enum.SubscriptionType;
import Recruitement.ITSF.Entity.OptionEntity;
import Recruitement.ITSF.Entity.SubscriptionEntity;
import Recruitement.ITSF.Exception.CustomClassException;
import Recruitement.ITSF.Repository.SubscriptionRepository;
import Recruitement.ITSF.Bean.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SubscriptionService {
    
    private static final Logger log = LoggerFactory.getLogger(SubscriptionService.class);
    private static final String OPTION_ALREADY_EXISTS = "option.already.exists";
    private static final String OPTION_HD_REQUIRES_NETFLIX = "option.hd.requires.netflix";
    private static final String OPTION_MUSIC_NOT_ALLOWED = "option.music.not.allowed";
    private static final String OPTION_NETFLIX_NOT_ALLOWED = "option.netflix.not.allowed";
    private static final String OPTION_ROAMING_NOT_ALLOWED = "option.roaming.not.allowed";
    private static final String OPTION_TYPE_NULL = "option.isNull";
    private static final String SUBSCRIPTION_CLIENT_ID_REQUIRED = "subscription.clientId.required";
    private static final String SUBSCRIPTION_NOT_FOUND = "subscription.isNull";
    private static final String SUBSCRIPTION_TYPE_REQUIRED = "subscription.type.required";

    private final SubscriptionRepository subRepository;

    public SubscriptionService(SubscriptionRepository pSubRepository) {
        this.subRepository = pSubRepository;
    }
    
    @Transactional
    public SubscriptionEntity addNewSubscription(SubscriptionEntity pSubscriptionEntity) {

        if (pSubscriptionEntity.getClientId() == null) {
            log.warn(SUBSCRIPTION_CLIENT_ID_REQUIRED);
            throw new CustomClassException(SUBSCRIPTION_CLIENT_ID_REQUIRED);
        }

        if (pSubscriptionEntity.getType() == null) {
            log.warn(SUBSCRIPTION_TYPE_REQUIRED);
            throw new CustomClassException(SUBSCRIPTION_TYPE_REQUIRED);
        }
        pSubscriptionEntity.setSubscriptionDateStart(LocalDateTime.now());
        
        List<OptionEntity> lOptionsList = pSubscriptionEntity.getOptionsList();
        for (OptionEntity option : lOptionsList) {
            manageOptions(pSubscriptionEntity, option);
        }

        return subRepository.save(pSubscriptionEntity);
    }

    @Transactional
    public SubscriptionEntity addOptionToExistingSubscription(SubscriptionEntity pSubscriptionEntity, OptionType pOptionType) {

        OptionEntity newOption = new OptionEntity();
        newOption.setName(pOptionType);
        newOption.setOptionSubDateStart(LocalDateTime.now());
        
        manageOptions(pSubscriptionEntity, newOption);
        
        pSubscriptionEntity.getOptionsList().add(newOption);
        
        return subRepository.save(pSubscriptionEntity);
    }
    
    @Transactional(readOnly = true)
    public SubscriptionEntity getSubscriptionById(Long pId) {
        return subRepository.findById(pId)
                .orElseThrow(() -> new CustomClassException(SUBSCRIPTION_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<Subscription> findAllSubscriptionsWithOptions() {
        List<SubscriptionEntity> subscriptions = subRepository.findAllSubscriptions();
        return subscriptions.stream().map(Subscription::fromSubscription).toList();
    }
    
    private void manageOptions(SubscriptionEntity pSubscriptionEntity, OptionEntity pOptionEntity) {
        OptionType lOptionType = pOptionEntity.getName();

        if (pOptionEntity.getOptionSubDateStart() == null) {
            pOptionEntity.setOptionSubDateStart(LocalDateTime.now());
        }

        if (lOptionType == null) {
            log.warn(OPTION_TYPE_NULL);
            throw new CustomClassException(OPTION_TYPE_NULL);
        }

        boolean optionAlreadyExistsInThisSubscription = pSubscriptionEntity.getOptionsList().stream()
                .filter(option -> option != pOptionEntity)
                .anyMatch(option -> option.getName() == lOptionType);

        if (optionAlreadyExistsInThisSubscription) {
            log.warn("Option {} is already added for this subscription {}", lOptionType, pSubscriptionEntity.getId());
            throw new CustomClassException(OPTION_ALREADY_EXISTS);
        }

        switch (lOptionType) {
            case ROAMING -> {
                if (pSubscriptionEntity.getType() != (SubscriptionType.MOBILE)) {
                    log.warn(OPTION_ROAMING_NOT_ALLOWED);
                    throw new CustomClassException(OPTION_ROAMING_NOT_ALLOWED);
                }
            }
            case NETFLIX -> {
                if (pSubscriptionEntity.getType() != (SubscriptionType.FIBER)) {
                    log.warn(OPTION_NETFLIX_NOT_ALLOWED);
                    throw new CustomClassException(OPTION_NETFLIX_NOT_ALLOWED);
                }
            }
            case HD -> {
                boolean isNetflixAdded = pSubscriptionEntity.getOptionsList().stream()
                        .anyMatch(option -> option.getName() == OptionType.NETFLIX);

                if (!isNetflixAdded) {
                    log.warn(OPTION_HD_REQUIRES_NETFLIX);
                    throw new CustomClassException(OPTION_HD_REQUIRES_NETFLIX);
                }
            }
            case MUSIC -> {
                if (pSubscriptionEntity.getType() == SubscriptionType.FIX) {
                    log.warn(OPTION_MUSIC_NOT_ALLOWED);
                    throw new CustomClassException(OPTION_MUSIC_NOT_ALLOWED);
                }
            }
        }
    }

}