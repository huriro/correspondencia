package com.example.controledecorrepondencias;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.io.Console;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class TelaLeiturasCadastro extends AppCompatActivity {
  private EditText editTextleituraagua,editTextleituraenergia;
  private TextView textViewleitagua,textViewleitenergis,textViewconsuagua,textViewconsuenergia
          ,textViewdataagua,textViewdataenergia,textVconsumoaguaatual,textVconsumoenergiatual
          ,txtpesquisaagua,txtpesquienergia;

  private Button buttonAgua,buttonEnergia;
  private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();
  int aguaA, aguaU,total =0, energiaA,energiaU,total2=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_leituras_cadastro);
        getSupportActionBar().setTitle("Leituras agua e energia");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final String usua = getIntent().getStringExtra("usua");
        textVconsumoaguaatual=findViewById(R.id.textViewconsumoatualagua);
        textVconsumoenergiatual=findViewById(R.id.textViewconsumoatualenergia);
        textViewdataagua=findViewById(R.id.textVdataagua);
        textViewdataenergia=findViewById(R.id.textVdataenergia);
        textViewleitagua=findViewById(R.id.textVleituraagua);
        textViewleitenergis=findViewById(R.id.textVleituraenergia);
        textViewconsuagua=findViewById(R.id.textVconsumoagua);
        textViewconsuenergia=findViewById(R.id.textVconsumoenergia);
        txtpesquisaagua=findViewById(R.id.txtpesquisaAgua);
        txtpesquienergia=findViewById(R.id.txtpesquisaenergia);
        editTextleituraagua=findViewById(R.id.editTextleituraAgua);
        editTextleituraenergia=findViewById(R.id.editTextleituraEnergia);
        buttonAgua=findViewById(R.id.buttonleituraAgua);
        buttonEnergia=findViewById(R.id.buttonleituraenergia);
        buttonAgua.setEnabled(false);
        buttonEnergia.setEnabled(false);
        final DatabaseReference Ultimaleitura = referencia.child("ultimaleitura");
        final DatabaseReference novasleituras = referencia.child("leituras");
        final DatabaseReference novasleiturasEnergia=referencia.child("leituraEnergia");
       filtroleitura();
       filtroleituraenergia();
       txtpesquisaagua.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent=new Intent(TelaLeiturasCadastro.this,TelaConsultaLeitura.class);
               startActivity(intent);
           }
       });
       txtpesquienergia.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent=new Intent(TelaLeiturasCadastro.this,TelaconsLeituEnergia.class);
               startActivity(intent);
           }
       });
       editTextleituraenergia.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

           }

           @Override
           public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               try {   if (!editTextleituraenergia.getText().toString().trim().equals("")){
                   buttonEnergia.setEnabled(true);
                   //String valor ="";
                   // valor = editTextleituraagua.getText().toString();
                   energiaA =Integer.parseInt(editTextleituraenergia.getText().toString());
                   total2 = energiaA-energiaU;
                   textVconsumoenergiatual.setText(Integer.toString(total2));
                   if(total2> -1){buttonEnergia.setEnabled(true);}else{buttonEnergia.setEnabled(false);}

               }else {
                   buttonEnergia.setEnabled(false);
                   textVconsumoenergiatual.setText("Consumo atual");
                   //Toast.makeText(TelaLeiturasCadastro.this,"aqui",Toast.LENGTH_LONG).show();
               }

               }catch (Exception e){}

           }

           @Override
           public void afterTextChanged(Editable editable) {

           }
       });
        editTextleituraagua.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


               try {   if (!editTextleituraagua.getText().toString().trim().equals("")){
                   buttonAgua.setEnabled(true);
                   //String valor ="";
                   // valor = editTextleituraagua.getText().toString();
                   aguaA =Integer.parseInt(editTextleituraagua.getText().toString());
                   total = aguaA-aguaU;
                   textVconsumoaguaatual.setText(Integer.toString(total));
                   if(total> -1){buttonAgua.setEnabled(true);}else{buttonAgua.setEnabled(false);}

               }else {
                   buttonAgua.setEnabled(false);
                   textVconsumoaguaatual.setText("Consumo atual");
                   //Toast.makeText(TelaLeiturasCadastro.this,"aqui",Toast.LENGTH_LONG).show();
               }

               }catch (Exception e){}


            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });
        buttonAgua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              if(editTextleituraagua.getText().toString().trim().equals("")){
                  Toast.makeText(TelaLeiturasCadastro.this,"aqui",Toast.LENGTH_LONG).show();

              }else {

                      Calendar calendar = Calendar.getInstance();

                      System.out.println("Current time => " + calendar.getTime());

                      SimpleDateFormat df = new SimpleDateFormat("MM/yyyy");
                      SimpleDateFormat dh = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                      String formattedDate = df.format(calendar.getTime());
                      String formattedDate2 = dh.format(calendar.getTime());
                      ControleleituraDia c = new ControleleituraDia();
                      c.setDataAgua(formattedDate2);
                      c.setLeituraAgua(editTextleituraagua.getText().toString());
                      c.setConsumoAgua(textVconsumoaguaatual.getText().toString());
                      c.setUsuario(usua);
                      c.setDatames(formattedDate);

                      novasleituras.push().setValue(c);
                      ControleleituraDia d = new ControleleituraDia();
                      d.setStatus("ativo");
                      d.setLeituraAgua(editTextleituraagua.getText().toString());
                  d.setConsumoAgua(textVconsumoaguaatual.getText().toString());
                  d.setDataAgua(formattedDate2);
                  d.setUsuario(usua);
                  Ultimaleitura.child("leituras").setValue(d);
                  Toast.makeText(TelaLeiturasCadastro.this,"Salvo com sucesso!",Toast.LENGTH_LONG).show();
                  filtroleitura();
                      editTextleituraagua.setText("");
                      // textViewconsuagua.setText(textVconsumoaguaatual.getText());




                  }
            }
        });
        buttonEnergia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(editTextleituraenergia.getText().equals("")){} else {
                    Toast.makeText(TelaLeiturasCadastro.this,"aqui",Toast.LENGTH_LONG).show();
                }

                Calendar calendar = Calendar.getInstance();

                System.out.println("Current time => " + calendar.getTime());

                SimpleDateFormat df = new SimpleDateFormat("MM/yyyy");
                SimpleDateFormat dh = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                String formattedDate = df.format(calendar.getTime());
                String formattedDate2 = dh.format(calendar.getTime());
                ControleleituraDia c = new ControleleituraDia();
                c.setDataenergia(formattedDate2);
                c.setLeituraEnergia(editTextleituraenergia.getText().toString());
                c.setConsumoEnergia(textVconsumoenergiatual.getText().toString());
                c.setUsuario(usua);
                c.setDatames(formattedDate);

                novasleiturasEnergia.push().setValue(c);
                ControleleituraDia d = new ControleleituraDia();
                d.setStatus("ativo2");
                d.setLeituraEnergia(editTextleituraenergia.getText().toString());
                d.setConsumoEnergia(textVconsumoenergiatual.getText().toString());
                d.setDataenergia(formattedDate2);
                d.setUsuario(usua);
                Ultimaleitura.child("leiturasEnergia").setValue(d);
                Toast.makeText(TelaLeiturasCadastro.this,"Salvo com sucesso!",Toast.LENGTH_LONG).show();
                filtroleitura();
                filtroleituraenergia();
                editTextleituraenergia.setText("");
            }
        });




    }

    public void filtroleitura(){
        final DatabaseReference Ultimaleitura = referencia.child("ultimaleitura");
        Query ultimaleitura = Ultimaleitura.orderByChild("status").equalTo("ativo");

        ultimaleitura.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                ControleleituraDia l = dataSnapshot.getValue(ControleleituraDia.class);
                textViewdataagua.setText(l.dataAgua);
                //textViewdataenergia.setText(l.dataenergia);
                textViewleitagua.setText("leitura  "+l.leituraAgua);
                // textViewleitenergis.setText("leitura  "+l.leituraEnergia);
                textViewconsuagua.setText("consumo  "+l.consumoAgua);
                //textViewconsuenergia.setText("consumo  "+l.consumoEnergia);
                aguaU = Integer.parseInt(l.leituraAgua);






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


    }
    public void filtroleituraenergia(){
        final DatabaseReference Ultimaleitura = referencia.child("ultimaleitura");
        Query ultimaleitura = Ultimaleitura.orderByChild("status").equalTo("ativo2");
        ultimaleitura.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ControleleituraDia l = dataSnapshot.getValue(ControleleituraDia.class);
               // textViewdataagua.setText(l.dataAgua);
                textViewdataenergia.setText(l.dataenergia);
               // textViewleitagua.setText("leitura  "+l.leituraAgua);
                textViewleitenergis.setText("leitura  "+l.leituraEnergia);
               // textViewconsuagua.setText("consumo  "+l.consumoAgua);
                textViewconsuenergia.setText("consumo  "+l.consumoEnergia);
                energiaU = Integer.parseInt(l.leituraEnergia);
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
    }

}
