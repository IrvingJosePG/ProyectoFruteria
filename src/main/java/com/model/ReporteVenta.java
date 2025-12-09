package com.model; 

public class ReporteVenta {
    private String FechaDisplay;
    private int folio_v;
    private String empleado;
    private String cliente;
    private String producto;
    private int cantidad;
    private double precio_u;
    private double total_venta;

    public ReporteVenta(String fecha, int folio_v, String empleado, String cliente, String producto, int cantidad, double precio_u, double total_venta) {
        this.FechaDisplay = fecha;
        this.folio_v = folio_v;
        this.empleado = empleado;
        this.cliente = cliente;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precio_u = precio_u;
        this.total_venta = total_venta;
    }
    
    public ReporteVenta(){
        
    }

    public String getFechaDisplay() {
        return FechaDisplay;
    }

    public void setFechaDisplay(String FechaDisplay) {
        this.FechaDisplay = FechaDisplay;
    }

    public int getFolio_v() {
        return folio_v;
    }

    public void setFolio_v(int folio_v) {
        this.folio_v = folio_v;
    }

    public String getEmpleado() {
        return empleado;
    }

    public void setEmpleado(String empleado) {
        this.empleado = empleado;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio_u() {
        return precio_u;
    }

    public void setPrecio_u(double precio_u) {
        this.precio_u = precio_u;
    }

    public double getTotal_venta() {
        return total_venta;
    }

    public void setTotal_venta(double total_venta) {
        this.total_venta = total_venta;
    }
    
    
}
