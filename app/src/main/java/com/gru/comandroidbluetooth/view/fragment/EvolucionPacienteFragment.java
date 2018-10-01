package com.gru.comandroidbluetooth.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gru.comandroidbluetooth.R;
import com.gru.comandroidbluetooth.adapter.EvolucionPacienteAdapter;
import com.gru.comandroidbluetooth.model.InternacionDetalleModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class EvolucionPacienteFragment extends Fragment
{
    ArrayList<InternacionDetalleModel> list;
    RecyclerView recycler;

    public EvolucionPacienteFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_panel_evolucion_paciente, container, false);

       recycler = view.findViewById(R.id.recycler);

       list = new ArrayList<>();
       if(getArguments()!=null)
       {
           list =(ArrayList<InternacionDetalleModel>) getArguments().get("lista");
           llenarArray();
       }

       return view;
    }

    private void llenarArray()
    {
        if(list.size()>0 && list!=null)
        {
            if(list.get(0).getDetalle() != null && !list.get(0).getDetalle().equals("null"))
            {
                Log.e("entre y detalle ",list.get(0).getDetalle());
                recycler.setAdapter(new EvolucionPacienteAdapter(getActivity(), list));
                recycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
            }
        }
    }



}
