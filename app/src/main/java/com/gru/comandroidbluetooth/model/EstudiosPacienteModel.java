package com.gru.comandroidbluetooth.model;

public class EstudiosPacienteModel
{
    private int id;
    private String ico;
    private String nro_estudi;
    private String tipo_estudio;
    private String derivante;
    private String Estado;

    public EstudiosPacienteModel(int id, String ico, String nro_estudi, String tipo_estudio, String derivante, String estado) {
        this.id = id;
        this.ico = ico;
        this.nro_estudi = nro_estudi;
        this.tipo_estudio = tipo_estudio;
        this.derivante = derivante;
        Estado = estado;
    }

    public EstudiosPacienteModel(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }

    public String getNro_estudi() {
        return nro_estudi;
    }

    public void setNro_estudi(String nro_estudi) {
        this.nro_estudi = nro_estudi;
    }

    public String getTipo_estudio() {
        return tipo_estudio;
    }

    public void setTipo_estudio(String tipo_estudio) {
        this.tipo_estudio = tipo_estudio;
    }

    public String getDerivante() {
        return derivante;
    }

    public void setDerivante(String derivante) {
        this.derivante = derivante;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }
}
