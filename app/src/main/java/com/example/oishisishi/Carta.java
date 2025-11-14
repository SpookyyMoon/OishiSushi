package com.example.oishisishi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oishisishi.adaptadores.ApiAdapter;
import com.example.oishisishi.adaptadores.ContenedorPlatosAdaptador;
import com.example.oishisishi.entidades.Comandas;
import com.example.oishisishi.entidades.Mesas;
import com.example.oishisishi.entidades.Platos;

import java.util.ArrayList;
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
    FrameLayout circuloNumeroCarrito;
    TextView numeroItems;
    Button botonVerCuenta;

    @Override
    protected void onResume() {
        super.onResume();
        if (mesaSeleccionada.carritoMesa == null) {
            mesaSeleccionada.carritoMesa = new ArrayList<>();
        }
        else if(mesaSeleccionada.carritoMesa.size() == 0) {
            circuloNumeroCarrito.setVisibility(View.INVISIBLE);
            numeroItems.setVisibility(View.INVISIBLE);
        }
        else {
            circuloNumeroCarrito.setVisibility(View.VISIBLE);
            numeroItems.setVisibility(View.VISIBLE);
            numeroItems.setText(String.valueOf(mesaSeleccionada.carritoMesa.size()));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.carta);

        mesaSeleccionada = (Mesas) getIntent().getSerializableExtra("mesaSeleccionada");
        if (mesaSeleccionada == null) {
            Log.e("onCreate Carta", "mesaSeleccionada es null!");
        }
        if (mesaSeleccionada.carritoMesa == null) {
            mesaSeleccionada.carritoMesa = new ArrayList<>();
        }

        botonVerCuenta = findViewById(R.id.botonVerCuenta);
        botonAtras = findViewById(R.id.atras);
        botonCarrito = findViewById(R.id.carrito);
        circuloNumeroCarrito = findViewById(R.id.circuloNumeroCarrito);
        numeroItems = findViewById(R.id.numeroItems);
        contenedorPlatos = findViewById(R.id.contenedorPlatos);
        contenedorPlatos.setHasFixedSize(true);
        contenedorPlatos.setLayoutManager((new LinearLayoutManager(this)));

        verificarComandaDeMesa();

        Call<List<Platos>> call = ApiAdapter.getApiService().getPlatos();
        call.enqueue(this);

        botonAtras.setOnClickListener(v -> {
            mesaSeleccionada.ocupadaMesa = false;
            Call<Mesas> callAtras = ApiAdapter.getApiService().updateMesas(mesaSeleccionada.numeroMesa, mesaSeleccionada);
            callAtras.enqueue(new Callback<Mesas>() {
                @Override
                public void onResponse(Call<Mesas> call, Response<Mesas> response) {
                    if (response.isSuccessful()) {
                        Log.d("onResponse mesas", "Mesa liberada -> " + mesaSeleccionada.numeroMesa);

                        Intent intent = new Intent(Carta.this, SeleccionMesa.class);
                        startActivity(intent);
                    }
                }

                @Override
                public void onFailure(Call<Mesas> call, Throwable t) {
                    Log.e("onFailure mesas", "Error al liberar la mesa", t);
                }
            });
        });

        botonCarrito.setOnClickListener(v -> {
            if (mesaSeleccionada.carritoMesa == null || mesaSeleccionada.carritoMesa.isEmpty()) {
                Intent intent = new Intent(Carta.this, CarritoVacio.class);
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(Carta.this, Carrito.class);
                intent.putExtra("mesaSeleccionada", mesaSeleccionada);
                startActivity(intent);
            }
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

    public void botonVerCuenta(View view) {
        Intent intent = new Intent(Carta.this, Cuenta.class);
        intent.putExtra("mesaSeleccionada", mesaSeleccionada);
        startActivity(intent);
    }

    public void mostrarPopup(Platos plato) {
        final int[] cantidad = {0};

        ConstraintLayout oscurecerFondo = findViewById(R.id.oscurecerFondo);
        ImageView imagenPlatoSelector = findViewById(R.id.imagenPlatoSelector);
        TextView nombrePlatoSelector = findViewById(R.id.nombrePlatoSelector);
        TextView unidadesPlatoSelector = findViewById(R.id.unidadesPlatoSelector);
        TextView precioPlatoSelector = findViewById(R.id.precioPlatoSelector);
        TextView cantidadPlatoSelector = findViewById(R.id.cantidadPlatoSelector);
        ImageView botonMasPlatoSelector = findViewById(R.id.botonMasPlatoSeleccion);
        ImageView botonMenosPlatoSelector = findViewById(R.id.botonMenosPlatoSelector);
        ImageView botonCerrarPlatoSelector = findViewById(R.id.botonCerrar);
        Button botonAddPlatoSelector = findViewById(R.id.botonAddPlatoSeleccion);

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

        botonAddPlatoSelector.setOnClickListener(v -> {
            oscurecerFondo.setVisibility(View.INVISIBLE);
            for (int i = 0; i < cantidad[0]; i++) {
                mesaSeleccionada.carritoMesa.add(plato);
            }

            circuloNumeroCarrito.setVisibility(View.VISIBLE);
            numeroItems.setVisibility(View.VISIBLE);
            numeroItems.setText(String.valueOf(mesaSeleccionada.carritoMesa.size()));

            Call<Mesas> call = ApiAdapter.getApiService().updateMesas(mesaSeleccionada.numeroMesa, mesaSeleccionada);
            call.enqueue(new Callback<Mesas>() {
                @Override
                public void onResponse(Call<Mesas> call, Response<Mesas> response) {
                    if (response.isSuccessful()) {
                        Log.d("onResponse mesas", "Plato añadido al carrito -> " + plato.nombrePlato);
                    }
                }

                @Override
                public void onFailure(Call<Mesas> call, Throwable t) {
                    Log.e("onFailure mesas", "Error al añadir plato", t);
                }
            });
        });
    }

    private void verificarComandaDeMesa() {

        ApiAdapter.getApiService().getComandas().enqueue(new Callback<List<Comandas>>() {
            @Override
            public void onResponse(Call<List<Comandas>> call, Response<List<Comandas>> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    botonAtras.setVisibility(View.VISIBLE);
                    botonVerCuenta.setVisibility(View.GONE);
                    return;
                }

                List<Comandas> comandas = response.body();

                boolean mesaTieneComandaAtendida = false;

                for (Comandas c : comandas) {
                    if (c.numeroMesa == mesaSeleccionada.numeroMesa && c.atendidaComanda) {
                        mesaTieneComandaAtendida = true;
                        break;
                    }
                }

                if (mesaTieneComandaAtendida) {
                    botonAtras.setVisibility(View.GONE);
                    botonVerCuenta.setVisibility(View.VISIBLE);
                } else {
                    botonAtras.setVisibility(View.VISIBLE);
                    botonVerCuenta.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<Comandas>> call, Throwable t) {
                botonAtras.setVisibility(View.VISIBLE);
                botonVerCuenta.setVisibility(View.GONE);
            }
        });
    }
}
