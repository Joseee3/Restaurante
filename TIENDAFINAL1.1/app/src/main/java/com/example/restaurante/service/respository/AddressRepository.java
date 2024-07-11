package com.example.restaurante.service.respository;

import com.example.restaurante.Entities.Direccion;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AddressRepository {

    private final static String ADDRESS_COLLECTION = "direcciones";

    public static void getAddress(CallbackGetAddress callback) {

        // FirebaseStore and FirebaseAuth setup
        final FirebaseAuth AUTH = FirebaseAuth.getInstance();
        final FirebaseFirestore STORAGE = FirebaseFirestore.getInstance();

        // Current User
        FirebaseUser user = AUTH.getCurrentUser();
        String userUid = user.getUid();

        STORAGE.collection(ADDRESS_COLLECTION).get().addOnSuccessListener(queryDocumentSnapshots -> {
            try {
                List<Direccion> addresses = new ArrayList<>();
                // Filter address by user uid
                for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                    Direccion address = document.toObject(Direccion.class);

                    if (userUid.equals(address.getId_user())) {
                        addresses.add(address);
                    }
                }
                callback.onSuccess(addresses);
            } catch (NullPointerException ex) {
                callback.onFailure("Problemas al identificar al usuario");
            } catch (Exception ex) {
                callback.onFailure("Error desconocido");
            }
        });
    }

    public interface CallbackGetAddress {
        void onSuccess(List<Direccion> addresses);

        void onFailure(String msgError);
    }

}
