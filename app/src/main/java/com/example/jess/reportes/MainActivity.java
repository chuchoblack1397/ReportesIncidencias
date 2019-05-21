package com.example.jess.reportes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clickParaBoton1();
    }

    private void clickParaBoton1() {
        CardView btn_Reportar = (CardView) findViewById(R.id.btnReportar);
        btn_Reportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ventana_reportar = new Intent(getApplicationContext(), reporteActivity.class);
                startActivity(ventana_reportar);
            }
        });
    }
}
