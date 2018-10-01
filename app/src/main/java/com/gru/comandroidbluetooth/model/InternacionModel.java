package com.gru.comandroidbluetooth.model;

import java.io.Serializable;
import java.util.Date;

public class InternacionModel implements Serializable
{
    private int id;
    private String created_at;
    private String nro_internacion;
    private DatosPacienteModel paciente;
    private PulseraModel pulsera;
    private String nro_habitacion;
    private String cama;
    private String observacion;
    private String motivo_consulta;
    private Date fecha_egreso;
    private UsuarioModel usuario;
    private String diagnostico_final;

    public InternacionModel(){}

    public InternacionModel(String nro_internacion, DatosPacienteModel paciente, String nro_habitacion, String cama, String motivo_consulta) {
        this.nro_internacion = nro_internacion;
        this.paciente = paciente;
        this.nro_habitacion = nro_habitacion;
        this.cama = cama;
        this.motivo_consulta = motivo_consulta;
    }

    public InternacionModel(int id, String created_at, String nro_internacion, DatosPacienteModel paciente, String nro_habitacion, String cama, String observacion, String motivo_consulta) {
        this.id = id;
        this.created_at = created_at;
        this.nro_internacion = nro_internacion;
        this.paciente = paciente;
        this.nro_habitacion = nro_habitacion;
        this.cama = cama;
        this.observacion = observacion;
        this.motivo_consulta = motivo_consulta;
    }

    public InternacionModel(int id, String created_at, String nro_internacion, DatosPacienteModel paciente, PulseraModel pulsera, String nro_habitacion, String cama, String observacion, String motivo_consulta, Date fecha_egreso, UsuarioModel usuario, String diagnostico_final) {
        this.id = id;
        this.created_at = created_at;
        this.nro_internacion = nro_internacion;
        this.paciente = paciente;
        this.pulsera = pulsera;
        this.nro_habitacion = nro_habitacion;
        this.cama = cama;
        this.observacion = observacion;
        this.motivo_consulta = motivo_consulta;
        this.fecha_egreso = fecha_egreso;
        this.usuario = usuario;
        this.diagnostico_final = diagnostico_final;
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

    public String getNro_internacion() {
        return nro_internacion;
    }

    public void setNro_internacion(String nro_internacion) {
        this.nro_internacion = nro_internacion;
    }

    public DatosPacienteModel getPaciente() {
        return paciente;
    }

    public void setPaciente(DatosPacienteModel paciente) {
        this.paciente = paciente;
    }

    public PulseraModel getPulsera() {
        return pulsera;
    }

    public void setPulsera(PulseraModel pulsera) {
        this.pulsera = pulsera;
    }

    public String getNro_habitacion() {
        return nro_habitacion;
    }

    public void setNro_habitacion(String nro_habitacion) {
        this.nro_habitacion = nro_habitacion;
    }

    public String getCama() {
        return cama;
    }

    public void setCama(String cama) {
        this.cama = cama;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getMotivo_consulta() {
        return motivo_consulta;
    }

    public void setMotivo_consulta(String motivo_consulta) {
        this.motivo_consulta = motivo_consulta;
    }

    public Date getFecha_egreso() {
        return fecha_egreso;
    }

    public void setFecha_egreso(Date fecha_egreso) {
        this.fecha_egreso = fecha_egreso;
    }

    public UsuarioModel getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioModel usuario) {
        this.usuario = usuario;
    }

    public String getDiagnostico_final() {
        return diagnostico_final;
    }

    public void setDiagnostico_final(String diagnostico_final) {
        this.diagnostico_final = diagnostico_final;
    }
}
