package com.example.controledecorrepondencias;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Telalogin extends AppCompatActivity {


    private Button btlogin;
    private EditText edtemail,edtsenha;
    private ProgressBar progressBar;
    private DatasetUsuario usuario;
    private FirebaseAuth autenticacao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telalogin);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        inicializarComponente();

        progressBar.setVisibility(View.GONE);
        btlogin.setEnabled(false);
        edtemail.setInputType(InputType.TYPE_CLASS_TEXT| InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        edtsenha.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edtsenha.length()>5){btlogin.setEnabled(true);}else{btlogin.setEnabled(false);} }
            @Override
            public void afterTextChanged(Editable editable) { }
        });
        // progressBar.setVisibility(View.GONE);

        btlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textoemail = edtemail.getText().toString();
                String textosenha =edtsenha.getText().toString();
                if (!textoemail.isEmpty()){
                    if (!textosenha.isEmpty()){
                        usuario =new DatasetUsuario();
                        usuario.setEmail(textoemail);
                        usuario.setSenha(textosenha);
                        loginusuario(usuario);

                    }else {
                        Toast.makeText(Telalogin.this,"preencha senha", Toast.LENGTH_LONG).show();

                    }
                }else { Toast.makeText(Telalogin.this,"preencha email", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void loginusuario(final DatasetUsuario usuario){
        progressBar.setVisibility(View.VISIBLE);
        //String textoemail = edtemail.getText().toString();
        //String textosenha =edtsenha.getText().toString();
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(usuario.getEmail(),usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Telalogin.this,"logado com sucesso", Toast.LENGTH_LONG).show();
                    //startActivity(new Intent(getApplicationContext(),TelaPrincincipal.class));

                    Intent intent = new Intent(Telalogin.this,TelaPrincincipal.class);
                    intent.putExtra("idusuario1",autenticacao.getUid());
                    finish();
                    startActivity(intent);
                }
                else {
                    progressBar.setVisibility(View.GONE);
                    String erroExcecao = "";
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e) {
                        erroExcecao="";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        erroExcecao="  emial ou senha inválida";
                    }catch (FirebaseAuthUserCollisionException e){
                        erroExcecao="";
                    }catch (Exception e){
                        erroExcecao=""+e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(Telalogin.this,"Erro"+erroExcecao, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void inicializarComponente(){
        btlogin=findViewById(R.id.buttonLogin);
        edtemail=findViewById(R.id.edloginusuario);
        edtsenha=findViewById(R.id.editTextSenhaLogin);
        progressBar=findViewById(R.id.progressBarteladelogin);
    }
}