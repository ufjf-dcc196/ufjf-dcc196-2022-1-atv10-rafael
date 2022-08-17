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
    @Query("SELECT * FROM localizacao WHERE estado LIKE :estado AND local LIKE :local ORDER BY cidade")
    List<Localizacao> findLocaisByEstado(String estado, String local);

    //Busca locais por estado e cidade (Utilizado para popular delegacias...etc)
    @Query("SELECT * FROM localizacao WHERE estado=:estado AND cidade LIKE :cidade ||'%' AND local LIKE :local ORDER BY cidade")
    List<Localizacao> findLocaisByCidadeAndEstadoAndLocal(String estado, String cidade, String local);

    //Busca locais por cidade, que não sejam delegacia
    @Query("SELECT * FROM localizacao WHERE cidade=:cidade AND estado LIKE :estado AND local NOT LIKE \"Delegacia\" order by local")
    List<Localizacao> findLocaisByCidadeAndEstado(String estado,String cidade);

    //Busca cidades com aeroporto
    @Query("SELECT * FROM localizacao WHERE populacao>150000 AND estado LIKE :estado AND local LIKE \"Delegacia\"")
    List<Localizacao> findCidadeComAeroportoByEstado(String estado);

    //Busca cidades do estado com aeroporto
    @Query("SELECT * FROM localizacao WHERE populacao>150000 AND estado LIKE :estado AND cidade LIKE :cidade ||'%' AND local LIKE \"Delegacia\"")
    List<Localizacao> findCidadesComAeroportoByEstadoAndCidade(String estado, String cidade);

}
