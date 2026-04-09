package org.example.db;

import org.example.models.DeliveryVan;
import org.example.models.HeavyTruck;
import org.example.models.Maintenance;
import org.example.models.Vehicle;
import org.example.models.dto.MostWastefulBrandsDTO;
import org.example.models.dto.VehicleOperatingCostDTO;
import org.example.models.dto.VehicleVersatilityDTO;
import org.example.models.dto.VehiclesRiskDTO;

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

        try (Connection conn = Connection_db.getConnection();
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
            System.out.println("Something went wrong: " + e.getMessage());
        }

        return new ArrayList<>(vehicleMap.values());
    }

    public Vehicle getVehicleById (int id_vehicle) {
        Vehicle v = null;

        String sql = "SELECT * FROM vehicles v " +
                "LEFT JOIN maintenances m ON v.id_vehicle = m.id_vehicle " +
                "WHERE v.id_vehicle = ?";

        try (Connection conn = Connection_db.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id_vehicle);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int idV = rs.getInt("id_vehicle");

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
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return v;
    }

    public List<VehiclesRiskDTO> getVehiclesWithRisk () {
        List<VehiclesRiskDTO> vehiclesRiskDTOList = null;

        String sql = "SELECT d.id_driver, d.name, d.lastname, SUM(r.distance) AS total_mileage " +
                "FROM drivers d " +
                "INNER JOIN routes r ON d.id_driver = r.id_driver " +
                "GROUP BY d.id_driver " +
                "HAVING total_mileage > 2000 " +
                "ORDER BY total_mileage DESC";

        try (Connection conn = Connection_db.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                vehiclesRiskDTOList.add(new VehiclesRiskDTO(
                        rs.getInt("id_vehicle"),
                        rs.getString("number_plate"),
                        rs.getString("brand"),
                        rs.getDouble("total_spent"),
                        rs.getDouble("risk_threshold")
                        )
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return vehiclesRiskDTOList;
    }

    public List<MostWastefulBrandsDTO> getRankingMostWastefulBrands () {
        List<MostWastefulBrandsDTO> MostWastefulBrandsList = new ArrayList<>();

        /*RANKING DE MARCAS MAS GASTONAS*/
        String sql = "SELECT v.brand, ROUND(AVG(r.fuel_consumed / r.distance), 2) AS average_fuel_consumed\n" +
                "FROM vehicles v\n" +
                "INNER JOIN routes r ON  v.id_vehicle = r.id_vehicle\n" +
                "GROUP BY v.brand\n" +
                "ORDER BY average_fuel_consumed DESC";

        try (Connection conn = Connection_db.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                MostWastefulBrandsList.add(new MostWastefulBrandsDTO(
                        rs.getString("brand"),
                        rs.getDouble("average_fuel_consumed")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return MostWastefulBrandsList;
    }

    public VehicleVersatilityDTO getMostVersatileVehicle() {
        VehicleVersatilityDTO mostVersatile = null;

        String sql = "SELECT v.id_vehicle, v.brand, v.model, COUNT(DISTINCT r.destination) AS visited_places " +
                "FROM vehicles v " +
                "INNER JOIN routes r ON v.id_vehicle = r.id_vehicle " +
                "GROUP BY v.id_vehicle, v.brand, v.model " +
                "ORDER BY visited_places DESC " +
                "LIMIT 1";

        try (Connection conn = Connection_db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                mostVersatile = new VehicleVersatilityDTO(
                        rs.getInt("id_vehicle"),
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getInt("visited_places")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error en reporte de versatilidad: " + e.getMessage());
        }

        return mostVersatile;
    }

    public List<VehicleOperatingCostDTO> getReportOperatingCosts() {
        List<VehicleOperatingCostDTO> report = new ArrayList<>();

        String sql = "SELECT v.id_vehicle, v.brand, v.number_plate, " +
                "COALESCE(route_costs.total_fuel, 0) AS costs_fuel, " +
                "COALESCE(maintenances_costs.total_maintenances, 0) AS all_maintenances_costs, " +
                "(COALESCE(route_costs.total_fuel, 0) + COALESCE(maintenances_costs.total_maintenances, 0)) AS total_operating_cost " +
                "FROM vehicles v " +
                "LEFT JOIN ( " +
                "    SELECT id_vehicle, SUM(fuel_consumed * 15000) AS total_fuel " +
                "    FROM routes GROUP BY id_vehicle " +
                ") AS route_costs ON v.id_vehicle = route_costs.id_vehicle " +
                "LEFT JOIN ( " +
                "    SELECT id_vehicle, SUM(cost) AS total_maintenances " +
                "    FROM maintenances GROUP BY id_vehicle " +
                ") AS maintenances_costs ON v.id_vehicle = maintenances_costs.id_vehicle " +
                "ORDER BY total_operating_cost DESC";

        try (Connection conn = Connection_db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                report.add(new VehicleOperatingCostDTO(
                        rs.getInt("id_vehicle"),
                        rs.getString("brand"),
                        rs.getString("number_plate"),
                        rs.getDouble("costs_fuel"),
                        rs.getDouble("all_maintenances_costs"),
                        rs.getDouble("total_operating_cost")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al generar reporte de costos operativos: " + e.getMessage());
        }

        return report;
    }
}
