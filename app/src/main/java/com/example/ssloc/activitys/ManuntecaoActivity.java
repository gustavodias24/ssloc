package com.example.ssloc.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ssloc.R;
import com.example.ssloc.databinding.ActivityManuntecaoBinding;
import com.example.ssloc.databinding.LayoutCarregandoBinding;
import com.example.ssloc.models.ManuModel;
import com.example.ssloc.models.MsgModel;
import com.example.ssloc.models.UsuarioModel;
import com.example.ssloc.services.ServiceApi;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ManuntecaoActivity extends AppCompatActivity {
    
    private Uri imgTroca, imgRecibo = null;
    private Dialog dialog_carregando;
    private Retrofit retrofitCadastro;
    private ServiceApi serviceApi;
    private SharedPreferences preferences;
    private static final int REQUEST_TROCA_IMAGE_SELECT = 1;
    private static final int REQUEST_RECIBO_IMAGE_SELECT = 2;
    private ActivityManuntecaoBinding vb;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vb = ActivityManuntecaoBinding.inflate(getLayoutInflater());
        setContentView(vb.getRoot());

        preferences = getSharedPreferences("UsuarioPreferences", Context.MODE_PRIVATE);

        criarRetrofitCadastro();
        criarAlertCarregando();

        verificarPlano();
        verificarStatusManu();

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        getSupportActionBar().setTitle("Manutenção");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Picasso.get().load(R.raw.logoparada).into(vb.logo);
        Picasso.get().load(R.raw.atencaooleo).into(vb.atencao);
        Picasso.get().load(R.raw.dicaoleo).into(vb.dica);

        vb.trocaBtn.setOnClickListener(trocaView -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_TROCA_IMAGE_SELECT);
        });

        vb.reciboBtn.setOnClickListener( recibo -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_RECIBO_IMAGE_SELECT);
        });
        
        vb.sentBtn.setOnClickListener( sendView -> {
            if ( imgRecibo == null || imgTroca == null){
                Toast.makeText(this, "Você precisa enviar as duas imagens juntas!", Toast.LENGTH_SHORT).show();
            }else{
                criarManu();
            }
        });
    }

    private void criarManu(){
        // Obter a data atual
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String currentDate = sdf.format(calendar.getTime());

        serviceApi.criarManu(
                new ManuModel(
                        currentDate,
                        preferences.getString("DadosUsuario", ""),
                        1,
                        imgTroca,
                        imgRecibo
                )
        ).enqueue(new Callback<MsgModel>() {
            @Override
            public void onResponse(Call<MsgModel> call, Response<MsgModel> response) {
                MsgModel msgModel = response.body();
                if ( response.isSuccessful() ){
                    Toast.makeText(ManuntecaoActivity.this, msgModel.getMsg(), Toast.LENGTH_SHORT).show();
                    verificarStatusManu();
                }else{
                    Toast.makeText(ManuntecaoActivity.this, "Erro de conexão, tente mais tarde.", Toast.LENGTH_SHORT).show();
                    dialog_carregando.dismiss();
                }
            }

            @Override
            public void onFailure(Call<MsgModel> call, Throwable t) {

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_TROCA_IMAGE_SELECT && resultCode == RESULT_OK && data != null) {
            // Obter a URI da imagem selecionada
            Uri selectedImageUri = data.getData();

            // Obter o nome do arquivo da imagem selecionada
            String imageName = getFileNameFromUri(selectedImageUri);
            vb.trocaText.setText(imageName);

            imgTroca = data.getData();

        }
        else if (requestCode == REQUEST_RECIBO_IMAGE_SELECT && resultCode == RESULT_OK && data != null){
            // Obter a URI da imagem selecionada
            Uri selectedImageUri = data.getData();

            // Obter o nome do arquivo da imagem selecionada
            String imageName = getFileNameFromUri(selectedImageUri);

            imgRecibo = data.getData();
            // Exibir o nome da imagem no TextView
            vb.rebicoText.setText(imageName);

        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if ( item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("Range")
    private String getFileNameFromUri(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
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
                        AlertDialog.Builder b = new AlertDialog.Builder(ManuntecaoActivity.this);
                        b.setTitle("Atenção");
                        b.setMessage("Você não tem nenhum plano ativo, faça um plano para acessar aqui!");
                        b.setPositiveButton("ok", (dialogInterface, i) -> finish());
                        b.setCancelable(false);
                        b.show();
                        Toast.makeText(ManuntecaoActivity.this, msgModel.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(ManuntecaoActivity.this, "Erro de conexão, tente mais tarde.", Toast.LENGTH_SHORT).show();
                    dialog_carregando.dismiss();
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
        AlertDialog.Builder b = new AlertDialog.Builder(ManuntecaoActivity.this);
        b.setView(
                LayoutCarregandoBinding.inflate(getLayoutInflater()).getRoot()
        );
        b.setCancelable(false);
        dialog_carregando = b.create();
    }

    private void verificarStatusManu(){
        dialog_carregando.show();
        serviceApi.temManu(new ManuModel(preferences.getString("DadosUsuario", ""))).enqueue(new Callback<MsgModel>() {
            @Override
            public void onResponse(Call<MsgModel> call, Response<MsgModel> response) {

                MsgModel msgModel = response.body();

                if ( response.isSuccessful() ){
                    dialog_carregando.dismiss();
                    vb.sentBtn.setText(msgModel.getMsg());
                }else{
                    Toast.makeText(ManuntecaoActivity.this, "Erro de conexão, tente mais tarde.", Toast.LENGTH_SHORT).show();
                    dialog_carregando.dismiss();
                }
            }

            @Override
            public void onFailure(Call<MsgModel> call, Throwable t) {

            }
        });
    }

}