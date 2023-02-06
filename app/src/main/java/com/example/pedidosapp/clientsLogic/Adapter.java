package com.example.pedidosapp.clientsLogic;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pedidosapp.ClientEdit;
import com.example.pedidosapp.Inicio;
import com.example.pedidosapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.pedidosapp.tabs.Clientes;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    Context context;
    DatabaseReference databaseReference;

    ArrayList<Client> list;

    // Constructor
    public Adapter(Context context, ArrayList<Client> list) {
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

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                context.startActivity(intent);
            }
        });
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
