package com.example.oishisishi;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
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

    public void alertaConfirmarCuenta(View view) {
        AlertDialog alertDialog = new AlertDialog.Builder(Cuenta.this).create();
        alertDialog.setTitle("Confirmacion de pago");
        alertDialog.setIcon(R.drawable.logo_le_upscale_balanced_x4_1);
        alertDialog.setMessage("¿Quieres pagar la cuenta y terminar de comer?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Aceptar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        terminarComida();
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public void alertaDespedida() {
        AlertDialog alertDialog = new AlertDialog.Builder(Cuenta.this).create();
        alertDialog.setTitle("¡Hasta pronto!");
        alertDialog.setIcon(R.drawable.logo_le_upscale_balanced_x4_1);
        alertDialog.setMessage("Esperamos que hayas disfrutado de tu comida en Oishii, ¡vuelve pronto!");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Cerrar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Cuenta.this, MainActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public void terminarComida() {
        platosCuenta.clear();
        mesaSeleccionada.ocupadaMesa = false;
        Call<Void> call = ApiAdapter.getApiService().deleteComandas(mesaSeleccionada.numeroMesa);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("onResponse cuenta", "Comandas borradas para la mesa -> " + mesaSeleccionada.numeroMesa);
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("onFailure cuenta", "Error al borrar comandas", t);
            }
        });
        Call<Mesas> callMesas = ApiAdapter.getApiService().updateMesas(mesaSeleccionada.numeroMesa, mesaSeleccionada);
        callMesas.enqueue(new Callback<Mesas>() {
            @Override
            public void onResponse(Call<Mesas> callMesas, Response<Mesas> response) {
                if (response.isSuccessful()) {
                    Log.d("onResponse cuenta", "Comandas eliminadas para la mesa -> " + mesaSeleccionada.numeroMesa);
                }
            }

            @Override
            public void onFailure(Call<Mesas> callMesas, Throwable t) {
                Log.e("onFailure cuenta", "Error al borrar comandas", t);
            }
        });
        alertaDespedida();
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
