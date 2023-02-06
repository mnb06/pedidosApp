package com.example.pedidosapp.clientsLogic;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.pedidosapp.R;

public class ClientDelete extends AppCompatActivity {

    private Button delete;
    private Button noDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_delete);
        delete = findViewById(R.id.deleteYes);
        noDelete = findViewById(R.id.deleteNo);

        delete.setOnClickListener(new View.OnClickListener() {

            // Llamada a la actividad para eliminar clientes
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Se ha registrado el pulso del boton.",
                        Toast.LENGTH_LONG).show();
            }
        });
    }



}