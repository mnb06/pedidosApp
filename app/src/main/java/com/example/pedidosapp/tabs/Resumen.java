package com.example.pedidosapp.tabs;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.pedidosapp.R;

import java.util.Calendar;


public class Resumen extends Fragment {

    private CalendarView calendario;
    private TextView fecha;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_resumen, container, false);

        //Enlace de UI

        calendario = view.findViewById(R.id.calendarView);
        fecha = view.findViewById(R.id.textFecha);

        // Método que setea la fecha de consulta
        calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                month ++;
                String fechaSeleccionada = day + "-" + month + "-" + year;
                fecha.setText(fechaSeleccionada);
            }
        });

        

        return view;
    }

}