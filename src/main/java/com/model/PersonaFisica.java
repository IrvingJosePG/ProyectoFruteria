package com.model;

public class PersonaFisica {

    private int id_c;
    private String nombre;
    
    public PersonaFisica(int id_c, String nombre) {
        this.id_c = id_c;
        this.nombre = nombre;
    }
    
    public int getId_c() {
            return id_c;
    }

    public void setId_c(int id_c) {
            this.id_c = id_c;
    }

    public String getNombre() {
            return nombre;
    }

    public void setNombre(String nombre) {
            this.nombre = nombre;
    }
}
    