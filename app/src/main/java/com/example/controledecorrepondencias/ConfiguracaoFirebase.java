package com.example.controledecorrepondencias;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfiguracaoFirebase {

    private static DatabaseReference referenciaFirebase;
    private static FirebaseAuth referencaAutenticacao;
    public static FirebaseAuth getFirebaseAutenticacao(){
        if(referencaAutenticacao==null){
            referencaAutenticacao=FirebaseAuth.getInstance();
        }
        return  referencaAutenticacao;
    }
    public static DatabaseReference getFirebase(){
      if(referenciaFirebase==null){
          referenciaFirebase =FirebaseDatabase.getInstance().getReference();
      }
      return referenciaFirebase;
    }
}
