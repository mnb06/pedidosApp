package com.example.pedidosapp.tabs;

import static android.content.ContentValues.TAG;

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
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pedidosapp.R;
import com.example.pedidosapp.clientsLogic.Client;
import com.example.pedidosapp.pedidosLogic.Pedido;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;


public class Resumen extends Fragment {

    private CalendarView calendario;
    private TextView fecha;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private ListView listView;
    public static ArrayList<String> list;


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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, list);

        // Método que setea la fecha de consulta
        calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                month ++;
                String fechaSeleccionada = day + "-" + month + "-" + year;
                fecha.setText(fechaSeleccionada);
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> pedidos = dataSnapshot.getChildren();
                        list.clear();
                        for (DataSnapshot ds : pedidos) {
                            Pedido pedido = ds.getValue(Pedido.class);

                            if ((pedido.getFecha()).equals(fechaSeleccionada)) {
                                //list.clear();
                                list.add("Pedido de " + pedido.getCliente());
                            }
                        }
                        if(list.isEmpty()){
                            list.add("No hay pedidos para la fecha");
                        }
                        listView.setAdapter(adapter);
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });
            }
        });
        // Read from the database
        return view;
    }

}