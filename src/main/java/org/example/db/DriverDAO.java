package org.example.db;

import org.example.models.Driver;
import org.example.models.DriverLicense;

import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

                    d.setLicense(driverLicense);
                }
            }

        } catch (SQLException e) {
            System.out.println("Ocurred an error: " + e.getMessage());
        }

        return new ArrayList<>(driverMap.values());
    }
}
