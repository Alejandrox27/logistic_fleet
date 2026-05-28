package org.example;

import org.example.Exceptions.DriverExceptions.DriverException;
import org.example.Exceptions.RouteException.RouteException;
import org.example.Exceptions.VehicleExceptions.VehicleException;
import org.example.models.dto.*;
import org.example.services.DriverService;
import org.example.services.RouteService;
import org.example.services.VehicleService;

import java.util.List;
import java.util.Scanner;

public class Main {

    // Instanciamos los servicios
    private static final VehicleService vehicleService = new VehicleService();
    private static final DriverService driverService = new DriverService();
    private static final RouteService routeService = new RouteService();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int option = -1;

        System.out.println("==================================================");
        System.out.println("  LOGISTICS SYSTEM - BASE MODULE TESTING  ");
        System.out.println("==================================================");

        while (option != 0) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. Vehicle Operations (Reports & Queries)");
            System.out.println("2. Driver Operations (Reports & Queries)");
            System.out.println("3. Route Operations (Reports & Queries)");
            System.out.println("0. Exit Application");
            System.out.print("Select an option: ");

            try {
                option = Integer.parseInt(scanner.nextLine());

                switch (option) {
                    case 1:
                        vehicleMenu(scanner);
                        break;
                    case 2:
                        driverMenu(scanner);
                        break;
                    case 3:
                        routeMenu(scanner);
                        break;
                    case 0:
                        System.out.println("Exiting the system. Goodbye!");
                        break;
                    default:
                        System.out.println("⚠️ Invalid option. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Error: Please enter a valid number.");
            } catch (Exception e) {
                System.out.println("❌ Unexpected System Error: " + e.getMessage());
            }
        }
        scanner.close();
    }

    // --- VEHICLE MENU ---
    private static void vehicleMenu(Scanner scanner) {
        System.out.println("\n--- VEHICLE MENU ---");
        System.out.println("1. View All Vehicles");
        System.out.println("2. Run Maintenance Risk Report (Auto-blocks vehicles)");
        System.out.println("3. Ranking: Most Wasteful Brands");
        System.out.println("4. Most Versatile Vehicle");
        System.out.println("5. Operating Costs Report");
        System.out.print("Select an option: ");

        int opt = Integer.parseInt(scanner.nextLine());

        switch (opt) {
            case 1:
                vehicleService.getAllVehicles().forEach(v ->
                        System.out.println(v.getNumber_plate() + " - " + v.getBrand() + " [" + v.getStatus() + "]")
                );
                break;
            case 2:
                List<VehiclesRiskDTO> risks = vehicleService.getVehiclesRequiringMaintenance();
                risks.forEach(r -> System.out.println("Risk Detected: " + r.getIdVehicle()));
                break;
            case 3:
                List<MostWastefulBrandsDTO> wasteful = vehicleService.getRankingMostWastefulBrands();
                wasteful.forEach(w -> System.out.println("Brand: " + w.getBrand() + " | Avg Fuel: " + w.getAverageFuelConsumed()));
                break;
            case 4:
                VehicleVersatilityDTO versatile = vehicleService.getMostVersatileVehicle();
                if(versatile != null) {
                    System.out.println("Winner: " + versatile.getBrand() + " " + versatile.getModel() + " - " + versatile.getVisitedPlaces() + " places.");
                }
                break;
            case 5:
                List<VehicleOperatingCostDTO> costs = vehicleService.getReportOperatingCosts();
                costs.forEach(c -> System.out.println(c.getBrand() + " (" + c.getNumberPlate() + ") Total Cost: $" + c.getTotalOperatingCost()));
                break;
            default:
                System.out.println("⚠️ Invalid vehicle option.");
        }
    }

    // --- DRIVER MENU ---
    private static void driverMenu(Scanner scanner) {
        System.out.println("\n--- DRIVER MENU ---");
        System.out.println("1. Run Fatigue Risk Report (Auto-blocks drivers)");
        System.out.println("2. View Inactive Drivers (Current Month)");
        System.out.print("Select an option: ");

        int opt = Integer.parseInt(scanner.nextLine());

        switch (opt) {
            case 1:
                List<DriverFatigueDTO> tired = driverService.getDriversWithFatigueRisk();
                tired.forEach(t -> System.out.println("Tired Driver: " + t.getName() + " " + t.getLastname() + " (" + t.getTotalMileage() + "km)"));
                break;
            case 2:
                driverService.getInactiveDriversCurrentMonth().forEach(d ->
                        System.out.println("Inactive: " + d.getName() + " " + d.getLastName())
                );
                break;
            default:
                System.out.println("⚠️ Invalid driver option.");
        }
    }

    // --- ROUTE MENU ---
    private static void routeMenu(Scanner scanner) {
        System.out.println("\n--- ROUTE MENU ---");
        System.out.println("1. View All Routes");
        System.out.println("2. Efficiency Report");
        System.out.print("Select an option: ");

        int opt = Integer.parseInt(scanner.nextLine());

        switch (opt) {
            case 1:
                routeService.getAllRoutes().forEach(r ->
                        System.out.println("Route ID " + r.getId_route() + ": " + r.getOrigin() + " -> " + r.getDestination() + " (" + r.getDistance() + "km)")
                );
                break;
            case 2:
                List<EfficiencyReportDTO> efficiency = routeService.getEfficiencyReport();
                efficiency.forEach(e -> System.out.println(e.toString()));
                break;
            default:
                System.out.println("⚠️ Invalid route option.");
        }
    }
}