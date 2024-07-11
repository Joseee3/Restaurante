package com.example.restaurante;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.restaurante.Entities.Carrito;
import com.example.restaurante.Entities.Pedido;
import com.example.restaurante.service.respository.OrderRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


public class DetailHistorialActivity extends AppCompatActivity {

    private final String ORDER_LOG = "GET_ORDER";
    private TextView textViewId;
    private TextView textViewFecha;
    private TextView textViewTotal;
    private TextView textViewItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_historial);

        // Start view components
        this.textViewId = findViewById(R.id.tvOrderId);
        this.textViewFecha = findViewById(R.id.tvOrderDate);
        this.textViewTotal = findViewById(R.id.tvOrderTotal);
        this.textViewItems = findViewById(R.id.tvOrderItems);

        // Get order ID
        String orderId = getIntent().getStringExtra("orderId");

        // Fetch details
        this.fetchDetails(orderId);

    }

    private void fetchDetails(String orderId) {
        OrderRepository.getOrderById(orderId, new OrderRepository.Callback() {
            @Override
            public void onSuccess(Pedido order) {
                displayData(order);
            }

            @Override
            public void onFailure(String msg) {
                createAlert("ERROR", msg, R.drawable.icon_error).show();
            }
        });
    }


    private void displayData(Pedido pedido) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String formattedDate = sdf.format(pedido.getDate());

        String details = this.formatShoppingCart(pedido.getCarritolist());


        this.textViewId.setText(String.format("ID: %s", pedido.getId()));
        this.textViewFecha.setText(String.format("Fecha: %s", formattedDate));
        this.textViewTotal.setText(String.format("Total: S/. %.2f", pedido.getTotal()));
        this.textViewItems.setText(String.format("Detalle: %s", details));

    }

    private String formatShoppingCart(List<Carrito> shoppingCart) {

        List<String> data = new ArrayList<>();
        int total = 0;

        for (Carrito cart : shoppingCart) {
            String tmp  = String.format("%s(%d)", cart.getName(), cart.getCantidad());
            total += cart.getCantidad();
            data.add(tmp);
        }

        data.add(String.format("Total(%d)", total));
        return data.stream().collect(Collectors.joining(", "));
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