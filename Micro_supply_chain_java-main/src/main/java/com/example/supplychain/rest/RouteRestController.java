package com.example.supplychain.rest;

import com.example.supplychain.dto.RouteOptimizationRequest;
import com.example.supplychain.dto.RouteOptimizationResponse;
import com.example.supplychain.service.RouteOptimizationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/routes")
public class RouteRestController {

    private final RouteOptimizationService routeOptimizationService;

    public RouteRestController(RouteOptimizationService routeOptimizationService) {
        this.routeOptimizationService = routeOptimizationService;
    }

    @PostMapping("/optimize")
    public RouteOptimizationResponse optimizeRoute(@RequestBody(required = false) RouteOptimizationRequest request) {
        return routeOptimizationService.optimizeRoute(request != null ? request : new RouteOptimizationRequest());
    }
}
