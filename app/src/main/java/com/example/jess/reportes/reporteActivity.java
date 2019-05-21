package com.example.jess.reportes;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class reporteActivity extends AppCompatActivity implements LocationListener, Response.Listener<JSONObject>,Response.ErrorListener{

    //-----------------------Variables foto
    ImageView imagenFoto;
    Button btnCapturar;
    Button btnEnviar;
    //------------------------------------------

    //-------------Varaible para tomar las fotografias
    static final int REQUEST_IMAGE_CAPTURE = 1;
    //-------------------------------------------------

    //---------------Variables coordenadas-----------
    private LocationManager nLocationManager;//variable para localizacion
    private String TAG = "LocalizacionAPP"; //variable para texto de permisos
    private TextView tvLat, tvLon;
    public String  enviaLatitud="", enviaLongitud="";
    //----------------------------------------------

    //--------------JSON para respuestas desde el webService----------
    RequestQueue Respuesta;
    JsonObjectRequest SolicitaObjetoJson;
    //--------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte);

        //------------------------Asignacion de varaibles a objetos
        btnCapturar = (Button) findViewById(R.id.fotoCapturar);
        imagenFoto = (ImageView) findViewById(R.id.imageViewFoto);
        btnEnviar = (Button) findViewById(R.id.botonEnviarReporte);
        //----------------------------------------------------------

        //-------- Accion para el boton de capturar
        btnCapturar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamarIntent();
            }
        });
        //----------------------------------

        //----------Accion para el boton ENVIAR---
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamarEnviarReporte();
            }
        });
        //---------------------------------------

        //--------------------- asignando varible a objetos------
        tvLat = (TextView) findViewById(R.id.tvLatitud);
        tvLon = (TextView) findViewById(R.id.tvLongitud);
        nLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        //--------------------------------------------------------

        //-------------------- al momento de iniciar la aplicacion cargar las coordenadas
        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                && (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            Log.d(TAG,"Faltan Persmisos");
            return;
        }
        nLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 01, 01, this);
        nLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0l,01,this);
        nLocationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 01,01, this);
        //-------------------------------------------------------------------

    }


    //----------------metodo del boton ENVIAR---------------
    private void llamarEnviarReporte() {
        //envia los datos a la cadena de conexion directamente
        //String url="http://192.168.1.69/reportesPrueba/webServiceReportes.php?latitud="+enviaLatitud+"&longitud="+enviaLongitud;
        String url="http://reportes.infinit.com.mx/webServiceReportes.php?latitud="+enviaLatitud+"&longitud="+enviaLongitud;

        //se envia la informacion
        SolicitaObjetoJson = new JsonObjectRequest(Request.Method.GET,url,null,this,this);

            // --- para reparar el bug del "VolleyTimeOutError"---
            SolicitaObjetoJson.setRetryPolicy(new DefaultRetryPolicy(
               10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));//---- fin de reparacion ----

        Respuesta.add(SolicitaObjetoJson);
        //----------------------------
    }
    //-----------------------------------------------------------------

    //----------------------------Metodo para tomar fotografias
    private void llamarIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

    }
    //---------------------------------fin metodo----------------------------------


    //-------------- Metodo de respuesta para la foto - tomar un bitmap pequeño
    //---------------y mostrasrse dentro del imageView
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imagenFoto.setImageBitmap(imageBitmap);
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        Respuesta = Volley.newRequestQueue(getApplicationContext());

        //----------muestra las corrdenadas en los textview de la app
        tvLat.setText(location.getLatitude()+"");
        tvLon.setText(location.getLongitude()+"");
        //------------------------------------------

        //almacena las corrdenadas para enviarlas
        enviaLatitud = location.getLatitude()+"";
        enviaLongitud = location.getLongitude()+"";
        //------------------------------------------
           }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    //-----------------------fin metodo --------------------------------------

    //---------------- al cerrar la aplicacion ------------------------
    @Override
    protected void onDestroy() {
        nLocationManager.removeUpdates(this);
        super.onDestroy();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplicationContext(),"Sin respuesta: "+error.toString(),Toast.LENGTH_SHORT).show();
        Log.i("ERROR",error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {
        //Toast.makeText(getApplicationContext(),"Se han enviado los datos",Toast.LENGTH_SHORT).show();
        try {
            // Obtener atributo "estado"
            String estado = response.getString("estado");

            switch (estado) {
                case "1": // EXITO
                    // Obtener array "metas" Json
                    String mensaje1 = response.getString("mensaje");
                    Toast.makeText(getApplicationContext(), mensaje1, Toast.LENGTH_LONG).show();
                    break;

                case "2": // FALLIDO
                    String mensaje2 = response.getString("mensaje");
                    Toast.makeText(getApplicationContext(), mensaje2, Toast.LENGTH_LONG).show();
                    break;

                case "0": //Sin conexion
                    String mensaje3 = response.getString("mensaje");
                    Toast.makeText(getApplicationContext(), mensaje3, Toast.LENGTH_LONG).show();
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    //---------------------------------------------------------------
}
