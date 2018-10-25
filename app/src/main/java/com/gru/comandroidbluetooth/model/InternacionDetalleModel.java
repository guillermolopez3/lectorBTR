package com.gru.comandroidbluetooth.model;

import java.util.Date;

public class InternacionDetalleModel
{
    private int id;
    private InternacionModel internacion;
    private String detalle;
    private String created_at;
    private int id_rol;
    private String rol;
    private String usuario;

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

    public InternacionDetalleModel(String detalle, String created_at, int id_rol,String rol,String usuario) {
        this.detalle = detalle;
        this.created_at = created_at;
        this.id_rol = id_rol;
        this.rol = rol;
        this.usuario=usuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public int getId_rol() {
        return id_rol;
    }

    public void setId_rol(int id_rol) {
        this.id_rol = id_rol;
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
