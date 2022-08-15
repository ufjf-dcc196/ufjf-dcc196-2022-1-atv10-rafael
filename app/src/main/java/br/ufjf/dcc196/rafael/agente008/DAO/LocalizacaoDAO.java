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

    @Query("SELECT DISTINCT cidades, estado FROM localizacao order by estado")
    List<Localizacao> findDistinctCidades();

    @Query("SELECT * FROM localizacao WHERE estado LIKE :estado AND local LIKE :local ORDER BY cidades")
    List<Localizacao> findDistinctCidadesByEstado(String estado,String local);

    @Query("SELECT * FROM localizacao WHERE cidades LIKE :cidades ||'%' AND local LIKE :local ORDER BY cidades")
    List<Localizacao> findDistinctCidadesByCidade(String cidades,String local);

    @Query("SELECT * FROM localizacao WHERE estado=:estado AND cidades LIKE :cidades ||'%' AND local LIKE :local ORDER BY cidades")
    List<Localizacao> findDistinctCidadesByEstadoAndCidade(String estado, String cidades,String local);

    @Query("SELECT * FROM localizacao WHERE cidades=:cidades AND local NOT LIKE \"Delegacia\"")
    List<Localizacao> findLocaisbyCidades(String cidades);

    @Query("SELECT * FROM localizacao WHERE populacao>150000 AND estado LIKE :estado AND local LIKE \"Delegacia\"")
    List<Localizacao> findCidadesComAeroportoByEstado(String estado);

    @Query("SELECT * FROM localizacao WHERE populacao>150000 AND estado LIKE :estado AND cidades LIKE :cidade AND local LIKE \"Delegacia\"")
    List<Localizacao> findCidadesComAeroportoByEstadoeCidade(String estado, String cidade);

}
