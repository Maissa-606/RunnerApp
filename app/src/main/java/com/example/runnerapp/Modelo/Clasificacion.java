package com.example.runnerapp.Modelo;

public class Clasificacion {
    String top;
    String foto;
    String nombrecompleto;
    String km;
    String Kcal;

    public Clasificacion() {

    }

    public Clasificacion(String top, String foto, String nombrecompleto, String km, String kcal) {
        this.top = top;
        this.foto = foto;
        this.nombrecompleto = nombrecompleto;
        this.km = km;
        Kcal = kcal;
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNombrecompleto() {
        return nombrecompleto;
    }

    public void setNombrecompleto(String nombrecompleto) {
        this.nombrecompleto = nombrecompleto;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }

    public String getKcal() {
        return Kcal;
    }

    public void setKcal(String kcal) {
        Kcal = kcal;
    }
}
