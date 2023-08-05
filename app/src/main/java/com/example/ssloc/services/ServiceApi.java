package com.example.ssloc.services;

import com.example.ssloc.models.ManuModel;
import com.example.ssloc.models.MsgModel;
import com.example.ssloc.models.PlanoModel;
import com.example.ssloc.models.UsuarioModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ServiceApi {

    @POST("cadastro")
    Call<MsgModel> criarCadastro(@Body UsuarioModel usuarioModel);

    @POST("login")
    Call<MsgModel>fazerLogin(@Body UsuarioModel usuarioModel);

    @POST("criarplano")
    Call<PlanoModel>criarPlano(@Body PlanoModel planoModel);

    @POST("exibirplanostatus")
    Call<PlanoModel>verStatus(@Body PlanoModel planoModel);

    @POST("templanos")
    Call<MsgModel>temPlanos(@Body UsuarioModel usuarioModel);

    @POST("manutencaostatus")
    Call<MsgModel>temManu(@Body ManuModel manuModel);

    @POST("manutencao")
    Call<MsgModel>criarManu(@Body ManuModel manuModel);

    @POST("apagarplano")
    Call<MsgModel>apagarPlano(@Body PlanoModel planoModel);
}
