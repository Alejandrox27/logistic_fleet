package org.example.services;

import org.example.Exceptions.DriverExceptions.DriverException;
import org.example.db.DriverDAO;
import org.example.models.Driver;
import org.example.models.DriverLicense;
import org.example.models.DriverStatus;
import org.example.models.dto.DriverFatigueDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DriverService implements IDriverService{
    @Override
    public void createDriver (Driver driver, ArrayList<DriverLicense> licenses) throws DriverException {
        Driver existingDriver = DriverDAO.getDriverByNumId(driver.getNum_identification());
        String regex = "^[a-zA-ZñÑáéíóúÁÉÍÓÚ]+$";

        if (licenses.isEmpty()) {
            throw new DriverException("The driver must have at least one category in his license");
        }

        //VALIDATIONS WITH THE NUMBER OF IDENTIFICATION
        if (driver.getNum_identification() <= 0) {
            throw new DriverException("The identification is not valid");
        }

        if (existingDriver != null) {
            throw new DriverException("Error: The identification " + driver.getNum_identification() + " is already in database.");
        }

        //VALIDATIONS FOR THE NAME
        if (driver.getName() == null || driver.getName().trim().isEmpty()) {
            throw new DriverException("the name cannot be empty.");
        }

        if (!driver.getName().matches(regex)) {
            throw new DriverException("the name: '" + driver.getName() + "' has especial chars.");
        }

        // VALIDATIONS FOR THE LASTNAME
        if (driver.getLastName() == null || driver.getLastName().trim().isEmpty()) {
            throw new DriverException("the lastname cannot be empty.");
        }

        if (!driver.getLastName().matches(regex)) {
            throw new DriverException("the lastname: '" + driver.getLastName() + "' has especial chars.");
        }

        // VALIDATIONS FOR THE SECOND LASTNAME
        if (driver.getSecondLastName() == null || driver.getSecondLastName().trim().isEmpty()) {
            throw new DriverException("the second lastname cannot be empty.");
        }

        if (!driver.getSecondLastName().matches(regex)) {
            throw new DriverException("the second lastname: '" + driver.getSecondLastName() + "' has especial chars.");
        }

        // Set default status (AVAILABLE)
        driver.setStatus(DriverStatus.AVAILABLE);

        // CREATE THE DRIVER
        int generatedId = DriverDAO.saveDriver(driver);

        if (generatedId != -1) {
            // 1. Usamos un Set para verificar que no haya categorías duplicadas (Propiedad de Conjuntos)
            // En matemáticas: A ∩ B = Ø (no debe haber intersección de categorías iguales)
            Set<String> uniqueCategories = new HashSet<>();

            for (DriverLicense lic : licenses) {
                // Validación A: Categoría Duplicada
                if (!uniqueCategories.add(lic.getCategory())) {
                    throw new DriverException("Error: The category '" + lic.getCategory() + "' is duplicated in the list");
                }

                // Validación B: Relación de Orden de Fechas
                if (!lic.getIssueDate().isBefore(lic.getExpiryDate())) {
                    throw new DriverException("Error at category " + lic.getCategory() +
                            ": the Expiry date can not be before the issue date");
                }

                // Validación C: Licencia no vencida (Opcional, según tu lógica de negocio)
                if (lic.getExpiryDate().isBefore(LocalDate.now())) {
                    System.out.println("Warning: The license " + lic.getCategory() + " is already expired.");
                }

                int categoryId = DriverDAO.getCategoryIdByName(lic.getCategory());

                if (categoryId == -1) {
                    throw new DriverException("Error: the category '" + lic.getCategory() + "' doesn't exists in the system.");
                }

                DriverDAO.saveLicense(generatedId, categoryId, lic);
            }
        }
        System.out.println("The driver was created succesfully");
    }

    @Override
    public List<DriverFatigueDTO> getDriversWithFatigueRisk() {
        List<DriverFatigueDTO> fatigueReport = DriverDAO.getReportDriverFatigue();

        for (DriverFatigueDTO dto : fatigueReport) {
            DriverDAO.updateStatus(dto.getIdDriver(), DriverStatus.RESTING);

            System.out.println("⚠️ Alert: Driver " + dto.getName() + " " + dto.getLastname() +
                    " moved to RESTING status due to fatigue (" + dto.getTotalMileage() + " km).");
        }

        return fatigueReport;
    }

    @Override
    public List<Driver> getInactiveDriversCurrentMonth() {
        List<Driver> inactiveDrivers = DriverDAO.GetDriversNotActiveMonth();

        if (inactiveDrivers.isEmpty()) {
            System.out.println("ℹ️ All drivers have been active this month.");
        } else {
            System.out.println("📋 Found " + inactiveDrivers.size() + " inactive drivers this month.");
        }

        return inactiveDrivers;
    }

    @Override
    public List<Driver> getInactiveDriversByPeriod(int month, int year) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Invalid month. Must be between 1 and 12.");
        }

        List<Driver> inactiveDrivers = DriverDAO.GetDriversNotActiveMonth(month, year);

        if (inactiveDrivers.isEmpty()) {
            System.out.println("ℹ️ All drivers have been active this month.");
        } else {
            System.out.println("📋 Found " + inactiveDrivers.size() + " inactive drivers this month.");
        }

        return inactiveDrivers;
    }
}
