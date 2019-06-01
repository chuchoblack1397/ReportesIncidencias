package com.example.jess.reportes.adaptadores;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jess.reportes.R;
import com.example.jess.reportes.entidades.Reportes;

import org.w3c.dom.Text;

import java.util.List;

public class adaptadorReportes extends RecyclerView.Adapter<adaptadorReportes.adaptadorHolder> {

    List<Reportes> listaReporte;

    public adaptadorReportes(List<Reportes> listaReporte) {
        this.listaReporte = listaReporte;
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


    }

    @Override
    public int getItemCount() {
        return listaReporte.size();
    }

    public class adaptadorHolder extends RecyclerView.ViewHolder{

        TextView txtIdReporte, txtLatitud, txtLongitud, txtTiempo;

        public adaptadorHolder(View itemView) {
            super(itemView);
            txtIdReporte = (TextView) itemView.findViewById(R.id.txtIdReporte);
            txtLatitud = (TextView) itemView.findViewById(R.id.txtLatitud);
            txtLongitud = (TextView) itemView.findViewById(R.id.txtLongitud);
            txtTiempo = (TextView) itemView.findViewById(R.id.txtTiempo);
        }
    }

}
