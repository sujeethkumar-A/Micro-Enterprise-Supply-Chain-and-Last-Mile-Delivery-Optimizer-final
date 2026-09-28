package com.example.supplychain.rest;

import com.example.supplychain.dto.OrderCreateRequest;
import com.example.supplychain.dto.OrderDTO;
import com.example.supplychain.model.CustomerOrder;
import com.example.supplychain.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderRestController {

    private final OrderService orderService;

    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<OrderDTO> getAllOrders() {
        return orderService.getAllOrders().stream()
                .map(orderService::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public OrderDTO getOrderById(@PathVariable Long id) {
        return orderService.toDTO(orderService.getOrderById(id));
    }

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody OrderCreateRequest request) {
        CustomerOrder created = orderService.createOrder(request);
        return new ResponseEntity<>(orderService.toDTO(created), HttpStatus.CREATED);
    }
}
