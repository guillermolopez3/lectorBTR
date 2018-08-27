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
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;


public class MainPacienteActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_paciente_activity);


        getSupportActionBar().setTitle("Main Pacientes");

        String x = getIntent().getStringExtra("pulsera");
        TextView textView = findViewById(R.id.mainPaciente);

        textView.setText(x);

    }

}



