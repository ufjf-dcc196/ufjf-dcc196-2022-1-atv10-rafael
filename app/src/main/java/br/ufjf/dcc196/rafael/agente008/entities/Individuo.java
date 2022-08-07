package br.ufjf.dcc196.rafael.agente008.entities;

import java.util.ArrayList;
import java.util.List;

public abstract class Individuo {
    private String nome;
    private List<Localizacao> locaisVisitados;

    public Individuo(){
        this.nome="--";
        this.locaisVisitados=new ArrayList<Localizacao>();
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Localizacao> getLocaisVisitados() {
        return this.locaisVisitados;
    }

    public void setLocaisVisitados(List<Localizacao> locaisVisitados) {
        this.locaisVisitados = locaisVisitados;
    }

}
