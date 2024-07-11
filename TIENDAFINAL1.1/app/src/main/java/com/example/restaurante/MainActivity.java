package com.example.restaurante;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
//import androidx.navigation.NavController;
//import androidx.navigation.Navigation;
//import androidx.navigation.ui.AppBarConfiguration;
//import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.restaurante.ui.Carrito.CarritoFragment;
import com.example.restaurante.ui.gallery.GalleryFragment;
import com.example.restaurante.ui.home.AllPlatosFragment;
import com.example.restaurante.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;


public class MainActivity extends AppCompatActivity {

    CarritoFragment carritoFragment =  new CarritoFragment();

    HomeFragment menu_home = new HomeFragment();
    AllPlatosFragment AllPlatos = new AllPlatosFragment();
    MenuPerfilActivity menuPerfilActivity = new MenuPerfilActivity();

    Editar_perfil editarPerfil = new Editar_perfil();

    private FirebaseFirestore mfirestore;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mfirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();



        LoadFragment(AllPlatos);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        BottomNavigationView navigation = findViewById(R.id.bottomnavegación);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem Item) {
            if (mAuth.getUid() == null){
                int id = Item.getItemId();
                if (id == R.id.menu_home)
                    LoadFragment(menu_home);
                else if (id == R.id.menu_sugeridos)
                    LoadFragment(menu_home);
                else if (id == R.id.menu_carrito)
                    LoadFragment(carritoFragment);
                else if (id == R.id.menu_perfil)
                    loadActivity(menuPerfilActivity);

            }else{
                int id = Item.getItemId();
                if (id == R.id.menu_home)
                    LoadFragment(menu_home);
                else if (id == R.id.menu_sugeridos)
                    LoadFragment(menu_home);
                else if (id == R.id.menu_carrito)
                    LoadFragment(carritoFragment);
                else if (id == R.id.menu_perfil)
                    loadActivityedit(editarPerfil);
                return true;
            }

            return true;
        }

    };

    public  void LoadFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }

    public void loadActivity(MenuPerfilActivity activity) {
        Intent intent = new Intent(this, activity.getClass());
        startActivity(intent);
    }

    public void loadActivityedit(Editar_perfil activity) {
        Intent intent = new Intent(this, activity.getClass());
        startActivity(intent);
    }



}