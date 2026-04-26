package org.example.services;

import org.example.Exceptions.DriverExceptions.DriverException;
import org.example.models.Driver;
import org.example.models.DriverLicense;
import org.example.models.dto.DriverFatigueDTO;

import java.util.ArrayList;
import java.util.List;

public interface IDriverService {
    void createDriver (Driver driver, ArrayList<DriverLicense> licenses) throws DriverException;

    List<DriverFatigueDTO> getDriversWithFatigueRisk();
}
