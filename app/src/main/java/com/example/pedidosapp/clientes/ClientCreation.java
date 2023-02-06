package com.example.pedidosapp.clientes;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pedidosapp.Inicio;
import com.example.pedidosapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ClientCreation extends AppCompatActivity {
    private Button upload;
    private EditText nombre, encargado, direccion;

    private FirebaseDatabase database;
    DatabaseReference mRootReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_creation);
        //Enlace de UI
        upload = findViewById(R.id.clientUpload);
        nombre = findViewById(R.id.clientName);
        encargado = findViewById(R.id.clientEncargado);
        direccion = findViewById(R.id.clientDireccion);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadClient();
            }
        });

        mRootReference = FirebaseDatabase.getInstance().getReference();


    }

    public void uploadClient(){
        String name = nombre.getText().toString();
        String employee = encargado.getText().toString();
        String direction = direccion.getText().toString();

        // Llamada al metodo que sube los datos a la db
        uploadData(name, employee, direction);
    }

    public void volverInicio(View view){
        Intent i = new Intent(this, Inicio.class);
        startActivity(i);
    }

    private void uploadData(String name, String employee, String direction) {
        // Hash donde se almacenan los datos a subir
        Map<String, Object> datosClientes = new HashMap<>();

        // Insercion de los datos en el hash
        datosClientes.put("nombre", name);
        datosClientes.put("encargado", employee);
        datosClientes.put("direccion", direction);

        // Se crea un hijo (similar a una tabla) y se ingresan los valores
        mRootReference.child("Clientes").push().setValue(datosClientes);

        // Notificacion Toast para mostrar si el cliente fue cargado
        Toast.makeText(getApplicationContext(), "Cliente cargado exitosamente.",
                Toast.LENGTH_LONG).show();
    }
}