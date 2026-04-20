package org.example.services;

import org.example.Exceptions.DriverExceptions.DriverException;
import org.example.db.DriverDAO;
import org.example.models.Driver;

public class DriverService implements IDriverService{
    @Override
    public void createDriver (Driver driver) throws DriverException {
        Driver existingDriver = DriverDAO.getDriverByNumId(driver.getNum_identification());

        if (existingDriver != null) {
            throw new DriverException("Error: La cédula " + driver.getNum_identification() + " ya está registrada.");
        }

        DriverDAO.saveDriver(driver);
    }
}
