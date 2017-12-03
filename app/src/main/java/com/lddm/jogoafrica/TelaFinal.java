package com.lddm.jogoafrica;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TelaFinal extends AppCompatActivity {

    List<Equipe> listaEquipes;
    TextView vencedor;
    ListView resul;
    ArrayAdapter adapter;
    ArrayList<String> listaResultados;
    Button voltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_final);

        listaEquipes = (List<Equipe>) getIntent().getSerializableExtra("listaEquipes");
        vencedor = (TextView) findViewById(R.id.vencedor);
        resul = (ListView) findViewById(R.id.resultados);
        voltar = (Button) findViewById(R.id.voltar);

        listaResultados = new ArrayList<>();
        //ordenaPontuacoes();
        String teste = listaEquipes.get(0).getNome();

        setaListaResultados();

        adapter = new ArrayAdapter<>(this, R.layout.item_lista, R.id.listaView, listaResultados);
        ListView list = (ListView) findViewById(R.id.resultados);
        list.setAdapter(adapter);

        String vencedorEh = achaEquipeVencedora();
        vencedor.setText(vencedorEh);

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent changeScreen = new Intent(TelaFinal.this, MainActivity.class);
                startActivity(changeScreen);
            }
        });

    }

    public String achaEquipeVencedora(){

        String nomeVencedora = listaEquipes.get(0).getNome();
        int pont = listaEquipes.get(0).getPontuacao();

        for(int i = 1; i < listaEquipes.size(); i++){
            if(listaEquipes.get(i).getPontuacao() > pont ){
                nomeVencedora = listaEquipes.get(i).getNome();
                pont = listaEquipes.get(i).getPontuacao();
            }
        }
        return nomeVencedora;

    }

    public void ordenaPontuacoes() {
       // List<Equipe> ordenada;

        Collections.sort(listaEquipes, new Comparator<Equipe>() {
            public int compare(Equipe e1, Equipe e2) {
                return e1.getPontuacao().compareTo(e2.getPontuacao());
            }
        });

    }

    public void setaListaResultados(){
        for(int i = 0; i < listaEquipes.size(); i++){
            String aux = listaEquipes.get(i).getNome() + " -- " + listaEquipes.get(i).getPontuacao() + " ponto(s)";
            listaResultados.add(aux);
        }
    }

}
