package com.example.pedidosapp.pedidosLogic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pedidosapp.R;
import com.example.pedidosapp.articleLogic.Articulo;
import com.example.pedidosapp.pedidosLogic.articles.AddArticle;
import com.example.pedidosapp.pedidosLogic.articles.ArticlesAdapter;
import com.example.pedidosapp.tabs.Pedidos;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PedidoCreation extends AppCompatActivity {

    Spinner cliente;
    Button fecha, upload, cancel, next;
    TextView fechaElegida;
    Calendar calendar;
    DatePickerDialog dpd;
    DatabaseReference ref;

    public static Pedido pedido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos_creation);

        pedido = new Pedido();

        // Conexion UI
        cliente = findViewById(R.id.pedidoCliente);
        fecha = findViewById(R.id.pedidoFecha);
        fechaElegida = findViewById(R.id.fechaElegida);
        next = findViewById(R.id.pedidoNext);
        //upload = findViewById(R.id.pedidoUpload);
        cancel = findViewById(R.id.pedidoCancel);


        // Instancia de la db
        ref = FirebaseDatabase.getInstance().getReference();
        loadSpinnerClientes();


        fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                dpd = new DatePickerDialog(PedidoCreation.this, new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker datePicker, int d, int m, int y) {
                        fechaElegida.setText(y + "-" + (m+1) + "-" + d);
                    }
                }, year, month, day);
                dpd.show();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cliente.getSelectedItem().toString().isEmpty() || fechaElegida.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Ingrese todos los campos.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), AddArticle.class);
                    intent.putExtra("cliente", cliente.getSelectedItem().toString());
                    intent.putExtra("fecha", fechaElegida.getText().toString());
                    startActivity(intent);

                }
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
        // Carga los clientes en el spinner de la creacion de pedidos
        public void loadSpinnerClientes() {
            List<String> clientes = new ArrayList<>();
            ref.child("Clientes").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String nombre = (String) ds.child("nombre").getValue();
                            clientes.add(nombre);
                        }

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(PedidoCreation.this, android.R.layout.simple_dropdown_item_1line, clientes);
                        cliente.setAdapter(arrayAdapter);
                    }
                    //return null;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }
}




