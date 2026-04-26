package org.example.services;

import org.example.Exceptions.DriverExceptions.DriverException;
import org.example.Exceptions.DriverExceptions.DriverFatigueException;
import org.example.Exceptions.DriverExceptions.DriverNotAvailableException;
import org.example.Exceptions.DriverExceptions.ExpiredLicenseException;
import org.example.Exceptions.RouteException.RouteException;
import org.example.Exceptions.RouteException.SameOriginAndDestinationException;
import org.example.Exceptions.VehicleExceptions.VehicleException;
import org.example.Exceptions.VehicleExceptions.VehicleNotAvailableException;
import org.example.db.DriverDAO;
import org.example.db.RoutesDAO;
import org.example.db.VehicleDAO;
import org.example.models.*;
import org.example.models.dto.DriverFatigueDTO;
import org.example.models.dto.EfficiencyReportDTO;

import java.util.List;

public class RouteService implements IRouteService {
    @Override
    public void createRoute(Route route) throws VehicleException, DriverException, RouteException {
        VehicleStatus currentStatusVehicle = VehicleDAO.checkDisponibility(route.getVehicle().getIdVehicle());
        DriverFatigueDTO tiredDriver = DriverDAO.getReportDriverFatigue(route.getDriver().getIdDriver());
        DriverStatus currentStatusDriver = DriverDAO.checkDisponibility(route.getDriver().getIdDriver());
        List<DriverLicense> driverLicenses = route.getDriver().getLicenses();

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

        // check if the licenses aren't null or empty
        if (driverLicenses == null || driverLicenses.isEmpty()) {
            throw new DriverException("The driver does not have any registered licenses.");
        }

        // Check driver license expiry date
        for (DriverLicense license : driverLicenses) {
            if (license.getCategory().equalsIgnoreCase("C2") || license.getCategory().equalsIgnoreCase("C3")) {
                if (license.isExpired()) {
                    throw new ExpiredLicenseException(license);
                }
            } else {
                throw new DriverException("The driver can't drive the vehicles because the license is not C2 or C3");
            }
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

    @Override
    public List<Route> getAllRoutes() {
        List<Route> routes = RoutesDAO.getAllRoutes();

        if (routes.isEmpty()) {
            System.out.println("ℹ️ No routes registered in the system yet.");
        }

        return routes;
    }

    @Override
    public List<EfficiencyReportDTO> getEfficiencyReport() {
        List<EfficiencyReportDTO> report = RoutesDAO.getEfficiencyReport();

        System.out.println("📊 Efficiency Report generated for drivers with more than 5 routes.");

        for (EfficiencyReportDTO dto : report) {
            if (dto.getEfficiencyAverage() < 0.15) {
                System.out.println("🌟 High Performance: " + dto.getDriverName() +
                        " with vehicle " + dto.getVehicleBrand());
            }
        }

        return report;
    }
}
