package recruitment.itsf.infra.entity;

import recruitment.itsf.domain.model.SubscriptionType;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "SUBSCRIPTION")
public class SubscriptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "CLIENT_ID", nullable = false)
    private Long clientId;

    @Column(name = "SUB_DATE_START", nullable = false)
    private LocalDateTime subscriptionDateStart;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE")
    private SubscriptionType type;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "SUBSCRIPTION_ID")
    private List<OptionEntity> optionsList = new ArrayList<>();

    public SubscriptionEntity() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public LocalDateTime getSubscriptionDateStart() {
        return subscriptionDateStart;
    }

    public void setSubscriptionDateStart(LocalDateTime subscriptionDateStart) {
        this.subscriptionDateStart = subscriptionDateStart;
    }

    public SubscriptionType getType() {
        return type;
    }

    public void setType(SubscriptionType type) {
        this.type = type;
    }

    public List<OptionEntity> getOptionsList() {
        return optionsList;
    }

    public void setOptionsList(List<OptionEntity> optionsList) {
        this.optionsList = optionsList;
    }
}
