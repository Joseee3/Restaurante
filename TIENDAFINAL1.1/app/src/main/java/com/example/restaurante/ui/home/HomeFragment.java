package com.example.restaurante.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurante.Entities.Platos;
import com.example.restaurante.R;
import com.example.restaurante.Adapters.PlatosItemAdapter;
import com.example.restaurante.service.IPlatosService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    private GridView mGridView;
    private RecyclerView recyclerView;
    private PlatosItemAdapter itemContactAdapter;
    private IPlatosService platosService;

    private SearchView searchView;

    private DatabaseReference MdataBase;
   private List<Platos> todosLosPlatos;

//   @Override
//   public void onCreate(Bundle savedInstanceState) {
//       super.onCreate(savedInstanceState);
//       if (todosLosPlatos == null) {
//           todosLosPlatos = new ArrayList<>();
//       }
//   }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = root.findViewById(R.id.recycler_view);


        MdataBase = FirebaseDatabase.getInstance().getReference("Platos");
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);



        // Inicializa el servicio
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://662f1f5643b6a7dce30e72cc.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        platosService = retrofit.create(IPlatosService.class);



        itemContactAdapter = new PlatosItemAdapter(HomeFragment.this, new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(itemContactAdapter);



        // Realiza la llamada a la API
        IPlatosService service = retrofit.create(IPlatosService.class);

        Call<List<Platos>> call = service.getAll();
        call.enqueue(new Callback<List<Platos>>() {
            @Override
            public void onResponse(Call<List<Platos>> call, Response<List<Platos>> response) {
                if (response.isSuccessful()) {
                    List<Platos> todosLosPlatos = response.body();
                    Log.d("API_CALL", "Received " + todosLosPlatos.size() + " platos from API");

                    // Filtra la lista para obtener solo los platos sugeridos
                    List<Platos> platosSugeridos = new ArrayList<>();
                    for (Platos plato : todosLosPlatos) {
                        if (plato.isFavorite()) {
                            platosSugeridos.add(plato);
                        }
                    }
                    almacenarDatosEnFirebase(todosLosPlatos);
                    itemContactAdapter = new PlatosItemAdapter(HomeFragment.this, platosSugeridos);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(itemContactAdapter);
                } else {
                    Toast.makeText(getContext(), "Error al cargar los datos", Toast.LENGTH_SHORT).show();
                    Log.d("API_CALL", "Error response from API: " + response.errorBody());
                    cardgarDatosDesdeFirebase();
                }
            }

            @Override
            public void onFailure(Call<List<Platos>> call, Throwable t) {
                Toast.makeText(getContext(), "Error al cargar los datos", Toast.LENGTH_SHORT).show();
                Log.d("API_CALL", "Failure calling API", t);
            }
        });

        ImageView filterIcon = root.findViewById(R.id.ic_filter);
       filterIcon.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Llama al método para obtener todos los platos
            Call<List<Platos>> call = platosService.getAll();
            call.enqueue(new Callback<List<Platos>>() {
                @Override
                public void onResponse(Call<List<Platos>> call, Response<List<Platos>> response) {
                    if (response.isSuccessful()) {
                        todosLosPlatos = response.body();

                        // Extrae las categorías de los platos
                        List<String> categories = new ArrayList<>();
                        for (Platos plato : todosLosPlatos) {
                            String categoria = plato.getCategoria();
                            if (!categories.contains(categoria)) {
                                categories.add(categoria);
                            }
                        }

                        // Agrega una categoría adicional para representar todos los platos sin filtrado
                        categories.add("Todos los platos");

                        // Muestra un diálogo con las diferentes categorías para que el usuario seleccione una
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Selecciona una categoría")
                                .setItems(categories.toArray(new String[0]), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // El 'which' argumento contiene el índice de la opción seleccionada
                                        String selectedCategory = categories.get(which);

                                        // Si el usuario seleccionó "Todos los platos", muestra todos los platos sin filtrado
                                        if (selectedCategory.equals("Todos los platos")) {
                                            itemContactAdapter.updateList1(todosLosPlatos);
                                        } else {
                                            // Filtra los platos por categoría
                                            List<Platos> filteredPlatos = new ArrayList<>();
                                            for (Platos plato : todosLosPlatos) {
                                                if (plato.getCategoria().equals(selectedCategory)) {
                                                    filteredPlatos.add(plato);
                                                }
                                            }

                                            // Actualiza el adaptador con los platos filtrados
                                            itemContactAdapter.updateList1(filteredPlatos);
                                        }
                                    }
                                });
                        builder.create().show();
                    } else {
                        Toast.makeText(getContext(), "Error al cargar los platos", Toast.LENGTH_SHORT).show();
                        Log.d("API_CALL", "Error response from API: " + response.errorBody());
                    }
                }

                @Override
                public void onFailure(Call<List<Platos>> call, Throwable t) {
                    Toast.makeText(getContext(), "Error al cargar los platos", Toast.LENGTH_SHORT).show();
                    Log.d("API_CALL", "Failure calling API", t);
                }
            });
        }
});

       //Inicializar Firebase Database Reference
        MdataBase = FirebaseDatabase.getInstance().getReference("Platos");
        // Inicializa el SearchView
        searchView = root.findViewById(R.id.searchView);

        // El resto de tu código...

        // Configura el SearchView
       searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.i("HomeFragment search:", query);
                performSearch(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    performSearch("");
                    searchView.clearFocus();
                }
                return true;
            }
        });

        cargarDatos();

        return root;
    }


