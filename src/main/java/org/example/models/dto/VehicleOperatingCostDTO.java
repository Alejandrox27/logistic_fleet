package org.example.models.dto;

public class VehicleOperatingCostDTO {
    private int idVehicle;
    private String brand;
    private String numberPlate;
    private double costsFuel;
    private double allMaintenancesCosts;
    private double totalOperatingCost;

    public VehicleOperatingCostDTO(int idVehicle, String brand, String numberPlate,
                                   double costsFuel, double allMaintenancesCosts, double totalOperatingCost) {
        this.idVehicle = idVehicle;
        this.brand = brand;
        this.numberPlate = numberPlate;
        this.costsFuel = costsFuel;
        this.allMaintenancesCosts = allMaintenancesCosts;
        this.totalOperatingCost = totalOperatingCost;
    }

    // Getters
    public int getIdVehicle() { return idVehicle; }
    public String getBrand() { return brand; }
    public String getNumberPlate() { return numberPlate; }
    public double getCostsFuel() { return costsFuel; }
    public double getAllMaintenancesCosts() { return allMaintenancesCosts; }
    public double getTotalOperatingCost() { return totalOperatingCost; }

    @Override
    public String toString() {
        return String.format("| %-5d | %-10s | %-8s | Fuel: %-10.2f | Maint: %-10.2f | Total: %-12.2f |",
                idVehicle, brand, numberPlate, costsFuel, allMaintenancesCosts, totalOperatingCost);
    }
}