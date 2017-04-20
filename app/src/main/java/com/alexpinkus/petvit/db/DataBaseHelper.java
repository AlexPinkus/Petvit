package com.alexpinkus.petvit.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Rodrigo on 19/04/17.
 */

final public class DataBaseHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "petvit.db";
    private static final int SCHEMA_VERSION = 1;

    private Context context;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);

        this.context = context;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN){
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL("PRAGMA foreign_keys = ON"); // Esto nos permite activar el delete de las llaves foráneas
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        applySqlFile(db, "schema.sql");
        applySqlFile(db, "initial_data.sql");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // ESTA FUNCIÓN SIRVE PARA LEER LOS DATOS QUE SE ENCUENTRAN EN LOS ARCHIVOS .SQL DE LA CARPETA ASSETS (CREADA PREVIAMENTE)
    // IMPORTANTE NO TENER COMENTARIOS EN LA BASE DE DATOS

    private void applySqlFile(SQLiteDatabase db, String filename){

        BufferedReader br = null;

        try{
            InputStream is = context.getAssets().open(filename);
            br = new BufferedReader(new InputStreamReader(is));

            StringBuilder statement = new StringBuilder();
            for (String line; (line = br.readLine()) != null;){
                line = line.trim();
                if (!TextUtils.isEmpty(line) && !line.startsWith("--")){
                    statement.append(line);
                }

                if (line.endsWith(";")){
                    db.execSQL(statement.toString());
                    statement.setLength(0);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (br != null){
                try {
                    br.close();
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }

        }
    }
}
