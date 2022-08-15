package br.ufjf.dcc196.rafael.agente008.entities;

public class Agente extends Individuo {

    private Double dinheiro;
    private Integer casosConcluidos;
    private Localizacao base;
    private Boolean existe;

    public Agente() {
        super();
        this.dinheiro=0.0;
        this.casosConcluidos=0;
        this.base=new Localizacao();
        this.existe=false;
    }

    public Double getDinheiro() {
        return this.dinheiro;
    }

    public void setDinheiro(Double dinheiro) {
        this.dinheiro = dinheiro;
    }

    public void incrDinheiro(Double dinheiro) {
        this.dinheiro += dinheiro;
    }

    public void decrDinheiro(Double dinheiro) {
        this.dinheiro -= dinheiro;
    }

    public Integer getCasosConcluidos() {
        return this.casosConcluidos;
    }

    public void setCasosConcluidos(Integer casosConcluidos) {
        this.casosConcluidos = casosConcluidos;
    }

    public void incrCasosConcluidos() {
        this.casosConcluidos++;
    }

    public Localizacao getBase() {
        return this.base;
    }

    public void setBase(Localizacao base) {
        this.base = base;
    }

    public void setExiste(Boolean existe){
        this.existe=existe;
    }

    public Boolean existe(){
        return this.existe;
    }

    public Localizacao getLocalizacaoAtual(){
        return this.getLocaisVisitados().get(this.getLocaisVisitados().size()-1);
    }
}
