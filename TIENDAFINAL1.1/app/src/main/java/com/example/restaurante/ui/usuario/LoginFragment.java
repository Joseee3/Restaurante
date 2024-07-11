package com.example.restaurante.ui.usuario;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
//import androidx.navigation.NavController;
//import androidx.navigation.Navigation;

import com.example.restaurante.Entities.Profile;
import com.example.restaurante.R;
import com.example.restaurante.service.IProfileService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginFragment extends Fragment {

    EditText mEtEmail;
    EditText mEtPassword;
    IProfileService service;
    Retrofit retrofit;

    private OnLoginSuccessListener listener;

    private UsuarioViewModel userViewModel;

    public void setOnLoginSuccessListener(OnLoginSuccessListener listener) {
        this.listener = listener;
    }

    TextView nombreusuario;
    TextView correousuario;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        //testValidationMethods();

        mEtEmail = view.findViewById(R.id.etEmail);
        mEtPassword = view.findViewById(R.id.etPassword);
        Button btnLogin = view.findViewById(R.id.btnLogin);

        userViewModel = new ViewModelProvider(requireActivity()).get(UsuarioViewModel.class);


        retrofit = new Retrofit.Builder()
                .baseUrl("https://661d24c2e7b95ad7fa6c4400.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(IProfileService.class);

        btnLogin.setOnClickListener(v -> {
            String email = mEtEmail.getText().toString();
            String password = mEtPassword.getText().toString();

            if (!isValidEmail(email)) {
                Toast.makeText(getActivity(), "El correo electrónico no es válido", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isValidPassword(password)) {
                Toast.makeText(getActivity(), "La contraseña no es válida", Toast.LENGTH_SHORT).show();
                return;
            }

            login(email, password);
        });

        return view;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 8;
    }

    public interface OnLoginSuccessListener {

    void onLoginSuccess(Profile profile);
}

//    private void login(String email, String password) {
//    service.getAllUsers().enqueue(new Callback<List<Profile>>() {
//        @Override
//        public void onResponse(Call<List<Profile>> call, Response<List<Profile>> response) {
//            if (response.isSuccessful()) {
//                List<Profile> profiles = response.body();
//                for (Profile profile : profiles) {
//                    if (profile.getEmail().equals(email) && profile.getPassword().equals(password)) {
//                        Toast.makeText(getActivity(), "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                }
//                Toast.makeText(getActivity(), "Correo electrónico o contraseña incorrectos", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(getActivity(), "Error en el inicio de sesión", Toast.LENGTH_SHORT).show();
//            }
//        }
//
//        @Override
//        public void onFailure(Call<List<Profile>> call, Throwable t) {
//            Toast.makeText(getActivity(), "Error en el inicio de sesión", Toast.LENGTH_SHORT).show();
//        }
//    });
//}



             private void login(String email, String password) {
                service.getAllUsers().enqueue(new Callback<List<Profile>>() {
                    @Override
                    public void onResponse(Call<List<Profile>> call, Response<List<Profile>> response) {
                        if (response.isSuccessful()) {
                            List<Profile> profiles = response.body();

                            for (Profile profile : profiles) {
                                if (profile.getEmail().equals(email) && profile.getPassword().equals(password)) {
                                    // Cuando el inicio de sesión sea exitoso, llama a onLoginSuccess
                                   userViewModel.setUserProfile(profile);
                                    Toast.makeText(getActivity(), "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();

                                    // Navega a la pantalla de inicio
//                                    NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
//                                    navController.navigate(R.id.nav_home);
//                                    return;
                                }
                            }
                            Toast.makeText(getActivity(), "Correo electrónico o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Error en el inicio de sesión", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Profile>> call, Throwable t) {
                        Toast.makeText(getActivity(), "Error en el inicio de sesión", Toast.LENGTH_SHORT).show();
                    }
                });
            }

}