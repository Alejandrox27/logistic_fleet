package org.example.models.dto;

public class EfficiencyReportDTO {
    private String driverName;
    private String vehicleBrand;
    private int vehicleModel;
    private double efficiencyAverage;
    private int totalRoutes;

    public EfficiencyReportDTO(String driverName, String vehicleBrand, int vehicleModel, double efficiencyAverage, int totalRoutes) {
        this.driverName = driverName;
        this.vehicleBrand = vehicleBrand;
        this.vehicleModel = vehicleModel;
        this.efficiencyAverage = efficiencyAverage;
        this.totalRoutes = totalRoutes;
    }

    @Override
    public String toString() {
        return String.format("| %-15s | %-10s | %-5d | %-10.4f | %-5d |",
                driverName, vehicleBrand, vehicleModel, efficiencyAverage, totalRoutes);
    }
}
