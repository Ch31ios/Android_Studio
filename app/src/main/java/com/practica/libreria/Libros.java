package com.practica.libreria;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

public class Libros extends AppCompatActivity {

    // INSTANCIAR IDs

    ImageButton volver, lista_libros;
    EditText id_libro, nombre_libro, coste_libro;
    Switch estatus_libro;
    ImageButton agregar_libro, encontrar_libro, editar_libro, eliminar_libro;

    // INSTANCIAR LA BASE DE DATOS

    DbLibreria Libro = new DbLibreria(this,"BbLibreria2",null,1);

    String id_vieja = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_libros);

        // INSTANCIAR LOS DATOS

        volver = findViewById(R.id.ibback);
        lista_libros = findViewById(R.id.ibbookslist);

        id_libro = findViewById(R.id.etidbook);
        nombre_libro = findViewById(R.id.etbookname);
        coste_libro = findViewById(R.id.etbookcost);

        estatus_libro = findViewById(R.id.swbookstatus);

        agregar_libro = findViewById(R.id.ibaddbook);
        encontrar_libro = findViewById(R.id.ibfindbook);
        editar_libro = findViewById(R.id.ibeditbook);
        eliminar_libro = findViewById(R.id.ibdeletebook);

        // -------------- //

        // CRUD

        // EVENTO AGREGAR LIBRO

        agregar_libro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!id_libro.getText().toString().isEmpty() && !nombre_libro.getText().toString().isEmpty() && !coste_libro.getText().toString().isEmpty()) {

                    SQLiteDatabase BdLibreriaRead = Libro.getReadableDatabase();

                    String query = "SELECT id_libro FROM Libros WHERE id_libro = '" + id_libro.getText().toString() + "'";

                    Cursor cLibro = BdLibreriaRead.rawQuery(query, null);

                    if (!cLibro.moveToFirst()) { // SI NO ECONTRO LA ID

                        SQLiteDatabase BdLibreriaWrite = Libro.getWritableDatabase();

                        ContentValues cvLibro = new ContentValues();

                        cvLibro.put("id_libro", id_libro.getText().toString());
                        cvLibro.put("nombre_libro", nombre_libro.getText().toString());
                        cvLibro.put("coste_libro", coste_libro.getText().toString());
                        cvLibro.put("estatus_libro", estatus_libro.isChecked() ? 1 : 0);

                        BdLibreriaWrite.insert("Libros",null, cvLibro);

                        BdLibreriaWrite.close();

                        Limpiar_campos();

                        Toast.makeText(getApplicationContext(),"LIBRO GUARDADO",Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(getApplicationContext(),"EL LIBRO YA EXISTE",Toast.LENGTH_SHORT).show();
                    }

                } else {

                    Toast.makeText(getApplicationContext(),"INGRESE TODOS LOS CAMPOS",Toast.LENGTH_SHORT).show();
                }
            }

        });


        // EVENTO ENCONTRAR USUARIO

        encontrar_libro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SQLiteDatabase DbLibreria = Libro.getReadableDatabase();

                String query = "SELECT nombre_libro, coste_libro, estatus_libro FROM Libros WHERE id_libro = '" + id_libro.getText().toString() + "'";

                Cursor cLibro = DbLibreria.rawQuery(query, null);

                if (cLibro.moveToFirst()) {

                    nombre_libro.setText(cLibro.getString(0));
                    coste_libro.setText(cLibro.getString(1));
                    estatus_libro.setChecked(cLibro.getInt(2) == 1 ? true : false);

                    id_vieja = id_libro.getText().toString();

                    DbLibreria.close();

                    Toast.makeText(getApplicationContext(), "ID ENCONTRADA", Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(getApplicationContext(), "ID NO ENCONTRADA", Toast.LENGTH_SHORT).show();
                }
            }

        });


        // EVENTO EDITAR USUARIO

        editar_libro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!id_libro.getText().toString().isEmpty() && !nombre_libro.getText().toString().isEmpty() && !coste_libro.getText().toString().isEmpty()) {

                    SQLiteDatabase DbLibreriaWrite = Libro.getWritableDatabase();

                    if (id_libro.getText().toString().equals(id_vieja)) {

                        DbLibreriaWrite.execSQL("UPDATE Libros SET nombre_libro = '" + nombre_libro.getText().toString() + "', coste_libro = '" + coste_libro.getText().toString() + "', estatus_libro = " + (estatus_libro.isChecked() ? 1 : 0) + " WHERE id_libro = '" + id_vieja + "'");

                        Limpiar_campos();

                        Toast.makeText(getApplicationContext(), "LIBRO EDITADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();

                    } else {

                        SQLiteDatabase DbLibreriaRead = Libro.getReadableDatabase();

                        String query = "SELECT id_libro FROM Libros WHERE id_libro = '" + id_libro.getText().toString() + "'";

                        Cursor cLibro = DbLibreriaRead.rawQuery(query, null);

                        if (!cLibro.moveToFirst()) { // NO ENCUENTRA LA ID INGRESADA

                            DbLibreriaWrite.execSQL("UPDATE Libros SET id_libro = '" + id_libro.getText().toString() + "', nombre_libro = '" + nombre_libro.getText().toString() + "', coste_libro = '" + coste_libro.getText().toString() + "', estatus_libro = " + (estatus_libro.isChecked() ? 1 : 0) + " WHERE id_libro = '" + id_vieja + "'");

                            Limpiar_campos();

                            Toast.makeText(getApplicationContext(), "LIBRO EDITADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();

                        } else {

                            Toast.makeText(getApplicationContext(), "LA ID YA EXISTE", Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {

                    Toast.makeText(getApplicationContext(),"INGRESE TODOS LOS CAMPOS",Toast.LENGTH_SHORT).show();
                }
            }

        });


        // METODO ELIMINAR USUARIO

        eliminar_libro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!id_libro.getText().toString().isEmpty() && !nombre_libro.getText().toString().isEmpty() && !coste_libro.getText().toString().isEmpty()) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Libros.this);

                    alertDialogBuilder.setMessage("¿SEGURO QUIERES ELIMINAR LIBRO?");

                    alertDialogBuilder.setPositiveButton("ELIMINAR", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                            SQLiteDatabase BdLibreria = Libro.getWritableDatabase();

                            BdLibreria.execSQL("DELETE FROM Libros WHERE id_libro = '" + id_libro.getText().toString() + "'");

                            Limpiar_campos();

                            Toast.makeText(getApplicationContext(),"LIBRO ELIMINADO CORRECTAMENTE",Toast.LENGTH_SHORT).show();
                        }
                    });

                    alertDialogBuilder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                            Toast.makeText(getApplicationContext(),"LIBRO NO ELIMINADO", Toast.LENGTH_SHORT).show();
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();

                    alertDialog.show();

                } else {

                    Toast.makeText(getApplicationContext(),"INGRESE TODOS LOS CAMPOS", Toast.LENGTH_SHORT).show();
                }
            }

        });


        // EVENTO VOLVER AL INICIO

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent volver = new Intent(getApplicationContext(), MainActivity.class);

                startActivity(volver);
            }
        });


        // EVENTO IR A LISTA DE LIBROS

        lista_libros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent lista_libros = new Intent(getApplicationContext(), Libros_lista.class);

                startActivity(lista_libros);
            }
        });
    }

    // LIMPIAR CAMPOS

    private void Limpiar_campos() {

        id_libro.setText("");
        nombre_libro.setText("");
        coste_libro.setText("");
        estatus_libro.setChecked(false);

    }
}
