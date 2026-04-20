package org.example.services;

import org.example.Exceptions.VehicleExceptions.VehicleException;
import org.example.models.Maintenance;
import org.example.models.Vehicle;

import java.util.ArrayList;

public interface IVehicleService {
    void createVehicle (Vehicle vehicle) throws VehicleException;
    void registerMaintenance (Vehicle vehicle, Maintenance m) throws  VehicleException;
}
