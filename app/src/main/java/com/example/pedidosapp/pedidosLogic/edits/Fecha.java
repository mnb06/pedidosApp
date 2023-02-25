package com.example.pedidosapp.pedidosLogic.edits;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.pedidosapp.R;
import com.example.pedidosapp.pedidosLogic.ArticuloDetail;
import com.example.pedidosapp.pedidosLogic.PedidoCreation;
import com.example.pedidosapp.tabs.Pedidos;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Fecha extends AppCompatActivity {

    Button uploadFecha, elegirFecha;
    DatabaseReference ref, ref2, ref3;
    ArrayList<ArticuloDetail> list;
    TextView nuevaFecha;
    Calendar calendar;
    DatePickerDialog dpd;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fecha);

        list = new ArrayList<>();

        Intent intent = getIntent();
        String pathArticulos = intent.getStringExtra("path");
        String client = intent.getStringExtra("cliente");

        // Enlace UI
        elegirFecha = findViewById(R.id.elegirFecha);
        uploadFecha = findViewById(R.id.uploadFecha);
        nuevaFecha = findViewById(R.id.nuevaFecha);

        String path = "Pedidos/" + intent.getStringExtra("cliente") + "_" + intent.getStringExtra("date");

        ref = FirebaseDatabase.getInstance().getReference();
        ref2 = FirebaseDatabase.getInstance().getReference(path + "/Articulos");
        ref3 = FirebaseDatabase.getInstance().getReference();


        elegirFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                dpd = new DatePickerDialog(Fecha.this, new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker datePicker, int d, int m, int y) {
                        nuevaFecha.setText(y + "-" + (m+1) + "-" + d);
                    }
                }, year, month, day);
                dpd.show();
            }
        });

        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ArticuloDetail articulo = dataSnapshot.getValue(ArticuloDetail.class);
                    list.add(articulo);
                }
                //adapterDetail.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        uploadFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = client + "_" + nuevaFecha.getText().toString();
                uploadData(client, nuevaFecha.getText().toString(), id);
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


    private void deleteOldOrder(String path) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child(path).setValue(null);
        list.clear();
    }
}