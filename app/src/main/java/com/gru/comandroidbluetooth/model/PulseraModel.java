package com.gru.comandroidbluetooth.model;

public class PulseraModel
{
    private int id;
    private String nro_pulsera;

    public PulseraModel(){};

    public PulseraModel(int id, String nro_pulsera) {
        this.id = id;
        this.nro_pulsera = nro_pulsera;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNro_pulsera() {
        return nro_pulsera;
    }

    public void setNro_pulsera(String nro_pulsera) {
        this.nro_pulsera = nro_pulsera;
    }
}
