package org.example.models;

public class HeavyTruck extends Vehicle {
    private int axles;
    private String fuelType;
    private static final double PRICE_BY_AXLE = 12000.0;

    public HeavyTruck(int id_vehicle, String number_plate, String brand, int model,
                      int load_capacity, int mileage, int axles, String fuelType) {

        super(id_vehicle, number_plate, brand, model, load_capacity, mileage);
        this.axles = axles;
        this.fuelType = fuelType;
    }

    @Override
    public double calculateToll () {
        return this.getAxles() * PRICE_BY_AXLE;
    }

    // --- Getters ---

    public int getAxles() {
        return axles;
    }

    public String getFuelType() {
        return fuelType;
    }

    // --- Setters ---

    public void setAxles(int axles) {
        this.axles = axles;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }
}