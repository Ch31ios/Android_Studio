package com.practica.libreria;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbLibreria extends SQLiteOpenHelper {

    // CREACION DE TABLAS

    String TblUsuarios = "CREATE TABLE Usuarios (id_usuario text primary key, nombre_usuario text, email_usuario text, contraseña_usuario text, estatus_usuario integer)";

    String TblLibros = "CREATE TABLE Libros (id_libro text primary key, nombre_libro text, coste_libro text, estatus_libro integer)";

    String TblRentar = "CREATE TABLE Rentar (id_renta integer primary key autoincrement, id_usuario text, id_libro text, fecha_renta date)";

    // ------------------ //

    public DbLibreria(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {

        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(TblUsuarios);
        db.execSQL(TblLibros);
        db.execSQL(TblRentar);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE Usuarios");
        db.execSQL(TblUsuarios);

        db.execSQL("DROP TABLE Libros");
        db.execSQL(TblLibros);

        db.execSQL("DROP TABLE Rentar");
        db.execSQL(TblRentar);
    }
}

