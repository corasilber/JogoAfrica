package com.lddm.jogoafrica;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by augusto on 02/12/17.
 */

public class TimerActivity extends AppCompatActivity {
    public static String PALAVRAS = "WORDS";
    public static String PALAVRAS_SOBRANDO = "SOBRANDO";
    public static String NOME_EQUIPE = "EQUIPE";
    public static String NOME_JOGADOR = "JOGADOR";

    private ArrayList<String> palavras, palavrasClone;
    private int corretos = -1, fase;
    private Random random = new Random();
    private CountDownTimer timer;
    private List<Equipe> listaEquipes;


    private TextView nomeJogador;
    private TextView nomeEquipe;
    private TextView palavraTextView;
    private TextView countDownTextView;
    private TextView contadorTextView;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer_activity);
        countDownTextView = (TextView) findViewById(R.id.countDownText);
        nomeJogador = (TextView) findViewById(R.id.jogadorTextView);
        nomeEquipe = (TextView) findViewById(R.id.equipeTextView);
        palavraTextView = (TextView) findViewById(R.id.palavraTextView);
        contadorTextView = (TextView) findViewById(R.id.counterTextView);

        palavras =(ArrayList<String>) getIntent().getSerializableExtra("todasPalavras");
        palavrasClone = (ArrayList<String>) getIntent().getSerializableExtra("todasPalavrasClone");
        listaEquipes =  (List<Equipe>) getIntent().getSerializableExtra("listaEquipes");

        fase = getIntent().getIntExtra("fase", 0);
        String equipe = getIntent().getStringExtra("nomeEquipe");
        String jogador = getIntent().getStringExtra("nomeJogador");
        long timestamp = getIntent().getLongExtra("targetTime", 0);

        nomeJogador.setText(jogador);
        nomeEquipe.setText(equipe);


        mudarPalavra();

        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
                mudarPalavra();
            }
        });

        timer  = new CountDownTimer(timestamp, 100) {
            @Override
            public void onTick(long l) {
                countDownTextView.setText(Long.toString(l / 1000));
            }

            @Override
            public void onFinish() {
                palavras.add(palavraTextView.getText().toString());
               encerrarAtividade();
            }
        };
        timer.start();

    }

    private void mudarPalavra() {
        if (!palavras.isEmpty()) {
            palavraTextView.setText(palavras.remove(random.nextInt(palavras.size())));
            contadorTextView.setText((++corretos) + "");
        } else {
            palavras = palavrasClone;
            corretos++;
            fase++;
            encerrarAtividade();
        }
    }

    private void encerrarAtividade() {
            timer.cancel();
            Intent intent = new Intent(TimerActivity.this, EquipeJoga.class);
            intent.putStringArrayListExtra(PALAVRAS_SOBRANDO, palavras);
            intent.putExtra("pontuacao", corretos);
            intent.putExtra("fase", fase);
            setResult(RESULT_OK, intent);
            finish();

    }
    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }

}


