//    private void cargarDatos() {
//        // Verificar la conexión a Internet
//        if (disponibilidadDeRed()) {
//            // Si hay conexión a Internet, carga los datos desde Retrofit y almacena en Firebase
//            cargarDatosDesdeRetrofit();
//        } else {
//            // Si no hay conexión a Internet, carga los datos desde Firebase
//            cardgarDatosDesdeFirebase();
//        }
//    }
//    private boolean disponibilidadDeRed() {
//        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        if (connectivityManager != null) {
//            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
//        }
//        return false;
//    }


    private void cargarDatos() {
    if (disponibilidadDeRed(getContext())) {
        cargarDatosDesdeRetrofit();
    } else {
        cardgarDatosDesdeFirebase();
    }
}

private void cargarDatosDesdeRetrofit() {
    Call<List<Platos>> call = platosService.getAll();
    call.enqueue(new Callback<List<Platos>>() {
        @Override
        public void onResponse(Call<List<Platos>> call, Response<List<Platos>> response) {
            if (response.isSuccessful()) {
                List<Platos> todosLosPlatos = response.body();
                Log.d("Retrofit", "Datos cargados desde Retrofit: " + todosLosPlatos.size() + " platos");
                almacenarDatosEnFirebase(todosLosPlatos);
                itemContactAdapter = new PlatosItemAdapter(HomeFragment.this, todosLosPlatos);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(itemContactAdapter);
            } else {
                Log.d("Retrofit", "Error al cargar los datos: " + response.errorBody());
                cardgarDatosDesdeFirebase();
            }
        }

        @Override
        public void onFailure(Call<List<Platos>> call, Throwable t) {
            cardgarDatosDesdeFirebase();
        }
    });
}

    private boolean disponibilidadDeRed(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }


    //FIREBASE
    private void almacenarDatosEnFirebase(List<Platos> platos) {
    for (Platos plato : platos) {
        // Usa el ID del plato como clave en lugar del nombre
        MdataBase.child(String.valueOf(plato.getId())).setValue(plato);
    }
}

private void cardgarDatosDesdeFirebase(){
        Log.d("Firebase", "Cargando datos desde Firebase...");
    MdataBase.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()){
                List<Platos> platos = new ArrayList<>();
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    Platos plato = snapshot1.getValue(Platos.class);
                    platos.add(plato);
                }
                // Usa la lista de platos para actualizar el adaptador
                todosLosPlatos = platos;
                itemContactAdapter = new PlatosItemAdapter(HomeFragment.this, todosLosPlatos);
                recyclerView.setAdapter(itemContactAdapter);
                Log.d("Firebase", "Datos cargados desde Firebase: " + todosLosPlatos.size() + " platos");
                itemContactAdapter.updateList1(todosLosPlatos);
            }else{
                Log.d("Firebase", "No hay datos almacenados en Firebase");
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.e("Firebase", "Error al cargar datos de Firebase: " + error.getMessage());
        }
    });
}
   // Implementa el método performSearch
    private void performSearch(String query) {
        Call<List<Platos>> call;
        if (query.isEmpty()) {
            // Si la consulta está vacía, obtén todos los platos
            call = platosService.getAll();
        } else {
            // Si la consulta no está vacía, realiza la búsqueda
            call = platosService.search(query);
        }

        call.enqueue(new Callback<List<Platos>>() {
            @Override
            public void onResponse(Call<List<Platos>> call, Response<List<Platos>> response) {
                if (response.isSuccessful()) {
                    List<Platos> platos = response.body();
                    itemContactAdapter = new PlatosItemAdapter(HomeFragment.this, platos);
                    recyclerView.setAdapter(itemContactAdapter);
                } else {
                    Toast.makeText(getContext(), "Error al cargar los datos", Toast.LENGTH_SHORT).show();
                    Log.d("API_CALL", "Error response from API: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Platos>> call, Throwable t) {
                Toast.makeText(getContext(), "Error al cargar los datos", Toast.LENGTH_SHORT).show();
                Log.d("API_CALL", "Failure calling API", t);
            }
        });
    }
}