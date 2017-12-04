package com.lddm.jogoafrica;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by augusto on 03/12/17.
 */

interface GetTeams {
    void getTeammates(EquipeJson equipeJson);
//    void areWordsComplete(boolean isComplete);
//    void startTimer(int response, int timestamp);
}

public class Networking {
//    private static String endpoint = "http://10.0.2.2:5000/";
    private static String endpoint = "http://augusto2112.pythonanywhere.com/";

    public static void createSession(final MainActivity c) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL endpointURL = new URL(endpoint + "create_session/");
                    HttpURLConnection con = (HttpURLConnection) endpointURL.openConnection();
                    if (con.getResponseCode() == 200) {
                        InputStream responseBody = con.getInputStream();
                        BufferedReader r = new BufferedReader(new InputStreamReader(responseBody));
                        final String line = r.readLine();
                        Handler mainHandler = new Handler(((AppCompatActivity) c).getMainLooper());
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                c.setSession(Integer.parseInt(line));
                            }
                        });
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public static void enviarEquipes(final ArrayList<String> equipes, final int session, final String numJogadores) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL endpointURL = new URL(endpoint + "send_teams?session="+session+"&num_jogadores=" + numJogadores);
                    HttpURLConnection con = (HttpURLConnection) endpointURL.openConnection();
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Content-Type", "application/json");
                    String data = new Gson().toJson(equipes);
                    con.setDoOutput(true);
                    con.getOutputStream().write(data.getBytes());
                    if (con.getResponseCode() == 201) {
                        // Success
                        // Further processing here

                    } else {
                        // Error handling code goes here
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public static void buscarEquipes(final MainActivity c, final int session) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL endpointURL = new URL(endpoint + "get_teams/" + session);
                    HttpURLConnection con = (HttpURLConnection) endpointURL.openConnection();
                    if (con.getResponseCode() == 200) {
                        InputStream responseBody = con.getInputStream();
                        BufferedReader r = new BufferedReader(new InputStreamReader(responseBody));
                        StringBuilder everything = new StringBuilder();
                        String line;
                        while( (line = r.readLine()) != null) {
                            everything.append(line);
                        }

                        final EquipeJson gameObject = new Gson().fromJson(everything.toString(), EquipeJson.class);
                        final ArrayList<String> arrayList = gameObject.nome_equipes;
                        final int numJogadores = gameObject.num_jogadores;

                        Handler mainHandler = new Handler(((AppCompatActivity) c).getMainLooper());
                        final ArrayList<String> finalArrayList = arrayList;
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                c.getTeams(finalArrayList, numJogadores);
                            }
                        });
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void enviarJogadores(final List<Jogador> jogadores, final int session) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL endpointURL = new URL(endpoint + "send_team_members/" + session);
                    HttpURLConnection con = (HttpURLConnection) endpointURL.openConnection();
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Content-Type", "application/json");
                    String data = new Gson().toJson(jogadores);
                    con.setDoOutput(true);
                    con.getOutputStream().write(data.getBytes());
                    if (con.getResponseCode() == 201) {
                        // Success
                        // Further processing here

                    } else {
                        // Error handling code goes here
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public static void buscarJogadores(final GetTeams c, final int session) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL endpointURL = new URL(endpoint + "get_team_members/" + session);
                    HttpURLConnection con = (HttpURLConnection) endpointURL.openConnection();
                    int i = con.getResponseCode();
                    if (con.getResponseCode() == 201) {
                        InputStream responseBody = con.getInputStream();
                        BufferedReader r = new BufferedReader(new InputStreamReader(responseBody));
                        StringBuilder everything = new StringBuilder();
                        String line;
                        while( (line = r.readLine()) != null) {
                            everything.append(line);
                        }
                        final EquipeJson teams = new Gson().fromJson(everything.toString(), EquipeJson.class);


                        Handler mainHandler = new Handler(((AppCompatActivity) c).getMainLooper());
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                c.getTeammates(teams);
                            }
                        });
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void startTimer(final EquipeJoga tp, final int session) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL endpointURL = new URL(endpoint + "start_timer/" + session);
                    HttpURLConnection con = (HttpURLConnection) endpointURL.openConnection();
                    con.setRequestMethod("GET");
                    if (con.getResponseCode() == 200) {
//                        InputStream responseBody = con.getInputStream();
//                        BufferedReader r = new BufferedReader(new InputStreamReader(responseBody));
//                        final String timestamp = r.readLine();
//                        Handler mainHandler = new Handler(((AppCompatActivity) tp).getMainLooper());
//                        mainHandler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                tp.startGame(Long.parseLong(timestamp));
//                            }
//                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public static void getTimer(final EquipeJoga tp, final int session) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL endpointURL = new URL(endpoint + "get_timer/" + session);
                    HttpURLConnection con = (HttpURLConnection) endpointURL.openConnection();
                    con.setRequestMethod("GET");
                    if (con.getResponseCode() == 200) {
                        InputStream responseBody = con.getInputStream();
                        BufferedReader r = new BufferedReader(new InputStreamReader(responseBody));
                        final long timestamp = Long.parseLong(r.readLine());
                        final long adjestedTimestamp = timestamp != 0 ? 60 * 1000 - timestamp : 0;
                        Handler mainHandler = new Handler(((AppCompatActivity) tp).getMainLooper());
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                tp.startGame(adjestedTimestamp);
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

    }

}
