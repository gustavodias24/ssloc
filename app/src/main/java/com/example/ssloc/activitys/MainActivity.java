package com.example.ssloc.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.example.ssloc.R;
import com.example.ssloc.activitys.LoginOuCadastroActivity;

public class MainActivity extends AppCompatActivity {

    private static final long SPLASH_DISPLAY_LENGTH = 3000;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = getSharedPreferences("UsuarioPreferences", Context.MODE_PRIVATE);

        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        new Handler().postDelayed(() -> {
            if( preferences.getString("DadosUsuario", null) != null){
                startActivity(new Intent(getApplicationContext(), MenuActivity.class));
            }else{
                startActivity(new Intent(getApplicationContext(), LoginOuCadastroActivity.class));
            }
            finish();
        }, SPLASH_DISPLAY_LENGTH);
    }
}