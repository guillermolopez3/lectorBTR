package com.gru.comandroidbluetooth.helper;

import java.util.UUID;

public class Constants
{
    public static final String NAME_LECTOR_1 = "LD121604000829";
    public static final String MAC_LECTOR_1 = "98:D3:31:FC:1F:F5";

    public static final String NAME_LECTOR_2 = "LD1214060893";
    public static final String MAC_LECTOR_2 = "98:D3:31:20:10:F6";

    public static final UUID UUID_SEGURO = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public static final int CLIENTE_ACEPTA=1;
    public static final int CLIENTE_CANCELA=2;

    public static final int TAREA_ACEPTA = 3;
    public static final int TAREA_CANCELA = 4;

    //public static final String URL_BASE = "http://conectarigualdadcordoba.com.ar/laravel/public/";
    public static final String URL_BASE = "http://www.algoritmia.com.ar/api/";
    public static final String URL_BUSCAR_PACIENTE = "getPaciente";
    public static final String URL_BUSCAR_PACIENTE_CON_PULSERA = "getPacientePulsera?";
    public static final String URL_REGISTRAR_PULSERA_PACIENTE = "registrarPulsera";
    public static final String URL_GET_ALL_HISTORICO = "getAllHistorico?";
    public static final String URL_INSERT_HISTORIA = "insertarHistorial";
    public static final String URL_COMPROBAR_USUARIO = "getUsuario";
    public static final String URL_INSERAR_TAREA = "insertarNuevaTarea";
    public static final String URL_GET_ALL_TAREAS = "getAlltareas";
    public static final String URL_ACTUALIZAR_TAREA = "actualizarTarea";
}
