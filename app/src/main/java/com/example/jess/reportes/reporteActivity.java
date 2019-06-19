package com.example.jess.reportes;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.jess.reportes.clases.Preferencias;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import java.util.SimpleTimeZone;

//public class reporteActivity extends AppCompatActivity implements LocationListener, Response.Listener<JSONObject>,Response.ErrorListener {
//implementacion para metodo POST
public class reporteActivity extends AppCompatActivity implements LocationListener{

    //-----------------------Variables foto
    ImageView imagenFoto;
    Button btnCapturar;
    Button btnEnviar;

    private String datoUsuario; //varaible que obtendra el valor del cache del usuario

    //------------------------------------------

    //-------------Varaible para tomar las fotografias
    static final String CARPETA_PRINCIPAL = "imgReportes/";
    static final String CARPETA_IMAGEN = "imagenes";
    static final String DIRECTORIO_IMAGEN = CARPETA_PRINCIPAL + CARPETA_IMAGEN;
    private String path;//almacena la ruta de la imagen
    public File fileImagen;
    public Bitmap imageBitmap;
    private static final int COD_FOTO = 20;
    //-------------------------------------------------

    //---------------Variables coordenadas-----------
    private LocationManager nLocationManager;//variable para localizacion
    private String TAG = "LocalizacionAPP"; //variable para texto de permisos
    private TextView tvLat, tvLon, tvFecha;
    public String enviaLatitud = "", enviaLongitud = "";
    public String imagenString="";
    //----------------------------------------------

    //--------------JSON para respuestas desde el webService----------
    RequestQueue Respuesta;
    StringRequest solicitudString;
    //-----------------
    //--------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte);

        datoUsuario = Preferencias.obtenerPreferenciaString(this,Preferencias.PREFERENCIA_USUARIO_LOGIN);//se obtiene el valor del cache del usuario

        //------------------------Asignacion de varaibles a objetos
        btnCapturar = (Button) findViewById(R.id.fotoCapturar);
        imagenFoto = (ImageView) findViewById(R.id.imageViewFoto);
        btnEnviar = (Button) findViewById(R.id.botonEnviarReporte);
        //----------------------------------------------------------


        //PRUEBA NUEVA PARA FOTO
        btnCapturar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCamara();
            }
        });
        //--FIN PRUEBA NUEVA


        //----------Accion para el boton ENVIAR---
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamarEnviarReporte(datoUsuario);
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
            Log.d(TAG, "Faltan Persmisos");
            return;
        }
        nLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 01, 01, this);
        nLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0l, 01, this);
        nLocationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 01, 01, this);
        //-------------------------------------------------------------------


        //tvFecha.setText(ObtenerFecha());
    }

    // METODO PARA OBTENER FECHA Y HROA
    private String ObtenerFecha(){
        long fechaLong = System.currentTimeMillis();
        SimpleDateFormat formatoTiempo = new SimpleDateFormat("MM-dd-yyyy_hh-mm-ss_a");
        String fechaString = formatoTiempo.format(fechaLong);
        //tvFecha.setText(fechaString);
        return fechaString;
    }

    //----------------metodo del boton ENVIAR---------------
    private void llamarEnviarReporte(final String miUsuario) {
        ///---++++++++++++++++++++++++++++++++++++para metodo POST +++++++++++++++++++++++++++++++++++++++++++++++
        String url="http://reportes.infinit.com.mx/webServiceReportes.php";//servidor remoto
        //String url="http://192.168.1.69/reportesPrueba/webServiceReportes.php";//servidor de prueba casa
        final String fecha = ObtenerFecha();
        mostrarMensajeEmergente("enviando");


        solicitudString = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equalsIgnoreCase("registro")){
                    mostrarMensajeEmergente("esconder");
                    //Toast.makeText(getApplicationContext(),"Se enviaron los datos con exito",Toast.LENGTH_SHORT).show();
                    mostrarMensajeEmergente("realizar");
                }
                else
                {
                    mostrarMensajeEmergente("esconder");
                    Toast.makeText(getApplicationContext(),"NO SE ENVIARON LOS DATOS: "+response.toString(),Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"No se pudo conectar",Toast.LENGTH_SHORT).show();
                mostrarMensajeEmergente("esconder");
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //--convirtiendo imagen a string
                imagenString = convertirImgString(imageBitmap);
                //----------

                //asignando los datos a los parametros
                String latitudString = enviaLatitud;
                String longitudString = enviaLongitud;
                String nombreFoto = "LAT_"+latitudString+"__LON_"+longitudString+"__DATETIME_"+fecha;

                //--alimentando los parametros que se enviaran por metodo POST
                Map<String,String> parametros = new HashMap<>();
                parametros.put("datoUsuario",miUsuario);
                parametros.put("latitud",latitudString);
                parametros.put("longitud",longitudString);
                parametros.put("nombre",nombreFoto);
                parametros.put("foto",imagenString);

                return parametros;
            }
        };
        Respuesta = Volley.newRequestQueue(getApplicationContext());
        Respuesta.add(solicitudString);
        //------------++++++++++++++++++--------fin metodo POST---------+++++++++++++++++--------------*/
    }//------------fin llamarEnviarReporte-----

    //------------------METODO MENSAJES EMERGENTES---------------
    private void mostrarMensajeEmergente(String recibido) {
        ProgressDialog progreso = new ProgressDialog(this);
        Toast mensajeEnviando = null;

        switch (recibido){
            case "enviando":
                mensajeEnviando = Toast.makeText(getApplicationContext(),"Enviando reporte....",Toast.LENGTH_LONG);
                mensajeEnviando.show();

                break;
            case "esconder":
                //mensajeEnviando.cancel();
                break;
            case "realizar":
                AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
                dialogo.setMessage("Se ha enviado tu reporte con Exito. ¿Quieres realizar otro reporte?");
                dialogo.setTitle("Listo!");
                dialogo.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialogo.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                });

                AlertDialog aler = dialogo.create();
                aler.show();

                break;
        }
    }
