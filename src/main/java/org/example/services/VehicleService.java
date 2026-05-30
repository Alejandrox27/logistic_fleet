package org.example.services;

import org.example.Exceptions.VehicleExceptions.VehicleException;
import org.example.db.VehicleDAO;
import org.example.models.Maintenance;
import org.example.models.Vehicle;
import org.example.models.VehicleStatus;
import org.example.models.dto.MostWastefulBrandsDTO;
import org.example.models.dto.VehicleOperatingCostDTO;
import org.example.models.dto.VehicleVersatilityDTO;
import org.example.models.dto.VehiclesRiskDTO;

import java.time.LocalDate;
import java.util.List;

public class VehicleService implements IVehicleService {

    @Override
    public List<Vehicle> getAllVehicles() {
        List<Vehicle> vehicles = VehicleDAO.getAllVehiclesWithMaintenances();

        if (vehicles.isEmpty()) {
            System.out.println("ℹ️ No vehicles registered in the database.");
        } else {
            System.out.println("📊 Total vehicles retrieved: " + vehicles.size());
        }

        return vehicles;
    }

    @Override
    public Vehicle getVehicleById(int idVehicle) throws VehicleException {
        // 1. Llamamos al DAO que se encarga de la base de datos
        Vehicle vehicle = VehicleDAO.getVehicleById(idVehicle);

        // 2. Aplicamos la lógica de negocio (Validación)
        if (vehicle == null) {
            throw new VehicleException("Error: The vehicle with ID " + idVehicle + " does not exist in the database.");
        }

        return vehicle;
    }

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

        vehicle.setStatus(VehicleStatus.MAINTENANCE);

        VehicleDAO.updateStatus(vehicle.getIdVehicle(), VehicleStatus.MAINTENANCE);

        vehicle.addMaintenance(m);
    }

    @Override
    public void changeStatus (Vehicle vehicle, VehicleStatus newStatus) {
        vehicle.setStatus(newStatus);

        VehicleDAO.updateStatus(vehicle.getIdVehicle(), newStatus);
    }

    @Override
    public List<VehiclesRiskDTO> getVehiclesRequiringMaintenance () {
        List<VehiclesRiskDTO> vehiclesWithRiskDTO = VehicleDAO.getVehiclesWithRisk();

        for (VehiclesRiskDTO dto: vehiclesWithRiskDTO) {
            VehicleDAO.updateStatus(dto.getIdVehicle(), VehicleStatus.MAINTENANCE_REQUIRED);
        }

        return vehiclesWithRiskDTO;
    }

    @Override
    public List<MostWastefulBrandsDTO> getRankingMostWastefulBrands() {
        List<MostWastefulBrandsDTO> ranking = VehicleDAO.getRankingMostWastefulBrands();

        if (ranking.isEmpty()) {
            System.out.println("ℹ️ No data available to calculate fuel consumption by brand.");
        }

        return ranking;
    }

    @Override
    public VehicleVersatilityDTO getMostVersatileVehicle() {
        VehicleVersatilityDTO versatile = VehicleDAO.getMostVersatileVehicle();

        if (versatile != null) {
            System.out.println("🏆 Most Versatile Vehicle: " + versatile.getBrand() +
                    " (" + versatile.getVisitedPlaces() + " destinations)");
        }

        return versatile;
    }

    @Override
    public List<VehicleOperatingCostDTO> getReportOperatingCosts() {
        List<VehicleOperatingCostDTO> report = VehicleDAO.getReportOperatingCosts();

        System.out.println("💰 Operating costs report generated for " + report.size() + " vehicles.");

        return report;
    }
}