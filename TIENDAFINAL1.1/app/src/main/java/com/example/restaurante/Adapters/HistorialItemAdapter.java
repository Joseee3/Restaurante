package com.example.restaurante.Adapters;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurante.DetailHistorialActivity;
import com.example.restaurante.R;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

public class HistorialItemAdapter extends RecyclerView.Adapter<HistorialItemAdapter.HistorialItemViewHolder> {

    private List<QueryDocumentSnapshot> pedidos;

    public interface ItemClickListener {
    void onItemClick(int position);
}
    private ItemClickListener listener;

    public HistorialItemAdapter(List<QueryDocumentSnapshot> pedidos, ItemClickListener listener) {
        this.pedidos = pedidos;
        this.listener = listener;
    }



    @NonNull
    @Override
    public HistorialItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_historial, parent, false);
//        Button btnVerDetalle = view.findViewById(R.id.btnDetail);
//        btnVerDetalle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(v.getContext(), "Botón clickeado", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(v.getContext(), DetailHistorialActivity.class);
//                v.getContext().startActivity(intent);
//            }
//        });
        return new HistorialItemViewHolder(view);
    }

    @Override
public void onBindViewHolder(@NonNull HistorialItemViewHolder holder, int position) {
    QueryDocumentSnapshot pedido = pedidos.get(position);

//    // Aquí puedes actualizar las vistas del ViewHolder con la información del pedido
//    //String orderId = pedido.getId();
//    String orderDate = pedido.getString("fecha");
//    Double orderTotal = pedido.getDouble("total");
//    String orderItems = pedido.getString("productos");
//
//    String ordenid = pedido.getString("idpedido");
//
//    Log.d("HistorialItemAdapter", "Pedido ID: " + ordenid);
//
//  holder.btnVerDetalle.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View v) {
//        Toast.makeText(v.getContext(), "Botón clickeado", Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(v.getContext(), DetailHistorialActivity.class);
//        intent.putExtra("pedidoId", pedido.getString("idpedido"));
//        v.getContext().startActivity(intent);
//    }
//});

        holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if (listener != null) {
            int position = holder.getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position);
            }
        }
    }
});
        String ordenid = pedido.getString("idpedido");
        String orderDate = pedido.getString("fecha");
        Double orderTotal = pedido.getDouble("total");
        String orderItems = pedido.getString("productos");

        Log.d("HistorialItemAdapter", "Pedido ID: " + ordenid);
        holder.btnVerDetalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí puedes agregar el código para crear y configurar tu objeto Carrito

                // Crear una intención para iniciar HistorialDetailActivity
                Intent intent = new Intent(v.getContext(), DetailHistorialActivity.class);

                // Aquí puedes agregar cualquier dato extra que quieras pasar a HistorialDetailActivity
                // Por ejemplo, si quieres pasar el objeto Carrito, puedes convertirlo a un string JSON y luego ponerlo como extra
                // String carritoJson = new Gson().toJson(carritoItem);
                // intent.putExtra("carrito", carritoJson);

                // Iniciar la actividad
                v.getContext().startActivity(intent);
            }
        });


    holder.tvOrderId.setText(ordenid);
    holder.tvOrderDate.setText(orderDate);
    holder.tvOrderTotal.setText(String.valueOf(orderTotal));
    holder.tvOrderItems.setText(orderItems);
}

    @Override
    public int getItemCount() {
        return pedidos.size();
    }

    static class HistorialItemViewHolder extends RecyclerView.ViewHolder {

        TextView tvOrderId;
        TextView tvOrderDate;
        TextView tvOrderTotal;
        TextView tvOrderItems;
        Button btnVerDetalle;

        public HistorialItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvOrderTotal = itemView.findViewById(R.id.tvOrderTotal);
            tvOrderItems = itemView.findViewById(R.id.tvOrderItems);
            btnVerDetalle = itemView.findViewById(R.id.btnDetail);
        }
    }
}