package br.ufjf.dcc196.rafael.agente008.entities;

public class Caso {
    private Integer dia;
    private Integer hora;
    private Integer status;
    private Agente agente;
    private Criminoso criminoso;
    public static final Integer INEXISTENTE=0;
    public static final Integer EM_ANDAMENTO=1;
    public static final Integer CONCLUIDO=2;
    public static final Integer PERDIDO=3;

    public Caso() {
        this.dia =0;
        this.hora =0;
        this.status=INEXISTENTE;
        this.agente=new Agente();
        this.criminoso=new Criminoso();
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

    //TODO Implementar randomização
    public void criarCasoAutomatico(Agente agente){
        this.agente=agente;
        this.agente.incrDinheiro(1000.0);
        this.criminoso.setNome("Ronaldo");
        this.criminoso.setCrime("Roubo");
        this.status=EM_ANDAMENTO;

    }
}
