package br.ufjf.dcc196.rafael.agente008;

import java.util.List;

public class Criminoso {
    private String nome, crime;
    private List<Localizacao> locaisVisitados;

    public Criminoso() {
        this.nome="";
        this.crime="";
        this.locaisVisitados=null;
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

    public List<Localizacao> getLocaisVisitados() {
        return this.locaisVisitados;
    }

    public void setLocaisVisitados(List<Localizacao> locaisVisitados) {
        this.locaisVisitados = locaisVisitados;
    }
}
