package com.example.ssloc.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ssloc.databinding.ActivityCadastroBinding;
import com.example.ssloc.databinding.LayoutCarregandoBinding;
import com.example.ssloc.models.CepModel;
import com.example.ssloc.models.MsgModel;
import com.example.ssloc.models.UsuarioModel;
import com.example.ssloc.services.ServiceApi;
import com.example.ssloc.services.ServiceCep;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CadastroActivity extends AppCompatActivity {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private UsuarioModel usuarioModel;
    private static final int REQUEST_CNH_IMAGE_SELECT = 1;
    private static final int REQUEST_COMPROVANTE_IMAGE_SELECT = 2;
    private Dialog dialog_carregando;
    private ActivityCadastroBinding vb;
    private Retrofit retrofitCep, retrofitCadastro;
    private ServiceCep serviceCep;
    private ServiceApi serviceApi;
    private ActionBar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vb = ActivityCadastroBinding.inflate(getLayoutInflater());
        setContentView(vb.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        preferences = getSharedPreferences("UsuarioPreferences", Context.MODE_PRIVATE);
        editor = preferences.edit();

        usuarioModel = new UsuarioModel(
                "",
                "",
                "",
                "",
                "",
                null,
                null,
                null
        );

        bar = getSupportActionBar();
        bar.setTitle("Cadastro");
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setDisplayShowHomeEnabled(true);

        criarRetrofitCep();
        criarRetrofitCadastro();
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

        vb.cnhSendBtn.setOnClickListener( cnhView -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_CNH_IMAGE_SELECT);
        });

        vb.comprovanteSendBtn.setOnClickListener( comprovanteView -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_COMPROVANTE_IMAGE_SELECT);
        });

        vb.finalizarBtn.setOnClickListener( finalView -> {
            clearErros();
            String msgError = "Esse campo é obrigatório!";
            String msgSenha = "As senhas não estão iguais!";

            String senha1,senha2;
            senha1 = vb.passField.getEditText().getText().toString().trim();
            senha2 = vb.repeatPassField.getEditText().getText().toString().trim();

            String loginString = vb.loginField.getEditText().getText().toString().trim();
            if (!loginString.isEmpty()) {
                usuarioModel.setLogin(loginString);
                String emailString = vb.emailField.getEditText().getText().toString().trim();
                if (!emailString.isEmpty()) {
                    usuarioModel.setEmail(emailString);
                    String telefoneString = vb.foneField.getEditText().getText().toString().trim();
                    if (!telefoneString.isEmpty()) {
                        usuarioModel.setTelefone(telefoneString);
                        String validadeCNHString = vb.validadeField.getEditText().getText().toString().trim();
                        if (!validadeCNHString.isEmpty()) {
                            usuarioModel.setValidadeCNH(validadeCNHString);
                            if (usuarioModel.getCep() != null) {
                                if (!senha1.isEmpty()){
                                    if(!senha2.isEmpty()){
                                        if ( senha2.equals(senha1)){
                                            usuarioModel.setSenha(senha1);
                                            if (usuarioModel.getFotoCNH() != null){
                                                if(usuarioModel.getFotoComprovante() != null){
                                                    criarCadastro();
                                                }else{
                                                    Toast.makeText(getApplicationContext(), "Envie a foto do comprovante!",Toast.LENGTH_LONG).show();
                                                }
                                            }else{
                                                Toast.makeText(getApplicationContext(), "Envie a foto da CNH!",Toast.LENGTH_SHORT).show();
                                            }
                                        }else{
                                            vb.repeatPassField.setError(msgSenha);
                                        }
                                    }else{
                                        vb.repeatPassField.setError(msgError);
                                    }
                                }else{
                                    vb.passField.setError(msgError);
                                }
                            }else{
                                vb.cepField.setError(msgError);
                            }
                        }else{
                            vb.validadeField.setError(msgError);
                        }
                    }else{
                        vb.foneField.setError(msgError);
                    }
                }else{
                    vb.emailField.setError(msgError);
                }
            }else{
                vb.loginField.setError(msgError);
            }
        });
    }

    public void clearErros(){
        vb.passField.setError(null);
        vb.repeatPassField.setError(null);
        vb.loginField.setError(null);
        vb.emailField.setError(null);
        vb.foneField.setError(null);
        vb.validadeField.setError(null);
        vb.cepField.setError(null);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CNH_IMAGE_SELECT && resultCode == RESULT_OK && data != null) {
            // Obter a URI da imagem selecionada
            Uri selectedImageUri = data.getData();
            
            // Obter o nome do arquivo da imagem selecionada
            String imageName = getFileNameFromUri(selectedImageUri);

            // Exibir o nome da imagem no TextView
            vb.cnhNameText.setText(imageName);

            // Agora, você pode usar a URI da imagem (selectedImageUri) para fazer o upload via Retrofit
            usuarioModel.setFotoCNH(selectedImageUri);
            // ou qualquer outra operação necessária.
        }
        else if (requestCode == REQUEST_COMPROVANTE_IMAGE_SELECT && resultCode == RESULT_OK && data != null){
            // Obter a URI da imagem selecionada
            Uri selectedImageUri = data.getData();

            // Obter o nome do arquivo da imagem selecionada
            String imageName = getFileNameFromUri(selectedImageUri);

            // Exibir o nome da imagem no TextView
            vb.comprovanteNameText.setText(imageName);

            // Agora, você pode usar a URI da imagem (selectedImageUri) para fazer o upload via Retrofit
            usuarioModel.setFotoComprovante(selectedImageUri);
            // ou qualquer outra operação necessária.
        }
    }
    // Método para obter o nome do arquivo da URI
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if ( item.getItemId() == android.R.id.home){
            startActivity(new Intent(getApplicationContext(), LoginOuCadastroActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void criarRetrofitCadastro(){
        retrofitCadastro = new Retrofit
                .Builder()
                .baseUrl("https://apissloc.vercel.app/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        serviceApi = retrofitCadastro.create(ServiceApi.class);
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
                    usuarioModel.setCep(cepModel);
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

    private void criarCadastro(){
        dialog_carregando.show();
        serviceApi.criarCadastro(usuarioModel).enqueue(new Callback<MsgModel>() {
            @Override
            public void onResponse(Call<MsgModel> call, Response<MsgModel> response) {
                MsgModel msgModel = response.body();
                if ( response.isSuccessful() ){
                    if ( !msgModel.getError() ){
                        editor.putString("DadosUsuario", usuarioModel.getLogin());
                        editor.apply();
                        startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                        Toast.makeText(CadastroActivity.this, msgModel.getMsg(), Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(CadastroActivity.this, msgModel.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(CadastroActivity.this, "Problema de conexão, tente novamente.", Toast.LENGTH_SHORT).show();
                    dialog_carregando.dismiss();
                }
            }

            @Override
            public void onFailure(Call<MsgModel> call, Throwable t) {

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