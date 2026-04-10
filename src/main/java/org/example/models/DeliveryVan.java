package org.example.models;

public class DeliveryVan extends Vehicle{
    public DeliveryVan(int id, String plate, String brand, int model, int load, int mileage, int axles, String fuelType) {
        super(id, plate, brand, model, load, mileage, axles, fuelType);
    }

    @Override
    public double calculateToll () {
        return 0.0;
    }
}
