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
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.ssloc.RecyclerItemClickListener;
import com.example.ssloc.adapter.UserAdapter;
import com.example.ssloc.databinding.ActivityAdminBinding;
import com.example.ssloc.databinding.LayoutCarregandoBinding;
import com.example.ssloc.models.UsuarioCompletoModel;
import com.example.ssloc.services.ServiceApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdminActivity extends AppCompatActivity {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Dialog dialog_carregando;
    private UserAdapter adapter;
    private Retrofit retrofitCadastro;
    private ServiceApi serviceApi;
    private ActivityAdminBinding vb;
    private RecyclerView recyclerUsers;
    private List<UsuarioCompletoModel> lista = new ArrayList<>();

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

            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        }));

        adapter = new UserAdapter(lista, getApplicationContext());
        recyclerUsers.setAdapter(adapter);

        vb.fabAtt.setOnClickListener( attView -> {
            listarTodosUsuarios();
        });

        vb.sairBtn.setOnClickListener( sairView -> {
            editor.putString("DadosUsuario", null);
            editor.apply();
            startActivity(new Intent(getApplicationContext(), LoginOuCadastroActivity.class));
            finish();
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

    private void criarAlertCarregando(){
        AlertDialog.Builder b = new AlertDialog.Builder(AdminActivity.this);
        b.setView(
                LayoutCarregandoBinding.inflate(getLayoutInflater()).getRoot()
        );
        b.setCancelable(false);
        dialog_carregando = b.create();
    }
}