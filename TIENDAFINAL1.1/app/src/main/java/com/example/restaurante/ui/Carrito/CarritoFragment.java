package com.example.restaurante.ui.Carrito;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurante.Entities.Carrito;
import com.example.restaurante.Entities.Pedido;
import com.example.restaurante.Entities.Platos;
import com.example.restaurante.R;
import com.example.restaurante.Adapters.CarritoItemAdapter;
import com.example.restaurante.SummeryActivity;
import com.example.restaurante.service.ICarritoService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CarritoFragment extends Fragment implements CarritoItemAdapter.OnTotalPriceChangeListener{
    private RecyclerView recyclerView;
    private CarritoItemAdapter adapter;
    private ArrayList<Carrito> carritoList = new ArrayList<>();

    private int itemCount = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_carrito, container, false);

        recyclerView = view.findViewById(R.id.rvCartItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new CarritoItemAdapter(carritoList, this);
        recyclerView.setAdapter(adapter);

        loadCarritoData();

//        Button btnCheckout = view.findViewById(R.id.btnCheckout);
//        btnCheckout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Iniciar ResumenCompraActivity
//                Intent intent = new Intent(getActivity(), Pagos.class);
//                startActivity(intent);
//            }
//        });

        Button btnCheckout = view.findViewById(R.id.btnCheckout);
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SummeryActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }

    private void updateTotalPrice() {
        double total = adapter.calculateTotalPrice();
        TextView tvTotalPrice = getView().findViewById(R.id.tvTotalPrice);
        tvTotalPrice.setText(String.format("Total: S%.2f", total));
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
                    Toast.makeText(getContext(), "Error al obtener los elementos del carrito", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Carrito>> call, Throwable t) {
                Toast.makeText(getContext(), "Error al obtener los elementos del carrito", Toast.LENGTH_SHORT).show();
            }
        });
    }

      @Override
    public void onTotalPriceChange(double newTotalPrice) {
        TextView tvTotalPrice = getView().findViewById(R.id.tvTotalPrice);
        tvTotalPrice.setText(String.format("Total: $%.2f", newTotalPrice));
    }

//    public void createPedido(){
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//        Pedido pedido = new Pedido();
//        pedido.setId("123");
//        pedido.setFecha("2022-01-01");
//        pedido.setTotal(100.0);
//
//        List<Platos> platos = new ArrayList<>();
//        for (Carrito item : carritoList) {
//            platos.add(new Platos(item.getName(), item.getCantidad()));
//        }
//        pedido.setPlatos(platos);
//
//        db.collection("pedidos").add(pedido)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.d(TAG, "Pedido añadido con ID: " + documentReference.getId());
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error añadiendo pedido", e);
//                    }
//                });
//    }
}