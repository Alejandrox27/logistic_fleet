package org.example;

import org.example.Exceptions.VehicleExceptions.VehicleException;
import org.example.models.*;
import org.example.models.dto.*;
import org.example.models.algorithms.DijkstraAlgorithm;
import org.example.models.graphs.City;
import org.example.models.graphs.CityGraph;
import org.example.models.views.RouteMapVisualizer;
import org.example.services.DriverService;
import org.example.services.RouteService;
import org.example.services.VehicleService;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {

    // Se instancian los servicios
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
    private static void vehicleMenu(Scanner scanner) throws VehicleException {
        System.out.println("\n--- VEHICLE MENU ---");
        System.out.println("1. View All Vehicles");
        System.out.println("2. Run Maintenance Risk Report (Auto-blocks vehicles)");
        System.out.println("3. Ranking: Most Wasteful Brands");
        System.out.println("4. Most Versatile Vehicle");
        System.out.println("5. Operating Costs Report");
        System.out.println("6. Create new vehicle");
        System.out.println("7. Register maintenance");
        System.out.println("8. Change vehicle status to AVAILABLE");
        System.out.println("9. Change vehicle status to MAINTENANCE REQUIRED");
        System.out.print("Select an option: ");

        int opt = Integer.parseInt(scanner.nextLine());
        int vehicleId;
        int vId;

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
            case 6:
                Vehicle newVehicle = null;
                System.out.print("Enter Plate (e.g., AAA123): ");
                String number_plate = scanner.nextLine();
                System.out.print("Enter Brand: ");
                String brand = scanner.nextLine();
                System.out.print("Enter Model: ");
                int model = Integer.parseInt(scanner.nextLine());
                System.out.print("Enter Load Capacity (kg): ");
                int capacity = Integer.parseInt(scanner.nextLine());
                System.out.print("Enter Mileage (km): ");
                int mileage = Integer.parseInt(scanner.nextLine());
                System.out.print("Enter fuel type: ");
                String fuel_type = scanner.nextLine();
                System.out.println("Enter number of axles");
                int axles = Integer.parseInt(scanner.nextLine());

                if (capacity > 3500) {
                    newVehicle = new HeavyTruck(number_plate, brand, model, capacity, mileage, axles, fuel_type, VehicleStatus.AVAILABLE);
                } else {
                    newVehicle = new DeliveryVan(number_plate, brand, model, capacity, mileage, axles, fuel_type, VehicleStatus.AVAILABLE);
                }

                try {
                        vehicleService.createVehicle(newVehicle);
                    } catch (VehicleException e) {
                    System.out.println("Error: " + e.getMessage());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                break;
            case 7:
                System.out.print("Enter Vehicle ID to register maintenance: ");
                vId = Integer.parseInt(scanner.nextLine());
                Vehicle vToUpdate = null;
                try {
                    vToUpdate = vehicleService.getVehicleById(vId);
                    vToUpdate.setId_vehicle(vId);
                } catch (VehicleException e) {
                    System.out.println(e.getMessage());
                    break;
                }


                Maintenance m = new Maintenance(null, null, 0, null);
                System.out.println("Enter Maintenance Date like this: (YYYY-MM-DD): ");
                m.setDate(LocalDate.parse(scanner.nextLine()));
                System.out.print("Enter Description: ");
                m.setDescription(scanner.nextLine());
                System.out.print("Enter Cost: ");
                m.setCost(Double.parseDouble(scanner.nextLine()));

                vehicleService.registerMaintenance(vToUpdate, m);
                System.out.println("✅ Maintenance registered successfully.");
                break;
            case 8:
                System.out.print("Enter Vehicle ID to change status to AVAILABLE: ");
                vehicleId = Integer.parseInt(scanner.nextLine());

                Vehicle vehicle = vehicleService.getVehicleById(vehicleId);
                vehicleService.changeStatus(vehicle, VehicleStatus.AVAILABLE);

                break;
            case 9:
                System.out.print("Enter Vehicle ID to change status to NEED MAINTENANCE: ");
                vehicleId = Integer.parseInt(scanner.nextLine());

                Vehicle vehicleNewStatus = vehicleService.getVehicleById(vehicleId);
                vehicleService.changeStatus(vehicleNewStatus, VehicleStatus.MAINTENANCE_REQUIRED);
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
    private static void routeMenu(Scanner scanner) throws Exception {
        System.out.println("\n--- ROUTE MENU ---");
        System.out.println("1. View All Routes");
        System.out.println("2. Efficiency Report");
        System.out.println("3. 🌐 VIEW VISUAL ROUTE MAP (GraphStream)");
        System.out.println("4. 🛣️ FIND SHORTEST ROUTE (Dijkstra Algorithm)");
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
            case 3:
                try {
                    visualizeDatabaseGraph();
                } catch (Exception e) {
                    System.out.println("error durante la carga de datos");
                }
                break;
            case 4:
                routeService.findShortestRoute(scanner);
                break;

            default:
                System.out.println("⚠️ Invalid route option.");
        }
    }

    // --- MÉTODO PARA MOSTRAR EL GRAFO ---
    private static void visualizeDatabaseGraph() throws Exception {
        System.out.println("\n🌐 Fetching routes from database and generating graph...");

        // 1. Obtenemos la lista de rutas reales de la base de datos
        List<Route> dbRoutes = routeService.getAllRoutes();

        if (dbRoutes.isEmpty()) {
            System.out.println("⚠️ No routes found in the database to build a map.");
            return;
        }

        // 2. Instanciamos nuestro modelo matemático del Grafo
        CityGraph cityGraph = new CityGraph();

        try {
            cityGraph = routeService.loadDbCityRoads();
        } catch (Exception e) {
            System.out.println("No se pudo cargar la base de datos");
        }

        // 5. ¡Llamamos a la Vista para dibujar el Grafo interactivo!
        System.out.println("🎨 Launching visual map window...");
        RouteMapVisualizer visualizer = new RouteMapVisualizer();
        visualizer.drawGraph(cityGraph);
    }


}