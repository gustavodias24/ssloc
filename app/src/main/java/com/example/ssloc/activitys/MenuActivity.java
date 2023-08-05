package com.example.ssloc.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.ssloc.R;
import com.example.ssloc.databinding.ActivityLoginOuCadastroBinding;
import com.example.ssloc.databinding.ActivityMenuBinding;
import com.squareup.picasso.Picasso;

public class MenuActivity extends AppCompatActivity {

    private ActivityMenuBinding vb;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vb = ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(vb.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        preferences = getSharedPreferences("UsuarioPreferences", Context.MODE_PRIVATE);
        editor = preferences.edit();

        Picasso.get().load(R.raw.logoparada).into(vb.imageView2);

        vb.meusPlanosBtn.setOnClickListener( meuPlanoView -> {
            startActivity(new Intent(getApplicationContext(), MeusPlanosActivity.class));
        });
        vb.manuntencaoBtn.setOnClickListener( manuView -> {
            startActivity(new Intent(getApplicationContext(), ManuntecaoActivity.class));
        });
        vb.encerrarContratoBtn.setOnClickListener( encerrarView -> {
            startActivity(new Intent(getApplicationContext(), EncerrarContratoActivity.class));
        });
        vb.suporteBtn.setOnClickListener( suporteView -> {
            startActivity(new Intent(getApplicationContext(), SuporteActivity.class));
        });
        vb.sairBtn.setOnClickListener( sairView -> {
            startActivity(new Intent(getApplicationContext(), LoginOuCadastroActivity.class));
            editor.putString("DadosUsuario", null);
            editor.apply();
            finish();
        });
    }
}