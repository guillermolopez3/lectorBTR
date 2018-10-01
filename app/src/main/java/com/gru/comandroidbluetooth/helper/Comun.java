package com.gru.comandroidbluetooth.helper;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.gru.comandroidbluetooth.R;

public class Comun
{
    public static void showToolbar(String tittle, boolean upButton, AppCompatActivity activity)
    {
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle(tittle);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }
}
