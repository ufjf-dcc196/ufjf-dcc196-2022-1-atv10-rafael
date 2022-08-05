package br.ufjf.dcc196.rafael.agente008;

import androidx.annotation.NonNull;

public class Localizacao {
    private String regiao, estado, cidade, local;
    private Integer populacao;

    public Localizacao() {
        this.regiao="";
        this.estado="";
        this.cidade="";
        this.local="";
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
