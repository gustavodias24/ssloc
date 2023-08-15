package com.example.ssloc.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ssloc.ImageUtils;
import com.example.ssloc.RecyclerItemClickListener;
import com.example.ssloc.adapter.UserAdapter;
import com.example.ssloc.adapter.VeiculoAdapter;
import com.example.ssloc.databinding.ActivityAdminBinding;
import com.example.ssloc.databinding.AdicionarVeiculoAdminBinding;
import com.example.ssloc.databinding.LayoutCarregandoBinding;
import com.example.ssloc.models.MsgModel;
import com.example.ssloc.models.UsuarioCompletoModel;
import com.example.ssloc.models.UsuarioModel;
import com.example.ssloc.models.VeiculoModel;
import com.example.ssloc.services.ServiceApi;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdminActivity extends AppCompatActivity {

    private VeiculoModel veiculoEditado = new VeiculoModel();
    private Dialog dialog_adicionar;
    private Dialog dialog_atualizar;
    private TextView nomeImagem;
    private TextView nomeImagemAtualizada;
    private VeiculoModel veiculoModel;
    private static final int REQUEST_VEICULO_IMAGE_SELECT = 1;
    private static final int REQUEST_ATUALIZAR_VEICULO_IMAGE_SELECT = 2;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Dialog dialog_carregando;
    private UserAdapter adapter;
    private VeiculoAdapter adapter2;
    private List<VeiculoModel> lista2 = new ArrayList<>();
    private Retrofit retrofitCadastro;
    private ServiceApi serviceApi;
    private ActivityAdminBinding vb;
    private RecyclerView recyclerUsers;
    private RecyclerView recyclerVeiculos;
    private List<UsuarioCompletoModel> lista = new ArrayList<>();

    private int alternador = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = getSharedPreferences("UsuarioPreferences", Context.MODE_PRIVATE);
        editor = preferences.edit();

        vb = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(vb.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        criarAlertCarregando();
        criarRetrofitCadastro();

        Objects.requireNonNull(getSupportActionBar()).setTitle("Admin");

        listarTodosUsuarios();

        recyclerUsers = vb.recyclerUsuarios;
        recyclerUsers.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerUsers.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        recyclerUsers.setHasFixedSize(true);

        recyclerVeiculos = vb.recyclerVeiculos;
        recyclerVeiculos.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerVeiculos.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        recyclerVeiculos.setHasFixedSize(true);
        adapter2 = new VeiculoAdapter(lista2, getApplicationContext());
        recyclerVeiculos.setAdapter(adapter2);

        recyclerVeiculos.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerVeiculos, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                atualizarVeiculo(lista2.get(position));
            }

            @Override
            public void onLongItemClick(View view, int position) {
                deletarVeiculo(lista2.get(position));
            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        }));
        recyclerUsers.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerUsers, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
            UsuarioCompletoModel usuarioClicado = lista.get(position);
            Intent i = new Intent(getApplicationContext(), ExibirUsuarioActivity.class);
