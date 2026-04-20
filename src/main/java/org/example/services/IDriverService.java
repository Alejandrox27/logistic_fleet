package org.example.services;

import org.example.Exceptions.DriverExceptions.DriverException;
import org.example.models.Driver;

public interface IDriverService {
    void createDriver (Driver driver) throws DriverException;
}
