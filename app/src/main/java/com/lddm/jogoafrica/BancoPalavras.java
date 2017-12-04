package com.lddm.jogoafrica;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.lddm.jogoafrica.data.JogoContract;
import com.lddm.jogoafrica.data.JogoDbHelper;

import java.util.ArrayList;

public class BancoPalavras extends AppCompatActivity {

    ListView listaPalavras;
    ArrayList<String> palavras;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banco_palavras);

       // listaPalavras = (ListView) findViewById(R.id.listaPalavras);
        palavras = new ArrayList<>();

        populaLista();
        adapter = new ArrayAdapter<>(this, R.layout.item_lista, R.id.listaView, palavras);
        ListView list = (ListView) findViewById(R.id.listaPalavras);
        list.setAdapter(adapter);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String palavra = palavras.get(i).toString();
                deletaSQL(palavra);
                adapter.notifyDataSetChanged();

            }
        });

        //resetar o banco de dados
        // SQLiteDatabase db = mDbHelper.getReadableDatabase();
        // mDbHelper.onUpgrade(db, 1, 1);
    }

    public void populaLista(){
        final JogoDbHelper mDbHelper = new JogoDbHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM palavras", null);

        if(cursor.moveToFirst()){

            do{
                String link = cursor.getString(cursor.getColumnIndex(JogoContract.PalavrasEntry.COLUMN_PALAVRA_NAME));
                palavras.add(link);

            } while (cursor.moveToNext());
        }
    }

    public void deletaSQL(String palavra){

        JogoDbHelper mDbHelper = new JogoDbHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        db.execSQL("DELETE FROM palavras WHERE nome='"+palavra+"'");
        db.close();
        palavras.clear();
        populaLista();

    }
}
