package br.ufjf.dcc196.rafael.agente008;

import java.util.ArrayList;
import java.util.List;

public class Caso {
    private Double dias;
    private Integer status;
    private Agente agente;
    private Criminoso criminoso;
    private List<Localizacao> locaisVisitados;
    public final Integer EM_ANDAMENTO=0;
    public final Integer CONCLUIDO=1;
    public final Integer PERDIDO=2;

    public Caso() {
        this.dias=0.0;
        this.status=EM_ANDAMENTO;
        this.agente=null;
        this.criminoso=null;
        this.locaisVisitados=new ArrayList<Localizacao>();
    }

    public Double getDias() {
        return this.dias;
    }

    public void setDias(Double dias) {
        this.dias = dias;
    }

    public void incrDias(Double dias) {
        this.dias += dias;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Agente getAgente() {
        return this.agente;
    }

    public void setAgente(Agente agente) {
        this.agente = agente;
    }

    public Criminoso getCriminoso() {
        return this.criminoso;
    }

    public void setCriminoso(Criminoso criminoso) {
        this.criminoso = criminoso;
    }

    public List<Localizacao> getLocaisVisitados() {
        return this.locaisVisitados;
    }

    public void setLocaisVisitados(List<Localizacao> locaisVisitados) {
        this.locaisVisitados = locaisVisitados;
    }
}
