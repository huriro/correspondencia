package com.example.controledecorrepondencias;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;

public class TelaConsulracorrespndencias2 extends AppCompatActivity {
    private ImageView imagemassint;
    private ProgressBar progressBarconsultafinal;
    private TextView txtapto,textViewdestinatario,textViewtipo,textViewdiscriminacao,textViewdataentrega
            ,textViewdataregistro,textViewdestinatariorecebeu,textViewusuarioregistrou,textViewusuarioentregou
            ,textViewStatus;
    private DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
    private Button whast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_consulracorrespndencias2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
        getSupportActionBar().setTitle("Consulta final");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        String codigo = getIntent().getStringExtra("codigo");
        imagemassint=findViewById(R.id.imageViewassinatura2);
        imagemassint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passarImagem();
            }
        });
        txtapto=findViewById(R.id.TextViewApto2);
        textViewdestinatario=findViewById(R.id.TextViewdestinatario2);
        textViewtipo=findViewById(R.id.TextViewTipo2);
        progressBarconsultafinal=findViewById(R.id.progressBarconsulFinal);
        whast=findViewById(R.id.buttonwhast);
        whast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Vamos carregar a imagem em um bitmap
                imagemassint.setDrawingCacheEnabled(true);
                imagemassint.buildDrawingCache();
                Bitmap b =imagemassint.getDrawingCache();

                Intent share = new Intent(Intent.ACTION_SEND);
                //setamos o tipo da imagem
                share.setType("image/jpeg");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                // comprimomos a imagem
                b.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                // Gravamos a imagem
                String path = MediaStore.Images.Media.insertImage(getContentResolver(), b, "Titulo da Imagem", null);
                // criamos uam Uri com o endereço que a imagem foi salva
                Uri imageUri =  Uri.parse(path);
                // Setmaos a Uri da imagem
                share.putExtra(Intent.EXTRA_STREAM, imageUri);
                // chama o compartilhamento
                startActivity(Intent.createChooser(share, "Selecione"));
            }
        });
        textViewdiscriminacao=findViewById(R.id.textViewdescriminacao2);
        textViewdataentrega =findViewById(R.id.textViewDataEntrega);
        textViewdataregistro=findViewById(R.id.textView8dataRegistro);
        textViewdestinatariorecebeu=findViewById(R.id.textView11destinatariorecebeu);
        textViewusuarioregistrou=findViewById(R.id.textView12UsuarioRegistrou);
        textViewusuarioentregou=findViewById(R.id.textView13Usuarioentregou);
        textViewStatus=findViewById(R.id.textView20Status);

        DatabaseReference correspondencia = reference.child("correspondencias");
        Query pesquisaAtiva = correspondencia.orderByChild("codigo").equalTo(codigo);
        pesquisaAtiva.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                DatasetFire a = dataSnapshot.getValue(DatasetFire.class);
                txtapto.setText("Apto: "+a.apto);
                textViewtipo.setText("Tipo: "+a.tipo);
                textViewdestinatario.setText("Destinatário: "+a.destinatario);
                //id=(dataSnapshot.getKey());
                textViewdiscriminacao.setText("Discriminação: "+a.discriminacao);
                textViewdataentrega.setText("Data Entrega: "+a.dataentrega);
                textViewdataregistro.setText("Data Registro: "+a.dataregistro);
                textViewdestinatariorecebeu.setText("Recebido por: "+a.quemrecebeu);
                textViewusuarioentregou.setText("Usuario Baixou: "+a.usuarioBaixou);
                textViewusuarioregistrou.setText("Usuario Registrou: "+a.usuarioregistro);
                textViewStatus.setText("Status: "+a.status);
                try {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                StorageReference imagens =storageReference.child("imagemassinatura");
                StorageReference imagearefe=imagens.child(a.imageAsiinatura);



                    Glide.with(TelaConsulracorrespndencias2.this)
                            .using(new FirebaseImageLoader())
                            .load(imagearefe)
                            .into(imagemassint);
                }catch (Exception e){
                    Toast.makeText(TelaConsulracorrespndencias2.this,"não foi possivel carregar a imagem, " +
                            "pode ser que ela ainda não exista"
                    ,Toast.LENGTH_LONG).show();
                }
                progressBarconsultafinal.setVisibility(View.GONE);

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

    }private void passarImagem() {
        // = findViewById(R.id.imageView);

        imagemassint.setDrawingCacheEnabled(true);
        imagemassint.buildDrawingCache();
        Bitmap bitmap =imagemassint.getDrawingCache();
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,75,bao);
        byte[] dadosassinatura = bao.toByteArray();
        Intent intent = new Intent(TelaConsulracorrespndencias2.this,visualizarimagem.class);
        intent.putExtra("imagem",dadosassinatura);
        startActivity(intent);

    }
}
