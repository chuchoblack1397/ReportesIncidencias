package com.example.jess.reportes;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.example.jess.reportes.clases.Preferencias;

public class MainActivity extends AppCompatActivity {

    private String datoUsuario;
    private TextView tvDatoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        datoUsuario = Preferencias.obtenerPreferenciaString(this,Preferencias.PREFERENCIA_USUARIO_LOGIN);

        tvDatoUsuario = (TextView) findViewById(R.id.tvUsuario);
        tvDatoUsuario.setText(datoUsuario);

        clickParaBoton1();
        clickParaBoton2();

        clickParaCerrarSession();
    }

    private void clickParaBoton1() {
        CardView btn_Reportar = (CardView) findViewById(R.id.btnReportar);
        btn_Reportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ventana_reportar = new Intent(getApplicationContext(), menuReportes.class);
                startActivity(ventana_reportar);
            }
        });
    }

    private void clickParaBoton2() {
        CardView btn_Reportar = (CardView) findViewById(R.id.btnMisReportes);
        btn_Reportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ventana_misreportes = new Intent(getApplicationContext(), consultaActivity.class);
                startActivity(ventana_misreportes);
            }
        });
    }

    private void clickParaCerrarSession(){
        TextView btn_Cerrar_Session = (TextView) findViewById(R.id.btnCerrarSesion);
        btn_Cerrar_Session.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Preferencias.guardarPreferenciaBoolean(MainActivity.this, false, Preferencias.PREFERENCIA_ESTADO_BOTON_CHECKBOX);
                Intent iniciarLogin = new Intent(MainActivity.this, login.class);
                startActivity(iniciarLogin);
                finish();
            }
        });
    }
}
