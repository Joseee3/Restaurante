package com.example.restaurante.ui.usuario;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.restaurante.Entities.Profile;
import com.example.restaurante.R;
import com.example.restaurante.service.IProfileService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileFragment extends Fragment {

    Retrofit retrofit;
    IProfileService service;
    EditText mEtNombre;
    EditText mEtApellidos;
    EditText mEtEmail;
    EditText mEtContraseña;
    EditText mEtAddress;
    EditText mEtPhone;
    Profile profile;

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        Button btnSave = view.findViewById(R.id.btnRegister);
        mEtNombre = view.findViewById(R.id.etnombre);
        mEtApellidos = view.findViewById(R.id.etApellido);
        mEtEmail = view.findViewById(R.id.etMail);
        mEtContraseña = view.findViewById(R.id.etContra);
        mEtAddress = view.findViewById(R.id.etAddress);
        mEtPhone = view.findViewById(R.id.etPhone);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://661d24c2e7b95ad7fa6c4400.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(IProfileService.class);

        setUpProfile();

        btnSave.setOnClickListener(v -> {
            saveOrUpdateProfile();
        });

        Bundle arguments = getArguments();
        if (arguments != null) {
            String userEmail = arguments.getString("email");
            String userPassword = arguments.getString("password");
            String userName = arguments.getString("name");
            String userLastName = arguments.getString("lastname");
            String userAddress = arguments.getString("address");
            String userPhone = arguments.getString("phone");
        }

        return view;
    }

    private void setUpProfile() {
        service.find(1).enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                if (response.isSuccessful()) {
                    profile = response.body();
                    mEtNombre.setText(profile.name);
                    mEtEmail.setText(profile.email);
                    mEtContraseña.setText(profile.password);
                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable throwable) {

            }
        });
    }

   private void saveOrUpdateProfile() {
    String name = mEtNombre.getText().toString();
    String apellido = mEtApellidos.getText().toString();
    String email = mEtEmail.getText().toString();
    String password = mEtContraseña.getText().toString();
    String address = mEtAddress.getText().toString();
    String phone = mEtPhone.getText().toString();

    if (name.isEmpty() || apellido.isEmpty() || email.isEmpty() || password.isEmpty()) {
        Toast.makeText(getActivity(), "Todos los campos deben ser llenados", Toast.LENGTH_SHORT).show();
        return;
    }
    if (!isValidEmail(email)) {
        Toast.makeText(getActivity(), "El correo electrónico no es válido", Toast.LENGTH_SHORT).show();
        return;
    }

    // Buscar perfil por correo electrónico
    service.findByEmail(email).enqueue(new Callback<Profile>() {
    @Override
    public void onResponse(Call<Profile> call, Response<Profile> response) {
        if (response.isSuccessful()) {
            Profile profile = response.body();
            if (profile != null) {
                // Si hay un perfil con ese correo electrónico, mostrar un mensaje
                Toast.makeText(getActivity(), "Ya existe un usuario con ese correo electrónico", Toast.LENGTH_SHORT).show();
            } else {
                // Si no hay perfiles con ese correo electrónico, crear un nuevo perfil
                createProfile(name, apellido, email, password, address, phone);
            }
        } else {
            Log.e("ProfileFragment", "Error response from API: " + response.message());
        }
    }
    @Override
    public void onFailure(Call<Profile> call, Throwable t) {
        Log.e("ProfileFragment", "Error calling API", t);
    }
});
}

    private void createProfile(String name, String apellido, String email, String password, String address, String phone) {
        Profile newProfile = new Profile();
        newProfile.setName(name);
        newProfile.setLastname(apellido);
        newProfile.setEmail(email);
        newProfile.setPassword(password);
        newProfile.setAddress(address);
        newProfile.setPhone(phone);

        Call<Profile> call = service.create(newProfile);
        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), "La información ha sido guardada correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("ProfileFragment", "Error response from API: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                Log.e("ProfileFragment", "Error calling API", t);
            }
        });
    }

    private void updateProfile(String name, String apellido, String email, String password, String address, String phone) {
        profile.setName(name);
        profile.setLastname(apellido);
        profile.setEmail(email);
        profile.setPassword(password);

        Call<Profile> call = service.update(profile.getId(), profile);
        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), "La información ha sido actualizada correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Error en la actualización", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                Toast.makeText(getActivity(), "Error en la actualización", Toast.LENGTH_SHORT).show();
            }
        });
    }
}