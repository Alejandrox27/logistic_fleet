package org.example.models.graphs;

public class Road {
    private int id;
    private City destination;
    private double distance;

    public Road (int id, City destination, double distance) {
        this.id = id;
        this.destination = destination;
        this.distance = distance;
    }

    public int getId() {
        return id;
    }

    public City getDestination() {
        return destination;
    }

    public double getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "-> " + destination.getName() + " (" + distance + " km)";
    }
}
