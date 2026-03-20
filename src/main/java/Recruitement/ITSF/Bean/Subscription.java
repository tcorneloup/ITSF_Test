package Recruitement.ITSF.Bean;

import Recruitement.ITSF.Entity.OptionEntity;
import Recruitement.ITSF.Entity.SubscriptionEntity;
import Recruitement.ITSF.Service.Enum.SubscriptionType;
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

    public void setOptionsList(List<Option> optionList) {
        this.optionsList = optionList;
    }

    public List<Option> getOptionsList() {
        return optionsList;
    }


    public static Subscription fromSubscription (SubscriptionEntity pSubscriptionEntity) {

        if ( pSubscriptionEntity == null) {
            return null;
        }
        Subscription lSubscription = new Subscription();

        lSubscription.setId(pSubscriptionEntity.getId());
        lSubscription.setClientId(pSubscriptionEntity.getClientId());
        lSubscription.setSubscriptionDateStart(pSubscriptionEntity.getSubscriptionDateStart());
        lSubscription.setType(pSubscriptionEntity.getType());
        lSubscription.setOptionsList(pSubscriptionEntity.getOptionsList().stream().map(Option::fromOption).toList());

        return lSubscription;
    }

    public SubscriptionEntity toSubscriptionEntity() {
        SubscriptionEntity lSubscriptionEntity = new SubscriptionEntity();
        lSubscriptionEntity.setId(id);
        lSubscriptionEntity.setClientId(clientId);
        lSubscriptionEntity.setSubscriptionDateStart(subscriptionDateStart);
        lSubscriptionEntity.setType(type);
        
        // Convert options beans to entities
        if (optionsList != null && !optionsList.isEmpty()) {
            List<OptionEntity> options = optionsList.stream()
                    .map(Option::toOptionEntity)
                    .toList();
            lSubscriptionEntity.setOptionsList(new ArrayList<>(options));
        }

        return lSubscriptionEntity;
    }
}
