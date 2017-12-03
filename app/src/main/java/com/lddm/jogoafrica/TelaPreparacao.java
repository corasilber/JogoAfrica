package com.lddm.jogoafrica;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class TelaPreparacao extends AppCompatActivity {

    List<Equipe> listaEquipes;
    ArrayList<String> todasPalavras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_preparacao);

        listaEquipes = (List<Equipe>) getIntent().getSerializableExtra("listaEquipes");
        todasPalavras = (ArrayList<String>) getIntent().getSerializableExtra("todasPalavras");



    }
}
