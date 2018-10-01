package com.gru.comandroidbluetooth.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gru.comandroidbluetooth.R;
import com.gru.comandroidbluetooth.backend.VolleySingleton;
import com.gru.comandroidbluetooth.helper.Constants;
import com.gru.comandroidbluetooth.model.DatosPacienteModel;
import com.gru.comandroidbluetooth.model.InternacionModel;
import com.gru.comandroidbluetooth.view.MainPacienteActivity;
import com.gru.comandroidbluetooth.view.RegistrarPulseraActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BusquedaPacienteAdapter extends RecyclerView.Adapter<BusquedaPacienteAdapter.BusquedaPacientesViewHolder>
{
    private ArrayList<DatosPacienteModel> list;
    private Activity activity;
    private String nro_pulsera;

    public BusquedaPacienteAdapter(ArrayList<DatosPacienteModel> list,Activity activity,String nro_pulsera)
    {
        this.list     = list;
        this.activity = activity;
        this.nro_pulsera = nro_pulsera;
    }
    @NonNull
    @Override
    public BusquedaPacientesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_list_buscar_paciente,parent,false);
        return new BusquedaPacientesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusquedaPacientesViewHolder holder, int position) {
        final DatosPacienteModel model = list.get(position);

        holder.nombre.setText(model.getApellido() + ", " + model.getNombre());
        holder.dni.setText("DNI: " + model.getDni());

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmarAsignacion(model);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void confirmarAsignacion(final DatosPacienteModel model)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();

        final View view = inflater.inflate(R.layout.dialog_confirmar_asignacion_pulsera,null);

        builder.setView(view);

        final TextView txt_datos_paciente  = view.findViewById(R.id.txtDatosPacienteRegistrar);

        final TextInputEditText edtNroInternacion,edtNroHabitacion,edtNroCama,edtMotivoConsulta;

        edtNroInternacion= view.findViewById(R.id.txtNroInternacion);
        edtNroHabitacion= view.findViewById(R.id.txtNroHabitacion);
        edtNroCama = view.findViewById(R.id.txtNroCama);
        edtMotivoConsulta= view.findViewById(R.id.txtMotivoConsulta);


        txt_datos_paciente.setText(model.getApellido() + ", " + model.getNombre() + " - " +"DNI:" + model.getDni());

        Button no = view.findViewById(R.id.btnCancelar);
        Button si = view.findViewById(R.id.btnAceptar);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.setCancelable(false);

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //alertDialog.dismiss();
                activity.finish();
            }
        });

        si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent i = new Intent(activity,MainPacienteActivity.class);
                i.putExtra("paciente",model);
                activity.startActivity(i);
                activity.finish();*/
                // alertDialog.dismiss();
                prepararDatosParaInsertar(edtNroInternacion.getText().toString(),edtNroHabitacion.getText().toString(),
                        edtNroCama.getText().toString(),edtMotivoConsulta.getText().toString(),model);
            }
        });
    }

    private void prepararDatosParaInsertar(String inter,String habi,String cama,String motivo,DatosPacienteModel model)
    {
        String in="",ha="",ca="",mo="";
        if(!inter.isEmpty()){
            in = inter;
        }
        if (!habi.isEmpty()){
            ha=habi;
        }
        if(!cama.isEmpty()){
            ca=cama;
        }
        if (!motivo.isEmpty())
        {
            mo=motivo;
        }
        InternacionModel internacionModel = new InternacionModel(in,model,ha,ca,mo);
        insertarInternacion(internacionModel);
    }

    private void insertarInternacion(final InternacionModel model)
    {
        final String url = Constants.URL_BASE + Constants.URL_REGISTRAR_PULSERA_PACIENTE;
        Log.e("url= ", url);
        final ProgressDialog dialog=new ProgressDialog(activity);
        dialog.setMessage("Guardando datos...");
        dialog.show();
        VolleySingleton.getInstancia(activity).
                addToRequestQueue(new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                dialog.hide();
                                try {
                                    JSONObject object = new JSONObject(response);

                                    String rta = (String) object.get("data");
                                    if(rta.equals("ok")){
                                        abrirActivityDatosPaciente(model);
                                    }else if(rta.equals("error"))
                                    {
                                        Toast.makeText(activity, "Error al guardar el nro de pulsera", Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(activity, "Error al guardar el nro de pulsera", Toast.LENGTH_LONG).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("Response", "error" + error.getMessage());
                                Toast.makeText(activity, "Error al guardar el nro de pulsera", Toast.LENGTH_LONG).show();
                            }
                        }){

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> map = new HashMap<String,String>();
                        map.put("nro_pulsera",nro_pulsera);
                        map.put("nro_internacion",model.getNro_internacion());
                        map.put("id_paciente", String.valueOf(model.getPaciente().getId()));
                        map.put("nro_habitacion",model.getNro_habitacion());
                        map.put("cama",model.getCama());
                        map.put("motivo_consulta",model.getMotivo_consulta());
                        return map;
                    }
                });
    }

    private void abrirActivityDatosPaciente(InternacionModel model)
    {
        Intent i = new Intent(activity,MainPacienteActivity.class);
        i.putExtra("paciente",model);
        i.putExtra("pulsera",nro_pulsera);
        activity.startActivity(i);
    }

    class BusquedaPacientesViewHolder extends RecyclerView.ViewHolder
    {
        TextView nombre, dni;
        CardView card;
        public BusquedaPacientesViewHolder(View v) {
            super(v);
            nombre = v.findViewById(R.id.txtNombre);
            dni    = v.findViewById(R.id.txtDni);
            card   = v.findViewById(R.id.card);
        }
    }
}
