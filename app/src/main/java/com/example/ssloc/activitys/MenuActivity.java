package com.example.ssloc.activitys;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.example.ssloc.R;
import com.example.ssloc.databinding.ActivityEncerrarContratoBinding;
import com.example.ssloc.databinding.ActivityLoginOuCadastroBinding;
import com.example.ssloc.databinding.ActivityMenuBinding;
import com.example.ssloc.databinding.ContratoPlanoBinding;
import com.squareup.picasso.Picasso;

public class MenuActivity extends AppCompatActivity {

    private ActivityMenuBinding vb;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private Dialog dialogContrato;
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
            if (preferences.getBoolean("contrato", false)){
                startActivity(new Intent(getApplicationContext(), MeusPlanosActivity.class));
                
            }else{
                Toast.makeText(this, "Aceite o contrato primeiro!", Toast.LENGTH_SHORT).show();
            }
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

        vb.contratoBtn.setOnClickListener( contratoView ->{
            if ( !preferences.getBoolean("contrato", false)){
                startActivity(new Intent(getApplicationContext(), ContratoNovoActivity.class));
            }else{
                Toast.makeText(this, "Você já aceitou o contrato!", Toast.LENGTH_SHORT).show();
            }
        });

        vb.entregaBtn.setOnClickListener( entregaView ->{
            startActivity(new Intent(getApplicationContext(), EntregaVeiculoActivity.class));
        });

        vb.veiculosBtn.setOnClickListener( veiculoView -> {
            startActivity(new Intent(getApplicationContext(), VeiculoActivity.class));
        });
    }


}