//----------------------fin metodo mensajes emergentes

    // nuevo metodo para la camara
    private void abrirCamara() {
        File miFile = new File(Environment.getExternalStorageDirectory(), DIRECTORIO_IMAGEN);
        boolean isCreada = miFile.exists();

        if (isCreada == false) {
            isCreada = miFile.mkdirs();
        }

        if(isCreada == true){
            Long consecutivo = System.currentTimeMillis() / 1000;
            String nombre = consecutivo.toString() + ".jpg";

            path = Environment.getExternalStorageDirectory() + File.separator + DIRECTORIO_IMAGEN + File.separator + nombre;//indicamos la ruta de almacenamiento

            fileImagen = new File(path);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileImagen));

            startActivityForResult(intent, COD_FOTO);

        }
    }
    // fin del nuevo metodo

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){
            case 20:
                MediaScannerConnection.scanFile(getApplicationContext(), new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("Path",""+path);
                    }
                });

                imageBitmap = BitmapFactory.decodeFile(path);
                imagenFoto.setImageBitmap(imageBitmap);



                break;
        }

        imageBitmap = redimensionarImagen(imageBitmap,300,170);

    }

    //-----------METODO PARA REDIMENCIONAR IMAGEN ---------------
    private Bitmap redimensionarImagen(Bitmap imageBitmap, float anchoNuevo, float altoNuevo) {
        int ancho = imageBitmap.getWidth();
        int alto = imageBitmap.getHeight();

        if(ancho>altoNuevo || alto>altoNuevo){
            float escalaAncho = anchoNuevo/ancho;
            float escalaAlto = altoNuevo/alto;

            Matrix matrix = new Matrix();
            matrix.postScale(escalaAncho,escalaAlto);

            return Bitmap.createBitmap(imageBitmap,0,0,ancho,alto,matrix,false);

        }else{
            return imageBitmap;
        }
    }
    //----------------------------fin metodo redimensionar----------------

    //--------------------- Metodo para convertir imagen bitmap-----------
    private String convertirImgString(Bitmap imageBitmap) {
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG,100,array);
        byte[] imagenByte = array.toByteArray();
        String imagenConvertidaString = Base64.encodeToString(imagenByte,Base64.DEFAULT);

        return imagenConvertidaString;
    }
    //----------------------------------------------------------------
    //-----------------------------------------------------------------



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
    //---------------------------------------------------------------
}
