package com.example.oishisishi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class CarritoVacio extends AppCompatActivity {
    ImageView botonAtras;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.carrito_vacio);
        botonAtras = findViewById(R.id.atras);

        botonAtras.setOnClickListener(v -> {
            Intent intent = new Intent(CarritoVacio.this, Carta.class);
            startActivity(intent);
        });
    }
}
