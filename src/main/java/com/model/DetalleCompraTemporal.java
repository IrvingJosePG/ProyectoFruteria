package com.model;

public class DetalleCompraTemporal {
    // El ID de la venta (se asignará al finalizar la transacción)
    // private int idVenta; 

    // Referencia al producto que se está vendiendo
    private int idProducto; 
    private String nombreProducto; // Útil para validaciones y visualización
    
    // Los datos de la venta
    private int cantidad;
    private double precioUnitario; // Precio al que se vendió (puede ser diferente al actual)
    private double subtotal;

    // Constructor
    public DetalleCompraTemporal(int idProducto, String nombreProducto, int cantidad, double precioUnitario, double subtotal) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
    }
    
    public DetalleCompraTemporal(){
        
    }
    // Getters y Setters (Necesarios para acceder a los datos al finalizar la venta)
    public int getIdProducto() {
        return idProducto;
    }
    
    public String getNombreProducto() {
        return nombreProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public double getSubtotal() {
        return subtotal;
    }
    
    // ... otros métodos (opcionales) ..
}