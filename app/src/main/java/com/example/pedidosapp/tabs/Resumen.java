package com.example.pedidosapp.tabs;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.pedidosapp.R;
import com.example.pedidosapp.articleLogic.Articulo;
import com.example.pedidosapp.pedidosLogic.Pedido;
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


public class Resumen extends Fragment {

    private CalendarView calendario;
    private TextView fecha;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private ListView listView;
    public static ArrayList<String> list;
    public static ArrayList<Pedido> listPedido;

    //Gestion de permisos para el PDF
    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), aceptado -> {
                if (aceptado) Toast.makeText(this.getContext(), "Permisos concedidos", Toast.LENGTH_SHORT).show();
                else Toast.makeText(this.getContext(), "Permisos denegados", Toast.LENGTH_SHORT).show();
            }
    );
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
        listPedido = new ArrayList<>();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, list);

        // Método que setea la fecha de consulta
        calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                month++;
                String fechaSeleccionada = day + "-" + month + "-" + year;
                fecha.setText(fechaSeleccionada);
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> pedidos = dataSnapshot.getChildren();
                        list.clear();
                        listPedido.clear();
                        for (DataSnapshot ds : pedidos) {
                            Pedido pedido = ds.getValue(Pedido.class);
                            if ((pedido.getFecha()).equals(fechaSeleccionada)) {
                                list.add("Pedido de " + pedido.getCliente());
                                listPedido.add(pedido);
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        ArrayList<Articulo> encargue = new ArrayList<>();
                                        if (!listPedido.isEmpty()) {
                                            Pedido ped = listPedido.get(i);
                                            Iterable<DataSnapshot> articulos = dataSnapshot.child(ped.getCliente()+"_"+ped.getFecha()).child("Articulos").getChildren();
                                            for (DataSnapshot ds : articulos) {
                                                Articulo a = ds.getValue(Articulo.class);
                                                Articulo art = new Articulo();
                                                art.setCantidad(a.getCantidad());
                                                art.setNombre(a.getNombre());
                                                encargue.add(a);
                                            }
                                            AlertDialog.Builder alerta = new AlertDialog.Builder(getContext());
                                            alerta.setMessage("Pedido de " + listPedido.get(i).getCliente() + " para la fecha " + listPedido.get(i).getFecha() + ": \n"
                                                            + mostrarArticulos(encargue))
                                                    .setCancelable(false)
                                                    .setPositiveButton("Descargar PDF", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            verificarPermisos(view, ped, encargue);
                                                            //crearPDF(ped, encargue);
                                                            dialogInterface.cancel();
                                                        }
                                                    })
                                                    .setNegativeButton("Volver", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            dialogInterface.cancel();
                                                        }
                                                    });
                                            AlertDialog verResumen = alerta.create();
                                            verResumen.setTitle("Información del Pedido");
                                            verResumen.show();
                                        }
                                    }
                                });
                            }
                        }
                        if (list.isEmpty()) {
                            list.add("No hay pedidos para la fecha");
                        }
                        listView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Error al cargar los pedidos.", error.toException());
                    }
                });
            }
        });


        return view;
    }

    //Crea la lista de articulos del pedido seleccionado
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
    private void verificarPermisos(View v, Pedido p, ArrayList<Articulo> art){
        if(ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            crearPDF(p, art);
        }else if(ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            Snackbar.make(v, "Permiso necesario para crear el archivo", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
            });
        }else{
            requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    //Creacion del PDF
    private void crearPDF(Pedido p, ArrayList<Articulo> art){
        try{
            String carpeta = "/Pedidos";
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + carpeta;
            //String path = "/sdcard/download" + carpeta;

            //Si no existe carpeta la crea
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
                Toast.makeText(this.getContext(), "CARPETA CREADA", Toast.LENGTH_SHORT).show();
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
                    "Articulos: " +  "\n\n\n",
                    FontFactory.getFont("Calibri", 20, Font.BOLD, BaseColor.BLACK)
            );
            doc.add(info);

            PdfPTable articulos = new PdfPTable(2);
            articulos.addCell("Nombre");
            articulos.addCell("Cantidad");

            for(int i = 0; i < art.size(); i++){
                articulos.addCell(art.get(i).getNombre());
                articulos.addCell(art.get(i).getCantidad());
            }
            doc.add(articulos);

            doc.close();
            Toast.makeText(this.getContext(), "Pedido descargado", Toast.LENGTH_LONG).show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this.getContext(), "Error en la descarga. Intente nuevamente", Toast.LENGTH_LONG).show();
        } catch (DocumentException e) {
            e.printStackTrace();
            Toast.makeText(this.getContext(), "Error en la descarga. Intente nuevamente", Toast.LENGTH_LONG).show();
        }
    }
}



