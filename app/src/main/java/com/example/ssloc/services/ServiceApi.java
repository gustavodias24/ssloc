package com.example.ssloc.services;

import com.example.ssloc.models.MsgModel;
import com.example.ssloc.models.UsuarioModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ServiceApi {

    @POST("cadastro")
    Call<MsgModel> criarCadastro(@Body UsuarioModel usuarioModel);

    @POST("login")
    Call<MsgModel>fazerLogin(@Body UsuarioModel usuarioModel);
}
