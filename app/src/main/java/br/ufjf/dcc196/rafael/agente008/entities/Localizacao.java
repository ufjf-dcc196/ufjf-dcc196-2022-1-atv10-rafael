package br.ufjf.dcc196.rafael.agente008.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
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
    @ColumnInfo(name="cidades")
    private String cidade;
    @ColumnInfo(name="local")
    private String local;
    @ColumnInfo(name="populacao")
    private Integer populacao;

    public Localizacao() {
        this.id=0;
        this.regiao="--";
        this.estado="--";
        this.cidade="--";
        this.local="--";
        this.populacao=0;
    }

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

    public Localizacao clone(){
        Localizacao clone = new Localizacao();
        clone.regiao=this.regiao;
        clone.estado=this.estado;
        clone.cidade=this.cidade;
        clone.local=this.local;
        clone.populacao=this.populacao;
        return clone;
    }


    //Randomiza um dos locais da cidade (Usado pra geração do criminoso)
    public static Localizacao randLocalDaCidade(Random rand, Localizacao localAtual, List<Localizacao> localizacoes){
//TODO ta dando pau aqui
        List<Localizacao> locaisCidadeAtual=new ArrayList<Localizacao>();

        for(Localizacao l: localizacoes){
            if(l.cidade.equals(localAtual.cidade)){
                locaisCidadeAtual.add(l);
            }
        }

        return locaisCidadeAtual.get(rand.nextInt(locaisCidadeAtual.size()));
    }

    public static Localizacao randDestino(Random rand, Localizacao localAtual, List<Localizacao> localizacoes){

        if(localAtual.getLocal().equals("Rodoviária") || localAtual.getLocal().equals("Aeroporto")){
            return randViagem(rand,localAtual,localizacoes);
        }
        return randLocalDaCidade(rand,localAtual,localizacoes);

    }

    //Randomiza um local de Destino
    public static Localizacao randViagem(Random rand, Localizacao localAtual, List<Localizacao> localizacoes){

        Localizacao destino;

        if(localAtual.local.equals("Aeroporto")){
            do{
                destino=localizacoes.get(rand.nextInt(localizacoes.size()));
            }while(destino.populacao<500000 && !destino.equals(localAtual));

            for(Localizacao l: localizacoes){
                if(l.cidade.equals(destino.cidade) &&
                        l.estado.equals(destino.estado) && l.local.equals("Aeroporto")){
                    return l;
                }
            }

        }else{

            do{
                destino=localizacoes.get(rand.nextInt(localizacoes.size()));
            }while(destino.regiao!=localAtual.regiao && !destino.equals(localAtual));

            for(Localizacao l: localizacoes){
                if(l.cidade.equals(destino.cidade) &&
                        l.estado.equals(destino.estado) && l.local.equals("Rodoviária")){
                    return l;
                }
            }
        }

        return destino;

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

    public static double precificarDeslocamento(Localizacao a, Localizacao b){

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

    public Localizacao copy(){
        Localizacao localizacao=new Localizacao();
        localizacao.id=this.id;
        localizacao.regiao=this.regiao;
        localizacao.estado=this.estado;
        localizacao.cidade=this.cidade;
        localizacao.populacao=this.populacao;
        localizacao.local=this.local;
        return localizacao;
    }


    @Override
    @NonNull
    public String toString(){
        return this.local + " de " + this.cidade + "/"+this.estado;
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
