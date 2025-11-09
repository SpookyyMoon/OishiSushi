package com.example.oishisishi;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.oishisishi.adaptadores.ApiAdapter;
import com.example.oishisishi.entidades.Comandas;
import com.example.oishisishi.entidades.Mesas;
import com.example.oishisishi.entidades.Platos;
import com.example.oishisishi.servicios.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SeleccionMesa extends AppCompatActivity implements Callback<List<Mesas>> {
    ImageView botonAtras, mesa1, mesa2, mesa3, mesa4, mesa5;
    List<Mesas> listaMesas;

    Mesas mesaSeleccionada;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.seleccion_mesa);
        Call<List<Mesas>> call = ApiAdapter.getApiService().getMesas();
        call.enqueue(this);

        botonAtras = findViewById(R.id.atras);
        mesa1 = findViewById(R.id.mesa1);
        mesa2 = findViewById(R.id.mesa2);
        mesa3 = findViewById(R.id.mesa3);
        mesa4 = findViewById(R.id.mesa4);
        mesa5 = findViewById(R.id.mesa5);

        // Desactiva las mesas hasta cargarse por retrofit (Evita null pointer)
        mesa1.setEnabled(false);
        mesa2.setEnabled(false);
        mesa3.setEnabled(false);
        mesa4.setEnabled(false);
        mesa5.setEnabled(false);

        botonAtras.setOnClickListener(v -> {
            Intent intent = new Intent(SeleccionMesa.this, MainActivity.class);
            startActivity(intent);
        });

        mesa1.setOnClickListener(v -> {
            Mesas mesaUno = listaMesas.get(0);
            if (!mesaUno.ocupadaMesa){
                mesa1.setImageResource(R.drawable.mesa1seleccionada);
                mesa2.setImageResource(R.drawable.mesa2);
                mesa3.setImageResource(R.drawable.mesa3);
                mesa4.setImageResource(R.drawable.mesa4);
                mesa5.setImageResource(R.drawable.mesa5);
                mesaSeleccionada = mesaUno;
            }
            else{
                System.out.println("Mesa ocupada!");
                // Añadir mensaje alerta
            }
        });

        mesa2.setOnClickListener(v -> {
            Mesas mesaDos = listaMesas.get(1);
            if(!mesaDos.ocupadaMesa){
                mesa2.setImageResource(R.drawable.mesa2seleccionada);
                mesa1.setImageResource(R.drawable.mesa1);
                mesa3.setImageResource(R.drawable.mesa3);
                mesa4.setImageResource(R.drawable.mesa4);
                mesa5.setImageResource(R.drawable.mesa5);
                mesaSeleccionada = mesaDos;
            }
            else{
                System.out.println("Mesa ocupada!");
                // Añadir mensaje alerta
            }
        });

        mesa3.setOnClickListener(v -> {
            Mesas mesaTres = listaMesas.get(2);
            if (!mesaTres.ocupadaMesa) {
                mesa3.setImageResource(R.drawable.mesa3seleccionada);
                mesa1.setImageResource(R.drawable.mesa1);
                mesa2.setImageResource(R.drawable.mesa2);
                mesa4.setImageResource(R.drawable.mesa4);
                mesa5.setImageResource(R.drawable.mesa5);
                mesaSeleccionada = mesaTres;
            }
            else {
                System.out.println("Mesa ocupada!");
                // Añadir mensaje alerta
            }
        });

        mesa4.setOnClickListener(v -> {
            Mesas mesaCuatro = listaMesas.get(3);
            if (!mesaCuatro.ocupadaMesa) {
                mesa4.setImageResource(R.drawable.mesa4seleccionada);
                mesa1.setImageResource(R.drawable.mesa1);
                mesa2.setImageResource(R.drawable.mesa2);
                mesa3.setImageResource(R.drawable.mesa3);
                mesa5.setImageResource(R.drawable.mesa5);
                mesaSeleccionada = mesaCuatro;
            }
            else {
                System.out.println("Mesa ocupada!");
                // Añadir mensaje alerta
            }
        });

        mesa5.setOnClickListener(v -> {
            Mesas mesaCinco = listaMesas.get(4);
            if (!mesaCinco.ocupadaMesa) {
                mesa5.setImageResource(R.drawable.mesa5seleccionada);
                mesa1.setImageResource(R.drawable.mesa1);
                mesa2.setImageResource(R.drawable.mesa2);
                mesa3.setImageResource(R.drawable.mesa3);
                mesa4.setImageResource(R.drawable.mesa4);
                mesaSeleccionada = mesaCinco;
            }
            else {
                System.out.println("Mesa ocupada!");
                // Añadir mensaje alerta
            }
        });
    }

    public void botonSiguiente(View view) {
        if (mesaSeleccionada == null){
            // Mensaje alerta
        }
        else{
            mesaSeleccionada.ocupadaMesa = true;

            Call<Mesas> call = ApiAdapter.getApiService().updateMesas(mesaSeleccionada.numeroMesa, mesaSeleccionada);
            call.enqueue(new Callback<Mesas>() {
                @Override
                public void onResponse(Call<Mesas> call, Response<Mesas> response) {
                    if (response.isSuccessful()) {
                        Log.d("onResponse mesas", "Mesa seleccionada -> " + mesaSeleccionada.numeroMesa);

                        Intent intent = new Intent(SeleccionMesa.this, Carta.class);
                        intent.putExtra("mesaSeleccionada", mesaSeleccionada);
                        startActivity(intent);
                    }
                }

                @Override
                public void onFailure(Call<Mesas> call, Throwable t) {
                    Log.e("onFailure mesas", "Error al ocupar la mesa", t);
                }
            });

        }
    }

    @Override
    public void onResponse(Call<List<Mesas>> call, Response<List<Mesas>> response) {
        if (response.isSuccessful()) {
            listaMesas = response.body();
            if (listaMesas != null && !listaMesas.isEmpty()) {
                Log.d("onResponse mesas", "Tamaño de la lista de mesas -> " + listaMesas.size());

                mesa1.setEnabled(true);
                mesa2.setEnabled(true);
                mesa3.setEnabled(true);
                mesa4.setEnabled(true);
                mesa5.setEnabled(true);
            }
        }
    }

    @Override
    public void onFailure(Call<List<Mesas>> call, Throwable t) {
        Log.e("onFailure mesas", "Error al obtener mesas", t);
    }
}