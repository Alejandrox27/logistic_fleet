package org.example.models;

import java.time.LocalDate;

public class Maintenance {
    private int id_maintenance;
    private LocalDate date;
    private String description;
    private double cost;
    private Vehicle vehicle;

    // --- CONSTRUCTOR ---
    public Maintenance(LocalDate date, String description,
                       double cost, Vehicle vehicle) {
        this.id_maintenance = -1;
        this.date = date;
        this.description = description;
        this.cost = cost;
        this.vehicle = vehicle;
    }

    // --- GETTERS ---

    public int getId_maintenance() {
        return id_maintenance;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public double getCost() {
        return cost;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    // --- SETTERS ---

    public void setId_maintenance (int id_maintenance) {
        this.id_maintenance = id_maintenance;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
}