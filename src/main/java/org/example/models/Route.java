package org.example.models;

import java.time.LocalDate;

public class Route {
    private int id_route;
    private String origin;
    private String destination;
    private int distance;
    private int fuel_consumed;
    private LocalDate travelDate;
    private Vehicle vehicle;
    private Driver driver;

    // --- CONSTRUCTOR ---
    public Route(int id_route, String origin, String destination, int distance,
                 int fuel_consumed, LocalDate travelDate, Vehicle vehicle, Driver driver) {
        this.id_route = id_route;
        this.origin = origin;
        this.destination = destination;
        this.distance = distance;
        this.fuel_consumed = fuel_consumed;
        this.travelDate = travelDate;
        this.vehicle = vehicle;
        this.driver = driver;
    }

    // --- GETTERS ---

    public int getId_route() {
        return id_route;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public int getDistance() {
        return distance;
    }

    public int getFuel_consumed() {
        return fuel_consumed;
    }

    public LocalDate getTravelDate() {
        return travelDate;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public Driver getDriver() {
        return driver;
    }

    // --- SETTERS ---

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setFuel_consumed(int fuel_consumed) {
        this.fuel_consumed = fuel_consumed;
    }

    public void setTravelDate(LocalDate travelDate) {
        this.travelDate = travelDate;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }
}