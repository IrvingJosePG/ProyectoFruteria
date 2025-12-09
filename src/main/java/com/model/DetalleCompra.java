package com.model;

public class DetalleCompra {
    private int folio_c;
    private int codigo;
    private int cantidad;

    public DetalleCompra(int folio_c, int codigo, int cantidad) {
        this.folio_c = folio_c;
        this.codigo = codigo;
        this.cantidad = cantidad;
    }
    
    // Constructor vacío (necesario para algunas operaciones)
    public DetalleCompra() {}

    public int getFolio_c() {
        return folio_c;
    }

    public void setFolio_c(int folio_c) {
        this.folio_c = folio_c;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    
}
