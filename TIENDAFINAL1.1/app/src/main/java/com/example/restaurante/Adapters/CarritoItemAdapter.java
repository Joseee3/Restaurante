package com.example.restaurante.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurante.CarritoActivity;
import com.example.restaurante.Entities.Carrito;
import com.example.restaurante.R;
import com.example.restaurante.service.ICarritoService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CarritoItemAdapter extends RecyclerView.Adapter<CarritoItemAdapter.CarritoItemViewHolder> {
    private List<Carrito> mData = new ArrayList<>();

    private CarritoActivity carritoActivity;

    private OnTotalPriceChangeListener totalPriceChangeListener;



    public CarritoItemAdapter(ArrayList<Carrito> carritoList, OnTotalPriceChangeListener listener) {
        this.mData = carritoList;
        this.totalPriceChangeListener = listener;
    }

    public void updateData(List<Carrito> newData) {
        this.mData.clear();
        this.mData.addAll(newData);
        notifyDataSetChanged();
    }

    public double calculateTotalPrice() {
        double total = 0;
        for (Carrito item : mData) {
            total += item.getTotal();
        }
        return total;
    }

//    public void updateAndNotifyChanges(int position) {
//    mData.remove(position);
//    notifyItemRemoved(position);
//    notifyItemRangeChanged(position, mData.size());
//    double newTotal = calculateTotalPrice();
//    carritoActivity.updateTotalPrice(newTotal);
//}

    @NonNull
    @Override
    public CarritoItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_carrito, parent, false);
        return new CarritoItemViewHolder(view);
    }

    public void addItem(Carrito newItem) {
        for (Carrito item : mData) {
            if (item.getId() == newItem.getId()) { // Usar == en lugar de equals
                // El item ya existe en el carrito, incrementa la cantidad
                item.setCantidad(item.getCantidad() + newItem.getCantidad());
                notifyDataSetChanged(); // Notifica al RecyclerView que los datos han cambiado
                return;
            }
        }

        // El item no existe en el carrito, lo agregamos
        mData.add(newItem);
        notifyDataSetChanged(); // Notifica al RecyclerView que los datos han cambiado
    }


    @Override
    public void onBindViewHolder(@NonNull CarritoItemViewHolder holder, int position) {
        Carrito data = mData.get(position);

        holder.tvname_car.setText(data.getName());
        holder.tvprecio_car.setText(String.valueOf(data.getPrice()));

        if (data.getImage() != null) {
            Picasso.get().load(data.getImage()).into(holder.iv_img);
        }

        holder.edtCantidad.setText(String.valueOf(data.getCantidad()));

//        holder.eliminar.setOnClickListener(v -> {
//            mData.remove(position);
//            notifyItemRemoved(position);
//            notifyItemRangeChanged(position, mData.size());
//        });

//        Retrofit retrofit = new Retrofit.Builder()
//            .baseUrl("https://662f1f5643b6a7dce30e72cc.mockapi.io/") // Asegúrate de que esta es la URL base correcta
//            .addConverterFactory(GsonConverterFactory.create())
//            .build();
//
//        ICarritoService service = retrofit.create(ICarritoService.class);


       holder.btnAdd.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        int cantidadActual = data.getCantidad();
        data.setCantidad(cantidadActual + 1);
        holder.edtCantidad.setText(String.valueOf(data.getCantidad()));

        double total = data.getPrice() * data.getCantidad();
        data.setTotal(total);


        // Actualiza la cantidad en la base de datos

        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://662f1f5643b6a7dce30e72cc.mockapi.io/") // Asegúrate de que esta es la URL base correcta
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        if (totalPriceChangeListener != null) {
            totalPriceChangeListener.onTotalPriceChange(calculateTotalPrice());
        }

        ICarritoService service = retrofit.create(ICarritoService.class);

        Call<Carrito> call = service.updateCantidad(data.getId(), data);
        call.enqueue(new Callback<Carrito>() {
            @Override
            public void onResponse(Call<Carrito> call, Response<Carrito> response) {
                if (!response.isSuccessful()) {
                    // Maneja el error
                }
            }

            @Override
            public void onFailure(Call<Carrito> call, Throwable t) {
                // Maneja el error
            }
        });
    }
});

holder.btnDecrease.setOnClickListener(new View.OnClickListener(){
    @Override
    public void onClick(View v){
        int cantidadActual = data.getCantidad();
        if (cantidadActual > 1) {
            data.setCantidad(cantidadActual - 1);
            holder.edtCantidad.setText(String.valueOf(data.getCantidad()));

            double total = data.getPrice() * data.getCantidad();
            data.setTotal(total);

            // Actualiza la cantidad en la base de datos
            Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://662f1f5643b6a7dce30e72cc.mockapi.io/") // Asegúrate de que esta es la URL base correcta
                .addConverterFactory(GsonConverterFactory.create())
                .build();

            if (totalPriceChangeListener != null) {
                totalPriceChangeListener.onTotalPriceChange(calculateTotalPrice());
            }

            ICarritoService service = retrofit.create(ICarritoService.class);
            Call<Carrito> call = service.updateCantidad(data.getId(), data);
            call.enqueue(new Callback<Carrito>() {
                @Override
                public void onResponse(Call<Carrito> call, Response<Carrito> response) {
                    if (!response.isSuccessful()) {
                        // Maneja el error
                    }
                }

                @Override
                public void onFailure(Call<Carrito> call, Throwable t) {
                    // Maneja el error
                }
            });
        }
    }
});

        holder.eliminar.setOnClickListener(v -> {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://662f1f5643b6a7dce30e72cc.mockapi.io/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();



            ICarritoService service = retrofit.create(ICarritoService.class);
            Call<Void> call = service.delete(data.id);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {

                    if (response.isSuccessful()) {
                        int position = holder.getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mData.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, mData.size());
                            updateTotalPrice();
//                            updateAndNotifyChanges(position);
                        }
                    } else {
                        Toast.makeText(holder.itemView.getContext(), "Error al eliminar el elemento", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable throwable) {
                    Toast.makeText(holder.itemView.getContext(), "Error al eliminar el elemento", Toast.LENGTH_SHORT).show();

                }

            });
        });
    }




    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class CarritoItemViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_img;
        TextView tvname_car;
        TextView tvprecio_car;
        TextView edtCantidad;
        ImageView btnAdd;
        ImageView btnDecrease;

        ImageView eliminar;

        public CarritoItemViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_img = itemView.findViewById(R.id.ivplatocarrito);
            tvname_car = itemView.findViewById(R.id.tvNombreProducto);
            tvprecio_car = itemView.findViewById(R.id.tvPrecioProducto);
            edtCantidad = itemView.findViewById(R.id.edtCantidad);
            btnAdd = itemView.findViewById(R.id.btnAdd);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);

            eliminar =itemView.findViewById(R.id.btnEliminarProducto);
        }


    }

            public interface OnTotalPriceChangeListener {
            void onTotalPriceChange(double newTotalPrice);
        }

        private void updateTotalPrice() {
            double total = 0;
            for (Carrito item : mData) {
                total += item.getTotal();
            }
            if (totalPriceChangeListener != null) {
                totalPriceChangeListener.onTotalPriceChange(total);
            }
        }
}