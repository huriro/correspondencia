package com.example.controledecorrepondencias;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.utilities.PushIdGenerator;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CadastroCorrespondencias extends AppCompatActivity  {
     private Spinner spinnertipo,spinnerapto;
     private EditText destinatario,discriminacao;
     private DatabaseReference reference= FirebaseDatabase.getInstance().getReference();


    private EditText editTextdestinatario;

    private String id,Aptos;
    String currentPhotoPath;
    Bitmap bitmapUplord;
    ProgressBar progressBar;
    static final int REQUEST_TAKE_PHOTO = 1;
    ImageView imageviewassinatura;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_correspondencias);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
        getSupportActionBar().setTitle("Cadastro de correspondencia");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},0);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final String usua = getIntent().getStringExtra("usua");
        final DatabaseReference correspondencia = reference.child("correspondencias");
       Button botaosalvarCadastro=(Button)findViewById(R.id.buttoncadastro);
        Button foto=(Button)findViewById(R.id.btnNovafoto);
        imageviewassinatura=findViewById(R.id.imgnovafoto);
        progressBar = findViewById(R.id.progressBarCadastro);
        destinatario =(EditText)findViewById(R.id.editTextdestinatatioCadastro);
        discriminacao = (EditText)findViewById(R.id.editText2discriminacãocadastro);
        spinnertipo=findViewById(R.id.spinnertipo);
        spinnerapto=findViewById(R.id.spinnerApto);
        progressBar.setVisibility(View.GONE);
        ArrayAdapter<CharSequence> adapterapto = ArrayAdapter.createFromResource(this,R.array.Aptos,android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adaptertipo = ArrayAdapter.createFromResource(this,R.array.tipo,android.R.layout.simple_spinner_item);
        adaptertipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterapto.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnertipo.setAdapter(adaptertipo);
        spinnerapto.setAdapter(adapterapto);
        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });
        botaosalvarCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stringtipo = null;
                String stringapto = null;
                progressBar.setVisibility(View.VISIBLE);
                Calendar c = Calendar.getInstance();

                System.out.println("Current time => "+c.getTime());

                SimpleDateFormat df = new SimpleDateFormat(" dd/MM/yyyy");
                SimpleDateFormat dh =new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss");
                String formattedDate = df.format(c.getTime());
                String formattedDate2 =dh.format(c.getTime());
              if(id==null){  id=formattedDate2.toString();}
                if(spinnertipo.getSelectedItem().toString().equals("") || spinnerapto.getSelectedItem().toString().equals("") ) {

                    Toast.makeText(getApplicationContext(),"INFORME O APTO E O TIPO DE CORRESPONDÊNÇIAS",Toast.LENGTH_LONG).show();
                  return;
                } else  {
                    //Spinner vazio
                    stringtipo = (String) spinnertipo.getSelectedItem();
                    stringapto =(String) spinnerapto.getSelectedItem();
                    Toast.makeText(getApplicationContext(),stringtipo+stringapto,Toast.LENGTH_LONG).show();


                  if(destinatario.getText().length()!= 0 && discriminacao.getText().length()!=0)
                  {
                      DatasetFire novocadastro = new DatasetFire();
                      novocadastro.setTipo(stringtipo);
                      novocadastro.setApto(stringapto);
                      novocadastro.setDestinatario(destinatario.getText().toString());
                      novocadastro.setDiscriminacao(discriminacao.getText().toString());
                      novocadastro.setCodigo(formattedDate2);
                      novocadastro.setDataentrega("");
                      novocadastro.setQuemrecebeu("");
                      novocadastro.setStatus("ativo");
                      novocadastro.setDataregistro(formattedDate);
                      novocadastro.setUsuarioregistro(usua);
                      novocadastro.setUsuarioBaixou("");
                      novocadastro.setImageAsiinatura(id+".jpeg");
                      if (imageviewassinatura.getDrawable()!=null){}
                      else {

                          Toast.makeText(CadastroCorrespondencias.this,"capture a foto da assinatura"
                                  ,Toast.LENGTH_LONG).show();
                          return;
                      }
                      correspondencia.push().setValue(novocadastro);
                     uploadDaImagem();

                      Toast.makeText(getApplicationContext(), "Salvo com sucesso", Toast.LENGTH_LONG).show();
                      progressBar.setVisibility(View.GONE);
                      stringapto = null;
                      stringtipo= null;
                      imageviewassinatura.setImageBitmap(null);
                      destinatario.setText("");
                      discriminacao.setText("");
                      spinnerapto.setSelection(0);
                      spinnertipo.setSelection(0);
                      id=null;

                      return;

                  }else {

                          Toast.makeText(getApplicationContext(), "INFORME O DESTINATARIO E A DISCRIMINAÇÃO", Toast.LENGTH_LONG).show();
                          return;

                      }


                  }// }else
                  //  {Toast.makeText(getApplicationContext(),"INFORME O DESTINATARIO E A DISCRIMINAÇÃO",Toast.LENGTH_LONG).show();
                  //     return;
                   // }
            }
        });



    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.home:
                startActivity(new Intent(this,CadastroCorrespondencias.class));
                finishAffinity();

               break;

        }
     return super.onOptionsItemSelected(item);

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
        bitmapUplord = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        imageviewassinatura.setImageBitmap(bitmap);
        boolean p = new File(currentPhotoPath).delete();
        if(p){}else {
            Toast.makeText(CadastroCorrespondencias.this,"não deletou",Toast.LENGTH_LONG)
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
        //imageviewassinatura.setDrawingCacheEnabled(true);
        //imageviewassinatura.buildDrawingCache();
        Bitmap bitmap =bitmapUplord;
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,75,bao);
        byte[] dadosassinatura = bao.toByteArray();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference imagens =storageReference.child("imagemassinatura");
        StorageReference imagearefe=imagens.child(id+".jpeg");
        UploadTask uploadTask=imagearefe.putBytes(dadosassinatura);
        uploadTask.addOnFailureListener(CadastroCorrespondencias.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CadastroCorrespondencias.this,"falha a salvar a imagem"+e.getMessage().toString(),Toast.LENGTH_LONG).show();
            }
        });
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
}
