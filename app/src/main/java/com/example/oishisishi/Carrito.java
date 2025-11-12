package com.example.oishisishi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oishisishi.adaptadores.ApiAdapter;
import com.example.oishisishi.adaptadores.ContenedorCarritoAdaptador;
import com.example.oishisishi.entidades.Comandas;
import com.example.oishisishi.entidades.Mesas;
import com.example.oishisishi.entidades.Platos;

import java.text.DecimalFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Carrito extends AppCompatActivity {
    ImageView botonAtras;
    Mesas mesaSeleccionada;
    ContenedorCarritoAdaptador adaptador;
    RecyclerView contenedorPlatosCarrito;
    TextView subTotalCarrito, ivaCarrito, totalCarrito;

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
        contenedorPlatosCarrito = findViewById(R.id.contenedorPlatosCarrito);
        subTotalCarrito = findViewById(R.id.subTotalCarrito);
        ivaCarrito = findViewById(R.id.ivaCarrito);
        totalCarrito = findViewById(R.id.totalCarrito);
        contenedorPlatosCarrito.setLayoutManager((new LinearLayoutManager(this)));
        contenedorPlatosCarrito.setHasFixedSize(true);

        precioRellenarDatos();

        botonAtras.setOnClickListener(v -> {
            Intent intent = new Intent(Carrito.this, Carta.class);
            intent.putExtra("mesaSeleccionada", mesaSeleccionada);
            startActivity(intent);
        });

        adaptador = new ContenedorCarritoAdaptador(
                mesaSeleccionada.carritoMesa,
                new ContenedorCarritoAdaptador.OnPlatoEliminarListener() {
                    @Override
                    public void onPlatoEliminar(Platos plato) {
                        eliminarPlato(plato);
                    }
                }
        );
        contenedorPlatosCarrito.setAdapter(adaptador);
    }

    public void botonPedirCarrito(View view) {
        ArrayList<Platos> copiaCarrito = new ArrayList<>(mesaSeleccionada.carritoMesa);
        Comandas nuevaComanda = new Comandas(mesaSeleccionada.numeroMesa, copiaCarrito, false, false);
        mesaSeleccionada.carritoMesa.clear();
        Call<Comandas> call = ApiAdapter.getApiService().createComanda(nuevaComanda);
        call.enqueue(new Callback<Comandas>() {
            @Override
            public void onResponse(Call<Comandas> call, Response<Comandas> response) {
                if (response.isSuccessful()) {
                    Log.d("onResponse carrito", "Comanda creada para la mesa -> " + nuevaComanda.numeroMesa);
                    Intent intent = new Intent(Carrito.this, Carta.class);
                    intent.putExtra("mesaSeleccionada", mesaSeleccionada);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Comandas> call, Throwable t) {
                Log.e("onFailure carrito", "Error al crear comanda", t);
            }
        });
        Call<Mesas> callMesas = ApiAdapter.getApiService().updateMesas(mesaSeleccionada.numeroMesa, mesaSeleccionada);
        callMesas.enqueue(new Callback<Mesas>() {
            @Override
            public void onResponse(Call<Mesas> callMesas, Response<Mesas> response) {
                if (response.isSuccessful()) {
                    Log.d("onResponse carrito", "Carrito borrado -> " + mesaSeleccionada.numeroMesa);
                }
            }

            @Override
            public void onFailure(Call<Mesas> callMesas, Throwable t) {
                Log.e("onFailure carrito", "Error al borrar carrito", t);
            }
        });
    }

    public void eliminarPlato(Platos plato) {
        mesaSeleccionada.carritoMesa.remove(plato);
        adaptador.notifyItemRemoved(mesaSeleccionada.carritoMesa.indexOf(plato));
        precioRellenarDatos();
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

    public void precioRellenarDatos() {
        DecimalFormat df = new DecimalFormat("#.##");
        double subTotal = 0;
        double IVA = 0;
        double total = 0;
        for(int i = 0; i < mesaSeleccionada.carritoMesa.size(); i++) {
            subTotal += mesaSeleccionada.carritoMesa.get(i).precioPlato;
        }
        IVA = Double.parseDouble(df.format((subTotal * (0.10))));
        total = Double.parseDouble(df.format(subTotal + IVA));
        subTotal = Double.parseDouble(df.format(subTotal));

        subTotalCarrito.setText(subTotal + "€");
        ivaCarrito.setText(IVA + "€");
        totalCarrito.setText(total + "€");
    }
}
