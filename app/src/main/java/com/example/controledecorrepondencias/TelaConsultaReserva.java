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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class TelaConsultaReserva extends AppCompatActivity {
    private RecyclerView recyclerView3;
    private ArrayList<Controlereservas> arrayList;
    private FirebaseRecyclerOptions<Controlereservas> options;
    private FirebaseRecyclerAdapter<Controlereservas,FirebaseViewHolder> adapter;
    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();
    String id;
    private AlertDialog alerta;


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
        setContentView(R.layout.activity_tela_consulta_reserva);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
        getSupportActionBar().setTitle("Consulta reservas");
        final String usua = getIntent().getStringExtra("usua");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        recyclerView3 = findViewById(R.id.recyclerView3);
        recyclerView3.setHasFixedSize(true);
        recyclerView3.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        recyclerView3.setLayoutManager(new LinearLayoutManager(this));
        arrayList=new ArrayList<Controlereservas>();

        final DatabaseReference reserva = referencia.child("Reservas");
        //databaseReferenca = FirebaseDatabase.getInstance().getReference().child("001");
        Query consultastatus = reserva.orderByChild("status").equalTo("ativo");
        reserva.keepSynced(true);
        //options = new FirebaseRecyclerOptions.Builder<Controlereservas>().setQuery(consultastatus,Controlereservas.class).build();
           options=new FirebaseRecyclerOptions.Builder<Controlereservas>().setQuery(consultastatus,Controlereservas.class).build();
        adapter=new FirebaseRecyclerAdapter<Controlereservas, FirebaseViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final FirebaseViewHolder holder, int position, @NonNull final Controlereservas model) {
                holder.apto.setText("Apto "+model.getAptoReserva());
                holder.tipo.setText(model.getTiporeserva());
                holder.destinatario.setText(model.getDtaEvento());

                holder.codigo.setText(model.getIdReserva());
                holder.menu_option2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Query consultareserva2 = reserva.orderByChild("idReserva").equalTo(model.getIdReserva());
                        consultareserva2.addChildEventListener(new ChildEventListener() {
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
                        PopupMenu popup = new PopupMenu(TelaConsultaReserva.this,holder.menu_option2);
                        //inflating menu from xml resource
                        popup.inflate(R.menu.menu_reserva);
                        //adding click listener
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.menu_baixar:
                                        final Controlereservas controlereservas= new Controlereservas();
                                        controlereservas.setStatus("Finalizado");
                                        controlereservas.setAptoReserva(model.getAptoReserva());
                                        controlereservas.setTiporeserva(model.getTiporeserva());
                                        controlereservas.setUsuarioQReservou(model.getUsuarioQReservou());
                                        controlereservas.setUsuarioQbaixou(usua);
                                        controlereservas.setResponsReserva(model.getResponsReserva());
                                        controlereservas.setIdReserva(model.getIdReserva());
                                        controlereservas.setDtaEvento(model.getDtaEvento());
                                        controlereservas.setDtareserva(model.getDtareserva());

                                        AlertDialog.Builder builder = new AlertDialog.Builder(TelaConsultaReserva.this);
                                        //define o titulo
                                        builder.setTitle("Baixar");
                                        //define a mensagem
                                        builder.setMessage("Quer baixar essa reserva ("+model.getIdReserva()+")?" );

                                        builder.setPositiveButton("Positivo", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                reserva.child(id).setValue(controlereservas).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        if(task.isSuccessful()){
                                                            Toast.makeText(getApplicationContext(),"Baixado com sucesso a reserva "+model.getIdReserva(),Toast.LENGTH_LONG).show();
                                                        }else{
                                                            Toast.makeText(getApplicationContext(),"erro",Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                            }
                                        });
                                        builder.setNegativeButton("Negativo", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                Toast.makeText(TelaConsultaReserva.this, "Cancelado" , Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        //cria o AlertDialog
                                        alerta = builder.create();
                                        //Exibe
                                        alerta.show();
                                        return true;

                                    case R.id.menu_excluir:
                                        AlertDialog.Builder builder2 = new AlertDialog.Builder(TelaConsultaReserva.this);
                                        //define o titulo
                                        builder2.setTitle("Excluir");
                                        //define a mensagem
                                        builder2.setMessage("Quer excluir essa reserva ("+model.getIdReserva()+")?" );

                                        builder2.setPositiveButton("Positivo", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                reserva.child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        if(task.isSuccessful()){
                                                            Toast.makeText(getApplicationContext(),"excluido o id "+model.getIdReserva(),Toast.LENGTH_LONG).show();
                                                        }else{
                                                            Toast.makeText(getApplicationContext(),"erro",Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                            }
                                        });
                                        builder2.setNegativeButton("Negativo", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                Toast.makeText(TelaConsultaReserva.this, "Cancelado" , Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        //cria o AlertDialog
                                        alerta = builder2.create();
                                        //Exibe
                                        alerta.show();

                                        return  true;
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

                return new FirebaseViewHolder(LayoutInflater.from(TelaConsultaReserva.this).inflate(R.layout.rowreserva, parent, false));
            }
        };
        recyclerView3.setAdapter(adapter);
    }
}
