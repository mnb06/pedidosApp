package com.example.pedidosapp.pedidosLogic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;

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

    private Button fecha, upload, cancel, add;

    Spinner cliente, articulo;
    TextView fechaElegida;
    Calendar calendar;
    DatePickerDialog dpd;
    DatabaseReference ref;

    Context context;

    static DatabaseReference mRootReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos_edit);

        mRootReference = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        String id = intent.getStringExtra("name");

        // Conexion UI
        cliente = findViewById(R.id.pedidoCliente);
        articulo = findViewById(R.id.pedidoArticulo);
        fecha = findViewById(R.id.pedidoFecha);
        fechaElegida = findViewById(R.id.fechaElegida);
        upload = (Button) findViewById(R.id.pedidoUpload);
        cancel = (Button) findViewById(R.id.pedidoCancel);
        add = (Button) findViewById(R.id.addArticulo);

        // Instancia de la db
        ref = FirebaseDatabase.getInstance().getReference();
        loadSpinner();
        loadSpinnerArticulos();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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
                    String fechaSeleccionada = y + "/" + m + "/" + d;
                    fechaElegida.setText(fechaSeleccionada);
                }
            } ,year,month,day);
            dpd.show();
        }


    });

        upload.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String client = cliente.getSelectedItem().toString();
            String fecha = fechaElegida.getText().toString();
            if (client.isEmpty() || fecha.isEmpty()){
                Toast.makeText(getApplicationContext(), "Ingrese todos los campos.",
                        Toast.LENGTH_LONG).show();
            }else {
                // Llamada al metodo que sube los datos a la db
                uploadData(client, fecha);
            }
        }
    });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
}

    private void uploadData(String client, String fecha) {
        // Hash donde se almacenan los datos a subir
        Map<String, Object> datosPedido = new HashMap<>();

        // Insercion de los datos en el hash
        datosPedido .put("Cliente", client);
        datosPedido.put("Fecha", fecha);
        //datosPedido.put("Articulos", direction);

        // Se crea un hijo (similar a una tabla) y se ingresan los valores
        String id = client;
        ref.child("Pedidos").child(id).setValue(datosPedido);

        // Notificacion Toast para mostrar si el pedido fue cargado
        Toast.makeText(getApplicationContext(), "Pedido cargado exitosamente.",
                Toast.LENGTH_LONG).show();
        Pedidos.list.clear();
        finish();
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

            }
        });
    }

    private void loadSpinnerArticulos() {
        List<String> articulos = new ArrayList<>();
        ref.child("Articulos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot ds : snapshot.getChildren()){
                        String nombre = ds.child("nombre").getValue().toString();
                        articulos.add(nombre);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(PedidoEdit.this, android.R.layout.simple_dropdown_item_1line, articulos);
                    articulo.setAdapter(arrayAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}