package br.ufjf.dcc196.rafael.agente008;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.ufjf.dcc196.rafael.agente008.entities.Agente;
import br.ufjf.dcc196.rafael.agente008.entities.Caso;
import br.ufjf.dcc196.rafael.agente008.entities.Criminoso;
import br.ufjf.dcc196.rafael.agente008.entities.Individuo;
import br.ufjf.dcc196.rafael.agente008.entities.Localizacao;

public class JogoRepository {
    private Context contexto;
    private SharedPreferences preferences;
    private final String PREFERENCES_NAME = "br.ufjf.dcc196.rafaelfreesz.agente008";
    private final String CASO_KEY = "CA";
    private final String AGENTE_KEY = "A";
    private final String CRIMINOSO_KEY = "CR";

    public JogoRepository(@NonNull Context context){
        this.contexto=context;
        this.preferences= context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public void setCaso(@NonNull Caso caso){

        try {
            JSONObject jsonObj = new JSONObject();

            jsonObj.put("dia", caso.getDia());
            jsonObj.put("hora", caso.getHora());
            jsonObj.put("status", caso.getStatus());

            setCriminoso(caso.getCriminoso());
            setAgente(caso.getAgente());

            SharedPreferences.Editor editor = this.preferences.edit();
            editor.putString(CASO_KEY ,jsonObj.toString());
            editor.apply();

        }catch (JSONException e){
            e.printStackTrace();
        }

    }

    public Caso getCaso(){
        try {

            Caso caso=new Caso();
            //TODO erro aqui, verificar
            JSONObject jsonObj = new JSONObject(this.preferences.getString(CASO_KEY,"{}"));

            caso.setDia(jsonObj.getInt("dia"));
            caso.setHora(jsonObj.getInt("hora"));
            caso.setStatus(jsonObj.getInt("status"));
            caso.setAgente(getAgente());
            caso.setCriminoso(getCriminoso());

            return caso;

        }catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    public void setAgente(@NonNull Agente agente){

        try {
            JSONArray jsonArray= new JSONArray();

            JSONObject jsonObj = new JSONObject();

            jsonObj.put("nomeAgente",agente.getNome());
            jsonObj.put("dinheiro", agente.getDinheiro());
            jsonObj.put("casosConcluidos", agente.getCasosConcluidos());
            jsonObj.put("existe", agente.existe());

            //base
            jsonObj.put("baseCidade", agente.getBase().getCidade());
            jsonObj.put("baseEstado", agente.getBase().getEstado());
            jsonObj.put("baseRegiao", agente.getBase().getRegiao());
            jsonObj.put("basePopulacao", agente.getBase().getPopulacao());
            jsonObj.put("baseLocal", agente.getBase().getLocal());

            jsonArray.put(jsonObj);

            setLocalizacoes(agente, jsonArray);

            String arrayStr=jsonArray.toString();

            SharedPreferences.Editor editor = this.preferences.edit();
            editor.putString(AGENTE_KEY ,arrayStr);
            editor.apply();

        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public Agente getAgente(){
        try {

            JSONArray jsonArray= new JSONArray(this.preferences.getString(AGENTE_KEY,"[]"));

            Agente agente=new Agente();
            JSONObject jsonObj = jsonArray.getJSONObject(0);

            agente.setNome(jsonObj.getString("nomeAgente"));
            agente.setDinheiro(jsonObj.getDouble("dinheiro"));
            agente.setCasosConcluidos(jsonObj.getInt("casosConcluidos"));
            agente.setExiste(jsonObj.getBoolean("existe"));

            //base
            agente.getBase().setCidade(jsonObj.getString("baseCidade"));
            agente.getBase().setEstado(jsonObj.getString("baseEstado"));
            agente.getBase().setRegiao(jsonObj.getString("baseRegiao"));
            agente.getBase().setPopulacao(jsonObj.getInt("basePopulacao"));
            agente.getBase().setLocal(jsonObj.getString("baseLocal"));


            agente.setLocaisVisitados(getLocalizacoes(agente, jsonArray));

            return agente;

        }catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    private List<Localizacao> getLocalizacoes(Individuo individuo, @NonNull JSONArray jsonArray){

        try {


            List<Localizacao> localizacoes = new ArrayList<Localizacao>();

            for (int i = 1; i < jsonArray.length(); i++) {
                Localizacao localizacao = new Localizacao();
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                localizacao.setCidade(jsonObj.getString("localCidade"));
                localizacao.setCidade(jsonObj.getString("localEstado"));
                localizacao.setCidade(jsonObj.getString("localRegiao"));
                localizacao.setCidade(jsonObj.getString("localPopulacao"));
                localizacao.setCidade(jsonObj.getString("localLocal"));

                localizacoes.add(localizacao);

            }

            return localizacoes;

        }catch (JSONException e){
            e.printStackTrace();
        }

        return null;
    }

    private void setLocalizacoes(@NonNull Individuo individuo, JSONArray jsonArray){
        try{
            //locais visitados
            JSONObject jsonObj;
            for(Localizacao l: individuo.getLocaisVisitados()){
                jsonObj=new JSONObject();
                jsonObj.put("localCidade", l.getCidade());
                jsonObj.put("localEstado", l.getEstado());
                jsonObj.put("localRegiao", l.getRegiao());
                jsonObj.put("localPopulacao", l.getPopulacao());
                jsonObj.put("localLocal", l.getLocal());
                jsonArray.put(jsonObj);
            }

        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    public void setCriminoso(@NonNull Criminoso criminoso){
        try {
            JSONArray jsonArray= new JSONArray();

            JSONObject jsonObj = new JSONObject();

            jsonObj.put("nomeCriminoso",criminoso.getNome());
            jsonObj.put("crime", criminoso.getCrime());

            jsonArray.put(jsonObj);

            setLocalizacoes(criminoso, jsonArray);

            String arrayStr=jsonArray.toString();

            SharedPreferences.Editor editor = this.preferences.edit();
            editor.putString(CRIMINOSO_KEY ,arrayStr);
            editor.apply();

        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public Criminoso getCriminoso(){
        try {

            JSONArray jsonArray= new JSONArray(this.preferences.getString(CRIMINOSO_KEY,"[]"));

            Criminoso criminoso=new Criminoso();
            JSONObject jsonObj = jsonArray.getJSONObject(0);

            criminoso.setNome(jsonObj.getString("nomeCriminoso"));
            criminoso.setCrime(jsonObj.getString("crime"));

            criminoso.setLocaisVisitados(getLocalizacoes(criminoso, jsonArray));

            return criminoso;

        }catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }

}
