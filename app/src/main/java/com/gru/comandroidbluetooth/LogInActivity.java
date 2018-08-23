package com.gru.comandroidbluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LogInActivity extends AppCompatActivity
{
    Button btnIngresar;
    ConstraintLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        coordinatorLayout = findViewById(R.id.coordinator);
        btnIngresar       = findViewById(R.id.btnIngresar);

        tieneBluetooth();

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LogInActivity.this,VinculandoDispositivoActivity.class));
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
            Snackbar.make(coordinatorLayout,"El dispositivo no cuenta con Bluetooth",Snackbar.LENGTH_INDEFINITE)
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
