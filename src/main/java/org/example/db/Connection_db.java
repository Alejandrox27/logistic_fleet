package org.example.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Connection_db {

    private static final String URL = "jdbc:mysql://localhost:3306/logistics_fleet_db";
    private static final String USER = "root";

    private static String PASS;

    // BLOQUE ESTÁTICO: Se ejecuta automáticamente 1 sola vez al cargar la clase
    static {
        Properties properties = new Properties();

        try (FileInputStream archivoInput = new FileInputStream("config.properties")) {
            properties.load(archivoInput);

            // Se llena la variable estática con lo que leemos del archivo config.properties
            PASS = properties.getProperty("db.PASS");

        } catch (IOException e) {
            System.out.println("Error: No se encontró el archivo config.properties.");
            System.out.println("Por favor, crea uno basado en config.properties.example");

            throw new RuntimeException("Error fatal: No se pudieron cargar las credenciales de la base de datos.", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        /*
        * Método para obtener la conexión a la base de datos
        * usando la URL, el usuario (root), y la contraseña
        * alojada en un archivo config.properties
        * */
        return DriverManager.getConnection(URL, USER, PASS);
    }
}