package com.lddm.jogoafrica.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by coras on 01/12/2017.
 */

public class JogoDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = JogoDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "jogo.db";

    private static final int DATABASE_VERSION = 1;

    public JogoDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Execute the SQL statement

        final  String SQL_CREATE_EQUIPE_TABLE =
                "CREATE TABLE " + JogoContract.EquipeEntry.TABLE_NAME + " (" +
                        JogoContract.EquipeEntry._ID + " INTEGER PRIMARY KEY,"+
                        JogoContract.EquipeEntry.COLUMN_EQUIPE_NAME +
                        " )";


        final String SQL_CREATE_JOGADOR_TABLE = "CREATE TABLE " + JogoContract.JogadoresEntry.TABLE_NAME + " (" +
                JogoContract.JogadoresEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                JogoContract.JogadoresEntry.COLUMN_JOGADOR_NAME + " TEXT NOT NULL, " +
                JogoContract.JogadoresEntry.COLUMN_FK_EQUIPE_ID + " INTEGER NOT NULL, " +
                "FOREIGN KEY (" +  JogoContract.JogadoresEntry.COLUMN_FK_EQUIPE_ID + ") REFERENCES " +
                JogoContract.EquipeEntry.TABLE_NAME +
                "(" + JogoContract.EquipeEntry._ID + ") ON DELETE CASCADE" +
                "); ";



        db.execSQL(SQL_CREATE_EQUIPE_TABLE);
        db.execSQL(SQL_CREATE_JOGADOR_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // The database is still at version 1, so there's nothing to do be done here.
        db.execSQL("DROP TABLE IF EXISTS " + JogoContract.EquipeEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + JogoContract.JogadoresEntry.TABLE_NAME);
        onCreate(db);
    }

}
