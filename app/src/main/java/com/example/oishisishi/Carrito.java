package com.example.oishisishi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oishisishi.adaptadores.ApiAdapter;
import com.example.oishisishi.adaptadores.ContenedorCarritoAdaptador;
import com.example.oishisishi.adaptadores.ContenedorPlatosAdaptador;
import com.example.oishisishi.entidades.Mesas;
import com.example.oishisishi.entidades.Platos;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Carrito extends AppCompatActivity {
    ImageView botonAtras;
    Mesas mesaSeleccionada;
    ContenedorCarritoAdaptador adaptador;
    RecyclerView contenedorPlatosCarrito;
    ImageView botonEliminarCarrito;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        mesaSeleccionada = (Mesas) getIntent().getSerializableExtra("mesaSeleccionada");

        if (mesaSeleccionada.carritoMesa == null || mesaSeleccionada.carritoMesa.size() == 0) {
            mesaSeleccionada.carritoMesa = new ArrayList<>();
        }

        if (mesaSeleccionada == null) {
            Log.e("onCreate Carrito", "mesaSeleccionada es null!");
        }
        setContentView(R.layout.carrito);

        botonAtras = findViewById(R.id.atras);
        botonEliminarCarrito.findViewById(R.id.botonEliminarCarrito);
        contenedorPlatosCarrito = findViewById(R.id.contenedorPlatosCarrito);
        contenedorPlatosCarrito.setLayoutManager((new LinearLayoutManager(this)));
        contenedorPlatosCarrito.setHasFixedSize(true);


        botonAtras.setOnClickListener(v -> {
            Intent intent = new Intent(Carrito.this, Carta.class);
            intent.putExtra("mesaSeleccionada", mesaSeleccionada);
            startActivity(intent);
        });

        adaptador = new ContenedorCarritoAdaptador(mesaSeleccionada.carritoMesa);
        contenedorPlatosCarrito.setAdapter(adaptador);
    }

    public void eliminarPlato(Platos plato) {
        mesaSeleccionada.carritoMesa.remove(plato);
        Call<Mesas> call = ApiAdapter.getApiService().updateMesas(mesaSeleccionada.numeroMesa, mesaSeleccionada);
        call.enqueue(new Callback<Mesas>() {
            @Override
            public void onResponse(Call<Mesas> call, Response<Mesas> response) {
                if (response.isSuccessful()) {
                    Log.d("onResponse carrito", "Plato eliminado del carrito -> " + plato.nombrePlato);
                }
            }

            @Override
            public void onFailure(Call<Mesas> call, Throwable t) {
                Log.e("onFailure carrito", "Error al eliminar plato", t);
            }
        });
    }
}
