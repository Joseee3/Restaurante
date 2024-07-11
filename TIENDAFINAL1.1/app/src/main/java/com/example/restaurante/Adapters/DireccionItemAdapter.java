package com.example.restaurante.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurante.Direccionesuser;
import com.example.restaurante.Entities.Direccion;
import com.example.restaurante.R;

import java.util.List;
//import com.google.firestore.FirestoreRecyclerAdapter;

public class DireccionItemAdapter extends RecyclerView.Adapter<DireccionItemAdapter.DireccionViewHolder> {

    private List<Direccion> direcciones; // Lista de direcciones
    private Direccionesuser context; // Contexto de la aplicación

    public DireccionItemAdapter(List<Direccion> direcciones, Direccionesuser context) {
        this.direcciones = direcciones;
        this.context = context;
    }

    public void setDirecciones(List<Direccion> direcciones) {
        this.direcciones = direcciones;
    }

    @NonNull
    @Override
    public DireccionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Reemplaza con tu layout
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_direccion, parent,false);

        return new DireccionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DireccionViewHolder holder, int position) {
        Direccion direccion = direcciones.get(position);

        holder.direccionTextView.setText(direccion.getDireccion());
        //holder.nombreDireccionTextView.setText(direccion.getNombredireccion());
        holder.tipoViviendaTextView.setText(direccion.getTipovivienda());
    }

    @Override
    public int getItemCount() {
        return direcciones.size();
    }

    public static class DireccionViewHolder extends RecyclerView.ViewHolder {

        private final TextView direccionTextView;
        //private final TextView nombreDireccionTextView;
        private final TextView tipoViviendaTextView;

        public DireccionViewHolder(@NonNull View itemView) {
            super(itemView);

            direccionTextView = itemView.findViewById(R.id.direccion); // Reemplaza con los ids de tus views
          //  nombreDireccionTextView = itemView.findViewById(R.id.);
            tipoViviendaTextView = itemView.findViewById(R.id.nomdirec);
        }
    }

}
