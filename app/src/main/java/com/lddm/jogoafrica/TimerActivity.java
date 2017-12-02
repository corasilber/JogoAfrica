package com.lddm.jogoafrica;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by augusto on 02/12/17.
 */

public class TimerActivity extends AppCompatActivity {
    public static String PALAVRAS = "WORDS";
    public static String NOME_EQUIPE = "EQUIPE";
    public static String NOME_JOGADOR = "JOGADOR";

    private ArrayList<String> palavras;
    private TextView nomeJogador;
    private TextView nomeEquipe;
    private TextView palavraTextView;
    private TextView countDownTextView;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer_activity);
        countDownTextView = (TextView) findViewById(R.id.countDownText);
        nomeJogador = (TextView) findViewById(R.id.countDownText);
        nomeEquipe = (TextView) findViewById(R.id.countDownText);
        palavraTextView = (TextView) findViewById(R.id.countDownText);

//        Intent intent = getIntent();
//        if (intent != null) {
//            palavras = intent.getStringArrayListExtra(PALAVRAS);
//            nomeJogador.setText(intent.getStringExtra(NOME_JOGADOR));
//            nomeEquipe.setText(intent.getStringExtra(NOME_EQUIPE));
//        } else {
            palavras = new ArrayList<String>();
            palavras.add("Bolacha");
            palavras.add("Figo");
            palavras.add("Moita");
            nomeJogador.setText("Augusto");
            nomeEquipe.setText("Afrimaster");
//        }

        palavraTextView.setText("60");
        new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long l) {
                countDownTextView.setText(Long.toString(l / 1000));
            }

            @Override
            public void onFinish() {
                countDownTextView.setText("done!");
            }
        }.start();

    }
}


























