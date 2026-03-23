package recruitment.itsf.web.controller;

import recruitment.itsf.domain.model.OptionType;
import recruitment.itsf.domain.model.Subscription;
import recruitment.itsf.domain.service.SubscriptionService;
import recruitment.itsf.web.dto.SubscriptionRequest;
import recruitment.itsf.web.dto.SubscriptionResponse;
import recruitment.itsf.web.mapper.SubscriptionDtoMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/itsf/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionResponse>> findAllSubscriptions() {
        List<Subscription> subscriptions = subscriptionService.findAllSubscriptionsWithOptions();

        List<SubscriptionResponse> subscriptionsResponse = subscriptions.stream()
                .map(SubscriptionDtoMapper::subscriptionDomainToResponse)
                .toList();

        return ResponseEntity.ok(subscriptionsResponse);
    }

    @PostMapping()
    public ResponseEntity<SubscriptionResponse> newSubscription(@Valid @RequestBody SubscriptionRequest request) {
        Subscription subscriptionToSave = SubscriptionDtoMapper.subscriptionRequestToDomain(request);

        Subscription savedSubscription = subscriptionService.addNewSubscription(subscriptionToSave);

        SubscriptionResponse response = SubscriptionDtoMapper.subscriptionDomainToResponse(savedSubscription);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/options/{option}")
    public ResponseEntity<SubscriptionResponse> addOptionToExistingSubscription(
            @PathVariable("id") Long id,
            @PathVariable("option") OptionType optionType) {

        Subscription subscriptionToUpdate = subscriptionService.getSubscriptionById(id);

        Subscription updatedSubscription = subscriptionService.addOptionToExistingSubscription(subscriptionToUpdate, optionType);

        SubscriptionResponse response = SubscriptionDtoMapper.subscriptionDomainToResponse(updatedSubscription);
        return ResponseEntity.ok(response);
    }
}
