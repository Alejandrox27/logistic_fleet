package org.example.services;

import org.example.Exceptions.DriverExceptions.DriverException;
import org.example.Exceptions.DriverExceptions.DriverFatigueException;
import org.example.Exceptions.DriverExceptions.DriverNotAvailableException;
import org.example.Exceptions.RouteException.RouteException;
import org.example.Exceptions.RouteException.SameOriginAndDestinationException;
import org.example.Exceptions.VehicleExceptions.VehicleException;
import org.example.Exceptions.VehicleExceptions.VehicleNotAvailableException;
import org.example.db.DriverDAO;
import org.example.db.RoutesDAO;
import org.example.db.VehicleDAO;
import org.example.models.DriverStatus;
import org.example.models.Route;
import org.example.models.VehicleStatus;
import org.example.models.dto.DriverFatigueDTO;

public class RouteService implements IRouteService {
    @Override
    public void createRoute(Route route) throws VehicleException, DriverException, RouteException {
        VehicleStatus currentStatusVehicle = VehicleDAO.checkDisponibility(route.getVehicle().getIdVehicle());
        DriverFatigueDTO tiredDriver = DriverDAO.getReportDriverFatigue(route.getDriver().getIdDriver());
        DriverStatus currentStatusDriver = DriverDAO.checkDisponibility(route.getDriver().getIdDriver());

        // Check if the destination is not the same as the origin
        if (route.getOrigin().equalsIgnoreCase(route.getDestination())) {
            throw new SameOriginAndDestinationException(route.getOrigin());
        }

        // check if the driver exists
        if (currentStatusDriver == null) {
            throw new DriverException("The Driver doesn't exists");
        }

        // check if the vehicle exists
        if (currentStatusVehicle == null) {
            throw new VehicleException("The vehicle doesn't exist.");
        }

        //check vehicle disponibility
        if (currentStatusVehicle != VehicleStatus.AVAILABLE) {
            throw new VehicleNotAvailableException(currentStatusVehicle);
        }

        // check Fatigue (>2000km)
        if (tiredDriver != null) {
            throw new DriverFatigueException(tiredDriver);
        }

        //check if the driver is available for a new route
        if (currentStatusDriver != DriverStatus.AVAILABLE) {
            throw new DriverNotAvailableException(currentStatusDriver);
        }

        RoutesDAO.saveRoute(route);
        VehicleDAO.updateStatus(route.getVehicle().getIdVehicle(), VehicleStatus.IN_ROUTE);
        DriverDAO.updateStatus(route.getDriver().getIdDriver(), DriverStatus.IN_ROUTE);
    }
}
