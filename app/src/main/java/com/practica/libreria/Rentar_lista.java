package com.practica.libreria;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

public class Rentar_lista extends AppCompatActivity {

    // INSTANCIAR IDs

    ImageButton volver;
    ListView lista_rentas;

    ArrayList<String> listado;

    // INSTANCIAR LA BASE DE DATOS

    DbLibreria Libro = new DbLibreria(this,"BbLibreria2",null,1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rentar_lista);

        // INSTANCIAR IDs

        volver = findViewById(R.id.ibback);
        lista_rentas = findViewById(R.id.lvrentlist);

        // -------------- //

        // EJECUTAR FUNCION

        CargarListado();


        // EVENTO VOLVER A RENTAR

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent volver = new Intent(getApplicationContext(), Rentar.class);

                startActivity(volver);
            }
        });

    }

    private void CargarListado(){

        listado = ListaUsuarios();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, listado);

        lista_rentas.setAdapter(adapter);

    }

    private ArrayList<String> ListaUsuarios() {

        ArrayList<String> datos = new ArrayList<String>();

        SQLiteDatabase DbLibreriaRead = Libro.getReadableDatabase();

        String query = "SELECT id_renta, id_usuario, id_libro, fecha_renta FROM Rentar";

        Cursor cRenta = DbLibreriaRead.rawQuery(query, null);

        if (cRenta.moveToFirst()) {

            do {

                String recUsuario = " |   " + cRenta.getString(0) + "   |   " + cRenta.getString(1) + "   |   " + cRenta.getString(2) + "   |   " + cRenta.getString(3)  + "   | ";

                datos.add(recUsuario);

            } while (cRenta.moveToNext());
        }

        DbLibreriaRead.close();
        return datos;
    }
}