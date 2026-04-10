package org.example.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Driver {
    private int id_driver;
    private int num_identification;
    private String name;
    private String lastName;
    private String secondLastName;
    private LocalDate contratationDate;
    private DriverStatus status;
    private List<DriverLicense> licenses;

    public Driver(int id_driver, int num_identification, String name, String lastName,
                  String secondLastName, LocalDate contratationDate, DriverStatus status) {
        this.id_driver = id_driver;
        this.num_identification = num_identification;
        this.name = name;
        this.lastName = lastName;
        this.secondLastName = secondLastName;
        this.contratationDate = contratationDate;
        this.status = status;
        this.licenses = new ArrayList<>();
    }

    // --- GETTERS ---

    public int getIdDriver() {
        return id_driver;
    }

    public int getNum_identification() {
        return num_identification;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getSecondLastName() {
        return secondLastName;
    }

    public LocalDate getContratationDate() {
        return contratationDate;
    }

    public List<DriverLicense> getLicenses() {
        return licenses;
    }

    public DriverStatus getStatus() { return status; }

    // --- SETTERS ---

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setSecondLastName(String secondLastName) {
        this.secondLastName = secondLastName;
    }

    public void addLicense(DriverLicense license) {
        this.licenses.add(license);
    }

    public void setStatus(DriverStatus status) { this.status = status; }
}