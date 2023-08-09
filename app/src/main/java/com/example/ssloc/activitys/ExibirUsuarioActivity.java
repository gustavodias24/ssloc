package com.example.ssloc.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.UserManager;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.ssloc.R;
import com.example.ssloc.databinding.ActivityExibirUsuarioBinding;
import com.example.ssloc.databinding.ExibirImagesLayoutBinding;
import com.example.ssloc.databinding.LayoutCarregandoBinding;
import com.example.ssloc.models.MsgModel;
import com.example.ssloc.models.UsuarioCompletoModel;
import com.example.ssloc.services.ServiceApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ExibirUsuarioActivity extends AppCompatActivity {
    private Retrofit retrofitCadastro;
    private ServiceApi serviceApi;
    private Dialog dialog_carregando;
    private ActivityExibirUsuarioBinding vb;
    private String endereco, dados, fotochn,fotoComprovante, login;

    private UsuarioCompletoModel usuarioCompleto;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vb = ActivityExibirUsuarioBinding.inflate(getLayoutInflater());
        setContentView(vb.getRoot());

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        criarRetrofitCadastro();
        criarAlertCarregando();

        Intent intent = getIntent();

        endereco = intent.getStringExtra("endereco");
        dados = intent.getStringExtra("dados");
        fotochn = intent.getStringExtra("fotocnh");
        fotoComprovante = intent.getStringExtra("fotocomprovante");
        login = intent.getStringExtra("login");


        getSupportActionBar().setTitle(login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        pegarDadosUsuario();

    }

    private void alertImage(String imageBase64){
        byte[] decodedBytes = Base64.decode(imageBase64, Base64.DEFAULT);
        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        // setImageBitMap
        AlertDialog.Builder b = new AlertDialog.Builder(ExibirUsuarioActivity.this);
        b.setTitle("Imagem.");
        ExibirImagesLayoutBinding imagesLayoutBinding = ExibirImagesLayoutBinding.inflate(getLayoutInflater());
        imagesLayoutBinding.imageView3.setImageBitmap(decodedBitmap);
        b.setView(imagesLayoutBinding.getRoot());
        b.setPositiveButton("fechar", null);
        b.create().show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if ( item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void criarAlertCarregando(){
        android.app.AlertDialog.Builder b = new android.app.AlertDialog.Builder(ExibirUsuarioActivity.this);
        b.setView(
                LayoutCarregandoBinding.inflate(getLayoutInflater()).getRoot()
        );
        b.setCancelable(false);
        dialog_carregando = b.create();
    }

    private void criarRetrofitCadastro(){
        retrofitCadastro = new Retrofit
                .Builder()
                .baseUrl("https://apissloc.vercel.app/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        serviceApi = retrofitCadastro.create(ServiceApi.class);
    }

    private void pegarDadosUsuario(){
        dialog_carregando.show();
        serviceApi.umUsuarioCompleto(login).enqueue(new Callback<List<UsuarioCompletoModel>>() {
            @Override
            public void onResponse(Call<List<UsuarioCompletoModel>> call, Response<List<UsuarioCompletoModel>> response) {

                if ( response.isSuccessful() ){
                    assert response.body() != null;
                    usuarioCompleto = response.body().get(0);
                    configuracoesUsuario(usuarioCompleto);

                    if ( usuarioCompleto.getTemmanu() ){
                            configurarLayoutManu();
                        }
                    if ( usuarioCompleto.getTemplano()  ){
                        configurarEssaBoxsta();
                        vb.layoutPlano.setVisibility(View.VISIBLE);
                        vb.planoText.setText( "Ativo: " +usuarioCompleto.getPlano().getAtivo()
                                + " Data: " + usuarioCompleto.plano.getData());
                        vb.verComprovantePlano.setOnClickListener(comprovantePlanoView -> {
                            alertImage(usuarioCompleto.getPlano().getComprovantePagamento());
                        });

                        vb.ativarPlano.setOnClickListener( ativarPlanoView -> {
                            dialog_carregando.show();
                            serviceApi.ativarPlano(usuarioCompleto.getPlano()).enqueue(new Callback<MsgModel>() {
                                @Override
                                public void onResponse(Call<MsgModel> call, Response<MsgModel> response) {
                                    if ( response.isSuccessful() ){
                                        vb.planoText.setText( "Ativo: true"
                                                + " Data: " + usuarioCompleto.plano.getData());
                                        Toast.makeText(ExibirUsuarioActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(ExibirUsuarioActivity.this, "Problema de conexão, tente novamente.", Toast.LENGTH_SHORT).show();
                                    }
                                    dialog_carregando.dismiss();
                                }

                                @Override
                                public void onFailure(Call<MsgModel> call, Throwable t) {

                                }
                            });
                        });
                    }
                }else{
                    Toast.makeText(ExibirUsuarioActivity.this, "Problema de conexão, tente novamente.", Toast.LENGTH_SHORT).show();
                    dialog_carregando.dismiss();
                }
                dialog_carregando.dismiss();
            }

            @Override
            public void onFailure(Call<List<UsuarioCompletoModel>> call, Throwable t) {

            }
        });
    }

    public void configurarEssaBoxsta(){
        vb.entrarEmContato1.setOnClickListener( zap -> {
            startActivity(
                    new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(
                                    "https://api.whatsapp.com/send/?phone="+ usuarioCompleto.getUsuarioModel().getTelefone() +"&text&type=phone_number&app_absent=0")));
        });
        vb.entrarEmContato2.setOnClickListener( zap2 -> {
            startActivity(
                    new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(
                                    "https://api.whatsapp.com/send/?phone="+ usuarioCompleto.getUsuarioModel().getTelefone() +"&text&type=phone_number&app_absent=0")));
        });
    }

    public void configurarLayoutManu(){
        vb.layoutManu.setVisibility(View.VISIBLE);

        String msgStatus = "";
        switch (usuarioCompleto.getManu().getStatus()){
            case 1:
                msgStatus = "Esperando análise";
                break;
            default:
                msgStatus = "Análise aprovada!";

        }
        vb.manuText.setText("STATUS: "+ msgStatus + " Data: " + usuarioCompleto.getManu().getData());

        vb.verRecibo.setOnClickListener( reciboview -> {
            alertImage(usuarioCompleto.getManu().getImageRecibo());
        });
        vb.verTroca.setOnClickListener( trocaview -> {
            alertImage(usuarioCompleto.getManu().getImageTroca());
        });

        vb.aprovarManu.setOnClickListener( aprovarView -> {
            dialog_carregando.show();
            serviceApi.aprovarManu( usuarioCompleto.getManu() ).enqueue(new Callback<MsgModel>() {
                @Override
                public void onResponse(Call<MsgModel> call, Response<MsgModel> response) {
                    if ( response.isSuccessful() ){
                        vb.manuText.setText("STATUS: Análise aprovada!" + " Data: " + usuarioCompleto.getManu().getData());
                        Toast.makeText(ExibirUsuarioActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(ExibirUsuarioActivity.this, "Problema de conexão, tente novamente.", Toast.LENGTH_SHORT).show();
                    }

                    dialog_carregando.dismiss();
                }

                @Override
                public void onFailure(Call<MsgModel> call, Throwable t) {

                }
            });
        });

    }

    private void configuracoesUsuario(UsuarioCompletoModel usuarioCompletoModel){
        vb.dadosGeraisText.setText(
                usuarioCompletoModel.getUsuarioModel().getCep().toString()
                        +  "\n" + usuarioCompletoModel.getUsuarioModel().toString()
        );

        vb.verCNH.setOnClickListener( verCNHView ->{
            alertImage(usuarioCompleto.getUsuarioModel().getFotoCNH());
        });
        vb.verComprovante.setOnClickListener( verComprovanteView ->{
            alertImage(usuarioCompleto.getUsuarioModel().getFotoComprovante());
        });
    }
}