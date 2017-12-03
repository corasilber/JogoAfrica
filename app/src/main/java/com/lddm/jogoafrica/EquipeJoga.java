package com.lddm.jogoafrica;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class EquipeJoga extends AppCompatActivity {

    List<Equipe> listaEquipes;
    ArrayList<String> todasPalavras;
    TextView nomeEquipe, nomeJogador;
    Button jogar;
    int countEquipe, countJogador, qtdPalavras, pontuacao;
    String equipe, jogador;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipe_joga);

        listaEquipes = (List<Equipe>) getIntent().getSerializableExtra("listaEquipes");
        todasPalavras = (ArrayList<String>) getIntent().getSerializableExtra("todasPalavras");
        nomeEquipe = (TextView) findViewById(R.id.nomeEquipe);
        nomeJogador = (TextView) findViewById (R.id.jogador);
        jogar = (Button) findViewById(R.id.jogar);

        qtdPalavras = todasPalavras.size();


            countEquipe = 0;
            countJogador = 0;
            equipe = listaEquipes.get(0).getNome();
            jogador = listaEquipes.get(0).getListaJogador().get(0).getNome();
            nomeEquipe.setText(equipe);
            nomeJogador.setText(jogador);



                jogar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent changeScreen = new Intent(EquipeJoga.this, TimerActivity.class);
                        changeScreen.putExtra("todasPalavras", todasPalavras);
                        changeScreen.putExtra("nomeEquipe", equipe);
                        changeScreen.putExtra("nomeJogador", jogador);
                        startActivityForResult(changeScreen, 1);

                    }
                });


       }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        countEquipe++;
        qtdPalavras++;
        todasPalavras = data.getStringArrayListExtra("SOBRANDO");
        pontuacao = data.getIntExtra("pontuacao", 0);
        int pontInicial = listaEquipes.get(countEquipe % listaEquipes.size()).getPontuacao();

        listaEquipes.get(countEquipe % listaEquipes.size()).setPontuacao(pontuacao + pontInicial);

            if(countEquipe % listaEquipes.size() == 0){
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

        super.onActivityResult(requestCode, resultCode, data);
    }

    public int achaQtdJogador(){
        int count =0;
        for(int i =0; i < listaEquipes.size();i++){
            count += listaEquipes.get(i).getListaJogador().size();
        }
        return count;
    }
}
