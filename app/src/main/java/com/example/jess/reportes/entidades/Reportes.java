package com.example.jess.reportes.entidades;

import android.graphics.Bitmap;

public class Reportes {
    private Integer idReporte;
    private String latitud;
    private String longitud;
    private String tiempo;
    private String ruta;
    private String tipoReporte;
    private String numPoste;
    private String propPoste;
    private String usoPoste;
    private String estadoPoste;

    public String getTipoReporte() {
        return tipoReporte;
    }

    public void setTipoReporte(String tipoReporte) {
        this.tipoReporte = tipoReporte;
    }

    public String getNumPoste() {
        return numPoste;
    }

    public void setNumPoste(String numPoste) {
        this.numPoste = numPoste;
    }

    public String getPropPoste() {
        return propPoste;
    }

    public void setPropPoste(String propPoste) {
        this.propPoste = propPoste;
    }

    public String getUsoPoste() {
        return usoPoste;
    }

    public void setUsoPoste(String usoPoste) {
        this.usoPoste = usoPoste;
    }

    public String getEstadoPoste() {
        return estadoPoste;
    }

    public void setEstadoPoste(String estadoPoste) {
        this.estadoPoste = estadoPoste;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public Integer getIdReporte() {
        return idReporte;
    }

    public void setIdReporte(Integer idReporte) {
        this.idReporte = idReporte;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }
}
