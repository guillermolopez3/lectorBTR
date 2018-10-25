package com.gru.comandroidbluetooth.model;

import java.io.Serializable;

public class TaskModel implements Serializable
{
    private int id;
    private String fecha_tarea;
    private String titulo;
    private String detalle;
    private String estado;
    private int id_rol;
    private String rol;

    public TaskModel(){}

    public TaskModel(int id, String fecha_tarea, String titulo, String detalle, String estado, int id_rol, String rol) {
        this.id = id;
        this.fecha_tarea = fecha_tarea;
        this.titulo = titulo;
        this.detalle = detalle;
        this.estado = estado;
        this.id_rol = id_rol;
        this.rol = rol;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFecha_tarea() {
        return fecha_tarea;
    }

    public void setFecha_tarea(String fecha_tarea) {
        this.fecha_tarea = fecha_tarea;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getId_rol() {
        return id_rol;
    }

    public void setId_rol(int id_rol) {
        this.id_rol = id_rol;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
