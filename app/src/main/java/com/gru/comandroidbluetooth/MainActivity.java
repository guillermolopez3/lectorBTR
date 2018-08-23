package com.gru.comandroidbluetooth;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gru.comandroidbluetooth.ConexionBluetooth.ConectarDispositivo;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    TextView txtVincular;
    TextInputEditText mostrarLlave;
    InputStream inputStream;
    private  String valorLlave="";
    private int count=0;
    BluetoothSocket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Bienvenido");

        txtVincular  = findViewById(R.id.txtConectar);
        mostrarLlave = findViewById(R.id.txtCodigoPulsera);


        ConectarDispositivo.getInstancia(this).empezarLectura();




        txtVincular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,ListarDispositivosActivity.class));
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
