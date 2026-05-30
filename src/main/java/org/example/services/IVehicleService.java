package org.example.services;

import org.example.Exceptions.VehicleExceptions.VehicleException;
import org.example.models.Maintenance;
import org.example.models.Vehicle;
import org.example.models.VehicleStatus;
import org.example.models.dto.MostWastefulBrandsDTO;
import org.example.models.dto.VehicleOperatingCostDTO;
import org.example.models.dto.VehicleVersatilityDTO;
import org.example.models.dto.VehiclesRiskDTO;

import java.util.ArrayList;
import java.util.List;

public interface IVehicleService {
    public List<Vehicle> getAllVehicles();
    public Vehicle getVehicleById(int idVehicle) throws VehicleException;
    void createVehicle (Vehicle vehicle) throws VehicleException;
    void registerMaintenance (Vehicle vehicle, Maintenance m) throws  VehicleException;
    public void changeStatus (Vehicle vehicle, VehicleStatus newStatus);
    List<VehiclesRiskDTO> getVehiclesRequiringMaintenance ();
    List<MostWastefulBrandsDTO> getRankingMostWastefulBrands();
    public VehicleVersatilityDTO getMostVersatileVehicle();
    public List<VehicleOperatingCostDTO> getReportOperatingCosts();
}
