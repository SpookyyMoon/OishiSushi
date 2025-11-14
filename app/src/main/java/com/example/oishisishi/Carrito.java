package com.example.oishisishi;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Carrito extends AppCompatActivity {
    ImageView botonAtras;
    Mesas mesaSeleccionada;
    ContenedorCarritoAdaptador adaptador;
    RecyclerView contenedorPlatosCarrito;
    TextView subTotalCarrito, ivaCarrito, totalCarrito;
    Button botonVerCuentaCarrito;

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

        botonVerCuentaCarrito = findViewById(R.id.botonVerCuentaCarrito);
        botonAtras = findViewById(R.id.atras);
        contenedorPlatosCarrito = findViewById(R.id.contenedorPlatosCuenta);
        subTotalCarrito = findViewById(R.id.subTotalCuenta);
        ivaCarrito = findViewById(R.id.ivaCuenta);
        totalCarrito = findViewById(R.id.totalCuenta);
        contenedorPlatosCarrito.setLayoutManager((new LinearLayoutManager(this)));
        contenedorPlatosCarrito.setHasFixedSize(true);

        verificarComandaDeMesa();
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

    public void botonVerCuentaCarrito(View view) {
        Intent intent = new Intent(Carrito.this, Cuenta.class);
        intent.putExtra("mesaSeleccionada", mesaSeleccionada);
        startActivity(intent);
    }

    public void alertaConfirmarPedido(View view) {
        AlertDialog alertDialog = new AlertDialog.Builder(Carrito.this).create();
        alertDialog.setTitle("Confirmacion de pedido");
        alertDialog.setIcon(R.drawable.logo_le_upscale_balanced_x4_1);
        alertDialog.setMessage("¿Estás seguro de que quieres confirmar el pedido?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Aceptar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        pedirCarrito();
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

    public void pedirCarrito() {
        ArrayList<Platos> copiaCarrito = new ArrayList<>(mesaSeleccionada.carritoMesa);
        Comandas nuevaComanda = new Comandas(mesaSeleccionada.numeroMesa, copiaCarrito, false);
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

    private void verificarComandaDeMesa() {

        ApiAdapter.getApiService().getComandas().enqueue(new Callback<List<Comandas>>() {
            @Override
            public void onResponse(Call<List<Comandas>> call, Response<List<Comandas>> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    botonVerCuentaCarrito.setVisibility(View.GONE);
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
                    botonVerCuentaCarrito.setVisibility(View.VISIBLE);
                } else {
                    botonVerCuentaCarrito.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<Comandas>> call, Throwable t) {
                botonVerCuentaCarrito.setVisibility(View.GONE);
            }
        });
    }
}
