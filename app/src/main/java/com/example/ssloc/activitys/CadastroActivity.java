package com.example.ssloc.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.ssloc.databinding.ActivityCadastroBinding;
import com.example.ssloc.databinding.LayoutCarregandoBinding;
import com.example.ssloc.models.CepModel;
import com.example.ssloc.services.ServiceCep;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CadastroActivity extends AppCompatActivity {
    private Dialog dialog_carregando;
    private ActivityCadastroBinding vb;
    private Retrofit retrofitCep;
    private ServiceCep serviceCep;
    private ActionBar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vb = ActivityCadastroBinding.inflate(getLayoutInflater());
        setContentView(vb.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        bar = getSupportActionBar();
        bar.setTitle("Cadastro");
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setDisplayShowHomeEnabled(true);

        criarRetrofitCep();
        criarAlertCarregando();

        vb.buscarEnderecoBtn.setOnClickListener( buscarEnd -> {
            String cepString = vb.cepField.getEditText().getText().toString().trim();

            if ( !cepString.isEmpty() ){
                vb.cepField.setError(null);
                puxarEndereco(cepString);
            }else{
                vb.cepField.setError(
                        "Preencha o campo cep!"
                );
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if ( item.getItemId() == android.R.id.home){
            startActivity(new Intent(getApplicationContext(), LoginOuCadastroActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void criarRetrofitCep(){
        retrofitCep = new Retrofit.Builder()
                .baseUrl("https://viacep.com.br/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        serviceCep = retrofitCep.create(ServiceCep.class);
    }

    private void puxarEndereco(String cepString){
        dialog_carregando.show();
        serviceCep.buscar_cep(cepString).enqueue(new Callback<CepModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<CepModel> call, Response<CepModel> response) {
                if (response.isSuccessful()){
                    dialog_carregando.dismiss();
                    CepModel cepModel = response.body();
                    vb.enderecoText.setText(
                            "Logradouro: " + cepModel.getLogradouro() + "\n" +
                                    "complemento: " + cepModel.getComplemento() + "\n" +
                                    "bairro: " + cepModel.getBairro() + "\n" +
                                    "localidade: " + cepModel.getLocalidade() + "\n"
                    );
                }else{
                    dialog_carregando.dismiss();
                    vb.cepField.setError(
                            "Cep inválido!"
                    );
                }
            }

            @Override
            public void onFailure(Call<CepModel> call, Throwable t) {

            }
        });
    }

    private void criarAlertCarregando(){
        AlertDialog.Builder b = new AlertDialog.Builder(CadastroActivity.this);
        b.setView(
                LayoutCarregandoBinding.inflate(getLayoutInflater()).getRoot()
        );
        b.setCancelable(false);
        dialog_carregando = b.create();
    }
}