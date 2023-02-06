package com.example.pedidosapp;

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

import com.example.pedidosapp.articulos.Articulo;
import com.example.pedidosapp.articulos.articulosCreacion;
import com.example.pedidosapp.articulos.AdapterArt;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Articulos extends Fragment {

    private FloatingActionButton create;
    private FloatingActionButton delete;
    RecyclerView recyclerView;
    DatabaseReference database;
    AdapterArt adapter;
    ArrayList<Articulo> list;
    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_articulos, container, false);

        // Conexion con UI
        create = view.findViewById(R.id.articuloCreate);
        delete = view.findViewById(R.id.articuloDelete);

        // Lista dinamica
        recyclerView = view.findViewById(R.id.listViewArticulos);

        // Database instance
        database = FirebaseDatabase.getInstance().getReference("Articulos");

        // Configuracion de la lista
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        list = new ArrayList<>();
        adapter = new AdapterArt(getContext(), list);
        recyclerView.setAdapter(adapter);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Articulo articulo = dataSnapshot.getValue(Articulo.class);
                    list.add(articulo);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Asignaciones a los botones
        create.setOnClickListener(new View.OnClickListener() {

            // Llamada a la actividad para crear articulos
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), articulosCreacion.class);
                getActivity().startActivity(intent);
            }
        });

        return view;
    }

}