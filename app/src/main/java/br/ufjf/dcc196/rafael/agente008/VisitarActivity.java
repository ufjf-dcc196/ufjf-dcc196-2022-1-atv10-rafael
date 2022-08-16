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

import br.ufjf.dcc196.rafael.agente008.Adapters.LocalizacaoAdapter;
import br.ufjf.dcc196.rafael.agente008.entities.Agente;
import br.ufjf.dcc196.rafael.agente008.entities.Caso;
import br.ufjf.dcc196.rafael.agente008.entities.Localizacao;

public class VisitarActivity extends AppCompatActivity {

    private TextView tvNomeAgenteVisita, tvDinheiroVisita,tvDiaVisita, tvHoraVisita, tvLocalSelecionado, tvLocalizacaoAtualVisita, tvCustoVisita, tvTempoVisita;
    private Button btnVisitarVisita, btnRetornarVisita;
    private RecyclerView rvLocaisVisita;
    private JogoRepository repo;
    private AppDatabase db;
    private LocalizacaoAdapter localizacaoAdapter;
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

        LocalizacaoAdapter.OnLocalizacaoClickListener listener= new LocalizacaoAdapter.OnLocalizacaoClickListener() {
            @Override
            public void onLocalizacaoClick(View source, int position) {
                tvLocalSelecionado.setText(localizacaoAdapter.getLocalizacao(position).toString());
                tvCustoVisita.setText("R$5.00");
                tvTempoVisita.setText(String.valueOf(Caso.HORAS_VISITA)+"h");
                btnVisitarVisita.setEnabled(true);
                localEscolhido=localizacaoAdapter.getLocalizacao(position);

            }
        };
        this.localizacaoAdapter=new LocalizacaoAdapter(this.localizacoes,listener);
        this.rvLocaisVisita.setAdapter(localizacaoAdapter);
    }

    //Carrega os dados do SharedPreferences
    private void loadData(){
        Agente agente=this.repo.getAgente();
        Caso caso=this.repo.getCaso();
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

        this.agente=this.repo.getAgente();
        this.caso=this.repo.getCaso();

        this.agente.setDinheiro(this.agente.getDinheiro()-5.0);
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

    //Geração de mensagem para o click da visita
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