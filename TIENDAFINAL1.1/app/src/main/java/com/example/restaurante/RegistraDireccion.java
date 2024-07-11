package com.example.restaurante;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistraDireccion extends AppCompatActivity {

    private final int ADDRESS_ADDED_CODE = 1;

    //declarar
    EditText direccion, depa, indicacion, nomdire;
    Button registradirecci;

    FirebaseFirestore mfirestore;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registra_direccion);

        mfirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        //intancia de edittext
        direccion = findViewById(R.id.etDireccion);
        depa = findViewById(R.id.etdepa);
        indicacion = findViewById(R.id.etindicaciones);
        nomdire = findViewById(R.id.etnomedirec);
        registradirecci = findViewById(R.id.btnRegister);

        registradirecci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String direcio = direccion.getText().toString().trim();
                String depart = depa.getText().toString().trim();
                String indicacin = indicacion.getText().toString().trim();
                String nombredire = nomdire.getText().toString().trim();

                if (direcio.isEmpty() && depart.isEmpty() && indicacin.isEmpty() && nombredire.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Ingresar los datos", Toast.LENGTH_SHORT).show();
                }else{
                    registerdirec(direcio,depart,indicacin,nombredire);
                }
            }
        });

    }

    private  void registerdirec (String direcci, String depart, String indica, String nomdirec){
        String iduser =mAuth.getCurrentUser().getUid();
        DocumentReference id = mfirestore.collection("user").document();

        Map<String, Object> map = new HashMap<>();
        map.put("id_user", iduser);
        map.put("id", id.getId());
        map.put("direccion", direcci);
        map.put("tipovivienda", depart);
        map.put("indicaciones", indica);
        map.put("nombredireccion", nomdirec);

        mfirestore.collection("direcciones").document(id.getId()).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "Creado exitosamente", Toast.LENGTH_SHORT).show();
                Intent returnIntent = new Intent();
                setResult(RESULT_OK, returnIntent);
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al ingresar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

}

