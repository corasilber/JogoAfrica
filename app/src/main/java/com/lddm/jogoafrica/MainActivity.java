package com.lddm.jogoafrica;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  {

    Button button, conectar, palavras;
    public static int session = -1;
    private Handler handler = new Handler();
    private Runnable runnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        conectar = (Button) findViewById(R.id.conectar);
        palavras = (Button) findViewById(R.id.palavras);

         runnable = new Runnable() {
            @Override
            public void run() {
                Networking.buscarEquipes(MainActivity.this, MainActivity.session);
                handler.postDelayed(this, 1000);
            }
        };


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Networking.createSession(MainActivity.this);
            }
        });

        conectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Title");

                final EditText input = new EditText(MainActivity.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.session = Integer.parseInt(input.getText().toString());
                        handler.postDelayed(runnable, 3000);

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });

        palavras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent changeScreen = new Intent(MainActivity.this, BancoPalavras.class);
                startActivity(changeScreen);
            }
        });

    }

    public void setSession(int session) {
        MainActivity.session = session;
        Networking.session = session;
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Informe aos jogadores o código: " + MainActivity.session);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent changeScreen = new Intent(MainActivity.this, MontarEquipe.class);
                        startActivity(changeScreen);
                    }
                });
        alertDialog.show();
    }


    public void getTeams(ArrayList<String> equipes, int numJogadores) {
        if (!equipes.isEmpty()) {
            handler.removeCallbacks(runnable);
            Intent changeScreen = new Intent(MainActivity.this, AdicionarJogador.class);
            changeScreen.putExtra("nomeEquipes", equipes);
            changeScreen.putExtra("qtdJogador", "" +numJogadores);

            startActivity(changeScreen);
        }
    }

    public void checarEquipes() {

    }
}
