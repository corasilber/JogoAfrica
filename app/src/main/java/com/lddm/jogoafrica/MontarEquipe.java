package com.lddm.jogoafrica;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.lddm.jogoafrica.data.JogoContract;
import com.lddm.jogoafrica.data.JogoDbHelper;

import java.util.ArrayList;

public class MontarEquipe extends AppCompatActivity {

    Spinner numeroEquipe, numJogador;

    Button inserirEquipe, adicionar;

    ArrayList<String> equipes;
    int numEquipe;

    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_montar_equipe);

        numeroEquipe =(Spinner) findViewById(R.id.numEquipe);
        numJogador = (Spinner) findViewById(R.id.numJogador);
        inserirEquipe = (Button) findViewById(R.id.buttonInserir) ;
        adicionar = (Button) findViewById(R.id.button3);

        equipes = new ArrayList<>();
        Integer[] items = new Integer[]{2,3,4};

        ArrayAdapter<Integer> adapterEquipe = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, items);
        numeroEquipe.setAdapter(adapterEquipe);


        ArrayAdapter<Integer> adapterJogador = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, items);
        numJogador.setAdapter(adapterJogador);



//        if(count <= numEquipe) {

            //aqui vai abrir um dialog para a pessoa digitar o nome da equipe
            inserirEquipe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alertBuilder =  new AlertDialog.Builder(MontarEquipe.this); // mostra um alert dialog
                    View view2 =  getLayoutInflater().inflate(R.layout.tela_adiciona_equipe,null);
                    final EditText nomeEquipe = (EditText) view2.findViewById(R.id.nomeEquipe);
                    Button addEquipe= (Button) view2.findViewById(R.id.adicionar);
                    numEquipe = Integer.parseInt(numeroEquipe.getSelectedItem().toString());
                        alertBuilder.setView(view2);

                    final AlertDialog dialog = alertBuilder.create();
                    dialog.show();


                    addEquipe.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if(count < numEquipe) {
                                if (!nomeEquipe.getText().toString().isEmpty()) {
                                    String nome = nomeEquipe.getText().toString(); // nome da equipe que a pessoa digitar
                                    equipes.add(nome);
                                    dialog.dismiss();
                                    count++;
                                } else {
                                    nomeEquipe.setError("Digite um nome para inserir!");
                                }
                            } else {
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "Erro! Já foi inserido todas equipes!", Toast.LENGTH_SHORT);
                                toast.show();
                            }

                        }
                    }); // fim setOnClick botão adicionar nome
                }
            }); // fim setOnclick botão inserir equipe
        //} // inserir somente quantidade de equipes pre selecionadas


        adicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Networking.enviarEquipes(equipes, MainActivity.session, numJogador.getSelectedItem().toString());
                Intent changeScreen = new Intent(MontarEquipe.this, AdicionarJogador.class);
                String qtdJogador = numJogador.getSelectedItem().toString();
                changeScreen.putExtra("nomeEquipes", equipes);
//                changeScreen.putExtra("qtdPalavras", palavras);
//                changeScreen.putExtra("qtdJogador", qtdJogador);
                    changeScreen.putExtra("qtdJogador", qtdJogador);
                startActivity(changeScreen);
            }
        });


    }


}
