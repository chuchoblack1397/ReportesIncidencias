package com.example.jess.reportes.clases;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.jess.reportes.login;

public class Preferencias {
    public static final String STRING_PREFERENCIAS = "mis.preferencias.de.login";
    public static final String PREFERENCIA_ESTADO_BOTON_CHECKBOX = "estado.boton.session";
    public static final String PREFERENCIA_USUARIO_LOGIN = "usuario.login";


    //-- metodo guardarPreferenciaBoolean
    public static void guardarPreferenciaBoolean(Context contexto, Boolean variableBooleana, String llave){
        SharedPreferences preferencias =contexto.getSharedPreferences(STRING_PREFERENCIAS,contexto.MODE_PRIVATE);
        preferencias.edit().putBoolean(llave,variableBooleana).apply();
    }// fin metoddo guardarPreferenciaBoolean


    //-- metodo guardarPreferenciaString
    public static void guardarPreferenciaString(Context contexto, String variableString, String llave){
        SharedPreferences preferencias =contexto.getSharedPreferences(STRING_PREFERENCIAS,contexto.MODE_PRIVATE);
        preferencias.edit().putString(llave,variableString).apply();
    }// fin metoddo guardarPreferenciaString


    //-- metodo para obtener el estado del boton en cache
    public static boolean obtenerPreferenciaBoolean(Context contexto, String llave){
        SharedPreferences preferencias = contexto.getSharedPreferences(STRING_PREFERENCIAS,contexto.MODE_PRIVATE);
        return preferencias.getBoolean(llave,false);//si es que nunca se ha guardado regresara false
    }// fin metoddo cambiar estado del booleano

    //-- metodo para obtener el estado del boton en cache
    public static String obtenerPreferenciaString(Context contexto, String llave){
        SharedPreferences preferencias = contexto.getSharedPreferences(STRING_PREFERENCIAS,contexto.MODE_PRIVATE);
        return preferencias.getString(llave,"");//si es que nunca se ha guardado regresara cadena vacia
    }// fin metoddo cambiar estado del String
}
