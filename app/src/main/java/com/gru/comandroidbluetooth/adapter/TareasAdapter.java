package com.gru.comandroidbluetooth.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.gru.comandroidbluetooth.view.fragment.TareasFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class TareasAdapter extends RecyclerView.Adapter<TareasAdapter.TareaHolder>
{
    private ArrayList<TaskModel> lista;
    private AppCompatActivity activity;
    private ICallBackListener callBackListener;

    public TareasAdapter(ArrayList<TaskModel> lista, Activity activity) {
        this.lista = lista;
        this.activity = (AppCompatActivity) activity;

        if(activity instanceof ICallBackListener){
            callBackListener = (ICallBackListener)activity;
        }
    }

    @NonNull
    @Override
    public TareaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_list_tareas,parent,false);
        return new TareaHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TareaHolder holder, final int position) {
        final TaskModel model = lista.get(position);

        holder.txtCreacion.setText("Realizar el " + model.getFecha_tarea() + " hs.");
        holder.txtTituloTarea.setText(model.getTitulo());
        holder.txtDetalle.setText(model.getDetalle());

        String estado = model.getEstado();

        if(estado.equals("PENDIENTE")){
            holder.txtEstado.setText("PENDIENTE");
            holder.txtEstado.setBackgroundResource(R.drawable.fondo_txt_pendiente);
        }
        else if(estado.equals("FINALIZADA"))
        {
            holder.txtEstado.setText("FINALIZADA");
            holder.txtEstado.setBackgroundResource(R.drawable.fondo_txt_finalizado);
        }
        if(model.getId_rol()==2)
        {
            Picasso.with(activity).load(R.drawable.doctor).into(holder.img);
        }else {
            Picasso.with(activity).load(R.drawable.enfermera).into(holder.img);
        }

        holder.card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(model.getEstado().equals("FINALIZADA"))
                {
                    Toast.makeText(activity, "Ya esta finalizada esa tarea", Toast.LENGTH_SHORT).show();
                }else {

                    mostrarDialogCambiarEstado(model,position);
                }
                return true;
            }
        });
    }

    private void mostrarDialogCambiarEstado(final TaskModel model, final int pos)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();

        final View view = inflater.inflate(R.layout.dialog_finalizar_tarea,null);

        builder.setView(view);

        TextView txtCreacion,txtTituloTarea,txtCreadoPor,txtDetalle, txtEstado;
        txtCreacion    = view.findViewById(R.id.txtCuandoSeCreo);
        txtTituloTarea = view.findViewById(R.id.txtTarea);
        txtDetalle     = view.findViewById(R.id.txtDetalle);
        txtEstado      = view.findViewById(R.id.txtEstadoTarea);

        txtCreacion.setText("Realizar el " + model.getFecha_tarea() + " hs.");
        txtTituloTarea.setText(model.getTitulo());
        txtDetalle.setText(model.getDetalle());

        String estado = model.getEstado();

        if(estado.equals("PENDIENTE")){
            txtEstado.setText("PENDIENTE");
            txtEstado.setBackgroundResource(R.drawable.fondo_txt_pendiente);
        }
        else if(estado.equals("FINALIZADA"))
        {
            txtEstado.setText("FINALIZADA");
            txtEstado.setBackgroundResource(R.drawable.fondo_txt_finalizado);
        }

        Button realizado = view.findViewById(R.id.btnRealizado);
        Button cancelado = view.findViewById(R.id.btnCancelar);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.setCancelable(false);

        cancelado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

            }
        });

        realizado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*BuildTaskCronogram cronogram = BuildTaskCronogram.getInstancia();
                cronogram.cambiarEstado(pos);
                alertDialog.dismiss();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.framePanelDerecha,new TareasFragment()).commit();*/
                actualizarTarea(model,alertDialog);
            }
        });
    }

    private void actualizarTarea(final TaskModel model,final AlertDialog dialogo) // si voy a ver el perfil, lo cargo
    {
        final String url_i = Constants.URL_BASE + Constants.URL_ACTUALIZAR_TAREA;
        Calendar calendar = Calendar.getInstance();
        final String fecha_act = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-"
                + calendar.get(Calendar.DATE) + " " + calendar.get(Calendar.HOUR_OF_DAY)+ ":" + calendar.get(Calendar.MINUTE);
        Log.e("url= ", url_i);
        final ProgressDialog dialog=new ProgressDialog(activity);
        dialog.setMessage("Guardando datos...");
        dialog.show();
        VolleySingleton.getInstancia(activity).
                addToRequestQueue(new StringRequest(Request.Method.POST, url_i,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.e("Response", "ok");
                                dialog.hide();
                                dialogo.dismiss();
                                //activity.getSupportFragmentManager().beginTransaction().replace(R.id.framePanelDerecha,new TareasFragment()).commit();
                                callBackListener.onCallBack(Constants.CLIENTE_ACEPTA);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("Response", "error " + error.toString());
                                Toast.makeText(activity, "No se pudo finalizar la tarea", Toast.LENGTH_SHORT).show();
                                dialog.hide();

                            }
                        }){

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> map = new HashMap<String,String>();
                        Log.e("hora",fecha_act);
                        map.put("id_usuario",String.valueOf(Comun.obtenerDatosUsuarioLogueado(activity).getId()));
                        map.put("fec",fecha_act);
                        map.put("id_t",String.valueOf(model.getId()));
                        return map;
                    }
                });
    }



    @Override
    public int getItemCount() {
        return lista.size();
    }

    class TareaHolder extends RecyclerView.ViewHolder
    {
        ImageView img;
        TextView txtCreacion,txtTituloTarea,txtDetalle, txtEstado;
        CardView card;
        public TareaHolder(View item) {
            super(item);
            img            = item.findViewById(R.id.imgTarea);
            txtCreacion    = item.findViewById(R.id.txtCuandoSeCreo);
            txtTituloTarea = item.findViewById(R.id.txtTarea);
            txtDetalle     = item.findViewById(R.id.txtDetalle);
            txtEstado      = item.findViewById(R.id.txtEstadoTarea);
            card           = item.findViewById(R.id.card);
        }
    }
}
