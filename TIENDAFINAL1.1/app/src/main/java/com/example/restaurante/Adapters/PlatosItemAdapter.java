package com.example.restaurante.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurante.PlatosDetailActivity;
import com.example.restaurante.Entities.Carrito;
import com.example.restaurante.Entities.Platos;
import com.example.restaurante.R;
import com.example.restaurante.service.ICarritoService;
import com.example.restaurante.ui.home.HomeFragment;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlatosItemAdapter extends RecyclerView.Adapter<PlatosItemAdapter.PlatosItemViewHolder> {
    private  List<Platos> mData;
    private Retrofit retrofit;
    private List<Carrito> carritoList ;

    private OnCartChangedListener onCartChangedListener;

    public PlatosItemAdapter(HomeFragment homeFragment, List<Platos> data) {

        this.mData = data;

        this.onCartChangedListener = onCartChangedListener;

        this.retrofit = new Retrofit.Builder()
                .baseUrl("https://662f1f5643b6a7dce30e72cc.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public void updateData(List<Carrito> newCarritoList) {
        this.carritoList = newCarritoList;
        notifyDataSetChanged();
    }

    public void updateList1(List<Platos> newPlatos) {
        this.mData= newPlatos;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public PlatosItemAdapter.PlatosItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // definir que layout se debe usar

        Button btnAddToCart;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_platos, parent, false);
        View view1 = inflater.inflate(R.layout.item_carrito, parent, false);



        return new PlatosItemAdapter.PlatosItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlatosItemAdapter.PlatosItemViewHolder holder, int position) {
        //ITEM CONTACT/PLATOS
        View view = holder.itemView;

        Platos data = mData.get(position);

        ImageView ivplato= view.findViewById(R.id.ivProducto);
        TextView tvname= view.findViewById(R.id.tvname);
        TextView tvcategoria = view.findViewById(R.id.tvcategoria);
        TextView tvprecio = view.findViewById(R.id.tvprecio);

        Button btnAddToCart = view.findViewById(R.id.btnAddToCart);

        tvname.setText(data.name);
        tvcategoria.setText(data.categoria);
        tvprecio.setText("S/. "+data.price);

        if (data.photo != null) {
            Picasso.get().load(data.photo).into(ivplato);
        }

//        //ITEM CARRITO
//        View view1 = holder.itemView;
//
//        Carrito data1 = carritoList.get(position);
//
//        ImageView iv_img= view.findViewById(R.id.ivProducto);
//        TextView tvname_car= view.findViewById(R.id.tvNombreProducto);
//        TextView tvprecio_car = view.findViewById(R.id.tvPrecioProducto);
//
//        Button btn_delete_prod= view.findViewById(R.id.btnEliminarProducto);
//
//        tvname_car.setText(data1.getName());
//        tvprecio_car.setText(data1.getPrice());
//
//
//        if (data.photo != null) {
//            Picasso.get().load(data.photo).into(ivplato);
//        }

        // Configura los datos del plato en el ViewHolder


    holder.btnAddCart.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {


//            Carrito carritoItem = new Carrito();
//            carritoItem.setName(data.name);
//            carritoItem.setImage(data.photo);
//            carritoItem.setPrice(Double.valueOf(data.price)); // Asegúrate de que data.price contiene el precio correcto
//            carritoItem.setCantidad(1); // Establece la cantidad inicial a 1
//            carritoItem.setTotal(Double.valueOf(data.price));

            Carrito carritoItem = new Carrito(
                data.getId(), // ID, puedes reemplazar esto con el ID correcto
                data.name,
                1, // La cantidad inicial es 1
                Double.valueOf(data.price),
                data.photo
            );
            carritoItem.setTotal(Double.valueOf(data.price));


            //incrementar la cantidad de un producto
//            int cantidadActual = data.getCantidad();
//            data.setCantidad(cantidadActual + 1);


//            double total = carritoItem.getPrice() * carritoItem.getCantidad();
//            data.setTotal(data.getPrice() * data.getCantidad());
//            carritoItem.setTotal(total);

            // Hacer la llamada a la API para obtener la lista del carrito
            ICarritoService service = retrofit.create(ICarritoService.class);
            Call<List<Carrito>> call = service.getCarrito();
            call.enqueue(new retrofit2.Callback<List<Carrito>>() {
                @Override
                public void onResponse(Call<List<Carrito>> call, Response<List<Carrito>> response) {
                    if (response.isSuccessful()) {
                        List<Carrito> carritoList = response.body();
                        for (Carrito item : carritoList) {
                            if (item.getName().equals(carritoItem.getName())) {
                                // El plato ya está en el carrito, incrementa la cantidad
                                item.setCantidad(item.getCantidad() + 1);


                                double total = item.getPrice() * item.getCantidad();
                                item.setTotal(total);

                                // Actualiza el item en el carrito
                                Call<Carrito> updateCall = service.update(item.getId(), item);
                                updateCall.enqueue(new retrofit2.Callback<Carrito>() {
                                    @Override
                                    public void onResponse(Call<Carrito> call, Response<Carrito> response) {
                                        Toast.makeText(v.getContext(), "Cantidad de producto incrementada", Toast.LENGTH_SHORT).show();
                                        updateTotalPrice();
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
                                updateTotalPrice();
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

        // LinearLayout itemLayoutPlatos = view.findViewById(R.id.itemLayoutContact);

        MaterialCardView itemLayoutPlatos = view.findViewById(R.id.itemLayoutContact);

        itemLayoutPlatos.setOnClickListener(v -> {
            Intent intent = new Intent(view.getContext(), PlatosDetailActivity.class);
            String sData = new Gson().toJson(data); // convierte el objeto en un string JSON
            intent.putExtra("contact", sData);
            intent.putExtra("price","S/." + data.price);
            view.getContext().startActivity(intent);


            //Define la transición
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) view.getContext(),
//                        itemLayoutPlatos, "zoom_in");
////                        Pair.create(ivplato, "imageTransition"),
////                        Pair.create(tvname, "nameTransition"),
////                        Pair.create(tvprecio, "priceTransition"));
//
//                view.getContext().startActivity(intent, options.toBundle());
//            } else {
//                view.getContext().startActivity(intent);
//            }
        });
    }



    @Override
    public int getItemCount() {

        return mData.size();
    }

    private void updateTotalPrice(){
        if (onCartChangedListener != null) {
            onCartChangedListener.onCartChanged();
        }
    }


    public static class PlatosItemViewHolder extends RecyclerView.ViewHolder {
        Button btnAddCart;

        ImageView ivplato;
        TextView tvname;
        TextView tvcategoria;
        TextView tvprecio;
        ImageView eliminar;
        public PlatosItemViewHolder(@NonNull View itemView) {
            super(itemView);
            btnAddCart = itemView.findViewById(R.id.btnAddToCart);
            ivplato = itemView.findViewById(R.id.ivProducto);
            tvname = itemView.findViewById(R.id.tvNombreProducto);
            tvprecio = itemView.findViewById(R.id.tvPrecioProducto);
            eliminar = itemView.findViewById(R.id.btnEliminarProducto);
        }
    }

            public interface OnCartChangedListener {
            void onCartChanged();
        }
}
