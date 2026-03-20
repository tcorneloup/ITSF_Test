package com.itsf.test.dto;

import com.itsf.test.enums.SubscriptionType;
import java.time.LocalDate;
import java.util.List;

public class SubscriptionDto {

    private Long id;
    private SubscriptionType type;
    private LocalDate subscriptionDate;
    private Long clientId;
    private List<SubscriptionOptionDto> options;

    public SubscriptionDto() {
    }

    public SubscriptionDto(Long id, SubscriptionType type, LocalDate subscriptionDate, Long clientId,
                           List<SubscriptionOptionDto> options) {
        this.id = id;
        this.type = type;
        this.subscriptionDate = subscriptionDate;
        this.clientId = clientId;
        this.options = options;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public List<SubscriptionOptionDto> getOptions() {
        return options;
    }

    public void setOptions(List<SubscriptionOptionDto> options) {
        this.options = options;
    }
}
