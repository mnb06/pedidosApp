package com.example.pedidosapp.tabs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.pedidosapp.R;
import com.example.pedidosapp.clientsLogic.AdapterClient;
import com.example.pedidosapp.clientsLogic.Client;
import com.example.pedidosapp.clientsLogic.ClientCreation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


public class Clientes extends Fragment {


    private FloatingActionButton create;
    private Button edit,delete, upload;

    private TextView options;

    SwipeRefreshLayout swipeRefreshLayout;
    Dialog createDialog;
    RecyclerView recyclerView;
    DatabaseReference database;
    AdapterClient adapterClient;
    public static ArrayList<Client> list;


    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_clientes, container, false);

        // Conexion con UI
        create = view.findViewById(R.id.clientCreate);



        // Refresh Swipe
        swipeRefreshLayout = view.findViewById(R.id.clientRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapterClient.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        // Lista dinamica
        recyclerView = view.findViewById(R.id.listViewClient);

        // Database instance
        database = FirebaseDatabase.getInstance().getReference("Clientes");

        // Configuracion de la lista
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        list = new ArrayList<>();
        adapterClient = new AdapterClient(getContext(), list);
        recyclerView.setAdapter(adapterClient);


        createDialog = new Dialog(getContext());



        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Client client = dataSnapshot.getValue(Client.class);
                    list.add(client);
                }
                adapterClient.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });


        // Asignaciones a los botones
        create.setOnClickListener(new View.OnClickListener() {

            // Llamada a la actividad para crear clientes
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), ClientCreation.class);
                getActivity().startActivity(intent);
            }
        });

        return view;
    }

    }