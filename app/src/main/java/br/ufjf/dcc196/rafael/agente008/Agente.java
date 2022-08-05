package br.ufjf.dcc196.rafael.agente008;

public class Agente {
    private String nome;
    private Double dinheiro;
    private Integer casosConcluidos;
    private Localizacao localizacao, base;

    public Agente() {
        this.nome="";
        this.dinheiro=0.0;
        this.casosConcluidos=0;
        this.localizacao=new Localizacao();
        this.base=new Localizacao();
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public Localizacao getLocalizacao() {
        return this.localizacao;
    }

    public void setLocalizacao(Localizacao localizacao) {
        this.localizacao = localizacao;
    }

    public Localizacao getBase() {
        return this.base;
    }

    public void setBase(Localizacao base) {
        this.base = base;
    }
}
