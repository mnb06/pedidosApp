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
import com.example.pedidosapp.articleLogic.Articulo;
import com.example.pedidosapp.pedidosLogic.AdapterPedido;
import com.example.pedidosapp.pedidosLogic.Pedido;
import com.example.pedidosapp.pedidosLogic.PedidoCreation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Pedidos extends Fragment {

    private Button edit,delete, upload;

    private TextView options;

    Dialog createDialog;
    RecyclerView recyclerView;
    DatabaseReference database, articulosRef;
    AdapterPedido adapterPedido;

    public static ArrayList<Pedido> list;
    public static ArrayList<Articulo> articulos;


    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pedidos, container, false);


        // Conexion con UI
        FloatingActionButton create = view.findViewById(R.id.pedidoCreate);


        // Lista dinamica
        recyclerView = view.findViewById(R.id.listViewPedidos);

        // Database instance
        database = FirebaseDatabase.getInstance().getReference("Pedidos");
        articulosRef = FirebaseDatabase.getInstance().getReference("Articulos");
        // Configuracion de la lista
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        list = new ArrayList<>();
        articulos = new ArrayList<>();
        adapterPedido = new AdapterPedido(getContext(), list, articulos);
        recyclerView.setAdapter(adapterPedido);


        createDialog = new Dialog(getContext());


        // Añade a la lista los elementos cargados en la bd
        database.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // Bucle que carga todos los pedidos en la lista
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Pedido pedido = dataSnapshot.getValue(Pedido.class);
                    list.add(pedido);

                }
                adapterPedido.notifyDataSetChanged();

               // return null;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        articulosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Articulo articulo = dataSnapshot.getValue(Articulo.class);
                    articulos.add(articulo);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        // Asignacion al boton create
        create.setOnClickListener(new View.OnClickListener() {

            // Llamada a la actividad para crear articulos
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), PedidoCreation.class);
                startActivity(intent);
            }
        });

        return view;
    }

}