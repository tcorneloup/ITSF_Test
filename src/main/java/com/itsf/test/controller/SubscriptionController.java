package com.itsf.test.controller;

import com.itsf.test.dto.AddOptionRequest;
import com.itsf.test.dto.CreateSubscriptionRequest;
import com.itsf.test.dto.SubscriptionDto;
import com.itsf.test.service.SubscriptionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionDto>> getAllSubscriptions() {
        return ResponseEntity.ok(subscriptionService.getAllSubscriptions());
    }

    @PostMapping
    public ResponseEntity<SubscriptionDto> createSubscription(
            @Valid @RequestBody CreateSubscriptionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subscriptionService.createSubscription(request));
    }

    @PostMapping("/{subscriptionId}/options")
    public ResponseEntity<SubscriptionDto> addOption(
            @PathVariable Long subscriptionId,
            @Valid @RequestBody AddOptionRequest request) {
        return ResponseEntity.ok(subscriptionService.addOption(subscriptionId, request));
    }
}
