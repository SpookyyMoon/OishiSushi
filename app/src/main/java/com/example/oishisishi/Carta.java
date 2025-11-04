package com.example.oishisishi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Carta extends AppCompatActivity {
    ImageView botonAtras, botonCarrito;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.carta);
        botonAtras = findViewById(R.id.atras);
        botonCarrito = findViewById(R.id.carrito);

        botonAtras.setOnClickListener(v -> {
            Intent intent = new Intent(Carta.this, SeleccionMesa.class);
            startActivity(intent);
        });

        botonCarrito.setOnClickListener(v -> {
            Intent intent = new Intent(Carta.this, CarritoVacio.class);
            startActivity(intent);
        });
    }
}
