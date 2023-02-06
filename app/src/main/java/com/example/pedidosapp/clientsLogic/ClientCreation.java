package com.example.pedidosapp.clientsLogic;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pedidosapp.R;
import com.example.pedidosapp.tabs.Clientes;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ClientCreation extends AppCompatActivity {

    private Button upload, cancel;
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
        upload = findViewById(R.id.clientUpload);
        cancel = findViewById(R.id.clientCancel);
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
        String employee = encargado.getText().toString();
        String direction = direccion.getText().toString();

        if (name.isEmpty() || employee.isEmpty() || direction.isEmpty()){
            Toast.makeText(getApplicationContext(), "Ingrese todos los campos.",
                    Toast.LENGTH_LONG).show();
        }else {
            // Llamada al metodo que sube los datos a la db
            uploadData(name, employee, direction);
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
        Clientes.list.clear();
        finish();
    }
}