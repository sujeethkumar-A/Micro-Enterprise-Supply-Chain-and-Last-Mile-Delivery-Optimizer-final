package com.example.supplychain.repository;

import com.example.supplychain.enums.OrderStatus;
import com.example.supplychain.model.CustomerOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<CustomerOrder, Long> {
    Page<CustomerOrder> findByStatus(OrderStatus status, Pageable pageable);
    
    long countByStatus(OrderStatus status);

    List<CustomerOrder> findTop5ByOrderByOrderDateDesc();
    
    List<CustomerOrder> findByCustomerId(Long customerId);
}
