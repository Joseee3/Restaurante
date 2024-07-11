package com.example.restaurante.service.respository;

import android.util.Log;

import com.example.restaurante.Entities.Pedido;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class OrderRepository {

    private static final String ORDER_COLLECTION = "pedidos";


    public static void getOrderById(String orderId, Callback callback) {
        FirebaseFirestore DB = FirebaseFirestore.getInstance();
        DB.collection(ORDER_COLLECTION)
                .document(orderId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Pedido order = queryDocumentSnapshots.toObject(Pedido.class);
                    Log.d("GET_ORDER", "Loaded: " + order.toString());
                    callback.onSuccess(order);
                })
                .addOnFailureListener(e -> callback.onFailure("Error obteniendo detalles"));
    }

    public static void getUserOrders(CallbackGetOrders callback) {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore DB = FirebaseFirestore.getInstance();

        DB.collection(ORDER_COLLECTION)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Pedido> orders = new ArrayList<>();

                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        Pedido order = document.toObject(Pedido.class);
                        if (order.getUserUid().equals(currentUser.getUid())) {
                            orders.add(order);
                        }
                    }
                    callback.onSuccess(orders);
                })
                .addOnFailureListener(e -> {
                    callback.onFailure("Error al cargar el historial");
                });
    }


    public static void createOrder(Pedido newOrder, Callback callback) {

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        newOrder.setUserUid(user.getUid());

        final FirebaseFirestore DB = FirebaseFirestore.getInstance();
        DocumentReference newOrderRef = DB.collection(ORDER_COLLECTION).document();

        String orderId = newOrderRef.getId();
        newOrder.setId(orderId);

        // Save order
        newOrderRef.set(newOrder)
                .addOnSuccessListener(unused -> {
                    callback.onSuccess(newOrder);
                })
                .addOnFailureListener(e -> {
                    callback.onFailure("Error al completar la compra");
                });
    }

    public interface Callback {
        void onSuccess(Pedido order);

        void onFailure(String msg);
    }

    public interface CallbackGetOrders {
        void onSuccess(List<Pedido> orders);

        void onFailure(String msg);
    }

}
