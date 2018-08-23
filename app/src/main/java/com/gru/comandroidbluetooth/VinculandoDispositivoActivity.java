package com.gru.comandroidbluetooth;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.gru.comandroidbluetooth.ConexionBluetooth.ConectarDispositivo;
import com.gru.comandroidbluetooth.Helper.IErrorVincular;

/* TODO: 22/08/2018

Esta es la clase que se encarga de conectar, cada vez que el soket no este conectado llama a esta clase

    * 1- Compruebo que el BT este activado
    * 2- Vinculo el dispositivo verificando que los aparatos ya esten vinculados
*/
public class VinculandoDispositivoActivity extends AppCompatActivity implements IErrorVincular {

    ProgressBar progressBar;
    ConectarDispositivo conectar;

    private final int REQUEST_ENABLE_BT         =1; //VARIABLE PARA SABER SI EL CLIENTE AL PREGUNTAR SI ACTIVA EL BLUET DICE SI O NO
    private final int REQUEST_COARSE_LOCATION   = 2; //verificar permisos en android 6+ coarse location



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vinculando_dispositivo_activity);
        getSupportActionBar().setTitle("Buscando");

        progressBar = findViewById(R.id.progressBar);

        conectar = ConectarDispositivo.getInstancia(this);


        tieneBluetoothActivado();


    }

    public void tieneBluetoothActivado()
    {
        //verifico si esta habilitado el bluetooth, si no es asi le digo al usuario si lo quiere hablitar
        if(!conectar.getmBluetoothAdapter().isEnabled()){
            Log.e("El bluet esta: ","deshabilitado");
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent,REQUEST_ENABLE_BT);
        }else {
            iniciarConexion();
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            verificarPermisos(); //lo hago para android 6+
        }
    }

    private void iniciarConexion()
    {
        conectar.vincularDispositivo();
    }

    private void verificarPermisos()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_COARSE_LOCATION);
        }
    }

    //al preguntar si tiene el bluetooth activado, aparece un dialog, si se presiona si o no, este lo atrapa eñ activityResul
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("request","" + resultCode + "-" + requestCode);
        switch (requestCode)
        {
            case REQUEST_ENABLE_BT: //resultado de preguntar al usuario si activa el BT
            {
                if(resultCode == RESULT_CANCELED)
                {
                    Toast.makeText(this,"Debe activar el bluetooth",Toast.LENGTH_LONG).show();
                    finish();
                }
                else if(resultCode == RESULT_OK){
                    iniciarConexion();
                }
            }
        }
    }

    @Override
    public void mensajeErrorVinculacion(String error) {
        Toast.makeText(this,error,Toast.LENGTH_LONG).show();
    }
}
