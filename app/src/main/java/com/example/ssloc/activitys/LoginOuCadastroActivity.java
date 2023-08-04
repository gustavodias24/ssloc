package com.example.ssloc.activitys;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.ssloc.R;
import com.example.ssloc.databinding.ActivityLoginOuCadastroBinding;
import com.example.ssloc.databinding.LayoutCarregandoBinding;
import com.example.ssloc.models.MsgModel;
import com.example.ssloc.models.UsuarioModel;
import com.example.ssloc.services.ServiceApi;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginOuCadastroActivity extends AppCompatActivity {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Dialog dialog_carregando;
    private Retrofit retrofitCadastro;
    private ServiceApi serviceApi;
    private ActionBar bar;
    private ActivityLoginOuCadastroBinding vb;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vb = ActivityLoginOuCadastroBinding.inflate(getLayoutInflater());
        setContentView(vb.getRoot());

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        preferences = getSharedPreferences("UsuarioPreferences", Context.MODE_PRIVATE);
        editor = preferences.edit();

        criarRetrofitCadastro();
        criarAlertCarregando();

        bar = getSupportActionBar();
        assert bar != null;
        bar.setTitle("Credenciamento");
        Picasso.get().load(R.raw.logoparada).into(vb.imageView);

        vb.criarBtn.setOnClickListener( viewCriar -> {
            startActivity(new Intent(getApplicationContext(), CadastroActivity.class));
            finish();
        });

        vb.entrarBtn.setOnClickListener( viewEntrar -> {
            String senha,login;

            String msgError = "Esse campo é obrigatório!";

            clearFields();

            senha = vb.passField.getEditText().getText().toString().trim();
            login = vb.loginField.getEditText().getText().toString().trim();

            if ( !login.isEmpty() ){
                if ( !senha.isEmpty() ){
                    fazerLogin(login, senha);
                }else{
                    vb.passField.setError(msgError);
                }
            }else{
                vb.loginField.setError(msgError);
            }
        });
    }

    private void clearFields(){
        vb.passField.setError(null);
        vb.loginField.setError(null);
    }
    private void fazerLogin(String login, String senha){

        dialog_carregando.show();

        UsuarioModel usuarioModel = new UsuarioModel();

        usuarioModel.setSenha(senha);
        usuarioModel.setLogin(login);

        serviceApi.fazerLogin(usuarioModel).enqueue(new Callback<MsgModel>() {
            @Override
            public void onResponse(Call<MsgModel> call, Response<MsgModel> response) {

                MsgModel msgModel = response.body();

                if ( response.isSuccessful() ){
                    dialog_carregando.dismiss();
                    if ( !msgModel.getError() ){
                        editor.putString("DadosUsuario", login);
                        editor.apply();
                        startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                        Toast.makeText(LoginOuCadastroActivity.this, msgModel.getMsg(), Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(LoginOuCadastroActivity.this, msgModel.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    dialog_carregando.dismiss();
                    Toast.makeText(LoginOuCadastroActivity.this, "Erro de conexão, tente mais tarde.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MsgModel> call, Throwable t) {

            }
        });
    }
    private void criarRetrofitCadastro(){
        retrofitCadastro = new Retrofit
                .Builder()
                .baseUrl("https://apissloc.vercel.app/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        serviceApi = retrofitCadastro.create(ServiceApi.class);
    }

    private void criarAlertCarregando(){
        AlertDialog.Builder b = new AlertDialog.Builder(LoginOuCadastroActivity.this);
        b.setView(
                LayoutCarregandoBinding.inflate(getLayoutInflater()).getRoot()
        );
        b.setCancelable(false);
        dialog_carregando = b.create();
    }
}