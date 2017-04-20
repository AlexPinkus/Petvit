package com.alexpinkus.petvit.db;

import android.content.ContentValues;

/**
 * Created by Rodrigo on 19/04/17.
 */

public class Pet {

    private int id;
    private String nombre;
    private int edad;
    private int peso;
    private int razaId;
    private String foto;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public int getRazaId() {
        return razaId;
    }

    public void setRazaId(int razaId) {
        this.razaId = razaId;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Pet(int id, String nombre, int edad, int peso, int razaId, String foto) {
        this.id = id;
        this.nombre = nombre;
        this.edad = edad;
        this.peso = peso;
        this.razaId = razaId;
        this.foto = foto;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(DBSchema.PetTable.Columns.ID, id);
        values.put(DBSchema.PetTable.Columns.NOMBRE, nombre);
        values.put(DBSchema.PetTable.Columns.EDAD, edad);
        values.put(DBSchema.PetTable.Columns.PESO, peso);
        values.put(DBSchema.PetTable.Columns.RAZA_ID, razaId);
        values.put(DBSchema.PetTable.Columns.FOTO, foto);

        return values;
    }
}
