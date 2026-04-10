package org.example.models.dto;

public class VehicleVersatilityDTO {
    private int idVehicle;
    private String brand;
    private String model;
    private int visitedPlaces; // Representa el COUNT(DISTINCT r.destination)

    public VehicleVersatilityDTO(int idVehicle, String brand, String model, int visitedPlaces) {
        this.idVehicle = idVehicle;
        this.brand = brand;
        this.model = model;
        this.visitedPlaces = visitedPlaces;
    }

    // Getters
    public int getIdVehicle() { return idVehicle; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public int getVisitedPlaces() { return visitedPlaces; }

    @Override
    public String toString() {
        return String.format("--- VEHÍCULO MÁS VERSÁTIL ---\n" +
                        "ID: %d\n" +
                        "Vehículo: %s %s\n" +
                        "Destinos Únicos Visitados: %d\n" +
                        "-----------------------------",
                idVehicle, brand, model, visitedPlaces);
    }
}