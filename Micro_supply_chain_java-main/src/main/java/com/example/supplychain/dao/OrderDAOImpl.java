package com.example.supplychain.dao;

import com.example.supplychain.enums.OrderStatus;
import com.example.supplychain.model.CustomerOrder;
import com.example.supplychain.repository.OrderRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class OrderDAOImpl implements OrderDAO {

    private final OrderRepository orderRepository;

    public OrderDAOImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public CustomerOrder save(CustomerOrder order) {
        return orderRepository.save(order);
    }

    @Override
    public Optional<CustomerOrder> findById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public List<CustomerOrder> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public long count() {
        return orderRepository.count();
    }

    @Override
    public long countByStatus(OrderStatus status) {
        return orderRepository.countByStatus(status);
    }

    @Override
    public List<CustomerOrder> findRecentOrders() {
        return orderRepository.findTop5ByOrderByOrderDateDesc();
    }
}
