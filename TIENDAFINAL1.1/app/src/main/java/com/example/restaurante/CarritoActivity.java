package com.example.restaurante;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurante.Adapters.PlatosItemAdapter;
import com.example.restaurante.Entities.Carrito;
import com.example.restaurante.Adapters.CarritoItemAdapter;
import com.example.restaurante.service.ICarritoService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CarritoActivity extends AppCompatActivity implements CarritoItemAdapter.OnTotalPriceChangeListener, PlatosItemAdapter.OnCartChangedListener {
    private RecyclerView recyclerView;
    private CarritoItemAdapter adapter;
    private ArrayList<Carrito> carritoList = new ArrayList<>();

    private int itemCount = 0;

    private EditText edtCantidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        recyclerView = findViewById(R.id.rvCartItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new CarritoItemAdapter(carritoList,this);
        recyclerView.setAdapter(adapter);


        loadCarritoData();


//        itemCount++;
//
//        Intent intent = new Intent("carUpdate");
//        intent.putExtra("itemCount", itemCount);
//        sendBroadcast(intent);


        Button btnCheckout = findViewById(R.id.btnCheckout);
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CarritoActivity.this, Pagos.class);
                startActivity(intent);
            }
        });
    }



    private void updateTotalPrice() {
        double total = adapter.calculateTotalPrice();
        TextView tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvTotalPrice.setText(String.format("Total: $%.2f", total));


//    public void updateTotalPrice() {
//    //Double total
//        // double total = adapter.calculateTotalPrice();
//    TextView tvTotalPrice = findViewById(R.id.tvTotalPrice);
//    tvTotalPrice.setText(String.format("Total: $%.2f", total));

//    public void updateTotalPrice(double total) {
//        TextView tvTotalPrice = findViewById(R.id.tvTotalPrice);
//        tvTotalPrice.setText(String.format("Total: $%.2f", total));
//    }
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
                    adapter.updateData(carritoList);
                    updateTotalPrice();
                } else {
                    Toast.makeText(CarritoActivity.this, "Error al obtener los elementos del carrito", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Carrito>> call, Throwable t) {
                Toast.makeText(CarritoActivity.this, "Error al obtener los elementos del carrito", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onTotalPriceChange(double newTotalPrice) {
        TextView tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvTotalPrice.setText(String.format("Total: $%.2f", newTotalPrice));
    }

    @Override
    public void onCartChanged() {
       updateTotalPrice();
    }
}