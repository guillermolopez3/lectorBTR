package com.gru.comandroidbluetooth.ConexionBluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.gru.comandroidbluetooth.Helper.Constants;
import com.gru.comandroidbluetooth.Helper.IErrorVincular;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.file.FileAlreadyExistsException;
import java.util.Set;

public class ConectarDispositivo
{
    private static ConectarDispositivo instancia;
    private Activity activity;
    private BluetoothDevice lector = null;

    private IErrorVincular iErrorVincular;

    private int cantidad_reconexiones=0;



    BluetoothSocket socket;
    InputStream inputStream;
    private  String valorLlave="";
    private int count=0;


    private BluetoothAdapter mBluetoothAdapter;



    public static ConectarDispositivo getInstancia(Activity ac)
    {
        if(instancia == null){
            instancia = new ConectarDispositivo(ac);
        }
        return instancia;
    }


    public BluetoothAdapter getmBluetoothAdapter() {
        return mBluetoothAdapter;
    }

    private ConectarDispositivo(Activity activity)
    {
        this.activity=activity;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(activity instanceof IErrorVincular)
        {
            iErrorVincular = (IErrorVincular)activity;
        }


        /*if(mBluetoothAdapter==null){

        }

        //verifico si esta habilitado el bluetooth, si no es asi le digo al usuario si lo quiere hablitar
        if(!mBluetoothAdapter.isEnabled()){
            Log.e("El bluet esta: ","deshabilitado");
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
             activity.startActivityForResult(enableBtIntent,REQUEST_ENABLE_BT);
        }
        else {
            Log.e("El bluet esta: ","habilitado");
        }

        pairedDeviace = mBluetoothAdapter.getBondedDevices(); //obtengo los disposit ya vinculados

        if(pairedDeviace.size()>0)
        {
            for(BluetoothDevice device : pairedDeviace) //recorro la lista y los muestroen el listView
            {
                lector=device;
            }

        }
        else {
            Log.e("Disposit ya vinculados","0");
        }*/
    }

    //busca si los aparatitos estan vinculados en android, si es asi inicia la conexion
    public void vincularDispositivo()
    {
        Set<BluetoothDevice> pairedDeviace = mBluetoothAdapter.getBondedDevices(); //obtengo los disposit ya vinculados

        if(pairedDeviace.size()>0)
        {
            for(BluetoothDevice device : pairedDeviace) //recorro la lista verificando que esten vinculados
            {
                if(device.getAddress().equals(Constants.MAC_LECTOR_1) || device.getAddress().equals(Constants.MAC_LECTOR_2))
                { //si son dispositivos aceptados, inicio la conección
                    lector=device;
                    conectar();
                    return;
                }
            }
            if(lector == null)
            {
                iErrorVincular.mensajeErrorVinculacion("Los dispositivos vinculados no corresponden a los autorizados");
            }

        }
        else { //no tengo dispositivos vinculados
            iErrorVincular.mensajeErrorVinculacion("No hay dispositivos vinculados en el sistema");
        }
    }


    public void conectar()
    {
        Thread hi = new Thread(new Runnable() {
            @Override
            public void run() {

                BluetoothSocket tmp = null;

                try{
                    tmp = lector.createInsecureRfcommSocketToServiceRecord(Constants.UUID_SEGURO);
                }catch (Exception e)
                {
                    Log.e("error","soket" + e.getMessage());
                    conectar();
                }

                socket = tmp;

                try {
                    socket.connect();
                    if(socket.isConnected())
                    {
                        Log.e("estado soket: ","conectado:");
                    }
                    //leer();

                }catch (Exception ex){
                    Log.e("error","conectar:" + ex.getMessage());
                    conectar();
                }


            }
        });
        if(cantidad_reconexiones <6) //intento la reconexión hasta 6 veces
        {
            hi.start();
        }else {
            iErrorVincular.mensajeErrorVinculacion("No se puede vincular el dispositivo, verifique que el mismo este encendido o cerca de la tablet ");
        }
   }

    public InputStream getInputStream() {
        return inputStream;
    }

    public BluetoothSocket getSocket() {
        return socket;
    }

    public void empezarLectura()
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
                    }catch (Exception e){Log.e("Error de byte", "fallo");}
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
            //Toast.makeText(this,"valor= " + valorLlave,Toast.LENGTH_LONG).show();
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
