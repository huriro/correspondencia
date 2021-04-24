package com.example.controledecorrepondencias;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class TelaPrincincipal extends AppCompatActivity {
    private LinearLayout linearLayout1;
    private Button botaoativas,botaonovas,botaocosul,boataoreser,botaoleituras,botaoteste;
    private  FirebaseAuth usuario = FirebaseAuth.getInstance();
    private ProgressBar progressBarprincial;
    private TextView nomeCondominio,nomeusuario,funcaousuario;
    private DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth autenticacao;
    private String usuar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_princincipal);
        setTitle("Principal");
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        linearLayout1=findViewById(R.id.LinearLayout1);
        nomeCondominio=findViewById(R.id.textViewNomeCondominio);
        nomeusuario=findViewById(R.id.textnomeUsuario);
        funcaousuario=findViewById(R.id.textViewfuncao);
        botaoativas=findViewById(R.id.botaotelalistaAtiva);
        botaonovas=findViewById(R.id.buttonovascorrrespondencias);
        botaocosul=findViewById(R.id.button4consultas);
        boataoreser=findViewById(R.id.button2novareserva);
        botaoleituras=findViewById(R.id.buttonLeituras);
        botaoteste=findViewById(R.id.buttonreserva);
        progressBarprincial=findViewById(R.id.progressBarPricipal);
        String idusuario=getIntent().getStringExtra("idusuario1");
        final DatabaseReference usuario = reference.child("Usuarios");
        boataoreser.setEnabled(false);
        botaonovas.setEnabled(false);
        botaoativas.setEnabled(false);
        botaocosul.setEnabled(false);
        Query pesquisaAtiva = usuario.orderByChild("id").equalTo(idusuario);
        pesquisaAtiva.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                progressBarprincial.setVisibility(View.VISIBLE);
                DatasetUsuario datasetUsuario = dataSnapshot.getValue(DatasetUsuario.class);
                nomeusuario.setText("Nome: "+datasetUsuario.nome);
                usuar=datasetUsuario.nome;
                nomeCondominio.setText("Condomínio: "+datasetUsuario.condominios);
                funcaousuario.setText("Função: "+datasetUsuario.funcao);
                boataoreser.setEnabled(true);
                botaonovas.setEnabled(true);
                botaoativas.setEnabled(true);
                botaocosul.setEnabled(true);
                progressBarprincial.setVisibility(View.GONE);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        botaocosul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TelaPrincincipal.this,TelaConsultaCorrespondencias.class);
                startActivity(intent);
                return;
            }
        });
        botaoativas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TelaPrincincipal.this,CorrespondeciasAtivas.class);
                intent.putExtra("usua",usuar);
                startActivity(intent);
                return;
            }
        });
        botaonovas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TelaPrincincipal.this,CadastroCorrespondencias.class);
                intent.putExtra("usua",usuar);
                startActivity(intent);
                return;
            }
        });
        boataoreser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(TelaPrincincipal.this,Reservas_Geral.class);
                intent.putExtra("usua",usuar);

                startActivity(intent);
                return;
            }
        });
        botaoleituras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TelaPrincincipal.this,TelaLeiturasCadastro.class);
                intent.putExtra("usua",usuar);
                startActivity(intent);
                return;
            }
        });
        botaoteste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TelaPrincincipal.this,TelaLeituraAptos.class);
                intent.putExtra("usua",usuar);
                startActivity(intent);
                return;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sair:
                deslogarUsuario();
                startActivity(new Intent(getApplicationContext(),Telalogin.class));
                finish();
                break;
            case R.id.menu_inicial:
                //deslogarUsuario();
                startActivity(new Intent(getApplicationContext(),inicial.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void deslogarUsuario(){
        try {usuario.signOut();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
