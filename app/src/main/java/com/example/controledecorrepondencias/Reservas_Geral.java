package com.example.controledecorrepondencias;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Reservas_Geral extends AppCompatActivity {
    private Button buttonNovaReserva,buttonConsulta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservas__geral);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
        getSupportActionBar().setTitle("Tela reservas");
        buttonConsulta=findViewById(R.id.buttonConsultasReservas);
        buttonNovaReserva=findViewById(R.id.buttonNovaReserva);
        final String usua = getIntent().getStringExtra("usua");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
        }
        buttonNovaReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Reservas_Geral.this,TelaResevaCadastro.class);
                intent.putExtra("usua",usua);

                startActivity(intent);
                return;
            }
        });
        buttonConsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Reservas_Geral.this,TelaConsultaReserva.class);
                intent.putExtra("usua",usua);

                startActivity(intent);
                return;
            }
        });
    }
}
