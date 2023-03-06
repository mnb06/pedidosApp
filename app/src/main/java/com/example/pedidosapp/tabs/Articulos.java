package com.example.pedidosapp.tabs;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pedidosapp.R;
import com.example.pedidosapp.articleLogic.*;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Articulos extends Fragment {


    private Button edit,delete, upload;

    private TextView options;

    RecyclerView recyclerView;
    DatabaseReference database;
    AdapterArt adapterArt;
    public static ArrayList<Articulo> list;


    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_articulos, container, false);

        // Conexion con UI
        FloatingActionButton create = view.findViewById(R.id.artCreate);


        // Lista dinamica
        recyclerView = view.findViewById(R.id.listViewArt);

        // Database instance
        database = FirebaseDatabase.getInstance().getReference("Articulos");

        // Configuracion de la lista
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        list = new ArrayList<>();
        adapterArt = new AdapterArt(getContext(), list);
        recyclerView.setAdapter(adapterArt);




        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Articulo articulo = dataSnapshot.getValue(Articulo.class);
                    list.add(articulo);
                }
                adapterArt.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Hubo un error intentando. Volver a probar", Toast.LENGTH_SHORT).show();}
        });


        // Asignaciones a los botones
        create.setOnClickListener(new View.OnClickListener() {

            // Llamada a la actividad para crear articulos
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), ArticuloCreation.class);
                startActivity(intent);
            }
        });

        return view;
    }

}