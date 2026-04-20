package org.example.models;

public class DeliveryVan extends Vehicle{
    public DeliveryVan(String plate, String brand, int model, int load, int mileage, int axles, String fuelType, VehicleStatus status) {
        super(plate, brand, model, load, mileage, axles, fuelType, status);
    }

    @Override
    public double calculateToll () {
        return 0.0;
    }
}
