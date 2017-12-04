package com.lddm.jogoafrica;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextClock;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EquipeJoga extends AppCompatActivity {

    List<Equipe> listaEquipes;
    ArrayList<String> todasPalavras, todasPalavrasClone, pontuacaoEquipes;
    TextView nomeEquipe, nomeJogador, fase;
    Button jogar;
    int countEquipe, countJogador, qtdPalavras, pontuacao, qualFase;
    String equipe, jogador;
    ListView listaPontuacao;
    ArrayAdapter<String> adapter;
    private Handler handler = new Handler();
    private Runnable runnable;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipe_joga);

        listaEquipes = (List<Equipe>) getIntent().getSerializableExtra("listaEquipes");
        todasPalavras = (ArrayList<String>) getIntent().getSerializableExtra("todasPalavras");
        todasPalavrasClone = (ArrayList<String>) ((ArrayList<String>) getIntent().getSerializableExtra("todasPalavras")).clone();

        nomeEquipe = (TextView) findViewById(R.id.nomeEquipe);
        nomeJogador = (TextView) findViewById (R.id.jogador);
        jogar = (Button) findViewById(R.id.jogar);
        listaPontuacao = (ListView) findViewById(R.id.pontuacao);
        fase = (TextView) findViewById(R.id.fase);
        pontuacaoEquipes = new ArrayList<>();

        populaListaPontuacao();
        qualFase = 0;

        adapter = new ArrayAdapter<>(this, R.layout.item_lista,R.id.listaView, pontuacaoEquipes);
        ListView list = (ListView) findViewById(R.id.pontuacao);
        list.setAdapter(adapter);

         qtdPalavras = todasPalavras.size();

        setaFaseTextView();


         countEquipe = 0;
         countJogador = 0;
         equipe = listaEquipes.get(0).getNome();
         jogador = listaEquipes.get(0).getListaJogador().get(0).getNome();
         nomeEquipe.setText(equipe);
         nomeJogador.setText(jogador);



        jogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Networking.startTimer(EquipeJoga.this, MainActivity.session);
            }
        });

        runnable = new Runnable() {
            @Override
            public void run() {
                Networking.getTimer(EquipeJoga.this, MainActivity.session);
                handler.postDelayed(this, 3000);
            }
        };
        handler.postDelayed(runnable, 3000);
       }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        pontuacao = data.getIntExtra("pontuacao", 0);
        int pontInicial = listaEquipes.get(countEquipe % listaEquipes.size()).getPontuacao();
        listaEquipes.get(countEquipe % listaEquipes.size()).setPontuacao(pontuacao + pontInicial);
        pontuacaoEquipes.clear(); // limpa a lista atual
        populaListaPontuacao();
        adapter.notifyDataSetChanged(); //atualiza a pontuação das equipes

        qualFase = data.getIntExtra("fase", 0);
        setaFaseTextView();

        countEquipe++;
        qtdPalavras++;
        todasPalavras = data.getStringArrayListExtra("SOBRANDO");


        if(qualFase < 4) {
            if (countEquipe % listaEquipes.size() == 0) {
                countJogador++;
                int size = listaEquipes.get(countEquipe % listaEquipes.size()).getListaJogador().size();

                equipe = listaEquipes.get(countEquipe % listaEquipes.size()).getNome();

                jogador = listaEquipes.get(countEquipe % listaEquipes.size()).getListaJogador()
                        .get(countJogador % size).getNome();

                nomeEquipe.setText(equipe);
                nomeJogador.setText(jogador);

            } else {
                int size = listaEquipes.get(countEquipe % listaEquipes.size()).getListaJogador().size();

                equipe = listaEquipes.get(countEquipe % listaEquipes.size()).getNome();
                jogador = listaEquipes.get(countEquipe % listaEquipes.size()).getListaJogador().get(countJogador % size).getNome();

                nomeEquipe.setText(equipe);
                nomeJogador.setText(jogador);
            }
        } else {
            Intent intent = new Intent(EquipeJoga.this, TelaFinal.class);
            intent.putExtra("listaEquipes", (Serializable) listaEquipes);
            //intent.putExtra("pontuacao", corretos);
            startActivity(intent);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public int achaQtdJogador(){
        int count =0;
        for(int i =0; i < listaEquipes.size();i++){
            count += listaEquipes.get(i).getListaJogador().size();
        }
        return count;
    }

    public void populaListaPontuacao(){
        for(int i = 0; i < listaEquipes.size(); i++){
            String aux = listaEquipes.get(i).getNome() + " -- " + listaEquipes.get(i).getPontuacao() + " ponto(s)";
            pontuacaoEquipes.add(aux);
        }
    }

    public void setaFaseTextView(){
        if(qualFase == 0){
            fase.setText("DESCREVA A PALAVRA");
        }else if(qualFase == 1){
            fase.setText("DESCREVA A PALAVRA USANDO SOMENTE UMA PALAVRA");
        } else if(qualFase == 2){
            fase.setText("FAÇA UMA MÍMICA");
        }
    }

    public void startGame(long timestamp) {
        if (timestamp > 0) {
            handler.removeCallbacks(runnable);
            Intent changeScreen = new Intent(EquipeJoga.this, TimerActivity.class);
            changeScreen.putExtra("todasPalavras", todasPalavras);
            changeScreen.putExtra("todasPalavrasClone", todasPalavrasClone);
            changeScreen.putExtra("nomeEquipe", equipe);
            changeScreen.putExtra("fase", qualFase);
            changeScreen.putExtra("nomeJogador", jogador);
            changeScreen.putExtra("listaEquipes", (Serializable) listaEquipes);
            changeScreen.putExtra("targetTime", timestamp);
            startActivityForResult(changeScreen, 1);
        }
    }

}
