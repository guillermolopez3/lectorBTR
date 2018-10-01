package com.gru.comandroidbluetooth.model;

import java.io.Serializable;
import java.util.Date;

public class DatosPacienteModel implements Serializable
{
    private int id;
    private String created_at;
    private String apellido;
    private String nombre;
    private String dni;
    private String fecha_nacimiento;
    private String nacionalidad;
    private String domicilio;
    private String tel;
    private String cel;
    private String observacion;
    private String os;
    private String plan;
    private String nro_afiliado;
    private String nombre_contacto;
    private String apellido_contacto;
    private String tel_contacto;
    private String domicilio_contacto;
    private String parentezco;

    public DatosPacienteModel(){}


    public DatosPacienteModel(int id, String created_at, String apellido, String nombre, String dni, String fecha_nacimiento, String nacionalidad, String domicilio, String tel, String cel, String observacion, String os, String plan, String nro_afiliado, String nombre_contacto, String apellido_contacto, String tel_contacto, String domicilio_contacto) {
        this.id = id;
        this.created_at = created_at;
        this.apellido = apellido;
        this.nombre = nombre;
        this.dni = dni;
        this.fecha_nacimiento = fecha_nacimiento;
        this.nacionalidad = nacionalidad;
        this.domicilio = domicilio;
        this.tel = tel;
        this.cel = cel;
        this.observacion = observacion;
        this.os = os;
        this.plan = plan;
        this.nro_afiliado = nro_afiliado;
        this.nombre_contacto = nombre_contacto;
        this.apellido_contacto = apellido_contacto;
        this.tel_contacto = tel_contacto;
        this.domicilio_contacto = domicilio_contacto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(String fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getCel() {
        return cel;
    }

    public void setCel(String cel) {
        this.cel = cel;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getNro_afiliado() {
        return nro_afiliado;
    }

    public void setNro_afiliado(String nro_afiliado) {
        this.nro_afiliado = nro_afiliado;
    }

    public String getNombre_contacto() {
        return nombre_contacto;
    }

    public void setNombre_contacto(String nombre_contacto) {
        this.nombre_contacto = nombre_contacto;
    }

    public String getApellido_contacto() {
        return apellido_contacto;
    }

    public void setApellido_contacto(String apellido_contacto) {
        this.apellido_contacto = apellido_contacto;
    }

    public String getTel_contacto() {
        return tel_contacto;
    }

    public void setTel_contacto(String tel_contacto) {
        this.tel_contacto = tel_contacto;
    }

    public String getDomicilio_contacto() {
        return domicilio_contacto;
    }

    public void setDomicilio_contacto(String domicilio_contacto) {
        this.domicilio_contacto = domicilio_contacto;
    }

    public String getParentezco() {
        return parentezco;
    }

    public void setParentezco(String parentezco) {
        this.parentezco = parentezco;
    }
}
