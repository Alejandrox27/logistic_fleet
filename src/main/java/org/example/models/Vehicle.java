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
    private int axles;
    private String fuelType;
    private VehicleStatus statusVehicle;
    private List<Maintenance> maintenanceHistory;

    public Vehicle (String number_plate, String brand, int model, int load_capacity,
                    int mileage, int axles, String fuelType, VehicleStatus statusVehicle) {
        this.id_vehicle = -1;
        this.number_plate = number_plate;
        this.brand = brand;
        this.model = model;
        this.load_capacity = load_capacity;
        this.mileage = mileage;
        this.axles = axles;
        this.fuelType = fuelType;
        this.statusVehicle = statusVehicle;
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
        double totalMaintenanceCost = 0;
        for (Maintenance m: maintenanceHistory) {
            totalMaintenanceCost += m.getCost();
        }

        return totalMaintenanceCost;
    }
    //--- GETTERS ---

    public int getIdVehicle() {
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

    public int getAxles () {
        return axles;
    }

    public String getFuelType() {
        return fuelType;
    }

    public VehicleStatus getStatus() { return statusVehicle; };

    // --- SETTERS ---
    public void setNumber_plate (String number_plate) {this.number_plate = number_plate;}

    public void setBrand (String brand) {this.brand = brand;}

    public void setModel (int model) {this.model = model;}

    public void setId_vehicle (int id_vehicle) {
        this.id_vehicle = id_vehicle;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public void setLoad_capacity(int load_capacity) {
        this.load_capacity = load_capacity;
    }

    public void setAxles(int axles) {
        this.axles = axles;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public void setStatus(VehicleStatus status) {
        this.statusVehicle = status;
    }


    @Override
    public String toString() {
        return "Vehicle [" +
                "ID: " + id_vehicle +
                " | Plate: '" + number_plate + '\'' +
                " | Brand: '" + brand + '\'' +
                " | Model: " + model +
                " | Load Cap: " + load_capacity + "kg" +
                " | Mileage: " + mileage + "km" +
                " | Maintenances: " + maintenanceHistory.size() +
                " | Total Maint. Cost: $" + getTotalMaintenanceCost() +
                ']';
    }

}
