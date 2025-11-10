package com.example.oishisishi.entidades;

import java.io.Serializable;
import java.util.List;

public class Mesas implements Serializable {
    public int numeroMesa;
    public boolean ocupadaMesa;
    public List<String> comandasMesa;
    public List<String> carritoMesa;
}
