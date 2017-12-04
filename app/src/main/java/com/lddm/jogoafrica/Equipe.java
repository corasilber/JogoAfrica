package com.lddm.jogoafrica;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by coras on 02/12/2017.
 */

public class Equipe implements Serializable{

    private String nome;
    private List<Jogador> listaJogador;

    private Integer pontuacao = 0;


    public Integer getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(Integer pontuacao) {
        this.pontuacao = pontuacao;
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Jogador> getListaJogador() {
        return listaJogador;
    }

    public void setListaJogador(List<Jogador> listaJogador) {
        this.listaJogador = listaJogador;
    }


}


class GameState {
    public int num_jogadores;
    public int connected;
    public ArrayList<String> words;
    public long timestamp;
    public ArrayList<String> nome_equipes;
    public HashMap<String, List<String>> equipes;
    public int words_version;
}