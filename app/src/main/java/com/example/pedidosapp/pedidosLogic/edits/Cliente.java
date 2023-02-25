package com.example.pedidosapp.pedidosLogic.edits;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pedidosapp.R;
import com.example.pedidosapp.articleLogic.Articulo;
import com.example.pedidosapp.pedidosLogic.ArticuloDetail;
import com.example.pedidosapp.pedidosLogic.Pedido;
import com.example.pedidosapp.tabs.Pedidos;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Cliente extends AppCompatActivity {

    Spinner spinner;
    Button uploadClient;
    DatabaseReference ref, ref2, ref3;
    ArrayList<ArticuloDetail> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);

        list = new ArrayList<>();

        Intent intent = getIntent();
        String pathArticulos = intent.getStringExtra("path");
        String date = intent.getStringExtra("date");

        // Enlace UI
        spinner = findViewById(R.id.pedidoEditCliente);
        uploadClient = findViewById(R.id.pedidoEditClienteConfirm);


        String path = "Pedidos/" + intent.getStringExtra("cliente") + "_" + intent.getStringExtra("date");

        ref = FirebaseDatabase.getInstance().getReference();
        ref2 = FirebaseDatabase.getInstance().getReference(path + "/Articulos");
        ref3 = FirebaseDatabase.getInstance().getReference();


        loadSpinner();


        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ArticuloDetail articulo = dataSnapshot.getValue(ArticuloDetail.class);
                    list.add(articulo);
                }
                //adapterDetail.notifyDataSetChanged();

                //return null;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });



        uploadClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = spinner.getSelectedItem() + "_" + date;
                uploadData(spinner.getSelectedItem().toString(), date, id);
                for (ArticuloDetail articulo: list) {
                    uploadArticles(articulo,id);

                }
                deleteOldOrder(path);
            }
        });
    }


    // Metodo para subir el cliente y fecha
    private void uploadData(String client, String fecha, String id) {

        // Hash donde se almacenan los datos a subir
        Map<String, Object> datosPedido = new HashMap<>();

        // Insercion de los datos en el hash
        datosPedido.put("cliente", client);
        datosPedido.put("fecha", fecha);

        // Se crea un hijo (similar a una tabla) y se ingresan los valores
        ref.child("Pedidos").child(id).setValue(datosPedido);
        Pedidos.list.clear();
        finish();
    }




    // Metodo para subir los articulos al nuevo pedido
    private void uploadArticles(ArticuloDetail articulo, String id) {

        Map<String, Object> articulosSeleccionados = new HashMap<>();

        articulosSeleccionados.put("nombre", articulo.getNombre());
        articulosSeleccionados.put("cantidad", articulo.getCantidad());

        // Se crea un hijo (similar a una tabla) y se ingresan los valores
        ref3.child("Pedidos").child(id).child("Articulos").child(articulo.getNombre()).setValue(articulosSeleccionados);
    }


    private void deleteOldOrder(String path){

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child(path).setValue(null);
        list.clear();
    }


    // Carga los clientes en el spinner
    private void loadSpinner() {
        List<String> clientes = new ArrayList<>();
        ref.child("Clientes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot ds : snapshot.getChildren()){
                        String nombre = ds.child("nombre").getValue().toString();
                        clientes.add(nombre);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Cliente.this, android.R.layout.simple_dropdown_item_1line, clientes);
                    spinner.setAdapter(arrayAdapter);
                }
                //return null;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error. Intente nuevamente", Toast.LENGTH_SHORT).show();
            }
        });
    }
}