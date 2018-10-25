package com.gru.comandroidbluetooth.view;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gru.comandroidbluetooth.R;
import com.gru.comandroidbluetooth.backend.VolleySingleton;
import com.gru.comandroidbluetooth.helper.Constants;
import com.gru.comandroidbluetooth.model.DatosPacienteModel;
import com.gru.comandroidbluetooth.model.InternacionModel;
import com.gru.comandroidbluetooth.model.UsuarioModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LogInActivity extends AppCompatActivity
{
    private Button btnIngresar;
    private LinearLayout linearLayout;
    private String usuario,pass;
    private TextInputEditText edtUsuario,edtPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        linearLayout = findViewById(R.id.linear);
        btnIngresar  = findViewById(R.id.btnIngresar);
        edtUsuario   = findViewById(R.id.inpUsuario);
        edtPass      = findViewById(R.id.inpPass);
        tieneBluetooth();

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //startActivity(new Intent(LogInActivity.this,VinculandoDispositivoActivity.class));
               // startActivity(new Intent(LogInActivity.this,MainPacienteActivity.class));
                //startActivity(new Intent(LogInActivity.this,RegistrarPulseraActivity.class));
                usuario = edtUsuario.getText().toString();
                pass    = edtPass.getText().toString();
                if(usuario.isEmpty() || pass.isEmpty()){
                    Toast.makeText(LogInActivity.this, "El usuario y la contraseña no deben estar vacios", Toast.LENGTH_SHORT).show();
                }
                else {
                    obtenerPerfil();
                }
            }
        });
    }

    //consulto, si no tiene bluetooth, no ingresa
    private void tieneBluetooth()
    {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if(adapter ==null)
        {
            btnIngresar.setEnabled(false);
            Snackbar.make(linearLayout,"El dispositivo no cuenta con Bluetooth",Snackbar.LENGTH_INDEFINITE)
                    .setAction("Salir", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                        }
                    }).show();
        }else {
            btnIngresar.setEnabled(true);
        }
    }

    private void obtenerPerfil() // si voy a ver el perfil, lo cargo
    {
        String URL_C = Constants.URL_BASE + Constants.URL_COMPROBAR_USUARIO + "?user="+ usuario +"&pass="+ pass;
        Log.e("url",URL_C);
        final ProgressDialog dialog=new ProgressDialog(LogInActivity.this);
        dialog.setMessage("Comprobando datos...");
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

                                        int id = o.getInt("id");

                                        if(id == -1)
                                        {
                                            Toast.makeText(LogInActivity.this, "Usuario y Contraseña Incorrectos", Toast.LENGTH_SHORT).show();
                                        }else {
                                            UsuarioModel us = new UsuarioModel(
                                                    o.getInt("id"),
                                                    o.getString("nombre_usuario"),
                                                    o.getString("rol"),
                                                    o.getInt("id_rol")
                                            );
                                            registrarUsuarioSharePref(us);
                                        }
                                    }

                                } catch (JSONException e)
                                {
                                    Log.e("error al consultar", e.toString());
                                    Toast.makeText(LogInActivity.this, "Error en la conexiión a internet", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Toast.makeText(LogInActivity.this, "Error en la conexiión a internet", Toast.LENGTH_SHORT).show();
                        Log.e("error al cargar perfil",error.toString());
                    }
                }));
    }

    private void registrarUsuarioSharePref(UsuarioModel u)
    {
        SharedPreferences pref = getSharedPreferences("usuario",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("id",u.getId());
        editor.putString("usuario",u.getNrombre_usuario());
        editor.putString("rol",u.getRol());
        editor.putInt("id_rol",u.getId_rol());
        editor.commit();
        startActivity(new Intent(LogInActivity.this,VinculandoDispositivoActivity.class));
    }

}
