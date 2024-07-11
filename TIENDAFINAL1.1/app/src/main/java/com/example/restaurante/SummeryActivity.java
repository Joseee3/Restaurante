package com.example.restaurante;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurante.Adapters.ResumenCompraAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.restaurante.Entities.Carrito;
import com.example.restaurante.Entities.Pedido;
import com.example.restaurante.service.ICarritoService;
import com.example.restaurante.service.respository.OrderRepository;

public class SummeryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ResumenCompraAdapter adapter;
    private ArrayList<Carrito> carritoList = new ArrayList<>();
    private double total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summery);

        recyclerView = findViewById(R.id.rvSummeryItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        RadioGroup rgDeliveryOptions = findViewById(R.id.rgDeliveryOptions);
        rgDeliveryOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                total = adapter.calculateTotalPrice();
                if (checkedId == R.id.rbDelivery) {
                    total += 5;
                }
                TextView tvTotalPrice1 = findViewById(R.id.tvTotalPrice1);
                tvTotalPrice1.setText(String.format("S/. %.2f", total));
            }
        });

        // Save order when you touch Checkout button
        Button btnCheckout = findViewById(R.id.btnCheckout);
        btnCheckout.setOnClickListener(v -> this.saveShopCar(carritoList));
        loadCarritoData();
    }


    private void saveShopCar(List<Carrito> carList) {

        // Create Pedido object
        Pedido order = new Pedido();
        order.setCarritolist(carList);
        order.setTotal(this.total);
        order.setDate(new Date());

        Toast.makeText(this, "Verificando solicitud orden", Toast.LENGTH_SHORT).show();

        OrderRepository.createOrder(order, new OrderRepository.Callback() {
            @Override
            public void onSuccess(Pedido order) {
                Intent intent = new Intent(SummeryActivity.this, Pagos.class);
                intent.putExtra("total", total);
                startActivity(intent);
            }

            @Override
            public void onFailure(String msg) {
                Toast.makeText(SummeryActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void loadCarritoData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://662f1f5643b6a7dce30e72cc.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ICarritoService service = retrofit.create(ICarritoService.class);
        Call<List<Carrito>> call = service.getAll();
        call.enqueue(new Callback<List<Carrito>>() {
            @Override
            public void onResponse(Call<List<Carrito>> call, Response<List<Carrito>> response) {
                if (response.isSuccessful()) {
                    carritoList = new ArrayList<>(response.body());
                    adapter = new ResumenCompraAdapter(carritoList);
                    recyclerView.setAdapter(adapter);

                    TextView tvTotalPrice1 = findViewById(R.id.tvTotalPrice1);
                    ;
                    double total = adapter.calculateTotalPrice();
                    tvTotalPrice1.setText(String.format("S/. %.2f", total));

                } else {
                    Toast.makeText(SummeryActivity.this, "Error al obtener los elementos del carrito", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Carrito>> call, Throwable t) {
                Toast.makeText(SummeryActivity.this, "Error al obtener los elementos del carrito", Toast.LENGTH_SHORT).show();
            }
        });
    }
}