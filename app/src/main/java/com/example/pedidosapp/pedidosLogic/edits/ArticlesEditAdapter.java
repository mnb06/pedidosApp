package com.example.pedidosapp.pedidosLogic.edits;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pedidosapp.R;
import com.example.pedidosapp.articleLogic.Articulo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ArticlesEditAdapter extends RecyclerView.Adapter<ArticlesEditAdapter.MyViewHolder> {

    Context context;
    ArrayList<Articulo> list;
    ArrayList<Articulo> cargados;

    // path del pedido correspondiente
    String path;


    // Constructor
    public ArticlesEditAdapter(Context context, ArrayList<Articulo> list, String path, ArrayList<Articulo> cargados) {
        this.context = context;
        this.list = list;
        this.path = path;
        this.cargados = cargados;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_edit_article, parent, false);
        return new MyViewHolder(v);



    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        // Traer el articulo
        Articulo articulo = list.get(position);
        holder.nombre.setText(articulo.getNombre());
        String oldCant = articulo.getCantidad();
        holder.stock.setText(articulo.getCantidad());
        holder.setIsRecyclable(false);


        // Agrega el articulo al pedido
        holder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Articulo elegido = new Articulo(articulo.getNombre(),holder.stock.getText().toString());
                uploadArticles(elegido, path);
                changeReserved(cargados, elegido, oldCant);

            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference ref = db.getReference(path);
                ref.child(articulo.getNombre()).setValue(null);
                Toast.makeText(context.getApplicationContext(), "Eliminado satisfactoriamente.",
                        Toast.LENGTH_LONG).show();
                list.clear();
            }
        });
    }


    private void uploadArticles(Articulo articulo, String id) {
        // Hash donde se almacenan los datos a subir
        Map<String, Object> articulosSeleccionados = new HashMap<>();

        // Insercion de los datos en el hash
        articulosSeleccionados .put("nombre", articulo.getNombre());
        articulosSeleccionados.put("cantidad", articulo.getStock());


        // Se crea un hijo (similar a una tabla) y se ingresan los valores
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference(path);
        ref.child(articulo.getNombre()).setValue(articulosSeleccionados);
        Toast.makeText(context, "Articulo Modificado exitosamente",
                Toast.LENGTH_LONG).show();
        list.clear();
    }

    private void changeReserved(ArrayList<Articulo> cargados, Articulo elegido, String oldCant) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("Articulos");
        Log.e("AAAAAAAAAAAAAAAA", Integer.toString(cargados.size()));
        for (Articulo cargado : cargados) {
            if (elegido.getNombre().equals(cargado.getNombre())) {
                int a = Integer.parseInt(elegido.getStock());
                int b = Integer.parseInt(cargado.getStockReservado());
                int c = Integer.parseInt(oldCant);
                int d = (b - c) + a;
                database.child(elegido.getNombre()).child("stockReservado").setValue(Integer.toString(d));
            }
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nombre;
        EditText stock;
        Button delete, save;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nombre = itemView.findViewById(R.id.articleEditName);
            stock = itemView.findViewById(R.id.articleEditStock);
            delete = itemView.findViewById(R.id.articleEditDelete);
            save = itemView.findViewById(R.id.articleEditSave);

        }
    }
}


