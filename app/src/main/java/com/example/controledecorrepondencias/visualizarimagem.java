package com.example.controledecorrepondencias;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

public class visualizarimagem extends AppCompatActivity {
 private ImageView imageView22;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizarimagem);
        //getSupportActionBar().hide();
        getSupportActionBar().setTitle("Imagem assinatura");
        getSupportActionBar().setDisplayUseLogoEnabled(true);
       getSupportActionBar().setHomeButtonEnabled(true);





       // getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //getWindow().setStatusBarColor(Color.TRANSPARENT);
        imageView22=findViewById(R.id.pv_image);
        try {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null){
                try {
                    byte[] imageInByte = bundle.getByteArray("imagem");
                    Bitmap bmp = BitmapFactory.decodeByteArray(imageInByte,0,imageInByte.length);
                    //imgs = findViewById(R.id.imageView22);
                    imageView22.setImageBitmap(bmp);
                }catch (Exception e){}
            }
        }catch (Exception e){
            Toast.makeText(this,""+e,Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_voltar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_voltar:

               // startActivity(new Intent(getApplicationContext(),Telalogin.class));
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);

    }
}