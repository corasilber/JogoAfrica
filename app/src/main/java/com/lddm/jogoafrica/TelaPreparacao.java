package com.lddm.jogoafrica;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TelaPreparacao extends AppCompatActivity {

    List<Equipe> listaEquipes;
    ArrayList<String> todasPalavras;
    Button jogar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_preparacao);

        listaEquipes = (List<Equipe>) getIntent().getSerializableExtra("listaEquipes");
        todasPalavras = (ArrayList<String>) getIntent().getSerializableExtra("todasPalavras");

        jogar = (Button) findViewById(R.id.button2);

        jogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent changeScreen = new Intent(TelaPreparacao.this, EquipeJoga.class);
                changeScreen.putExtra("listaEquipes", (Serializable) listaEquipes);
                changeScreen.putExtra("todasPalavras", todasPalavras);
                startActivity(changeScreen);
            }
        });


    }
}
