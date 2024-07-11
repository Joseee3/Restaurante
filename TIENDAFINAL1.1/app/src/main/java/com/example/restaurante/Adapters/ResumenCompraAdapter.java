package com.example.restaurante.Adapters;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.restaurante.R;

import com.example.restaurante.Entities.Carrito;

import java.util.List;

public class ResumenCompraAdapter extends RecyclerView.Adapter<ResumenCompraAdapter.ResumenCompraViewHolder> {

    private List<Carrito> carritoList;

    public ResumenCompraAdapter(List<Carrito> carritoList) {
        this.carritoList = carritoList;
    }

    @NonNull
    @Override
    public ResumenCompraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_summery, parent, false);
        return new ResumenCompraViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResumenCompraViewHolder holder, int position) {
        Carrito item = carritoList.get(position);

        String productName = item.getName();
        double productPrice = item.getPrice();
        int productQuantity = item.getCantidad();
        String productImage = item.getImage();

        holder.tvProductName.setText(productName);
        holder.tvProductPrice.setText(String.format("S/. %.2f", productPrice));
        holder.tvProductQuantity.setText(String.valueOf(productQuantity));

        Glide.with(holder.itemView.getContext())
                .load(productImage)
                .into(holder.ivProductImage);
    }

    @Override
    public int getItemCount() {
        return carritoList.size();
    }

    static class ResumenCompraViewHolder extends RecyclerView.ViewHolder {

        TextView tvProductName;
        TextView tvProductPrice;
        TextView tvProductQuantity;
        ImageView ivProductImage;

        public ResumenCompraViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvNombreProducto);
            tvProductPrice = itemView.findViewById(R.id.tvPrecioProducto);
            tvProductQuantity = itemView.findViewById(R.id.edtCantidad);
            ivProductImage = itemView.findViewById(R.id.ivplatocarrito);

        }
    }

    public double calculateTotalPrice() {
        double total = 0;
        for (Carrito item : carritoList) {
            total += item.getPrice() * item.getCantidad();
        }
        return total;
    }
}