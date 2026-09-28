package com.example.supplychain.service;

import com.example.supplychain.dto.RouteOptimizationRequest;
import com.example.supplychain.dto.RouteOptimizationResponse;
import com.example.supplychain.dto.RouteStopDTO;
import com.example.supplychain.enums.DeliveryStatus;
import com.example.supplychain.model.Customer;
import com.example.supplychain.model.Delivery;
import com.example.supplychain.repository.DeliveryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RouteOptimizationService {

    private static final double EARTH_RADIUS_KM = 6371.0;

    @Value("${app.delivery.base-cost:30.0}")
    private double baseDeliveryCost;

    @Value("${app.delivery.cost-per-km:8.0}")
    private double costPerKm;

    @Value("${app.delivery.speed-kmh:30.0}")
    private double averageSpeedKmh;

    @Value("${app.warehouse.name:Central Depot & Micro-Enterprise Hub}")
    private String defaultDepotName;

    @Value("${app.warehouse.latitude:12.9716}")
    private double defaultDepotLat;

    @Value("${app.warehouse.longitude:77.5946}")
    private double defaultDepotLon;

    private final DeliveryRepository deliveryRepository;

    public RouteOptimizationService(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    /**
     * Calculates the great-circle distance between two geographic coordinates
     * using the Haversine formula.
     *
     * @param lat1 Latitude of point 1 in degrees
     * @param lon1 Longitude of point 1 in degrees
     * @param lat2 Latitude of point 2 in degrees
     * @param lon2 Longitude of point 2 in degrees
     * @return Distance in kilometers
     */
    public double calculateHaversineDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double originLatRad = Math.toRadians(lat1);
        double targetLatRad = Math.toRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(originLatRad) * Math.cos(targetLatRad);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = EARTH_RADIUS_KM * c;
        return Math.round(distance * 100.0) / 100.0;
    }

    /**
     * Calculates the estimated delivery cost for a given distance.
     * Formula: Base Cost + (Distance * Cost Per Kilometer)
     */
    public double calculateDeliveryCost(double distanceKm) {
        double cost = baseDeliveryCost + (distanceKm * costPerKm);
        return Math.round(cost * 100.0) / 100.0;
    }

    /**
     * Calculates the estimated delivery travel time in minutes.
     * Formula: (Distance / Average Speed) * 60 minutes
     */
    public double calculateDeliveryTimeMinutes(double distanceKm) {
        if (averageSpeedKmh <= 0) {
            return 0.0;
        }
        double timeMinutes = (distanceKm / averageSpeedKmh) * 60.0;
        return Math.round(timeMinutes * 10.0) / 10.0;
    }

    /**
     * Optimizes delivery order using the Nearest-Neighbour Greedy Heuristic:
     * 1. Start at the Enterprise Depot.
     * 2. Find the unvisited pending customer that is closest to current position.
     * 3. Move to that customer and mark as visited.
     * 4. Repeat until all selected deliveries are sequenced.
     *
     * Note: This is a fast, practical heuristic suitable for micro-enterprises.
     * It does not claim mathematical global optimality, but provides efficient
     * real-world last-mile routing.
     */
    public RouteOptimizationResponse optimizeRoute(RouteOptimizationRequest request) {
        String depotName = (request != null && request.getDepotName() != null && !request.getDepotName().isEmpty())
                ? request.getDepotName() : defaultDepotName;
        double currentLat = (request != null && request.getDepotLatitude() != null)
                ? request.getDepotLatitude() : defaultDepotLat;
        double currentLon = (request != null && request.getDepotLongitude() != null)
                ? request.getDepotLongitude() : defaultDepotLon;

        // Retrieve deliveries to optimize
        List<Delivery> candidateDeliveries;
        if (request != null && request.getDeliveryIds() != null && !request.getDeliveryIds().isEmpty()) {
            candidateDeliveries = deliveryRepository.findAllById(request.getDeliveryIds());
        } else {
            candidateDeliveries = deliveryRepository.findByStatus(DeliveryStatus.PENDING);
        }

        RouteOptimizationResponse response = new RouteOptimizationResponse();
        response.setDepotName(depotName);
        response.setDepotLatitude(currentLat);
        response.setDepotLongitude(currentLon);
        response.setAlgorithmExplanation("Nearest-neighbour heuristic for simple last-mile route planning.");

        List<RouteStopDTO> stops = new ArrayList<>();

        // Stop 0: Depot Start Point
        RouteStopDTO depotStop = new RouteStopDTO();
        depotStop.setSequenceNumber(0);
        depotStop.setStopType("DEPOT");
        depotStop.setLocationName(depotName);
        depotStop.setCustomerName("Enterprise Warehouse / Depot");
        depotStop.setAddress("Central Distribution Center");
        depotStop.setLatitude(currentLat);
        depotStop.setLongitude(currentLon);
        depotStop.setLegDistanceKm(0.0);
        depotStop.setCumulativeDistanceKm(0.0);
        depotStop.setLegTimeMinutes(0.0);
        depotStop.setCumulativeTimeMinutes(0.0);
        depotStop.setLegCost(0.0);
        depotStop.setCumulativeCost(0.0);
        stops.add(depotStop);

        if (candidateDeliveries.isEmpty()) {
            response.setTotalStops(0);
            response.setTotalDistanceKm(0.0);
            response.setTotalEstimatedTimeMinutes(0.0);
            response.setTotalEstimatedCost(0.0);
            response.setStops(stops);
            return response;
        }

        List<Delivery> unvisited = new ArrayList<>(candidateDeliveries);
        double cumulativeDist = 0.0;
        double cumulativeTime = 0.0;
        int sequence = 1;

        while (!unvisited.isEmpty()) {
            Delivery nearestDelivery = null;
            double shortestDistance = Double.MAX_VALUE;

            for (Delivery d : unvisited) {
                if (d.getOrder() == null || d.getOrder().getCustomer() == null) {
                    continue;
                }
                Customer cust = d.getOrder().getCustomer();
                double dist = calculateHaversineDistance(currentLat, currentLon, cust.getLatitude(), cust.getLongitude());
                if (dist < shortestDistance) {
                    shortestDistance = dist;
                    nearestDelivery = d;
                }
            }

            if (nearestDelivery == null) {
                break;
            }

            Customer customer = nearestDelivery.getOrder().getCustomer();
            unvisited.remove(nearestDelivery);

            cumulativeDist += shortestDistance;
            cumulativeDist = Math.round(cumulativeDist * 100.0) / 100.0;

            double legTime = calculateDeliveryTimeMinutes(shortestDistance);
            cumulativeTime += legTime;
            cumulativeTime = Math.round(cumulativeTime * 10.0) / 10.0;

            double legCost = Math.round((shortestDistance * costPerKm) * 100.0) / 100.0;
            double cumulativeCost = Math.round((baseDeliveryCost + cumulativeDist * costPerKm) * 100.0) / 100.0;

            RouteStopDTO stop = new RouteStopDTO();
            stop.setSequenceNumber(sequence++);
            stop.setStopType("CUSTOMER");
            stop.setDeliveryId(nearestDelivery.getId());
            stop.setOrderId(nearestDelivery.getOrder().getId());
            stop.setLocationName("Stop #" + stop.getSequenceNumber() + ": " + customer.getName());
            stop.setCustomerName(customer.getName());
            stop.setPhone(customer.getPhone());
            stop.setAddress(customer.getAddress());
            stop.setCity(customer.getCity());
            stop.setLatitude(customer.getLatitude());
            stop.setLongitude(customer.getLongitude());
            stop.setLegDistanceKm(shortestDistance);
            stop.setCumulativeDistanceKm(cumulativeDist);
            stop.setLegTimeMinutes(legTime);
            stop.setCumulativeTimeMinutes(cumulativeTime);
            stop.setLegCost(legCost);
            stop.setCumulativeCost(cumulativeCost);

            stops.add(stop);

            // Update current position to this visited customer
            currentLat = customer.getLatitude();
            currentLon = customer.getLongitude();
        }

        response.setTotalStops(stops.size() - 1);
        response.setTotalDistanceKm(cumulativeDist);
        response.setTotalEstimatedTimeMinutes(cumulativeTime);
        double totalCost = stops.size() > 1 ? Math.round((baseDeliveryCost + (cumulativeDist * costPerKm)) * 100.0) / 100.0 : 0.0;
        response.setTotalEstimatedCost(totalCost);
        response.setStops(stops);

        return response;
    }

    public double getBaseDeliveryCost() {
        return baseDeliveryCost;
    }

    public double getCostPerKm() {
        return costPerKm;
    }

    public double getAverageSpeedKmh() {
        return averageSpeedKmh;
    }

    public String getDefaultDepotName() {
        return defaultDepotName;
    }

    public double getDefaultDepotLat() {
        return defaultDepotLat;
    }

    public double getDefaultDepotLon() {
        return defaultDepotLon;
    }
}
