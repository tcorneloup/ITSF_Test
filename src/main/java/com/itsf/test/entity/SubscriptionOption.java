package com.itsf.test.entity;

import com.itsf.test.enums.OptionName;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "subscription_option")
public class SubscriptionOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OptionName name;

    @Column(name = "subscription_date", nullable = false)
    private LocalDate subscriptionDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id", nullable = false)
    private Subscription subscription;

    public SubscriptionOption() {
    }

    public SubscriptionOption(OptionName name, LocalDate subscriptionDate) {
        this.name = name;
        this.subscriptionDate = subscriptionDate;
    }

    public Long getId() {
        return id;
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

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }
}
