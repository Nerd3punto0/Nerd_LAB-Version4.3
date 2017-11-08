package com.jhonlopera.nerd30;

/**
 * Created by Jhon on 30/10/2017.
 */

public class Jugador {
    private String id, correo, nombre;
    private long puntaje4imagenes,puntajeConcentrese,puntajeTopo;

    public long getPuntajeConcentrese() {
        return puntajeConcentrese;
    }

    public void setPuntajeConcentrese(long puntajeConcentrese) {
        this.puntajeConcentrese = puntajeConcentrese;
    }

    public long getPuntajeTopo() {
        return puntajeTopo;
    }

    public void setPuntajeTopo(long puntajeTopo) {
        this.puntajeTopo = puntajeTopo;
    }

    public Jugador(String id, String correo, String nombre, long puntaje4imagenes, long puntajeConcentrese, long puntajeTopo) {
        this.id = id;
        this.correo = correo;
        this.nombre = nombre;
        this.puntaje4imagenes = puntaje4imagenes;
        this.puntajeConcentrese = puntajeConcentrese;
        this.puntajeTopo = puntajeTopo;
    }

    public Jugador() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public long getPuntaje4imagenes() {
        return puntaje4imagenes;
    }

    public void setPuntaje4imagenes(long puntaje4imagenes) {
        this.puntaje4imagenes = puntaje4imagenes;
    }
}