package org.example.db;

import org.example.models.*;
import org.example.models.Driver;
import org.example.models.dto.EfficiencyReportDTO;

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

    public List<EfficiencyReportDTO> getEfficiencyReport () {
        List<EfficiencyReportDTO> reportDTOList = new ArrayList<>();
        String sql = "SELECT d.name, " +
                "v.brand, " +
                "v.model, " +
                "AVG(r.fuel_consumed / r.distance) AS efficiency_average, " +
                "COUNT(r.id_route) AS total_routes " +
                "FROM routes r " +
                "INNER JOIN vehicles v ON r.id_vehicle = v.id_vehicle " +
                "INNER JOIN drivers d ON r.id_driver = d.id_driver " +
                "GROUP BY d.id_driver, v.id_vehicle " +
                "HAVING total_routes > 5";

        try (Connection conn = Connection_db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                reportDTOList.add(new EfficiencyReportDTO(
                        rs.getString("name"),
                        rs.getString("brand"),
                        rs.getInt("model"),
                        rs.getDouble("efficiency_average"),
                        rs.getInt("total_routes")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return reportDTOList;
    }

    public void saveRoute(Route route) {
        String sql = "  INSERT INTO route (origin, destination, distance, fuel_consumed, travel_date, id_vehicle, id_driver) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Connection_db.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, route.getOrigin());
            pstmt.setString(2, route.getDestination());
            pstmt.setDouble(3, route.getDistance());
            pstmt.setDouble(4, route.getFuelConsumed());
            pstmt.setDate(5, java.sql.Date.valueOf(route.getTravelDate()));
            pstmt.setInt(6, route.getVehicle().getIdVehicle());
            pstmt.setInt(7, route.getDriver().getIdDriver());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
