package com.example.restaurante;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.RoundedCorner;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.example.restaurante.Adapters.CarritoItemAdapter;
import com.example.restaurante.Entities.Carrito;
import com.example.restaurante.Entities.Platos;
import com.example.restaurante.service.ICarritoService;
import com.example.restaurante.service.IPlatosService;
import com.google.android.material.shape.RoundedCornerTreatment;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;
import java.util.List;


public class PlatosDetailActivity extends AppCompatActivity {

    private Platos mPlatos;

    private TextView tvprecio;

    private Retrofit retrofit;

    private IPlatosService platosService;

    //private EditText edtCantidad;

   //private EditText edtCantidad;
    private List<Carrito> mData;

    private ICarritoService service;


    private CarritoItemAdapter.OnTotalPriceChangeListener totalPriceChangeListener;


    public void onTotalPriceChange(double totalPrice) {
    TextView tvTotalPrice = findViewById(R.id.tvTotalPrice);
    tvTotalPrice.setText(String.format("S/. %.2f", totalPrice));
}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_platos_detail);

//        totalPriceChangeListener = new CarritoltemAdapter.OnTotalPriceChangeListener() {
//            @Override
//            public void onTotalPriceChange(double newTotalPrice) {
//        // Actualiza el precio total en la interfaz de usuario
//                TextView tvTotalPrice = findViewByld(R.id.tvTotalPrice);
//                tvTotalPrice.setText(String.format("S/. %.2f", newTotalPrice));


        mData = new ArrayList<>();


        // Inicializa el servicio
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://662f1f5643b6a7dce30e72cc.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        platosService = retrofit.create(IPlatosService.class);
        service = retrofit.create(ICarritoService.class);

        //edtCantidad  = findViewById(R.id.edtCantidad);

        //Transitions



        Intent intent = getIntent();
        String sData = intent.getStringExtra("contact");

        Log.i("MAIN_APP", sData);

        mPlatos = new Gson().fromJson(sData, Platos.class); // convierte el string JSON a un objeto
Button btnAddToCart = findViewById(R.id.btnAddToCart);
btnAddToCart.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
//        Carrito carritoItem = new Carrito();
//        carritoItem.setName(mPlatos.name);
//        carritoItem.setImage(mPlatos.photo);
//        carritoItem.setPrice(Double.valueOf(mPlatos.price));

        Carrito carritoItem = new Carrito(
                mPlatos.getId(), // ID del plato
                mPlatos.getName(), // Nombre del plato
                1, // Cantidad inicial
                Double.valueOf(mPlatos.getPrice()), // Precio del plato
                mPlatos.getPhoto() // Foto del plato
            );
        carritoItem.setCantidad(1); // Establece la cantidad inicial a 1

        // Crear una instancia de la interfaz del servicio
        ICarritoService service = retrofit.create(ICarritoService.class);
        // Hacer la llamada a la API para obtener la lista del carrito
        Call<List<Carrito>> call = service.getCarrito();
        call.enqueue(new retrofit2.Callback<List<Carrito>>() {
            @Override
            public void onResponse(Call<List<Carrito>> call, Response<List<Carrito>> response) {
                if (response.isSuccessful()) {
                    List<Carrito> carritoList = response.body();
                    if (response.isSuccessful()) {
                        mData = response.body();
                        }
                         for (Carrito item : carritoList) {
                        if (item.getName().equals(carritoItem.getName())) {
                            // El plato ya está en el carrito, incrementa la cantidad
                            item.setCantidad(item.getCantidad() + 1);

                           // edtCantidad.setText(String.valueOf(item.getCantidad()));
                            // Actualiza el item en el carrito
                            Call<Carrito> updateCall = service.update(item.getId(), item);
                            updateCall.enqueue(new retrofit2.Callback<Carrito>() {
                                @Override
                                public void onResponse(Call<Carrito> call, Response<Carrito> response) {
                                    Toast.makeText(v.getContext(), "Cantidad de producto incrementada", Toast.LENGTH_SHORT).show();

                                    if (totalPriceChangeListener != null) {
                                        totalPriceChangeListener.onTotalPriceChange(calculateTotalPrice());
                                    }
                                }

                                @Override
                                public void onFailure(Call<Carrito> call, Throwable throwable) {
                                    Toast.makeText(v.getContext(), "Error al incrementar la cantidad del producto", Toast.LENGTH_SHORT).show();
                                }
                            });
                            return; // Detiene la ejecución del método
                        }
                    }
                    // El plato no está en el carrito, agrega un nuevo item
                    Call<Carrito> createCall = service.create(carritoItem);
                    createCall.enqueue(new retrofit2.Callback<Carrito>() {
                        @Override
                        public void onResponse(Call<Carrito> call, Response<Carrito> response) {
                            Toast.makeText(v.getContext(), "Producto agregado al carrito", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<Carrito> call, Throwable throwable) {
                            Toast.makeText(v.getContext(), "Error al agregar al carrito", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(v.getContext(), "Error al obtener el carrito", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Carrito>> call, Throwable throwable) {
                Toast.makeText(v.getContext(), "Error al obtener el carrito", Toast.LENGTH_SHORT).show();
            }
        });
    }
});

        setUpView();
    }

    private void setUpView() {
        TextView tvnombreDetail = findViewById(R.id.tvnombreDetail);
        TextView tvdescripcionDetail = findViewById(R.id.tvdescripciondetail);
        TextView tvprecioDetail = findViewById(R.id.tvpreciodetalil);
        ImageView ivAvatar = findViewById(R.id.ivAvatar);

        //EditText edtCantidad = findViewById(R.id.edtCantidad);

        //EditText edtCantidad = findViewById(R.id.edtCantidad);

        tvnombreDetail.setText(mPlatos.name);
        tvdescripcionDetail.setText(mPlatos.description);
        tvprecioDetail.setText("S/"+ mPlatos.price+".00");

        //edtCantidad.setText("1");

        if (mPlatos.photo != null) {
            // Picasso.get().load(mPlatos.photo).into(ivAvatar);
            Picasso.get().load(mPlatos.photo).transform(new RoundedCornersTransformation(50, 0)).into(ivAvatar);
        }
    }

    private double calculateTotalPrice() {
        double total = 0;
        for (Carrito item : mData) {
            total += item.getTotal();
        }
        return total;
    }
}
