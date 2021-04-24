package com.example.controledecorrepondencias;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class CorrespondeciasAtivas extends AppCompatActivity {
    private RecyclerView recyclerView1;
    ArrayList<DatasetFire>arrayList;
    private FirebaseRecyclerOptions<DatasetFire>options;
    private  FirebaseRecyclerAdapter<DatasetFire,FirebaseViewHolder>adapter;
    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();
    private AlertDialog alerta;
   String id;

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_correspondecias_ativas);
        setTitle("Lista de correspondências ativas");
        final String usua = getIntent().getStringExtra("usua");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        recyclerView1 = findViewById(R.id.recyclerView1);
        recyclerView1.setHasFixedSize(true);
        recyclerView1.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        arrayList=new ArrayList<DatasetFire>();

        final DatabaseReference correspo = referencia.child("correspondencias");
        //databaseReferenca = FirebaseDatabase.getInstance().getReference().child("001");
        final Query consultastatus = correspo.orderByChild("status").equalTo("ativo");
        correspo.keepSynced(true);




        options = new FirebaseRecyclerOptions.Builder<DatasetFire>().setQuery(consultastatus,DatasetFire.class).build();

       adapter=new FirebaseRecyclerAdapter<DatasetFire, FirebaseViewHolder>(options) {
           @Override
           protected void onBindViewHolder(@NonNull final FirebaseViewHolder holder, int position, @NonNull final DatasetFire model) {

                 holder.apto.setText("Apto "+model.getApto());
                 holder.tipo.setText(model.getDiscriminacao());
                 holder.destinatario.setText(model.getDestinatario());

                 holder.codigo.setText(model.getCodigo());
               try {
                   StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                   StorageReference imagens =storageReference.child("imagemassinatura");
                   StorageReference imagearefe=imagens.child(model.imageAsiinatura);



                   Glide.with(CorrespondeciasAtivas.this)
                           .using(new FirebaseImageLoader())
                           .load(imagearefe)
                           .into(holder.imageView);
               }catch (Exception e){
                   Toast.makeText(CorrespondeciasAtivas.this,"não foi possivel carregar a imagem, " +
                                   "pode ser que ela ainda não exista"
                           ,Toast.LENGTH_LONG).show();
               }

                 holder.menu_option.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                         final Query consultastatus2 = correspo.orderByChild("codigo").equalTo(model.getCodigo());
                        consultastatus2.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                id=dataSnapshot.getKey();
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
                         //creating a popup menu
                         PopupMenu popup = new PopupMenu(CorrespondeciasAtivas.this, holder.menu_option);
                         //inflating menu from xml resource
                         popup.inflate(R.menu.options_menu);


                         //adding click listener
                         popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                             @Override
                             public boolean onMenuItemClick(MenuItem item) {
                                 switch (item.getItemId()) {
                                     case R.id.menu_baixar:
                                         //handle menu1 click
                                         Intent intent = new Intent(CorrespondeciasAtivas.this,BaixarCorrespondencia.class);
                                         intent.putExtra("codigo",model.getCodigo());
                                         intent.putExtra("apto",model.getApto());
                                         intent.putExtra("tipo",model.getTipo());
                                         intent.putExtra("dataregistro",model.dataregistro);
                                         intent.putExtra("usua",usua);
                                         intent.putExtra("usuarioregis",model.getUsuarioregistro());
                                         startActivity(intent);

                                         return true;



                                     case R.id.menu_excluir:
                                         //handle menu3 click
                                         AlertDialog.Builder builder = new AlertDialog.Builder(CorrespondeciasAtivas.this);
                                         //define o titulo
                                         builder.setTitle("Excluir");
                                         //define a mensagem
                                         builder.setMessage("Quer excluir esse id ("+model.getDiscriminacao()+")?" );
                                         //define um botão como positivo
                                         builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
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
                                                 StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                                                 StorageReference imagens =storageReference.child("imagemassinatura");
                                                 StorageReference imagearefe=imagens.child(model.imageAsiinatura);
                                                 imagearefe.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                     @Override
                                                     public void onSuccess(Void aVoid) {
                                                         Toast.makeText(getApplicationContext(),"imagem apagada com sucesso",Toast.LENGTH_LONG).show();

                                                     }
                                                 }).addOnFailureListener(new OnFailureListener() {
                                                     @Override
                                                     public void onFailure(@NonNull Exception e) {
                                                         Toast.makeText(getApplicationContext(),"erro! a imagem não foi apagada "+e.toString(),Toast.LENGTH_LONG).show();


                                                     }
                                                 });
                                                 //Toast.makeText(CorrespondeciasAtivas.this, "positivo=" + arg1, Toast.LENGTH_SHORT).show();
                                             }
                                         });
                                         //define um botão como negativo.
                                         builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                                             public void onClick(DialogInterface arg0, int arg1) {
                                                 Toast.makeText(CorrespondeciasAtivas.this, "Cancelado" , Toast.LENGTH_SHORT).show();
                                             }
                                         });
                                         //cria o AlertDialog
                                         alerta = builder.create();
                                         //Exibe
                                         alerta.show();


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

           @NonNull
           @Override
           public FirebaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

               return new FirebaseViewHolder(LayoutInflater.from(CorrespondeciasAtivas.this).inflate(R.layout.row,parent,false));
           }
       };





        recyclerView1.setAdapter(adapter);
    }

}
