package com.example.supplychain.dao;

import com.example.supplychain.enums.OrderStatus;
import com.example.supplychain.model.CustomerOrder;
import java.util.List;
import java.util.Optional;

public interface OrderDAO {
    CustomerOrder save(CustomerOrder order);
    Optional<CustomerOrder> findById(Long id);
    List<CustomerOrder> findAll();
    long count();
    long countByStatus(OrderStatus status);
    List<CustomerOrder> findRecentOrders();
}
