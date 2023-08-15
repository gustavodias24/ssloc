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
import com.example.ssloc.databinding.ActivityContratoNovoBinding;
import com.squareup.picasso.Picasso;

public class ContratoNovoActivity extends AppCompatActivity {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private ActivityContratoNovoBinding vb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vb = ActivityContratoNovoBinding.inflate(getLayoutInflater());
        setContentView(vb.getRoot());

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        preferences = getSharedPreferences("UsuarioPreferences", Context.MODE_PRIVATE);
        editor = preferences.edit();

        vb.proseguirBtn.setOnClickListener(
                view -> {
                    if (vb.radio.isChecked()) {
                        editor.putBoolean("contrato", true);
                        editor.apply();
                        Toast.makeText(this, "Termos aceitos.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Aceite os termos!", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    @SuppressLint("ResourceType")
    @Override
    protected void onResume() {
        super.onResume();
        Picasso.get().load(R.raw.logoparada).into(vb.luego);
        Picasso.get().load(R.raw.atencaocontrato).into(vb.atentioncontrato);
        Picasso.get().load("https://scontent.fimp2-2.fna.fbcdn.net/v/t39.30808-6/366975209_1316698615908043_1451763021603741448_n.jpg?_nc_cat=104&cb=99be929b-59f725be&ccb=1-7&_nc_sid=7f8c78&_nc_ohc=dAeZ5xc--XUAX_csG76&_nc_ht=scontent.fimp2-2.fna&oh=00_AfC3in37TqtOvVIbeL0LDswqgkl_VOgr-c5ff_ayzn5eVw&oe=64DCBFB2").into(vb.contraite);
        getSupportActionBar().setTitle("Contrato");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if ( item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}