package br.ufjf.dcc196.rafael.agente008;

import java.util.ArrayList;
import java.util.List;

public class Caso {
    private Integer dia;
    private Integer hora;
    private Integer status;
    private Agente agente;
    private Criminoso criminoso;
    private List<Localizacao> locaisVisitados;
    public final Integer EM_ANDAMENTO=0;
    public final Integer CONCLUIDO=1;
    public final Integer PERDIDO=2;

    public Caso() {
        this.dia =0;
        this.hora =0;
        this.status=EM_ANDAMENTO;
        this.agente=null;
        this.criminoso=new Criminoso();
        this.locaisVisitados=new ArrayList<Localizacao>();
    }

    public Integer getDia() {
        return this.dia;
    }

    public void setDia(Integer dia) {
        this.dia = dia;
    }

    public void incrDia() {
        this.dia++;
    }

    public Integer getHora() {
        return this.hora;
    }

    public void setHora(Integer hora) {
        this.hora = hora;
    }

    public void incrHora(Integer hora) {
        this.hora += hora;
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

    //TODO Implementar randomização
    public void criarCasoAutomatico(Agente agente){
        this.agente=agente;
        this.agente.incrDinheiro(1000.0);
        this.criminoso.setNome("Ronaldo");
        this.criminoso.setCrime("Roubo");

    }
}
