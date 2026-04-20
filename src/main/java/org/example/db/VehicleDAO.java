package org.example.db;

import org.example.models.*;
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
    public static List<Vehicle> getAllVehiclesWithMaintenances () {
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
                                rs.getString("fuel_type"),
                                VehicleStatus.valueOf(rs.getString("status"))

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
                                rs.getString("fuel_type"),
                                VehicleStatus.valueOf(rs.getString("status"))

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

    public static Vehicle getVehicleById (int id_vehicle) {
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
                                    rs.getString("fuel_type"),
                                    VehicleStatus.valueOf(rs.getString("status"))


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
                                    rs.getString("fuel_type"),
                                    VehicleStatus.valueOf(rs.getString("status"))

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

    public static Vehicle getVehicleByPlate(String plate) {
        Vehicle v = null;

        String sql = "SELECT v.*, m.id_maintenance, m.date AS m_date, m.description AS m_desc, m.cost AS m_cost " +
                "FROM vehicles v " +
                "LEFT JOIN maintenances m ON v.id_vehicle = m.id_vehicle " +
                "WHERE v.number_plate = ?";

        try (Connection conn = Connection_db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, plate);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    if (v == null) {
                        int idV = rs.getInt("id_vehicle");
                        int cap = rs.getInt("load_capacity");

                        if (cap > 3500) {
                            v = new HeavyTruck(idV, rs.getString("number_plate"), rs.getString("brand"),
                                    rs.getInt("model"), cap, rs.getInt("mileage"),
                                    rs.getInt("axles"), rs.getString("fuel_type"),
                                    VehicleStatus.valueOf(rs.getString("status")));
                        } else {
                            v = new DeliveryVan(idV, rs.getString("number_plate"), rs.getString("brand"),
                                    rs.getInt("model"), cap, rs.getInt("mileage"),
                                    rs.getInt("axles"), rs.getString("fuel_type"),
                                    VehicleStatus.valueOf(rs.getString("status")));
                        }
                    }

                    int idM = rs.getInt("id_maintenance");
                    if (idM > 0) {
                        java.sql.Date sqlDate = rs.getDate("m_date");
                        if (sqlDate != null) {
                            Maintenance m = new Maintenance(
                                    idM,
                                    sqlDate.toLocalDate(),
                                    rs.getString("m_desc"),
                                    rs.getDouble("m_cost"),
                                    v
                            );
                            v.addMaintenance(m);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error VehicleDAO: " + e.getMessage());
        }
        return v;
    }
    public static List<VehiclesRiskDTO> getVehiclesWithRisk () {
        List<VehiclesRiskDTO> vehiclesRiskDTOList = new ArrayList<>();

        String sql = "SELECT v.id_vehicle, v.number_plate, v.brand, " +
                "SUM(m.cost) AS total_spent, " +
                "(v.load_capacity * 0.5) AS risk_threshold " +
                "FROM vehicles v " +
                "INNER JOIN maintenances m ON v.id_vehicle = m.id_vehicle " +
                "GROUP BY v.id_vehicle " +
                "HAVING total_spent > risk_threshold";

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

    public static List<MostWastefulBrandsDTO> getRankingMostWastefulBrands () {
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

    public static VehicleVersatilityDTO getMostVersatileVehicle() {
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

    public static List<VehicleOperatingCostDTO> getReportOperatingCosts() {
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

    public static VehicleStatus checkDisponibility(int id_vehicle) {
        VehicleStatus status = null;

        String sql = "SELECT v.status " +
                "FROM vehicles v " +
                "WHERE v.id_vehicle = ?";

        try (Connection conn = Connection_db.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id_vehicle);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String statusStr = rs.getString("status");

                    if (statusStr != null) {
                        status = VehicleStatus.valueOf(statusStr);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return status;
    }

    public static void updateStatus(int id_vehicle, VehicleStatus status) {
        // Corregimos id_driver por id_vehicle para que apunte a la tabla correcta
        String sql = "UPDATE vehicles SET status = ? WHERE id_vehicle = ?";

        try (Connection conn = Connection_db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status.name());
            pstmt.setInt(2, id_vehicle);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ Status updated to " + status + " for vehicle ID: " + id_vehicle);
            } else {
                System.out.println("⚠️ No vehicle found with ID: " + id_vehicle);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error updating vehicle status: " + e.getMessage());
        }
    }

    public static void saveVehicle(Vehicle vehicle) {
        String sql = "INSERT INTO vehicles (number_plate, brand, model, load_capacity, mileage, axles, fuel_type, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Connection_db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, vehicle.getNumber_plate());
            pstmt.setString(2, vehicle.getBrand());
            pstmt.setInt(3, vehicle.getModel());
            pstmt.setInt(4, vehicle.getLoad_capacity());
            pstmt.setInt(5, vehicle.getMileage());
            pstmt.setInt(6, vehicle.getAxles());
            pstmt.setString(7, vehicle.getFuelType());
            pstmt.setString(8, vehicle.getStatus().name());

            pstmt.executeUpdate();
            System.out.println("Vehicle saved successfully in the database.");

        } catch (SQLException e) {
            System.err.println("Error saving vehicle: " + e.getMessage());
        }
    }

}
