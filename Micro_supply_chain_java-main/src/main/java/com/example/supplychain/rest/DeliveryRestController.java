package com.example.supplychain.rest;

import com.example.supplychain.dto.DeliveryDTO;
import com.example.supplychain.enums.DeliveryStatus;
import com.example.supplychain.model.Delivery;
import com.example.supplychain.service.DeliveryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/deliveries")
public class DeliveryRestController {

    private final DeliveryService deliveryService;

    public DeliveryRestController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @GetMapping
    public List<DeliveryDTO> getAllDeliveries() {
        return deliveryService.getAllDeliveries().stream()
                .map(deliveryService::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public DeliveryDTO getDeliveryById(@PathVariable Long id) {
        return deliveryService.toDTO(deliveryService.getDeliveryById(id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<DeliveryDTO> updateDeliveryStatus(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String statusStr = payload.get("status");
        if (statusStr == null) {
            return ResponseEntity.badRequest().build();
        }
        DeliveryStatus status = DeliveryStatus.valueOf(statusStr.toUpperCase());
        Delivery updated = deliveryService.updateDeliveryStatus(id, status);
        return ResponseEntity.ok(deliveryService.toDTO(updated));
    }
}
