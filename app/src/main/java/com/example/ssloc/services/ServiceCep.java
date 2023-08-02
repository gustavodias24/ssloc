package com.example.ssloc.services;

import com.example.ssloc.models.CepModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ServiceCep{
    @GET("ws/{cep}/json/")
    Call<CepModel> buscar_cep(@Path("cep") String cep);

}
