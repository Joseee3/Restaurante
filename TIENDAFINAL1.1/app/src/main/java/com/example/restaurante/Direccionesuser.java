package com.example.restaurante;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurante.Adapters.DireccionItemAdapter;
import com.example.restaurante.Entities.Direccion;
import com.example.restaurante.service.respository.AddressRepository;

import java.util.ArrayList;
import java.util.List;

public class Direccionesuser extends AppCompatActivity {

    private String LOG_TAG = "LOAD_ADDRESSES";
    private DireccionItemAdapter adapter;
    private final int ADDRESS_ADDED_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_direccionesuser);

        // Recyclerview and Adapter setup
        this.adapter = new DireccionItemAdapter(new ArrayList<>(), this);

        RecyclerView rv = this.findViewById(R.id.direcciones_recyclerview);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(this.adapter);

        // Buttons actions setup
        Button btnAddAddress = findViewById(R.id.btn_direcci);
        btnAddAddress.setOnClickListener(v -> addNewAddress());

        // Fetch addresses
        this.fetchAddresses();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADDRESS_ADDED_CODE) {
            if (resultCode == RESULT_OK) {
                this.fetchAddresses();
            }
        }
    }

    private void fetchAddresses() {
        AddressRepository.getAddress(new AddressRepository.CallbackGetAddress() {
            @Override
            public void onSuccess(List<Direccion> addresses) {

                if (!addresses.isEmpty()) {
                    // Update data's adapter
                    adapter.setDirecciones(addresses);
                    adapter.notifyDataSetChanged();
                } else { // Theres is not address for this user
                    createAlert(
                            "INFORMACIÓN DE OPERACIÓN",
                            "Usted no tiene direcciones agregadas, agregue direcciones" +
                                    "usando el boton en la parte inferior",
                            R.drawable.icon_success
                    ).show();
                }
            }

            @Override
            public void onFailure(String msgError) {
                createAlert(
                        "ERROR DE OPERACIÓN",
                        msgError,
                        R.drawable.icon_error
                ).show();
            }
        });
    }

    private void addNewAddress() {

        Intent intent = new Intent(this, RegistraDireccion.class);
        this.startActivityForResult(intent, ADDRESS_ADDED_CODE);
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