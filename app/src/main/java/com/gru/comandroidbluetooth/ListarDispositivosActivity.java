package com.gru.comandroidbluetooth;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.gru.comandroidbluetooth.ConexionBluetooth.ConectarDispositivo;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;


public class ListarDispositivosActivity extends AppCompatActivity
{
    private final int REQUEST_ENABLE_BT=1;
    private ListView listView;
    private ArrayList<String> mDeviceList;
    private ArrayAdapter<String> adapter;
    private BluetoothAdapter mBluetoothAdapter;
    Set<BluetoothDevice> pairedDeviace;
    BluetoothSocket socket;
    BluetoothDevice lector;
    private UUID uuid = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    InputStream inputStream;

    private  String valorLlave="";
    private int count=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mostrar_dispositivos_activity);


        getSupportActionBar().setTitle("Lector de llaves");

        listView        = findViewById(R.id.listView);

        mDeviceList     = new ArrayList<>();
        adapter         = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,mDeviceList);
        listView.setAdapter(adapter);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(mBluetoothAdapter==null){
            Toast.makeText(this,"Dispositivo sin bluetooth",Toast.LENGTH_LONG);
        }

        //verifico si esta habilitado el bluetooth, si no es asi le digo al usuario si lo quiere hablitar
        if(!mBluetoothAdapter.isEnabled()){
            Log.e("El bluet esta: ","deshabilitado");
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent,REQUEST_ENABLE_BT);
        }
        else {
            Log.e("El bluet esta: ","habilitado");
        }

        pairedDeviace = mBluetoothAdapter.getBondedDevices(); //obtengo los disposit ya vinculados

        if(pairedDeviace.size()>0)
        {
            for(BluetoothDevice device : pairedDeviace) //recorro la lista y los muestroen el listView
            {
                Log.e("nombre del disposit:", device.getAddress());
                //mDeviceList.add(device.getAddress());
                mDeviceList.add(device.getName());
                lector=device;
            }
            adapter.notifyDataSetChanged();
        }
        else {
            Log.e("Disposit ya vinculados","0");
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(ListarDispositivosActivity.this,lector.getAddress(),Toast.LENGTH_LONG).show();
                Toast.makeText(ListarDispositivosActivity.this,lector.getName(),Toast.LENGTH_LONG).show();
                //conectar();
                //ConectarDispositivo.getInstancia().conectar(lector);
                finish();

            }
        });


    }

   /* public void conectar()
    {
        BluetoothSocket tmp=null;
        try{

            Method m = lector.getClass().getMethod("createRfcommSocket",new Class[]{int.class});
            tmp= (BluetoothSocket) m.invoke(lector,Integer.valueOf(1));


        }catch (Exception e){Log.e("la conexion del soket", "fallo");}
        socket=tmp;
        try{
            socket.connect();
        }catch (Exception e){Log.e("la conexion del soket 1", "fallo");}

        try{
            inputStream= socket.getInputStream();

        }catch (IOException e){Log.e("la conexion del input", "fallo" + e.toString());}
        empezarLectura();

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
    }*/





}



