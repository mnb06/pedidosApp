package com.example.pedidosapp.pedidosLogic;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pedidosapp.Inicio;
import com.example.pedidosapp.R;
import com.example.pedidosapp.articleLogic.Articulo;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class Completar extends AppCompatActivity {

    FirebaseDatabase db;
    DatabaseReference ref1, ref2;

    //Gestion de permisos para el PDF
    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), aceptado -> {
                if (aceptado)
                    Toast.makeText(getApplicationContext(), "Permisos concedidos", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "Permisos denegados", Toast.LENGTH_SHORT).show();
            }
    );

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completar);

        Intent intent = getIntent();
        String client = intent.getStringExtra("cliente");
        String date = intent.getStringExtra("fecha");
        String path = intent.getStringExtra("path");

        ref1 = FirebaseDatabase.getInstance().getReference();
        ref2 = FirebaseDatabase.getInstance().getReference();

        //Conexion UI

        Button completar = findViewById(R.id.completar);
        Button descargarPDF = findViewById(R.id.descargarPDF);
        Button volver = findViewById(R.id.volver);
        TextView informacion = findViewById(R.id.informacion);

        Pedido pedido = new Pedido();
        pedido.setCliente(client);
        pedido.setFecha(date);

        //Muestra de información

        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datosPedido) {
                ArrayList<Articulo> encargue = new ArrayList<>();
                Iterable<DataSnapshot> articulos = datosPedido.child("Pedidos").child(pedido.getCliente() + "_" + pedido.getFecha()).child("Articulos").getChildren();
                for (DataSnapshot ds : articulos) {
                    Articulo a = ds.getValue(Articulo.class);
                    Articulo art = new Articulo();
                    art.setCantidad(a.getCantidad());
                    art.setNombre(a.getNombre());
                    encargue.add(a);
                }
                String info = "Pedido de " + client + " para el " + date + ": \n" + mostrarArticulos(encargue);
                informacion.setText(info);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Completar.this, "Hubo un error intentando. Volver a probar", Toast.LENGTH_SHORT).show();
            }
        });

        descargarPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Articulo> encargue = new ArrayList<>();
                ref1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot datosPedido) {

                        Iterable<DataSnapshot> articulos = datosPedido.child("Pedidos").child(pedido.getCliente() + "_" + pedido.getFecha()).child("Articulos").getChildren();
                        for (DataSnapshot ds : articulos) {
                            Articulo a = ds.getValue(Articulo.class);
                            Articulo art = new Articulo();
                            art.setCantidad(a.getCantidad());
                            art.setNombre(a.getNombre());
                            encargue.add(art);
                        }
                        verificarPermisos(view, pedido, encargue);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Completar.this, "Hubo un error intentando. Volver a probar", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        completar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                completarPedido(pedido);
            }
        });

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Completar.this, Inicio.class);
                startActivity(intent);
            }
        });

    }
    @Override
    public void onStop(){
        finish();
        super.onStop();
    }

    @Override
    public void onDestroy(){
        finish();
        super.onDestroy();
    }

    //Metodos de la clase
    private static String mostrarArticulos(@NonNull ArrayList<Articulo> lista) {
        String nombre;
        String cantidad;
        String linea = "";
        for (Articulo art : lista) {
            nombre = art.getNombre();
            cantidad = art.getCantidad();
            linea = linea + "\n" + nombre + " " + cantidad;
        }
        return linea;
    }

    //Metodo validador de permisos
    private void verificarPermisos(View v, Pedido p, ArrayList<Articulo> art) {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            crearPDF(p, art);
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Snackbar.make(v, "Permiso necesario para crear el archivo", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
            });
        } else {
            requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    //Creacion del PDF
    private void crearPDF(Pedido p, ArrayList<Articulo> art) {
        try {
            String carpeta = "/Pedidos";
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + carpeta;

            //Si no existe carpeta la crea
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
                Toast.makeText(getApplicationContext(), "CARPETA CREADA", Toast.LENGTH_SHORT).show();
            }

            String nombrePDF = "Pedido de " + p.getCliente() + " (" + p.getFecha() + ").pdf";
            File archivo = new File(dir, nombrePDF);
            FileOutputStream fos = new FileOutputStream(archivo);

            //Crea archivo
            Document doc = new Document();
            PdfWriter.getInstance(doc, fos);

            //Estructura del documento
            doc.open();

            Paragraph titulo = new Paragraph(
                    "Información del pedido\n\n\n\n",
                    FontFactory.getFont("Calibri", 26, Font.BOLDITALIC, BaseColor.BLUE)
            );
            doc.add(titulo);

            Paragraph info = new Paragraph(
                    "Cliente: " + p.getCliente() + "\n" +
                            "Fecha: " + p.getFecha() + "\n" +
                            "Articulos: " + "\n\n\n",
                    FontFactory.getFont("Calibri", 20, Font.BOLD, BaseColor.BLACK)
            );
            doc.add(info);

            PdfPTable articulos = new PdfPTable(2);
            articulos.addCell("Nombre");
            articulos.addCell("Cantidad");

            for (int i = 0; i < art.size(); i++) {
                articulos.addCell(art.get(i).getNombre());
                articulos.addCell(art.get(i).getCantidad() + " kg.");
            }
            doc.add(articulos);

            doc.close();
            Toast.makeText(getApplicationContext(), "Pedido descargado", Toast.LENGTH_LONG).show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error en la descarga. Intente nuevamente", Toast.LENGTH_LONG).show();
        } catch (DocumentException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error en la descarga. Intente nuevamente", Toast.LENGTH_LONG).show();
        }
    }

    private void completarPedido(Pedido pedido) {
        AlertDialog.Builder alerta = new AlertDialog.Builder(Completar.this);
        alerta.setMessage("¿Esta seguro que desea completar el pedido? Se va a restar el stock y eliminar el pedido de la lista")
                .setCancelable(false)
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       String path = pedido.getCliente() + "_" + pedido.getFecha();
                        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                            //Carga articulos del pedido
                           @Override
                           public void onDataChange(@NonNull DataSnapshot datosDB) {
                               ArrayList<Articulo> encargue = new ArrayList<>();
                               ArrayList<Boolean> existencias = new ArrayList<>();
                               Iterable<DataSnapshot> articulos = datosDB.child("Pedidos").child(path).child("Articulos").getChildren();
                               for (DataSnapshot ds : articulos) {
                                   Articulo a = ds.getValue(Articulo.class);
                                   Articulo art = new Articulo();
                                   art.setCantidad(a.getCantidad());
                                   art.setNombre(a.getNombre());
                                   encargue.add(art);
                               }

                               //Carga articulos de la db
                               ArrayList<Articulo> almacenados = new ArrayList<>();
                               Iterable<DataSnapshot> articulosAlmacenados = datosDB.child("Articulos").getChildren();
                               for (DataSnapshot ds : articulosAlmacenados) {
                                   Articulo a = ds.getValue(Articulo.class);
                                   Articulo art = new Articulo();
                                   art.setStock(a.getStock());
                                   art.setNombre(a.getNombre());
                                   art.setStockReservado(a.getStockReservado());
                                   almacenados.add(art);
                                   }

                               //Comprueba que haya stock de todos los articulos del pedido

                               for (int i = 0; i < almacenados.size(); i++) {
                                   for (int j = 0; j < encargue.size(); j++) {
                                       if (almacenados.get(i).getNombre().equals(encargue.get(j).getNombre())) {
                                           int cantidad = Integer.parseInt(encargue.get(j).getCantidad());
                                           int stock = Integer.parseInt(almacenados.get(i).getStock());
                                           Boolean caso;
                                           if (stock < cantidad) {
                                               caso = false;
                                           } else {
                                               caso = true;
                                           }
                                           existencias.add(caso);
                                       }
                                   }
                               }

                               //Resta stock vinculando lo cargado en el encargue con lo almacenado en la db si hay stock, si no emite mensaje de error
                               if (checkStock(existencias)) {
                                   for (int i = 0; i < almacenados.size(); i++) {
                                       for (int j = 0; j < encargue.size(); j++) {
                                           if (almacenados.get(i).getNombre().equals(encargue.get(j).getNombre())) {
                                               int cantidad = Integer.parseInt(encargue.get(j).getCantidad());
                                               int stock = Integer.parseInt(almacenados.get(i).getStock());
                                               int reservado = Integer.parseInt(almacenados.get(i).getStockReservado());
                                               ref2.child("Articulos").child(almacenados.get(i).getNombre()).child("stock").setValue(String.valueOf(stock - cantidad));
                                               ref2.child("Articulos").child(almacenados.get(i).getNombre()).child("stockReservado").setValue(String.valueOf(reservado - cantidad));
                                               deleteCompletedOrder(path);
                                               finish();
                                               Intent intent = new Intent(Completar.this, Inicio.class);
                                               startActivity(intent);
                                           }
                                       }
                                   }
                               }else{
                                   Toast.makeText(Completar.this, "Falta stock de algunos artículos para completar el pedido. No se puede completar", Toast.LENGTH_SHORT).show();
                                   finish();
                                   Intent intent = new Intent(Completar.this, Inicio.class);
                                   startActivity(intent);
                               }
                           }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(Completar.this, "Hubo un error intentando. Volver a probar", Toast.LENGTH_SHORT).show();
                            }
                        });
                        Intent intent = new Intent(getApplicationContext(), Inicio.class);
                        startActivity(intent);
                        dialogInterface.cancel();
                    }

                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        dialogInterface.cancel();
                    }
                });
        AlertDialog completar = alerta.create();
        completar.setTitle("Completar Pedido");
        completar.show();
    }

    private void deleteCompletedOrder(String path){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("Pedidos").child(path).setValue(null);
        Toast.makeText(Completar.this, "Pedido completo!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private Boolean checkStock(ArrayList<Boolean> existencias){
        Boolean check = true;
        for(int i = 0; i < existencias.size(); i++){
            check = check & existencias.get(i);
        }
        return check;
    }
}