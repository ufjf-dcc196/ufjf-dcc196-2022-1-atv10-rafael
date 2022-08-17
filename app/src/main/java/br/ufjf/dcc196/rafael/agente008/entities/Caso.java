package br.ufjf.dcc196.rafael.agente008.entities;
//Classe que representa o caso
public class Caso {
    private Integer dia;
    private Integer hora;
    private Integer status;
    private Criminoso criminoso;
    public static final Integer INEXISTENTE=0;
    public static final Integer EM_ANDAMENTO=1;
    public static final Integer CONCLUIDO=2;
    public static final Integer FALIU=3;
    public static final Double PRECO_VISITA=5.0;
    public static final Double PRECO_VIAGEM_INTERMUNICIPAL=100.0;
    public static final Double PRECO_VIAGEM_INTERESTADUAL=150.0;
    public static final Double PRECO_VIAGEM_INTERREGIONAL=180.0;
    public static final Integer HORAS_VISITA=1;
    public static final Integer HORAS_VIAGEM_INTERMUNICIPAL=2;
    public static final Integer HORAS_VIAGEM_INTERESTADUAL=3;
    public static final Integer HORAS_VIAGEM_INTERREGIONAL=4;
    public static final Integer MAX_HORAS_TRABALHADAS_POR_DIA=16;

    public Caso() {
        this.dia =0;
        this.hora =0;
        this.status=INEXISTENTE;
        this.criminoso=new Criminoso();
    }

    //--Getters/Setters
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

    public Criminoso getCriminoso() {
        return this.criminoso;
    }

    public void setCriminoso(Criminoso criminoso) {
        this.criminoso = criminoso;
    }

    //Traduz o status em strings para serem exibidas na activity
    public static String traduzirStatus(Integer status){
        switch (status){
            case 1:
                return "Em andamento";
            case 2:
                return "Concluido";
            case 3:
                return "Perdido";

        }
        return "Inexistente";
    }
    public static String toEmptyJson(){
        return "{\"dia\":0,\"hora\":0,\"status\":0}";
    }
}
