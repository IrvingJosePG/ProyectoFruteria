package com.model;

import java.time.LocalDate;

public class Venta {
    
    private Integer folioV;     // Para capturar el folio generado por la DB
    private LocalDate fecha;
    private int idCliente;
    private int idEmpleado;
    
    // Constructor (puedes usar el vacío y setters)
    public Venta() {
        this.fecha = LocalDate.now(); // Inicializa la fecha automáticamente
    }
    
    // Getters y Setters:
    public Integer getFolioV() { return folioV; }
    public void setFolioV(Integer folioV) { this.folioV = folioV; }
    
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
    
    public int getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(int idEmpleado) { this.idEmpleado = idEmpleado; }
}