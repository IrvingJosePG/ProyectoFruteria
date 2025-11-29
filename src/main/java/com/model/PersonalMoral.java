package com.model;

public class PersonalMoral {

    private int id_c;
    private String razon_social;
    
    public PersonalMoral(int id_c, String razon_social) {
        this.id_c = id_c;
        this.razon_social = razon_social;
    }
    
    public int getId_c() {
            return id_c;
    }

    public void setId_c(int id_c) {
            this.id_c = id_c;
    }

    public String getRazon_social() {
            return razon_social;
    }

    public void setRazon_social(String razon_social) {
            this.razon_social = razon_social;
    }
}
    
