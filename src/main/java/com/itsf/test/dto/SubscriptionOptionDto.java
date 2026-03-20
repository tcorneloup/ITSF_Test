package com.itsf.test.dto;

import com.itsf.test.enums.OptionName;
import java.time.LocalDate;

public class SubscriptionOptionDto {

    private Long id;
    private OptionName name;
    private LocalDate subscriptionDate;

    public SubscriptionOptionDto() {
    }

    public SubscriptionOptionDto(Long id, OptionName name, LocalDate subscriptionDate) {
        this.id = id;
        this.name = name;
        this.subscriptionDate = subscriptionDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
