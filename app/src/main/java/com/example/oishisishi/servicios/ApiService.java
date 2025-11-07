package com.example.oishisishi.servicios;

import com.example.oishisishi.entidades.Comandas;
import com.example.oishisishi.entidades.Mesas;
import com.example.oishisishi.entidades.Platos;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface ApiService {
    @GET("platos")
    Call<List<Platos>> getPlatos();

    @GET("comandas")
    Call<List<Comandas>> getComandas();

    @GET("mesas")
    Call<List<Mesas>> getMesas();
}

