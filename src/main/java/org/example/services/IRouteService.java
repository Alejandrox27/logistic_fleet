package org.example.services;

import org.example.Exceptions.DriverExceptions.DriverException;
import org.example.Exceptions.RouteException.RouteException;
import org.example.Exceptions.VehicleExceptions.VehicleException;
import org.example.models.Route;
import org.example.models.dto.EfficiencyReportDTO;

import java.util.List;

public interface IRouteService {
    void createRoute(Route route) throws VehicleException, DriverException, RouteException;

    List<Route> getAllRoutes();

    List<EfficiencyReportDTO> getEfficiencyReport();
}
