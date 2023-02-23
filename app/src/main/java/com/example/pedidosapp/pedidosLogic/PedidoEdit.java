package com.example.pedidosapp.pedidosLogic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pedidosapp.R;
import com.example.pedidosapp.pedidosLogic.articles.AddArticle;
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

public class PedidoEdit extends AppCompatActivity {

    Spinner cliente;
    private Button fecha, cancel, next;
    TextView fechaElegida;
    Calendar calendar;
    DatePickerDialog dpd;

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
        String name = intent.getStringExtra("cliente");
        String fe = intent.getStringExtra("fecha");

        pedido = new Pedido();

        // Conexion UI
        cliente = findViewById(R.id.pedidoCliente);
        fecha = findViewById(R.id.pedidoFecha);
        fechaElegida = findViewById(R.id.fechaElegida);
        cancel = (Button) findViewById(R.id.pedidoCancel);
        next = (Button) findViewById(R.id.pedidoEditNext);
        ref = db.getReference();

        // Instancia de la db


        DatabaseReference editar = db.getReference("Pedidos");
        String id = name + "_" + fe;
        editar.child(id).setValue(null);

        loadSpinner();


        fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                dpd = new DatePickerDialog(PedidoEdit.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int d, int m, int y) {
                        m++;
                        String fechaSeleccionada = y + "-" + m + "-" + d;
                        fechaElegida.setText(fechaSeleccionada);
                    }
                } ,year,month,day);
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
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(PedidoEdit.this, android.R.layout.simple_dropdown_item_1line, clientes);
                    cliente.setAdapter(arrayAdapter);
                }
    }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error. Intente nuevamente", Toast.LENGTH_SHORT).show();
            }
        });
    }
}