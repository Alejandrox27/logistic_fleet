package org.example.services;

import org.example.Exceptions.VehicleExceptions;
import org.example.Exceptions.VehicleNotAvailableException;
import org.example.db.RoutesDAO;
import org.example.db.VehicleDAO;
import org.example.models.Route;
import org.example.models.VehicleStatus;

public class RouteService {
    public String createRoute(Route route) throws VehicleExceptions {
        VehicleStatus currentStatus = VehicleDAO.checkDisponibility(route.getVehicle().getIdVehicle());

        if (currentStatus == null) {
            throw new VehicleExceptions("The vehicle doesn't exist.");
        }

        if (currentStatus != VehicleStatus.AVAILABLE) {
            throw new VehicleNotAvailableException(currentStatus);
        }

        RoutesDAO.saveRoute(route);
        return "Route created successfully!";
    }
}
