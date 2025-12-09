package com.model;

public class Cliente {
    
    private int id_c;
    private String telefono;
    private String rfc;
    private String domicilio;
    private String tipo;
    private boolean estado;

    private String nombre;
    
    private String razonSocial;

    public Cliente(int id_c, String telefono, String rfc, String domicilio, String nombre) {
        this.id_c = id_c;
        this.telefono = telefono;
        this.rfc = rfc;
        this.domicilio = domicilio;
        this.nombre = nombre;
    }
    
    // Constructor vacío (necesario para algunas operaciones)
    public Cliente() {}
    
    
    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
    
    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public int getId_c() {
        return id_c;
    }

    public void setId_c(int id_c) {
        this.id_c = id_c;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    // 💡 Método CRUCIAL para JComboBox:
    // Este método define qué texto se mostrará en el JComboBox.
    @Override
    public String toString() {
        return id_c + " , " + nombre;
    }
}