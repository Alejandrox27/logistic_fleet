package org.example.models.algorithms;

import org.example.models.graphs.City;
import org.example.models.graphs.CityGraph;
import org.example.models.graphs.Road;

import java.util.List;

public class DijkstraAlgorithm {

    public record RouteResult(List<City> path, double totalDistance) {
        public boolean found() { return !path.isEmpty(); }
    }

    // Recibe el grafo ya construido
    public RouteResult calculateRoute(CityGraph cityGraph, City origin, City destination) {
        RouteResult result = new RouteResult(List.of(), -1);
        // lógica de Dijkstra aquí
        return result;
    }
}