package com.example.pedidosapp.articleLogic;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pedidosapp.R;
import com.example.pedidosapp.tabs.Articulos;
import com.example.pedidosapp.tabs.Clientes;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ArticuloCreation extends AppCompatActivity {

    private EditText nombre, stock, stockMin;

    static DatabaseReference mRootReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articulo_creation);

        mRootReference = FirebaseDatabase.getInstance().getReference();

        //Enlace de UI
        nombre = findViewById(R.id.artName);
        stock = findViewById(R.id.artStock);
        stockMin = findViewById(R.id.artStockMin);
        Button upload = findViewById(R.id.artUpload);
        Button cancel = findViewById(R.id.artCancel);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadClient();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


    public void uploadClient(){
        String name = nombre.getText().toString();
        String s = stock.getText().toString();
        String sM = stockMin.getText().toString();

        if (name.isEmpty() || s.isEmpty() || sM.isEmpty()){
            Toast.makeText(getApplicationContext(), "Ingrese todos los campos.",
                    Toast.LENGTH_LONG).show();
        }else {

            DatabaseReference validation = FirebaseDatabase.getInstance().getReference().child("Articulos").child(name);
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Toast.makeText(getApplicationContext(), "Ya existe un articulo con el mismo nombre",
                                Toast.LENGTH_LONG).show();
                    } else {
                        // Llamada al metodo que sube los datos a la db
                        uploadData(name, s, sM);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            };
            validation.addListenerForSingleValueEvent(valueEventListener);

        }
    }

    private void uploadData(String name, String s, String sM) {
        // Hash donde se almacenan los datos a subir
        Map<String, Object> datosArticulo = new HashMap<>();

        // Insercion de los datos en el hash
        datosArticulo .put("nombre", name);
        datosArticulo.put("stock", s);
        datosArticulo.put("stockMin", sM);
        datosArticulo.put("stockReservado", "0");

        // Se crea un hijo (similar a una tabla) y se ingresan los valores
        mRootReference.child("Articulos").child(name).setValue(datosArticulo);

        // Notificacion Toast para mostrar si el cliente fue cargado
        Toast.makeText(getApplicationContext(), "Articulo cargado exitosamente.",
                Toast.LENGTH_LONG).show();
        Articulos.list.clear();
        finish();
    }
}