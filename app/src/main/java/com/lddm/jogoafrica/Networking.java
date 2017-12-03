package com.lddm.jogoafrica;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by augusto on 03/12/17.
 */

interface TeamAPI {
    void setSession(int session);
    void getTeams(ArrayList<String> equipes);

}

interface GetTeams {
//    void getWords(ArrayList<String> palavras);
//    void areWordsComplete(boolean isComplete);
//    void startTimer(int response, int timestamp);
}

public class Networking {
//    private static String endpoint = "http://10.0.2.2:5000/";
    private static String endpoint = "http://augusto2112.pythonanywhere.com/";

    public static void createSession(final TeamAPI c) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL endpointURL = new URL(endpoint + "create_session");
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

    public static void enviarEquipes(final ArrayList<String> equipes, final int session) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL endpointURL = new URL(endpoint + "send_teams/" + session);
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

    public static void buscarEquipes(final TeamAPI c, final int session) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL endpointURL = new URL(endpoint + "get_teams/" + session);
                    HttpURLConnection con = (HttpURLConnection) endpointURL.openConnection();
                    int i = con.getResponseCode();
                    if (con.getResponseCode() == 200) {
                        InputStream responseBody = con.getInputStream();
                        BufferedReader r = new BufferedReader(new InputStreamReader(responseBody));
                        StringBuilder everything = new StringBuilder();
                        String line;
                        while( (line = r.readLine()) != null) {
                            everything.append(line);
                        }
                        String[] teams = new Gson().fromJson(everything.toString(), String[].class);
                        final ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(teams));
                        i++;
                        Handler mainHandler = new Handler(((AppCompatActivity) c).getMainLooper());
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                c.getTeams(arrayList);
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
}
