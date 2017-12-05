package com.lddm.jogoafrica;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
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

public class TimerActivity extends AppCompatActivity implements GameStateInterface {
    public static String PALAVRAS = "WORDS";
    public static String PALAVRAS_SOBRANDO = "SOBRANDO";
    public static String NOME_EQUIPE = "EQUIPE";
    public static String NOME_JOGADOR = "JOGADOR";

    private ArrayList<String> palavras, palavrasClone;
    private int corretos = -1, fase;
    private Random random = new Random();
    private CountDownTimer timer;
    private List<Equipe> listaEquipes;
    private boolean comecouJogo;
    private int versaoPalavras;

    private TextView nomeJogador;
    private TextView nomeEquipe;
    private TextView palavraTextView;
    private TextView countDownTextView;
    private TextView contadorTextView;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Networking.getGameState(TimerActivity.this, MainActivity.session);
            handler.postDelayed(this, 1000);
        }
    };


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
        Intent intent = getIntent();
        comecouJogo = getIntent().getBooleanExtra("comecouJogo", false);

        fase = getIntent().getIntExtra("fase", 0);
        String equipe = getIntent().getStringExtra("nomeEquipe");
        String jogador = getIntent().getStringExtra("nomeJogador");
        long timestamp = getIntent().getLongExtra("targetTime", 0);
        versaoPalavras = getIntent().getIntExtra("versaoPalavras", 0);
        nomeJogador.setText(jogador);
        nomeEquipe.setText(equipe);


        mudarPalavra();

        if (comecouJogo) {
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
        }
        timer  = new CountDownTimer(timestamp, 100) {
            @Override
            public void onTick(long l) {
                countDownTextView.setText(Long.toString(l / 1000));
            }

            @Override
            public void onFinish() {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(3000);
                palavras.add(palavraTextView.getText().toString());
               encerrarAtividade();
            }
        };
        timer.start();
        if (!comecouJogo)
            handler.postDelayed(runnable, 1000);
    }

    private void mudarPalavra() {
        if (!palavras.isEmpty() ) {
            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(500);
            if(comecouJogo) {
                palavraTextView.setText(palavras.remove(random.nextInt(palavras.size())));
                contadorTextView.setText((++corretos) + "");
            } else {
                palavraTextView.setText("");
            }
        } else {
            corretos++;
            fase++;
            encerrarAtividade();

        }
    }

    private void encerrarAtividade() {
            timer.cancel();
            handler.removeCallbacks(runnable);
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
        if (comecouJogo)
            mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        if (comecouJogo)
            mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }

    public void getGameState(GameState gameState) {
        if (gameState.words_version > versaoPalavras) {
            encerrarAtividade();
        }
    }

}


























