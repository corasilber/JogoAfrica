package com.lddm.jogoafrica;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.lddm.jogoafrica.data.JogoContract;
import com.lddm.jogoafrica.data.JogoDbHelper;

public class MontarEquipe extends AppCompatActivity {

    Spinner numeroEquipe, numJogador, qtdPalavras;

    Button inserirEquipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_montar_equipe);

        numeroEquipe =(Spinner) findViewById(R.id.numEquipe);
        numJogador = (Spinner) findViewById(R.id.numJogador);
        qtdPalavras = (Spinner) findViewById(R.id.qtdPalavra);
        inserirEquipe = (Button) findViewById(R.id.buttonInserir) ;

        Integer[] items = new Integer[]{1,2,3};

        ArrayAdapter<Integer> adapterEquipe = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, items);
        numeroEquipe.setAdapter(adapterEquipe);

        ArrayAdapter<Integer> adapterJogador = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, items);
        numJogador.setAdapter(adapterJogador);

        ArrayAdapter<Integer> adapterPalavras = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, items);
        qtdPalavras.setAdapter(adapterPalavras);

        //aqui vai abrir um dialog para a pessoa digitar o nome da equipe
        inserirEquipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertBuilder =  new AlertDialog.Builder(MontarEquipe.this); // mostra um alert dialog
                View view2 =  getLayoutInflater().inflate(R.layout.tela_adiciona_equipe,null);
                final EditText nomeEquipe = (EditText) view2.findViewById(R.id.nomeEquipe);
                Button addLink = (Button) view2.findViewById(R.id.button);

                alertBuilder.setView(view2);

                final AlertDialog dialog = alertBuilder.create();
                dialog.show();

                addLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        if(!nomeEquipe.getText().toString().isEmpty()){
                            String nome = nomeEquipe.getText().toString(); // nome da equipe que a pessoa digitar
                            //precisamos adicionar no banco de dados
                            //vamos criar uma tabela para equipe com nomeEquipe e nome jogadores?
                            adicionaEquipeSQL(nome);
                            dialog.dismiss();
                        } else {
                            nomeEquipe.setError("Digite um nome para inserir!");

                        }

                    }
                }); // fim setOnClick botão adicionar nome
            }
        }); // fim setOnclick botão inserir equipe

    }



    //insere na tabela Equipe o nomes das equipes digitadas
    public void adicionaEquipeSQL(String nome){

        final JogoDbHelper mDbHelper = new JogoDbHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(JogoContract.EquipeEntry.COLUMN_EQUIPE_NAME, nome);
        db.insert(JogoContract.EquipeEntry.TABLE_NAME, null, values);

    }
}
