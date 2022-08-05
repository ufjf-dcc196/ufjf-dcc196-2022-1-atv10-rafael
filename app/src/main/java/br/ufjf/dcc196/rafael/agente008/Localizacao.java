package br.ufjf.dcc196.rafael.agente008;

public class Localizacao {
    private String regiao, estado, cidade, local;
    Integer populacao;

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
}
