package com.gru.comandroidbluetooth.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gru.comandroidbluetooth.R;
import com.gru.comandroidbluetooth.model.EstudiosPacienteModel;
import com.gru.comandroidbluetooth.view.fragment.EstudiosPacienteDetalleFragment;

import java.util.ArrayList;

public class EstudiosPacienteAdapter extends RecyclerView.Adapter<EstudiosPacienteAdapter.HolderEstudio>
{
    ArrayList<EstudiosPacienteModel> lista;
    Activity activity;
    FragmentManager manager;

    public EstudiosPacienteAdapter(ArrayList<EstudiosPacienteModel> lista,Activity activity, FragmentManager manager)
    {
        this.lista=lista;
        this.activity=activity;
        this.manager=manager;
    }
    @NonNull
    @Override
    public HolderEstudio onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_list_estudio_paciente,parent,false);
        return new HolderEstudio(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderEstudio holder, int position) {
        final EstudiosPacienteModel model = lista.get(position);

        holder.tipo_estudio.setText(model.getTipo_estudio());
        holder.nro_estudio.setText(model.getNro_estudi());
        holder.derivante.setText(model.getDerivante());
        holder.estado.setText(model.getEstado());

        try{

            if(model.getEstado().equals("FINALIZADO"))
            {
                holder.estado.setTextColor(activity.getResources().getColor(R.color.boton_inicio));
            }
        }catch (Exception e){}

        try{
            if(model.getIco().equals("eco"))
            {
                holder.imagen.setImageDrawable(activity.getResources().getDrawable(R.drawable.eco_ico));
            }
            else if(model.getIco().equals("rx"))
            {
                holder.imagen.setImageDrawable(activity.getResources().getDrawable(R.drawable.rx_ico));
            }
            else if(model.getIco().equals("cito"))
            {
                holder.imagen.setImageDrawable(activity.getResources().getDrawable(R.drawable.lab_ico));
            }
            else if(model.getIco().equals("ecg"))
            {
                holder.imagen.setImageDrawable(activity.getResources().getDrawable(R.drawable.ecg_ico));
            }
        }catch (Exception ex){}

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(model.getEstado().equals("FINALIZADO"))
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("tipoEstudi",model.getTipo_estudio());
                    bundle.putString("nroEstudio",model.getNro_estudi());
                    Fragment fragment = new EstudiosPacienteDetalleFragment();
                    fragment.setArguments(bundle);
                    manager.beginTransaction().replace(R.id.framePanelDerecha,fragment).commit();
                }else {
                    Toast.makeText(activity, "El estudio aún esta en proceso", Toast.LENGTH_SHORT).show();
                }
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
        TextView nro_estudio,tipo_estudio,derivante,estado;
        CardView cardView;

        public HolderEstudio(View itemView) {
            super(itemView);
            imagen = itemView.findViewById(R.id.imgTarea);
            nro_estudio = itemView.findViewById(R.id.txtCuandoSeCreo);
            tipo_estudio = itemView.findViewById(R.id.txtTarea);
            derivante = itemView.findViewById(R.id.txtCreadoPor);
            estado = itemView.findViewById(R.id.txtEstadoEstudio);
            cardView = itemView.findViewById(R.id.cardEstudio);
        }
    }
}
