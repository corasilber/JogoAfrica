package com.lddm.jogoafrica.data;

import android.provider.BaseColumns;

/**
 * Created by coras on 01/12/2017.
 */

public class JogoContract {


    private JogoContract(){}

    /**
     *  Criei uma tabela Equipe que irá conter o nome das equipes
     *  Criei uma tabela Jogador que irá conter o nome do jogador e qual equipe ele pertence (usando FK)
     */


    public static abstract class EquipeEntry implements BaseColumns {

        public static final String TABLE_NAME ="equipe";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_EQUIPE_NAME ="nome";

    }

    public static final class JogadoresEntry implements BaseColumns {
        public static final String TABLE_NAME = "jogadores";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_JOGADOR_NAME = "nome";
        public static final String COLUMN_FK_EQUIPE_ID = "equipeID";
    }



}
