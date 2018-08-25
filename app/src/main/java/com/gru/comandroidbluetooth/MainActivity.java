package com.gru.comandroidbluetooth;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gru.comandroidbluetooth.ConexionBluetooth.ConectarDispositivo;
import com.gru.comandroidbluetooth.ConexionBluetooth.LeerPulseras;
import com.gru.comandroidbluetooth.Helper.ILectura;

import java.io.InputStream;


/*
    TODO
    * Para detener el hilo hago: conexion.setHilo_corriendo(false);
    * para volver a lanzarlo:
    * conexion.setHilo_corriendo(true);
                conexion.empezarLectura();
    *
 */
public class MainActivity extends AppCompatActivity {

    TextView txtVincular;
    TextInputEditText mostrarLlave;
    InputStream inputStream;
    private  String valorLlave="";
    private int count=0;
    BluetoothSocket socket;
    private ConectarDispositivo conexion;

    LeerPulseras pulseras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Bienvenido");

        conexion = ConectarDispositivo.getInstancia(this);



        Button btnHilo = findViewById(R.id.btnHilo);
        btnHilo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              conexion.setHilo_corriendo(false);
            }
        });
        Button iniciar = findViewById(R.id.btnHiloSeguir);
        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                conexion.setHilo_corriendo(true);
                conexion.empezarLectura();
            }
        });
    }

    void empezarLectura()
    {
        final boolean termino=false;
        final Handler handler = new Handler();


        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted() && !termino)
                {
                    try {
                        int byteAvaiable = inputStream.available();
                        if(byteAvaiable>0)
                        {
                            byte[] paketByte= new byte[byteAvaiable];
                            inputStream.read(paketByte);

                            valorLlave+= convertirValorObtenido(paketByte);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mostrarValor();
                                }
                            });
                        }
                    }catch (Exception e){
                        Log.e("Error de byte", "fallo");}
                }

            }
        });
        hilo.start();
    }
    private void mostrarValor()
    {
        if(count==0){
            count++;
        }else {
            Toast.makeText(this,"valor= " + valorLlave,Toast.LENGTH_LONG).show();
            valorLlave="";
            count=0;
        }

    }

    private void pararHilo()
    {
        conexion.setHilo_corriendo(false);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.e("estoy en","on post resume");
        if(conexion.getSocket() !=null)
        {
            conexion.setHilo_corriendo(true);
            conexion.empezarLectura();
            Log.e("conexion","soket vinculado");
        }else {
            Log.e("conexion","soket null");
            //conexion.setHilo_corriendo(true);
            Intent i = new Intent(MainActivity.this,VinculandoDispositivoActivity.class);

            startActivity(i);
            finish();
            Toast.makeText(this,"Se perdió la conexión, reconectando",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        pararHilo();
    }

    @Override
    protected void onStop() {
        super.onStop();
        pararHilo();
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


}
