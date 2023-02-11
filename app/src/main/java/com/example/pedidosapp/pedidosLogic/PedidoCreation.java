package com.example.pedidosapp.pedidosLogic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.pedidosapp.articleLogic.ArticuloEdit;
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

public class PedidoCreation extends AppCompatActivity {

    Spinner cliente;
    Button fecha, upload, cancel, add;
    TextView fechaElegida;
    Calendar calendar;

    DatePickerDialog dpd;
    DatabaseReference ref;

    RecyclerView listView;

    ArrayList<Articulo> articles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos_creation);

        // Conexion UI
        cliente = findViewById(R.id.pedidoCliente);
        fecha = findViewById(R.id.pedidoFecha);
        listView = findViewById(R.id.listViewArticulosSeleccionados);
        fechaElegida = findViewById(R.id.fechaElegida);
        add = findViewById(R.id.addArticulo);
        upload = (Button) findViewById(R.id.pedidoUpload);
        cancel = (Button) findViewById(R.id.pedidoCancel);
        //add = (Button) findViewById(R.id.addArticulo);

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
                    @Override
                    public void onDateSet(DatePicker datePicker, int d, int m, int y) {
                        m++;
                        fechaElegida.setText(y + "-" + m + "-" + d);
                    }
                } ,year,month,day);
                dpd.show();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddArticle.class);
                startActivity(intent);
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

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /* llamada al metodo para agregar articulos
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        */

    }


    private void uploadData(String client, String fecha) {
        // Hash donde se almacenan los datos a subir
        Map<String, Object> datosPedido = new HashMap<>();

        // Insercion de los datos en el hash
        datosPedido .put("cliente", client);
        datosPedido.put("fecha", fecha);
        //datosPedido.put("Articulos", direction);

        // Se crea un hijo (similar a una tabla) y se ingresan los valores
        String id = client + fecha;
        ref.child("Pedidos").child(id).setValue(datosPedido);

        // Notificacion Toast para mostrar si el pedido fue cargado
        Toast.makeText(getApplicationContext(), "Pedido cargado exitosamente.",
                Toast.LENGTH_LONG).show();
        Pedidos.list.clear();
        finish();
    }

    // Carga los clientes en el spinner de la creacion de pedidos
    public void loadSpinnerClientes(){
        List<String> clientes = new ArrayList<>();
        ref.child("Clientes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot ds : snapshot.getChildren()){
                        String nombre = (String) ds.child("nombre").getValue();
                        clientes.add(nombre);
                    }

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(PedidoCreation.this, android.R.layout.simple_dropdown_item_1line, clientes);
                    cliente.setAdapter(arrayAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}

