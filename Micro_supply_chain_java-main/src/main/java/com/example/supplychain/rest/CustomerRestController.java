package com.example.supplychain.rest;

import com.example.supplychain.dto.CustomerDTO;
import com.example.supplychain.model.Customer;
import com.example.supplychain.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customers")
public class CustomerRestController {

    private final CustomerService customerService;

    public CustomerRestController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<CustomerDTO> getAllCustomers() {
        return customerService.getAllCustomers().stream()
                .map(customerService::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public CustomerDTO getCustomerById(@PathVariable Long id) {
        return customerService.toDTO(customerService.getCustomerById(id));
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        Customer customer = new Customer(
                customerDTO.getName(),
                customerDTO.getPhone(),
                customerDTO.getEmail(),
                customerDTO.getAddress(),
                customerDTO.getCity(),
                customerDTO.getLatitude(),
                customerDTO.getLongitude()
        );
        Customer saved = customerService.createCustomer(customer);
        return new ResponseEntity<>(customerService.toDTO(saved), HttpStatus.CREATED);
    }
}
