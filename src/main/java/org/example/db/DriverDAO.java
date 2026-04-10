package org.example.db;

import org.example.models.Driver;
import org.example.models.DriverLicense;
import org.example.models.DriverStatus;
import org.example.models.VehicleStatus;
import org.example.models.dto.DriverFatigueDTO;

import java.sql.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DriverDAO {
    public static List<Driver> getAllDriversWithLicense () {
        Map<Integer, Driver> driverMap = new HashMap<>();

        String sql = "SELECT * FROM drivers d " +
                "LEFT JOIN driver_licenses dl ON d.id_driver = dl.id_driver " +
                "LEFT JOIN license_categories lc ON dl.id_category = lc.id_category";

        try (Connection conn = Connection_db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)
        ) {

            while (rs.next()) {
                int idDriver = rs.getInt("id_driver");

                Driver d = driverMap.get(idDriver);

                if (d == null) {
                     d = new Driver(
                             idDriver,
                             rs.getInt("num_identification"),
                             rs.getString("name"),
                             rs.getString("lastname"),
                             rs.getString("second_lastname"),
                             rs.getDate("contratation_date").toLocalDate(),
                             DriverStatus.valueOf(rs.getString("status"))
                     );

                     driverMap.put(idDriver, d);
                }

                int idLicense = rs.getInt("id_license");

                if (idLicense > 0) {
                    DriverLicense driverLicense = new DriverLicense(
                            idLicense,
                            rs.getDate("issue_date").toLocalDate(),
                            rs.getDate("expiry_date").toLocalDate(),
                            rs.getString("description"),
                            rs.getString("category_name"),
                            d
                    );

                    d.addLicense(driverLicense);
                }
            }

        } catch (SQLException e) {
            System.out.println("Ocurred an error: " + e.getMessage());
        }

        return new ArrayList<>(driverMap.values());
    }

    public static Driver getDriverById(int id_driver) {
        Driver driver = null;

        String sql = "SELECT * FROM drivers d " +
                "LEFT JOIN driver_licenses dl ON d.id_driver = dl.id_driver " +
                "LEFT JOIN license_categories lc ON dl.id_category = lc.id_category " +
                "WHERE d.id_driver = ?";
        try (Connection conn = Connection_db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id_driver);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    if (driver == null) {
                        driver = new Driver(
                                rs.getInt("id_driver"),
                                rs.getInt("num_identification"),
                                rs.getString("name"),
                                rs.getString("lastname"),
                                rs.getString("second_lastname"),
                                rs.getDate("contratation_date").toLocalDate(),
                                DriverStatus.valueOf(rs.getString("status"))

                        );
                    }
                    int idLicense = rs.getInt("id_license");

                    if (idLicense > 0) {
                        DriverLicense driverLicense = new DriverLicense(
                                idLicense,
                                rs.getDate("issue_date").toLocalDate(),
                                rs.getDate("expiry_date").toLocalDate(),
                                rs.getString("description"),
                                rs.getString("category_name"),
                                driver
                        );

                        driver.addLicense(driverLicense);
                    }

                }

            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return driver;
    }

    public static List<DriverFatigueDTO> getReportDriverFatigue () {
        List<DriverFatigueDTO> DriversReportList = new ArrayList<>();

        /*sql driver fatigue*/
        String sql = "SELECT d.id_driver, d.name, d.lastname, SUM(r.distance) AS total_mileage " +
                "FROM drivers d " +
                "INNER JOIN routes r ON d.id_driver = r.id_driver " +
                "GROUP BY d.id_driver, d.name, d.lastname " +
                "HAVING total_mileage > 2000 " +
                "ORDER BY total_mileage DESC";

        try (Connection conn = Connection_db.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)
        ) {

            while (rs.next()) {
                DriversReportList.add(new DriverFatigueDTO(
                        rs.getInt("id_driver"),
                        rs.getString("name"),
                        rs.getString("lastname"),
                        rs.getDouble("total_mileage")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return DriversReportList;
    }

    public static DriverFatigueDTO getReportDriverFatigue (int id_driver) {
        DriverFatigueDTO driver = null;

        /*sql driver fatigue*/
        String sql = "SELECT d.id_driver, d.name, d.lastname, SUM(r.distance) AS total_mileage " +
                "FROM drivers d " +
                "INNER JOIN routes r ON d.id_driver = r.id_driver " +
                "WHERE d.id_driver = ? " +
                "GROUP BY d.id_driver, d.name, d.lastname " +
                "HAVING total_mileage > 2000";

        try (Connection conn = Connection_db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id_driver);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    driver = new DriverFatigueDTO(
                            rs.getInt("id_driver"),
                            rs.getString("name"),
                            rs.getString("lastname"),
                            rs.getDouble("total_mileage")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return driver;
    }

    public static List<Driver> GetDriversNotActiveMonth () {
        List<Driver> driverList = new ArrayList<>();

        String sql = "SELECT *\n" +
                "FROM drivers d\n" +
                "WHERE d.id_driver NOT IN (\n" +
                "\tSELECT id_driver\n" +
                "    FROM routes\n" +
                "    WHERE travel_date BETWEEN ? AND ?\n" +
                ")";

        LocalDate firstDay = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDay = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());

        try (Connection conn = Connection_db.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, java.sql.Date.valueOf(firstDay));
            pstmt.setDate(2, java.sql.Date.valueOf(lastDay));

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    driverList.add(new Driver(
                            rs.getInt("id_driver"),
                            rs.getInt("num_identification"),
                            rs.getString("name"),
                            rs.getString("lastname"),
                            rs.getString("second_lastname"),
                            rs.getDate("contratation_date").toLocalDate(),
                            DriverStatus.valueOf(rs.getString("status"))

                    ));
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return driverList;
    }

    public static List<Driver> GetDriversNotActiveMonth (int month, int year) {
        List<Driver> driverList = new ArrayList<>();

        String sql = "SELECT *\n" +
                "FROM drivers d\n" +
                "WHERE d.id_driver NOT IN (\n" +
                "\tSELECT id_driver\n" +
                "    FROM routes\n" +
                "    WHERE travel_date BETWEEN ? AND ?\n" +
                ")";

        LocalDate firstDay = YearMonth.of(year, month).atDay(1);
        LocalDate lastDay = firstDay.with(TemporalAdjusters.lastDayOfMonth());


        try (Connection conn = Connection_db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, java.sql.Date.valueOf(firstDay));
            pstmt.setDate(2, java.sql.Date.valueOf(lastDay));

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    driverList.add(new Driver(
                            rs.getInt("id_driver"),
                            rs.getInt("num_identification"),
                            rs.getString("name"),
                            rs.getString("lastname"),
                            rs.getString("second_lastname"),
                            rs.getDate("contratation_date").toLocalDate(),
                            DriverStatus.valueOf(rs.getString("status"))

                    ));
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return driverList;
    }

    public static DriverStatus checkDisponibility (int id_driver) {
        DriverStatus status = null;

        String sql = "SELECT d.status " +
                "FROM drivers d " +
                "WHERE d.id_driver = ?";

        try (Connection conn = Connection_db.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id_driver);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String statusStr = rs.getString("status");
                    if (statusStr != null) {
                        status = DriverStatus.valueOf(statusStr);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return status;
    }

    public static void updateStatus(int id_driver, DriverStatus status) {
        String sql = "UPDATE drivers " +
                "SET status = ? " +
                "WHERE id_driver = ?";

        try (Connection conn = Connection_db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status.name());
            pstmt.setInt(2, id_driver);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ Driver status updated to: " + status);
            } else {
                System.out.println("⚠️ No driver found with ID: " + id_driver);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error updating driver status: " + e.getMessage());
        }
    }
}
