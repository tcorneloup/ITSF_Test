package Recruitement.ITSF.Repository;

import Recruitement.ITSF.Entity.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {

    @Query("select distinct s from SubscriptionEntity s left join fetch s.optionsList")
    List<SubscriptionEntity> findAllSubscriptions();

}
