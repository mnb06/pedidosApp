package com.example.pedidosapp.clientsLogic;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pedidosapp.R;
import com.example.pedidosapp.tabs.Clientes;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ClientEdit extends AppCompatActivity {

    private Button upload, cancel;
    private EditText encargado, direccion;
    private TextView nombre;

    static DatabaseReference mRootReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_edit);

        mRootReference = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        String id = intent.getStringExtra("name");

        //Enlace de UI
        nombre = findViewById(R.id.clientName);
        nombre.setText(id);

        encargado = findViewById(R.id.clientEncargado);
        encargado.setText(intent.getStringExtra("encargado"));

        direccion = findViewById(R.id.clientDireccion);
        direccion.setText(intent.getStringExtra("direccion"));

        upload = findViewById(R.id.clientUpload);
        cancel = findViewById(R.id.clientCancel);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uploadClient(id);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


    public void uploadClient(String nombre){

        String employee = encargado.getText().toString();
        String direction = direccion.getText().toString();

        if (employee.isEmpty() || direction.isEmpty()){
            Toast.makeText(getApplicationContext(), "Ingrese todos los campos.",
                    Toast.LENGTH_LONG).show();
        }else {
            // Llamada al metodo que sube los datos a la db
            uploadData(nombre,employee, direction);
        }
    }

    private void uploadData(String nombre, String employee, String direction) {
        // Hash donde se almacenan los datos a subir
        Map<String, Object> datosClientes = new HashMap<>();

        // Insercion de los datos en el hash
        datosClientes.put("nombre", nombre);
        datosClientes.put("encargado", employee);
        datosClientes.put("direccion", direction);

        // Se crea un hijo (similar a una tabla) y se ingresan los valores
        mRootReference.child("Clientes").child(nombre).setValue(datosClientes);

        // Notificacion Toast para mostrar si el cliente fue cargado
        Toast.makeText(getApplicationContext(), "Cliente editado exitosamente.",
                Toast.LENGTH_LONG).show();
        Clientes.list.clear();
        finish();
    }
}