package com.example.ssloc.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.example.ssloc.R;
import com.example.ssloc.databinding.ActivityMenuBinding;
import com.squareup.picasso.Picasso;

public class MenuActivity extends AppCompatActivity {

    private ActivityMenuBinding vb;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vb = ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(vb.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        Picasso.get().load(R.raw.logoparada).into(vb.imageView2);


        vb.suporteBtn.setOnClickListener( suporteView -> {
            startActivity(new Intent(getApplicationContext(), SuporteActivity.class));
        });
    }
}