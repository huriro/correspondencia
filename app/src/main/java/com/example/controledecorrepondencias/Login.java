package com.example.controledecorrepondencias;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

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

public class Login extends AppCompatActivity {
    private Button btlogin;
    private EditText edtemail,senha;
    private ProgressBar progressBar;
    private DatasetUsuario usuario;
    private FirebaseAuth autenticacao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btlogin=findViewById(R.id.button3);
        edtemail=findViewById(R.id.edEmail);
        senha=findViewById(R.id.edSenha1);

        btlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String textoemail = edtemail.getText().toString();
                String textosenha =senha.getText().toString();
                if (!textoemail.isEmpty()){
                    if (!textosenha.isEmpty()){
                        if (senha.length()>5){}else{
                            Toast.makeText(Login.this,"a senha deve ser de no minimo 6 digito",Toast.LENGTH_LONG).show();
                            return;}
                        usuario =new DatasetUsuario();
                        usuario.setEmail(textoemail);
                        usuario.setSenha(textosenha);
                       loginusuario(usuario);

                    }else {
                        Toast.makeText(Login.this,"preencha senha",Toast.LENGTH_LONG).show();

                    }
                }else { Toast.makeText(Login.this,"preencha email",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void loginusuario(final DatasetUsuario usuario){
       // progressBar.setVisibility(View.VISIBLE);
        //String textoemail = edtemail.getText().toString();
        //String textosenha =edtsenha.getText().toString();
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(usuario.getEmail(),usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    //progressBar.setVisibility(View.GONE);
                    Toast.makeText(Login.this,"logado com sucesso",Toast.LENGTH_LONG).show();
                    //startActivity(new Intent(getApplicationContext(),TelaPrincincipal.class));

                    Intent intent = new Intent(Login.this,TelaPrincincipal.class);
                    intent.putExtra("idusuario1",autenticacao.getUid());

                    startActivity(intent);
                    finish();
                }
                else {
                   // progressBar.setVisibility(View.GONE);
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
                    Toast.makeText(Login.this,"Erro"+erroExcecao,Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}