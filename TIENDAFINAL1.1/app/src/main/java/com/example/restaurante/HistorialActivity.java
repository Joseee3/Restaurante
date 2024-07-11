package com.example.restaurante;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurante.Adapters.PedidoAdapter;
import com.example.restaurante.Entities.Pedido;
import com.example.restaurante.service.respository.OrderRepository;

import java.util.ArrayList;
import java.util.List;

public class HistorialActivity extends AppCompatActivity {

    private PedidoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        // Recycler view setup
        RecyclerView rvHistory = findViewById(R.id.rvHistorial);
        rvHistory.setLayoutManager(new LinearLayoutManager(this));

        adapter = new PedidoAdapter(this, new ArrayList<>());
        rvHistory.setAdapter(adapter);

        // Fetch order history
        this.fetchOrders();
    }

    private void fetchOrders() {
        OrderRepository.getUserOrders(new OrderRepository.CallbackGetOrders() {
            @Override
            public void onSuccess(List<Pedido> orders) {
                if (!orders.isEmpty()) {
                    // Update adapter dataset
                    adapter.setPedidoList(orders);
                    adapter.notifyDataSetChanged();
                } else {
                    createAlert(
                            "OPERACIÓN EXITOSA",
                            "Historial vacío",
                            R.drawable.icon_success
                    ).show();
                }
            }

            @Override
            public void onFailure(String msg) {
                createAlert("ERROR", msg, R.drawable.icon_error).show();
            }
        });
    }

    private AlertDialog createAlert(String title, String msg, int icon) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setIcon(icon);
        builder.setPositiveButton("Ok", ((dialog, which) -> {
        }));
        return builder.create();
    }

}

