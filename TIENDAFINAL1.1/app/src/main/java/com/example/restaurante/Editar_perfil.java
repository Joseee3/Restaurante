package com.example.restaurante;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class Editar_perfil extends AppCompatActivity {

    ImageView photo_user;
    TextView mEtemail, nombreuser;

    Button btninfo,  btndirec;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_editar_perfil);

        mAuth = FirebaseAuth.getInstance();

        mEtemail = findViewById(R.id.correoUser);
        nombreuser =  findViewById(R.id.nameUser);
        photo_user = findViewById(R.id.photouser);
        btninfo = findViewById(R.id.btn_info);
        btndirec = findViewById(R.id.btn_direcciones);

        btndirec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Editar_perfil.this, Direccionesuser.class));
            }
        });


        btninfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Editar_perfil.this, ProfileActivity.class));
            }
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Button btnCerrarSesion = findViewById(R.id.btn_cerrarsesion);
        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();

//                // Borra el nombre del TextView
//                nombreuser.setText("");

                // Redirigir al usuario a la pantalla de perfil después de cerrar la sesión
                Intent intent = new Intent(Editar_perfil.this, MenuPerfilActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button btnHistorial = findViewById(R.id.btn_historial);
        btnHistorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HistorialActivity.class);
                startActivity(intent);
            }
        });


        db.collection("user").document(mAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String name = documentSnapshot.getString("name");
                String email = documentSnapshot.getString("email");
                String photo = documentSnapshot.getString("photo");

                nombreuser.setText(name);
                mEtemail.setText(email);
                try{
                    if (!photo.equals("")){
                        Toast toast = Toast.makeText(getApplicationContext(), "Cargando foto", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP,0,200);
                        toast.show();

                        Glide.with(Editar_perfil.this)
                                .load(photo)
                                .into(photo_user);
                    }
                }catch (Exception e){
                    Log.v("Error", "e" + e);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al obtener los datos", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

}