package com.example.restaurante;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.restaurante.Entities.Profile;
import com.example.restaurante.service.IProfileService;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Retrofit;

public class ProfileActivity extends AppCompatActivity {


    //para guardar los datos del usuario
    EditText mEtNombre,  mEtApellidos, mEtEmail, mEtContraseña, mEtphone;
    Button btnregistro;
    FirebaseFirestore mfirestore;
    FirebaseAuth mAuth;

    //para guardar imagen del usuario
    ImageView imageedit;
    Button btn_add, btn_dele;
    StorageReference storageReference;
    String storage_path = "ImagenUser/*";
    private static final int COD_SEL_STORAGE = 200;
    private static final int COD_SEL_IMAGE=300;
    private Uri inagen_url;
    String photo = "photo";
    String idd;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //instancia para la imagen del usuario
        storageReference = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(this);

        //instancia para cargar los datos personales
        mfirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        //instanciar la foto y botones de agregar y eliminar; agregar funcionalidad a los botones de agregar y eliminar foto
        imageedit = findViewById(R.id.imageedit);
        btn_add = findViewById(R.id.btn_photo);
        btn_dele = findViewById(R.id.btn_remove_photo);

        //instabcia de datos personales
        mEtNombre = findViewById(R.id.etnombre);
          mEtApellidos = findViewById(R.id.etApellido);
          mEtEmail = findViewById(R.id.etMail);
        mEtContraseña = findViewById(R.id.etContra);
        mEtphone = findViewById(R.id.etPhone);
        btnregistro = findViewById(R.id.btnRegister);


        //condicional para regitrar o para modificar usuario
        if (mAuth.getUid() == null){

            btnregistro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String nameUser = mEtNombre.getText().toString().trim();
                    String apellidoUser = mEtApellidos.getText().toString().trim();
                    String emailUser = mEtEmail.getText().toString().trim();
                    String contraseñaUser = mEtContraseña.getText().toString().trim();
                    String phone = mEtphone.getText().toString().trim();
                    String imagen = imageedit.toString().trim();

                    if (nameUser.isEmpty() && apellidoUser.isEmpty() && emailUser.isEmpty() && contraseñaUser.isEmpty() && phone.isEmpty()){
                        Toast.makeText(ProfileActivity.this, "complete los datos", Toast.LENGTH_SHORT).show();
                    }else{
                        registreUser(nameUser,apellidoUser, emailUser, contraseñaUser,phone, imagen);
                    }
                }
            });
        }else{
            btnregistro.setText("Actualizar");
            getuser(mAuth.getUid());
            btnregistro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String nameUser = mEtNombre.getText().toString().trim();
                    String apellidoUser = mEtApellidos.getText().toString().trim();
                    String emailUser = mEtEmail.getText().toString().trim();
                    String contraseñaUser = mEtContraseña.getText().toString().trim();
                    String phone = mEtphone.getText().toString().trim();
                    String imagen = imageedit.toString().trim();

                    if (nameUser.isEmpty() && apellidoUser.isEmpty() && emailUser.isEmpty() && contraseñaUser.isEmpty() && phone.isEmpty()){
                        Toast.makeText(ProfileActivity.this, "complete los datos", Toast.LENGTH_SHORT).show();
                    }else{
                        updateuser(nameUser,apellidoUser, emailUser, contraseñaUser,phone, imagen);
                    }

                }
            });
        }

        //funcionalidad del boton de registro

