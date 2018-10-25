package com.gru.comandroidbluetooth.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gru.comandroidbluetooth.R;
import com.gru.comandroidbluetooth.helper.Comun;
import com.gru.comandroidbluetooth.model.DatosPacienteModel;
import com.gru.comandroidbluetooth.model.InternacionModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DatosPacienteFragment extends Fragment
{
    private String nro_pulsera="";

    private TextView apellido,nombre,fec_nac,edad,nacionalidad,dni,domic,tel,cel,observacion,os,plan,nro_afil;
    private TextView parent,apellido_par,nombre_par,tel_par,cel_par,domi_par,nro_intern,fec_ingr,txt_pul,habit,obser_inter,motivo_cons;

    private InternacionModel model_paciente;
    public DatosPacienteFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_panel_datos_paciente,container,false);

        if(getArguments() != null)
        {
            nro_pulsera = getArguments().getString("pulsera");
            model_paciente = (InternacionModel) getArguments().get("paciente");
        }
        TextView txt_nro_pulsera = view.findViewById(R.id.txtPulsera);
        txt_nro_pulsera.setText(nro_pulsera);
        findViewDatosPaciente(view);
        cargarDatosEnVista();
        return view;
    }

    private void findViewDatosPaciente(View v)
    {
        apellido     = v.findViewById(R.id.txtApellido);
        nombre       = v.findViewById(R.id.txtNombre);
        fec_nac      = v.findViewById(R.id.txtFecNac);
        edad         = v.findViewById(R.id.txtEdad);
        nacionalidad = v.findViewById(R.id.txtNacionalidad);
        dni          = v.findViewById(R.id.txtDni);
        domic        = v.findViewById(R.id.txtDomicilio);
        tel          = v.findViewById(R.id.txtTel);
        cel          = v.findViewById(R.id.txtCel);
        observacion  = v.findViewById(R.id.txtObservaciones);
        os           = v.findViewById(R.id.txtOS);
        plan         = v.findViewById(R.id.txtPlan);
        nro_afil     = v.findViewById(R.id.txtNroAfiliado);
        parent       = v.findViewById(R.id.txtParentezco);
        apellido_par = v.findViewById(R.id.txtApellidoContacto);
        nombre_par   = v.findViewById(R.id.txtNombreContacto);
        tel_par      = v.findViewById(R.id.txtTelContacto);
        cel_par      = v.findViewById(R.id.txtCelContacto);
        domi_par     = v.findViewById(R.id.txtDomContacto);
        nro_intern   = v.findViewById(R.id.txtNroInternacion);
        fec_ingr     = v.findViewById(R.id.txtFechaIngreso);
        txt_pul      = v.findViewById(R.id.txtNroPulsera);
        habit        = v.findViewById(R.id.txtHabitacionInternacion);
        obser_inter  = v.findViewById(R.id.txtObservacionInternacion);
        motivo_cons  = v.findViewById(R.id.txtMotivoConsulta);

    }

    private void cargarDatosEnVista()
    {
        verificarTextoVacio(apellido,model_paciente.getPaciente().getApellido());
        verificarTextoVacio(nombre,model_paciente.getPaciente().getNombre());
        fec_nac.setText(Comun.convertirDateEnString(model_paciente.getPaciente().getFecha_nacimiento()));
        verificarTextoVacio(edad,model_paciente.getPaciente().getEdad());
        verificarTextoVacio(nacionalidad,model_paciente.getPaciente().getNacionalidad());
        verificarTextoVacio(dni,model_paciente.getPaciente().getDni());
        verificarTextoVacio(domic,model_paciente.getPaciente().getDomicilio());
        verificarTextoVacio(tel,model_paciente.getPaciente().getTel());
        verificarTextoVacio(cel,model_paciente.getPaciente().getCel());
        verificarTextoVacio(observacion,model_paciente.getPaciente().getObservacion());
        verificarTextoVacio(os,model_paciente.getPaciente().getOs());
        verificarTextoVacio(plan,model_paciente.getPaciente().getPlan());
        verificarTextoVacio(nro_afil,model_paciente.getPaciente().getNro_afiliado());
        verificarTextoVacio(parent,"Familiar");
        verificarTextoVacio(apellido_par,model_paciente.getPaciente().getApellido_contacto());
        verificarTextoVacio(nombre_par,model_paciente.getPaciente().getNombre_contacto());
        verificarTextoVacio(tel_par,model_paciente.getPaciente().getTel_contacto());
        verificarTextoVacio(cel_par,model_paciente.getPaciente().getTel_contacto());
        verificarTextoVacio(domi_par,model_paciente.getPaciente().getDomicilio_contacto());
        verificarTextoVacio(nro_intern,model_paciente.getNro_internacion());
        //verificarTextoVacio(fec_ingr,model_paciente.getCreated_at());

        fec_ingr.setText(Comun.convertirStringEnFecha(model_paciente.getCreated_at()));
        try{
            habit.setText("Habit " + model_paciente.getNro_habitacion() + " cama " + model_paciente.getCama());
        }catch (Exception e){ habit.setText("");}
        verificarTextoVacio(obser_inter,model_paciente.getObservacion());
        verificarTextoVacio(motivo_cons,model_paciente.getMotivo_consulta());
    }

    private void verificarTextoVacio(TextView textView,String texto)
    {
        String resul = "";
        try{
            if(texto.isEmpty() || texto.equals("null"))
            {
                resul = "";
            }else {
                resul = texto;
            }
        }catch (Exception e)
        {
            resul = "";
        }
        textView.setText(resul);
    }



}
