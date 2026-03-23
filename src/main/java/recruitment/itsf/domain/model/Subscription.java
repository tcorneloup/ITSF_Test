package recruitment.itsf.domain.model;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Subscription {

    private Long id;
    
    @NotNull
    private Long clientId;

    private LocalDateTime subscriptionDateStart;
    
    @NotNull
    private SubscriptionType type;

    private List<Option> optionsList = new ArrayList<>();

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setSubscriptionDateStart(LocalDateTime subscriptionDateStart) {
        this.subscriptionDateStart = subscriptionDateStart;
    }

    public LocalDateTime getSubscriptionDateStart() {
        return subscriptionDateStart;
    }

    public void setType(SubscriptionType type) {
        this.type = type;
    }

    public SubscriptionType getType() {
        return type;
    }
    public void setOptionList(List<Option> optionList) {
        this.optionsList = optionList;
    }

    public List<Option> getOptionsList() {
        return optionsList;
    }
}
