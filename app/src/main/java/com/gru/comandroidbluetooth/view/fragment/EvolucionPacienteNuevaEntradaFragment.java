package com.gru.comandroidbluetooth.view.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gru.comandroidbluetooth.R;
import com.gru.comandroidbluetooth.backend.VolleySingleton;
import com.gru.comandroidbluetooth.helper.Constants;
import com.gru.comandroidbluetooth.helper.ICallBackListener;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class EvolucionPacienteNuevaEntradaFragment extends Fragment
{
    private ICallBackListener callBackListener;
    private TextInputEditText texto;
    private String id_internacion;

    public EvolucionPacienteNuevaEntradaFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_evolucion_paciente_nueva_entrada,container,false);
        texto = view.findViewById(R.id.txtNuevaEntrada);
        Bundle bundle = getArguments();
        if(bundle != null)
        {
            id_internacion = bundle.getString("id");
        }
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
        Button aceptar = view.findViewById(R.id.btnIngresar);
        Button cancelar = view.findViewById(R.id.btnCancelar);

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(texto.getText()))
                {
                    Snackbar.make(view,"debe Ingresar una descripción",Snackbar.LENGTH_SHORT).show();
                }
                else {
                    if(callBackListener != null){
                        insertarHistorial();
                    }
                }

            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(callBackListener != null){
                    callBackListener.onCallBack(Constants.CLIENTE_CANCELA);
                }
            }
        });
    }

    private void insertarHistorial() // si voy a ver el perfil, lo cargo
    {
        final String url = Constants.URL_BASE + Constants.URL_INSERT_HISTORIA;
        Log.e("url= ", url);
        final ProgressDialog dialog=new ProgressDialog(getActivity());
        dialog.setMessage("Guardando datos...");
        dialog.show();
        Log.e("id inter",id_internacion + " " + texto.getText().toString());
        VolleySingleton.getInstancia(getActivity()).
                addToRequestQueue(new StringRequest(Request.Method.POST, url,
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
                                Log.e("Response", "error" + error.getMessage());
                                callBackListener.onCallBack(Constants.CLIENTE_CANCELA);
                            }
                        }){

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> map = new HashMap<String,String>();
                        map.put("detalle",texto.getText().toString());
                        map.put("id",id_internacion);
                        return map;
                    }
                });
    }
}
