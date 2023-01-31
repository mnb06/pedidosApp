package com.example.pedidosapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class Clientes extends Fragment {

    private Button create;
    private Button modify;
    private Button delete;
    private Button list;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clientes, container, false);

        // Conexion con UI
        create = view.findViewById(R.id.clientCreate);

        // Asignacion a la funcion del boton
        create.setOnClickListener(new View.OnClickListener() {

            // Metodo para crear clientes
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), ClientCreation.class);
                getActivity().startActivity(intent);
            }
        });

        return view;
    }




//    public void cliModify(View view){
//        Intent intent = new Intent();
//        intent.setClass(getActivity(), Principal.class);
//        getActivity().startActivity(intent);
//    }
//
//    public void cliDelete(View view){
//        Intent intent = new Intent();
//        intent.setClass(getActivity(), Principal.class);
//        getActivity().startActivity(intent);
//    }
//
//    public void cliView(View view){
//        Intent intent = new Intent();
//        intent.setClass(getActivity(), Principal.class);
//        getActivity().startActivity(intent);
//    }


}