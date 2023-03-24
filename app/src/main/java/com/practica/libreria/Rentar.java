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

public class Rentar extends AppCompatActivity {

    // INSTANCIAR IDs

    ImageButton volver, lista_rentas;
    EditText id_renta, id_usuario, id_libro;
    EditText fecha_renta;
    ImageButton agregar_renta, encontrar_renta, editar_renta, eliminar_renta;

    // INSTANCIAR LA BASE DE DATOS

    DbLibreria Renta = new DbLibreria(this,"BbLibreria2",null,1);

    String id_vieja = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rentar);

        // INSTANCIAR LOS DATOS

        volver = findViewById(R.id.ibback);
        lista_rentas = findViewById(R.id.ibrentlist);

        id_renta = findViewById(R.id.etidrent);
        id_usuario = findViewById(R.id.etiduser);
        id_libro = findViewById(R.id.etidbook);

        fecha_renta = findViewById(R.id.etrentdate);

        agregar_renta = findViewById(R.id.ibaddrent);
        encontrar_renta = findViewById(R.id.ibfindrent);
        editar_renta = findViewById(R.id.ibeditrent);
        eliminar_renta = findViewById(R.id.ibdeleterent);

        // -------------- //

        // CRUD

        // EVENTO AGREGAR RENTA

        agregar_renta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!id_renta.getText().toString().isEmpty() && !id_usuario.getText().toString().isEmpty() && !id_libro.getText().toString().isEmpty() && !fecha_renta.getText().toString().isEmpty()) {

                    SQLiteDatabase BdLibreriaRead = Renta.getReadableDatabase();

                    String query = "SELECT id_renta FROM Rentar WHERE id_renta = '" + id_renta.getText().toString() + "'";

                    Cursor cRenta = BdLibreriaRead.rawQuery(query, null);


                    // CONDICIONES DEL PROYECTO (A TENER EN CUENTA)

                    String libroQuery = "SELECT id_libro FROM Libros WHERE id_libro = '" + id_libro.getText().toString() + "' AND estatus_libro = 1";
                    String usuarioQuery = "SELECT id_usuario FROM Usuarios WHERE id_usuario = '" + id_usuario.getText().toString() + "' AND estatus_usuario = 1";

                    Cursor cLibro = BdLibreriaRead.rawQuery(libroQuery, null);
                    Cursor cUsuario = BdLibreriaRead.rawQuery(usuarioQuery, null);

                    if (!cLibro.moveToFirst()) {

                        Toast.makeText(getApplicationContext(),"LIBRO INEXISTENTE O NO DISPIBLE",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!cUsuario.moveToFirst()) {

                        Toast.makeText(getApplicationContext(),"USUARIO INEXISTENTE O SANCIONADO",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // VERIFICAR SI EL LIBRO ES PRESTADO

                    String libroPrestadoQuery = "SELECT id_libro FROM Rentar WHERE id_libro = '" + id_libro.getText().toString() + "'";
                    Cursor cLibroPrestado = BdLibreriaRead.rawQuery(libroPrestadoQuery, null);

                    if (cLibroPrestado.moveToFirst()) {

                        Toast.makeText(getApplicationContext(),"EL LIBRO YA ESTA PRESTADO",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // -------------------------------------------- //

                    if (!cRenta.moveToFirst()) { // SI NO ECONTRO LA ID
                        SQLiteDatabase BdLibreriaWrite = Renta.getWritableDatabase();
                        ContentValues cvRenta = new ContentValues();

                        cvRenta.put("id_renta", id_renta.getText().toString());
                        cvRenta.put("id_usuario", id_usuario.getText().toString());
                        cvRenta.put("id_libro", id_libro.getText().toString());
                        cvRenta.put("fecha_renta", fecha_renta.getText().toString());

                        BdLibreriaWrite.insert("Rentar",null, cvRenta);

                        BdLibreriaWrite.close();

                        Limpiar_campos();

                        Toast.makeText(getApplicationContext(),"RENTA GUARDADA",Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(getApplicationContext(),"LA RENTA YA EXISTE",Toast.LENGTH_SHORT).show();
                    }

                } else {

                    Toast.makeText(getApplicationContext(),"INGRESE TODOS LOS CAMPOS",Toast.LENGTH_SHORT).show();
                }
            }

        });


        // EVENTO ENCONTRAR USUARIO

        encontrar_renta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SQLiteDatabase DbLibreria = Renta.getReadableDatabase();

                String query = "SELECT id_usuario, id_libro, fecha_renta FROM Rentar WHERE id_renta = '" + id_renta.getText().toString() + "'";

                Cursor cRenta = DbLibreria.rawQuery(query, null);

                if (cRenta.moveToFirst()) {

                    id_usuario.setText(cRenta.getString(0));
                    id_libro.setText(cRenta.getString(1));
                    fecha_renta.setText(cRenta.getString(2));

                    id_vieja = id_renta.getText().toString();

                    DbLibreria.close();

                    Toast.makeText(getApplicationContext(), "RENTA ENCONTRADA", Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(getApplicationContext(), "RENTA NO ENCONTRADA", Toast.LENGTH_SHORT).show();
                }
            }

        });


        // EVENTO EDITAR USUARIO

        editar_renta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!id_renta.getText().toString().isEmpty() && !id_usuario.getText().toString().isEmpty() && !id_libro.getText().toString().isEmpty() && !fecha_renta.getText().toString().isEmpty()) {

                    SQLiteDatabase DbLibreriaWrite = Renta.getWritableDatabase();

                    if (id_renta.getText().toString().equals(id_vieja)) {

                        DbLibreriaWrite.execSQL("UPDATE Rentar SET id_usuario = '" + id_usuario.getText().toString() + "', id_libro = '" + id_libro.getText().toString() + "" + "', fecha_renta = " + fecha_renta.getText().toString() + " WHERE id_renta = '" + id_vieja + "'");

                        Limpiar_campos();

                        Toast.makeText(getApplicationContext(), "LIBRO EDITADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();

                    } else {

                        SQLiteDatabase DbLibreriaRead = Renta.getReadableDatabase();

                        String query = "SELECT id_libro FROM Libros WHERE id_libro = '" + id_libro.getText().toString() + "'";

                        Cursor cRenta = DbLibreriaRead.rawQuery(query, null);

                        if (!cRenta.moveToFirst()) { // NO ENCUENTRA LA ID INGRESADA

                            DbLibreriaWrite.execSQL("UPDATE Rentar SET id_renta = '" + id_renta.getText().toString() + "', id_usuario = '" + id_usuario.getText().toString() +  "', id_libro = '" + id_libro.getText().toString() + "" + "', fecha_renta = " + fecha_renta.getText().toString() + " WHERE id_renta = '" + id_vieja + "'");

                            Limpiar_campos();

                            Toast.makeText(getApplicationContext(), "RENTA EDITADA CORRECTAMENTE", Toast.LENGTH_SHORT).show();

                        } else {

                            Toast.makeText(getApplicationContext(), "LA ID RENTA YA EXISTE", Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {

                    Toast.makeText(getApplicationContext(),"INGRESE TODOS LOS CAMPOS",Toast.LENGTH_SHORT).show();
                }
            }

        });


        // METODO ELIMINAR USUARIO

        eliminar_renta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!id_renta.getText().toString().isEmpty() && !id_usuario.getText().toString().isEmpty() && !id_libro.getText().toString().isEmpty() && !fecha_renta.getText().toString().isEmpty()) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Rentar.this);

                    alertDialogBuilder.setMessage("¿SEGURO QUIERES ELIMINAR RENTA?");

                    alertDialogBuilder.setPositiveButton("ELIMINAR", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                            SQLiteDatabase BdLibreria = Renta.getWritableDatabase();

                            BdLibreria.execSQL("DELETE FROM Rentar WHERE id_renta = '" + id_renta.getText().toString() + "'");

                            Limpiar_campos();

                            Toast.makeText(getApplicationContext(),"RENTA ELIMINADO CORRECTAMENTE",Toast.LENGTH_SHORT).show();
                        }
                    });

                    alertDialogBuilder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                            Toast.makeText(getApplicationContext(),"RENTA NO ELIMINADO", Toast.LENGTH_SHORT).show();
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


        // EVENTO IR A LISTA DE RENTAS

        lista_rentas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent lista_rentas = new Intent(getApplicationContext(), Rentar_lista.class);

                startActivity(lista_rentas);
            }
        });
    }

    private void Limpiar_campos() {

        id_renta.setText("");
        id_usuario.setText("");
        id_libro.setText("");
        fecha_renta.setText("");

    }
}