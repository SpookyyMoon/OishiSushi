package com.example.oishisishi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.oishisishi.entidades.Mesas;

public class Carrito extends AppCompatActivity {
    ImageView botonAtras;
    Mesas mesaSeleccionada;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        mesaSeleccionada = (Mesas) getIntent().getSerializableExtra("mesaSeleccionada");
        if (mesaSeleccionada == null) {
            Log.e("onCreate Carrito", "mesaSeleccionada es null!");
        }
        setContentView(R.layout.carrito_vacio);
        botonAtras = findViewById(R.id.atras);

        botonAtras.setOnClickListener(v -> {
            Intent intent = new Intent(Carrito.this, Carta.class);
            intent.putExtra("mesaSeleccionada", mesaSeleccionada);
            startActivity(intent);
        });
    }
}
