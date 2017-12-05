package com.lddm.jogoafrica;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  {

    Button button, conectar, palavras, localizacao;
    public static int session = -1;
    private Handler handler = new Handler();
    private Runnable runnable;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        button = (Button) findViewById(R.id.button);
        conectar = (Button) findViewById(R.id.conectar);
        palavras = (Button) findViewById(R.id.palavras);
        localizacao = (Button) findViewById(R.id.localizacao);

         runnable = new Runnable() {
            @Override
            public void run() {
                Networking.buscarEquipes(MainActivity.this, MainActivity.session);
                handler.postDelayed(this, 500);
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
                builder.setTitle("Informe o código");

                final EditText input = new EditText(MainActivity.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.session = Integer.parseInt(input.getText().toString());
                        handler.postDelayed(runnable, 500);

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

        localizacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    MainActivity.this.mFusedLocationClient.getLastLocation()
                            .addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    // Got last known location. In some rare situations this can be null.
                                    if (location != null) {
                                        MainActivity.this.location = location;
                                    }
                                    showLocationMessage();

                                }
                            });

                } catch (SecurityException e) {
                    // sem permissao
                }

            }
        });
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

    }

    public void setSession(int session) {
        MainActivity.session = session;
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Código de acesso");
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


    @Override
    protected void onStart() {
        super.onStart();
    }

    private void showLocationMessage() {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Sua localização");

        if (location != null) {
            alertDialog.setMessage("Sua longitude é: " + location.getLongitude() + " sua latitude é: " + location.getLatitude());
        } else {
            alertDialog.setMessage("Não foi possível adquirir sua localização, tente mudar seu modo de localização " +
                    "para Alta Precisão antes de abrir o aplicativo. Se você já tiver feito isso, tente abrir o Google Maps para " +
                    "adquirir uma localização");
        }
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (location == null)
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
    }

}
