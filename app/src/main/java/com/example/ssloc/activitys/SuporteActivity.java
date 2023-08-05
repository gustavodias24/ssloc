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
import com.example.ssloc.databinding.ActivitySuporteBinding;
import com.squareup.picasso.Picasso;

public class SuporteActivity extends AppCompatActivity {

    private ActivitySuporteBinding vb;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vb = ActivitySuporteBinding.inflate(getLayoutInflater());
        setContentView(vb.getRoot());

        getSupportActionBar().setTitle("Suporte");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Picasso.get().load(R.raw.logoparada).into(vb.logo);
        Picasso.get().load(R.raw.duvidas).into(vb.duvidas);
        Picasso.get().load(R.raw.qrcodesuporte).into(vb.qrCode);

        vb.zapBtn.setOnClickListener( zapView -> {
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