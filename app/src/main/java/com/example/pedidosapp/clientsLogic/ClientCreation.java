package com.example.pedidosapp.clientsLogic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pedidosapp.R;
import com.example.pedidosapp.tabs.Clientes;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ClientCreation extends AppCompatActivity {

    private EditText nombre, encargado, direccion;

    static DatabaseReference mRootReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_creation);

        mRootReference = FirebaseDatabase.getInstance().getReference();

        //Enlace de UI
        nombre = findViewById(R.id.clientName);
        encargado = findViewById(R.id.clientEncargado);
        direccion = findViewById(R.id.clientDireccion);
        Button upload = findViewById(R.id.clientUpload);
        Button cancel = findViewById(R.id.clientCancel);

        upload.setOnClickListener(view -> uploadClient());

        cancel.setOnClickListener(view -> finish());

    }


    public void uploadClient(){
        String name = nombre.getText().toString();
        String employee = encargado.getText().toString();
        String direction = direccion.getText().toString();

        if (name.isEmpty() || employee.isEmpty() || direction.isEmpty()){
            Toast.makeText(getApplicationContext(), "Ingrese todos los campos",
                    Toast.LENGTH_LONG).show();
        }else {

            DatabaseReference validation = FirebaseDatabase.getInstance().getReference().child("Clientes").child(name);
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Toast.makeText(getApplicationContext(), "Ya existe un cliente con el mismo nombre",
                                Toast.LENGTH_LONG).show();
                    } else {
                        // Llamada al metodo que sube los datos a la db
                        uploadData(name, employee, direction);
                        Clientes.list.clear();
                        finish();
                    }
                }
                @Override
                    public void onCancelled(DatabaseError databaseError) {}
                };
                validation.addListenerForSingleValueEvent(valueEventListener);
        }
    }

    private void uploadData(String name, String employee, String direction) {
        // Hash donde se almacenan los datos a subir
        Map<String, Object> datosClientes = new HashMap<>();

        // Insercion de los datos en el hash
        datosClientes .put("nombre", name);
        datosClientes.put("encargado", employee);
        datosClientes.put("direccion", direction);

        // Se crea un hijo (similar a una tabla) y se ingresan los valores
        mRootReference.child("Clientes").child(name).setValue(datosClientes);

        // Notificacion Toast para mostrar si el cliente fue cargado
        Toast.makeText(getApplicationContext(), "Cliente cargado exitosamente.",
                Toast.LENGTH_LONG).show();
    }
}