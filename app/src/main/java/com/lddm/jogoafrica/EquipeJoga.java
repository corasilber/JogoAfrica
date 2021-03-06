package com.lddm.jogoafrica;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EquipeJoga extends AppCompatActivity implements GameStateInterface{

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
    private Runnable getNewWords;
    private Runnable gameStateRunnable = new Runnable() {
        @Override
        public void run() {
            Networking.getGameState(EquipeJoga.this, MainActivity.session);
            handler.postDelayed(this, 500);
        }
    };
    private boolean comecouJogo = false;
    private int versaoPalavras = 0;

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
                comecouJogo = true;
                Networking.startTimer(EquipeJoga.this, MainActivity.session);
            }
        });

        runnable = new Runnable() {
            @Override
            public void run() {
                Networking.getTimer(EquipeJoga.this, MainActivity.session);
                handler.postDelayed(this, 500);
            }
        };

        getNewWords = new Runnable() {
            @Override
            public void run() {
                Networking.getGameState(EquipeJoga.this, MainActivity.session);
                handler.postDelayed(this, 500);

            }
        };
        listenToServer();
       }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (comecouJogo) {
            Networking.stopTimer(this, MainActivity.session, data.getStringArrayListExtra("SOBRANDO"));
            handler.postDelayed(gameStateRunnable, 500);
        } else {
            handler.postDelayed(gameStateRunnable, 500);
        }

//        qualFase = data.getIntExtra("fase", 0);

        comecouJogo = false;


        super.onActivityResult(requestCode, resultCode, data);
    }

    public void activityResultServidor(ArrayList<String> palavras){
        // pontuacao = data.getIntExtra("pontuacao", 0);
        pontuacao = todasPalavras.size() - palavras.size();
        todasPalavras = palavras;
        int pontInicial = listaEquipes.get(countEquipe % listaEquipes.size()).getPontuacao();

        listaEquipes.get(countEquipe % listaEquipes.size()).setPontuacao(pontuacao + pontInicial);
        pontuacaoEquipes.clear(); // limpa a lista atual
        populaListaPontuacao();
        adapter.notifyDataSetChanged(); //atualiza a pontuação das equipes

        if (palavras.isEmpty()) {
            qualFase++;
            todasPalavras = new ArrayList<>(todasPalavrasClone);
        }
        setaFaseTextView();

        countEquipe++;
        qtdPalavras++;

        if(qualFase < 3) {
            //já rodou por todas equipes -> muda jogador
            if (countEquipe % listaEquipes.size() == 0) {
                countJogador++;
                int size = listaEquipes.get(countEquipe % listaEquipes.size()).getListaJogador().size();

                equipe = listaEquipes.get(countEquipe % listaEquipes.size()).getNome();
                jogador = listaEquipes.get(countEquipe % listaEquipes.size())
                        .getListaJogador().get(countJogador % size)
                        .getNome();

                nomeEquipe.setText(equipe);
                nomeJogador.setText(jogador);

            } else {
                int size = listaEquipes.get(countEquipe % listaEquipes.size()).getListaJogador().size();

                equipe = listaEquipes.get(countEquipe % listaEquipes.size()).getNome();
                jogador = listaEquipes.get(countEquipe % listaEquipes.size())
                        .getListaJogador()
                        .get(countJogador % size)
                        .getNome();

                nomeEquipe.setText(equipe);
                nomeJogador.setText(jogador);
            }
            listenToServer();
        } else {
            Intent intent = new Intent(EquipeJoga.this, TelaFinal.class);
            intent.putExtra("listaEquipes", (Serializable) listaEquipes);
            startActivity(intent);
        }

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
            fase.setText("DESCREVA USANDO UMA PALAVRA");
        } else if(qualFase == 2){
            fase.setText("FAÇA UMA MÍMICA");
        }
    }

    public void didStartGame(boolean didStart) {
        comecouJogo = didStart;
    }

    public void startGame(long timestamp) {
        if (timestamp > 0) {
            handler.removeCallbacks(runnable);
            handler.removeCallbacks(getNewWords);
            handler.removeCallbacks(gameStateRunnable);
            Intent changeScreen = new Intent(EquipeJoga.this, TimerActivity.class);
            changeScreen.putExtra("todasPalavras", todasPalavras);
            changeScreen.putExtra("todasPalavrasClone", todasPalavrasClone);
            changeScreen.putExtra("nomeEquipe", equipe);
            changeScreen.putExtra("fase", qualFase);
            changeScreen.putExtra("comecouJogo", comecouJogo);
            changeScreen.putExtra("nomeJogador", jogador);
            changeScreen.putExtra("listaEquipes", (Serializable) listaEquipes);
            changeScreen.putExtra("targetTime", timestamp);
            changeScreen.putExtra("versaoPalavras", versaoPalavras);
            startActivityForResult(changeScreen, 1);
        }
    }

    public void getGameState(GameState gameState) {
        if (gameState.words_version > versaoPalavras) {
            handler.removeCallbacks(gameStateRunnable);
            versaoPalavras = gameState.words_version;
            activityResultServidor(gameState.words);
        }
    }

    private void listenToServer() {
        handler.postDelayed(runnable, 500);
    }

}