//        btnregistro.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String nameUser = mEtNombre.getText().toString().trim();
//                String apellidoUser = mEtApellidos.getText().toString().trim();
//                String emailUser = mEtEmail.getText().toString().trim();
//                String contraseñaUser = mEtContraseña.getText().toString().trim();
//                String phone = mEtphone.getText().toString().trim();
//                String imagen = imageedit.toString().trim();
//
//                if (nameUser.isEmpty() && apellidoUser.isEmpty() && emailUser.isEmpty() && contraseñaUser.isEmpty() && phone.isEmpty()){
//                    Toast.makeText(ProfileActivity.this, "complete los datos", Toast.LENGTH_SHORT).show();
//                }else{
//                    registreUser(nameUser,apellidoUser, emailUser, contraseñaUser,phone, imagen);
//                }
//            }
//        });

        //evento boton agregar
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPhoto();
            }
        });
        //evento boton eliminar foto
        btn_dele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("photo", "");
                mfirestore.collection("Usuario").endAt(map);
                Toast.makeText(ProfileActivity.this, "Foto eliminada", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadPhoto() {
        //SIRVE PARA ABRIR LA GALERIA
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");//PARA VER TODAS LA IMAGENES

        startActivityForResult(i, COD_SEL_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK){
            if (requestCode == COD_SEL_IMAGE){
                inagen_url = data.getData();
                subirPhoto(inagen_url);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void subirPhoto(Uri inagenUrl) {
        //se ase uso del progressDialog para mostrar un mesaje y a la vez se visualise
//        progressDialog.setMessage("Actualizar foto");
//        progressDialog.show();
        //se declara la variable donde colocalos el STORAGE_PATCH que es la carpeta donde se va aguardar la imagen, se le pasa la variable photo, y tambien

        DocumentReference id = mfirestore.collection("user").document();
        String rute_storage_photo = storage_path + "" + photo + "" + mAuth.getUid();
        StorageReference reference = storageReference.child(rute_storage_photo);
        reference.putFile(inagenUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> UriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!UriTask.isSuccessful());
                    if(UriTask.isSuccessful()){
                        UriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String download = uri.toString();
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("photo", download);
                                mfirestore.collection("user").document().update(map);
           //                     Toast.makeText(ProfileActivity.this, "Foto actualizada", Toast.LENGTH_SHORT).show();
//                                progressDialog.dismiss();
                            }
                        });
                    }


            }     }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileActivity.this, "Error al cargar foto", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registreUser(String nameUser, String apellidoUser, String emailUser, String contrasenaUser, String phoneuser, String imagen) {
        mAuth.createUserWithEmailAndPassword(emailUser, contrasenaUser).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                String id = mAuth.getCurrentUser().getUid();
                Map<String, Object> map = new HashMap<>();
                map.put("id", id);

                map.put("name", nameUser);
                map.put("apellidos", apellidoUser);
                map.put("email", emailUser);
                map.put("contraseña", contrasenaUser);
                map.put("phone", phoneuser);
                map.put("photo", imagen);


                mfirestore.collection("user").document(id).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        finish();
                      //  startActivity(new Intent(ProfileActivity.this,MainActivity.class));
                        Toast.makeText(ProfileActivity.this, "Usuario registrado con exito", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfileActivity.this, "Error al guardar", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileActivity.this, "ERROR AL REGISTRAR", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //para actualizar los datos
    private void updateuser (String nameUser, String apellidoUser, String emailUser, String contrasenaUser, String phoneuser, String imagen){
        Map<String, Object> map = new HashMap<>();
        map.put("name", nameUser);
        map.put("apellidos", apellidoUser);
        map.put("email", emailUser);
        map.put("contraseña", contrasenaUser);
        map.put("phone", phoneuser);
        map.put("photo", imagen);

        mfirestore.collection("user").document(mAuth.getUid()).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "Actualizado exitosamente", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al actualizar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //para getear los datos al registrar
    private void getuser(String id){
        mfirestore.collection("user").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String name = documentSnapshot.getString("name");
                String apellido = documentSnapshot.getString("apellidos");
                String email = documentSnapshot.getString("email");
                String contra = documentSnapshot.getString("contraseña");
                String phone = documentSnapshot.getString("phone");
                String photo = documentSnapshot.getString("photo");

                mEtNombre.setText(name);
                mEtApellidos.setText(apellido);
                mEtEmail.setText(email);
                mEtContraseña.setText(contra);
                mEtphone.setText(phone);
                try{
                    if (!photo.equals("")){
                        Toast toast = Toast.makeText(getApplicationContext(), "Cargando foto", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP,0,200);
                        toast.show();

                        Glide.with(ProfileActivity.this)
                                .load(photo)
                                .into(imageedit);
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