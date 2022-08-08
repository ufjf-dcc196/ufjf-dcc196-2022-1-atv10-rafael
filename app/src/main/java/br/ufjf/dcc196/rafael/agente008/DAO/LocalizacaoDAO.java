package br.ufjf.dcc196.rafael.agente008.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.ufjf.dcc196.rafael.agente008.entities.Localizacao;

@Dao
public interface LocalizacaoDAO {

    @Insert
    void insertLocalizacao(Localizacao localizacao);

    @Query("SELECT * from localizacao")
    List<Localizacao> findAll();

    @Query("SELECT * from localizacao WHERE id=:id LIMIT 1")
    Localizacao findById(Long id);

    @Update
    void save(Localizacao localizacao);

    @Delete
    void delete(Localizacao localizacao);

    @Query("DELETE from localizacao")
    void deleteAll();

    @Query("SELECT count(*) from localizacao")
    Integer countAll();

    @Query("SELECT DISTINCT cidades, estado FROM localizacao ORDER BY cidades")
    List<Localizacao> findDistinctCidades();


}
