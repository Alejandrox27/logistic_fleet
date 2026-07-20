package org.example.models.algorithms;

import org.example.models.Route;
import org.example.models.graphs.City;
import org.example.models.graphs.CityGraph;
import org.example.services.RouteService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DijkstraAlgorithm {
    private static final RouteService routeService = new RouteService();

    // map of cities and their roads
    CityGraph cityGraph = new CityGraph(); //Map<City, List<Road>>

    public DijkstraAlgorithm () {
        //get all routes from database with routeService
        List<Route> dbRoutes = routeService.getAllRoutes();

        if (dbRoutes.isEmpty()) {
            System.out.println("⚠️ No routes found in the database to build a map.");
        }

        java.util.Map<String, City> uniqueCities = new java.util.HashMap<>();
        int cityIdCounter = 0;

        // Load routes from dbRoutes
        for (Route route : dbRoutes) {
            String originName = route.getOrigin();
            String destName = route.getDestination();
            double distance = route.getDistance();
            int routeId = route.getId_route();

            if (!uniqueCities.containsKey(originName)) {
                uniqueCities.put(originName, new City(cityIdCounter++, originName));
            }
            if (!uniqueCities.containsKey(destName)) {
                uniqueCities.put(destName, new City(cityIdCounter++, destName));
            }

            City originCity = uniqueCities.get(originName);
            City destinationCity = uniqueCities.get(destName);

            cityGraph.addTwoWayRoad(originCity, destinationCity, distance, routeId, routeId + 1000);
         }
    };

    public Map<Double, List<City>> getOptimalRoad (City origin, City destination) {
        Map<Double, List<City>> OptimalRoute = new HashMap<>();

        

        return OptimalRoute;
    };
}
