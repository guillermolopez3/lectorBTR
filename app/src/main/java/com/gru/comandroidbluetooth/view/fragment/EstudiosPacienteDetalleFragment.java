package com.gru.comandroidbluetooth.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gru.comandroidbluetooth.R;
import com.gru.comandroidbluetooth.adapter.EstudiosPacienteAdapter;
import com.gru.comandroidbluetooth.adapter.EstudiosPacienteDetalleAdapter;
import com.gru.comandroidbluetooth.model.EstudiosPacienteModel;

import java.util.ArrayList;

public class EstudiosPacienteDetalleFragment extends Fragment
{
    EstudiosPacienteDetalleAdapter adapter;

    public EstudiosPacienteDetalleFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_panel_estudios_paciente_detalle,container,false);

        String titulo = "";
        if(getArguments()!=null)
        {
            titulo = "Estudios Médicos > " + getArguments().getString("tipoEstudi","") + " > " + getArguments().getString("nroEstudio","");
            TextView txt = view.findViewById(R.id.txtTituloDetalleEstudio);
            txt.setText(titulo);
        }
        RecyclerView recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false));
        adapter = new EstudiosPacienteDetalleAdapter(dummyModelPac1(),getActivity(),getFragmentManager());
        recyclerView.setAdapter(adapter);

        return view;
    }

    private ArrayList<EstudiosPacienteModel> dummyModelPac1()
    {
        ArrayList<EstudiosPacienteModel> list = new ArrayList<>();
        EstudiosPacienteModel model;
        model= new EstudiosPacienteModel(1,"eco","Estudio N° 457624.8 - 16/08/2018","ECOGRAFÍA DE ABDOMEN","DERIVANTE: Sergio Balverrín","EN PROCESO");
        list.add(model);

        model= new EstudiosPacienteModel(2,"eco","Estudio N° 787547.0 - 16/08/2018","TAC DE PELVIS","DERIVANTE: Sergio Balverrín","EN PROCESO");
        list.add(model);

        /*model= new EstudiosPacienteModel(3,"rx","Estudio N° 677859.4 - 23/04/2018","RX CERVICAL FRENTE","DERIVANTE: Virginia Melica","FINALIZADO");
        list.add(model);

        model= new EstudiosPacienteModel(4,"rx","Estudio N° 528179.2 - 17/03/2018","RX CERVICAL PERFIL","DERIVANTE: Jorge Avalos","FINALIZADO");
        list.add(model);

        model= new EstudiosPacienteModel(5,"cito","Estudio N° 02415890 - 26/03/2018","CITOLÓGICO COMPLETO","DERIVANTE: Virginia Melica","FINALIZADO");
        list.add(model);

        model= new EstudiosPacienteModel(6,"ecg","Estudio N° 0241590 - 26/03/2018","ECG PRE-QUIRÚRGICO","DERIVANTE: Virginia Melica","FINALIZADO");
        list.add(model);*/

        return list;
    }
}
