package com.itsf.test.repository;

import com.itsf.test.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    @Query("SELECT DISTINCT s FROM Subscription s LEFT JOIN FETCH s.options")
    List<Subscription> findAllWithOptions();
}
