package com.example.jess.reportes;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.jess.reportes.adaptadores.adaptadorReportes;
import com.example.jess.reportes.clases.Preferencias;
import com.example.jess.reportes.entidades.Reportes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class consultaActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener{

    RecyclerView recyclerReportes;
    ArrayList<Reportes> listaReportes;

    RequestQueue solicitud;
    JsonObjectRequest jsonObjectRequest;

    private String datoUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);

        recyclerReportes = (RecyclerView) findViewById(R.id.recyclerId);
        recyclerReportes.setLayoutManager(new LinearLayoutManager(this));
        recyclerReportes.setHasFixedSize(true);

        solicitud = Volley.newRequestQueue(getApplicationContext());

        datoUsuario = Preferencias.obtenerPreferenciaString(this,Preferencias.PREFERENCIA_USUARIO_LOGIN);
        cargarWebService(datoUsuario);

    }

    private void cargarWebService(final String miUsuario){
        //String url="http://reportesv2.infinit.com.mx/consultaReporte.php?datoUsuario="+miUsuario;
        String url="http://incidencias.geoportal.mx/consultaReporte.php?datoUsuario="+miUsuario;

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        solicitud.add(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplicationContext(),"No se puede conectar: "+error.toString(),Toast.LENGTH_SHORT).show();
        System.out.println();
        Log.d("ERROR: ",error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {
        Reportes reporte = null;
        listaReportes = new ArrayList<>();

        JSONArray json = response.optJSONArray("reportes");

        try {
            for (int i = 0; i < json.length(); i++) {
                reporte = new Reportes();
                JSONObject jsonObject = null;
                jsonObject = json.getJSONObject(i);

                reporte.setIdReporte(jsonObject.optInt("idReporte"));
                reporte.setLatitud(jsonObject.optString("latitud"));
                reporte.setLongitud(jsonObject.optString("longitud"));
                reporte.setTiempo(jsonObject.optString("tiempo"));
                reporte.setTipoReporte(jsonObject.optString("tipoReporte"));
                reporte.setNumPoste(jsonObject.optString("numPostes"));
                reporte.setPropPoste(jsonObject.optString("propietarioPostes"));
                reporte.setUsoPoste(jsonObject.optString("usoPostes"));
                reporte.setEstadoPoste(jsonObject.optString("estadoPostes"));
                reporte.setRuta(jsonObject.optString("urlFoto"));

                listaReportes.add(reporte);
            }//fin for

            adaptadorReportes adapter = new adaptadorReportes(listaReportes, getApplicationContext());
            recyclerReportes.setAdapter(adapter);


        }//fin try
        catch (JSONException ex){
            ex.printStackTrace();
            Toast.makeText(getApplicationContext(),"No se ha podido establecer la conexion con el servidor: "+ex.toString(),Toast.LENGTH_SHORT).show();
            System.out.println();
            Log.d("ERROR: ",ex.toString());
        }//fin catch

    }//fin response
}
