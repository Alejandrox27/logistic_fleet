package org.example.db;

import org.example.models.Driver;
import org.example.models.DriverLicense;
import org.example.models.dto.DriverFatigueDTO;

import java.sql.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DriverDAO {
    public List<Driver> getAllDriversWithLicense () {
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
                             rs.getDate("contratation_date").toLocalDate()
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

    public Driver getDriverById(int id_driver) {
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
                                rs.getDate("contratation_date").toLocalDate()
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

    public List<DriverFatigueDTO> getReportDriverFatigue () {
        List<DriverFatigueDTO> DriversReportList = new ArrayList<>();

        /*sql driver fatigue*/
        String sql = "SELECT d.id_driver, d.name, d.lastname, SUM(r.distance) AS total_mileage " +
                "FROM drivers d " +
                "INNER JOIN routes r ON d.id_driver = r.id_driver " +
                "GROUP BY d.id_driver " +
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
}
