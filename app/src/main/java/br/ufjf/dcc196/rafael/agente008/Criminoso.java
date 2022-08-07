package br.ufjf.dcc196.rafael.agente008;

import java.util.List;

public class Criminoso extends Individuo {
    private String crime;
    private List<Localizacao> locaisVisitados;

    public Criminoso() {
        super();
        this.crime="";
    }

    public String getCrime() {
        return this.crime;
    }

    public void setCrime(String crime) {
        this.crime = crime;
    }

}
