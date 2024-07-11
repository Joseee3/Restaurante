package com.example.restaurante;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.restaurante.Editar_perfil;


public class LoginActivity extends AppCompatActivity {



    TextView correouser, nombreuser;


        EditText mEtemail;
    EditText mEtPassword;
    Button mBtnLogin;

    String emailuser;

     FirebaseFirestore mfirestore;
  FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();



        mEtemail = findViewById(R.id.etEmail);
        mEtPassword = findViewById(R.id.etPassword);
        mBtnLogin = findViewById(R.id.btnLogin);

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailuser = mEtemail.getText().toString().trim();
                String contrauser= mEtPassword.getText().toString().trim();

                if (emailuser.isEmpty() && contrauser.isEmpty())
                    Toast.makeText(LoginActivity.this, "Ingresar los datos", Toast.LENGTH_SHORT).show();
                else{
                    loginUser(emailuser, contrauser);
                }

            }
        });

    }

    private void loginUser(String emailuser, String contrauser) {
        mAuth.signInWithEmailAndPassword(emailuser, contrauser).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    finish();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    Toast.makeText(LoginActivity.this,"Bienvenido", Toast.LENGTH_SHORT).show();

                    //para mostrar datos del usuario
//                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                        fetchAndDisplayUserInfo(user);

                    // Crea un Intent para iniciar Editarusurio
//                    Intent inten = new Intent(LoginActivity.this, Editar_perfil.class);

                    //colocar los datos del usuario como extra
//                    inten.putExtra("useremail", user.getEmail());
//                    inten.putExtra("username", user.getDisplayName());
//                    if (user.getPhotoUrl() != null){
//                        inten.putExtra("userphoto", user.getPhotoUrl().toString());
//                    }

                }
                else
                    Toast.makeText(LoginActivity.this,"Error",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, "Error al inciar sesión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchAndDisplayUserInfo( FirebaseUser user ){
            if (user != null){
                //getear el email
                String email =user.getEmail();
                correouser = findViewById(R.id.correoUser);
                correouser.setText(email);

                //getear el nombre
                String nombreusu = user.getDisplayName();
                if (nombreusu != null){
                    nombreuser = findViewById(R.id.nameUser);
                    nombreuser.setText(nombreusu);
                }

                //getear la foto
                Uri photoURl = user.getPhotoUrl();
                if (photoURl != null){
                    ImageView photousuario = findViewById(R.id.photouser);
                    Glide.with(this)
                            .load(photoURl)
                            .into(photousuario);
                }

            }
    }
}

