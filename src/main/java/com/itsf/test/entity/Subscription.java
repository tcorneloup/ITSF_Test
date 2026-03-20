package com.itsf.test.entity;

import com.itsf.test.enums.SubscriptionType;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "subscription")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionType type;

    @Column(name = "subscription_date", nullable = false)
    private LocalDate subscriptionDate;

    @Column(name = "client_id", nullable = false)
    private Long clientId;

    @OneToMany(mappedBy = "subscription", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SubscriptionOption> options = new ArrayList<>();

    public Subscription() {
    }

    public Subscription(SubscriptionType type, LocalDate subscriptionDate, Long clientId) {
        this.type = type;
        this.subscriptionDate = subscriptionDate;
        this.clientId = clientId;
    }

    public Long getId() {
        return id;
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

    public List<SubscriptionOption> getOptions() {
        return options;
    }

    public void addOption(SubscriptionOption option) {
        options.add(option);
        option.setSubscription(this);
    }
}
