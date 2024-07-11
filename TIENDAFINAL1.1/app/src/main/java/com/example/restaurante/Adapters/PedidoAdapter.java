package com.example.restaurante.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurante.DetailHistorialActivity;
import com.example.restaurante.Entities.Pedido;
import com.example.restaurante.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.PedidoViewHolder> {
    private Context mCtx;
    private List<Pedido> pedidoList;

    public PedidoAdapter(Context mCtx, List<Pedido> pedidoList) {
        this.mCtx = mCtx;
        this.pedidoList = pedidoList;
    }

    public void setPedidoList(List<Pedido> pedidoList) {
        this.pedidoList = pedidoList;
    }

    @NonNull
    @Override
    public PedidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.item_historial, parent, false);
        return new PedidoViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull PedidoViewHolder holder, int position) {
        Pedido pedido = pedidoList.get(position);
        Log.d("GET_ORDER", pedido.toString());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = "";
        if (pedido.getDate() != null) {
            formattedDate = sdf.format(pedido.getDate());
        } else {
            // Si la fecha es null, establecer la fecha actual
            pedido.setDate(new Date());
            formattedDate = sdf.format(pedido.getDate());
        }

        holder.textViewFecha.setText(formattedDate);
        holder.textViewTotal.setText(String.valueOf(pedido.getTotal()));

        // Here we open the detail view
        Intent intent = new Intent(this.mCtx, DetailHistorialActivity.class);
        intent.putExtra("orderId", pedido.getId());
        holder.btnDetail.setOnClickListener(v -> {
            this.mCtx.startActivity(intent);
            Log.d("GET_ORDER", "Sending ID: " + pedido.getId());
        });
    }


    @Override
    public int getItemCount() {
        return pedidoList.size();
    }

    class PedidoViewHolder extends RecyclerView.ViewHolder {
        TextView textViewFecha, textViewTotal;
        Button btnDetail;

        public PedidoViewHolder(View itemView) {
            super(itemView);

            textViewFecha = itemView.findViewById(R.id.tvFecha);
            textViewTotal = itemView.findViewById(R.id.tvTotalPrice);
            btnDetail = itemView.findViewById(R.id.btnDetail);
        }
    }
}