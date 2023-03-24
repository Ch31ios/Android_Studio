package com.practica.libreria;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // INSTANCIAR IDs

        ImageButton usuarios = findViewById(R.id.ibusers);
        ImageButton libros = findViewById(R.id.ibbooks);
        ImageButton rentar_libros = findViewById(R.id.ibrentbooks);

        // -------------- //





        // EVENTO IR A PESTAÑA USUARIOS

        usuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent usuarios = new Intent(getApplicationContext(), Usuarios.class);

                startActivity(usuarios);
            }
        });


        // EVENTO IR A PESTAÑA LIBROS

        libros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent libros = new Intent(getApplicationContext(), Libros.class);

                startActivity(libros);
            }
        });


        // EVENTO IR A PESTAÑA RENTAR LIBROS

        rentar_libros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent rentar_libros = new Intent(getApplicationContext(), Rentar.class);

                startActivity(rentar_libros);
            }
        });


    }
}