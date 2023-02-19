package com.example.pedidosapp.tabs;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pedidosapp.R;
import com.example.pedidosapp.articleLogic.Articulo;
import com.example.pedidosapp.clientsLogic.Client;
import com.example.pedidosapp.pedidosLogic.Pedido;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Context;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class Resumen extends Fragment {

    private CalendarView calendario;
    private TextView fecha;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private ListView listView;
    public static ArrayList<String> list;
    public static ArrayList<Pedido> listPedido;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_resumen, container, false);

        //Enlace de UI

        calendario = view.findViewById(R.id.calendarView);
        fecha = view.findViewById(R.id.textFecha);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Pedidos");

        listView = view.findViewById(R.id.listaPedidos);
        list = new ArrayList<>();
        listPedido = new ArrayList<>();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, list);

        // Método que setea la fecha de consulta
        calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                month++;
                String fechaSeleccionada = day + "-" + month + "-" + year;
                fecha.setText(fechaSeleccionada);
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> pedidos = dataSnapshot.getChildren();
                        list.clear();
                        listPedido.clear();
                        for (DataSnapshot ds : pedidos) {
                            Pedido pedido = ds.getValue(Pedido.class);
                            if ((pedido.getFecha()).equals(fechaSeleccionada)) {
                                list.add("Pedido de " + pedido.getCliente());
                                listPedido.add(pedido);
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        ArrayList<Articulo> encargue;
                                        if(!listPedido.isEmpty()){
                                            AlertDialog.Builder alerta = new AlertDialog.Builder(getContext());
                                            encargue = listPedido.get(i).getListArticulos();
                                            alerta.setMessage("Pedido de " + listPedido.get(i).getCliente() + " para la fecha " + listPedido.get(i).getFecha() + ": \n")
                                                    //+ mostrarArticulos(encargue))
                                                    .setCancelable(false)
                                                    .setPositiveButton("Descargar PDF", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            dialogInterface.cancel();
                                                        }
                                                    })
                                                    .setNegativeButton("Volver", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            dialogInterface.cancel();
                                                        }
                                                    });
                                            AlertDialog verResumen = alerta.create();
                                            verResumen.setTitle("Información del Pedido");
                                            verResumen.show();
                                        }
                                    }
                                });
                            }
                        }
                        if (list.isEmpty()) {
                            list.add("No hay pedidos para la fecha");
                        }
                        listView.setAdapter(adapter);
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Error al cargar los pedidos.", error.toException());
                    }
                });
            }
        });


        return view;
        }

    private static String mostrarArticulos(@NonNull ArrayList<Articulo> lista){
        String nombre;
        String cantidad;
        String linea = "";
        for (Articulo art: lista) {
            nombre = art.getNombre();
            cantidad = art.getCantidad();
            linea = linea + "/n" + nombre + " " + cantidad;
        }
        return linea;
  }
}



