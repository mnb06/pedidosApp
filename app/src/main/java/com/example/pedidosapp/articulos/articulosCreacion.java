package com.example.pedidosapp.articulos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.pedidosapp.Inicio;
import com.example.pedidosapp.R;

public class articulosCreacion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articulos_creacion);
    }

    public void volverInicio(View view){
        Intent i = new Intent(this, Inicio.class);
        startActivity(i);
    }
}