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

public class Usuarios_lista extends AppCompatActivity {

    // INSTANCIAR IDs

    ImageButton volver;
    ListView lista_usuarios;

    ArrayList<String> listado;

    // INSTANCIAR LA BASE DE DATOS

    DbLibreria Usuario = new DbLibreria(this,"BbLibreria2",null,1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios_lista);

        // INSTANCIAR LOS DATOS

        volver = findViewById(R.id.ibback);
        lista_usuarios = findViewById(R.id.lvuserslist);

        // -------------- //

        // EJECUTAR FUNCION

        CargarListado();


        // EVENTO VOLVER A USUARIOS

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent volver = new Intent(getApplicationContext(), Usuarios.class);

                startActivity(volver);
            }
        });

    }

    private void CargarListado(){

        listado = ListaUsuarios();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, listado);

        lista_usuarios.setAdapter(adapter);

    }

    private ArrayList<String> ListaUsuarios() {

        ArrayList<String> datos = new ArrayList<String>();

        SQLiteDatabase DbLibreriaRead = Usuario.getReadableDatabase();

        String query = "SELECT id_usuario, nombre_usuario, email_usuario, contraseña_usuario, estatus_usuario FROM Usuarios";

        Cursor cUsuarios = DbLibreriaRead.rawQuery(query, null);

        if (cUsuarios.moveToFirst()) {

            do {

                String Estatus = cUsuarios.getInt(4) == 0 ? "INACTIVE" : "ACTIVE";

                String recUsuario = " |   " + cUsuarios.getString(0) + "   |   " + cUsuarios.getString(1) + "   |   " + cUsuarios.getString(2) + "   |   " + Estatus + "   | ";

                datos.add(recUsuario);

            } while (cUsuarios.moveToNext());
        }

        DbLibreriaRead.close();
        return datos;
    }
}