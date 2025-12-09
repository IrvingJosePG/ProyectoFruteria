package com.model;

import java.sql.Date;

/**
 * Modelo para contener los datos de un detalle de compra, utilizado para reportes.
 * Combina datos de Compra, Detalle_Compra, Proveedor, Empleado y Producto.
 */
public class ReporteCompra {
    private int folioC;
    private String FechaDisplay;
    private String nombreProveedor;
    private String nombreEmpleado;
    private String descripcionProducto;
    private int cantidad;
    private double costoUnitario;
    private double total;

    public ReporteCompra() {
    }

    public int getFolioC() {
        return folioC;
    }

    public void setFolioC(int folioC) {
        this.folioC = folioC;
    }

    public String getFechaDisplay() {
        return FechaDisplay;
    }

    public void setFechaDisplay(String fecha) {
        this.FechaDisplay = fecha;
    }

    public String getNombreProveedor() {
        return nombreProveedor;
    }

    public void setNombreProveedor(String nombreProveedor) {
        this.nombreProveedor = nombreProveedor;
    }

    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
    }

    public String getDescripcionProducto() {
        return descripcionProducto;
    }

    public void setDescripcionProducto(String descripcionProducto) {
        this.descripcionProducto = descripcionProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getCostoUnitario() {
        return costoUnitario;
    }

    public void setCostoUnitario(double costoUnitario) {
        this.costoUnitario = costoUnitario;
    }
    
    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}