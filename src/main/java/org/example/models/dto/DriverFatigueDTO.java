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

    // Getters necesarios para la lógica de Services
    public int getIdDriver() { return idDriver; }
    public String getName() { return name; }
    public String getLastname() { return lastname; }
    public double getTotalMileage() { return totalMileage; }

    @Override
    public String toString() {
        // Formato de tabla: ID | Nombre Completo | Kilometraje
        return String.format("| %-5d | %-25s | %-12.2f km |",
                idDriver, name + " " + lastname, totalMileage);
    }
}