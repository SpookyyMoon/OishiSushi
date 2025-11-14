package com.example.oishisishi.entidades;

import java.util.List;

public class Comandas {
    public int numeroMesa;
    public List<Platos> pedidoPlatos;
    public boolean atendidaComanda;

    public Comandas(int numeroMesa, List<Platos> pedidoPlatos, boolean atendidaComanda) {
        this.numeroMesa = numeroMesa;
        this.pedidoPlatos = pedidoPlatos;
        this.atendidaComanda = atendidaComanda;
    }
}