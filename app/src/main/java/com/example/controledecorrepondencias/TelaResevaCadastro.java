package com.example.controledecorrepondencias;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TelaResevaCadastro extends AppCompatActivity {
    private Spinner spinnerreserva,spinneraptoreserva;
    private EditText editTextDta,editTextrespreserva;
    private Button buttonsalvarCadstradoreserva;
    private TextView textViewStatus;
    private DatabaseReference reference= FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_reseva_cadastro);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final DatabaseReference reserva = reference.child("Reservas");
        editTextDta=findViewById(R.id.editTextdatareservacadastro);
        final String usua = getIntent().getStringExtra("usua");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
        getSupportActionBar().setTitle("Cadastro de nova reserva");
        editTextrespreserva=findViewById(R.id.editTextresponsavelreservacadastro);
        buttonsalvarCadstradoreserva=findViewById(R.id.buttonCadastrareserva);
        spinneraptoreserva=findViewById(R.id.spinneraptoreservas);
        textViewStatus=findViewById(R.id.textViewStatusReseva);
        spinnerreserva = findViewById(R.id.spinnertipodereserva);
        ArrayAdapter<CharSequence> adapterreservas = ArrayAdapter.createFromResource(this,R.array.tipodereserva,android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapterApto = ArrayAdapter.createFromResource(this,R.array.Aptos,android.R.layout.simple_spinner_item);
        adapterApto.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinneraptoreserva.setAdapter(adapterApto);
        adapterreservas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerreserva.setAdapter(adapterreservas);
        editTextDta=findViewById(R.id.editTextdatareservacadastro);
        editTextDta.addTextChangedListener(Mask.insert(Mask.DATA_MASK,  editTextDta));
        buttonsalvarCadstradoreserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Calendar c = Calendar.getInstance();

                System.out.println("Current time => "+c.getTime());

                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                final String formattedDate = df.format(c.getTime());
                if(spinnerreserva.getSelectedItem().toString().equals("") || spinneraptoreserva.getSelectedItem().toString().equals("") ) {

                    Toast.makeText(getApplicationContext(),"INFORME O APTO E O TIPO DE RESERVA",Toast.LENGTH_LONG).show();
                    return;
                }else
                    {
                        if(editTextrespreserva.getText().length()!= 0 && editTextDta.getText().length()!=0){
                      // stringtiporeserva = spinnerreserva.getSelectedItem().toString();
                     //strinaptoreserva =(String) spinneraptoreserva.getSelectedItem();

                        final String  nulo=null;

                            final Query pesquisaAtiva = reserva.orderByChild("idReserva").equalTo(editTextDta.getText().toString()+spinnerreserva.getSelectedItem().toString());
                                      pesquisaAtiva.addListenerForSingleValueEvent(new ValueEventListener() {
                             @Override
                             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()){
                                    Controlereservas a= dataSnapshot.getValue(Controlereservas.class);
                                    Toast.makeText(TelaResevaCadastro.this,"já existeuma reserva para "+a.idReserva,Toast.LENGTH_LONG).show();

                                }else{
                                     String stringtiporeserva = null;
                                      String strinaptoreserva = null;
                                      stringtiporeserva = spinnerreserva.getSelectedItem().toString();
                                      strinaptoreserva =(String) spinneraptoreserva.getSelectedItem();
                                      Controlereservas CntReservas = new Controlereservas();
                                      CntReservas.setDtaEvento(editTextDta.getText().toString());
                                      CntReservas.setDtareserva(formattedDate);
                                      CntReservas.setIdReserva(editTextDta.getText().toString()+stringtiporeserva);
                                      CntReservas.setResponsReserva(editTextrespreserva.getText().toString());
                                      CntReservas.setUsuarioQbaixou("");
                                      CntReservas.setStatus("ativo");
                                      CntReservas.setUsuarioQReservou(usua);
                                      CntReservas.setTiporeserva(stringtiporeserva);
                                      CntReservas.setAptoReserva(strinaptoreserva);

                                      reserva.push().setValue(CntReservas);
                                      Toast.makeText(getApplicationContext(), "Salvo com sucesso", Toast.LENGTH_LONG).show();
                                      strinaptoreserva = null;
                                      stringtiporeserva= null;
                                      editTextrespreserva.setText("");
                                      editTextDta.setText("");
                                      spinneraptoreserva.setSelection(0);
                                      spinnerreserva.setSelection(0);
                                     return;
                                    //Toast.makeText(TelaResevaCadastro.this,"já nao existeuma reserva para ",Toast.LENGTH_LONG).show();

                                }



                             }

                             @Override
                             public void onCancelled(@NonNull DatabaseError databaseError) {
                                 Toast.makeText(TelaResevaCadastro.this,"já existeuma reserva para "+databaseError.toString(),Toast.LENGTH_LONG).show();
                                 return;
                             }

                         });




                        }else{
                            Toast.makeText(getApplicationContext(), "INFORME O DESTINATARIO E A DATA", Toast.LENGTH_LONG).show();
                            return;
                        }
                }
            }
        });

    }
}
