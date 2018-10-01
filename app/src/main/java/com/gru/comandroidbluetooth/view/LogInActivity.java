package com.gru.comandroidbluetooth.view;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.gru.comandroidbluetooth.R;

public class LogInActivity extends AppCompatActivity
{
    Button btnIngresar;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        linearLayout = findViewById(R.id.linear);
        btnIngresar       = findViewById(R.id.btnIngresar);

        tieneBluetooth();

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(LogInActivity.this,VinculandoDispositivoActivity.class));
               // startActivity(new Intent(LogInActivity.this,MainPacienteActivity.class));
                //startActivity(new Intent(LogInActivity.this,RegistrarPulseraActivity.class));
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
}
