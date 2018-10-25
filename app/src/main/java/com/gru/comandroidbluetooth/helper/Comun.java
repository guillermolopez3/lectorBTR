package com.gru.comandroidbluetooth.helper;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.gru.comandroidbluetooth.R;
import com.gru.comandroidbluetooth.model.UsuarioModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class Comun
{
    public static void showToolbar(String tittle, boolean upButton, AppCompatActivity activity)
    {
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle(tittle);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }

    //convierte yyyy-mm-dd hh:mm:ss a dd-mm-yyyy hh:mm
    public static  String convertirStringEnFecha(String dia)
    {
        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss",Locale.ENGLISH);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat format_salida = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        String fecha_convertida="";
        Date fecha_conv = new Date();
        try
        {
            fecha_conv = format.parse(dia);
            fecha_convertida = format_salida.format(fecha_conv);

        }catch (ParseException e)
        {
            Log.e("DatosPacientesFragment", e.toString());
        }
        return fecha_convertida;
    }

    //convierte del formato yyyy-MM-dd que viene como string a dd-mm-yyyy devuelto como string
    public static  String convertirDateEnString(String dia)
    {
        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss",Locale.ENGLISH);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format_salida = new SimpleDateFormat("dd-MM-yyyy");
        String fecha_convertida="";
        Date fecha_conv = new Date();
        try
        {
            fecha_conv = format.parse(dia);
            fecha_convertida = format_salida.format(fecha_conv);

        }catch (ParseException e)
        {
            Log.e("DatosPacientesFragment", e.toString());
        }
        return fecha_convertida;
    }

    public static void actualizarMenu(Menu menu, Activity activity)
    {
        MenuItem menuItem = menu.findItem(R.id.menu_usuario);
        menuItem.setTitle(obtenerNombreUsuario(activity));
    }

    private static String obtenerNombreUsuario(Activity a)
    {
        SharedPreferences pref =  a.getSharedPreferences("usuario",MODE_PRIVATE);
        String nombre = pref.getString("usuario","");
        return nombre;
    }

    public static UsuarioModel obtenerDatosUsuarioLogueado(Activity a)
    {
        SharedPreferences pref =  a.getSharedPreferences("usuario",MODE_PRIVATE);
        UsuarioModel usuario = new UsuarioModel();
        usuario.setId(pref.getInt("id",0));
        usuario.setNrombre_usuario(pref.getString("usuario",""));
        usuario.setRol(pref.getString("rol",""));
        usuario.setId_rol(pref.getInt("id_rol",0));
        return usuario;
    }
}
