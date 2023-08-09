package com.example.ssloc.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.ssloc.R;
import com.example.ssloc.databinding.ActivityEntregaVeiculoBinding;
import com.squareup.picasso.Picasso;

public class EntregaVeiculoActivity extends AppCompatActivity {

    private ActivityEntregaVeiculoBinding vb;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vb = ActivityEntregaVeiculoBinding.inflate(getLayoutInflater());
        setContentView(vb.getRoot());

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().setTitle("Endereço de entrega");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Picasso.get().load(R.raw.logoparada).into(vb.logo);
        Picasso.get().load(R.raw.atencaoentrega).into(vb.atencao);

        vb.btnzap.setOnClickListener( zapView -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send/?phone=5596991466663&text&type=phone_number&app_absent=0")));
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if ( item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}