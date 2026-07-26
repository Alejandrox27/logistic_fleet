package org.example.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Connection_db {

    private static final String URL;
    private static final String USER;
    private static final String PASS;

    /*
     * BLOQUE ESTÁTICO: Se ejecuta automáticamente 1 sola vez al cargar la clase.
     *
     * Estrategia de configuración (en orden de prioridad):
     *   1. Variables de entorno del sistema (Docker / CI / producción)
     *   2. Archivo config.properties (desarrollo local)
     *
     * Variables de entorno reconocidas:
     *   DB_HOST     → host de la base de datos   (default: localhost)
     *   DB_PORT     → puerto                      (default: 3306)
     *   DB_NAME     → nombre de la base de datos  (default: logistics_fleet_db)
     *   DB_USER     → usuario de MySQL            (default: root)
     *   DB_PASS     → contraseña de MySQL
     */
    static {
        // ── 1. Intentar leer variables de entorno ──────────────────────────────
        String envHost  = System.getenv("DB_HOST");
        String envPort  = System.getenv("DB_PORT");
        String envName  = System.getenv("DB_NAME");
        String envUser  = System.getenv("DB_USER");
        String envPass  = System.getenv("DB_PASS");

        if (envHost != null && envPass != null) {
            // Configuración Docker / entorno externo
            String host = envHost;
            String port = (envPort != null) ? envPort : "3306";
            String name = (envName != null) ? envName : "logistics_fleet_db";
            URL  = "jdbc:mysql://" + host + ":" + port + "/" + name
                   + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
            USER = (envUser != null) ? envUser : "root";
            PASS = envPass;

        } else {
            // ── 2. Caer en config.properties (desarrollo local) ────────────────
            Properties properties = new Properties();
            String urlFromFile  = null;
            String userFromFile = null;
            String passFromFile = null;

            try (FileInputStream archivoInput = new FileInputStream("config.properties")) {
                properties.load(archivoInput);

                passFromFile = properties.getProperty("db.PASS");
                // Soporte opcional de host/user en config.properties
                String host = properties.getProperty("db.HOST", "localhost");
                String port = properties.getProperty("db.PORT", "3306");
                String name = properties.getProperty("db.NAME", "logistics_fleet_db");
                userFromFile = properties.getProperty("db.USER", "root");

                urlFromFile = "jdbc:mysql://" + host + ":" + port + "/" + name
                              + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

            } catch (IOException e) {
                System.out.println("Error: No se encontró el archivo config.properties.");
                System.out.println("Por favor, crea uno basado en config.properties.example");
                throw new RuntimeException(
                    "Error fatal: No se pudieron cargar las credenciales de la base de datos.", e);
            }

            URL  = urlFromFile;
            USER = userFromFile;
            PASS = passFromFile;
        }
    }

    public static Connection getConnection() throws SQLException {
        /*
         * Método para obtener la conexión a la base de datos
         * usando la URL, usuario y contraseña resueltos
         * en el bloque estático de inicialización.
         */
        return DriverManager.getConnection(URL, USER, PASS);
    }
}