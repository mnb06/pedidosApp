package com.example.pedidosapp.pedidosLogic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.pedidosapp.R;
import com.example.pedidosapp.articleLogic.Articulo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PedidoDetail extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference database;
    AdapterDetail adapterDetail;
    public static ArrayList<Articulo> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido_detail);

        Intent intent = getIntent();
        String path = "Pedidos/" + intent.getStringExtra("cliente") + "_" + intent.getStringExtra("fecha") + "/Articulos";

        // Lista dinamica
        recyclerView = findViewById(R.id.detailList);

        // Database instance
        database = FirebaseDatabase.getInstance().getReference(path);

        // Configuracion de la lista
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        list = new ArrayList<>();
        adapterDetail = new AdapterDetail(getApplicationContext(), list);
        recyclerView.setAdapter(adapterDetail);




        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Articulo articulo = dataSnapshot.getValue(Articulo.class);
                    list.add(articulo);
                }
                adapterDetail.notifyDataSetChanged();

                //return null;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}