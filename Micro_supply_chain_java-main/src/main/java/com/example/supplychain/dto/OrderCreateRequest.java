package com.example.supplychain.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class OrderCreateRequest {

    @NotNull(message = "Customer is required")
    private Long customerId;

    @NotEmpty(message = "Order must contain at least one item")
    private List<OrderItemRequest> items = new ArrayList<>();

    public OrderCreateRequest() {
    }

    public OrderCreateRequest(Long customerId, List<OrderItemRequest> items) {
        this.customerId = customerId;
        this.items = items;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public List<OrderItemRequest> getItems() {
        return items;
    }

    public void setItems(List<OrderItemRequest> items) {
        this.items = items;
    }
}
