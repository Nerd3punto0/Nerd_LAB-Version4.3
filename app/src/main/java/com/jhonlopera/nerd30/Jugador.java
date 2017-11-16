package com.jhonlopera.nerd30;

/**
 * Created by Jhon on 30/10/2017.
 */

public class Jugador {
    private String id, correo, nombre;
    private long puntaje4imagenes,puntajeConcentrese,puntajeTopo;
    private int nivel4img, nivelcon, niveltopo;

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

    public Jugador(String id, String correo, String nombre, long puntaje4imagenes, long puntajeConcentrese, long puntajeTopo,
                   int nivel4img, int nivelcon, int niveltopo) {
        this.id = id;
        this.correo = correo;
        this.nombre = nombre;
        this.puntaje4imagenes = puntaje4imagenes;
        this.puntajeConcentrese = puntajeConcentrese;
        this.puntajeTopo = puntajeTopo;
        this.nivel4img = nivel4img;
        this.nivelcon = nivelcon;
        this.niveltopo = niveltopo;
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

    public int getNivel4img() {
        return nivel4img;
    }

    public void setNivel4img(int nivel4img) {
        this.nivel4img = nivel4img;
    }

    public int getNivelcon() {
        return nivelcon;
    }

    public void setNivelcon(int nivelcon) {
        this.nivelcon = nivelcon;
    }

    public int getNiveltopo() {
        return niveltopo;
    }

    public void setNiveltopo(int niveltopo) {
        this.niveltopo = niveltopo;
    }
}