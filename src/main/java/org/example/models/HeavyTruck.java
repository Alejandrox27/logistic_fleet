package org.example.models;

public class HeavyTruck extends Vehicle {
    private static final double PRICE_BY_AXLE = 12000.0;

    public HeavyTruck(int id_vehicle, String number_plate, String brand, int model,
                      int load_capacity, int mileage, int axles, String fuelType, VehicleStatus status) {

        super(id_vehicle, number_plate, brand, model, load_capacity, mileage, axles, fuelType, status);
    }

    @Override
    public double calculateToll () {
        return this.getAxles() * PRICE_BY_AXLE;
    }
}