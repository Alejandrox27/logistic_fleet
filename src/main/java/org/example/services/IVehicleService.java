package org.example.services;

import org.example.Exceptions.VehicleExceptions.VehicleException;
import org.example.models.Vehicle;

public interface IVehicleService {
    void createVehicle (Vehicle vehicle) throws VehicleException;
}
