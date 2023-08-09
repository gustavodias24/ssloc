package com.example.ssloc.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ssloc.R;
import com.example.ssloc.databinding.ActivityContratoBinding;
import com.squareup.picasso.Picasso;

public class ContratoActivity extends AppCompatActivity {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private ActivityContratoBinding vb;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vb = ActivityContratoBinding.inflate(getLayoutInflater());
        setContentView(vb.getRoot());

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        preferences = getSharedPreferences("UsuarioPreferences", Context.MODE_PRIVATE);
        editor = preferences.edit();
        getSupportActionBar().setTitle("Contrato");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Picasso.get().load(R.raw.logoparada).into(vb.logo);
        Picasso.get().load(R.raw.atencaocontrato).into(vb.atencaoContrato);
        Picasso.get().load(R.raw.contrato).into(vb.contrato);

        vb.proseguirBtn.setOnClickListener( prosseguirView -> {
            if( vb.radioButton2.isChecked() ){
                editor.putBoolean("contrato", true);
                editor.apply();
                Toast.makeText(this, "Termos aceitos.", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(this, "Aceite os termos!", Toast.LENGTH_SHORT).show();
            }
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
