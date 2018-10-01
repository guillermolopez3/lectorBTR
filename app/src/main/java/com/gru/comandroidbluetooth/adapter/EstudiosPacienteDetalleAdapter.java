package com.gru.comandroidbluetooth.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gru.comandroidbluetooth.R;
import com.gru.comandroidbluetooth.model.EstudiosPacienteModel;
import com.gru.comandroidbluetooth.view.ImagenEstudioActivity;

import java.util.ArrayList;

public class EstudiosPacienteDetalleAdapter extends RecyclerView.Adapter<EstudiosPacienteDetalleAdapter.HolderEstudio>
{
    ArrayList<EstudiosPacienteModel> lista;
    Activity activity;
    FragmentManager manager;

    public EstudiosPacienteDetalleAdapter(ArrayList<EstudiosPacienteModel> lista,Activity activity, FragmentManager manager)
    {
        this.lista=lista;
        this.activity=activity;
        this.manager=manager;
    }

    @NonNull
    @Override
    public HolderEstudio onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_list_estudio_paciente_detalle,parent,false);
        return new EstudiosPacienteDetalleAdapter.HolderEstudio(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderEstudio holder, int position) {

        EstudiosPacienteModel model = lista.get(position);

        holder.nombre_estudio.setText(model.getTipo_estudio());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.startActivity(new Intent(activity, ImagenEstudioActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    class HolderEstudio extends RecyclerView.ViewHolder
    {
        ImageView imagen;
        TextView nombre_estudio;
        CardView cardView;

        public HolderEstudio(View itemView) {
            super(itemView);
            imagen = itemView.findViewById(R.id.imgTarea);
            nombre_estudio = itemView.findViewById(R.id.txtNombreEstudio);
            cardView = itemView.findViewById(R.id.cardEstudio);
        }
    }
}
