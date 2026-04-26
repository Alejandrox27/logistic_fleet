package org.example.services;

import org.example.Exceptions.VehicleExceptions.VehicleException;
import org.example.db.VehicleDAO;
import org.example.models.Maintenance;
import org.example.models.Vehicle;
import org.example.models.VehicleStatus;
import org.example.models.dto.VehiclesRiskDTO;

import java.time.LocalDate;
import java.util.List;

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

    @Override
    public void registerMaintenance(Vehicle vehicle, Maintenance m) throws VehicleException {
        if (m.getDate().isAfter(LocalDate.now())) {
            throw new VehicleException("The maintenance date cannot be in the future.");
        }

        VehicleDAO.saveMaintenance(vehicle.getIdVehicle(), m);

        vehicle.addMaintenance(m);
    }

    @Override
    public List<VehiclesRiskDTO> getVehiclesRequiringMaintenance () {
        List<VehiclesRiskDTO> vehiclesWithRiskDTO = VehicleDAO.getVehiclesWithRisk();

        for (VehiclesRiskDTO dto: vehiclesWithRiskDTO) {
            VehicleDAO.updateStatus(dto.getIdVehicle(), VehicleStatus.MAINTENANCE_REQUIRED);
        }

        return vehiclesWithRiskDTO;
    }
}