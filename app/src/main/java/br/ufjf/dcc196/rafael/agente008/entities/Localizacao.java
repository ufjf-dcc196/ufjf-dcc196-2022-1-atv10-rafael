package br.ufjf.dcc196.rafael.agente008.entities;
//Classe da entidade Localização
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Entity(tableName = "localizacao")
public class Localizacao {

    @PrimaryKey
    private Integer id;
    @ColumnInfo(name="regiao")
    private String regiao;
    @ColumnInfo(name="estado")
    private String estado;
    @ColumnInfo(name="cidade")
    private String cidade;
    @ColumnInfo(name="local")
    private String local;
    @ColumnInfo(name="populacao")
    private Integer populacao;
    @Ignore
    private String dica;

    public Localizacao() {
        this.id=0;
        this.regiao="";
        this.estado="";
        this.cidade="";
        this.local="";
        this.populacao=0;
        this.dica ="";
    }

    //Getters/Setters
    public String getRegiao() {
        return this.regiao;
    }

    public void setRegiao(String regiao) {
        this.regiao = regiao;
    }

    public String getEstado() {
        return this.estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCidade() {
        return this.cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getLocal() {
        return this.local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public Integer getPopulacao() {return this.populacao;}

    public void setPopulacao(Integer populacao) {this.populacao = populacao;}

    public Integer getId() {return id;}

    public void setId(Integer id) {this.id = id;}

    public String getDica() {
        return this.dica;
    }

    public void setDica(String dica) {
        this.dica = dica;
    }

    //Retorna uma cópia de this
    public Localizacao clone(){
        Localizacao clone = new Localizacao();
        clone.regiao=this.regiao;
        clone.estado=this.estado;
        clone.cidade=this.cidade;
        clone.local=this.local;
        clone.populacao=this.populacao;
        return clone;
    }


    //Randomiza um dos locais da cidade (Usado pra geração da rota do criminoso) //TODO Corrigir Rodoviaria para Rodoviária, assim como no db
    public static Localizacao randLocalDaCidade(Random rand, Localizacao localAtual , List<Localizacao> rota, List<Localizacao> localizacoes){
        List<Localizacao> locaisCidadeAtual=new ArrayList<Localizacao>();

        for(Localizacao l: localizacoes){
            if(l.cidade.equals(localAtual.cidade) && (!jaVisitado(l,rota) || l.local.equals("Rodoviária")|| l.local.equals("Aeroporto")) && !l.local.equals("Delegacia")){
                locaisCidadeAtual.add(l);
            }
        }

        return locaisCidadeAtual.get(rand.nextInt(locaisCidadeAtual.size()));
    }

    //Definição aleatória do destido de um criminoso //TODO Corrigir Rodoviaria para Rodoviária, assim como no db
    public static Localizacao randDestino(Random rand, List<Localizacao> rota, List<Localizacao> localizacoes){
        Localizacao localAtual=rota.get(rota.size()-1);
        if(localAtual.getLocal().equals("Rodoviária")){
            return randLocaisRegiao(rand,localizacoes,localAtual);
        }else if(localAtual.getLocal().equals("Aeroporto")){
            return randLocaisDeCidadeComAeroporto(rand,localizacoes,localAtual);
        }
        return randLocalDaCidade(rand,localAtual, rota,localizacoes);

    }

    //Randomiza uma rodoviaria da mesma regiao que se encontra o criminoso
    private static Localizacao randLocaisRegiao(Random rand, List<Localizacao> localizacoes, Localizacao origem){
        List<Localizacao> locaisRegiao = getLocaisRegiao(localizacoes,origem);
        return locaisRegiao.get(rand.nextInt(locaisRegiao.size()));
    }

    //Randomiza um aeroporto
    private static Localizacao randLocaisDeCidadeComAeroporto(Random rand, List<Localizacao> localizacoes, Localizacao origem){
        List<Localizacao> cidadesComAeroportos = getCidadesComAeroportos(localizacoes,origem);
        return cidadesComAeroportos.get(rand.nextInt(cidadesComAeroportos.size()));
    }

    //Retorna todos os aeroportos da mesma regiao
    private static List<Localizacao> getCidadesComAeroportos(List<Localizacao> localizacoes, Localizacao origem){
        List<Localizacao> cidadesComAeroportos=new ArrayList<Localizacao>();
        for(Localizacao l: localizacoes){
            if(!l.local.equals("Aeroporto") && l.getPopulacao()>150000 && !l.cidade.equals(origem.cidade)){
                cidadesComAeroportos.add(l);
            }
        }
        return cidadesComAeroportos;
    }

    //Retorna todas as rodoviarias da mesma regiao
    private static List<Localizacao> getLocaisRegiao(List<Localizacao> localizacoes, Localizacao origem){
        List<Localizacao> locaisRegiao=new ArrayList<Localizacao>();
        for(Localizacao l: localizacoes){
            if(!l.local.equals("Rodoviária") && l.regiao.equals(origem.regiao) && !l.cidade.equals(origem.cidade)){
                locaisRegiao.add(l);
            }
        }
        return locaisRegiao;
    }

    //Verifica se este local ja foi visitado
    public static Boolean jaVisitado(Localizacao local, List<Localizacao> rota){

        for(Localizacao l:rota){
            if (l.equals(local)){
                return true;
            }
        }
        return false;
    }

    //Precifica o custo da viagem entre dois locais //TODO modificar o custo se de avião/ônibus
    public static Double precificarDeslocamento(Localizacao a, Localizacao b){

        if(!a.regiao.equals(b.regiao)){
            return Caso.PRECO_VIAGEM_INTERREGIONAL;
        }else if(!a.estado.equals(b.estado)){
            return Caso.PRECO_VIAGEM_INTERESTADUAL;
        }else if(!a.cidade.equals(b.cidade)){
            return Caso.PRECO_VIAGEM_INTERMUNICIPAL;
        }else{
            return Caso.PRECO_VISITA;
        }
    }

    //Retorna o tempo de viagem entre dois locais //TODO modificar o tempo se de avião/ônibus
    public static Integer quantificarTempo(Localizacao a, Localizacao b){

        if(!a.regiao.equals(b.regiao)){
            return Caso.HORAS_VIAGEM_INTERREGIONAL;
        }else if(!a.estado.equals(b.estado)){
            return Caso.HORAS_VIAGEM_INTERESTADUAL;
        }else if(!a.cidade.equals(b.cidade)){
            return Caso.HORAS_VIAGEM_INTERMUNICIPAL;
        }else{
            return Caso.HORAS_VISITA;
        }
    }


    @Override
    @NonNull
    public String toString() {
        if (this.equals(new Localizacao())) {
            return "Sem local";
        } else {
            return this.local + " de " + this.cidade + "/" + this.estado;
        }
    }

    @Override
    public boolean equals(Object o){
        Localizacao l=(Localizacao) o;
        return (l.getEstado().equals(this.estado) &&
                l.getCidade().equals(this.cidade) &&
                l.getLocal().equals(this.local)&&
                l.getPopulacao().equals(this.populacao));
    }
}
