package com.runlife.goatleg.runlife.lista_carreras;

import android.net.Uri;

import java.util.Date;

/**
 * Created by javi on 30/05/2018.
 */

public class Carrera {

    private Date fecha;
    private String hora;
    private String lugar;
    private String nombre;
    private String descripcion;
    private String distancia;
    private Uri link;

    public Carrera(Date fecha, String hora, String lugar, String nombre, String descripcion, String distancia, Uri link) {
        this.fecha = fecha;
        this.hora = hora;
        this.lugar = lugar;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.distancia = distancia;
        this.link = link;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Uri getLink() {
        return link;
    }

    public void setLink(Uri link) {
        this.link = link;
    }

    public String getDistancia() {
        return distancia;
    }

    public void setDistancia(String distancia) {
        this.distancia = distancia;
    }
}
