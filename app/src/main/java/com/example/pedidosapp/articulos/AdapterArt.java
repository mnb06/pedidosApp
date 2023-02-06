package com.example.pedidosapp.articulos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pedidosapp.R;
import java.util.ArrayList;

public class AdapterArt extends RecyclerView.Adapter<AdapterArt.MyViewHolder> {
    Context context;
    ArrayList<Articulo> list;

        // Constructor
     public AdapterArt(Context context, ArrayList<Articulo> list) {
          this.context = context;
          this.list = list;
        }

     @NonNull
     @Override
     public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
          View v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
          return new MyViewHolder(v);
        }

     @Override
     public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

         Articulo articulo = list.get(position);

         //Aca tira error, como el articulo todavia no se cargo lo toma como puntero nulo. Temporariamente puse un toast
         try{
             holder.nombre.setText(articulo.getNombre());
         }catch(Exception e){
             Toast.makeText(context.getApplicationContext(), "No hay artículos cargados.",
                     Toast.LENGTH_SHORT).show();
         }
        }

     @Override
     public int getItemCount() {
            return list.size();
        }

     public static class MyViewHolder extends RecyclerView.ViewHolder{
         TextView nombre;
         public MyViewHolder(@NonNull View itemView) {
             super(itemView);

             nombre = itemView.findViewById(R.id.nombreArticulo);
            }
        }
   }

