package com.example.pedidosapp.pedidosLogic.articles;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.pedidosapp.R;
import com.example.pedidosapp.articleLogic.Articulo;
import com.example.pedidosapp.pedidosLogic.AdapterPedido;
import com.example.pedidosapp.pedidosLogic.Pedido;
import com.example.pedidosapp.pedidosLogic.PedidoCreation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddArticle extends AppCompatActivity {

    private Button edit,delete, upload;

    private TextView options;

    Dialog createDialog;
    RecyclerView recyclerView;
    DatabaseReference database;
    ArticlesAdapter adapterArticleAdd;

    public static ArrayList<Articulo> list;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_article);



        // Lista dinamica
        recyclerView = findViewById(R.id.addArticleList);

        // Database instance
        database = FirebaseDatabase.getInstance().getReference("Articulos");

        // Configuracion de la lista
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        list = new ArrayList<>();
        adapterArticleAdd = new ArticlesAdapter(getApplicationContext(), list);
        recyclerView.setAdapter(adapterArticleAdd);


        createDialog = new Dialog(getApplicationContext());


        // Añade a la lista los elementos cargados en la bd
        database.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // Bucle que carga todos los pedidos en la lista
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Articulo articulo = dataSnapshot.getValue(Articulo.class);
                    list.add(articulo);

                }
                adapterArticleAdd.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });


    }

}