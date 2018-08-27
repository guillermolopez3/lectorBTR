package com.gru.comandroidbluetooth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.gru.comandroidbluetooth.ConexionBluetooth.ConectarDispositivo;
import com.gru.comandroidbluetooth.Helper.IConexionBT;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leer_pulsera);

        getSupportActionBar().setTitle("Bienvenido");

        conexion = ConectarDispositivo.getInstancia(this);
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
        Log.e("main act","on pause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        pararHilo();
        Log.e("main act","on stop");
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
        Log.e("Main activity", "conexion correcta");
    }

    @Override
    public void errorVincular(String mensaje) {

    }

    @Override
    public void idPulsera(String nro_pulsera) {
        Intent i = new Intent(this,MainPacienteActivity.class);
        i.putExtra("pulsera",nro_pulsera);
        startActivity(i);
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
}
