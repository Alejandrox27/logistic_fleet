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
import org.example.models.algorithms.DijkstraAlgorithm;
import org.example.models.dto.DriverFatigueDTO;
import org.example.models.dto.EfficiencyReportDTO;
import org.example.models.graphs.City;
import org.example.models.graphs.CityGraph;
import org.example.services.RouteService;

import java.util.List;
import java.util.Scanner;

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

    // --- MÉTODO PARA ENCONTRAR LA RUTA MÁS CORTA (DIJKSTRA) ---
    @Override
    public void findShortestRoute(Scanner scanner) {
        System.out.println("\n🛣️ SHORTEST ROUTE FINDER (Dijkstra Algorithm)");
        System.out.println("================================================");

        // 1. Cargar el grafo desde la base de datos
        CityGraph cityGraph;
        try {
            cityGraph = loadDbCityRoads();
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            return;
        }

        // 2. Mostrar las ciudades disponibles en el grafo
        List<City> cities = cityGraph.getCities();
        System.out.println("\n📍 Available cities (" + cities.size() + "):");
        cities.stream()
                .map(City::getName)
                .sorted()
                .forEach(name -> System.out.println("   • " + name));

        // 3. Pedir origen y destino al usuario
        System.out.print("\nEnter ORIGIN city: ");
        String originName = scanner.nextLine().trim();
        System.out.print("Enter DESTINATION city: ");
        String destName = scanner.nextLine().trim();

        // 4. Buscar las ciudades en el grafo (comparación sin distinguir mayúsculas)
        City origin = null;
        City destination = null;
        for (City c : cities) {
            if (c.getName().equalsIgnoreCase(originName)) {
                origin = c;
            }
            if (c.getName().equalsIgnoreCase(destName)) {
                destination = c;
            }
        }

        if (origin == null) {
            System.out.println("❌ Origin city '" + originName + "' not found in the graph.");
            return;
        }
        if (destination == null) {
            System.out.println("❌ Destination city '" + destName + "' not found in the graph.");
            return;
        }
        if (origin.equals(destination)) {
            System.out.println("⚠️ Origin and destination are the same city!");
            return;
        }

        // 5. Ejecutar Dijkstra
        System.out.println("\n⏳ Running Dijkstra's algorithm...");
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm();
        DijkstraAlgorithm.RouteResult result = dijkstra.calculateRoute(cityGraph, origin, destination);

        // 6. Mostrar resultados
        if (!result.found()) {
            System.out.println("❌ No route found from " + origin.getName() + " to " + destination.getName() + ".");
            return;
        }

        System.out.println("\n✅ SHORTEST ROUTE FOUND!");
        System.out.println("════════════════════════════════════════════");
        System.out.println("📏 Total distance: " + result.totalDistance() + " km");
        System.out.println("🔢 Number of stops: " + (result.path().size() - 1));
        System.out.println("\n🗺️ Route:");

        List<City> path = result.path();
        for (int i = 0; i < path.size(); i++) {
            if (i == 0) {
                System.out.println("   🟢 " + path.get(i).getName() + " (START)");
            } else if (i == path.size() - 1) {
                System.out.println("   🔴 " + path.get(i).getName() + " (END)");
            } else {
                System.out.println("   ⬇️  " + path.get(i).getName());
            }
        }
        System.out.println("════════════════════════════════════════════");
    }

    // --- METODO PARA CARGAR CityGraph DESDE LA BASE DE DATOS ---
    @Override
    public CityGraph loadDbCityRoads () throws Exception {
        RouteService routeService = new RouteService();
        List<Route> dbRoutes = routeService.getAllRoutes();

        if (dbRoutes.isEmpty()) {
            throw new Exception("La base de datos esta vacia, no tiene datos de rutas");
        }

        // 2. Instanciamos nuestro modelo matemático del Grafo
        CityGraph cityGraph = new CityGraph();

        // 3. Estructura auxiliar para no duplicar Ciudades y asignarles un ID único
        // Mapea el nombre de la ciudad (String) -> Al objeto Ciudad (City)
        java.util.Map<String, City> uniqueCities = new java.util.HashMap<>();
        int cityIdCounter = 0;

        // 4. Recorremos las rutas de la base de datos para armar el Grafo
        for (Route route : dbRoutes) {
            String originName = route.getOrigin();
            String destName = route.getDestination();
            double distance = route.getDistance();
            int routeId = route.getId_route();

            // Si la ciudad origen no ha sido creada en memoria, la creamos y le asignamos un ID
            if (!uniqueCities.containsKey(originName)) {
                uniqueCities.put(originName, new City(cityIdCounter++, originName));
            }
            // Lo mismo para la ciudad destino
            if (!uniqueCities.containsKey(destName)) {
                uniqueCities.put(destName, new City(cityIdCounter++, destName));
            }

            // Obtenemos los objetos City correspondientes
            City originCity = uniqueCities.get(originName);
            City destinationCity = uniqueCities.get(destName);

            // Agregamos la conexión bidireccional (Ida y Vuelta) al Grafo Matemático
            // Usamos el id de la ruta para identificar la arista
            cityGraph.addTwoWayRoad(originCity, destinationCity, distance, routeId, routeId + 1000);
        }

        return cityGraph;
    }
}
