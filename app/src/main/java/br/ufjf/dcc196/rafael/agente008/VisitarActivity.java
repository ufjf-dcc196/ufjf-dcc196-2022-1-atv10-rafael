package br.ufjf.dcc196.rafael.agente008;
//Classe da activity visitar
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

import br.ufjf.dcc196.rafael.agente008.Adapters.LocalAdapter;
import br.ufjf.dcc196.rafael.agente008.entities.Agente;
import br.ufjf.dcc196.rafael.agente008.entities.Caso;
import br.ufjf.dcc196.rafael.agente008.entities.Localizacao;

public class VisitarActivity extends AppCompatActivity {

    private TextView tvNomeAgenteVisita, tvDinheiroVisita,tvDiaVisita, tvHoraVisita, tvLocalSelecionado, tvLocalizacaoAtualVisita, tvCustoVisita, tvTempoVisita;
    private Button btnVisitarVisita, btnRetornarVisita;
    private RecyclerView rvLocaisVisita;
    private JogoRepository repo;
    private AppDatabase db;
    private LocalAdapter localAdapter;
    private List<Localizacao> localizacoes;
    private Localizacao localEscolhido;
    private Agente agente;
    private Caso caso;
    public static final int RESULT_VISITAR = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitar);

        this.db=AppDatabase.getInstance(getApplicationContext());
        this.repo=new JogoRepository(getApplicationContext());

        buildViews();
        loadData();
        loadLocalizacoes();
    }

    //Atribuição de views para variaveis
    private void buildViews(){
        //TextViews
        this.tvNomeAgenteVisita=findViewById(R.id.tvNomeAgenteVisita);
        this.tvDinheiroVisita=findViewById(R.id.tvDinheiroVisita);
        this.tvDiaVisita=findViewById(R.id.tvDiaVisita);
        this.tvHoraVisita=findViewById(R.id.tvHoraVisita);
        this.tvLocalizacaoAtualVisita =findViewById(R.id.tvLocalizacaoAtualVisita);
        this.tvLocalSelecionado=findViewById(R.id.tvLocalSelecionado);
        this.tvCustoVisita=findViewById(R.id.tvCustoVisita);
        this.tvTempoVisita=findViewById(R.id.tvTempoVisita);

        //Buttons
        this.btnVisitarVisita=findViewById(R.id.btnVisitar);
        this.btnRetornarVisita=findViewById(R.id.btnRetornaVisita);

        //RecyclerView
        this.rvLocaisVisita=findViewById(R.id.rvLocaisVisita);
    }

    //Busca no db as localizações da cidade referida
    private void loadLocalizacoes(){
        Agente agente = this.repo.getAgente();

        new Thread(new Runnable() {
            @Override
            public void run() {
                String cidade =agente.getLocalizacaoAtual().getCidade();
                localizacoes=db.localizacaoDAO().findLocaisbyCidades(cidade);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        buildRv();
                    }
                });
            }

        }).start();

    }

    private void buildRv(){
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        this.rvLocaisVisita.setLayoutManager(layoutManager);

        LocalAdapter.OnLocalClickListener listener= new LocalAdapter.OnLocalClickListener() {
            @Override
            public void onLocalClick(View source, int position) {
                tvLocalSelecionado.setText(localAdapter.getLocalizacao(position).getLocal());
                tvCustoVisita.setText("R$5.00");
                tvTempoVisita.setText(String.valueOf(Caso.HORAS_VISITA)+"h");
                btnVisitarVisita.setEnabled(true);
                localEscolhido=localAdapter.getLocalizacao(position);

            }
        };
        this.localAdapter=new LocalAdapter(this.localizacoes,listener);
        this.rvLocaisVisita.setAdapter(localAdapter);
    }

    //Carrega os dados do SharedPreferences
    private void loadData(){
        this.agente=this.repo.getAgente();
        this.caso=this.repo.getCaso();
        this.tvNomeAgenteVisita.setText(agente.getNome());
        this.tvDinheiroVisita.setText("R$"+agente.getDinheiro().toString()+"0");
        this.tvLocalizacaoAtualVisita.setText(agente.getLocaisVisitados().get(agente.getLocaisVisitados().size()-1).toString());
        this.tvDiaVisita.setText(caso.getDia().toString());
        this.tvHoraVisita.setText(caso.getHora().toString());
        this.tvLocalizacaoAtualVisita.setText(agente.getLocaisVisitados().get(agente.getLocaisVisitados().size()-1).toString());
        this.tvNomeAgenteVisita.setText(agente.getNome());
    }

    //-Tratamento dos clicks nos botoes
    public void retornarClick(View v){
        Intent resultado = new Intent();
        setResult(-1, resultado);
        finish();

    }
    public void visitarClick(View v){

        this.agente.decrDinheiro(5.0);
        this.agente.getLocaisVisitados().add(this.localEscolhido);
        this.caso.setHora(this.caso.getHora()+caso.HORAS_VISITA);
        Boolean mudouDia=false;

        if(this.agente.getDinheiro()<0.0){
            this.caso.setStatus(Caso.FALIU);
        }else if(this.agente.getLocalizacaoAtual().equals(this.caso.getCriminoso().getLocalizacaoAtual())){
            this.caso.setStatus(Caso.CONCLUIDO);
        }else if(this.caso.getHora()>=Caso.MAX_HORAS_TRABALHADAS_POR_DIA){
            this.caso.incrDia();
            this.caso.setHora(0);
            mudouDia=true;
        }

        gerarDica();

        this.repo.setAgente(this.agente);
        this.repo.setCaso(this.caso);

        Intent resultado = new Intent();
        setResult(RESULT_VISITAR, resultado);
        if(!mudouDia){
            finish();
        }else {
            gerarVisitarMensagem();
        }

    }

    //Geração da dica de onde o criminoso está
    public void gerarDica() {

        List<Localizacao> locaisCriminoso = this.caso.getCriminoso().getLocaisVisitados();
        Localizacao proximoLocalCriminoso = null;

        if(!this.agente.getLocalizacaoAtual().equals(this.caso.getCriminoso().getLocalizacaoAtual())) {
            for (int i = 0; i < locaisCriminoso.size(); i++) {
                if (locaisCriminoso.get(i).equals(this.agente.getLocalizacaoAtual())) {
                    proximoLocalCriminoso = locaisCriminoso.get(i + 1);
                    break;
                }
            }

            String dica="";
            if (proximoLocalCriminoso == null) {
                dica = "Não temos informação dessa pessoa por aqui";
                this.agente.getLocalizacaoAtual().setDica(dica);
            } else {

                if (!Localizacao.jaVisitado(proximoLocalCriminoso,this.agente.getLocaisVisitados())) {
                    Random rand = new Random();

                    Integer selecao = rand.nextInt(4);
                    dica = "Eu estive com essa pessoa. ";

                    switch (selecao) {
                        case 0:
                            dica += "Ouvi ele dizendo algo sobre ir ";
                            break;
                        case 1:
                            dica += "Ele falou sobre ir ";
                            break;
                        case 2:
                            dica += "No telefone, ouvi ele diszer algo sobre ir ";
                            break;
                        case 3:
                            dica += "Ouvi um comentário dele sobre ir ";
                            break;
                    }

                    if (this.agente.getLocalizacaoAtual().getLocal().equals("Aeroporto") ||
                            this.agente.getLocalizacaoAtual().getLocal().equals("Rodoviaria")) {

                        dica += "a " + proximoLocalCriminoso.getCidade() + "/" + proximoLocalCriminoso.getEstado() + ".";
                    } else {
                        if (proximoLocalCriminoso.getLocal().contains("Restaurante")) {
                            dica += "a um restaurante.";
                        } else if (proximoLocalCriminoso.getLocal().contains("Farmácia")) {
                            dica += "a uma farmácia.";
                        } else if (proximoLocalCriminoso.getLocal().contains("Padaria")) {
                            dica += "a uma padaria.";
                        } else if (proximoLocalCriminoso.getLocal().contains("Barzinho")) {
                            dica += "a um barzinho.";
                        } else if (proximoLocalCriminoso.getLocal().contains("Residência")) {
                            dica += "a uma redisência.";
                        } else if (proximoLocalCriminoso.getLocal().contains("Posto de combustível")) {
                            dica += "a um posto de combustível.";
                        } else if (proximoLocalCriminoso.getLocal().contains("Hospital")) {
                            dica += "a um hospital.";
                        } else if (proximoLocalCriminoso.getLocal().contains("Rodoviaria")) {
                            dica += "a rodoviária.";
                        } else if (proximoLocalCriminoso.getLocal().contains("Aeroporto")) {
                            dica += "ao aeroporto.";
                        }
                    }
                    this.agente.getLocalizacaoAtual().setDica(dica);
                }else{
                    Localizacao localAnteriorAgente;
                    for(Localizacao l: this.agente.getLocaisVisitados()){
                        if(l.equals(this.agente.getLocalizacaoAtual())){
                            this.agente.getLocalizacaoAtual().setDica(l.getDica());
                            break;
                        }
                    }
                }
            }
        }
    }

    //Geração de mensagens para o click da visita
    private void gerarVisitarMensagem() {
        String mensagem = " Voce atingiu 16 horas de trabalho, as proximas tarefas ocorrerão no dia seguinte";
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Horas excedidas!!");
        dialogBuilder.setMessage(mensagem);
        dialogBuilder.setPositiveButton("Ok!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        dialogBuilder.create();
        dialogBuilder.show();
    }

}