package org.example.services;

import org.example.db.DriverDAO;
import org.example.models.Driver;

public class DriverService implements IDriverService{
    @Override
    public void createDriver (Driver driver) {


        DriverDAO.saveDriver(driver);
    }
}
