package com.example.ssloc.activitys;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.ssloc.R;
import com.example.ssloc.databinding.ActivityLoginOuCadastroBinding;
import com.squareup.picasso.Picasso;

public class LoginOuCadastroActivity extends AppCompatActivity {
    private ActionBar bar;
    private ActivityLoginOuCadastroBinding vb;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vb = ActivityLoginOuCadastroBinding.inflate(getLayoutInflater());
        setContentView(vb.getRoot());

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        bar = getSupportActionBar();
        bar.setTitle("Credenciamento");
        Picasso.get().load(R.raw.logoparada).into(vb.imageView);

        vb.criarBtn.setOnClickListener( viewCriar -> {
            startActivity(new Intent(getApplicationContext(), CadastroActivity.class));
            finish();
        });
    }
}