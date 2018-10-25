package com.gru.comandroidbluetooth.model;

public class TareaModel
{
    private int id;
    private int id_internacion;
    private String created_at;
    private String updated_at;
    private String estado;
    private String detalle;
    private String fecha_aviso;
    private String hora_aviso;
    private String titulo_tarea;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_internacion() {
        return id_internacion;
    }

    public void setId_internacion(int id_internacion) {
        this.id_internacion = id_internacion;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getFecha_aviso() {
        return fecha_aviso;
    }

    public void setFecha_aviso(String fecha_aviso) {
        this.fecha_aviso = fecha_aviso;
    }

    public String getHora_aviso() {
        return hora_aviso;
    }

    public void setHora_aviso(String hora_aviso) {
        this.hora_aviso = hora_aviso;
    }

    public String getTitulo_tarea() {
        return titulo_tarea;
    }

    public void setTitulo_tarea(String titulo_tarea) {
        this.titulo_tarea = titulo_tarea;
    }
}
