package com.example.ssloc;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;

import com.example.ssloc.databinding.ActivityLoginOuCadastroBinding;
import com.squareup.picasso.Picasso;

public class LoginOuCadastroActivity extends AppCompatActivity {
    private ActionBar bar;
    private ActivityLoginOuCadastroBinding vb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vb = ActivityLoginOuCadastroBinding.inflate(getLayoutInflater());
        setContentView(vb.getRoot());

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        bar = getSupportActionBar();
        bar.setTitle("Credenciamento");
        Picasso.get().load(R.raw.logoparada).into(vb.imageView);
    }
}