package org.example.services;

import org.example.Exceptions.DriverExceptions.DriverException;
import org.example.Exceptions.DriverExceptions.DriverFatigueException;
import org.example.Exceptions.VehicleExceptions.VehicleException;
import org.example.Exceptions.VehicleExceptions.VehicleNotAvailableException;
import org.example.db.DriverDAO;
import org.example.db.RoutesDAO;
import org.example.db.VehicleDAO;
import org.example.models.Route;
import org.example.models.VehicleStatus;
import org.example.models.dto.DriverFatigueDTO;

public class RouteService {
    public String createRoute(Route route) throws VehicleException, DriverFatigueException {
        VehicleStatus currentStatus = VehicleDAO.checkDisponibility(route.getVehicle().getIdVehicle());
        DriverFatigueDTO driver = DriverDAO.getReportDriverFatigue(route.getDriver().getIdDriver());

        if (currentStatus == null) {
            throw new VehicleException("The vehicle doesn't exist.");
        }

        if (currentStatus != VehicleStatus.AVAILABLE) {
            throw new VehicleNotAvailableException(currentStatus);
        }

        if (driver != null) {
            throw new DriverFatigueException(driver);
        }

        RoutesDAO.saveRoute(route);
        return "Route created successfully!";
    }
}
