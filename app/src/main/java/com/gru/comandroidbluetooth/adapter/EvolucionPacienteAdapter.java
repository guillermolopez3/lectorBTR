package com.gru.comandroidbluetooth.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gru.comandroidbluetooth.R;
import com.gru.comandroidbluetooth.model.InternacionDetalleModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EvolucionPacienteAdapter extends RecyclerView.Adapter<EvolucionPacienteAdapter.EvolucionViewModel>
{
    private Activity activity;
    private ArrayList<InternacionDetalleModel> data;

    public EvolucionPacienteAdapter(Activity activity,ArrayList<InternacionDetalleModel> data)
    {
        this.activity=activity;
        this.data = data;
    }

    @NonNull
    @Override
    public EvolucionViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_list_evolucion_paciente,parent,false);
        return new EvolucionViewModel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EvolucionViewModel holder, int position) {
        InternacionDetalleModel model = data.get(position);

        //holder.doctor.setText(model.getDoctor());
        holder.descripcion.setText(model.getDetalle());

        holder.fecha.setText(model.getCreated_at());
    }


    private String fecha(Date fecha_ing)
    {
        String fecha = "/ /";
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            fecha = format.format(fecha_ing);
        }catch (Exception ex){
            Log.e("Error al conv: ","exception parse date: " + ex.getMessage());}
        return fecha;
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    class EvolucionViewModel extends RecyclerView.ViewHolder
    {
        TextView doctor, descripcion, fecha;
        public EvolucionViewModel(View itemView) {
            super(itemView);
            doctor = itemView.findViewById(R.id.itemTitulo);
            descripcion = itemView.findViewById(R.id.itemSubtitulo);
            fecha = itemView.findViewById(R.id.itemFecha);
        }
    }
}

