package com.example.pedidosapp.pedidosLogic;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.pedidosapp.R;
import com.example.pedidosapp.pedidosLogic.edits.Cliente;
import com.example.pedidosapp.pedidosLogic.edits.Fecha;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PedidoEdit extends AppCompatActivity {



    FirebaseDatabase db;
    DatabaseReference ref;

    static DatabaseReference mRootReference;

    public static Pedido pedido;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos_edit);

        db = FirebaseDatabase.getInstance();
        mRootReference = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        String client = intent.getStringExtra("cliente");
        String date = intent.getStringExtra("fecha");



        // Conexion UI
        Button clientChange = findViewById(R.id.clientChange);
        Button dateChange = findViewById(R.id.dateChange);
        Button articlesChange = findViewById(R.id.articlesChange);
        Button cancel = findViewById(R.id.pedidoEditCancel);



        String path = "Pedidos/" + client + "_" + date + "/Articulos";


        clientChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PedidoEdit.this, Cliente.class);
                intent.putExtra("date", date);
                intent.putExtra("path", path);
                intent.putExtra("cliente", client);
                startActivity(intent);
            }
        });

        dateChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PedidoEdit.this, Fecha.class);
                intent.putExtra("date", date);
                intent.putExtra("path", path);
                intent.putExtra("cliente", client);
                startActivity(intent);
            }
        });



        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}