package Recruitement.ITSF.Controller;

import Recruitement.ITSF.Bean.Subscription;
import Recruitement.ITSF.Entity.SubscriptionEntity;
import Recruitement.ITSF.Service.Enum.OptionType;
import Recruitement.ITSF.Service.SubscriptionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("api/ITSF/subscription")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping
    public ResponseEntity<List<Subscription>> findAllSubscriptions() {
        List<Subscription> lSubscriptions = subscriptionService.findAllSubscriptionsWithOptions();
        return ResponseEntity.ok(lSubscriptions);
    }

    @PostMapping("/newSubscription")
    public ResponseEntity<Subscription> newSubscription(@Valid @RequestBody Subscription pSubscription) {

        SubscriptionEntity lSubscriptionEntity = pSubscription.toSubscriptionEntity();
        SubscriptionEntity lSavedSubscriptionEntity = subscriptionService.addNewSubscription(lSubscriptionEntity);
        Subscription lSubscription = Subscription.fromSubscription(lSavedSubscriptionEntity);
        return ResponseEntity.ok(lSubscription);
    }

    @PostMapping("/{id}/{option}")
    public ResponseEntity<Subscription> addOptionToExistingSubscription(
            @PathVariable("id") Long pId,
            @PathVariable("option") OptionType pOptionType) {

        SubscriptionEntity lSubscriptionEntity = subscriptionService.getSubscriptionById(pId);
        SubscriptionEntity updatedSubEntity = subscriptionService.addOptionToExistingSubscription(lSubscriptionEntity, pOptionType);

        Subscription lSubscription = Subscription.fromSubscription(updatedSubEntity);
        return ResponseEntity.ok(lSubscription);
    }



}
