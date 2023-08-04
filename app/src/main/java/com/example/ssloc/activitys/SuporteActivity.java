package com.example.ssloc.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

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

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Picasso.get().load(R.raw.logoparada).into(vb.logo);
        Picasso.get().load(R.raw.duvidas).into(vb.duvidas);
        Picasso.get().load(R.raw.qrcodesuporte).into(vb.qrCode);

        vb.zapBtn.setOnClickListener( zapView -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send/?phone=559691017925&text&type=phone_number&app_absent=0")));
        });
    }
}