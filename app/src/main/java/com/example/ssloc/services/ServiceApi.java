package com.example.ssloc.services;

import com.example.ssloc.models.ManuModel;
import com.example.ssloc.models.MsgModel;
import com.example.ssloc.models.PlanoModel;
import com.example.ssloc.models.UsuarioCompletoModel;
import com.example.ssloc.models.UsuarioModel;
import com.example.ssloc.models.VeiculoModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

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

    @GET("todosusuarios")
    Call<List<UsuarioCompletoModel>>listarUsuarios();

    @GET("todosusuarios")
    Call<List<UsuarioCompletoModel>>umUsuarioCompleto(@Query("login") String login);

    @POST("ativarplano")
    Call<MsgModel>ativarPlano(@Body PlanoModel planoModel);

    @POST("aprovarmanu")
    Call<MsgModel>aprovarManu(@Body ManuModel manuModel);

    @POST("criarveiculo")
    Call<MsgModel>criarveiculo(@Body VeiculoModel veiculoModel);

    @GET("pegarveiculo")
    Call<List<VeiculoModel>>pegarveiculos();

    @GET("pegarveiculo")
    Call<List<VeiculoModel>>pegarveiculosdisponiveis(@Query("disponivel") Boolean disponivel);


    @POST("atualizarveiculo")
    Call<MsgModel>atualizarveiculos(@Body VeiculoModel veiculoModel);

    @POST("deleteveiculo")
    Call<MsgModel>deletarVeiculo(@Body VeiculoModel veiculoModel);

    @POST("deleteusuario")
    Call<MsgModel>deleteUsuario(@Body UsuarioModel usuarioModel);

}
