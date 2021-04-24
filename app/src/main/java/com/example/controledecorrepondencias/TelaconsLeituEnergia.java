package com.example.controledecorrepondencias;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class TelaconsLeituEnergia extends AppCompatActivity {
    private RecyclerView recyclerView2;
    private ArrayList<ControleleituraDia> arrayList;
    private FirebaseRecyclerOptions<ControleleituraDia> options;
    //private Button buttonFiltroCorresp;

    private EditText editDigitedata;
    private Spinner spinnerselecionecorresp;
    private FirebaseRecyclerAdapter<ControleleituraDia,FirebaseViewHolder> adapter;
    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telacons_leitu_energia);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
        getSupportActionBar().setTitle("Consulta historico consumo");
        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //final String usua = getIntent().getStringExtra("usua");
        //spinnerselecionecorresp= findViewById(R.id.spinnertipodeconsultas);
        //buttonFiltroCorresp=findViewById(R.id.buttonfiltrocorrespondencia);
        editDigitedata=findViewById(R.id.editTextpequiEnergia);
        editDigitedata.addTextChangedListener(Mask.insert(Mask.DATA_MES_MASK,  editDigitedata));
        recyclerView2 = findViewById(R.id.recyclerVleituraEnergia);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        arrayList=new ArrayList<ControleleituraDia>();

        // ArrayAdapter<CharSequence> adaptertipo = ArrayAdapter.createFromResource(this,R.array.tipoconsulta,android.R.layout.simple_spinner_item);
        //adaptertipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinnerselecionecorresp.setAdapter(adaptertipo);

        editDigitedata.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                DatabaseReference novaleitura = referencia.child("leituraEnergia");
                Query leituras=novaleitura.orderByChild("datames").startAt(editDigitedata.getText().toString())
                        .endAt(editDigitedata.getText().toString()+"\uf8ff");
                novaleitura.keepSynced(true);
                options = new FirebaseRecyclerOptions.Builder<ControleleituraDia>().setQuery(leituras,ControleleituraDia.class).build();
                adapter=new FirebaseRecyclerAdapter<ControleleituraDia, FirebaseViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull FirebaseViewHolder holder, int position, @NonNull ControleleituraDia model) {
                        holder.dataleitura.setText(model.getDataenergia());
                        holder.leitura.setText("Leitura: "+model.getLeituraEnergia());
                        holder.consumo.setText("Consumo: "+model.getConsumoEnergia());

                    }

                    @NonNull
                    @Override
                    public FirebaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        return new FirebaseViewHolder(LayoutInflater.from(TelaconsLeituEnergia.this).inflate(R.layout.rowenergia,parent,false));
                    }
                };
                adapter.startListening();
                recyclerView2.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
