package com.example.controledecorrepondencias;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class BaixarCorrespondencia extends AppCompatActivity {
    private TextView textViewapto,TextViewdestinatario,TextViewtipo,TextViewdiscriminacao,textViewid;
    private Button botaosalvar;
    private EditText editTextdestinatario;
    private DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
    private String id,Aptos,imagemcarta;
    String currentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;
    ImageView imageviewassinatura;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baixar_correspondencia);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
        getSupportActionBar().setTitle("finalizar entrega");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final String usua = getIntent().getStringExtra("usua");
        final String usuarioregis=getIntent().getStringExtra("usuarioregis");
        //nomeassinatura = UUID.randomUUID().toString();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {
         ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},0);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
        }
        editTextdestinatario=findViewById(R.id.editTextdestinatario);
        botaosalvar=findViewById(R.id.buttonsalvar);
        textViewapto = findViewById(R.id.TextViewApto);
        TextViewtipo=findViewById(R.id.TextViewTipo);
        TextViewdestinatario=findViewById(R.id.TextViewdestinatario);
        TextViewdiscriminacao=findViewById(R.id.textViewdescriminacao);
        textViewid=findViewById(R.id.texTViewid);

        imageviewassinatura=findViewById(R.id.imageViewassinatura);
        String apto = getIntent().getStringExtra("apto");
        String tipo = getIntent().getStringExtra("tipo");
        String codigo = getIntent().getStringExtra("codigo");
        final String dataregistro = getIntent().getStringExtra("dataregistro");
        //Log.i("valor", apto+"valor2"+tipo+"valor3"+codigo);
        final DatabaseReference correspondencia = reference.child("correspondencias");
        Query pesquisaAtiva = correspondencia.orderByChild("codigo").equalTo(codigo);
        pesquisaAtiva.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                DatasetFire a = dataSnapshot.getValue(DatasetFire.class);
                textViewapto.setText("Apto "+a.apto);
                TextViewtipo.setText(a.tipo);
                TextViewdestinatario.setText(a.destinatario);
                id=(dataSnapshot.getKey());
                imagemcarta=(a.imageAsiinatura.toString());
                TextViewdiscriminacao.setText(a.discriminacao);
                Aptos=a.apto;

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
        findViewById(R.id.buttonFoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //tirarFoto();
                dispatchTakePictureIntent();
            }
        });
        botaosalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();

                System.out.println("Current time => "+c.getTime());

                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = df.format(c.getTime());
                // formattedDate have current date/time
                //Toast.makeText(this, formattedDate, Toast.LENGTH_SHORT).show();
              DatasetFire baixacorresp = new DatasetFire();
              baixacorresp.setQuemrecebeu(editTextdestinatario.getText().toString());
              baixacorresp.setStatus("entregue");
              baixacorresp.setDataentrega(formattedDate);
              baixacorresp.setDataregistro(dataregistro);
              baixacorresp.setCodigo(id);
              baixacorresp.setApto(Aptos);
              baixacorresp.setDiscriminacao(TextViewdiscriminacao.getText().toString());
              baixacorresp.setTipo(TextViewtipo.getText().toString());
              baixacorresp.setDestinatario(TextViewdestinatario.getText().toString());
              baixacorresp.setUsuarioBaixou(usua);
              baixacorresp.setUsuarioregistro(usuarioregis);
              baixacorresp.setImageAsiinatura(imagemcarta+".jpeg");
              if (imageviewassinatura.getDrawable()!=null){}
              else {

                  Toast.makeText(BaixarCorrespondencia.this,"capture a foto da assinatura"
                  ,Toast.LENGTH_LONG).show();
                  return;
              }
              if (editTextdestinatario.getText().length()!= 0){
              correspondencia.child(id).setValue(baixacorresp);
               uploadDaImagem();

              Context context = getApplicationContext();
                CharSequence text = "Salvo Com sucesso!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
              finish();}else{Context context = getApplicationContext();
                  CharSequence text = "você precisa informar quem estar recebendo";
                  int duration = Toast.LENGTH_SHORT;

                  Toast toast = Toast.makeText(context, text, duration);
                  toast.show();
                  return;

              }
            }
        });


    }
    public void tirarFoto(){
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,123);
    }

    private File createImageFile() throws IOException {
        // Crie um nome de arquivo de imagem
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Verifique se há uma atividade de câmera para lidar com a intenção
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            //
            //Crie o arquivo para onde a foto deve ir
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue apenas se o arquivo foi criado com sucesso
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
    private void setPic() {
        // Obter as dimensões da visualização
        int targetW = imageviewassinatura.getWidth();
        int targetH = imageviewassinatura.getHeight();

        // Obter as dimensões do bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determinar quanto reduzir a imagem
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decodifique o arquivo de imagem em um Bitmap dimensionado para preencher a tela
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = 2;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        imageviewassinatura.setImageBitmap(bitmap);
        boolean p = new File(currentPhotoPath).delete();
        if(p){}else {
            Toast.makeText(BaixarCorrespondencia.this,"não deletou",Toast.LENGTH_LONG)
                    .show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_TAKE_PHOTO && resultCode==RESULT_OK){
            //galleryAddPic();
            setPic();
            galleryAddPic();
           /* Bundle extras = data.getExtras();
            Bitmap imagem= (Bitmap)extras.get("data");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imagem.compress(Bitmap.CompressFormat.JPEG,100,baos);
            byte[] dadosImagem=baos.toByteArray();
            Bitmap imagem2= BitmapFactory.decodeByteArray(dadosImagem,0,dadosImagem.length);
            imageviewassinatura.setImageBitmap(imagem2);*/
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void uploadDaImagem(){
        imageviewassinatura.setDrawingCacheEnabled(true);
        imageviewassinatura.buildDrawingCache();
        Bitmap bitmap =imageviewassinatura.getDrawingCache();
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,75,bao);
        byte[] dadosassinatura = bao.toByteArray();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference imagens =storageReference.child("imagemassinatura");
        StorageReference imagearefe=imagens.child(imagemcarta+".jpeg");
        UploadTask uploadTask=imagearefe.putBytes(dadosassinatura);
        uploadTask.addOnFailureListener(BaixarCorrespondencia.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
             Toast.makeText(BaixarCorrespondencia.this,"falha a salvar a imagem"+e.getMessage().toString(),Toast.LENGTH_LONG).show();
            }
        });
    }


}
