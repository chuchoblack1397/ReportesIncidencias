package com.example.jess.reportes.adaptadores;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.jess.reportes.R;
import com.example.jess.reportes.entidades.Reportes;

import java.util.List;

public class adaptadorReportes extends RecyclerView.Adapter<adaptadorReportes.adaptadorHolder> {

    List<Reportes> listaReporte;

    RequestQueue request;
    Context context;

    public adaptadorReportes(List<Reportes> listaReporte, Context context) {
        this.listaReporte = listaReporte;
        this.context=context;
        request = Volley.newRequestQueue(context);
    }

    @Override
    public adaptadorHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_reportes,parent,false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        return new adaptadorHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull adaptadorHolder holder, int posicion) {
        holder.txtIdReporte.setText(listaReporte.get(posicion).getIdReporte().toString());
        holder.txtLatitud.setText(listaReporte.get(posicion).getLatitud().toString());
        holder.txtLongitud.setText(listaReporte.get(posicion).getLongitud().toString());
        holder.txtTiempo.setText(listaReporte.get(posicion).getTiempo().toString());
        holder.txtTipoReporte.setText(listaReporte.get(posicion).getTipoReporte().toString());
        holder.txtNumPoste.setText(listaReporte.get(posicion).getNumPoste().toString());
        holder.txtPropietarioPoste.setText(listaReporte.get(posicion).getPropPoste().toString());
        holder.txtUsoPoste.setText(listaReporte.get(posicion).getUsoPoste().toString());
        holder.txtEstadoPoste.setText(listaReporte.get(posicion).getEstadoPoste().toString());

        if(listaReporte.get(posicion).getRuta() !=null){

            cargarImagenWebService(listaReporte.get(posicion).getRuta(), holder);

        }else{
            holder.imageView.setImageResource(R.drawable.ic_monochrome_photos_black_24dp);
        }


    }

    private void cargarImagenWebService(String ruta, final adaptadorHolder holder) {
        String urlImagen = ruta;
        urlImagen = urlImagen.replace(" ","%20");//en caso de que el enlace tenga espacios

        ImageRequest imageRequest = new ImageRequest(urlImagen, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                holder.imageView.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"No se puede cargar la imagen: "+error.toString(),Toast.LENGTH_SHORT).show();
                System.out.println();
                Log.d("ERROR: ",error.toString());
            }
        });
        request.add(imageRequest);
    }

    @Override
    public int getItemCount() {
        return listaReporte.size();
    }

    public class adaptadorHolder extends RecyclerView.ViewHolder{

        TextView txtIdReporte, txtLatitud, txtLongitud, txtTiempo, txtTipoReporte, txtNumPoste, txtPropietarioPoste, txtUsoPoste, txtEstadoPoste;
        ImageView imageView;

        public adaptadorHolder(View itemView) {
            super(itemView);
            txtIdReporte = (TextView) itemView.findViewById(R.id.txtIdReporte);
            txtLatitud = (TextView) itemView.findViewById(R.id.txtLatitud);
            txtLongitud = (TextView) itemView.findViewById(R.id.txtLongitud);
            txtTiempo = (TextView) itemView.findViewById(R.id.txtTiempo);

            txtTipoReporte = (TextView) itemView.findViewById(R.id.txtTipo);
            txtNumPoste = (TextView) itemView.findViewById(R.id.txtNumero);
            txtPropietarioPoste = (TextView) itemView.findViewById(R.id.txtPropietario);
            txtUsoPoste = (TextView) itemView.findViewById(R.id.txtUso);
            txtEstadoPoste = (TextView) itemView.findViewById(R.id.txtEstado);

            imageView = (ImageView) itemView.findViewById(R.id.imgReporte);
        }
    }

}
