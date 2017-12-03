package com.lddm.jogoafrica;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by coras on 02/12/2017.
 */

public class Jogador implements Serializable{

    public Jogador() {}

    private String nome;

    private String equipe;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEquipe() {
        return equipe;
    }

    public void setEquipe(String equipe) {
        this.equipe = equipe;
    }

    public String[] getPalavras() {
        return palavras;
    }

    public void setPalavras(String[] palavras) {
        this.palavras = palavras;
    }

    private String[] palavras;


}
