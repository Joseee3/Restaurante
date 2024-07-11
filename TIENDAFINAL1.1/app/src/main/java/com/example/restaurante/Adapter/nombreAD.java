package com.example.restaurante.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurante.Entities.Nombre;
import com.example.restaurante.R;

import java.util.List;

public class nombreAD extends RecyclerView.Adapter<nombreAD.NombreViewHolder> {
    private List<Nombre> nombres;

    public nombreAD(List<Nombre> nombres) {
        this.nombres = nombres;
    }

    @Override
    public NombreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nombre, parent, false);
        return new NombreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NombreViewHolder holder, int position) {
        Nombre nombre = nombres.get(position);
        holder.nombreTextView.setText(nombre.getNombre());
    }

    @Override
    public int getItemCount() {
        return nombres.size();
    }

    public static class NombreViewHolder extends RecyclerView.ViewHolder {
        public TextView nombreTextView;

        public NombreViewHolder(View view) {
            super(view);
            nombreTextView = view.findViewById(R.id.nombre);
        }
    }
}