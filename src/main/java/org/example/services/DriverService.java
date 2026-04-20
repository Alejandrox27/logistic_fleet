package org.example.services;

import org.example.Exceptions.DriverExceptions.DriverException;
import org.example.db.DriverDAO;
import org.example.models.Driver;
import org.example.models.DriverStatus;

public class DriverService implements IDriverService{
    @Override
    public void createDriver (Driver driver) throws DriverException {
        Driver existingDriver = DriverDAO.getDriverByNumId(driver.getNum_identification());
        String regex = "^[a-zA-ZñÑáéíóúÁÉÍÓÚ]+$";

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
        DriverDAO.saveDriver(driver);
        System.out.println("The driver was created succesfully");
    }
}
