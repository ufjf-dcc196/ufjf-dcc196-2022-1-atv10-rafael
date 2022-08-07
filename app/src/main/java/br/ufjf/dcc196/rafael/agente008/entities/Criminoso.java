package br.ufjf.dcc196.rafael.agente008.entities;

public class Criminoso extends Individuo {
    private String crime;

    public Criminoso() {
        super();
        this.crime="--";
    }

    public String getCrime() {
        return this.crime;
    }

    public void setCrime(String crime) {
        this.crime = crime;
    }

}
