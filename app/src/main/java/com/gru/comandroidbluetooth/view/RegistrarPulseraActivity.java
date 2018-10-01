package com.gru.comandroidbluetooth.view;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gru.comandroidbluetooth.R;
import com.gru.comandroidbluetooth.adapter.BusquedaPacienteAdapter;
import com.gru.comandroidbluetooth.backend.VolleySingleton;
import com.gru.comandroidbluetooth.helper.Comun;
import com.gru.comandroidbluetooth.helper.Constants;
import com.gru.comandroidbluetooth.model.DatosPacienteModel;
import com.gru.comandroidbluetooth.view.fragment.DatePickerFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegistrarPulseraActivity extends AppCompatActivity
{
    TextInputEditText txt_pulsera,txt_apellido,txt_dni;
    EditText dp_fecha_nac;
    Button btn_buscar,btn_salir;
    ArrayList<DatosPacienteModel> list;
    RecyclerView recyclerView;
    ProgressBar progressBar;

    private String fec_nac = ""; //variable para la consulta a la BD que busca 1987/10/13 yyyy/mm/dd
    private String nro_pulsera = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_pulsera);

        Comun.showToolbar("Asignación de pulsera a paciente",true,this);

        if(getIntent() == null)
        {
            Toast.makeText(this,"Error al leer la pulsera",Toast.LENGTH_LONG).show();
            finish();
        }

        nro_pulsera = getIntent().getStringExtra("pulsera");

        txt_pulsera  = findViewById(R.id.txtNroPulsera);
        txt_apellido = findViewById(R.id.txtApellido);
        dp_fecha_nac = findViewById(R.id.fechaNacimiento);
        txt_dni      = findViewById(R.id.txtDni);
        btn_buscar   = findViewById(R.id.btnBuscar);
        btn_salir    = findViewById(R.id.btnCancelar);
        recyclerView = findViewById(R.id.recycler);
        progressBar  = findViewById(R.id.progress);

        progressBar.setVisibility(View.GONE);
        txt_apellido.requestFocus();
        list = new ArrayList<>();
        txt_pulsera.setText(nro_pulsera);

        dp_fecha_nac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        btn_buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buscarPaciente();
            }
        });
    }

    //metodo para mostrar y obtener la fecha de nacimiento del paciente
    private void showDatePickerDialog()
    {
        DatePickerFragment dialog = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int mes, int dia) {
                final String selecday = dia + "/" + (mes + 1) + "/" + year;
                fec_nac = year + "/" + (mes + 1) + "/" + dia;
                dp_fecha_nac.setText(selecday);
            }
        });
        dialog.show(getSupportFragmentManager(),"datePicker");
    }

    private void confirmarAsignacion()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        final View view = inflater.inflate(R.layout.dialog_confirmar_asignacion_pulsera,null);

        builder.setView(view);

        final TextView txt_datos_paciente  = view.findViewById(R.id.txtDatosPacienteRegistrar);

        txt_datos_paciente.setText("López, Guillermo Andrés, DNI: 33320088");

        Button no = view.findViewById(R.id.btnCancelar);
        Button si = view.findViewById(R.id.btnAceptar);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.setCancelable(false);

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //alertDialog.dismiss();
                finish();
            }
        });

        si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrarPulseraActivity.this,MainPacienteActivity.class));
                finish();
                // alertDialog.dismiss();
            }
        });
    }

    private void buscarPaciente()
    {
        final String url = Constants.URL_BASE + Constants.URL_BUSCAR_PACIENTE;
        Log.e("url= ", Constants.URL_BASE + Constants.URL_BUSCAR_PACIENTE);
        progressBar.setVisibility(View.VISIBLE);
        VolleySingleton.getInstancia(this).
                addToRequestQueue(new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressBar.setVisibility(View.GONE);
                                try{
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    for(int i=0; i<jsonArray.length();i++)
                                    {
                                        JSONObject o = jsonArray.getJSONObject(i);

                                        DatosPacienteModel post = new DatosPacienteModel(
                                                o.getInt("id"),
                                                o.getString("created_at"),
                                                o.getString("apellido"),
                                                o.getString("nombre"),
                                                o.getString("nro_documento"),
                                                o.getString("fecha_nacimiento"),
                                                o.getString("nacionalidad"),
                                                o.getString("domicilio"),
                                                o.getString("tel"),
                                                o.getString("cel"),
                                                o.getString("observacion"),
                                                o.getString("os"),
                                                o.getString("plan"),
                                                o.getString("nro_afiliado"),
                                                o.getString("nombre_contacto"),
                                                o.getString("apellido_contacto"),
                                                o.getString("tel_contacto"),
                                                o.getString("domicilio_contacto")
                                        );
                                        list.add(post);
                                    }
                                    cargarListaConDatosDePacientes();

                                }catch (JSONException e){
                                    Log.e("jsonEx Buscar pac", e.getMessage());
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("Response", "error" + error.getMessage());
                                Toast.makeText(RegistrarPulseraActivity.this,"Ocurrió un error, vuelva a intentarlo",Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }){

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> map = new HashMap<String,String>();
                        map.put("dni",txt_dni.getText().toString());
                        map.put("ape",txt_apellido.getText().toString());
                        map.put("fec_nac",fec_nac);
                        return map;
                    }
                });
    }

    private void cargarListaConDatosDePacientes()
    {
        if(list.size()>0)
        {
            recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
            recyclerView.setAdapter(new BusquedaPacienteAdapter(list,this,nro_pulsera));
        }
        else {
            Toast.makeText(RegistrarPulseraActivity.this,"No hay pacientes registrados con los datos ingresados",Toast.LENGTH_LONG).show();
        }
    }
}
