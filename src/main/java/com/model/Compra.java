
package com.model;


import java.time.LocalDate;

public class Compra {
    private int folio_c;
    private int no_lote;
    private LocalDate fecha;
    private int id_p;
    private int id_e;

    public Compra(int folio_c, int no_lote, LocalDate fecha, int id_p, int id_e) {
        this.folio_c = folio_c;
        this.no_lote = no_lote;
        this.fecha = fecha;
        this.id_p = id_p;
        this.id_e = id_e;
    }
    
    // Constructor vacío (necesario para algunas operaciones)
    public Compra() {}

    public int getFolio_c() {
        return folio_c;
    }

    public void setFolio_c(int folio_c) {
        this.folio_c = folio_c;
    }

    public int getNo_lote() {
        return no_lote;
    }

    public void setNo_lote(int no_lote) {
        this.no_lote = no_lote;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public int getId_p() {
        return id_p;
    }

    public void setId_p(int id_p) {
        this.id_p = id_p;
    }

    public int getId_e() {
        return id_e;
    }

    public void setId_e(int id_e) {
        this.id_e = id_e;
    }
    
    
}
