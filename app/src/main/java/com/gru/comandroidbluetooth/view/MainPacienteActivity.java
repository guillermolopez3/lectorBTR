package com.gru.comandroidbluetooth.view;



import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.gru.comandroidbluetooth.R;
import com.gru.comandroidbluetooth.backend.VolleySingleton;
import com.gru.comandroidbluetooth.helper.Comun;
import com.gru.comandroidbluetooth.helper.Constants;
import com.gru.comandroidbluetooth.helper.ICallBackListener;
import com.gru.comandroidbluetooth.model.InternacionDetalleModel;
import com.gru.comandroidbluetooth.model.InternacionModel;
import com.gru.comandroidbluetooth.model.TaskModel;
import com.gru.comandroidbluetooth.model.UsuarioModel;
import com.gru.comandroidbluetooth.view.fragment.DatosPacienteFragment;
import com.gru.comandroidbluetooth.view.fragment.EstudiosPacienteFragment;
import com.gru.comandroidbluetooth.view.fragment.EvolucionPacienteFragment;
import com.gru.comandroidbluetooth.view.fragment.EvolucionPacienteNuevaEntradaFragment;
import com.gru.comandroidbluetooth.view.fragment.TareasFragment;
import com.gru.comandroidbluetooth.view.fragment.NuevaTareaFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainPacienteActivity extends AppCompatActivity implements ICallBackListener
{
    private boolean two_panel;
    private Bundle bundle;
    private String nro_pulsera="";
    private Button btn_datos,btn_estudios,btn_evolucion,btn_tareas;
    private FloatingActionButton fab;

    private InternacionModel model_paciente;

    private TextView nombre,fecha_nac_dni,fecha_ingreso,dias,habitacion;

    private ArrayList<InternacionDetalleModel> listaInternacion;
    private ArrayList<TaskModel> lista_task;

    private Fragment frag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_paciente_activity);

        Comun.showToolbar("Datos del paciente",false,this);
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
        lista_task = new ArrayList<>();


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
                cambiarFondoColorBoton(btn_datos,btn_estudios,btn_evolucion,btn_tareas);
            }
        });

        btn_estudios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.framePanelDerecha,new EstudiosPacienteFragment()).commit();
                mostrarOcultarFab(false);
                cambiarFondoColorBoton(btn_estudios,btn_datos,btn_evolucion,btn_tareas);
            }
        });

        btn_evolucion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarHistorico();
                mostrarOcultarFab(true);
                cambiarFondoColorBoton(btn_evolucion,btn_estudios,btn_datos,btn_tareas);
            }
        });

        btn_tareas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //frag = new TareasFragment();
               // getSupportFragmentManager().beginTransaction().replace(R.id.framePanelDerecha,frag).commit();
                cargarTareas();
                mostrarOcultarFab(false);
                cambiarFondoColorBoton(btn_tareas,btn_datos,btn_evolucion,btn_estudios);
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
                    getSupportFragmentManager().beginTransaction().replace(R.id.framePanelDerecha,new NuevaTareaFragment()).commit();
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
        fecha_nac_dni.setText(Comun.convertirDateEnString(model_paciente.getPaciente().getFecha_nacimiento()) + " | DNI:" + model_paciente.getPaciente().getDni());
        fecha_ingreso.setText(Comun.convertirStringEnFecha(model_paciente.getCreated_at()));
        dias.setText(verificarTextoVacio(model_paciente.getDias_internados()));
        String habi = verificarTextoVacio(model_paciente.getNro_habitacion());
        String cama = verificarTextoVacio(model_paciente.getCama());

        habitacion.setText("Habit " + habi + " cama " + cama);
    }

    private void cambiarFondoColorBoton(Button con_color,Button sin_c1,Button sin_c2,Button sin_c3)
    {
        con_color.setBackgroundResource(R.color.fondo_botones_menu);
        sin_c1.setBackgroundResource(R.color.transparente);
        sin_c2.setBackgroundResource(R.color.transparente);
        sin_c3.setBackgroundResource(R.color.transparente);
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

   /* private void cargarHistorico()
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
                                        InternacionDetalleModel model = new InternacionDetalleModel( o.getString("detalle"),
                                                o.getString("created_at"),o.getInt("id_rol"),o.getString("rol"),
                                                o.getString("usuario"));
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



    }*/

    private void cargarHistorico()
    {
        String URL_C = Constants.URL_BASE + Constants.URL_GET_ALL_HISTORICO + "id=" + model_paciente.getId();
        listaInternacion.clear();
        Log.e("url",URL_C);
        final ProgressDialog dialog=new ProgressDialog(this);
        dialog.setMessage("Cargando Datos...");
        dialog.show();
        VolleySingleton.getInstancia(this).
                addToRequestQueue(new StringRequest(Request.Method.GET, URL_C,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                dialog.dismiss();
                                try {
                                    Log.e("response", response);
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray array= jsonObject.getJSONArray("data");

                                    for(int i=0; i<array.length();i++)
                                    {
                                        JSONObject o = array.getJSONObject(i);

                                        int id = 0;

                                        if(o.has("id"))
                                        {
                                            o.getInt("id");
                                        }



                                        if(id == -1)
                                        {
                                           // Toast.makeText(MainPacienteActivity.this, "Error al bu", Toast.LENGTH_SHORT).show();
                                        }else {
                                            InternacionDetalleModel model = new InternacionDetalleModel( o.getString("detalle"),
                                                    o.getString("created_at"),o.getInt("id_rol"),o.getString("rol"),
                                                    o.getString("usuario"));
                                            listaInternacion.add(model);
                                        }
                                    }
                                    llenarRecycler();

                                } catch (JSONException e)
                                {
                                    Log.e("Error historila", e.getMessage());
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Log.e("Error historila", error.getMessage());
                    }
                }));
    }


    private void cargarTareas()
    {
        String URL_C = Constants.URL_BASE + Constants.URL_GET_ALL_TAREAS + "?id_inter=" + model_paciente.getId();
        lista_task.clear();
        Log.e("url",URL_C);
        final ProgressDialog dialog=new ProgressDialog(this);
        dialog.setMessage("Cargando Datos...");
        dialog.show();
        VolleySingleton.getInstancia(this).
                addToRequestQueue(new StringRequest(Request.Method.GET, URL_C,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                dialog.dismiss();
                                try {
                                    Log.e("response", response);
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray array= jsonObject.getJSONArray("data");

                                    for(int i=0; i<array.length();i++)
                                    {
                                        JSONObject o = array.getJSONObject(i);

                                        TaskModel task = new TaskModel(o.getInt("id"),o.getString("fecha"),
                                                o.getString("titulo"),o.getString("detalle"),o.getString("estado"),
                                                o.getInt("id_rol"),o.getString("rol"));

                                            lista_task.add(task);
                                    }
                                    abrirFragmentTareas();

                                } catch (JSONException e)
                                {
                                    //Log.e("Error tarea", e.getMessage());
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        //Log.e("Error tarea", error.getMessage());
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

    private void abrirFragmentTareas()
    {
        frag = new TareasFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("lista",lista_task);
        bundle.putInt("id_inter",model_paciente.getId());
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
            else{
                llenarRecycler();
            }
            mostrarOcultarFab(true);
        }
        else if(frag instanceof TareasFragment)
        {
            if(estado== Constants.CLIENTE_ACEPTA)
            {
                cargarTareas();
            }
            else {
                abrirFragmentTareas();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_usuario,menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Comun.actualizarMenu(menu,this);
        return super.onPrepareOptionsMenu(menu);
    }
}



