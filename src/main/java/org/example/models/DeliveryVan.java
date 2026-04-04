package org.example.models;

public class DeliveryVan extends Vehicle{
    public DeliveryVan(int id, String plate, String brand, int model, int load, int mileage) {
        super(id, plate, brand, model, load, mileage);
    }

    @Override
    public double calculateToll () {
        return 0.0;
    }
}
