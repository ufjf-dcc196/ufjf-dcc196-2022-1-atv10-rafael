package br.ufjf.dcc196.rafael.agente008.entities;

import java.util.List;
import java.util.Random;

import br.ufjf.dcc196.rafael.agente008.DAO.DatabaseBuilder;

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

    public static Criminoso gerar(Random rand, Agente agente, List<Localizacao> localizacoes){
        Criminoso criminoso=new Criminoso();

        //Randomiza nome e crime
        criminoso.setNome(DatabaseBuilder.getNomes().get(rand.nextInt(DatabaseBuilder.getNomes().size())));
        criminoso.setCrime(DatabaseBuilder.getCrimes().get(rand.nextInt(DatabaseBuilder.getCrimes().size())));

        //Randomiza a rota do criminoso
        criminoso.randRota(rand,agente,localizacoes);

        return criminoso;
    }

    //Randomizando rota do criminoso
    private void randRota(Random rand, Agente agente , List<Localizacao> localizacoes){
        List<Localizacao> rota = getLocaisVisitados();

        //Definindo primeiro local do criminoso
        rota.add(Localizacao.randLocalDaCidade(rand,agente.getBase(), localizacoes));

        Double custo;
        Localizacao localAtual;
        Localizacao localEscolhido=null;
        Double dinheiro=1000.0;
        Boolean continuar=true;

        //Enquanto houver dinheiro, escolha um lugar novo aleatoriamente
        while(continuar){
            localAtual=rota.get(rota.size()-1);

            do {
                localEscolhido=Localizacao.randDestino(rand,localAtual,localizacoes);
            }while (Localizacao.jaVisitado(localEscolhido,rota));

            custo=Localizacao.precificarDeslocamento(localAtual,localEscolhido);

            if(custo>dinheiro){
                continuar=false;
            }else{
                dinheiro-=custo;
                rota.add(localEscolhido);
            }

        }

    }
}
