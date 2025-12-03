
package com.model;

public class Empleado {
    private int id_e;
    private String nombre;
    private String turno;
    private double salario;

    public Empleado(int id_c, String nombre, String turno, double domicilio) {
        this.id_e = id_c;
        this.nombre = nombre;
        this.turno = turno;
        this.salario = domicilio;
    }
    
     // Constructor vacío (necesario para algunas operaciones)
    public Empleado() {}
    
    public int getId_e() {
        return id_e;
    }

    public void setId_e(int id_e) {
        this.id_e = id_e;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }
    
    // 💡 Método CRUCIAL para JComboBox:
    // Este método define qué texto se mostrará en el JComboBox.
    @Override
    public String toString() {
        return id_e + " , " + nombre;
    }
}
