package com.example.restaurante;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restaurante.Entities.Pedido;
import com.example.restaurante.Entities.Carrito;
import com.example.restaurante.Entities.Platos;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pagos extends AppCompatActivity {

    private List<Carrito> carritoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment_success);

        double total = getIntent().getDoubleExtra("total", 0);
        TextView tvPaymentMessage = findViewById(R.id.tv_payment_message);
        tvPaymentMessage.setText(String.format("Tu pago de S/. %.2f fue completado exitosamente. Se ha enviado un recibo a tu correo electrónico.", total));

        Button btnCompletePurchase = findViewById(R.id.btn_continue_shopping);
        btnCompletePurchase.setOnClickListener(view -> this.finish());

    }
}