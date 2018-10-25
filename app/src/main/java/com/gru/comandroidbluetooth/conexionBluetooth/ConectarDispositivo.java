package com.gru.comandroidbluetooth.conexionBluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.gru.comandroidbluetooth.helper.Constants;
import com.gru.comandroidbluetooth.helper.IConexionBT;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import static java.lang.Thread.sleep;

public class ConectarDispositivo
{
    private static ConectarDispositivo instancia;
    private Activity activity;
    private BluetoothDevice lector = null;

    private IConexionBT iConexionBT;

    private int cantidad_reconexiones=0; //variable para reconectar hasta 6 veces antes de avisar
    private boolean hilo_corriendo = true;



    BluetoothSocket socket;
    InputStream inputStream;
    private  String valorLlave="";
    private int count=0;


    private BluetoothAdapter mBluetoothAdapter;

    public void setActivity(Activity activity) {
        iConexionBT = (IConexionBT)activity;
    }

    public static ConectarDispositivo getInstancia(Activity a)
    {
       if(instancia == null){
                instancia = new ConectarDispositivo(a);
       }
        return instancia;
    }

    private ConectarDispositivo(Activity activity)
    {
        this.activity=activity;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(activity instanceof IConexionBT)
        {
            iConexionBT   = (IConexionBT)activity;
        }

        Log.e("activity conec", activity.getLocalClassName());

    }

    public BluetoothAdapter getmBluetoothAdapter() {
        return mBluetoothAdapter;
    }

    public BluetoothDevice getBluetDeviace() {
        return lector;
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
                iConexionBT.errorVincular("Los dispositivos vinculados no corresponden a los autorizados");
            }

        }
        else { //no tengo dispositivos vinculados
            iConexionBT.errorVincular("No hay dispositivos vinculados en el sistema");
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
                        iConexionBT.coneccionCorrecta(true);
                    }

                }catch (Exception ex){
                    Log.e("error","conectar:" + ex.getMessage());
                    conectar();
                }


            }
        });
        hi.start();
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
                        sleep(500); //no sacar, de esta manera lee todos los nros d ela pulsera de una vez y no en dos partes
                        String mensaje = mostrarMensaje(buffer);//no eliminar, hace que no lea todos ceros cuando no lee
                        if(!mensaje.isEmpty()){
                            if(mensaje.length()==16){
                                iConexionBT.idPulsera(mensaje);
                            }
                        }
                        Log.e("mensaje leido",mensaje);
                    }catch (IOException e){
                        Log.e("exception","while corriendo hilo ioex");
                    }
                    catch (InterruptedException ex){
                        Log.e("exception","interrupted excep");
                    }

                }
            }
        });
        hilo.setName("lectura");
        hilo.start();
    }

    private String mostrarMensaje(byte[] data) //convierto los bytes leido en codifo hexa
    {

        StringBuilder sb = new StringBuilder(data.length *3);
        for(byte b : data)
        {
           sb.append(String.format("%02X ",b));
        }
        Log.e("valor",sb.toString());
        return quitarEspacioNroPulsera(sb.toString());
    }

    private String quitarEspacioNroPulsera(String men)
    {
        String nuevo_numero= men.replace(" ","");
        Log.e("nro pulsera sin espacio", nuevo_numero);
       // conviertoADecimal(nuevo_numero);
        return nuevo_numero;
    }

    private void conviertoADecimal(String nro_en_hexa)
    {
        try{
            //int hexa = Integer.parseInt(nro_en_hexa.substring(6,8),16);
            double hexa = Integer.parseInt(nro_en_hexa,16);
            hexa = (Double) hexa;
            //String decimal = String.valueOf(hexa,10).
            Log.e("DECIMAL","" + hexa);

        }catch (Exception e){Log.e("excep conv decimal",e.toString());}
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

    public void finalizarObjeto() //metodo que finaliza el objeto para q al volver en el on resume se cree de nuevo y se lo pase a la interface la actividad q lo llama
    {
        instancia = null;
    }
}
