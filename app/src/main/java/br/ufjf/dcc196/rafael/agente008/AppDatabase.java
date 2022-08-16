package br.ufjf.dcc196.rafael.agente008;
//Classe para instanciação do db
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import br.ufjf.dcc196.rafael.agente008.DAO.LocalizacaoDAO;
import br.ufjf.dcc196.rafael.agente008.entities.Localizacao;

@Database(entities = {Localizacao.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public static final String DB_NAME="jogo-db";
    private static AppDatabase INSTANCE;

    public abstract LocalizacaoDAO localizacaoDAO();

    public static AppDatabase getInstance(Context contexto){

        if(INSTANCE==null){
            INSTANCE= Room.databaseBuilder(
                    contexto.getApplicationContext(),
                    AppDatabase.class,
                    DB_NAME
            ).build();
        }
        return INSTANCE;

    }
}
