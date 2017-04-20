package com.alexpinkus.petvit.db;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rodrigo on 19/04/17.
 */

class PetCursor extends CursorWrapper {
    public PetCursor(Cursor cursor) {
        super(cursor);
    }

    public Pet getPet(){
        Cursor cursor = getWrappedCursor();
        return new Pet(cursor.getInt(cursor.getColumnIndex(DBSchema.PetTable.Columns.ID)),
                cursor.getString(cursor.getColumnIndex(DBSchema.PetTable.Columns.NOMBRE)),
                cursor.getInt(cursor.getColumnIndex(DBSchema.PetTable.Columns.EDAD)),
                cursor.getInt(cursor.getColumnIndex(DBSchema.PetTable.Columns.PESO)),
                cursor.getInt(cursor.getColumnIndex(DBSchema.PetTable.Columns.RAZA_ID)),
                cursor.getString(cursor.getColumnIndex(DBSchema.PetTable.Columns.FOTO)));
    }
}

class RazaCursor extends CursorWrapper {
    public RazaCursor(Cursor cursor) {
        super(cursor);
    }

    public Raza getAllRazas(){
        Cursor cursor = getWrappedCursor();
        return new Raza(cursor.getInt(cursor.getColumnIndex(DBSchema.RazasTable.Columns.ID)),
                cursor.getInt(cursor.getColumnIndex(DBSchema.RazasTable.Columns.TIPO_RAZA)),
                cursor.getString(cursor.getColumnIndex(DBSchema.RazasTable.Columns.NOMBRE_RAZA)),
                cursor.getString(cursor.getColumnIndex(DBSchema.RazasTable.Columns.PESO_ADULTO)),
                cursor.getString(cursor.getColumnIndex(DBSchema.RazasTable.Columns.ALTURA_ADULTO)),
                cursor.getString(cursor.getColumnIndex(DBSchema.RazasTable.Columns.DESCRIPCION)),
                cursor.getString(cursor.getColumnIndex(DBSchema.RazasTable.Columns.ESPERANZA)),
                cursor.getString(cursor.getColumnIndex(DBSchema.RazasTable.Columns.ORIGEN)),
                cursor.getString(cursor.getColumnIndex(DBSchema.RazasTable.Columns.HISTORIA)),
                cursor.getString(cursor.getColumnIndex(DBSchema.RazasTable.Columns.FOTO)));
    }
}

class PorcionesCursor extends CursorWrapper {
    public PorcionesCursor(Cursor cursor) {
        super(cursor);
    }

    public Porciones getPorciones(){
        Cursor cursor = getWrappedCursor();
        return new Porciones(cursor.getInt(cursor.getColumnIndex("gramos")));
    }
}

public final class Querys {

    private DataBaseHelper dataBaseHelper;
    private SQLiteDatabase db;

    // CREAMOS EL CONSTRUCTOR, .GETWRITABLEDATABASE NOS PERMITIRA ESCRIBIR EN LA MISMA
    public Querys(Context context){
        dataBaseHelper = new DataBaseHelper(context);
        db = dataBaseHelper.getWritableDatabase();
    }

    // ESTA ES LA CONSULTA DE OBTENER A TODOS LOS CLIENTES, SU NOMBRE DEBERIA SER GETALLCLIENTSBYNAME
    public Pet getMyPet(){
       // ArrayList<Pet> list = new ArrayList<Pet>();
        Pet myPet = new Pet(0, "", 0, 0, 0, "");
        PetCursor cursor = new PetCursor(db.rawQuery("SELECT * FROM pet", null));
        while (cursor.moveToNext()){
            myPet = cursor.getPet();
        }
        cursor.close();

        return myPet;
    }

    public Porciones getMyPorcion(){
        // ArrayList<Pet> list = new ArrayList<Pet>();
        Porciones myPorcion = new Porciones(0);
        PorcionesCursor cursor = new PorcionesCursor(db.rawQuery("select gramos from pet p inner join razas r on (p.raza_id = r.tipo_raza) inner join porciones po on (po.raza_id = r.id) where edad_pet=p.edad", null));
        while (cursor.moveToNext()){
            myPorcion = cursor.getPorciones();
        }
        cursor.close();

        return myPorcion;
    }

    public List<Raza> getAllRazas(){
        ArrayList<Raza> list = new ArrayList<Raza>();

        RazaCursor cursor = new RazaCursor(db.rawQuery("SELECT * FROM razas", null));
        while (cursor.moveToNext()){
            list.add(cursor.getAllRazas());
        }
        cursor.close();

        return list;
    }

    public void updatePet(Pet pet, String petID){
        db.update(DBSchema.PetTable.NAME,
                pet.toContentValues(),
                DBSchema.PetTable.Columns.ID + " LIKE ?",
                new String[]{petID});
    }
}
