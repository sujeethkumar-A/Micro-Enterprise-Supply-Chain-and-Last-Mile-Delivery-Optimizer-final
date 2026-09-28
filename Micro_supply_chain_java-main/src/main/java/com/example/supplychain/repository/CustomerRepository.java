package com.example.supplychain.repository;

import com.example.supplychain.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Page<Customer> findByNameContainingIgnoreCase(String name, Pageable pageable);
    
    Page<Customer> findByCityContainingIgnoreCase(String city, Pageable pageable);

    List<Customer> findByNameContainingIgnoreCase(String name);
}
