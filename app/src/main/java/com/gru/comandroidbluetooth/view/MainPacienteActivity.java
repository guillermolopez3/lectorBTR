package com.gru.comandroidbluetooth.view;



import android.app.ProgressDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.gru.comandroidbluetooth.R;
import com.gru.comandroidbluetooth.backend.VolleySingleton;
import com.gru.comandroidbluetooth.helper.Constants;
import com.gru.comandroidbluetooth.helper.ICallBackListener;
import com.gru.comandroidbluetooth.model.DatosPacienteModel;
import com.gru.comandroidbluetooth.model.InternacionDetalleModel;
import com.gru.comandroidbluetooth.model.InternacionModel;
import com.gru.comandroidbluetooth.view.fragment.DatosPacienteFragment;
import com.gru.comandroidbluetooth.view.fragment.EstudiosPacienteFragment;
import com.gru.comandroidbluetooth.view.fragment.EvolucionPacienteFragment;
import com.gru.comandroidbluetooth.view.fragment.EvolucionPacienteNuevaEntradaFragment;
import com.gru.comandroidbluetooth.view.fragment.TareasFragment;
import com.gru.comandroidbluetooth.view.fragment.TaskFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainPacienteActivity extends AppCompatActivity implements ICallBackListener
{
    private boolean two_panel;
    private Bundle bundle;
    private String nro_pulsera="";
    private ImageButton btn_datos,btn_estudios,btn_evolucion,btn_tareas;
    private FloatingActionButton fab;

    private InternacionModel model_paciente;

    private TextView nombre,fecha_nac_dni,fecha_ingreso,dias,habitacion;

    private ArrayList<InternacionDetalleModel> listaInternacion;

    private Fragment frag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_paciente_activity);

        if(findViewById(R.id.framePanelDerecha) != null)
        {
           two_panel = true;
        }

        if(getIntent().getExtras() != null)
        {
            nro_pulsera = getIntent().getStringExtra("pulsera");
            model_paciente    = (InternacionModel) getIntent().getExtras().get("paciente");
        }

        final DatosPacienteFragment paciente = new DatosPacienteFragment();
        bundle = new Bundle();
        bundle.putString("pulsera",nro_pulsera);
        bundle.putSerializable("paciente",model_paciente);
        paciente.setArguments(bundle);

        listaInternacion = new ArrayList<>();


        btn_datos       = findViewById(R.id.btnDatosPaciente);
        btn_estudios    = findViewById(R.id.btnEstudiosMedicos);
        btn_evolucion   = findViewById(R.id.btnEvolucion);
        btn_tareas      = findViewById(R.id.btnTareas);
        fab             = findViewById(R.id.fab);

        getSupportFragmentManager().beginTransaction().replace(R.id.framePanelDerecha,paciente).commit();
        mostrarOcultarFab(false);

        btn_datos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.framePanelDerecha,paciente).commit();
                mostrarOcultarFab(false);
            }
        });

        btn_estudios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.framePanelDerecha,new EstudiosPacienteFragment()).commit();
                mostrarOcultarFab(false);
            }
        });

        btn_evolucion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarHistorico();
                mostrarOcultarFab(true);
            }
        });

        btn_tareas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frag = new TareasFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.framePanelDerecha,frag).commit();
                mostrarOcultarFab(true);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(frag instanceof EvolucionPacienteFragment)
                {
                    Fragment fra = new EvolucionPacienteNuevaEntradaFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("id",String.valueOf(model_paciente.getId()));
                    fra.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.framePanelDerecha,fra).commit();
                }
                else if(frag instanceof TareasFragment)
                {
                    getSupportFragmentManager().beginTransaction().replace(R.id.framePanelDerecha,new TaskFragment()).commit();
                }
                mostrarOcultarFab(false);
            }
        });

        bindearCampos();

    }

    private void bindearCampos()
    {
        nombre = findViewById(R.id.txtNombrePac);
        fecha_nac_dni = findViewById(R.id.txtDni);
        fecha_ingreso = findViewById(R.id.txtFecInternacion);
        dias = findViewById(R.id.txtDiasInternacion);
        habitacion = findViewById(R.id.txtCama);

        nombre.setText(model_paciente.getPaciente().getApellido() + " "  + model_paciente.getPaciente().getNombre());
        fecha_nac_dni.setText(model_paciente.getPaciente().getFecha_nacimiento() + " | DNI:" + model_paciente.getPaciente().getDni());
        fecha_ingreso.setText(model_paciente.getCreated_at());
        String habi = verificarTextoVacio(model_paciente.getNro_habitacion());
        String cama = verificarTextoVacio(model_paciente.getCama());

        habitacion.setText("Habit " + habi + " cama " + cama);
    }

    private String verificarTextoVacio(String texto)
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
        return  resul;
    }

    private void mostrarOcultarFab(boolean muestro)
    {
        if(muestro){
            fab.show();
        }else {
            fab.hide();
        }
    }

    private void cargarHistorico()
    {
        String url = Constants.URL_BASE + Constants.URL_GET_ALL_HISTORICO + "id=" + model_paciente.getId();
        listaInternacion.clear();
        Log.e("url= ", url);
        final ProgressDialog dialog=new ProgressDialog(this);
        dialog.setMessage("Cargando Datos...");
        dialog.show();
        VolleySingleton.getInstancia(this).
                addToRequestQueue(new JsonArrayRequest(url,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                try {
                                    for (int i = 0; i < response.length(); i++) {
                                        Log.e("leng response ", "" + i);
                                        JSONObject o = response.getJSONObject(i);
                                        InternacionDetalleModel model = new InternacionDetalleModel(
                                                o.getString("created_at"), o.getString("detalle"));
                                        listaInternacion.add(model);
                                    }
                                    dialog.dismiss();
                                    llenarRecycler();
                                } catch (JSONException e) {
                                    Log.e("Error listener",e.getMessage());
                                    //e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error historila", error.getMessage());
                    }
                }));

    }

    private void llenarRecycler()
    {
        frag = new EvolucionPacienteFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("lista", listaInternacion);
        frag.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.framePanelDerecha,frag).commit();
    }

    @Override
    public void onCallBack(int estado) {
        if(frag instanceof EvolucionPacienteFragment)
        {
            if(estado== Constants.CLIENTE_ACEPTA)
            {
                cargarHistorico();
            }
            else {
                llenarRecycler();
            }
        }
        else if(frag instanceof TareasFragment)
        {

        }
        mostrarOcultarFab(true);
    }
}



