package com.example.oishisishi;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.oishisishi.adaptadores.ApiAdapter;
import com.example.oishisishi.entidades.Mesas;

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
                disponibilidadMesas();
                mesa1.setImageResource(R.drawable.mesa1seleccionada);
                mesaSeleccionada = mesaUno;
            }
            else{
                alertaMesaOcupada();
            }
        });

        mesa2.setOnClickListener(v -> {
            Mesas mesaDos = listaMesas.get(1);
            if(!mesaDos.ocupadaMesa){
                disponibilidadMesas();
                mesa2.setImageResource(R.drawable.mesa2seleccionada);
                mesaSeleccionada = mesaDos;
            }
            else{
                alertaMesaOcupada();
            }
        });

        mesa3.setOnClickListener(v -> {
            Mesas mesaTres = listaMesas.get(2);
            if (!mesaTres.ocupadaMesa) {
                disponibilidadMesas();
                mesa3.setImageResource(R.drawable.mesa3seleccionada);
                mesaSeleccionada = mesaTres;
            }
            else {
                alertaMesaOcupada();
            }
        });

        mesa4.setOnClickListener(v -> {
            Mesas mesaCuatro = listaMesas.get(3);
            if (!mesaCuatro.ocupadaMesa) {
                disponibilidadMesas();
                mesa4.setImageResource(R.drawable.mesa4seleccionada);
                mesaSeleccionada = mesaCuatro;
            }
            else {
                alertaMesaOcupada();
            }
        });

        mesa5.setOnClickListener(v -> {
            Mesas mesaCinco = listaMesas.get(4);
            if (!mesaCinco.ocupadaMesa) {
                disponibilidadMesas();
                mesa5.setImageResource(R.drawable.mesa5seleccionada);
                mesaSeleccionada = mesaCinco;
            }
            else {
                alertaMesaOcupada();
            }
        });
    }

    public void alertaMesaOcupada() {
        AlertDialog alertDialog = new AlertDialog.Builder(SeleccionMesa.this).create();
        alertDialog.setTitle("¡Mesa ocupada!");
        alertDialog.setIcon(R.drawable.logo_le_upscale_balanced_x4_1);
        alertDialog.setMessage("Esta mesa está ocupada, escoge otra");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Aceptar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public void disponibilidadMesas() {
        for (Mesas mesa : listaMesas) {
            switch (mesa.numeroMesa) {
                case 1:
                    if(mesa.ocupadaMesa) {
                        mesa1.setImageResource(R.drawable.mesa1ocupada);
                    }
                    else{
                        mesa1.setImageResource(R.drawable.mesa1);
                    }
                    break;
                case 2:
                    if(mesa.ocupadaMesa) {
                        mesa2.setImageResource(R.drawable.mesa2ocupada);
                    }
                    else{
                        mesa2.setImageResource(R.drawable.mesa2);
                    }
                    break;
                case 3:
                    if(mesa.ocupadaMesa) {
                        mesa3.setImageResource(R.drawable.mesa3ocupada);
                    }
                    else{
                        mesa3.setImageResource(R.drawable.mesa3);
                    }
                    break;
                case 4:
                    if(mesa.ocupadaMesa) {
                        mesa4.setImageResource(R.drawable.mesa4ocupada);
                    }
                    else{
                        mesa4.setImageResource(R.drawable.mesa4);
                    }
                    break;
                case 5:
                    if(mesa.ocupadaMesa) {
                        mesa5.setImageResource(R.drawable.mesa5ocupada);
                    }
                    else{
                        mesa5.setImageResource(R.drawable.mesa5);
                    }
                    break;
                default:
                    break;
            }
        }
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
                    Log.e("onFailure mesas", "Error al ocupar la mesa.", t);
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
        disponibilidadMesas();
    }

    @Override
    public void onFailure(Call<List<Mesas>> call, Throwable t) {
        Log.e("onFailure mesas", "Error al obtener mesas", t);
    }
}