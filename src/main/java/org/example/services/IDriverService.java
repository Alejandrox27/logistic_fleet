package org.example.services;

import org.example.Exceptions.DriverExceptions.DriverException;
import org.example.models.Driver;
import org.example.models.DriverLicense;

import java.util.ArrayList;

public interface IDriverService {
    void createDriver (Driver driver, ArrayList<DriverLicense> licenses) throws DriverException;
}
