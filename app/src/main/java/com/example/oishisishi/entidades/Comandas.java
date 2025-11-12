package com.example.oishisishi.entidades;

import java.util.List;

public class Comandas {
    public int numeroMesa;
    public List<Platos> pedidoPlatos;
    public boolean atendidaComanda;
    public boolean pagadaComida;

    public Comandas(int numeroMesa, List<Platos> pedidoPlatos, boolean atendidaComanda, boolean pagadaComida) {
        this.numeroMesa = numeroMesa;
        this.pedidoPlatos = pedidoPlatos;
        this.atendidaComanda = atendidaComanda;
        this.pagadaComida = pagadaComida;
    }
}