package com.gru.comandroidbluetooth.Helper;

public interface IConexionBT
{
    void coneccionCorrecta(Boolean conectado);

    void errorVincular(String mensaje);

    void idPulsera(String nro_pulsera);
}
