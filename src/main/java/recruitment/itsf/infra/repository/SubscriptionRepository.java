package recruitment.itsf.infra.repository;

import recruitment.itsf.infra.entity.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {

    @Query("select distinct s from SubscriptionEntity s left join fetch s.optionsList")
    List<SubscriptionEntity> findAllSubscriptions();

}
