package com.example.supplychain.dto;

import java.util.ArrayList;
import java.util.List;

public class RouteOptimizationResponse {

    private String depotName;
    private double depotLatitude;
    private double depotLongitude;
    private int totalStops;
    private double totalDistanceKm;
    private double totalEstimatedTimeMinutes;
    private double totalEstimatedCost;
    private List<RouteStopDTO> stops = new ArrayList<>();
    private String algorithmExplanation;

    public RouteOptimizationResponse() {
    }

    public String getDepotName() {
        return depotName;
    }

    public void setDepotName(String depotName) {
        this.depotName = depotName;
    }

    public double getDepotLatitude() {
        return depotLatitude;
    }

    public void setDepotLatitude(double depotLatitude) {
        this.depotLatitude = depotLatitude;
    }

    public double getDepotLongitude() {
        return depotLongitude;
    }

    public void setDepotLongitude(double depotLongitude) {
        this.depotLongitude = depotLongitude;
    }

    public int getTotalStops() {
        return totalStops;
    }

    public void setTotalStops(int totalStops) {
        this.totalStops = totalStops;
    }

    public double getTotalDistanceKm() {
        return totalDistanceKm;
    }

    public void setTotalDistanceKm(double totalDistanceKm) {
        this.totalDistanceKm = totalDistanceKm;
    }

    public double getTotalEstimatedTimeMinutes() {
        return totalEstimatedTimeMinutes;
    }

    public void setTotalEstimatedTimeMinutes(double totalEstimatedTimeMinutes) {
        this.totalEstimatedTimeMinutes = totalEstimatedTimeMinutes;
    }

    public double getTotalEstimatedCost() {
        return totalEstimatedCost;
    }

    public void setTotalEstimatedCost(double totalEstimatedCost) {
        this.totalEstimatedCost = totalEstimatedCost;
    }

    public List<RouteStopDTO> getStops() {
        return stops;
    }

    public void setStops(List<RouteStopDTO> stops) {
        this.stops = stops;
    }

    public String getAlgorithmExplanation() {
        return algorithmExplanation;
    }

    public void setAlgorithmExplanation(String algorithmExplanation) {
        this.algorithmExplanation = algorithmExplanation;
    }
}
