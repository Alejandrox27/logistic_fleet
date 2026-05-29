package org.example.models.views;

import org.example.models.graphs.City;
import org.example.models.graphs.CityGraph;
import org.example.models.graphs.Road;

// dependencias para grafos
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.List;
import java.util.Map;

public class RouteMapVisualizer {
    public void drawGraph (CityGraph cityGraph) {
        //Usar swing para la ventana
        System.setProperty("org.graphstream.ui", "swing");

        //Crear el grafo visual
        Graph graph = new SingleGraph("Logistic Map");

        //Estilo con CSS
        graph.setAttribute("ui.stylesheet",
                "node { fill-color: #3498db; size: 20px; text-size: 16px; text-color: #2c3e50; text-alignment: under; }" +
                        "edge { fill-color: #7f8c8d; text-size: 14px; text-color: #c0392b; }"
        );
        graph.setAttribute("ui.antialias");

        //Usar los vertices (Ciudades) como los nodos del grafo
        Map<City, List<Road>> adjList = cityGraph.getAdjacencyList();
        for(City city : adjList.keySet()) {
            //Crear el nodo visual colocando el nombre de la ciudad como etiqueta y el id al nodo
            org.graphstream.graph.Node n = graph.addNode(String.valueOf(city.getIdCity()));
            n.setAttribute("ui.label", city.getName());
        }

        //Usar como aristas las carreteras con su destino y sus distancias
        for (Map.Entry<City, List<Road>> entry : adjList.entrySet()) {
            City origin = entry.getKey();

            for (Road road : entry.getValue()) {
                String edgeId = origin.getIdCity() + "-" + road.getDestination().getIdCity();

                //Evitar duplicados
                if (graph.getEdge(edgeId) == null && graph.getEdge(road.getDestination().getIdCity() + "-" + origin.getIdCity()) == null) {
                    // addEdge(id, origen, destino, dirigido?)
                    org.graphstream.graph.Edge e = graph.addEdge(edgeId,
                            String.valueOf(origin.getIdCity()),
                            String.valueOf(road.getDestination().getIdCity()),
                            false
                            );

                    e.setAttribute("ui.label", road.getDistance() + " km");
                }
            }
        }

        graph.display();
    }
}
