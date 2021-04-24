package com.example.controledecorrepondencias;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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
import java.util.ArrayList;

import static android.content.Intent.ACTION_SEND;
import static android.content.Intent.EXTRA_STREAM;

public class Telacosultaleituraaptos extends AppCompatActivity {
    private RecyclerView recyclerView2;
    private ArrayList<Controleleituraaptos> arrayList;
    private FirebaseRecyclerOptions<Controleleituraaptos> options;
    //private Button buttonFiltroCorresp;
    private TextView textViewmenu;
    private Query consultastatus = null;
    private EditText editTextdigiteconsulta;
    private Spinner spinnerselecionetipo;
    private FirebaseRecyclerAdapter<Controleleituraaptos,FirebaseViewHolder> adapter;
    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telacosultaleituraaptos);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
        getSupportActionBar().setTitle("Historico de consumo dos aptos");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final String usua = getIntent().getStringExtra("usua");
        spinnerselecionetipo= findViewById(R.id.spnrTipoConsLeituraApto);
        //buttonFiltroCorresp=findViewById(R.id.buttonfiltrocorrespondencia);
        editTextdigiteconsulta=findViewById(R.id.edtdigiteConsLeituraApto);
        textViewmenu=findViewById(R.id.textViewOptions3);
        recyclerView2 = findViewById(R.id.recyclerVleituraApto);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        arrayList =new ArrayList<Controleleituraaptos>();
        final ArrayList<String>arrayList1= new ArrayList<String>();
        arrayList1.add("Relatorio de consumo de agua dos aptos"+"\n\n");
        final ArrayList<String>arrayList2= new ArrayList<String>();
        arrayList2.add("Relatorio de consumo de agua dos aptos"+"\n\n");
        ArrayAdapter<CharSequence> adaptertipo = ArrayAdapter.createFromResource(this,R.array.tipoleituraapto,android.R.layout.simple_spinner_item);
        adaptertipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerselecionetipo.setAdapter(adaptertipo);
        spinnerselecionetipo.setOnItemSelectedListener(new  AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView parent,View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                if(item.equals("Data")){editTextdigiteconsulta.setHint("Digite uma data ##/##/####");}
                else{editTextdigiteconsulta.setHint(
                        "Digite o numero do apto");}
            }
            public void onNothingSelected(AdapterView parent){}
        });
        editTextdigiteconsulta.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                consultastatus = null;
                DatabaseReference novasleituras = referencia.child("leituras-aptos");
                //DatabaseReference correspo = referencia.child("correspondencias");
                if(spinnerselecionetipo.getSelectedItem().toString().equals("Apto")){
                    //Toast.makeText(TelaConsultaCorrespondencias.this,"estou aqui",Toast.LENGTH_LONG).show();
                    consultastatus = novasleituras.orderByChild("apto").equalTo("Apto-"+
                            editTextdigiteconsulta.getText().toString());

                    arrayList1.clear();
                    arrayList1.add("Relatorio de consumo de agua dos aptos\""+"\n\n");arrayList2.clear();
                    arrayList2.add("Relatorio de consumo de agua dos aptos\""+"\n\n"); }
                if(spinnerselecionetipo.getSelectedItem().toString().equals("Data")){
                    consultastatus = novasleituras.orderByChild("data").startAt(
                            editTextdigiteconsulta.getText().toString()
                    ).endAt(editTextdigiteconsulta.getText().toString()+"\uf8ff");
                    arrayList1.clear();
                    arrayList1.add("Relatorio de consumo de agua dos aptos\""+"\n\n");arrayList2.clear();
                    arrayList2.add("Relatorio de consumo de agua dos aptos\""+"\n\n"); }
                novasleituras.keepSynced(true);
                options = new FirebaseRecyclerOptions.Builder<Controleleituraaptos>().setQuery(consultastatus, Controleleituraaptos.class).build();
                adapter=new FirebaseRecyclerAdapter<Controleleituraaptos, FirebaseViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull FirebaseViewHolder holder, int position, @NonNull final Controleleituraaptos model) {
                        holder.aptoleitura.setText(model.getApto()+" ");
                        holder.dataleitura.setText(model.getData());
                        holder.leitura.setText("leitura"+model.getLeitura());
                        holder.consumo.setText("consumo: "+model.getConsumo());
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //Intent intent = new Intent(TelaConsultaCorrespondencias.this,TelaConsulracorrespndencias2.class);
                                //intent.putExtra("codigo",model.getCodigo());
                               // startActivity(intent);
                            }
                        });
                    }
                    @NonNull
                    @Override
                    public FirebaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

                        return new FirebaseViewHolder(LayoutInflater.from(Telacosultaleituraaptos.this).inflate(R.layout.rowleituras,parent,false));
                    }
                };
                consultastatus.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Controleleituraaptos model1 =dataSnapshot.getValue(Controleleituraaptos.class);
                        for (int i=0;i<1;i++){
                            //texto.setText(f.apto+i+ i++ );
                            arrayList1.add("\n"+model1.getApto()+"\n"+"Data: "+model1.getData()+"  Leitura: "+model1.getLeitura()
                                    +"  Consumo: "+model1.getConsumo() +"\n");}
                    }@Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }
                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });
                consultastatus.limitToLast(10).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Controleleituraaptos model1 =dataSnapshot.getValue(Controleleituraaptos.class);
                        for (int i=0;i<1;i++){
                            //texto.setText(f.apto+i+ i++ );
                            arrayList2.add("\n"+model1.getApto()+"\n"+"Data: "+model1.getData()+"  Leitura: "+model1.getLeitura()
                                    +"  Consumo: "+model1.getConsumo() +"\n");
                        }
                    }@Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }
                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });
                adapter.startListening();
                recyclerView2.setAdapter(adapter);
            }
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) { }
        });
        textViewmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(Telacosultaleituraaptos.this, textViewmenu);
                //inflating menu from xml resource
                popup.inflate(R.menu.menu_compartilhar);


                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_exportarpdf:
                                //handle menu1 click

                                createPdf(arrayList1.toString()+"\n\n\n  Humberto Rodrigues");

                                return true;

                            case R.id.menu_exportarpdf10:
                                //handle menu1 click

                                createPdf(arrayList2.toString());

                                return true;

                            case R.id.menu_exportartexto:
                                //handle menu1 click

                                Intent intentsend = new Intent();
                                intentsend.setAction(ACTION_SEND);
                                intentsend.putExtra(Intent.EXTRA_TEXT,arrayList2.toString());
                                intentsend.setType("text/plain");
                                Intent shareIntent = Intent.createChooser(intentsend,null);
                                startActivity(shareIntent);

                                return true;
                            case R.id.menu_exportarwhatsapp:
                                enviarWhats(arrayList2.toString());
                                //handle menu3 click
                             /*  AlertDialog.Builder builder = new AlertDialog.Builder(CorrespondeciasAtivas.this);
                               //define o titulo
                               builder.setTitle("Excluir");
                               //define a mensagem
                               builder.setMessage("Quer excluir esse id ("+model.getDiscriminacao()+")?" );
                               //define um botão como positivo
                               builder.setPositiveButton("Positivo", new DialogInterface.OnClickListener() {
                                   public void onClick(DialogInterface arg0, int arg1) {
                                       correspo.child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                           @Override
                                           public void onComplete(@NonNull Task<Void> task) {
                                               if(task.isSuccessful()){
                                                   Toast.makeText(getApplicationContext(),"excluido o id "+model.getDiscriminacao(),Toast.LENGTH_LONG).show();
                                               }else{
                                                   Toast.makeText(getApplicationContext(),"erro",Toast.LENGTH_LONG).show();
                                               }
                                           }
                                       });
                                       //Toast.makeText(CorrespondeciasAtivas.this, "positivo=" + arg1, Toast.LENGTH_SHORT).show();
                                   }
                               });
                               //define um botão como negativo.
                               builder.setNegativeButton("Negativo", new DialogInterface.OnClickListener() {
                                   public void onClick(DialogInterface arg0, int arg1) {
                                       Toast.makeText(CorrespondeciasAtivas.this, "Cancelado" , Toast.LENGTH_SHORT).show();
                                   }
                               });
                               //cria o AlertDialog
                               alerta = builder.create();
                               //Exibe
                               alerta.show();*/


                                return true;
                            default:
                                return false;
                        }
                    }
                });
                //displaying the popup
                popup.show();
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
            arquivo = FileProvider.getUriForFile(Telacosultaleituraaptos.this,
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
