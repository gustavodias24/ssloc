package com.example.ssloc.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ssloc.R;
import com.example.ssloc.databinding.ActivityVeiculoBinding;
import com.example.ssloc.databinding.LayoutCarregandoBinding;
import com.example.ssloc.models.MsgModel;
import com.example.ssloc.models.VeiculoModel;
import com.example.ssloc.services.ServiceApi;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VeiculoActivity extends AppCompatActivity {
    int countMoto = 0;
    private Retrofit retrofit;
    private ServiceApi serviceApi;
    private ActivityVeiculoBinding binding;
    private Dialog dialog_carregando;

    private int countPlan = 0;
    private List<Integer> planos = new ArrayList<>();

    private VeiculoModel veiculoAtt = new VeiculoModel();

    private List<VeiculoModel> lista = new ArrayList<>();
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVeiculoBinding.inflate(getLayoutInflater());

        getSupportActionBar().setTitle("Veículos");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setContentView(binding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Picasso.get().load(R.raw.logoparada).into(binding.logo);
        criarRetrofit();
        criarAlertCarregando();
        listarVeiculos();
        configurarPlanos();


        binding.concluirPlano.setOnClickListener( concluirView -> {
            indisponibilizarMoto();
        });

    }
    public void criarRetrofit(){
        retrofit = new Retrofit
                .Builder()
                .baseUrl("https://apissloc.vercel.app/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        serviceApi = retrofit.create(ServiceApi.class);
    }

    private void criarAlertCarregando(){
        AlertDialog.Builder b = new AlertDialog.Builder(VeiculoActivity.this);
        b.setView(
                LayoutCarregandoBinding.inflate(getLayoutInflater()).getRoot()
        );
        b.setCancelable(false);
        dialog_carregando = b.create();
    }

    private void listarVeiculos(){
        dialog_carregando.show();
        serviceApi.pegarveiculosdisponiveis(true).enqueue(new Callback<List<VeiculoModel>>() {
            @Override
            public void onResponse(Call<List<VeiculoModel>> call, Response<List<VeiculoModel>> response) {
                if ( response.isSuccessful()){
                    lista.addAll(response.body());
                    if ( lista.size() == 0){
                        Toast.makeText(VeiculoActivity.this, "Não há veículos disponíveis", Toast.LENGTH_LONG).show();
                        finish();
                    }else{
                        configurarVeiculos();
                        dialog_carregando.dismiss();
                    }
                }else{
                    dialog_carregando.dismiss();
                    Toast.makeText(VeiculoActivity.this, "Erro de conexão, tente mais tarde!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<VeiculoModel>> call, Throwable t) {
                Toast.makeText(VeiculoActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                dialog_carregando.dismiss();
            }
        });
    }

    public void configurarVeiculos(){
        int limite = lista.size() - 1;

        VeiculoModel veiculoModel = lista.get(limite);
        byte[] decodedBytes = Base64.decode(veiculoModel.getFoto(), Base64.DEFAULT);
        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        binding.motoImagem.setImageBitmap(decodedBitmap);
        binding.descriEdispo.setText(veiculoModel.getDescricao() + "(DISPONÍVEL)");

        veiculoAtt.set_id(lista.get(countMoto).get_id());
        veiculoAtt.setDescricao(lista.get(countMoto).getDescricao());
        veiculoAtt.setFoto(lista.get(countMoto).getFoto());
        veiculoAtt.setDisponivel(false);

        binding.proxMoto.setOnClickListener( proxView -> {
            if (countMoto == limite){
                countMoto = 0;
            }else{
                countMoto ++;
            }
            VeiculoModel altenarVeiculo = lista.get(countMoto);
            byte[] decodedBytes2 = Base64.decode(altenarVeiculo.getFoto(), Base64.DEFAULT);
            Bitmap decodedBitmap2 = BitmapFactory.decodeByteArray(decodedBytes2, 0, decodedBytes2.length);

            veiculoAtt.set_id(lista.get(countMoto).get_id());
            veiculoAtt.setDescricao(lista.get(countMoto).getDescricao());
            veiculoAtt.setFoto(lista.get(countMoto).getFoto());
            veiculoAtt.setDisponivel(false);

            binding.motoImagem.setImageBitmap(decodedBitmap2);
            binding.descriEdispo.setText(altenarVeiculo.getDescricao() + "(DISPONÍVEL)");

        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if ( item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void configurarPlanos(){

        planos.add(R.raw.plano1);
        planos.add(R.raw.plano2);
        planos.add(R.raw.plano3);

        binding.planoImage.setImageResource(planos.get(countPlan));

        binding.proxPlan.setOnClickListener( planoView -> {
            if ( countPlan == 2){
                countPlan = 0;
            }else{
                countPlan++;
            }
            binding.planoImage.setImageResource(planos.get(countPlan));
        });
    }

    public void indisponibilizarMoto(){
        dialog_carregando.show();
        serviceApi.atualizarveiculos(veiculoAtt).enqueue(new Callback<MsgModel>() {
            @Override
            public void onResponse(Call<MsgModel> call, Response<MsgModel> response) {
                if ( response.isSuccessful()){
                    Toast.makeText(VeiculoActivity.this, "Aceite o contrato.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), ContratoNovoActivity.class));
                    finish();
                }else{
                    dialog_carregando.dismiss();
                    Toast.makeText(VeiculoActivity.this, "Erro de conexão, tente mais tarde!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MsgModel> call, Throwable t) {

            }
        });
    }
}