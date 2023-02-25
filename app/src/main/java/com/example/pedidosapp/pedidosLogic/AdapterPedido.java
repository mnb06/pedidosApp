package com.example.pedidosapp.pedidosLogic;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pedidosapp.R;
import com.example.pedidosapp.articleLogic.ArticuloEdit;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class AdapterPedido extends RecyclerView.Adapter<AdapterPedido.MyViewHolder> {

    Context context;

    ArrayList<Pedido> list;

    // Constructor
    public AdapterPedido(Context context, ArrayList<Pedido> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

            Pedido pedido = list.get(position);
            holder.cliente.setText(pedido.getCliente());
            holder.fecha.setText(pedido.getFecha());
            //holder.stockMin.setText(pe.getListArticulos());
            holder.setIsRecyclable(false);


            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, PedidoDetail.class);
                    intent.putExtra("cliente", pedido.getCliente());
                    intent.putExtra("fecha", pedido.getFecha());
                    context.startActivity(intent);
                }
            });

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference ref = db.getReference("Pedidos");
                    String id = pedido.getCliente() + "_" + pedido.getFecha();
                    ref.child(id).setValue(null);
                    Toast.makeText(context.getApplicationContext(), "Eliminado satisfactoriamente.",
                            Toast.LENGTH_LONG).show();
                    list.clear();
                }
            });

            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, PedidoEdit.class);
                    intent.putExtra("cliente", pedido.getCliente());
                    intent.putExtra("fecha", pedido.getFecha());
                    context.startActivity(intent);
                }
            });

            holder.complete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String path = "Pedidos/" + pedido.getCliente() + "_" + pedido.getFecha() + "/Articulos";
                    Intent intent = new Intent(context, Completar.class);
                    intent.putExtra("cliente", pedido.getCliente());
                    intent.putExtra("fecha", pedido.getFecha());
                    intent.putExtra("articulos", path);
                    context.startActivity(intent);
                }
            });
        }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView cliente, fecha;
        Button view, complete, delete, edit;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            cliente = itemView.findViewById(R.id.orderClient);
            fecha = itemView.findViewById(R.id.orderDate);
            view = itemView.findViewById(R.id.viewArticles);
            complete = itemView.findViewById(R.id.orderComplete);
            delete = itemView.findViewById(R.id.orderDelete);
            edit = itemView.findViewById(R.id.orderEdit);
        }
    }


}


