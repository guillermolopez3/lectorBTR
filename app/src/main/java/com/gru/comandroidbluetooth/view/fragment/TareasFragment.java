package com.gru.comandroidbluetooth.view.fragment;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.gru.comandroidbluetooth.R;
import com.gru.comandroidbluetooth.adapter.EvolucionPacienteAdapter;
import com.gru.comandroidbluetooth.adapter.TareasAdapter;
import com.gru.comandroidbluetooth.helper.BuildTaskCronogram;
import com.gru.comandroidbluetooth.model.InternacionDetalleModel;
import com.gru.comandroidbluetooth.model.TareaModel;
import com.gru.comandroidbluetooth.model.TaskModel;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class TareasFragment extends Fragment
{
    private RecyclerView recyclerView;
    private BuildTaskCronogram cronogram;
    private ProgressBar progressBar;
    private FloatingActionButton fab;

    private ArrayList<TaskModel> lista;

    private int id_internacion;

    public TareasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tareas, container, false);

        cronogram = BuildTaskCronogram.getInstancia();

        recyclerView = view.findViewById(R.id.recycler);
        progressBar  = view.findViewById(R.id.progressTarea);
        fab          = view.findViewById(R.id.fab);

        lista = new ArrayList<>();

        if(getArguments() !=null)
        {
            lista =(ArrayList<TaskModel>) getArguments().get("lista");
            id_internacion = getArguments().getInt("id_inter");
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));

        llenarProgressBar();
        llenarRecycler();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment n_frag = new NuevaTareaFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("id_inter",id_internacion);
                n_frag.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framePanelDerecha,n_frag).commit();
            }
        });


        return view;
    }

   /* private void llenarProgressBar()
    {
        int cant_pendiente = cronogram.cantTareasPenidente();
        int total = cronogram.getLista().size();
        progressBar.setMax(total);
        progressBar.setProgress(cant_pendiente);
    }*/

   private void llenarProgressBar()
   {
       int cant_pendiente=0;
       int total = lista.size();
       progressBar.setMax(total);
       for (TaskModel t : lista)
       {
            if(t.getEstado().equals("PENDIENTE")){
                cant_pendiente = cant_pendiente +1;
            }
       }
       progressBar.setProgress(cant_pendiente);
   }

   private void llenarRecycler()
   {
       if(lista.size()>0 && lista!=null)
       {
           if(lista.get(0).getTitulo() != null && !lista.get(0).getTitulo().equals("null"))
           {
               TareasAdapter adapter = new TareasAdapter(lista,getActivity());
               recyclerView.setAdapter(adapter);
               recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
           }
       }
   }



}
