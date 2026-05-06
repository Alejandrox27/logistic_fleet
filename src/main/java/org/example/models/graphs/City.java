package org.example.models.graphs;

import java.util.Objects;

public class City {
    private int idCity;
    private String name;

    public City(int idCity, String name) {
        this.idCity = idCity;
        this.name = name;
    }

    public int getIdCity() {
        return idCity;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        // 1. Si son exactamente el mismo objeto en memoria, son iguales (Reflexividad)
        if (this == o) return true;

        // 2. Si el otro objeto es nulo o ni siquiera es una Ciudad, no son iguales
        if (o == null || getClass() != o.getClass()) return false;

        // 3. Convertimos el objeto genérico a 'City'
        City city = (City) o;

        // 4. Dos ciudades son la misma SI Y SOLO SI tienen el mismo ID
        return idCity == city.getIdCity();
    }

    @Override
    public int hashCode() {
        // Genera un código numérico único basado en el ID de la ciudad
        return Objects.hash(idCity);
    }

    @Override
    public String toString() {
        return name;
    }
}
