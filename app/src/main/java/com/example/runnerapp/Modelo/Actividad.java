package com.example.runnerapp.Modelo;

public class Actividad {
    String codigo_actividad;
    String fecha;
    String kilometro;
    String tiempo;
    String kcal;


    public Actividad() {

    }

    public Actividad(String codigo_actividad, String fecha, String kilometro, String tiempo, String kcal) {
        this.codigo_actividad = codigo_actividad;
        this.fecha = fecha;
        this.kilometro = kilometro;
        this.tiempo = tiempo;
        this.kcal = kcal;
    }

    public String getKcal() {
        return kcal;
    }

    public void setKcal(String kcal) {
        this.kcal = kcal;
    }

    public String getCodigo_actividad() {
        return codigo_actividad;
    }

    public void setCodigo_actividad(String codigo_actividad) {
        this.codigo_actividad = codigo_actividad;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getKilometro() {
        return kilometro;
    }

    public void setKilometro(String kilometro) {
        this.kilometro = kilometro;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }
}
