package com.gru.comandroidbluetooth.view.fragment;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.gru.comandroidbluetooth.R;
import com.gru.comandroidbluetooth.view.LeerPulseraActivity;
import com.gru.comandroidbluetooth.view.MainPacienteActivity;
import com.gru.comandroidbluetooth.view.RegistrarPulseraActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskFragment extends Fragment
{
    TextView txtFecha,txtHora,txtRepetir,txtFechaFin;
    Switch sw_repe;
    LinearLayout linearRepetir;
    Spinner spTipo,spHoraMin;
    RadioButton rbFin,rbDespuesDe;

    public TaskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);

        txtFecha = view.findViewById(R.id.txtFecha);
        txtHora  = view.findViewById(R.id.txtHora);
        sw_repe  = view.findViewById(R.id.switchRepetir);
        txtRepetir = view.findViewById(R.id.txtRepetir);
        linearRepetir = view.findViewById(R.id.linearRepetir);
        spTipo = view.findViewById(R.id.spTipo);
        spHoraMin = view.findViewById(R.id.spHsMin);
        rbFin = view.findViewById(R.id.rbFinaliza);
        rbDespuesDe = view.findViewById(R.id.rbCantTomas);
        txtFechaFin = view.findViewById(R.id.txtFechaFin);

        llevarSpinerHorasMinutos(24);
        controlRadioBtn();
        controlFechaFin();

        linearRepetir.setVisibility(View.GONE);

        txtFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(txtFecha);
            }
        });

        txtHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePkr();
            }
        });

        sw_repe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    //Toast.makeText(getActivity(), "chequed", Toast.LENGTH_SHORT).show();
                    txtRepetir.setVisibility(View.GONE);
                    linearRepetir.setVisibility(View.VISIBLE);

                }else {
                    //Toast.makeText(getActivity(), "no chequed", Toast.LENGTH_SHORT).show();
                    linearRepetir.setVisibility(View.GONE);
                    txtRepetir.setVisibility(View.VISIBLE);
                }
            }
        });

        spTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0)
                {
                    llevarSpinerHorasMinutos(24);
                }else if(i==1)
                {
                    llevarSpinerHorasMinutos(60);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }

    private void controlRadioBtn()
    {
        rbFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rbDespuesDe.isChecked()){
                    rbDespuesDe.setChecked(false);
                    rbFin.setChecked(true);
                }
            }
        });
        rbDespuesDe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rbFin.isChecked()){
                    rbDespuesDe.setChecked(true);
                    rbFin.setChecked(false);
                }
            }
        });
    }

    private void controlFechaFin()
    {
       txtFechaFin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(rbFin.isChecked())
                    {
                        showDatePickerDialog(txtFechaFin);
                    }
                }
            });
    }

    private void llevarSpinerHorasMinutos(int limite)
    {
        List<String> list = new ArrayList<>();

        for(int i=0; i<limite;i++)
        {
            list.add(String.valueOf(i+1));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spHoraMin.setAdapter(adapter);
    }


    private void showDatePickerDialog(final TextView textView)
    {
        DatePickerFragment dialog = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int mes, int dia) {
                final String selecday = dia + "/" + (mes + 1) + "/" + year;
               String fec_nac = year + "/" + (mes + 1) + "/" + dia;
                textView.setText(selecday);
            }
        });
        dialog.show(getChildFragmentManager(),"datePicker");
    }

    private void showTimePkr()
    {
        TimePckrDialog time = TimePckrDialog.newInstancia(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hs, int min) {
                final String m_time = String.valueOf(hs) + ":" + String.valueOf(min);
                txtHora.setText(m_time);
            }
        });
        time.show(getChildFragmentManager(),"timePicker");
    }

}
