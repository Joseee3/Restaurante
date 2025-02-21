package com.example.restaurante.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurante.PlatosDetailActivity;
import com.example.restaurante.Entities.Contact;
import com.example.restaurante.R;
import com.google.gson.Gson;


import java.util.List;

public class ContactItemAdapter extends RecyclerView.Adapter<ContactItemAdapter.ContactItemViewHolder> {
    private final List<Contact> mData;
    public ContactItemAdapter(List<Contact> data) {
        this.mData = data;
    }

    @NonNull
    @Override
    public ContactItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // definir que layout se debe usar
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_platos, parent, false);
        return new ContactItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactItemViewHolder holder, int position) {
        View view = holder.itemView;
        Contact data = mData.get(position);

//        TextView tvContactName = view.findViewById(R.id.tvContactName);
//        TextView tvContactLastName = view.findViewById(R.id.tvContactLastName);
//        TextView tvContactPhoneNumber = view.findViewById(R.id.tvContactPhoneNumber);
//
//        tvContactName.setText(data.name);
//        tvContactLastName.setText(data.lastName);
//        tvContactPhoneNumber.setText(data.phoneNumber);


        LinearLayout itemLayoutContact = view.findViewById(R.id.itemLayoutContact);

        itemLayoutContact.setOnClickListener(v -> {
            Intent intent = new Intent(view.getContext(), PlatosDetailActivity.class);
            String sData = new Gson().toJson(data); // convierte el objeto en un string JSON
            intent.putExtra("contact", sData);
            view.getContext().startActivity(intent);
        });


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ContactItemViewHolder extends RecyclerView.ViewHolder {

        public ContactItemViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}