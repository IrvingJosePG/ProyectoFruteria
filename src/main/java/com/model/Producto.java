package com.model;

public class Producto {
    
    private int codigo;
    private String descripcion;
    private String categoria;
    private String unidad_medida;
    private int exitencia;
    private double precio_c;
    private double precio_v;
    private boolean esVendible;
    
    public Producto(int codigo, String descripcion, String categoria, String unidad_medida, int exitencia, double precio_c, double precio_v, boolean esVendible) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.unidad_medida = unidad_medida;
        this.exitencia = exitencia;
        this.precio_c = precio_c;
        this.precio_v = precio_v;
        this.esVendible = esVendible;
    }
    // Constructor vacío (necesario)
    public Producto() {
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getUnidad_medida() {
        return unidad_medida;
    }

    public void setUnidad_medida(String unidad_medida) {
        this.unidad_medida = unidad_medida;
    }

    public int getExitencia() {
        return exitencia;
    }

    public void setExitencia(int exitencia) {
        this.exitencia = exitencia;
    }

    public double getPrecio_c() {
        return precio_c;
    }

    public void setPrecio_c(double precio_c) {
        this.precio_c = precio_c;
    }

    public double getPrecio_v() {
        return precio_v;
    }

    public void setPrecio_v(double precio_v) {
        this.precio_v = precio_v;
    }

    public boolean isEsVendible() {
        return esVendible;
    }

    public void setEsVendible(boolean esVendible) {
        this.esVendible = esVendible;
    } 
    
}