package com.model;

public class Proveedor {
    
    private int id_p;
    private String nombre;
    private String ciudad;
    private String contacto;
    private String tel_contacto;

    public Proveedor(int id_p, String nombre, String ciudad, String contacto, String tel_contacto) {
        this.id_p = id_p;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.contacto = contacto;
        this.tel_contacto = tel_contacto;
    }
    
    // Constructor vacío (necesario para algunas operaciones)
    public Proveedor() {}

    public int getId_p() {
        return id_p;
    }

    public void setId_p(int id_p) {
        this.id_p = id_p;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getTel_contacto() {
        return tel_contacto;
    }

    public void setTel_contacto(String tel_contacto) {
        this.tel_contacto = tel_contacto;
    }
    
    // 💡 Método CRUCIAL para JComboBox:
    // Este método define qué texto se mostrará en el JComboBox.
    @Override
    public String toString() {
        return id_p + " , " + nombre;
    }
}
