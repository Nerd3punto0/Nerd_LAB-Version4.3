package com.jhonlopera.nerd30;

/**
 * Created by Jhon on 3/12/2017.
 */

public class PuntajeJuego {
    public String name, puntaje,id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPuntaje() {
        return puntaje;

    }

    public void setPuntaje(String puntaje) {
        this.puntaje = puntaje;
    }

    public PuntajeJuego(String name, String puntaje,String id) {
        this.name = name;

        this.puntaje = puntaje;

        this.id=id;
    }
}

