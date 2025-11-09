package com.example.oishisishi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oishisishi.adaptadores.ApiAdapter;
import com.example.oishisishi.adaptadores.ContenedorPlatosAdaptador;
import com.example.oishisishi.entidades.Mesas;
import com.example.oishisishi.entidades.Platos;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Carta extends AppCompatActivity implements Callback<List<Platos>> {
    ImageView botonAtras, botonCarrito;
    List<Platos> listaPlatos;
    Mesas mesaSeleccionada;
    RecyclerView contenedorPlatos;
    ContenedorPlatosAdaptador adaptador;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.carta);

        mesaSeleccionada = (Mesas) getIntent().getSerializableExtra("mesaSeleccionada");

        botonAtras = findViewById(R.id.atras);
        botonCarrito = findViewById(R.id.carrito);
        contenedorPlatos = findViewById(R.id.contenedorPlatos);
        contenedorPlatos.setHasFixedSize(true);
        contenedorPlatos.setLayoutManager((new LinearLayoutManager(this)));

        Call<List<Platos>> call = ApiAdapter.getApiService().getPlatos();
        call.enqueue(this);

        botonAtras.setOnClickListener(v -> {
            Intent intent = new Intent(Carta.this, SeleccionMesa.class);
            startActivity(intent);
        });

        botonCarrito.setOnClickListener(v -> {
            Intent intent = new Intent(Carta.this, CarritoVacio.class);
            startActivity(intent);
        });
    }

    @Override
    public void onResponse(Call<List<Platos>> call, Response<List<Platos>> response) {
        if (response.isSuccessful()) {
            listaPlatos = response.body();

            if (listaPlatos != null && !listaPlatos.isEmpty()) {
                Log.d("onResponse platos", "Tamaño de la lista de platos -> " + listaPlatos.size());

                adaptador = new ContenedorPlatosAdaptador(listaPlatos);
                contenedorPlatos.setAdapter(adaptador);
            } else {
                Log.w("onResponse platos", "Lista de platos vacia");
            }
        }
    }

    @Override
    public void onFailure(Call<List<Platos>> call, Throwable t) {
        Log.e("onFailure platos", "Error al obtener platos", t);
    }

    public void mostrarPopup(Platos plato) {
        final int[] cantidad = {0};

        ConstraintLayout oscurecerFondo = findViewById(R.id.oscurecerFondo);
        ImageView imagenPlatoSelector = findViewById(R.id.imagenPlatoSelector);
        TextView nombrePlatoSelector = findViewById(R.id.nombrePlatoSelector3);
        TextView unidadesPlatoSelector = findViewById(R.id.unidadesPlatoSelector);
        TextView precioPlatoSelector = findViewById(R.id.precioPlatoSelector);
        TextView cantidadPlatoSelector = findViewById(R.id.cantidadPlatoSelector);
        ImageView botonMasPlatoSelector = findViewById(R.id.botonMasPlatoSeleccion);
        ImageView botonMenosPlatoSelector = findViewById(R.id.botonMenosPlatoSelector);
        ImageView botonCerrarPlatoSelector = findViewById(R.id.botonCerrar);
        Button botonAñadirPlatoSelector = findViewById(R.id.botonAddPlatoSeleccion);

        oscurecerFondo.setVisibility(View.VISIBLE);
        nombrePlatoSelector.setText(plato.nombrePlato);
        unidadesPlatoSelector.setText(plato.unidadesPlato + "Uds.");
        precioPlatoSelector.setText(plato.precioPlato + "€");
        cantidadPlatoSelector.setText(String.valueOf(cantidad[0]));

        switch (plato.nombrePlato) {
            case "Nigiri de Gambas":
                imagenPlatoSelector.setImageResource(R.drawable.sushi_nigiri_gamba);
                break;
            case "Nigiri de Atún":
                imagenPlatoSelector.setImageResource(R.drawable.sushi_nigiri_atun);
                break;
            case "Nigiri de Salmón":
                imagenPlatoSelector.setImageResource(R.drawable.sushi_nigiri_salmon);
                break;
            case "Hosomaki":
                imagenPlatoSelector.setImageResource(R.drawable.roll_hosomaki);
                break;
            case "Futomaki":
                imagenPlatoSelector.setImageResource(R.drawable.roll_futomaki);
                break;
            case "Ramen Miso":
                imagenPlatoSelector.setImageResource(R.drawable.ramen_miso);
                break;
        }

        botonMasPlatoSelector.setOnClickListener(v -> {
            cantidad[0]++;
            cantidadPlatoSelector.setText(String.valueOf(cantidad[0]));
        });

        botonMenosPlatoSelector.setOnClickListener(v -> {
            if(cantidad[0] > 0) {
                cantidad[0]--;
            }
            else{
                cantidad[0] = 0;
            }
            cantidadPlatoSelector.setText(String.valueOf(cantidad[0]));
        });

        botonCerrarPlatoSelector.setOnClickListener(v -> {
            oscurecerFondo.setVisibility(View.INVISIBLE);
        });

        botonAñadirPlatoSelector.setOnClickListener(v -> {
            oscurecerFondo.setVisibility(View.INVISIBLE);
            // Añadir plato a cesta + icono cesta con platos
        });
    }
}
