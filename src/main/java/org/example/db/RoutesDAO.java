package org.example.db;

import org.example.models.*;
import org.example.models.Driver;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoutesDAO {
    public List<Route> getAllRoutes () {
        List<Route> routesList = new ArrayList<>();

        String sql = "SELECT * FROM routes r " +
                "INNER JOIN drivers d ON r.id_driver = d.id_driver " +
                "INNER JOIN vehicles v ON r.id_vehicle = v.id_vehicle";

        try (Connection conn = Connection_db.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                Driver driver = new Driver(
                        rs.getInt("id_driver"),
                        rs.getInt("num_identification"),
                        rs.getString("name"),
                        rs.getString("lastname"),
                        rs.getString("second_lastname"),
                        rs.getDate("contratation_date").toLocalDate()
                );

                Vehicle vehicle = null;
                if (rs.getInt("load_capacity") > 3500) {
                    vehicle = new HeavyTruck(
                            rs.getInt("id_vehicle"),
                            rs.getString("number_plate"),
                            rs.getString("brand"),
                            rs.getInt("model"),
                            rs.getInt("load_capacity"),
                            rs.getInt("mileage"),
                            rs.getInt("axles"),
                            rs.getString("fuel_type")

                    );
                } else {
                    vehicle = new DeliveryVan(
                            rs.getInt("id_vehicle"),
                            rs.getString("number_plate"),
                            rs.getString("brand"),
                            rs.getInt("model"),
                            rs.getInt("load_capacity"),
                            rs.getInt("mileage"),
                            rs.getInt("axles"),
                            rs.getString("fuel_type")
                    );
                }

                Route route = new Route(
                        rs.getInt("id_route"),
                        rs.getString("origin"),
                        rs.getString("destination"),
                        rs.getInt("distance"),
                        rs.getInt("fuel_consumed"),
                        rs.getDate("travel_date").toLocalDate(),
                        vehicle,
                        driver
                );
                routesList.add(route);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return routesList;
    }
}
