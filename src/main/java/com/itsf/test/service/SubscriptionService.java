package com.itsf.test.service;

import com.itsf.test.dto.AddOptionRequest;
import com.itsf.test.dto.CreateSubscriptionRequest;
import com.itsf.test.dto.SubscriptionDto;
import com.itsf.test.dto.SubscriptionOptionDto;
import com.itsf.test.entity.Subscription;
import com.itsf.test.entity.SubscriptionOption;
import com.itsf.test.enums.OptionName;
import com.itsf.test.enums.SubscriptionType;
import com.itsf.test.exception.BusinessException;
import com.itsf.test.exception.ResourceNotFoundException;
import com.itsf.test.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Transactional(readOnly = true)
    public List<SubscriptionDto> getAllSubscriptions() {
        return subscriptionRepository.findAllWithOptions().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public SubscriptionDto createSubscription(CreateSubscriptionRequest request) {
        Subscription subscription = new Subscription(
                request.getType(),
                request.getSubscriptionDate(),
                request.getClientId()
        );
        return toDto(subscriptionRepository.save(subscription));
    }

    public SubscriptionDto addOption(Long subscriptionId, AddOptionRequest request) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Subscription not found with id: " + subscriptionId));

        validateOption(subscription, request.getName());

        SubscriptionOption option = new SubscriptionOption(request.getName(), request.getSubscriptionDate());
        subscription.addOption(option);

        return toDto(subscriptionRepository.save(subscription));
    }

    private void validateOption(Subscription subscription, OptionName optionName) {
        SubscriptionType type = subscription.getType();
        List<OptionName> existingOptions = subscription.getOptions().stream()
                .map(SubscriptionOption::getName)
                .collect(Collectors.toList());

        // A subscription cannot have the same option twice
        if (existingOptions.contains(optionName)) {
            throw new BusinessException("Subscription already has option: " + optionName);
        }

        switch (optionName) {
            case ROAMING:
                // ROAMING can only be added to a MOBILE subscription
                if (type != SubscriptionType.MOBILE) {
                    throw new BusinessException("ROAMING option can only be added to a MOBILE subscription");
                }
                break;
            case NETFLIX:
                // NETFLIX can only be added to a FIBER subscription
                if (type != SubscriptionType.FIBER) {
                    throw new BusinessException("NETFLIX option can only be added to a FIBER subscription");
                }
                break;
            case HD:
                // HD option can only be added if a NETFLIX option already exists
                if (!existingOptions.contains(OptionName.NETFLIX)) {
                    throw new BusinessException("HD option can only be added if a NETFLIX option already exists");
                }
                break;
            case MUSIC:
                // MUSIC can be added to MOBILE or FIBER subscriptions
                if (type != SubscriptionType.MOBILE && type != SubscriptionType.FIBER) {
                    throw new BusinessException("MUSIC option can only be added to a MOBILE or FIBER subscription");
                }
                break;
            default:
                throw new BusinessException("Unknown option: " + optionName);
        }
    }

    private SubscriptionDto toDto(Subscription subscription) {
        List<SubscriptionOptionDto> optionDtos = subscription.getOptions().stream()
                .map(o -> new SubscriptionOptionDto(o.getId(), o.getName(), o.getSubscriptionDate()))
                .collect(Collectors.toList());
        return new SubscriptionDto(
                subscription.getId(),
                subscription.getType(),
                subscription.getSubscriptionDate(),
                subscription.getClientId(),
                optionDtos
        );
    }
}
