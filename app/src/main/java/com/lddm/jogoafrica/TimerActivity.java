package com.lddm.jogoafrica;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by augusto on 02/12/17.
 */

public class TimerActivity extends AppCompatActivity {
    public static String PALAVRAS = "WORDS";
    public static String PALAVRAS_SOBRANDO = "SOBRANDO";
    public static String NOME_EQUIPE = "EQUIPE";
    public static String NOME_JOGADOR = "JOGADOR";

    private ArrayList<String> palavras;
    private int corretos = -1;
    private Random random = new Random();
    private CountDownTimer timer;

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

//        Intent intent = getIntent();
//        if (intent != null) {
//            palavras = intent.getStringArrayListExtra(PALAVRAS);
//            nomeJogador.setText(intent.getStringExtra(NOME_JOGADOR));
//            nomeEquipe.setText(intent.getStringExtra(NOME_EQUIPE));
//        } else {

            palavras =(ArrayList<String>) getIntent().getSerializableExtra("todasPalavras");
            String equipe = getIntent().getStringExtra("nomeEquipe");
            String jogador = getIntent().getStringExtra("nomeJogador");

            //palavras = new ArrayList<String>();
           // palavras.add("Bolacha");
//            palavras.add("Figo");
//            palavras.add("Moita");
            nomeJogador.setText(jogador);
            nomeEquipe.setText(equipe);
//        }

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

        timer  = new CountDownTimer(1000, 1000) {
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
            encerrarAtividade();

        }
    }

    private void encerrarAtividade() {
        timer.cancel();
        Intent intent = new Intent(TimerActivity.this, EquipeJoga.class);
        intent.putStringArrayListExtra(PALAVRAS_SOBRANDO, palavras);
        intent.putExtra("pontuacao", corretos);
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


























