package com.model;

public class Auditoria {
   private int id_a;
   private String nombretabla;
   private String operacion;
   private String usuariobd;
   private String FechaDisplay;

    public Auditoria(int id_a, String nombretabla, String operacion, String usuariobd, String fecha) {
        this.id_a = id_a;
        this.nombretabla = nombretabla;
        this.operacion = operacion;
        this.usuariobd = usuariobd;
        this.FechaDisplay = fecha;
    }
    
    public Auditoria(){
        
    }

    public int getId_a() {
        return id_a;
    }

    public void setId_a(int id_a) {
        this.id_a = id_a;
    }

    public String getNombretabla() {
        return nombretabla;
    }

    public void setNombretabla(String nombretabla) {
        this.nombretabla = nombretabla;
    }

    public String getOperacion() {
        return operacion;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    public String getUsuariobd() {
        return usuariobd;
    }

    public void setUsuariobd(String usuariobd) {
        this.usuariobd = usuariobd;
    }

    public String getFechaDisplay() {
        return FechaDisplay;
    }

    public void setFechaDisplay(String FechaDisplay) {
        this.FechaDisplay = FechaDisplay;
    }
   
   
}
