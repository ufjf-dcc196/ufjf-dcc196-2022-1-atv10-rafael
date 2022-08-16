package br.ufjf.dcc196.rafael.agente008.DAO;
//Implementação dos comandos SQL
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

    @Update
    void save(Localizacao localizacao);

    @Delete
    void delete(Localizacao localizacao);

    @Query("SELECT * from localizacao")
    List<Localizacao> findAll();

    @Query("SELECT * from localizacao WHERE id=:id LIMIT 1")
    Localizacao findById(Long id);

    //Exclui tudo
    @Query("DELETE from localizacao")
    void deleteAll();

    //Contagem de localizações
    @Query("SELECT count(*) from localizacao")
    Integer countAll();

    //Busca locais por estado (Utilizado para popular delegacias...etc)
    @Query("SELECT * FROM localizacao WHERE estado LIKE :estado AND local LIKE :local ORDER BY cidades")
    List<Localizacao> findLocaisByEstado(String estado, String local);

    //Busca locais por estado e cidade (Utilizado para popular delegacias...etc)
    @Query("SELECT * FROM localizacao WHERE estado=:estado AND cidades LIKE :cidades ||'%' AND local LIKE :local ORDER BY cidades")
    List<Localizacao> findLocaisByCidadesAndEstado(String estado, String cidades, String local);

    //Busca locais por cidade, que não sejam delegacia
    @Query("SELECT * FROM localizacao WHERE cidades=:cidades AND local NOT LIKE \"Delegacia\" order by local")
    List<Localizacao> findLocaisbyCidades(String cidades);

    //Busca cidades com aeroporto
    @Query("SELECT * FROM localizacao WHERE populacao>150000 AND estado LIKE :estado AND local LIKE \"Delegacia\"")
    List<Localizacao> findCidadesComAeroportoByEstado(String estado);

    //Busca cidades do estado com aeroporto
    @Query("SELECT * FROM localizacao WHERE populacao>150000 AND estado LIKE :estado AND cidades LIKE :cidade AND local LIKE \"Delegacia\"")
    List<Localizacao> findCidadesComAeroportoByEstadoeCidade(String estado, String cidade);

}
