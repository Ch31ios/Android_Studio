package com.practica.libreria;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

public class Libros_lista extends AppCompatActivity {

    // INSTANCIAR IDs

    ImageButton volver;
    ListView lista_libros;

    ArrayList<String> listado;

    // INSTANCIAR LA BASE DE DATOS

    DbLibreria Libro = new DbLibreria(this,"BbLibreria2",null,1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_libros_lista);

        // INSTANCIAR LOS DATOS

        volver = findViewById(R.id.ibback);
        lista_libros = findViewById(R.id.lvbookslist);

        // -------------- //

        // EJECUTAR FUNCION

        CargarListado();


        // EVENTO VOLVER A LIBROS

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent volver = new Intent(getApplicationContext(), Libros.class);

                startActivity(volver);
            }
        });

    }

    private void CargarListado(){

        listado = ListaUsuarios();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, listado);

        lista_libros.setAdapter(adapter);

    }

    private ArrayList<String> ListaUsuarios() {

        ArrayList<String> datos = new ArrayList<String>();

        SQLiteDatabase DbLibreriaRead = Libro.getReadableDatabase();

        String query = "SELECT id_libro, nombre_libro, coste_libro, estatus_libro FROM Libros";

        Cursor cLibro = DbLibreriaRead.rawQuery(query, null);

        if (cLibro.moveToFirst()) {

            do {

                String Estatus = cLibro.getInt(3) == 0 ? "UNAVALIBLE" : "AVALIBLE";

                String recUsuario =  " |   " + cLibro.getString(0) + "   |   " + cLibro.getString(1) + "   |   " + cLibro.getString(2) + "   |   " + Estatus  + "   | ";

                datos.add(recUsuario);

            } while (cLibro.moveToNext());
        }

        DbLibreriaRead.close();
        return datos;
    }
}