package org.example.db;

import org.example.models.DeliveryVan;
import org.example.models.HeavyTruck;
import org.example.models.Maintenance;
import org.example.models.Vehicle;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VehicleDAO {
    public List<Vehicle> getAllVehiclesWithMaintenances () {
        Map<Integer, Vehicle> vehicleMap = new HashMap<>();

        String sql = "SELECT * FROM Vehicles v " +
                "LEFT JOIN maintenances m ON v.id_vehicle = m.id_vehicle";

        try (java.sql.Connection conn = Connection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int idV = rs.getInt("id_vehicle");

                Vehicle v = vehicleMap.get(idV);

                if(v == null) {
                    if (rs.getInt("load_capacity") > 3500) {
                        v = new HeavyTruck(
                                idV,
                                rs.getString("number_plate"),
                                rs.getString("brand"),
                                rs.getInt("model"),
                                rs.getInt("load_capacity"),
                                rs.getInt("mileage"),
                                rs.getInt("axles"),
                                rs.getString("fuel_type")

                        );
                        vehicleMap.put(idV, v);
                    } else {
                        v = new DeliveryVan(
                                idV,
                                rs.getString("number_plate"),
                                rs.getString("brand"),
                                rs.getInt("model"),
                                rs.getInt("load_capacity"),
                                rs.getInt("mileage"),
                                rs.getInt("axles"),
                                rs.getString("fuel_type")
                        );
                        vehicleMap.put(idV, v);
                    }
                }

                int idM = rs.getInt("id_maintenance");

                if (idM > 0) {
                    Maintenance m = new Maintenance(
                            idM,
                            rs.getDate("date").toLocalDate(),
                            rs.getString("description"),
                            rs.getDouble("cost"),
                            v
                    );

                    v.addMaintenance(m);
                }

            }
        } catch (SQLException e) {
            System.out.println("Something went wrong: " + e);
        }

        return new ArrayList<>(vehicleMap.values());
    }
}
