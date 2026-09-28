package com.example.supplychain.service;

import com.example.supplychain.dto.CustomerDTO;
import com.example.supplychain.exception.ResourceNotFoundException;
import com.example.supplychain.model.Customer;
import com.example.supplychain.repository.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Long id, Customer updatedCustomer) {
        Customer existing = getCustomerById(id);
        existing.setName(updatedCustomer.getName());
        existing.setPhone(updatedCustomer.getPhone());
        existing.setEmail(updatedCustomer.getEmail());
        existing.setAddress(updatedCustomer.getAddress());
        existing.setCity(updatedCustomer.getCity());
        existing.setLatitude(updatedCustomer.getLatitude());
        existing.setLongitude(updatedCustomer.getLongitude());
        return customerRepository.save(existing);
    }

    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer not found with ID: " + id);
        }
        customerRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @Transactional(readOnly = true)
    public Page<Customer> getCustomersPaged(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "name"));
        if (search != null && !search.trim().isEmpty()) {
            return customerRepository.findByNameContainingIgnoreCase(search.trim(), pageable);
        }
        return customerRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public long countCustomers() {
        return customerRepository.count();
    }

    public CustomerDTO toDTO(Customer customer) {
        return new CustomerDTO(
                customer.getId(),
                customer.getName(),
                customer.getPhone(),
                customer.getEmail(),
                customer.getAddress(),
                customer.getCity(),
                customer.getLatitude(),
                customer.getLongitude()
        );
    }
}
