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
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ssloc.R;
import com.example.ssloc.databinding.ActivityMeusPlanosBinding;
import com.example.ssloc.databinding.LayoutCarregandoBinding;
import com.example.ssloc.models.ManuModel;
import com.example.ssloc.models.PlanoModel;
import com.example.ssloc.services.ServiceApi;
import com.example.ssloc.services.ServiceCep;
import com.squareup.picasso.Picasso;

import java.net.InetSocketAddress;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MeusPlanosActivity extends AppCompatActivity {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Dialog dialog_carregando;
    private Retrofit retrofitCadastro;
    private ServiceApi serviceApi;
     private ActivityMeusPlanosBinding vb;
     private static final int REQUEST_COMPROVANTE_IMAGE_SELECT = 1;

     private String validade1ano = "Válido por 1 ano";
     private String CHAVE_900_REIAS = "00020126580014br.gov.bcb.pix0136626e4a9a-0501-462c-9edd-869f6290b71a5204000053039865406900.005802BR5914PEDRO DE SOUSA6009Sao Paulo62070503***6304A074";
     private String validade1semana = "Válido por 1 semana";
     private String CHAVE_240_REAIS = "00020126360014br.gov.bcb.pix0114+55969910179255204000053039865406240.005802BR5914PEDRO DE SOUSA6009Sao Paulo62070503***6304CC9A";
     private String validade1dia = "Válido por 1 dia";
     private String CHAVE_40_REAIS = "00020126360014br.gov.bcb.pix0114+5596991017925520400005303986540540.005802BR5914PEDRO DE SOUSA6009Sao Paulo62070503***6304EB58";

     private int altenardor = 0;
     ClipboardManager clip;
     @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vb = ActivityMeusPlanosBinding.inflate(getLayoutInflater());
        setContentView(vb.getRoot());

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        preferences = getSharedPreferences("UsuarioPreferences", Context.MODE_PRIVATE);
        editor = preferences.edit();

        criarRetrofitCadastro();
        criarAlertCarregando();

        clip = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        getSupportActionBar().setTitle("Meus planos");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Picasso.get().load(R.raw.logoparada).into(vb.logo);

        alternarEntrePlanos(0);

        vb.enviarBtn.setOnClickListener( comprovanteView -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_COMPROVANTE_IMAGE_SELECT);
        });

        vb.proxBtn.setOnClickListener( proxiView ->{
            if (altenardor != 2){
                altenardor ++;
            }else{
                altenardor = 0;
            }
            alternarEntrePlanos(altenardor);
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if ( item.getItemId() == android.R.id.home){
            startActivity(new Intent(getApplicationContext(), MenuActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_COMPROVANTE_IMAGE_SELECT && resultCode == RESULT_OK && data != null) {
            // Obter a URI da imagem selecionada
            Uri selectedImageUri = data.getData();

            // Obter o nome do arquivo da imagem selecionada
            String imageName = getFileNameFromUri(selectedImageUri);
            vb.imageText.setText(imageName);

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String currentDate = sdf.format(calendar.getTime());

            criarPlano(
                    new PlanoModel( "",
                            preferences.getString("DadosUsuario", ""),
                            false,
                            false,
                            altenardor,
                            currentDate,
                            selectedImageUri
                    )
            );

        }
    }

    private void criarPlano(PlanoModel planoModel){

         dialog_carregando.show();
         serviceApi.criarPlano(planoModel).enqueue(new Callback<PlanoModel>() {
             @Override
             public void onResponse(Call<PlanoModel> call, Response<PlanoModel> response) {
                 PlanoModel planoRequest = response.body();
                 if ( response.isSuccessful() ){
                     if ( !planoRequest.getError() ){
                         dialog_carregando.dismiss();
                         Toast.makeText(MeusPlanosActivity.this, planoRequest.getMsg(), Toast.LENGTH_SHORT).show();
                         vb.enviarBtn.setText("Em análise");
                         vb.enviarBtn.setTextColor(Color.GREEN);
                     }else{
                         dialog_carregando.dismiss();
                         Toast.makeText(MeusPlanosActivity.this, planoRequest.getMsg(), Toast.LENGTH_SHORT).show();
                     }
                 }else{
                     dialog_carregando.dismiss();
                     Toast.makeText(MeusPlanosActivity.this, "Problema de conexão,tente mais tarde.", Toast.LENGTH_SHORT).show();
                 }
             }

             @Override
             public void onFailure(Call<PlanoModel> call, Throwable t) {

             }
         });
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

    @SuppressLint("ResourceType")
    private void alternarEntrePlanos(int n){
         switch (n){
             case 0:
                 vb.qrcode.setOnClickListener( qrView -> {
                     clip.setPrimaryClip(ClipData.newPlainText("pix", CHAVE_900_REIAS));
                     Toast.makeText(this, "Pix copiado para área de transferência", Toast.LENGTH_LONG).show();
                 });
                 Picasso.get().load(R.raw.qrcodeplano900).into(vb.qrcode);
                 verificarStatus(
                         new PlanoModel(
                                 preferences.getString("DadosUsuario", ""),
                                 altenardor)
                 );
                 vb.validadeText.setText(validade1ano);
                 break;
             case 1:
                 vb.validadeText.setText(validade1semana);
                 vb.qrcode.setOnClickListener( qrView -> {
                     clip.setPrimaryClip(ClipData.newPlainText("pix",CHAVE_240_REAIS));
                     Toast.makeText(this, "Pix copiado para área de transferência", Toast.LENGTH_LONG).show();
                 });
                 Picasso.get().load(R.raw.qrcodeplano240).into(vb.qrcode);
                 verificarStatus(
                         new PlanoModel(
                                 preferences.getString("DadosUsuario", ""),
                                 altenardor)
                 );
                 break;
             case 2:
                 vb.validadeText.setText(validade1dia);
                 vb.qrcode.setOnClickListener( qrView -> {
                     clip.setPrimaryClip(ClipData.newPlainText("pix",CHAVE_40_REAIS));
                     Toast.makeText(this, "Pix copiado para área de transferência", Toast.LENGTH_LONG).show();
                 });
                 Picasso.get().load(R.raw.qrcodeplano40).into(vb.qrcode);
                 verificarStatus(
                         new PlanoModel(
                                 preferences.getString("DadosUsuario", ""),
                                 altenardor)
                 );
                 break;
             default:
                 Toast.makeText(this, "Plano", Toast.LENGTH_SHORT).show();
         }
    }

    private void verificarStatus(PlanoModel planoModel){
         dialog_carregando.show();
         serviceApi.verStatus(planoModel).enqueue(new Callback<PlanoModel>() {
             @Override
             public void onResponse(Call<PlanoModel> call, Response<PlanoModel> response) {

                 PlanoModel planoRequest = response.body();

                 if ( response.isSuccessful() ){
                     if ( planoRequest.getAtivo() ){
                         vb.enviarBtn.setText(planoRequest.getMsg());
                         vb.enviarBtn.setTextColor(Color.parseColor("#FEFE01"));
                         dialog_carregando.dismiss();
                     }else{
                         Toast.makeText(MeusPlanosActivity.this, planoRequest.getMsg() , Toast.LENGTH_LONG).show();
                         vb.enviarBtn.setText(planoRequest.getMsg());
                         vb.enviarBtn.setTextColor(Color.RED);
                         dialog_carregando.dismiss();
                     }
                 }else{
                     dialog_carregando.dismiss();
                     Toast.makeText(MeusPlanosActivity.this, "Problema de conexão, tente mais tarde!", Toast.LENGTH_SHORT).show();
                 }
             }

             @Override
             public void onFailure(Call<PlanoModel> call, Throwable t) {

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
        AlertDialog.Builder b = new AlertDialog.Builder(MeusPlanosActivity.this);
        b.setView(
                LayoutCarregandoBinding.inflate(getLayoutInflater()).getRoot()
        );
        b.setCancelable(false);
        dialog_carregando = b.create();
    }


}