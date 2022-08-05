package br.ufjf.dcc196.rafael.agente008;

public class Criminoso {
    private String nome, crime;
    private Localizacao localizacao;

    public Criminoso() {
        this.nome="";
        this.crime="";
        this.localizacao=new Localizacao();
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCrime() {
        return this.crime;
    }

    public void setCrime(String crime) {
        this.crime = crime;
    }

    public Localizacao getLocalizacao() {
        return this.localizacao;
    }

    public void setLocalizacao(Localizacao localizacao) {
        this.localizacao = localizacao;
    }
}
