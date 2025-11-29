package com.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static Conexion instance;

    // Puedes poner aquí la configuración de la base de datos
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/proyectobd_fruteria";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "12345678";

    // Constructor privado, no inicializa una conexión aquí
    private Conexion() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found.");
            e.printStackTrace();
        }
    }

    public static Conexion getInstance() { // Ya no lanza SQLException aquí
        if (instance == null) {
            synchronized (Conexion.class) {
                if (instance == null) {
                    instance = new Conexion();
                }
            }
        }
        return instance;
    }

    // ESTE ES EL MÉTODO CLAVE: Cada llamada a getConnection() devuelve una NUEVA conexión.
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    // No necesitas un método closeConnection() aquí, ya que cada conexión es gestionada por el llamador.
    // El llamador es responsable de cerrar la conexión que obtiene.
}