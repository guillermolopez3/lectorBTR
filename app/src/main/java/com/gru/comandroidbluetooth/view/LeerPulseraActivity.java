package com.gru.comandroidbluetooth.view;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gru.comandroidbluetooth.R;
import com.gru.comandroidbluetooth.backend.VolleySingleton;
import com.gru.comandroidbluetooth.conexionBluetooth.ConectarDispositivo;
import com.gru.comandroidbluetooth.helper.Comun;
import com.gru.comandroidbluetooth.helper.Constants;
import com.gru.comandroidbluetooth.helper.IConexionBT;
import com.gru.comandroidbluetooth.model.DatosPacienteModel;
import com.gru.comandroidbluetooth.model.InternacionModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/*
    TODO
    * Para detener el hilo hago: conexion.setHilo_corriendo(false);
    * para volver a lanzarlo:
    * conexion.setHilo_corriendo(true);
                conexion.empezarLectura();
    *
 */
public class LeerPulseraActivity extends AppCompatActivity implements IConexionBT {

    private ConectarDispositivo conexion;
    private ArrayList<InternacionModel> lista;
    private String nro_pulsera = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leer_pulsera);

        Comun.showToolbar("Gestión de pacientes",false,this);

        conexion = ConectarDispositivo.getInstancia(this);
        conexion.setActivity(this);
        lista = new ArrayList<>();
    }




    private void pararHilo()
    {
        conexion.setHilo_corriendo(false);
    }

    //metodo que se dispara en dos oportunidades, cuando inicia, luego del oncreate y cuando vuelve la actividad de segundo plano
    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.e("estoy en","on post resume");

        if(conexion.getSocket() != null)
        {
            conexion.setHilo_corriendo(true);
            conexion.empezarLectura();
            Log.e("onResume","soket SIGUE vinculado");
        }else {
            Log.e("onResume","soket no vinculado");
            reconectar();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        pararHilo();
        Log.e("leer pul","on pause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        pararHilo();
        Log.e("leer pul","on stop");
    }

    private void reconectar() //al perder la conexion intentamos recnectar
    {
        conexion.finalizarObjeto();
        Toast.makeText(this,"Se perdió la conexión, reconectando",Toast.LENGTH_LONG).show();
        Intent i = new Intent(LeerPulseraActivity.this,VinculandoDispositivoActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        startActivity(i);
        this.finish();
    }



    private String convertirValorObtenido(byte [] data)
    {
        StringBuilder sb = new StringBuilder(data.length *3);
        for(byte b : data)
        {
            sb.append(String.format("%02X ",b));
        }
        Log.e("valor",sb.toString());
        return sb.toString();
    }


    @Override
    public void coneccionCorrecta(Boolean conectado) {
        Log.e("Leer pulsera", "conexion correcta");
    }

    @Override
    public void errorVincular(String mensaje) {

    }

    @Override
    public void idPulsera(String nro_pulsera) {
       //this.nro_pulsera = nro_pulsera;
       convertirDecimal(nro_pulsera);
       //para mostrar un dialog en el hilo ppal tengo que llamar al hilo de la view
       runOnUiThread(new Runnable() {
           @Override
           public void run() {
               obtenerPerfil();
           }
       });

    }

    private void convertirDecimal(String nro_pulsera_exa)
    {
        try{
            String x = nro_pulsera_exa.substring(6,14);//ayud memo, subStr(inicio lect,fin no inclus) ej Hola subs(0,2)=Ho
            int hexa = Integer.parseInt(nro_pulsera_exa.substring(6,14),16);//convierto el valor a decimal
            String nro_decimal = String.format("%010d",hexa);
            Log.e("DECIMAL","" + nro_decimal);
            this.nro_pulsera = nro_decimal;

        }catch (Exception e){Log.e("excep conv decimal",e.toString());}
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try{
            conexion.getSocket().close();
            conexion.finalizarObjeto();
            finish();
        }catch (Exception e){}

    }

    //METODOS PPARA TRAER LOS DATOS DE INTERNET Y MOSTRARLOS
    private void obtenerPerfil() // si voy a ver el perfil, lo cargo
    {
        lista.clear();
        Log.e("url= ", Constants.URL_BASE + Constants.URL_BUSCAR_PACIENTE_CON_PULSERA + "pulsera=" + nro_pulsera);
        final ProgressDialog dialog=new ProgressDialog(LeerPulseraActivity.this);
        dialog.setMessage("Cargando datos...");
        dialog.show();
        VolleySingleton.getInstancia(this).
                addToRequestQueue(new StringRequest(Request.Method.GET,
                        Constants.URL_BASE + Constants.URL_BUSCAR_PACIENTE_CON_PULSERA + "pulsera=" + nro_pulsera,
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

                                        DatosPacienteModel post = new DatosPacienteModel(
                                                o.getInt("id_paciente"),
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
                                                o.getString("domicilio_contacto"),
                                                o.getString("edad")
                                        );
                                        InternacionModel internacion = new InternacionModel(
                                                o.getInt("id_internacion"),
                                                o.getString("created_at"),
                                                o.getString("dias_internado"),
                                                o.getString("nro_internacion"),
                                                post,
                                                o.getString("nro_pulsera"),
                                                o.getString("nro_habitacion"),
                                                o.getString("cama"),
                                                o.getString("observacion"),
                                                o.getString("motivo_consulta")

                                        );
                                        lista.add(internacion);
                                    }
                                    abrirPerfil(lista);
                                } catch (JSONException e)
                                {
                                     Log.e("error al consultar", e.toString());
                                    Toast.makeText(LeerPulseraActivity.this,"Error al obtener los datos del servidor. Verificar conexión de internet",Toast.LENGTH_LONG).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Toast.makeText(LeerPulseraActivity.this,"Error al obtener los datos del servidor. Verificar conexión de internet",Toast.LENGTH_LONG).show();
                        Log.e("error al cargar perfil",error.toString());
                    }
                }));
    }

    private void abrirPerfil(ArrayList<InternacionModel> pos) //una vez obtenido el perfil, voy al activity
    {
        if(lista.size()==0)
        {
            //dialog().show();
            Toast.makeText(this, "La pulsera no se encuentra registrada", Toast.LENGTH_LONG).show();
        }
        else {
            Intent i = new Intent(LeerPulseraActivity.this,MainPacienteActivity.class);
            i.putExtra("paciente",pos.get(0));
            i.putExtra("pulsera",nro_pulsera);
            startActivity(i);
        }
    }

    public AlertDialog dialog()
    {
        Log.e("Estoy en ","dialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(LeerPulseraActivity.this);

        builder.setTitle("Pulsera no registrada")
                .setMessage("¿Desea registrar la pulsera?")
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //abrir activity para registrar
                        Intent in = new Intent(LeerPulseraActivity.this,RegistrarPulseraActivity.class);
                        in.putExtra("pulsera",nro_pulsera);
                        startActivity(in);
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(LeerPulseraActivity.this,"Pulsera No Registrada",Toast.LENGTH_LONG).show();
                    }
                });
        return builder.create();
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
