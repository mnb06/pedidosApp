package com.example.pedidosapp.articleLogic;

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
import com.example.pedidosapp.tabs.Articulos;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ArticuloEdit extends AppCompatActivity {

    private Button upload, cancel;
    private EditText stock, stockMin;
    private TextView nombre;

    static DatabaseReference mRootReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articulo_edit);

        mRootReference = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        String id = intent.getStringExtra("name");

        //Enlace de UI
        nombre = findViewById(R.id.artName);
        nombre.setText(id);

        stock = findViewById(R.id.artStock);
        stockMin = findViewById(R.id.artStockMin);
        upload = findViewById(R.id.artUpload);
        cancel = findViewById(R.id.artCancel);

        upload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                uploadArticle(id);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


    public void uploadArticle(String nombre){

        String s = stock.getText().toString();
        String sM = stockMin.getText().toString();

        if (s.isEmpty() || sM.isEmpty()){
            Toast.makeText(getApplicationContext(), "Ingrese todos los campos.",
                    Toast.LENGTH_LONG).show();
        }else {
            // Llamada al metodo que sube los datos a la db
            uploadData(nombre, s, sM);
        }
    }

    private void uploadData(String nombre, String s, String sM) {
        // Hash donde se almacenan los datos a subir
        Map<String, Object> datosArticulo = new HashMap<>();

        // Insercion de los datos en el hash
        datosArticulo.put("nombre", nombre);
        datosArticulo.put("stock", s);
        datosArticulo.put("stockMin", sM);

        // Se crea un hijo (similar a una tabla) y se ingresan los valores
        mRootReference.child("Articulos").child(nombre).setValue(datosArticulo);

        // Notificacion Toast para mostrar si el articulo fue editado
        Toast.makeText(getApplicationContext(), "Articulo editado exitosamente.",
                Toast.LENGTH_LONG).show();
        Articulos.list.clear();
        finish();
    }
}