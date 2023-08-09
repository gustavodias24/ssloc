package com.example.ssloc.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ssloc.R;
import com.example.ssloc.databinding.ActivityEncerrarContratoBinding;
import com.example.ssloc.databinding.LayoutCarregandoBinding;
import com.example.ssloc.models.MsgModel;
import com.example.ssloc.models.PlanoModel;
import com.example.ssloc.models.UsuarioModel;
import com.example.ssloc.services.ServiceApi;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EncerrarContratoActivity extends AppCompatActivity {

    private SharedPreferences preferences;
    private Dialog dialog_carregando;
    private Retrofit retrofitCadastro;
    private ServiceApi serviceApi;
    private Dialog dialog_encerrado;
    private ActivityEncerrarContratoBinding vb;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vb = ActivityEncerrarContratoBinding.inflate(getLayoutInflater());
        setContentView(vb.getRoot());

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        criarRetrofitCadastro();

        preferences = getSharedPreferences("UsuarioPreferences", Context.MODE_PRIVATE);

        getSupportActionBar().setTitle("Encerrar contrato");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Picasso.get().load(R.raw.logoparada).into(vb.logo);
        Picasso.get().load(R.raw.termoencerracao).into(vb.encerramento);
        Picasso.get().load(R.raw.recisaocontrato).into(vb.encerramentocontrato);

        criar_dialog_encerrado();
        criarAlertCarregando();

        verificarPlano();

        vb.encerrarBtn.setOnClickListener( encerrarView -> {
            SignaturePad signaturePad = vb.assinaturaPad;
            Bitmap signatureBitmap = signaturePad.getSignatureBitmap();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            signatureBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT); // enviar sapoha pro back

            if ( vb.checkTermos.isChecked() ){
                dialog_carregando.show();
                serviceApi.apagarPlano(new PlanoModel( preferences.getString("DadosUsuario", ""), 1)).enqueue(new Callback<MsgModel>() {
                    @Override
                    public void onResponse(Call<MsgModel> call, Response<MsgModel> response) {
                        MsgModel msgModel = response.body();
                        if ( response.isSuccessful() ){
                            dialog_carregando.dismiss();
                            dialog_encerrado.show();
                            vb.encerrarBtn.setText("Encerrado");
                            vb.encerrarBtn.setTextColor(Color.parseColor("#FEFE01"));
                        }else{
                            Toast.makeText(EncerrarContratoActivity.this, "Erro de conexão, tente mais tarde.", Toast.LENGTH_SHORT).show();
                            dialog_carregando.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<MsgModel> call, Throwable t) {

                    }
                });

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

    public void criar_dialog_encerrado(){
        AlertDialog.Builder b = new AlertDialog.Builder(EncerrarContratoActivity.this);
        b.setTitle("Importante");
        b.setMessage("SEU CONTRATO DE ALUGUEL COM A SSLOC\n" +
                "FOI ENCERRADO DIRIJA-SE A UNIDADE\n" +
                "PARA VISTORIA E ENTREGA DO VEÍCULO. ");
        b.setPositiveButton("ok", (dialogInterface, i) -> finish());
        b.setCancelable(false);
        dialog_encerrado = b.create();
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
        android.app.AlertDialog.Builder b = new android.app.AlertDialog.Builder(EncerrarContratoActivity.this);
        b.setView(
                LayoutCarregandoBinding.inflate(getLayoutInflater()).getRoot()
        );
        b.setCancelable(false);
        dialog_carregando = b.create();
    }

    private void verificarPlano(){
        dialog_carregando.show();
        serviceApi.temPlanos(new UsuarioModel(preferences.getString("DadosUsuario", ""))).enqueue(new Callback<MsgModel>() {
            @Override
            public void onResponse(Call<MsgModel> call, Response<MsgModel> response) {
                MsgModel msgModel = response.body();
                if ( response.isSuccessful() ){
                    if ( !msgModel.getError() ){
                        dialog_carregando.dismiss();
                    }else{
                        dialog_carregando.dismiss();
                        android.app.AlertDialog.Builder b = new android.app.AlertDialog.Builder(EncerrarContratoActivity.this);
                        b.setTitle("Atenção");
                        b.setMessage("Você não tem nenhum plano ativo, faça um plano para acessar aqui!");
                        b.setPositiveButton("ok", (dialogInterface, i) -> finish());
                        b.setCancelable(false);
                        b.show();
                        Toast.makeText(EncerrarContratoActivity.this, msgModel.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(EncerrarContratoActivity.this, "Erro de conexão, tente mais tarde.", Toast.LENGTH_SHORT).show();
                    dialog_carregando.dismiss();
                }
            }

            @Override
            public void onFailure(Call<MsgModel> call, Throwable t) {

            }
        });
    }
}