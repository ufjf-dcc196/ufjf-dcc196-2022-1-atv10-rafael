package br.ufjf.dcc196.rafael.agente008.entities;
//Superclasse que representa um indivíduo
import java.util.ArrayList;
import java.util.List;

public abstract class Individuo {
    private String nome;
    private List<Localizacao> locaisVisitados;

    public Individuo(){
        this.nome="";
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
    public Localizacao getLocalizacaoAtual() {
        return this.getLocaisVisitados().get(this.getLocaisVisitados().size() - 1);
    }
}
