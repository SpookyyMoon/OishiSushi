package com.example.oishisishi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SeleccionMesa extends AppCompatActivity {
    ImageView botonAtras, mesa1, mesa2, mesa3, mesa4, mesa5;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.seleccion_mesa);
        botonAtras = findViewById(R.id.atras);
        mesa1 = findViewById(R.id.mesa1);
        mesa2 = findViewById(R.id.mesa2);
        mesa3 = findViewById(R.id.mesa3);
        mesa4 = findViewById(R.id.mesa4);
        mesa5 = findViewById(R.id.mesa5);

        botonAtras.setOnClickListener(v -> {
            Intent intent = new Intent(SeleccionMesa.this, MainActivity.class);
            startActivity(intent);
        });

        mesa1.setOnClickListener(v -> {
            mesa1.setImageResource(R.drawable.mesa1seleccionada);
            mesa2.setImageResource(R.drawable.mesa2);
            mesa3.setImageResource(R.drawable.mesa3);
            mesa4.setImageResource(R.drawable.mesa4);
            mesa5.setImageResource(R.drawable.mesa5);
        });

        mesa2.setOnClickListener(v -> {
            mesa2.setImageResource(R.drawable.mesa2seleccionada);
            mesa1.setImageResource(R.drawable.mesa1);
            mesa3.setImageResource(R.drawable.mesa3);
            mesa4.setImageResource(R.drawable.mesa4);
            mesa5.setImageResource(R.drawable.mesa5);
        });

        mesa3.setOnClickListener(v -> {
            mesa3.setImageResource(R.drawable.mesa3seleccionada);
            mesa1.setImageResource(R.drawable.mesa1);
            mesa2.setImageResource(R.drawable.mesa2);
            mesa4.setImageResource(R.drawable.mesa4);
            mesa5.setImageResource(R.drawable.mesa5);
        });

        mesa4.setOnClickListener(v -> {
            mesa4.setImageResource(R.drawable.mesa4seleccionada);
            mesa1.setImageResource(R.drawable.mesa1);
            mesa2.setImageResource(R.drawable.mesa2);
            mesa3.setImageResource(R.drawable.mesa3);
            mesa5.setImageResource(R.drawable.mesa5);
        });

        mesa5.setOnClickListener(v -> {
            mesa5.setImageResource(R.drawable.mesa5seleccionada);
            mesa1.setImageResource(R.drawable.mesa1);
            mesa2.setImageResource(R.drawable.mesa2);
            mesa3.setImageResource(R.drawable.mesa3);
            mesa4.setImageResource(R.drawable.mesa4);
        });
    }

    public void botonSiguiente(View view) {
        Intent intent = new Intent(this, Carta.class);
        startActivity(intent);
    }
}
