package com.gru.comandroidbluetooth.view.fragment;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gru.comandroidbluetooth.R;
import com.gru.comandroidbluetooth.backend.VolleySingleton;
import com.gru.comandroidbluetooth.helper.BuildTaskCronogram;
import com.gru.comandroidbluetooth.helper.Comun;
import com.gru.comandroidbluetooth.helper.Constants;
import com.gru.comandroidbluetooth.helper.ICallBackListener;
import com.gru.comandroidbluetooth.model.TareaModel;
import com.gru.comandroidbluetooth.model.TaskModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class NuevaTareaFragment extends Fragment
{
    private static final String TAG = "NuevaTareaFragment";
    TextView txtFecha,txtHora,txtRepetir,txtFechaFin;
    Switch sw_repe;
    LinearLayout linearRepetir;
    Spinner spTipo,spHoraMin;
    RadioButton rbFin,rbDespuesDe;
    Button btnCerrar, btnGuardar;
    BuildTaskCronogram cronogram;
    EditText txtTitulo, txtDetalle, txtCantTomas;

    private int id_internacion;

    private ICallBackListener callBackListener;

    public NuevaTareaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nueva_tarea, container, false);

        cronogram = BuildTaskCronogram.getInstancia();

        if(getArguments()!=null)
        {
            id_internacion = getArguments().getInt("id_inter");
        }

        bindearView(view);

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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getActivity() instanceof ICallBackListener){
            callBackListener = (ICallBackListener)getActivity();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framePanelDerecha,new TareasFragment()).commit();
                if(callBackListener != null){
                    callBackListener.onCallBack(Constants.CLIENTE_CANCELA);
                }
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(callBackListener != null){
                    guardartarea();
                }
            }
        });

    }

    private void bindearView(View view)
    {
        txtFecha      = view.findViewById(R.id.txtFecha);
        txtHora       = view.findViewById(R.id.txtHora);
        sw_repe       = view.findViewById(R.id.switchRepetir);
        txtRepetir    = view.findViewById(R.id.txtRepetir);
        linearRepetir = view.findViewById(R.id.linearRepetir);
        spTipo        = view.findViewById(R.id.spTipo);
        spHoraMin     = view.findViewById(R.id.spHsMin);
        rbFin         = view.findViewById(R.id.rbFinaliza);
        rbDespuesDe   = view.findViewById(R.id.rbCantTomas);
        txtFechaFin   = view.findViewById(R.id.txtFechaFin);
        btnCerrar     = view.findViewById(R.id.btnCancelar);
        btnGuardar    = view.findViewById(R.id.btnGuardar);
        txtTitulo     = view.findViewById(R.id.txtTituloTarea);
        txtDetalle    = view.findViewById(R.id.txtDetalleTarea);
        txtCantTomas  = view.findViewById(R.id.etCantidadTomas);

        Calendar ca = Calendar.getInstance();
        txtFecha.setText(ca.get(Calendar.DATE) + "/" + (ca.get(Calendar.MONTH) + 1) + "/" + ca.get(Calendar.YEAR));
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
               //fecha_bd = year + "-" + (mes + 1) + "-" + dia;
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

    private void guardartarea()
    {
        if(txtTitulo.getText().toString().isEmpty() || txtDetalle.getText().toString().isEmpty())
        {
            Toast.makeText(getActivity(), "Los campos de título y destalle no pueden estar vacios", Toast.LENGTH_LONG).show();
            return;
        }

        TaskModel model = new TaskModel();
        Calendar calendar = Calendar.getInstance();
        //model.setFecha_tarea(calendar.getTime().toString()); //fecha en que creo la tarea
        model.setTitulo(txtTitulo.getText().toString());
        model.setDetalle(txtDetalle.getText().toString());
        model.setFecha_tarea(formatearFecha() + " " +txtHora.getText().toString()); //fecha del primer aviso
        //model.setHora_aviso(txtHora.getText().toString()); //hora del primer aviso
        model.setEstado("PENDIENTE");

        if(sw_repe.isChecked())
        {
            //guardarTareaConRepeticion(model);
        }
        else {
            //guardarTareaSinRepeticion(model);
            insertarTareaEnBD(model);
        }

    }

    private String formatearFecha()
    {
        String fe="";
        String fecha = txtFecha.getText().toString();
        SimpleDateFormat format = new SimpleDateFormat("dd/mm/yyyy");

        try {
                Date date = format.parse(fecha);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
                fe = formatter.format(date);
        }catch (ParseException e){Log.e("formar error", e.toString());}
        Log.e("fecha",String.valueOf(fe));
        return  fe;
    }

    private void guardarTareaConRepeticion(TareaModel model)
    {
        cronogram.addListItem(model); //agrego a la lista la tarea y en las sig repe tengo que hacer n-1

        if(rbDespuesDe.isChecked())
        {
            int hs_o_min; //dependiendo si el spinner seleccionado es hs o min
            if(spTipo.getSelectedItem().toString().equals("horas"))
            {
                hs_o_min = Calendar.HOUR_OF_DAY;
            }else {
                hs_o_min = Calendar.MINUTE;
            }
            cronogram.cartgarTareasConRepeticionDespuesDe(model,hs_o_min,
                    spHoraMin.getSelectedItem().toString(),txtCantTomas.getText().toString());
        }
        else if(rbFin.isChecked())
        {
            int hs_o_min; //dependiendo si el spinner seleccionado es hs o min
            if(spTipo.getSelectedItem().toString().equals("horas"))
            {
                hs_o_min = Calendar.HOUR_OF_DAY;
                cronogram.cargarTareaConRepeticionFinaliza(model,hs_o_min,spHoraMin.getSelectedItem().toString()
                ,txtFechaFin.getText().toString());
            }else {
                hs_o_min = Calendar.MINUTE;
            }
        }

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framePanelDerecha,new TareasFragment()).commit();
    }

    private void guardarTareaSinRepeticion(TareaModel model)
    {
       // cronogram.addListItem(model);
        //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framePanelDerecha,new TareasFragment()).commit();
        //insertarTareaEnBD();
    }

    private void insertarTareaEnBD(final TaskModel model) // si voy a ver el perfil, lo cargo
    {
        final String url_i = Constants.URL_BASE + Constants.URL_INSERAR_TAREA;
        Log.e("url= ", url_i);
        final ProgressDialog dialog=new ProgressDialog(getActivity());
        dialog.setMessage("Guardando datos...");
        dialog.show();
        //Log.e("id inter",id_internacion + " " + texto.getText().toString());
        VolleySingleton.getInstancia(getActivity()).
                addToRequestQueue(new StringRequest(Request.Method.POST, url_i,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.e("Response", "ok");
                                dialog.hide();
                                callBackListener.onCallBack(Constants.CLIENTE_ACEPTA);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("Response", "error " + error.toString());
                                String toS = model.getTitulo() + "-" + model.getDetalle() + "-" + model.getFecha_tarea()
                                        + "-" + id_internacion + "-" + String.valueOf(Comun.obtenerDatosUsuarioLogueado(getActivity()).getId());
                                Log.e("Response", toS);
                                Toast.makeText(getActivity(), "Error al guardar la tarea", Toast.LENGTH_SHORT).show();
                                dialog.hide();

                            }
                        }){

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> map = new HashMap<String,String>();
                        map.put("titulo",model.getTitulo());
                        map.put("detalle",model.getDetalle());
                        map.put("id_usuario",String.valueOf(Comun.obtenerDatosUsuarioLogueado(getActivity()).getId()));
                        map.put("fec",model.getFecha_tarea());
                        map.put("id_inter",String.valueOf(id_internacion));
                        return map;
                    }
                });
    }
}
