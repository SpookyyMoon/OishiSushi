package com.example.oishisishi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SeleccionMesa extends AppCompatActivity {
    ImageView botonAtras, botonCarrito, mesa1, mesa2, mesa3, mesa4, mesa5;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.seleccion_mesa);
        botonAtras = findViewById(R.id.atras);
        botonCarrito = findViewById(R.id.carrito);
        mesa1 = findViewById(R.id.mesa1);
        mesa2 = findViewById(R.id.mesa2);
        mesa3 = findViewById(R.id.mesa3);
        mesa4 = findViewById(R.id.mesa4);
        mesa5 = findViewById(R.id.mesa5);

        botonAtras.setOnClickListener(v -> {
            Intent intent = new Intent(SeleccionMesa.this, MainActivity.class);
            startActivity(intent);
        });

        botonCarrito.setOnClickListener(v -> {
            Intent intent = new Intent(SeleccionMesa.this, CarritoVacio.class);
            startActivity(intent);
        });

        mesa1.setOnClickListener(v -> {
            mesa1.setImageResource(R.drawable.mesa1seleccionada);
        });

        mesa2.setOnClickListener(v -> {
            mesa2.setImageResource(R.drawable.mesa2seleccionada);
        });

        mesa3.setOnClickListener(v -> {
            mesa3.setImageResource(R.drawable.mesa3seleccionada);
        });

        mesa4.setOnClickListener(v -> {
            mesa4.setImageResource(R.drawable.mesa4seleccionada);
        });

        mesa5.setOnClickListener(v -> {
            mesa5.setImageResource(R.drawable.mesa5seleccionada);
        });
    }

    public void botonSiguiente(View view) {
        Intent intent = new Intent(this, Carta.class);
        startActivity(intent);
    }
}
