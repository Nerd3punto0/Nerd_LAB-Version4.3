package com.jhonlopera.nerd30;

/**
 * Created by Jhon on 25/10/2017.
 */

public interface OpenInterface {

    void OpenCuatroImagenesMenu();
    void OpenConcentreseMenu();
    void OpenTopoMenu();
    void OpenCuantroImagenes();
    void cambiarnombre(String nuevonombre);
    void eliminardatos();
    void cerrarjuego();
    void guardarpreferencias(int contadorbroma);
    void OpenConcentrese();
    void OpenTopo();
    void cerrarSesion();
    void actualizarpuntajes(long p4imagenes,long pconcentrese,long ptopo);
    void estadomusica(int musicstate);
}
