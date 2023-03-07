package com.example.pedidosapp.clientsLogic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pedidosapp.R;

import java.util.ArrayList;

import com.example.pedidosapp.pedidosLogic.Pedido;
import com.example.pedidosapp.tabs.Clientes;
import com.example.pedidosapp.tabs.Pedidos;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class AdapterClient extends RecyclerView.Adapter<AdapterClient.MyViewHolder> {

    Context context;
    DatabaseReference databaseReference;

    ArrayList<Client> list;
    ArrayList<Pedido> pedidos;

    // Constructor
    public AdapterClient(Context context, ArrayList<Client> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_client, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Client client = list.get(position);

        holder.nombre.setText(client.getNombre());
        holder.encargado.setText(client.getEncargado());
        holder.direccion.setText(client.getDireccion());
        holder.setIsRecyclable(false);

        holder.delete.setOnClickListener(view -> {

            pedidos = Pedidos.list;

            if (check(client.getNombre())) {
                Toast.makeText(context.getApplicationContext(), "El cliente tiene pedidos activos, no se pudo eliminar",
                        Toast.LENGTH_LONG).show();
            } else {
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference ref = db.getReference("Clientes");

                ref.child(client.getNombre()).setValue(null);
                Toast.makeText(context.getApplicationContext(), "Eliminado satisfactoriamente.",
                        Toast.LENGTH_LONG).show();
                list.clear();


            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ClientEdit.class);
                intent.putExtra("name", client.getNombre());
                intent.putExtra("encargado", client.getEncargado());
                intent.putExtra("direccion", client.getDireccion());
                context.startActivity(intent);
            }
        });
    }



    private boolean check(String name){
        boolean isTrue = false;
        for (Pedido pedido : pedidos) {
            if (name.equals(pedido.getCliente())) {
                 isTrue = true;
                 break;
            }
        }
        return isTrue;
    }


    @Override
    public int getItemCount() {
        return list.size();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nombre, encargado, direccion;
        Button delete, edit;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nombre = itemView.findViewById(R.id.clientName);
            encargado = itemView.findViewById(R.id.clientEncargado);
            direccion = itemView.findViewById(R.id.clientDireccion);
            delete = itemView.findViewById(R.id.clientDelete);
            edit = itemView.findViewById(R.id.clientEdit);


        }
    }
}
