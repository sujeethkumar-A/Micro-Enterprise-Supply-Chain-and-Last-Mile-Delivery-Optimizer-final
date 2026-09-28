package com.example.supplychain.repository;

import com.example.supplychain.enums.DeliveryStatus;
import com.example.supplychain.model.Delivery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    List<Delivery> findByStatus(DeliveryStatus status);
    
    Page<Delivery> findByStatus(DeliveryStatus status, Pageable pageable);

    long countByStatus(DeliveryStatus status);

    Optional<Delivery> findByOrderId(Long orderId);

    List<Delivery> findTop5ByOrderByDeliveryDateDesc();
}
