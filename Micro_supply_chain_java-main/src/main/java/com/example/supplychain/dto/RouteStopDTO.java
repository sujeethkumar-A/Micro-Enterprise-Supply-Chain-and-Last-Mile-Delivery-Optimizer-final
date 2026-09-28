package com.example.supplychain.dto;

public class RouteStopDTO {

    private int sequenceNumber;
    private String stopType; // "DEPOT" or "CUSTOMER"
    private String locationName;
    private Long deliveryId;
    private Long orderId;
    private String customerName;
    private String phone;
    private String address;
    private String city;
    private double latitude;
    private double longitude;
    private double legDistanceKm;
    private double cumulativeDistanceKm;
    private double legTimeMinutes;
    private double cumulativeTimeMinutes;
    private double legCost;
    private double cumulativeCost;

    public RouteStopDTO() {
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getStopType() {
        return stopType;
    }

    public void setStopType(String stopType) {
        this.stopType = stopType;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Long getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Long deliveryId) {
        this.deliveryId = deliveryId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLegDistanceKm() {
        return legDistanceKm;
    }

    public void setLegDistanceKm(double legDistanceKm) {
        this.legDistanceKm = legDistanceKm;
    }

    public double getCumulativeDistanceKm() {
        return cumulativeDistanceKm;
    }

    public void setCumulativeDistanceKm(double cumulativeDistanceKm) {
        this.cumulativeDistanceKm = cumulativeDistanceKm;
    }

    public double getLegTimeMinutes() {
        return legTimeMinutes;
    }

    public void setLegTimeMinutes(double legTimeMinutes) {
        this.legTimeMinutes = legTimeMinutes;
    }

    public double getCumulativeTimeMinutes() {
        return cumulativeTimeMinutes;
    }

    public void setCumulativeTimeMinutes(double cumulativeTimeMinutes) {
        this.cumulativeTimeMinutes = cumulativeTimeMinutes;
    }

    public double getLegCost() {
        return legCost;
    }

    public void setLegCost(double legCost) {
        this.legCost = legCost;
    }

    public double getCumulativeCost() {
        return cumulativeCost;
    }

    public void setCumulativeCost(double cumulativeCost) {
        this.cumulativeCost = cumulativeCost;
    }
}
