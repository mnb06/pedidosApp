package com.example.pedidosapp.pedidosLogic.articles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pedidosapp.R;
import com.example.pedidosapp.articleLogic.Articulo;
import com.example.pedidosapp.pedidosLogic.Pedido;
import com.example.pedidosapp.tabs.Pedidos;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddArticle extends AppCompatActivity {

    private Button edit;
    private Button delete;
    private Button add;

    private TextView options;

    Dialog createDialog;
    RecyclerView recyclerView;
    DatabaseReference database;
    DatabaseReference ref;
    ArticlesAdapter articlesAdapter;

    public static ArrayList<Articulo> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_article);

        String cliente = getIntent().getStringExtra("cliente");
        String fecha = getIntent().getStringExtra("fecha");


        Button upload = findViewById(R.id.pedidoUpload);


        // Lista dinamica
        recyclerView = findViewById(R.id.addArticleList);

        // Database instance
        database = FirebaseDatabase.getInstance().getReference("Articulos");
        ref = FirebaseDatabase.getInstance().getReference("Pedidos");
        // Configuracion de la lista
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        list = new ArrayList<>();
        articlesAdapter = new ArticlesAdapter(getApplicationContext(), list);
        recyclerView.setAdapter(articlesAdapter);


        createDialog = new Dialog(getApplicationContext());


        // Añade a la lista los elementos cargados en la bd
        database.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // Bucle que carga todos los pedidos en la lista
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Articulo articulo = dataSnapshot.getValue(Articulo.class);
                    list.add(articulo);
                }
                articlesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = cliente + "_" + fecha;
                uploadData(cliente, fecha, id);
                for (Articulo articulo : articlesAdapter.elegidos
                     ) {
                    uploadArticles(articulo, id);
                }
                finish();
            }
        });
    }
        private void uploadData(String client, String fecha, String id) {

            // Hash donde se almacenan los datos a subir
            Map<String, Object> datosPedido = new HashMap<>();

            // Insercion de los datos en el hash
            datosPedido .put("cliente", client);
            datosPedido.put("fecha", fecha);

            // Se crea un hijo (similar a una tabla) y se ingresan los valores
            ref.child(id).setValue(datosPedido);
            Pedidos.list.clear();
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
