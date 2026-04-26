package org.example.models.dto;

public class DriverFatigueDTO {
    private int idDriver;
    private String name;
    private String lastname;
    private double totalMileage;

    public DriverFatigueDTO(int idDriver, String name, String lastname, double totalMileage) {
        this.idDriver = idDriver;
        this.name = name;
        this.lastname = lastname;
        this.totalMileage = totalMileage;
    }

    // GETTERS
    public int getIdDriver() { return idDriver; }
    public String getName() { return name; }
    public String getLastname() { return lastname; }
    public double getTotalMileage() { return totalMileage; }

    @Override
    public String toString() {
        // TABLE FORMAT
        return String.format("| %-5d | %-25s | %-12.2f km |",
                idDriver, name + " " + lastname, totalMileage);
    }
}