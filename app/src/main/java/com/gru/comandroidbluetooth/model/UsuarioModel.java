package com.gru.comandroidbluetooth.model;

public class UsuarioModel {
    private int id;
    private String nrombre_usuario;
    private String rol;
    private int id_rol;

    public UsuarioModel(){}
    public UsuarioModel(int id, String nrombre_usuario, String rol) {
        this.id = id;
        this.nrombre_usuario = nrombre_usuario;
        this.rol = rol;
    }

    public UsuarioModel(int id, String nrombre_usuario, String rol, int id_rol) {
        this.id = id;
        this.nrombre_usuario = nrombre_usuario;
        this.rol = rol;
        this.id_rol = id_rol;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNrombre_usuario() {
        return nrombre_usuario;
    }

    public void setNrombre_usuario(String nrombre_usuario) {
        this.nrombre_usuario = nrombre_usuario;
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
}
