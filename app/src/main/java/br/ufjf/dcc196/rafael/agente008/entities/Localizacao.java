package br.ufjf.dcc196.rafael.agente008.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "localizacao")
public class Localizacao {

    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name="regiao")
    private String regiao;
    @ColumnInfo(name="estado")
    private String estado;
    @ColumnInfo(name="cidades")
    private String cidade;
    @ColumnInfo(name="local")
    private String local;
    @ColumnInfo(name="populacao")
    private Integer populacao;

    public Localizacao() {
        this.regiao="--";
        this.estado="--";
        this.cidade="--";
        this.local="--";
        this.populacao=0;
    }

    public String getRegiao() {
        return this.regiao;
    }

    public void setRegiao(String regiao) {
        this.regiao = regiao;
    }

    public String getEstado() {
        return this.estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCidade() {
        return this.cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getLocal() {
        return this.local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public Integer getPopulacao() {return this.populacao;}

    public void setPopulacao(Integer populacao) {this.populacao = populacao;}

    public long getId() {return id;}

    public void setId(long id) {this.id = id;}

    public Localizacao clone(){
        Localizacao clone = new Localizacao();
        clone.regiao=this.regiao;
        clone.estado=this.estado;
        clone.cidade=this.cidade;
        clone.local=this.local;
        clone.populacao=this.populacao;
        return clone;
    }

    @Override
    @NonNull
    public String toString(){
        return this.local + " de " + this.cidade + "/"+this.estado;
    }
}
