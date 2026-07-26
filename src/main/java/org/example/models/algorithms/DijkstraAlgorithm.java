package org.example.models.algorithms;

import org.example.models.graphs.City;
import org.example.models.graphs.CityGraph;
import org.example.models.graphs.Road;

import java.util.*;

public class DijkstraAlgorithm {

    public record RouteResult(List<City> path, double totalDistance) {
        public boolean found() { return !path.isEmpty(); }
    }

    /**
     * Calcula la ruta más corta entre dos ciudades usando el algoritmo de Dijkstra.
     *
     * Complejidad: O((|V| + |E|) log |V|) gracias a la PriorityQueue.
     *
     * @param cityGraph   El grafo ponderado con las ciudades y carreteras
     * @param origin      Ciudad de origen
     * @param destination Ciudad de destino
     * @return RouteResult con el camino (lista de ciudades) y la distancia total
     */
    public RouteResult calculateRoute(CityGraph cityGraph, City origin, City destination) {
        Map<City, List<Road>> adjList = cityGraph.getAdjacencyList();

        // distances[ciudad] = la menor distancia conocida desde el origen hasta esa ciudad.
        // Al inicio, todo está en infinito (aún no hemos descubierto ningún camino).
        Map<City, Double> distances = new HashMap<>();

        // previous[ciudad] = "¿Desde qué ciudad llegué aquí con la distancia mínima?"
        // Esto sirve para RECONSTRUIR el camino al final.
        Map<City, City> previous = new HashMap<>();

        // Conjunto de ciudades ya "cerradas": una vez que una ciudad entra aquí,
        // su distancia ya es la ÓPTIMA y no se vuelve a procesar.
        Set<City> visited = new HashSet<>();

        // Inicializamos todas las distancias a infinito
        for (City city : adjList.keySet()) {
            distances.put(city, Double.MAX_VALUE);
        }

        // La distancia del origen a sí mismo es 0
        distances.put(origin, 0.0);

        // Cada elemento es un arreglo {distanciaAcumulada, idCiudad}.
        // La cola siempre nos devuelve PRIMERO la ciudad con menor distancia.
        // Esto es lo que hace al algoritmo "greedy" (voraz): en cada paso
        // procesamos la ciudad más prometedora.
        PriorityQueue<double[]> pq = new PriorityQueue<>(Comparator.comparingDouble(a -> a[0]));
        pq.offer(new double[]{0.0, origin.getIdCity()});

        // Mapa auxiliar para buscar un objeto City por su ID numérico.
        // Necesario porque la PriorityQueue almacena el ID como double.
        Map<Integer, City> cityById = new HashMap<>();
        for (City c : adjList.keySet()) {
            cityById.put(c.getIdCity(), c);
        }

        // Mientras haya ciudades pendientes por procesar...
        while (!pq.isEmpty()) {

            // Extraemos la ciudad con MENOR distancia acumulada
            double[] current = pq.poll();
            double currentDist = current[0];
            City currentCity = cityById.get((int) current[1]);

            // Protección: si la ciudad no existe en el mapa o ya la visitamos, saltamos.
            // Puede pasar que una misma ciudad entre varias veces a la cola
            // (cuando se relaja más de una vez). La primera vez que la sacamos
            // es con la distancia óptima; las siguientes se ignoran aquí.
            if (currentCity == null || visited.contains(currentCity)) {
                continue;
            }

            // Marcamos la ciudad como visitada (su distancia ya es la definitiva)
            visited.add(currentCity);

            // Si ya llegamos al destino, podemos parar.
            // No hay camino más corto posible porque la cola nos dio el mínimo.
            if (currentCity.equals(destination)) {
                break;
            }

            // Para cada carretera que sale de la ciudad actual...
            //
            //   d(vecino) = min( d(vecino),  d(actual) + w(actual, vecino) )
            //
            // "Si puedo llegar al vecino pasando por la ciudad actual
            //  con MENOR distancia que la que ya tenía, ACTUALIZO."

            List<Road> roads = adjList.getOrDefault(currentCity, List.of());

            for (Road road : roads) {
                City neighbor = road.getDestination();

                // Si el vecino ya fue visitado, su distancia ya es óptima, no hay nada que mejorar
                if (visited.contains(neighbor)) {
                    continue;
                }

                // Calculamos la nueva distancia pasando por la ciudad actual
                double newDist = currentDist + road.getDistance();

                // ¿Es mejor que la distancia que ya teníamos?
                if (newDist < distances.getOrDefault(neighbor, Double.MAX_VALUE)) {
                    // ¡Sí! Encontramos un camino más corto. Actualizamos.
                    distances.put(neighbor, newDist);
                    previous.put(neighbor, currentCity);

                    // Agregamos el vecino a la cola con su nueva distancia
                    // para que sea procesado en el futuro
                    pq.offer(new double[]{newDist, neighbor.getIdCity()});
                }
            }
        }

        // Si la distancia al destino sigue siendo infinito, no hay ruta posible.
        if (!distances.containsKey(destination) || distances.get(destination) == Double.MAX_VALUE) {
            return new RouteResult(List.of(), -1);
        }

        // Reconstruimos el camino DESDE el destino HASTA el origen
        // usando el mapa 'previous' (predecesores).
        //
        // Ejemplo: previous = {Cali→Pereira, Pereira→Armenia, Armenia→Ibagué, Ibagué→Bogotá}
        // Recorrido: Cali → Pereira → Armenia → Ibagué → Bogotá
        // Al agregar al inicio (addFirst) queda: Bogotá → Ibagué → Armenia → Pereira → Cali
        LinkedList<City> path = new LinkedList<>();
        City step = destination;
        while (step != null) {
            path.addFirst(step);        // Agregar al inicio de la lista
            step = previous.get(step);  // Ir al predecesor
        }

        return new RouteResult(path, distances.get(destination));
    }
}