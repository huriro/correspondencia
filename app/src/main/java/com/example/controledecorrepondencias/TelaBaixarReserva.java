package com.example.controledecorrepondencias;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;

import static android.content.Intent.ACTION_SEND;
import static android.content.Intent.ACTION_VIEW;
import static android.content.Intent.EXTRA_STREAM;
import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;

public class TelaBaixarReserva extends AppCompatActivity {
    private Button botao,botaocompartilhar;
    private EditText texto;
    private PdfDocument pdf;
    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_baixar_reserva);
        final ArrayList<String>arrayList=new ArrayList<String>();
        botao=findViewById(R.id.button);
        texto=findViewById(R.id.editText);
        botaocompartilhar=findViewById(R.id.button2);
        final ArrayList<String> agenda = new ArrayList();




        DatabaseReference correspo = referencia.child("correspondencias");
        Query consultastatus = correspo.orderByChild("apto").equalTo("81").limitToLast(15);
        consultastatus.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                DatasetFire f =dataSnapshot.getValue(DatasetFire.class);
                   for (int i=0;i<1;i++){
                   //texto.setText(f.apto+i+ i++ );
                   arrayList.add("Apto: "+f.getApto()+"\n "+"Destinatario: "+f.getDestinatario()+"\n "
                           +"Data do registro: "+f.getDataregistro()+"\n "+"Data da entrega: "+f.getDataentrega()
                           +"\n "+"Tipo: "+f.getTipo()+"\n "+"Discrminacção: "+f.getDiscriminacao()+"\n "
                           +"Status: "+f.getStatus()+"\n"+"\n");
                   texto.setText("Relatorio de Correspondencias do apto "+f.getApto()+"\n\n"+arrayList.toString());
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

        // [ B ] usando o método add() para gravar 4 contatos na agenda
        agenda.add("Juca Bala;11 1111-1111\n");
        agenda.add("Marcos Paqueta;22 2222-2222\n");
        agenda.add("Maria Antonieta;33 3333-3333\n");
        agenda.add("Antônio Conselheiro;44 4444-4444");

        int i;

        // [ C ] mostrando os "n" contatos da agenda (usando o índice)
        // número de elementos da agenda: método size()
        System.out.printf("Percorrendo o ArrayList (usando o índice)\n");
        int n = agenda.size();
        for (i=0; i<n; i++) {
            texto.setText(agenda.toString());
        }
        botao.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                createPdf(texto.getText().toString());
            }
        });
        botaocompartilhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               enviarWhats(texto.getText().toString());
               /* Intent intentsend = new Intent();
                intentsend.setAction(ACTION_SEND);
                intentsend.putExtra(Intent.EXTRA_TEXT,texto.getText());
                intentsend.setType(texto.toString());
                Intent shareIntent = Intent.createChooser(intentsend,null);
                startActivity(shareIntent);*/
            }
        });
    }
    public void enviarWhats(String mensagem){
        PackageManager pm = getPackageManager();

        try {
            String text = mensagem;

        Intent waintent = new Intent(ACTION_SEND);
        waintent.setType("text/plain");
        PackageInfo info =pm.getPackageInfo("com.whatsapp",PackageManager.GET_META_DATA);
        waintent.setPackage("com.whatsapp");
        waintent.putExtra(Intent.EXTRA_TEXT,text);
        startActivity(waintent);
        }catch (PackageManager.NameNotFoundException e){}
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void createPdf(String sometext){

        File directory_path  = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(directory_path,"/pdf/");
        if (!file.exists()) {
            file.mkdirs();
        }

            Document document = new Document();
        try{
            PdfWriter.getInstance(document,new FileOutputStream(directory_path+"/pdf/Relatorio.pdf"));
            document.open();
            document.addAuthor("portariatruth");
            document.add(new Paragraph(sometext));
            document.close();

           // Toast.makeText(this,"salvo em "+directory_path+"/pdf/meupdf.pdf",Toast.LENGTH_LONG).show();
        }
        catch (Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();

        }


        try {
       //String filePath = Environment.getExternalStorageDirectory()+"/mypdf/"+"meupdf.pdf";
        File file2 = new File(directory_path +"/pdf/"+ "Relatorio.pdf");
        Uri arquivo;
        arquivo = FileProvider.getUriForFile(
               TelaBaixarReserva.this,
               "com.example.android.fileprovider",
               file2 );
        Intent intent = new Intent();
        intent.setAction(ACTION_SEND);
        intent.setType("application/pdf"); // identifica o tipo de arquivo que vc quer compartilhar, isso permite que a lista de programas exibidas seja correta(so exibira apps capazes de lerem pdf

        intent.putExtra(EXTRA_STREAM,arquivo);
        intent.putExtra(Intent.EXTRA_TEXT,"Relatorio Pdf");
        intent.putExtra(Intent.EXTRA_TITLE,"Relatorio pdf");
        //adiciona o arquivo a ser compartilhado
        startActivity(Intent.createChooser(intent,"compartilhar"));
          startActivity(intent);
        } catch (ActivityNotFoundException e) {
            //Caso o usuário não tenha um visualizador de PDF, instrua-o aqui a baixar
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }


    }
}
