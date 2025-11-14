package com.example.oishisishi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oishisishi.adaptadores.ApiAdapter;
import com.example.oishisishi.adaptadores.ContenedorCuentaAdaptador;
import com.example.oishisishi.entidades.Comandas;
import com.example.oishisishi.entidades.Mesas;
import com.example.oishisishi.entidades.Platos;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Cuenta extends AppCompatActivity {
    ImageView botonAtras;
    Mesas mesaSeleccionada;
    ContenedorCuentaAdaptador adaptador;
    RecyclerView contenedorPlatosCuenta;
    TextView subTotalCuenta, ivaCuenta, totalCuenta;

    List<Platos> platosCuenta = new ArrayList<>();

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        mesaSeleccionada = (Mesas) getIntent().getSerializableExtra("mesaSeleccionada");

        if (mesaSeleccionada == null) {
            Log.e("onCreate Cuenta", "mesaSeleccionada es null!");
        }
        setContentView(R.layout.ver_cuenta);

        botonAtras = findViewById(R.id.atras);
        contenedorPlatosCuenta = findViewById(R.id.contenedorPlatosCuenta);
        subTotalCuenta = findViewById(R.id.subTotalCuenta);
        ivaCuenta = findViewById(R.id.ivaCuenta);
        totalCuenta = findViewById(R.id.totalCuenta);
        contenedorPlatosCuenta.setLayoutManager((new LinearLayoutManager(this)));
        contenedorPlatosCuenta.setHasFixedSize(true);

        cargarComandasDeMesa();

        botonAtras.setOnClickListener(v -> {
            Intent intent = new Intent(Cuenta.this, Carta.class);
            intent.putExtra("mesaSeleccionada", mesaSeleccionada);
            startActivity(intent);
        });
    }

    public void precioRellenarDatos() {
        DecimalFormat df = new DecimalFormat("#.##");
        double subTotal = 0;
        double IVA = 0;
        double total = 0;
        for (Platos p : platosCuenta) {
            subTotal += p.precioPlato;
        }
        IVA = Double.parseDouble(df.format((subTotal * (0.10))));
        total = Double.parseDouble(df.format(subTotal + IVA));
        subTotal = Double.parseDouble(df.format(subTotal));

        subTotalCuenta.setText(subTotal + "€");
        ivaCuenta.setText(IVA + "€");
        totalCuenta.setText(total + "€");
    }

    private void cargarComandasDeMesa() {
        ApiAdapter.getApiService().getComandas().enqueue(new Callback<List<Comandas>>() {
            @Override
            public void onResponse(Call<List<Comandas>> call, Response<List<Comandas>> response) {
                if (!response.isSuccessful() || response.body() == null) return;

                List<Comandas> comandas = response.body();

                platosCuenta.clear();

                for (Comandas c : comandas) {
                    if (c.numeroMesa == mesaSeleccionada.numeroMesa) {
                        platosCuenta.addAll(c.pedidoPlatos);
                    }
                }

                adaptador = new ContenedorCuentaAdaptador(platosCuenta);
                contenedorPlatosCuenta.setAdapter(adaptador);

                precioRellenarDatos();
            }

            @Override
            public void onFailure(Call<List<Comandas>> call, Throwable t) {
                Log.e("Cuenta", "Error al obtener comandas", t);
            }
        });
    }
}
