package com.itsf.test.dto;

import com.itsf.test.enums.OptionName;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class AddOptionRequest {

    @NotNull(message = "Option name is required")
    private OptionName name;

    @NotNull(message = "Subscription date is required")
    private LocalDate subscriptionDate;

    public OptionName getName() {
        return name;
    }

    public void setName(OptionName name) {
        this.name = name;
    }

    public LocalDate getSubscriptionDate() {
        return subscriptionDate;
    }

    public void setSubscriptionDate(LocalDate subscriptionDate) {
        this.subscriptionDate = subscriptionDate;
    }
}
