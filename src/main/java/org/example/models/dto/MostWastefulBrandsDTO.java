package org.example.models.dto;

public class MostWastefulBrandsDTO {
    private String brand;
    private double averageFuelConsumed;

    public MostWastefulBrandsDTO(String brand, double averageFuelConsumed) {
        this.brand = brand;
        this.averageFuelConsumed = averageFuelConsumed;
    }

    public String getBrand () {
        return this.brand;
    }

    public double getAverageFuelConsumed () {
        return this.averageFuelConsumed;
    }

    @Override
    public String toString() {
        return String.format("| %-15s | %-5.4f |",
                brand, averageFuelConsumed);
    }
}