//            i.putExtra("endereco", usuarioClicado.getUsuarioModel().getCep().toString());
//            i.putExtra("dados", usuarioClicado.getUsuarioModel().toString());
//            i.putExtra("fotocnh", usuarioClicado.getUsuarioModel().getFotoCNH());
//            i.putExtra("fotocomprovante", usuarioClicado.getUsuarioModel().getFotoComprovante());
            i.putExtra("login", usuarioClicado.getUsuarioModel().getLogin());

            try{
                startActivity(i);

            }catch (Exception e){
                Log.d("uaisir", "onItemClick: " + e.getMessage());
            }

            }

            @Override
            public void onLongItemClick(View view, int position) {
                deletarUsuario(lista.get(position).getUsuarioModel());
            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        }));

        adapter = new UserAdapter(lista, getApplicationContext());
        recyclerUsers.setAdapter(adapter);

        vb.fabAtt.setOnClickListener( attView -> {
            if ( vb.textViewTipo.getText().toString().equals("veículos")){
                adicionarVeiculos();
            }else{
                listarTodosUsuarios();
            }
        });

        vb.sairBtn.setOnClickListener( sairView -> {
            editor.putString("DadosUsuario", null);
            editor.apply();
            startActivity(new Intent(getApplicationContext(), LoginOuCadastroActivity.class));
            finish();
        });

        vb.altenarbtn.setOnClickListener( alternarView -> {
            if ( alternador == 0){
                listarTodosUsuarios();
                vb.altenarbtn.setText("usuários");
                vb.textViewTipo.setText("usuários");
                vb.recyclerUsuarios.setVisibility(View.VISIBLE);
                vb.recyclerVeiculos.setVisibility(View.GONE);
                alternador++;
            }else{
                alternador = 0;
                vb.recyclerUsuarios.setVisibility(View.GONE);
                vb.recyclerVeiculos.setVisibility(View.VISIBLE);
                vb.altenarbtn.setText("veículos");
                vb.textViewTipo.setText("veículos");
                listarTodosVeiculos();
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

    private void listarTodosUsuarios(){
        dialog_carregando.show();
        lista.clear();

        serviceApi.listarUsuarios().enqueue(new Callback<List<UsuarioCompletoModel>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<List<UsuarioCompletoModel>> call, @NonNull Response<List<UsuarioCompletoModel>> response) {

                if ( response.isSuccessful() ){
                    List<UsuarioCompletoModel> listaApi = response.body();

                    assert listaApi != null;
                    lista.addAll(listaApi);

                    adapter.notifyDataSetChanged();
                    dialog_carregando.dismiss();
                }else{
                    Toast.makeText(AdminActivity.this, "Erro de conexão, tente mais tarde.", Toast.LENGTH_SHORT).show();

                    dialog_carregando.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<UsuarioCompletoModel>> call, Throwable t) {
                dialog_carregando.dismiss();
                Log.d("fluminece", t.toString());
                Toast.makeText(AdminActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void listarTodosVeiculos(){
        dialog_carregando.show();
        lista2.clear();

        serviceApi.pegarveiculos().enqueue(new Callback<List<VeiculoModel>>() {
            @Override
            public void onResponse(Call<List<VeiculoModel>> call, Response<List<VeiculoModel>> response) {
                if ( response.isSuccessful() ){
                    lista2.addAll(response.body());
                    adapter2.notifyDataSetChanged();
                    dialog_carregando.dismiss();
                }else{
                    Toast.makeText(AdminActivity.this, "Erro de conexão, tente mais tarde.", Toast.LENGTH_SHORT).show();

                    dialog_carregando.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<VeiculoModel>> call, Throwable t) {

            }
        });

    }

    private void criarAlertCarregando(){
        AlertDialog.Builder b = new AlertDialog.Builder(AdminActivity.this);
        b.setView(
                LayoutCarregandoBinding.inflate(getLayoutInflater()).getRoot()
        );
        b.setCancelable(false);
        dialog_carregando = b.create();
    }

    public void adicionarVeiculos(){

        veiculoModel = new VeiculoModel(true, "", "");

        AlertDialog.Builder b = new AlertDialog.Builder(AdminActivity.this);

        AdicionarVeiculoAdminBinding addVeiculoBinding = AdicionarVeiculoAdminBinding.inflate(getLayoutInflater());

        addVeiculoBinding.enviarImagem.setOnClickListener( imageView -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_VEICULO_IMAGE_SELECT);
        });

        nomeImagem = addVeiculoBinding.nomeImagem;

        addVeiculoBinding.ok.setOnClickListener( okView -> {
            dialog_carregando.show();
            veiculoModel.setDescricao(
                    addVeiculoBinding.editTextDescricao.getText().toString()
            );

            if( !addVeiculoBinding.radioDisponivel.isChecked() ){
                veiculoModel.setDisponivel(false);
            }
            veiculoModel.set_id("");
            serviceApi.criarveiculo(veiculoModel).enqueue(new Callback<MsgModel>() {
                @Override
                public void onResponse(Call<MsgModel> call, Response<MsgModel> response) {
                    if ( response.isSuccessful() ){

                        Toast.makeText(AdminActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        dialog_carregando.dismiss();
                        dialog_adicionar.dismiss();
                        listarTodosVeiculos();

                    }else{
                        Toast.makeText(AdminActivity.this, "Erro de conexão, tente mais tarde.", Toast.LENGTH_SHORT).show();
                        dialog_carregando.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<MsgModel> call, Throwable t) {

                }
            });
        });

        b.setView(addVeiculoBinding.getRoot());

        dialog_adicionar = b.create();
        dialog_adicionar.show();

    }

    public void atualizarVeiculo(VeiculoModel veiculoClicado){
            AlertDialog.Builder b = new AlertDialog.Builder(AdminActivity.this);
            AdicionarVeiculoAdminBinding addBinding = AdicionarVeiculoAdminBinding.inflate(getLayoutInflater());

            veiculoEditado.setFoto(veiculoClicado.getFoto());

            addBinding.editTextDescricao.setText(veiculoClicado.getDescricao());
            if ( veiculoClicado.disponivel ){
                addBinding.radioDisponivel.setChecked(true);
            }else{
                addBinding.radioIndisponivel.setChecked(true);
            }
            nomeImagemAtualizada = addBinding.nomeImagem;
            addBinding.enviarImagem.setOnClickListener( enviarView -> {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_ATUALIZAR_VEICULO_IMAGE_SELECT);
            });
            addBinding.ok.setText("Editar");
            addBinding.ok.setOnClickListener( okView -> {
                veiculoEditado.setDescricao(addBinding.editTextDescricao.getText().toString());
                if ( addBinding.radioDisponivel.isChecked() ){
                    veiculoEditado.setDisponivel(true);
                }else{
                    veiculoEditado.setDisponivel(false);
                }

                dialog_carregando.show();

                veiculoEditado.set_id(veiculoClicado.get_id());
                serviceApi.atualizarveiculos(veiculoEditado).enqueue(new Callback<MsgModel>() {
                    @Override
                    public void onResponse(Call<MsgModel> call, Response<MsgModel> response) {
                        if ( response.isSuccessful() ){
                            Toast.makeText(AdminActivity.this, "Atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                            listarTodosVeiculos();
                            dialog_carregando.dismiss();
                        }else{
                            Toast.makeText(AdminActivity.this, "Erro de conexão, tente mais tarde.", Toast.LENGTH_SHORT).show();
                            dialog_carregando.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<MsgModel> call, Throwable t) {
                        Toast.makeText(AdminActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog_carregando.dismiss();
                    }
                });
            });

            b.setView(addBinding.getRoot());

            dialog_atualizar = b.create();
            dialog_atualizar.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_VEICULO_IMAGE_SELECT && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            // Obter o nome do arquivo da imagem selecionada
            String imageName = getFileNameFromUri(selectedImageUri);
            veiculoModel.setFoto(ImageUtils.imageToBase64Comprimida(selectedImageUri, getApplicationContext()));
            // Exibir o nome da imagem no TextView
            nomeImagem.setText(imageName);

        }else if (requestCode == REQUEST_ATUALIZAR_VEICULO_IMAGE_SELECT && resultCode == RESULT_OK && data != null){
            Uri selectedImageUri = data.getData();
            // Obter o nome do arquivo da imagem selecionada
            String imageName = getFileNameFromUri(selectedImageUri);
            veiculoEditado.setFoto(ImageUtils.imageToBase64Comprimida(selectedImageUri, getApplicationContext()));
            nomeImagemAtualizada.setText(imageName);
        }
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

//    private String imageToBase64(Uri imageUri) {
//        try {
//            InputStream inputStream = getContentResolver().openInputStream(imageUri);
//            byte[] bytes = new byte[inputStream.available()];
//            inputStream.read(bytes);
//            return Base64.encodeToString(bytes, Base64.DEFAULT);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return "";
//    }

    public void deletarVeiculo(VeiculoModel veiculoModel){
        dialog_carregando.show();
        serviceApi.deletarVeiculo(veiculoModel).enqueue(new Callback<MsgModel>() {
            @Override
            public void onResponse(Call<MsgModel> call, Response<MsgModel> response) {
                if ( response.isSuccessful() ){
                    Toast.makeText(AdminActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    listarTodosVeiculos();
                }else{
                    Toast.makeText(AdminActivity.this, "erro de conexão, tente mais tarde!", Toast.LENGTH_SHORT).show();
                }
                dialog_carregando.dismiss();
            }

            @Override
            public void onFailure(Call<MsgModel> call, Throwable t) {

            }
        });
    }

    public void deletarUsuario(UsuarioModel usuarioModel){
        dialog_carregando.show();

        serviceApi.deleteUsuario(usuarioModel).enqueue(new Callback<MsgModel>() {
            @Override
            public void onResponse(Call<MsgModel> call, Response<MsgModel> response) {
                if ( response.isSuccessful() ){
                    Toast.makeText(AdminActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    listarTodosUsuarios();
                }else{
                    Toast.makeText(AdminActivity.this, "erro de conexão, tente mais tarde!", Toast.LENGTH_SHORT).show();
                }
                dialog_carregando.dismiss();
            }

            @Override
            public void onFailure(Call<MsgModel> call, Throwable t) {

            }
        });
    }

}