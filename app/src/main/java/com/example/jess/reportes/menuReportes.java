package com.example.jess.reportes;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.view.View;

public class menuReportes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_reportes);

        clickParaBoton1();
        clickParaBoton2();
        clickParaBoton3();


    }

    private void clickParaBoton1() {
        CardView btnReportarPoste = (CardView) findViewById(R.id.btnReportarPoste);
        btnReportarPoste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ventana_reportarPoste = new Intent(getApplicationContext(), reportePoste.class);
                startActivity(ventana_reportarPoste);
            }
        });
    }

    private void clickParaBoton2() {
        CardView btnReportarMedidor = (CardView) findViewById(R.id.btnReportarMedidor);
        btnReportarMedidor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ventana_reportarMedidor = new Intent(getApplicationContext(), consultaActivity.class);
                startActivity(ventana_reportarMedidor);
            }
        });
    }

    private void clickParaBoton3() {
        CardView btnReportarFrente = (CardView) findViewById(R.id.btnReportarFrente);
        btnReportarFrente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ventana_reportarMFrente = new Intent(getApplicationContext(), consultaActivity.class);
                startActivity(ventana_reportarMFrente);
            }
        });
    }
}
