package com.example.pedidosapp.articulos;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pedidosapp.Inicio;
import com.example.pedidosapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class articulosCreacion extends AppCompatActivity {
    private Button upload;
    private EditText nombre;

    private FirebaseDatabase database;
    DatabaseReference mRootReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articulos_creacion);
        upload = findViewById(R.id.articuloUpload);
        nombre = findViewById(R.id.nombreArticulo);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadArticulo();
            }
        });

        mRootReference = FirebaseDatabase.getInstance().getReference();
    }

    public void uploadArticulo(){
        String name = nombre.getText().toString();
        // Llamada al metodo que sube los datos a la db
        uploadData(name);
    }

    private void uploadData(String name) {
        // Hash donde se almacenan los datos a subir
        Map<String, Object> datosArticulos = new HashMap<>();

        // Insercion de los datos en el hash
        datosArticulos.put("nombre", name);

        // Se crea un hijo (similar a una tabla) y se ingresan los valores
        mRootReference.child("Articulos").push().setValue(datosArticulos);

        // Notificacion Toast para mostrar si el cliente fue cargado
        Toast.makeText(getApplicationContext(), "Artículo cargado exitosamente.",
                Toast.LENGTH_LONG).show();
    }

    public void volverInicio(View view){
        Intent i = new Intent(this, Inicio.class);
        startActivity(i);
    }
}