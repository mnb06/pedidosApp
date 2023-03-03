package com.example.pedidosapp.pedidosLogic.edits;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pedidosapp.Inicio;
import com.example.pedidosapp.R;
import com.example.pedidosapp.articleLogic.Articulo;
import com.example.pedidosapp.tabs.Pedidos;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Articulos extends AppCompatActivity implements Serializable{


    RecyclerView recyclerView;
    DatabaseReference ref, database;
    ArticlesEditAdapter articlesEditAdapter;

    public static ArrayList<Articulo> list, cargados;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_article);

        String cliente = getIntent().getStringExtra("cliente");
        String fecha = getIntent().getStringExtra("date");
        String path = getIntent().getStringExtra("path");


        Button upload = findViewById(R.id.pedidoEditArticlesConfirm);
        FloatingActionButton add = findViewById(R.id.editAddArticle);


        // Lista dinamica
        recyclerView = findViewById(R.id.editArticleList);

        // Database reference instance
        ref = FirebaseDatabase.getInstance().getReference(path);
        database = FirebaseDatabase.getInstance().getReference("Articulos");


        // Configuracion de la lista
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        list = new ArrayList<>();
        cargados = new ArrayList<>();
        articlesEditAdapter = new ArticlesEditAdapter(getApplicationContext(), list, path, cargados);
        recyclerView.setAdapter(articlesEditAdapter);



        // Añade a la lista los elementos cargados en la bd
        ref.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // Bucle que carga todos los pedidos en la lista
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Articulo articulo = dataSnapshot.getValue(Articulo.class);
                    list.add(articulo);
                }
                articlesEditAdapter.notifyDataSetChanged();
                //return null;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Articulo articulo = dataSnapshot.getValue(Articulo.class);
                    cargados.add(articulo);
                }
                //articlesEditAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });


        add.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), AddEditArticle.class);
            intent.putExtra("cliente", cliente);
            intent.putExtra("fecha", fecha);
            intent.putExtra("path", path);
            startActivity(intent);
            finish();
        });


        upload.setOnClickListener(view -> {
            Toast.makeText(getApplicationContext(), "Pedido modificado correctamente",
                    Toast.LENGTH_LONG).show();
            finish();
        });
    }
        private void uplodData(String client, String fecha, String id) {

            // Hash donde se almacenan los datos a subir
            Map<String, Object> datosPedido = new HashMap<>();

            // Insercion de los datos en el hash
            datosPedido .put("cliente", client);
            datosPedido.put("fecha", fecha);

            // Se crea un hijo (similar a una tabla) y se ingresan los valores
            ref.child(id).setValue(datosPedido);
            Pedidos.list.clear();
            Intent intent = new Intent(getApplicationContext(), Inicio.class);
            startActivity(intent);
            finish();
        }

        private void uploadArticles(Articulo articulo, String id) {
            // Hash donde se almacenan los datos a subir
            Map<String, Object> articulosSeleccionados = new HashMap<>();

            // Insercion de los datos en el hash
            articulosSeleccionados .put("nombre", articulo.getNombre());
            articulosSeleccionados.put("cantidad", articulo.getStock());


            // Se crea un hijo (similar a una tabla) y se ingresan los valores
            ref.child(id).child("Articulos").child(articulo.getNombre()).setValue(articulosSeleccionados);

            Toast.makeText(getApplicationContext(), "Pedido cargado correctamente",
                    Toast.LENGTH_LONG).show();
            finish();
        }

}
