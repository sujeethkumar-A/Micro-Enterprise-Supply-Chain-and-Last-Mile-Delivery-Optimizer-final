package com.example.supplychain.dto;

import java.util.List;

public class RouteOptimizationRequest {

    private String depotName;
    private Double depotLatitude;
    private Double depotLongitude;
    private List<Long> deliveryIds;

    public RouteOptimizationRequest() {
    }

    public RouteOptimizationRequest(String depotName, Double depotLatitude, Double depotLongitude, List<Long> deliveryIds) {
        this.depotName = depotName;
        this.depotLatitude = depotLatitude;
        this.depotLongitude = depotLongitude;
        this.deliveryIds = deliveryIds;
    }

    public String getDepotName() {
        return depotName;
    }

    public void setDepotName(String depotName) {
        this.depotName = depotName;
    }

    public Double getDepotLatitude() {
        return depotLatitude;
    }

    public void setDepotLatitude(Double depotLatitude) {
        this.depotLatitude = depotLatitude;
    }

    public Double getDepotLongitude() {
        return depotLongitude;
    }

    public void setDepotLongitude(Double depotLongitude) {
        this.depotLongitude = depotLongitude;
    }

    public List<Long> getDeliveryIds() {
        return deliveryIds;
    }

    public void setDeliveryIds(List<Long> deliveryIds) {
        this.deliveryIds = deliveryIds;
    }
}
