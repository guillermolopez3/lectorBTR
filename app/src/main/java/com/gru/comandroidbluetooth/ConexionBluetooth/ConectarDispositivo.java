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
import com.gru.comandroidbluetooth.Helper.IConexionCorrecta;
import com.gru.comandroidbluetooth.Helper.IErrorVincular;
import com.gru.comandroidbluetooth.Helper.ILectura;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.file.FileAlreadyExistsException;
import java.util.Set;

import static java.lang.Thread.sleep;

public class ConectarDispositivo
{
    private static ConectarDispositivo instancia;
    private Activity activity;
    private BluetoothDevice lector = null;

    private IErrorVincular iErrorVincular;
    private IConexionCorrecta iConexionCorrecta;

    private int cantidad_reconexiones=0; //variable para reconectar hasta 6 veces antes de avisar
    private boolean hilo_corriendo = true;



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

    private ConectarDispositivo(Activity activity)
    {
        this.activity=activity;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(activity instanceof IErrorVincular && activity instanceof IConexionCorrecta)
        {
            iErrorVincular      = (IErrorVincular)activity;
            iConexionCorrecta   = (IConexionCorrecta)activity;
        }


    }

    public BluetoothAdapter getmBluetoothAdapter() {
        return mBluetoothAdapter;
    }



    public InputStream getInputStream() {
        return inputStream;
    }

    public BluetoothSocket getSocket() {
        return socket;
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
                        //empezarLectura();
                        iConexionCorrecta.coneccionCorrecta(true);
                    }

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


    public void empezarLectura()
    {
        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream tmpInputStream = null;

                try{
                    tmpInputStream = socket.getInputStream();
                }catch (IOException e){Log.e("Error","input" + e.getMessage());}

                inputStream = tmpInputStream;

                int bytes;

                while (hilo_corriendo)
                {
                    try{

                        byte[] buffer = new byte[inputStream.available()];
                        bytes = inputStream.read(buffer);
                        //handler.obtainMessage(Constant.MSG_LEER,bytes,-1,buffer).sendToTarget();
                        Log.e("Leyendo","pulseras");
                        //iLectura.lecturaPulsera("prueba");
                        sleep(1000);
                    }catch (IOException e){}
                    catch (InterruptedException ex){}

                }
            }
        });
        hilo.setName("lectura");
        hilo.start();
    }

    public boolean getHilo_corriendo() {
        return hilo_corriendo;
    }

    public void setHilo_corriendo(boolean hilo_corriendo) {
        this.hilo_corriendo = hilo_corriendo;
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
