package com.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static Conexion instance;

     private static final String DB_URL = "jdbc:postgresql://localhost:5432/proyectobd_fruteria";
    // Nota: El usuario/contraseña por defecto (postgres/12345678) ya no se usarán para la autenticación, 
    // pero pueden ser útiles para otras conexiones de administración si es necesario.

    // Constructor privado, no inicializa una conexión aquí
    private Conexion() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found.");
            e.printStackTrace();
        }
    }

    public static Conexion getInstance() {
            if (instance == null) {
                synchronized (Conexion.class) {
                    if (instance == null) {
                        instance = new Conexion();
                    }
                }
            }
            return instance;
        }

    // Método para conexiones estáticas (ej. para cargar tablas al inicio)
    // Puedes mantenerlo si usas un usuario administrador genérico para cargar datos.
    public Connection getConnection() throws SQLException {
        // Usa las credenciales de administración si es necesario, si no, puedes eliminar este método.
        return DriverManager.getConnection(DB_URL, "postgres", "12345678"); 
    }

    public Connection getConnection(String user, String password) throws SQLException {
        return DriverManager.getConnection(DB_URL, user, password);
    }
}