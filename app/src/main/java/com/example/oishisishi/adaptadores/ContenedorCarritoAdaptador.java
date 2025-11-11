package com.example.oishisishi.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oishisishi.R;
import com.example.oishisishi.entidades.Platos;

import java.util.List;

public class ContenedorCarritoAdaptador extends RecyclerView.Adapter<ContenedorCarritoAdaptador.PlatoViewHolder> {

    List<Platos> listaPlatos;


    public ContenedorCarritoAdaptador(List<Platos> listaPlatos){
        this.listaPlatos = listaPlatos;
    }

    @NonNull
    @Override
    public ContenedorCarritoAdaptador.PlatoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.plantilla_plato_carrito, parent, false);
        return new ContenedorCarritoAdaptador.PlatoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContenedorCarritoAdaptador.PlatoViewHolder holder, int position) {
        Platos plato = listaPlatos.get(position);

        holder.nombrePlato.setText(plato.nombrePlato);
        holder.precioPlato.setText(plato.precioPlato + "€");
        holder.unidadesPlato.setText(plato.unidadesPlato + "Uds.");
        switch (plato.nombrePlato) {
            case "Nigiri de Gambas":
                holder.imagenPlato.setImageResource((R.drawable.sushi_nigiri_gamba));
                break;
            case "Nigiri de Atún":
                holder.imagenPlato.setImageResource((R.drawable.sushi_nigiri_atun));
                break;
            case "Nigiri de Salmón":
                holder.imagenPlato.setImageResource((R.drawable.sushi_nigiri_salmon));
                break;
            case "Hosomaki":
                holder.imagenPlato.setImageResource((R.drawable.roll_hosomaki));
                break;
            case "Futomaki":
                holder.imagenPlato.setImageResource((R.drawable.roll_futomaki));
                break;
            case "Ramen Miso":
                holder.imagenPlato.setImageResource((R.drawable.ramen_miso));
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return listaPlatos.size();
    }

    public static class PlatoViewHolder extends RecyclerView.ViewHolder {
        ImageView imagenPlato;
        TextView nombrePlato, precioPlato, unidadesPlato;

        public PlatoViewHolder(@NonNull View itemView) {
            super(itemView);
            imagenPlato = itemView.findViewById(R.id.imagenPlato);
            nombrePlato = itemView.findViewById(R.id.nombrePlato);
            precioPlato = itemView.findViewById(R.id.precioPlato);
            unidadesPlato = itemView.findViewById(R.id.unidadesPlato);
        }
    }

}
