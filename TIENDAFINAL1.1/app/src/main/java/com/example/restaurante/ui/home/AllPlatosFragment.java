package com.example.restaurante.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AllPlatosFragment  extends Fragment {
    private RecyclerView recyclerView;
    private PlatosItemAdapter itemContactAdapter;
    private IPlatosService platosService;

    private SearchView searchView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = root.findViewById(R.id.recycler_view);

        // Inicializa el servicio
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://662f1f5643b6a7dce30e72cc.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        platosService = retrofit.create(IPlatosService.class);

        // Realiza la llamada a la API
        IPlatosService service = retrofit.create(IPlatosService.class);

       Call<List<Platos>> call = service.getAll();
        call.enqueue(new Callback<List<Platos>>() {
            @Override
            public void onResponse(Call<List<Platos>> call, Response<List<Platos>> response) {
                if (response.isSuccessful()) {
                    List<Platos> todosLosPlatos = response.body();
                    Log.d("API_CALL", "Received " + todosLosPlatos.size() + " platos from API");

                    // No filtra la lista, pasa todos los platos al adaptador
                    itemContactAdapter = new PlatosItemAdapter(new HomeFragment(), todosLosPlatos);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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

        searchView = root.findViewById(R.id.searchView);
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

        return root;
    }

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
                   // itemContactAdapter = new PlatosItemAdapter(HomeFragment.this, platos);
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
