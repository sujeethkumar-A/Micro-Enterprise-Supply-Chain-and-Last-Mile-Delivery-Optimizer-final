package com.example.supplychain.controller;

import com.example.supplychain.dto.RouteOptimizationRequest;
import com.example.supplychain.dto.RouteOptimizationResponse;
import com.example.supplychain.enums.DeliveryStatus;
import com.example.supplychain.model.Delivery;
import com.example.supplychain.service.DeliveryService;
import com.example.supplychain.service.RouteOptimizationService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class DeliveryController {

    private final DeliveryService deliveryService;
    private final RouteOptimizationService routeOptimizationService;

    public DeliveryController(DeliveryService deliveryService, RouteOptimizationService routeOptimizationService) {
        this.deliveryService = deliveryService;
        this.routeOptimizationService = routeOptimizationService;
    }

    @GetMapping("/deliveries")
    public String listDeliveries(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 @RequestParam(required = false) DeliveryStatus status,
                                 Model model) {
        Page<Delivery> deliveryPage = deliveryService.getDeliveriesPaged(page, size, status);
        model.addAttribute("deliveryPage", deliveryPage);
        model.addAttribute("deliveries", deliveryPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", deliveryPage.getTotalPages());
        model.addAttribute("totalItems", deliveryPage.getTotalElements());
        model.addAttribute("selectedStatus", status);
        model.addAttribute("statuses", DeliveryStatus.values());
        return "deliveries";
    }

    @GetMapping("/deliveries/{id}")
    public String viewDeliveryDetails(@PathVariable Long id, Model model) {
        Delivery delivery = deliveryService.getDeliveryById(id);
        model.addAttribute("delivery", delivery);
        model.addAttribute("statuses", DeliveryStatus.values());
        return "delivery-details";
    }

    @PostMapping("/deliveries/{id}/status")
    public String updateDeliveryStatus(@PathVariable Long id,
                                       @RequestParam DeliveryStatus status,
                                       RedirectAttributes redirectAttributes) {
        deliveryService.updateDeliveryStatus(id, status);
        redirectAttributes.addFlashAttribute("successMessage", "Delivery #" + id + " updated to status " + status);
        return "redirect:/deliveries/" + id;
    }

    @GetMapping("/routes")
    public String showRoutePlanner(Model model) {
        List<Delivery> pendingDeliveries = deliveryService.getPendingDeliveries();
        model.addAttribute("pendingDeliveries", pendingDeliveries);
        model.addAttribute("depotName", routeOptimizationService.getDefaultDepotName());
        model.addAttribute("depotLat", routeOptimizationService.getDefaultDepotLat());
        model.addAttribute("depotLon", routeOptimizationService.getDefaultDepotLon());
        model.addAttribute("routeResult", null);
        return "route";
    }

    @PostMapping("/routes/plan")
    public String planRoute(@RequestParam(required = false) List<Long> deliveryIds,
                            @RequestParam(required = false) String depotName,
                            @RequestParam(required = false) Double depotLat,
                            @RequestParam(required = false) Double depotLon,
                            Model model) {
        RouteOptimizationRequest request = new RouteOptimizationRequest(depotName, depotLat, depotLon, deliveryIds);
        RouteOptimizationResponse routeResponse = routeOptimizationService.optimizeRoute(request);

        List<Delivery> pendingDeliveries = deliveryService.getPendingDeliveries();
        model.addAttribute("pendingDeliveries", pendingDeliveries);
        model.addAttribute("selectedDeliveryIds", deliveryIds);
        model.addAttribute("depotName", routeResponse.getDepotName());
        model.addAttribute("depotLat", routeResponse.getDepotLatitude());
        model.addAttribute("depotLon", routeResponse.getDepotLongitude());
        model.addAttribute("routeResult", routeResponse);

        return "route";
    }
}
