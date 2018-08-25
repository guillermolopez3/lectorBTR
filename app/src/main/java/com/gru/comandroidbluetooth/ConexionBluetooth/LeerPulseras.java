package com.gru.comandroidbluetooth.ConexionBluetooth;

import android.util.Log;

public class LeerPulseras extends Thread
{
    private boolean leo=true;
    public LeerPulseras(){}


    @Override
    public void run() {
        super.run();
        while (leo)
        {
            Log.e("Hilo Pulsera", "leyendo");
            try
            {
                sleep(1000);
            } catch (InterruptedException ex){}

        }

    }

    public boolean getLeo() {
        return leo;
    }

    public void setLeo(boolean leo) {
        this.leo = leo;
    }
}
