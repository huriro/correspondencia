package com.example.controledecorrepondencias;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FirebaseViewHolder extends RecyclerView.ViewHolder {
    public TextView apto,aptoleitura,tipo,codigo,destinatario,menu_option,menu_option2,dataleitura,leitura,consumo;
    ImageView imageView;
    public FirebaseViewHolder(@NonNull View itemView) {
        super(itemView);
        apto=itemView.findViewById(R.id.txwapto);
        tipo= itemView.findViewById(R.id.txwtipo);
        codigo= itemView.findViewById(R.id.txwcodigo);
        imageView=itemView.findViewById(R.id.imageView2);
        destinatario=itemView.findViewById(R.id.txwdestinatario);
        menu_option=itemView.findViewById(R.id.textViewOptions);
        menu_option2=itemView.findViewById(R.id.textViewOptionsreserva);
        dataleitura=itemView.findViewById(R.id.txtdataleitura);
        leitura=itemView.findViewById(R.id.txtleitura);
         consumo=itemView.findViewById(R.id.txtconsumo);
         aptoleitura=itemView.findViewById(R.id.txtaptoleitura);
    }
    public void setImage(int image) {
        imageView.setImageResource(image);
    }
}
