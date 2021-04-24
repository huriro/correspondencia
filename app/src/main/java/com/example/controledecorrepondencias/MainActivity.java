package com.example.controledecorrepondencias;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements Runnable {
    private ProgressBar progressBarinicial;
    // private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth usuario = FirebaseAuth.getInstance();


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        getWindow().setStatusBarColor(Color.WHITE);

        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 0);
        }
        Handler handler = new Handler(); //contador de tempo
        handler.postDelayed(this, 2000); //o exemplo 2000 = 2 segundos
        setTitle("");


        // deslogar usuario
        /*usuario.signOut();
        if (usuario.getCurrentUser() != null){
            Log.i("login","usuario logado!");
        }else {Log.i("login","usuario não logado!");}


        //login ususario
        usuario.signInWithEmailAndPassword("humbertorodrigues2005@gmail.com","123456").addOnCompleteListener( new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    Log.i("logo usuario","logado com sucesso!");
                }else
                {Log.i("logo usuario","não logado!");

                }
            }
        });*/
       /*/criar usuario/
      final DatabaseReference usuarios = referencia.child("Usuarios");
       usuario.createUserWithEmailAndPassword("humbertorodrigues2005@ddddf.com","123456")
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                       if (task.isSuccessful())
                       {Log.i("novousuario","Cadastrado com sucesso!");
                           textView.setText(usuario.getUid());
                           //Log.i("novo usuario","Cadastrado com sucesso!");
                       }
                       else
                           {textView.setText("apto");

                               Log.i("novo usuario","algo deu errado!");
                               return;

                           }
                    }
                });
       if (textView.getText().toString().contains("apto"))
       {

       }else{
           DatasetUsuario novousuario = new DatasetUsuario();
        novousuario.setId(usuario.getUid());
        novousuario.setNome("Humberto");
        novousuario.setEmail("humbertorodrigues2005@hotmail.com");
        novousuario.setFuncao("Zelador");
        novousuario.setTelefone("");
        usuarios.push().setValue(novousuario);
       }*/

        //eviar dados
        //referencia.child("pontos").child("depontos").setValue("100");
        /*DatabaseReference correspondencia = referencia.child("correspondencias");
        correspondencia.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                DatasetFire a = dataSnapshot.getValue(DatasetFire.class);
                //textView.setText(a.apto);
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
        });*/
       /* correspondencia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i("FIREBASE",dataSnapshot.getValue().toString());
                DatasetFire a = dataSnapshot.getValue(DatasetFire.class);
                textView.setText("apto"+a);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
    }

    @Override
    public void run() {
        if (usuario.getCurrentUser() != null) {
            Log.i("AUTH", "onAuthStateChanged:signed_in:" + usuario.getUid());
            Intent intent = new Intent(MainActivity.this, TelaPrincincipal.class);
            intent.putExtra("idusuario1", usuario.getUid());

             startActivity(intent);
            // progressBarinicial.setVisibility(View.GONE);
             finish();


             }else
             {Intent intent = new Intent(MainActivity.this,Telalogin.class);
              startActivity(intent);
            // progressBarinicial.setVisibility(View.GONE);
             finish();
            }
        }
    }

