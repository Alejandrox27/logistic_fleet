package org.example.models;

import java.time.LocalDate;

public class DriverLicense {
    private int id_license;
    private LocalDate issueDate;
    private LocalDate  expiryDate;
    private String description;
    private String category;
    private Driver driver;

    public DriverLicense(int id_license, LocalDate issueDate, LocalDate expiryDate,
                         String description, String category, Driver driver) {
        this.id_license = id_license;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
        this.description = description;
        this.category = category;
        this.driver = driver;
    }

    public boolean isExpired() {
        return LocalDate.now().isAfter(this.expiryDate);
    }

    // --- GETTERS ---

    public int getId_license() {
        return id_license;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public Driver getDriver() {
        return driver;
    }

    // --- SETTERS ---

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }
}