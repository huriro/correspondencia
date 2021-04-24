package com.example.controledecorrepondencias;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TelaLeituraAptos extends AppCompatActivity {
    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();
    private TextView txtApto,txtconsumo,txtleitura,txtdata,txtconsumoatual,txtverhistorico;
    private Button btnsalvar;
    private EditText edtnovaleitura;
    DatabaseReference refleituraapto = referencia.child("ultimaleituraaptos");
    DatabaseReference novasleituras = referencia.child("leituras-aptos");
    String formattedDate ;
    String formattedDate2 ;
    Query consultastatus = refleituraapto;
    String verdata;
    int ultmleitura, atualeitura, consumoatual =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Leituras agua dos aptos");

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_tela_leitura_aptos);
        txtApto=findViewById(R.id.textVAptoultimaleitura);
        txtconsumo=findViewById(R.id.textVcosumoaptoultima);
        txtdata=findViewById(R.id.textVdataultimaleitura);
        txtleitura=findViewById(R.id.textleituraultima);
        txtconsumoatual=findViewById(R.id.textVConsaptoAtual);
        edtnovaleitura=findViewById(R.id.edtNovaleituraApto);
        btnsalvar=findViewById(R.id.btnsalvarleituraatualapto);
        txtverhistorico=findViewById(R.id.txtverhistorico);
        btnsalvar.setEnabled(true);
        Calendar calendar = Calendar.getInstance();

        System.out.println("Current time => " + calendar.getTime());

        SimpleDateFormat df = new SimpleDateFormat("MM/yyyy");
        SimpleDateFormat dh = new SimpleDateFormat("dd/MM/yyyy");
        formattedDate = df.format(calendar.getTime());
        formattedDate2 = dh.format(calendar.getTime());

        iddata();
     edtnovaleitura.addTextChangedListener(new TextWatcher() {
         @Override
         public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

         }

         @Override
         public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
             if (!edtnovaleitura.getText().toString().trim().equals("")){
                 atualeitura =Integer.parseInt(edtnovaleitura.getText().toString());
                 consumoatual =  atualeitura-ultmleitura;
                 txtconsumoatual.setText(Integer.toString(consumoatual));
                 btnsalvar.setEnabled(true);
                 if(consumoatual> -1){btnsalvar.setEnabled(true);}else{btnsalvar.setEnabled(false);}

             }
             else{
                 btnsalvar.setEnabled(false);
                 txtconsumoatual.setText("Consumo atual");
             }




         }

         @Override
         public void afterTextChanged(Editable editable) {

         }
     });
     txtverhistorico.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             Intent intent = new Intent(TelaLeituraAptos.this,Telacosultaleituraaptos.class);
            // intent.putExtra("usua",usuar);

             startActivity(intent);
             return;
         }
     });
     btnsalvar.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

             Controleleituraaptos c = new Controleleituraaptos();
             c.setApto(txtApto.getText().toString());
             c.setData(formattedDate2);
             c.setConsumo(txtconsumoatual.getText().toString());
             c.setLeitura(edtnovaleitura.getText().toString());
             c.setLeiturista("");
             c.setControl("ver");
             novasleituras.push().setValue(c);
             refleituraapto.child(txtApto.getText().toString()).setValue(c);
             Toast.makeText(TelaLeituraAptos.this,"Salvo com sucesso!",Toast.LENGTH_LONG).show();
             iddata();
             edtnovaleitura.setText("");
             btnsalvar.setEnabled(false);
             txtconsumoatual.setText("Consumo atual");

         }
     });

    }
    public void iddata(){

            // DatabaseReference refleituraapto = referencia.child("ultimaleituraaptos");
            //Query consultastatus = refleituraapto;
            consultastatus.orderByChild("control").equalTo("ver").limitToFirst(1).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    Controleleituraaptos c = dataSnapshot.getValue(Controleleituraaptos.class);
                    verdata = c.data;

                    //DatabaseReference refleituraapto = referencia.child("ultimaleituraaptos");

                    // Query consultastatus = refleituraapto;
                    consultastatus.orderByChild("data").equalTo(verdata).limitToLast(1).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            Controleleituraaptos c = dataSnapshot.getValue(Controleleituraaptos.class);
                            txtApto.setText(dataSnapshot.getKey());
                            txtdata.setText(c.getData());
                            txtconsumo.setText("Consumo anterior: " + c.getConsumo());
                            txtleitura.setText("Leitura anterior: " + c.getLeitura());
                            ultmleitura = Integer.parseInt(c.getLeitura());
                            txtconsumoatual.setText(formattedDate2);
                            if(txtdata.getText().toString().equals(txtconsumoatual.getText().toString())) {
                                edtnovaleitura.setEnabled(false);
                                btnsalvar.setEnabled(false);
                                Toast.makeText(TelaLeituraAptos.this,"parabêns hoje você ja realizou a leitura",Toast.LENGTH_LONG).show();
                                txtconsumoatual.setText("parabêns hoje você já realizou a leitura. \n" +
                                        "Espere o proximo dia para uma nova leitura\n" +
                                        "Obrigado por entender. ");
                                txtconsumoatual.setTextColor(Color.BLUE);
                            }else {
                                edtnovaleitura.setEnabled(true);
                                txtconsumoatual.setText("Consumo atual");
                                txtconsumoatual.setTextColor(Color.BLACK);

                            }
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
