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

public class Usuarios extends AppCompatActivity {

    // INSTANCIAR IDs

    ImageButton volver, lista_usuarios;
    EditText id_usuario, nombre_usuario, email_usuario, contraseña_usuario;
    Switch estatus_usuario;
    ImageButton agregar_usuario, encontrar_usuario, editar_usuario, eliminar_usuario;

    // INSTANCIAR LA BASE DE DATOS

    DbLibreria Usuario = new DbLibreria(this,"BbLibreria2",null,1);

    String id_vieja = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);

        // INSTANCIAR LOS DATOS

        volver = findViewById(R.id.ibback);
        lista_usuarios = findViewById(R.id.ibuserslist);

        id_usuario = findViewById(R.id.etiduser);
        nombre_usuario = findViewById(R.id.etusername);
        email_usuario = findViewById(R.id.etuseremail);
        contraseña_usuario = findViewById(R.id.etuserpassword);

        estatus_usuario = findViewById(R.id.swuserstatus);

        agregar_usuario = findViewById(R.id.ibadduser);
        encontrar_usuario = findViewById(R.id.ibfinduser);
        editar_usuario = findViewById(R.id.ibedituser);
        eliminar_usuario = findViewById(R.id.ibdeleteuser);

        // -------------- //

        // CRUD

        // EVENTO AGREGAR USUARIO

        agregar_usuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!id_usuario.getText().toString().isEmpty() && !nombre_usuario.getText().toString().isEmpty() && !email_usuario.getText().toString().isEmpty() && !contraseña_usuario.getText().toString().isEmpty()) {

                    SQLiteDatabase BdLibreriaRead = Usuario.getReadableDatabase();

                    String query = "SELECT id_usuario FROM Usuarios WHERE id_usuario = '" + id_usuario.getText().toString() + "'";

                    Cursor cUsuario = BdLibreriaRead.rawQuery(query, null);

                    if (!cUsuario.moveToFirst()) { // SI NO ECONTRO LA ID

                        SQLiteDatabase BdLibreriaWrite = Usuario.getWritableDatabase();

                        ContentValues cvUsuario = new ContentValues();

                        cvUsuario.put("id_usuario", id_usuario.getText().toString());
                        cvUsuario.put("nombre_usuario", nombre_usuario.getText().toString());
                        cvUsuario.put("email_usuario", email_usuario.getText().toString());
                        cvUsuario.put("contraseña_usuario", contraseña_usuario.getText().toString());
                        cvUsuario.put("estatus_usuario", estatus_usuario.isChecked() ? 1 : 0);

                        BdLibreriaWrite.insert("Usuarios",null, cvUsuario);

                        BdLibreriaWrite.close();

                        Limpiar_campos();

                        Toast.makeText(getApplicationContext(),"USUARIO GUARDADO",Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(getApplicationContext(),"EL USUARIO YA EXISTE",Toast.LENGTH_SHORT).show();
                    }

                } else {

                    Toast.makeText(getApplicationContext(),"INGRESE TODOS LOS CAMPOS",Toast.LENGTH_SHORT).show();
                }
            }

        });


        // EVENTO ENCONTRAR USUARIO

        encontrar_usuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SQLiteDatabase DbLibreria = Usuario.getReadableDatabase();

                String query = "SELECT nombre_usuario, email_usuario, estatus_usuario FROM Usuarios WHERE id_usuario = '" + id_usuario.getText().toString() + "'";

                Cursor cUsuario = DbLibreria.rawQuery(query, null);

                if (cUsuario.moveToFirst()) {

                    nombre_usuario.setText(cUsuario.getString(0));
                    email_usuario.setText(cUsuario.getString(1));
                    estatus_usuario.setChecked(cUsuario.getInt(2) == 1 ? true : false);

                    id_vieja = id_usuario.getText().toString();

                    DbLibreria.close();

                    Toast.makeText(getApplicationContext(), "ID ENCONTRADA", Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(getApplicationContext(), "ID NO ENCONTRADA", Toast.LENGTH_SHORT).show();
                }
            }

        });


        // EVENTO EDITAR USUARIO

        editar_usuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!id_usuario.getText().toString().isEmpty() && !nombre_usuario.getText().toString().isEmpty() && !email_usuario.getText().toString().isEmpty() && !contraseña_usuario.getText().toString().isEmpty()) {

                    SQLiteDatabase DbLibreriaWrite = Usuario.getWritableDatabase();

                    if (id_usuario.getText().toString().equals(id_vieja)) {

                        DbLibreriaWrite.execSQL("UPDATE Usuarios SET nombre_usuario = '" + nombre_usuario.getText().toString() + "', email_usuario = '" + email_usuario.getText().toString() + "', estatus_usuario = " + (estatus_usuario.isChecked() ? 1 : 0) + " WHERE id_usuario = '" + id_vieja + "'");

                        Limpiar_campos();

                        Toast.makeText(getApplicationContext(), "USUARIO EDITADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();

                    } else {

                        SQLiteDatabase DbLibreriaRead = Usuario.getReadableDatabase();

                        String query = "SELECT id_usuario FROM Usuarios WHERE id_usuario = '" + id_usuario.getText().toString() + "'";

                        Cursor cUsuario = DbLibreriaRead.rawQuery(query, null);

                        if (!cUsuario.moveToFirst()) { // NO ENCUENTRA LA ID INGRESADA

                            DbLibreriaWrite.execSQL("UPDATE Usuarios SET id_usuario = '" + id_usuario.getText().toString() + "', nombre_usuario = '" + nombre_usuario.getText().toString() + "', email_usuario = '" + email_usuario.getText().toString() + "', estatus_usuario = " + (estatus_usuario.isChecked() ? 1 : 0) + " WHERE id_usuario = '" + id_vieja + "'");

                            Limpiar_campos();

                            Toast.makeText(getApplicationContext(), "USUARIO EDITADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();

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

        eliminar_usuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!id_usuario.getText().toString().isEmpty() && !nombre_usuario.getText().toString().isEmpty() && !email_usuario.getText().toString().isEmpty() && !contraseña_usuario.getText().toString().isEmpty()) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Usuarios.this);

                    alertDialogBuilder.setMessage("¿SEGURO QUIERES ELIMINAR USUARIO?");

                    alertDialogBuilder.setPositiveButton("ELIMINAR", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                            SQLiteDatabase BdLibreria = Usuario.getWritableDatabase();

                            BdLibreria.execSQL("DELETE FROM Usuarios WHERE id_usuario = '" + id_usuario.getText().toString() + "'");

                            Limpiar_campos();

                            Toast.makeText(getApplicationContext(),"USUARIO ELIMINADO CORRECTAMENTE",Toast.LENGTH_SHORT).show();
                        }
                    });

                    alertDialogBuilder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                            Toast.makeText(getApplicationContext(),"USUARIO NO ELIMINADO", Toast.LENGTH_SHORT).show();
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


        // EVENTO IR A LISTA DE USUARIOS

        lista_usuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent lista_usuarios = new Intent(getApplicationContext(), Usuarios_lista.class);

                startActivity(lista_usuarios);
            }
        });

    }

    // LIMPIAR CAMPOS

    private void Limpiar_campos() {

        id_usuario.setText("");
        nombre_usuario.setText("");
        email_usuario.setText("");
        contraseña_usuario.setText("");
        estatus_usuario.setChecked(false);

    }
}
