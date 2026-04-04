package org.example.models;

import java.util.ArrayList;
import java.util.List;

public abstract class Vehicle {
    private int id_vehicle;
    private String number_plate;
    private String brand;
    private int model;
    private int load_capacity;
    private int mileage;
    private List<Maintenance> maintenanceHistory;

    public Vehicle (int id_vehicle, String number_plate, String brand, int model, int load_capacity, int mileage) {
        this.id_vehicle = id_vehicle;
        this.number_plate = number_plate;
        this.brand = brand;
        this.model = model;
        this.load_capacity = load_capacity;
        this.mileage = mileage;
        this.maintenanceHistory = new ArrayList<>();
    }

    public abstract double calculateToll ();

    public List<Maintenance> getMaintenanceHistory() {
        return maintenanceHistory;
    }

    public void addMaintenance (Maintenance maintenance) {
        this.maintenanceHistory.add(maintenance);
    }

    public double getTotalMaintenanceCost() {
        int totalMaintenanceCost = 0;
        for (Maintenance m: maintenanceHistory) {
            totalMaintenanceCost += m.getCost();
        }

        return totalMaintenanceCost;
    }
    //--- GETTERS ---

    public int getId_vehicle() {
        return id_vehicle;
    }

    public String getNumber_plate() {
        return number_plate;
    }

    public String getBrand() {
        return brand;
    }

    public int getModel() {
        return model;
    }

    public int getLoad_capacity() {
        return load_capacity;
    }

    public int getMileage() {
        return mileage;
    }

    // --- SETTERS ---

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public void setLoad_capacity(int load_capacity) {
        this.load_capacity = load_capacity;
    }


}
