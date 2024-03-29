package com.example.pedidosapp.pedidosLogic;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pedidosapp.R;
import com.example.pedidosapp.articleLogic.Articulo;
import com.example.pedidosapp.articleLogic.ArticuloEdit;
import com.example.pedidosapp.tabs.Pedidos;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


public class AdapterPedido extends RecyclerView.Adapter<AdapterPedido.MyViewHolder> {

    Context context;

    ArrayList<Pedido> list;
    ArrayList<Articulo> articulos;

    ArrayList<Articulo> cargados;

    // Constructor
    public AdapterPedido(Context context, ArrayList<Pedido> list, ArrayList<Articulo> articulos) {
        this.context = context;
        this.list = list;
        this.articulos = articulos;
        this.cargados = new ArrayList<>();
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
            String path = "Pedidos/" + pedido.getCliente() + "_" + pedido.getFecha() + "/Articulos";

            holder.cliente.setText(pedido.getCliente());
            holder.fecha.setText(pedido.getFecha());
            //holder.stockMin.setText(pe.getListArticulos());
            holder.setIsRecyclable(false);


            holder.view.setOnClickListener(view -> {
                Intent intent = new Intent(context, PedidoDetail.class);
                intent.putExtra("cliente", pedido.getCliente());
                intent.putExtra("fecha", pedido.getFecha());
                context.startActivity(intent);
            });

            holder.edit.setOnClickListener(view -> {
                Intent intent = new Intent(context, PedidoEdit.class);
                intent.putExtra("cliente", pedido.getCliente());
                intent.putExtra("fecha", pedido.getFecha());
                context.startActivity(intent);
             });

            holder.delete.setOnClickListener(view -> {
                deleteOrder(pedido, path, articulos, cargados);

            });

            holder.complete.setOnClickListener(view -> {
                Intent intent = new Intent(context, Completar.class);
                intent.putExtra("cliente", pedido.getCliente());
                intent.putExtra("fecha", pedido.getFecha());
                intent.putExtra("articulos", path);
                context.startActivity(intent);
            });
        }


    private void deleteOrder(Pedido pedido, String path, ArrayList<Articulo> articulos, ArrayList<Articulo> cargados) {

        androidx.appcompat.app.AlertDialog.Builder alerta = new androidx.appcompat.app.AlertDialog.Builder(context);

        alerta.setMessage("Esta seguro que desea eliminar el pedido?")
                .setCancelable(false)
                .setPositiveButton("Sí", (DialogInterface.OnClickListener) (dialogInterface, i) -> {
                    undoStockReservado(path, articulos, cargados);
                    deleteAll(pedido);
                    list.clear();
                    notifyDataSetChanged();
                })
                .setNegativeButton("No", (DialogInterface.OnClickListener) (dialogInterface, i) -> dialogInterface.cancel());
        AlertDialog completar = alerta.create();
        completar.setTitle("Eliminar Pedido");
        completar.show();
    }

    private void undoStockReservado(String pedidosArtPath, ArrayList<Articulo> articulos, ArrayList<Articulo> cargados){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        // Carga de los articulos del pedido
        ref.child(pedidosArtPath).addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // Bucle que carga todos los articulos en la lista
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Articulo articulo = dataSnapshot.getValue(Articulo.class);
                    cargados.add(articulo);
                };
                compare(cargados, articulos);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context.getApplicationContext(), "Hubo un error intentando. Volver a probar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void compare(ArrayList<Articulo> cargados, ArrayList<Articulo> articulos){
        for (Articulo cargado: cargados) {
            for (Articulo articulo : articulos) {
                // chequeo si el nombre coincide
                if (cargado.getNombre().equals(articulo.getNombre())){
                    int a = Integer.parseInt(articulo.getStockReservado());
                    int b = Integer.parseInt(cargado.getCantidad());
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Articulos");
                    ref.child(articulo.getNombre()).child("stockReservado").setValue(Integer.toString(a-b));
                }
            }
        }
    }

    private void deleteAll(Pedido pedido) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("Pedidos");
        String id = pedido.getCliente() + "_" + pedido.getFecha();
        ref.child(id).setValue(null);
        Toast.makeText(context.getApplicationContext(), "Eliminado satisfactoriamente.",
                Toast.LENGTH_LONG).show();
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


