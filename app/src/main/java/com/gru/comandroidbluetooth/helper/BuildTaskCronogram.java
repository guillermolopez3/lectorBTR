package com.gru.comandroidbluetooth.helper;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.gru.comandroidbluetooth.model.TareaModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BuildTaskCronogram
{
    private static final String TAG = "BuildTaskCronogram";
    private ArrayList<TareaModel> lista;
    private static BuildTaskCronogram instance;

    public static BuildTaskCronogram getInstancia()
    {
        if(instance == null)
        {
            instance = new BuildTaskCronogram();
        }
        return instance;
    }

    private BuildTaskCronogram()
    {
        lista = new ArrayList<>();
        dummy();
    }

    public ArrayList<TareaModel> getLista() {
        return lista;
    }


    public int cantTareasPenidente() //devuelve la cantidad de tareas pendientes que tengo
    {
        int cant =0;

        for (TareaModel mo: lista)
        {
            if(mo.getEstado().equals("PENDIENTE"))
            {
                cant = cant +1;
            }
        }
        return cant;
    }

    public void limpiarLista()
    {
        lista.clear();
    }

    public void addListItem(TareaModel model)
    {
        lista.add(model);
    }


    //si esta seleccionado la tarea despues de tantas tomas
    public void cartgarTareasConRepeticionDespuesDe(TareaModel model,int unid_medi,String cant_tiempo,String tomas)
    {
        Date fecha = convertirStringEnFecha(model.getFecha_aviso(),model.getHora_aviso());
        Calendar calendar = Calendar.getInstance();

        if(fecha != null)
        {
            calendar.setTime(fecha);
            calcularCantidadDeRepeticionesHorasMin(unid_medi,calendar,cant_tiempo,tomas,model);
        }
    }

    //le paso un string con la fecha y hs seleccionada y lo convierto a Date para trabajar mejor las fechas
    private Date convertirStringEnFecha(String dia, String hora)
    {
        String fecha = dia + " " + hora;
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        Date fecha_conv = new Date();
        try
        {
            fecha_conv = format.parse(fecha);
        }catch (ParseException e)
        {
            Log.e(TAG, e.toString());
        }
        return fecha_conv;
    }

    //tipo = si es finaliza el ó es despues de x tomas. Tomas= cantdad de tomas de medicam
    private void calcularCantidadDeRepeticionesHorasMin(int hs_o_min,Calendar calendar,String cant_tiempo,String tomas,final TareaModel model)
    {
        int cant_tomas = Integer.parseInt(tomas);
        TareaModel mo;

        if(!tomas.equals(""))
        {
            for(int i=0; i<cant_tomas -1;i++)
            {
                calendar.add(hs_o_min,Integer.parseInt(cant_tiempo));
                mo = new TareaModel();
                mo.setTitulo_tarea(model.getTitulo_tarea());
                mo.setDetalle(model.getDetalle());
                mo.setEstado(model.getEstado());
                mo.setFecha_aviso(calendar.get(Calendar.DATE) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR));
                mo.setHora_aviso(calendar.get(Calendar.HOUR_OF_DAY)+ ":" + calendar.get(Calendar.MINUTE));
                lista.add(mo);
                Date nueva_fecha = new Date();
                nueva_fecha = convertirStringEnFecha(mo.getFecha_aviso(),mo.getHora_aviso());
                calendar.setTime(nueva_fecha);
                calendar.setTime(calendar.getTime());
            }
        }

    }

    public void cargarTareaConRepeticionFinaliza(TareaModel model,int unid_medi,String cant_tiempo,String finaliza)
    {
        Date fecha = convertirStringEnFecha(model.getFecha_aviso(),model.getHora_aviso());
        Date fecha_fin = convertirStringEnFecha(finaliza,"00:00");
        Calendar calendar = Calendar.getInstance();

        if(fecha != null)
        {
            calendar.setTime(fecha);
            calcularCantidadDeRepeticionesFechaFin(unid_medi,calendar,cant_tiempo,fecha_fin,model);
        }
    }

    private void calcularCantidadDeRepeticionesFechaFin(int hs_o_min,Calendar calendar,String cant_tiempo,Date fecha_fin,final TareaModel model)
    {
        TareaModel mo;
        Date nueva_fecha = new Date(2300,1,1);

        //fecha fin = 17-10 fecha_actual= 1/1/2030 cada 12 hs-----> 15/10-> 20hs, 16/10->8hs, 16/10->20hs
        while (fecha_fin.compareTo(nueva_fecha) <= 0)
        {
            calendar.add(hs_o_min,Integer.parseInt(cant_tiempo));
            mo = new TareaModel();
            mo.setTitulo_tarea(model.getTitulo_tarea());
            mo.setDetalle(model.getDetalle());
            mo.setEstado(model.getEstado());
            mo.setFecha_aviso(calendar.get(Calendar.DATE) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR));
            mo.setHora_aviso(calendar.get(Calendar.HOUR_OF_DAY)+ ":" + calendar.get(Calendar.MINUTE));
            lista.add(mo);
            nueva_fecha = convertirStringEnFecha(mo.getFecha_aviso(),mo.getHora_aviso()); //16/10
            calendar.setTime(nueva_fecha);
            calendar.setTime(calendar.getTime());
            Log.e("entro while", "" + nueva_fecha + "- fin" + fecha_fin);
            int x = fecha_fin.compareTo(nueva_fecha);
            Log.e("dif fecha", "" + x);
        }

    }

    public void cambiarEstado(int position)
    {
        lista.get(position).setEstado("FINALIZADA");
    }

    public void dummy()
    {
        TareaModel model = new TareaModel();

        Calendar calendar = Calendar.getInstance();
        model.setCreated_at(Comun.convertirDateEnString(calendar.getTime().toString()));
        model.setTitulo_tarea("Clocar ibuprofeno");
        model.setDetalle("Colocar ibuprofeno vía oral");
        model.setFecha_aviso("4/10/2018");
        model.setHora_aviso("14");
        model.setEstado("PENDIENTE");
        lista.add(model);

        model = new TareaModel();
        model.setCreated_at(calendar.getTime().toString());
        model.setTitulo_tarea("Clocar ibuprofeno");
        model.setDetalle("Colocar ibuprofeno vía oral");
        model.setFecha_aviso("3/10/2018");
        model.setHora_aviso("14");
        model.setEstado("FINALIZADA");
        lista.add(model);

    }
}
