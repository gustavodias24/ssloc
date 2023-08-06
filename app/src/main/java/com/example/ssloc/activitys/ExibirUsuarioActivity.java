package com.example.ssloc.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;

import com.example.ssloc.R;
import com.example.ssloc.databinding.ActivityExibirUsuarioBinding;
import com.example.ssloc.databinding.ExibirImagesLayoutBinding;
import com.example.ssloc.models.UsuarioCompletoModel;

public class ExibirUsuarioActivity extends AppCompatActivity {

    private ActivityExibirUsuarioBinding vb;
    private String endereco, dados, fotochn,fotoComprovante;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vb = ActivityExibirUsuarioBinding.inflate(getLayoutInflater());
        setContentView(vb.getRoot());

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Intent intent = getIntent();

        endereco = intent.getStringExtra("endereco");
        dados = intent.getStringExtra("dados");
        fotochn = intent.getStringExtra("fotocnh");
        fotoComprovante = intent.getStringExtra("fotocomprovante");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        vb.dadosGeraisText.setText(
                endereco +  "\n" + dados
                );

        vb.verCNH.setOnClickListener( verCNHView ->{
            alertImage(fotochn);
        });
        vb.verComprovante.setOnClickListener( verComprovanteView ->{
            alertImage(fotoComprovante);
        });
    }

    private void alertImage(String imageBase64){
        byte[] decodedBytes = Base64.decode(imageBase64, Base64.DEFAULT);
        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        // setImageBitMap
        AlertDialog.Builder b = new AlertDialog.Builder(ExibirUsuarioActivity.this);
        b.setTitle("Imagem.");
        ExibirImagesLayoutBinding imagesLayoutBinding = ExibirImagesLayoutBinding.inflate(getLayoutInflater());
        imagesLayoutBinding.imageView3.setImageBitmap(decodedBitmap);
        b.setView(imagesLayoutBinding.getRoot());
        b.setPositiveButton("fechar", null);
        b.create().show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if ( item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}