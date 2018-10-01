package com.gru.comandroidbluetooth.model;

import java.util.Date;

public class InternacionDetalleModel
{
    private int id;
    private InternacionModel internacion;
    private String detalle;
    private String created_at;

    public InternacionDetalleModel(){}

    public InternacionDetalleModel(int id, InternacionModel internacion, String detalle, String created_at) {
        this.id = id;
        this.internacion = internacion;
        this.detalle = detalle;
        this.created_at = created_at;
    }

    public InternacionDetalleModel(String created_at,String detalle) {
        this.detalle = detalle;
        this.created_at = created_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public InternacionModel getInternacion() {
        return internacion;
    }

    public void setInternacion(InternacionModel internacion) {
        this.internacion = internacion;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }
}
