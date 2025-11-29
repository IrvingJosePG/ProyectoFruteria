package com.model;

public class Usuario {
    
    private int idEmpleado;
    private String nombreEmpleado;
    private int idRol;
    private String nombreRol; // Ej: "Administrador" o "Vendedor"

    // Constructor
    public Usuario(int idEmpleado, String nombreEmpleado, int idRol, String nombreRol) {
        this.idEmpleado = idEmpleado;
        this.nombreEmpleado = nombreEmpleado;
        this.idRol = idRol;
        this.nombreRol = nombreRol;
    }

    // --- Getters ---
    // NO se necesitan Setters para este modelo después de la autenticación
    
    public int getIdEmpleado() {
        return idEmpleado;
    }

    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public int getIdRol() {
        return idRol;
    }

    public String getNombreRol() {
        return nombreRol;
    }
}
