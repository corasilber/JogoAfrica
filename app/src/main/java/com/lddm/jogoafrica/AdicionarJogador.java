package com.lddm.jogoafrica;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AdicionarJogador extends AppCompatActivity {

    ArrayList<String> listaEquipes, todasPalavras;
    Integer quantidadePalavras, quantidadeJogadores;
    List<Equipe> equipe = new ArrayList<>();
    List<Jogador> jogadores = new ArrayList<>();
    int count = 0;
    Button jogar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_jogador);

       // listaEquipes =  getIntent().getStringArrayListExtra("nomeEquipes");
       listaEquipes = (ArrayList<String>) getIntent().getSerializableExtra("nomeEquipes");
       populaEquipe(listaEquipes);
       todasPalavras = new ArrayList<>();

        jogar = (Button) findViewById(R.id.jogarButton);


       // quantidadePalavras = Integer.parseInt(getIntent().getStringExtra("qtdPalavras")); // pega a qtd palavras do outro intent
       // quantidadeJogadores = Integer.parseInt(getIntent().getStringExtra("qtdJogadores")); // pega a qtd de jogadores do outro intent


       final LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linearLayout);

       //adicionar vários botões de acordo com a quantidade de equipes
        for(int i =0; i<listaEquipes.size(); i++){
           Button botao = new Button(AdicionarJogador.this);
                botao.setText(listaEquipes.get(i));
                botao.setId(i); // id do botao sera a posicao na lista com nome das equipes
                linearLayout.addView(botao);
       }


       for(int i = 0; i < listaEquipes.size(); i++){

           final Button botao = (Button) findViewById(i);

                botao.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Jogador jog = new Jogador();
                        String nomeEqui = botao.getText().toString(); // pega equipe selecionada
                        jog.setEquipe(nomeEqui);

                        AlertDialog.Builder alertBuilder =  new AlertDialog.Builder(AdicionarJogador.this); // mostra um alert dialog
                        View view2 =  getLayoutInflater().inflate(R.layout.tela_adiciona_jogador_palavra,null);


                        final EditText nome = (EditText) view2.findViewById(R.id.nomeJogador);
                        final EditText palavraA = (EditText) view2.findViewById(R.id.palavra1);
                        final EditText palavraB = (EditText) view2.findViewById(R.id.palavra2);
                        final EditText palavraC = (EditText) view2.findViewById(R.id.palavra3);
                        Button addJogador = (Button) view2.findViewById(R.id.adicionaPalavras);

                        alertBuilder.setView(view2);
                        final AlertDialog dialog = alertBuilder.create();

                        dialog.show();

                        addJogador.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if ( !nome.getText().toString().isEmpty() ||
                                        !palavraA.getText().toString().isEmpty() ||
                                        !palavraB.getText().toString().isEmpty() ||
                                        !palavraC.getText().toString().isEmpty()) {

                                    jog.setNome(nome.getText().toString());

                                    String palavraUm = palavraA.getText().toString();
                                    String palavraDois= palavraB.getText().toString();
                                    String palavraTres = palavraC.getText().toString();
                                    todasPalavras.add(palavraUm);
                                    todasPalavras.add(palavraDois);
                                    todasPalavras.add(palavraTres);

                                    String[] array = {palavraUm, palavraDois, palavraTres};
                                    jog.setPalavras(array);
                                    Toast toast = Toast.makeText(getApplicationContext(),
                                            "Palavras adicionadas com sucesso!", Toast.LENGTH_SHORT);
                                    toast.show();
                                    jogadores.add(jog);
                                    dialog.dismiss();
                                } else {
                                    Toast toast = Toast.makeText(getApplicationContext(),
                                            "Erro! Falta inserir alguma(s) palavra(s) e/ou nome", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            }
                        });
                    }
                });

       }

       jogar.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               adicionaJogadoresNaListaEquipe();
               Intent changeScreen = new Intent(AdicionarJogador.this, TelaPreparacao.class);
                    changeScreen.putExtra("listaEquipes", (Serializable) equipe);
                    changeScreen.putExtra("todasPalavras", todasPalavras);
               startActivity(changeScreen);

           }
       });
    }

    public void populaEquipe(ArrayList<String> listaEquipes){


        for(int i =0; i < listaEquipes.size(); i++){
            Equipe e = new Equipe();
            e.setNome(listaEquipes.get(i));
            equipe.add(e);
        }
    }

    public int getPosicaoNaLista(String nome){
        int pos=0;
        for(int i = 0; i < equipe.size(); i++){
            if(equipe.get(i).getNome().equals(nome)) pos = i;
        }
        return pos;
    }
     public void adicionaJogadoresNaListaEquipe(){

         for(int i =0; i < equipe.size(); i++){
             String nomeEquipe = equipe.get(i).getNome();
             List<Jogador> joga = new ArrayList<>(); // nova lista de jogadores da mesma equipe

             for(int k =0; k < jogadores.size(); k++){
                 if(jogadores.get(k).getEquipe() == nomeEquipe){
                     joga.add(jogadores.get(k));
                 }
             }
             equipe.get(i).setListaJogador(joga);
         }
     }

}
