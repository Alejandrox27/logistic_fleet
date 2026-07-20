package org.example.models.graphs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CityGraph {
    private Map<City, List<Road>> adjacencyList;

    public CityGraph () {
        this.adjacencyList = new HashMap<>();
    }

    public void addCity (City city) {
        adjacencyList.putIfAbsent(city, new ArrayList<>());
    }

    public void addRoad(City source, City destination, double distance, int id_road) {
        addCity(source);
        addCity(destination);

        Road road = new Road(id_road, destination, distance);

        adjacencyList.get(source).add(road);
    }

    public void addTwoWayRoad(City cityA, City cityB, double distance, int idRoadA, int idRoadB) {
        // para rutas que sean de ida y regreso (normalmente siempre sera asi)
        addRoad(cityA, cityB, distance, idRoadA);
        addRoad(cityB, cityA, distance, idRoadB);
    }

    public List<City> getCities () {
        return new ArrayList<>(adjacencyList.keySet());
    }

    //GETTER
    public Map<City, List<Road>> getAdjacencyList() {
        return adjacencyList;
    }
}
