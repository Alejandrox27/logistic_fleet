package org.example.models.dto;

public class VehiclesRiskDTO {
    private int id_vehicle;
    private String number_plate;
    private double total_spent;
    private double risk_threshold;

    public VehiclesRiskDTO(int id_vehicle, String number_plate, double total_spent, double risk_threshold) {
        this.id_vehicle = id_vehicle;
        this.number_plate = number_plate;
        this.total_spent = total_spent;
        this.risk_threshold = risk_threshold;
    }

    @Override
    public String toString() {
        return String.format("| %-10d | %-10s | %-12.2f | %-12.2f |",
                id_vehicle, number_plate, total_spent, risk_threshold);
    }

    public int getIdVehicle() { return id_vehicle; }
    public String getNumberPlate() { return number_plate; }
    public double getTotalSpent() { return total_spent; }
    public double getRiskThreshold() { return risk_threshold; }
}
