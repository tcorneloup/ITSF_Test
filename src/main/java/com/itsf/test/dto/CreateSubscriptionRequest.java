package com.itsf.test.dto;

import com.itsf.test.enums.SubscriptionType;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class CreateSubscriptionRequest {

    @NotNull(message = "Subscription type is required")
    private SubscriptionType type;

    @NotNull(message = "Subscription date is required")
    private LocalDate subscriptionDate;

    @NotNull(message = "Client id is required")
    private Long clientId;

    public SubscriptionType getType() {
        return type;
    }

    public void setType(SubscriptionType type) {
        this.type = type;
    }

    public LocalDate getSubscriptionDate() {
        return subscriptionDate;
    }

    public void setSubscriptionDate(LocalDate subscriptionDate) {
        this.subscriptionDate = subscriptionDate;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
}
