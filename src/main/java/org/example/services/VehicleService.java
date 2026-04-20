package org.example.services;

import org.example.Exceptions.VehicleExceptions.VehicleException;
import org.example.db.VehicleDAO;
import org.example.models.Vehicle;
import org.example.models.VehicleStatus;

public class VehicleService implements IVehicleService {

    @Override
    public void createVehicle(Vehicle vehicle) throws VehicleException {
        // 1. VALIDATE IF THE PLATE ALREADY EXISTS
        if (VehicleDAO.getVehicleByPlate(vehicle.getNumber_plate()) != null) {
            throw new VehicleException("The vehicle with plate " + vehicle.getNumber_plate() + " already exists.");
        }

        // 2. VALIDATE THE PLATE FORMAT
        String plateRegex = "^[A-Z]{3}[0-9]{3}$";
        if (!vehicle.getNumber_plate().toUpperCase().matches(plateRegex)) {
            throw new VehicleException("Invalid plate format. Expected AAA123.");
        }

        // 3. VALIDATE LOAD CAPACITY
        if (vehicle.getLoad_capacity() <= 0) {
            throw new VehicleException("Load capacity must be greater than 0.");
        }

        if (vehicle.getMileage() < 0) {
            throw new VehicleException("Mileage cannot be negative.");
        }

        vehicle.setStatus(VehicleStatus.AVAILABLE);

        // 5. SAVE
        VehicleDAO.saveVehicle(vehicle);
        System.out.println("✅ Vehicle " + vehicle.getNumber_plate() + " created successfully.");
    }
}