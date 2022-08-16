package br.ufjf.dcc196.rafael.agente008;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import br.ufjf.dcc196.rafael.agente008.Adapters.LocalizacaoAdapter;
import br.ufjf.dcc196.rafael.agente008.entities.*;

@SuppressLint({"SetTextI18n", "NonConstantResourceId"})
public class MainActivity extends AppCompatActivity {

    private TextView tvNomeAgente, tvDia, tvHora, tvDinheiro, tvNomeCriminoso, tvCrime, tvStatus, tvLocalizacaoAtual;
    private Button btnNovoJogo, btnVisitarLocal, btnFazerViagem, btnNovoCaso;
    private RecyclerView rvLocaisVisitados;
    private JogoRepository repo;
    private AppDatabase db;
    private ActivityResultLauncher<Intent> launcher;
    private LocalizacaoAdapter localizacaoAdapter;
    private List<Localizacao> localizacoes;
    private Random rand;
    private Agente agente;
    private Caso caso;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.db=AppDatabase.getInstance(getApplicationContext());
        this.rand=new Random();
        this.repo=new JogoRepository(getApplicationContext());


        buildLauncher();
        buildViews();

        //Carregando as Localizações
        new Thread(new Runnable() {
            @Override
            public void run() {

                localizacoes=db.localizacaoDAO().findAll();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadData();
                        btnNovoJogo.setEnabled(true);
                    }
                });
            }

        }).start();

    }

    //Busca o Agente o Caso no SharesPreferences
    private void loadData(){
        this.agente=this.repo.getAgente();

        this.tvNomeAgente.setText(this.agente.getNome().toString());
        this.tvDinheiro.setText("R$" + this.agente.getDinheiro().toString() + "0");
        this.tvLocalizacaoAtual.setText(this.agente.getLocalizacaoAtual().toString());
        this.btnNovoCaso.setEnabled(true);

        this.caso=this.repo.getCaso();

        this.tvDia.setText(caso.getDia().toString());
        this.tvHora.setText(caso.getHora().toString());
        this.tvStatus.setText(Caso.traduzirStatus(caso.getStatus()));
        this.tvNomeCriminoso.setText(caso.getCriminoso().getNome().toString());
        this.tvCrime.setText(caso.getCriminoso().getCrime().toString());

        if(!this.agente.existe()){
            noAgenteViewsSet();
        }else if(!caso.getStatus().equals(Caso.EM_ANDAMENTO)){
            noCasoViewsSet();
        }else{
            hasCasoViewSet();
        }
        buildRv();

    }

    //Reseta o caso atual
    private void resetCaso(){
        this.caso=new Caso();
        this.repo.setCaso(this.caso);
    }

    //Constroi e popula o recyclerView
    private void buildRv(){
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        this.rvLocaisVisitados.setLayoutManager(layoutManager);

        LocalizacaoAdapter.OnLocalizacaoClickListener listener= new LocalizacaoAdapter.OnLocalizacaoClickListener() {
            @Override
            public void onLocalizacaoClick(View source, int position) {
            }
        };
        this.localizacaoAdapter=new LocalizacaoAdapter(this.repo.getAgente().getLocaisVisitados(),listener);
        this.rvLocaisVisitados.setAdapter(localizacaoAdapter);
    }

    //Atribuição de views para variaveis
    private void buildViews(){
        //TextViews
        this.tvDia=findViewById(R.id.tvDia);
        this.tvHora=findViewById(R.id.tvHora);
        this.tvDinheiro=findViewById(R.id.tvDinheiro);
        this.tvNomeCriminoso=findViewById(R.id.tvNomeCriminoso);
        this.tvCrime=findViewById(R.id.tvCrime);
        this.tvStatus=findViewById(R.id.tvStatus);
        this.tvLocalizacaoAtual=findViewById(R.id.tvLocalizacaoAtual);
        this.tvNomeAgente=findViewById(R.id.tvNomeAgente);

        //Buttons
        this.btnNovoJogo=findViewById(R.id.btnNovoJogo);
        this.btnVisitarLocal=findViewById(R.id.btnVisitarLocal);
        this.btnFazerViagem =findViewById(R.id.btnFazerViagem);
        this.btnNovoCaso=findViewById(R.id.btnNovoCaso);

        //RecyclerView
        this.rvLocaisVisitados=findViewById(R.id.rvLocaisVisitados);
    }

    //Tratamento do launcher, para retorno das activities
    private void buildLauncher(){
        this.launcher= registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {

                if(result.getResultCode()==NovoJogoActivity.RESULT_NOVO_JOGO){
                    resetCaso();
                    btnNovoCaso.setEnabled(true);
                    loadData();
                } else if(result.getResultCode()==VisitarActivity.RESULT_VISITAR ||
                        result.getResultCode()==ViajarActivity.RESULT_VIAJAR){
                    loadData();
                    verificarJogo();
                }

            }
        });

    }

    //--Ajustam as views e as entidades de acordo com o status do jogo
    private void verificarJogo(){
        if(this.caso.getStatus().equals(Caso.FALIU)){
            tratarFalencia();
        }else if(this.caso.getStatus().equals(Caso.CONCLUIDO)){
            tratarConclusao();
        }
    }
    private void tratarFalencia(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Você faliu!");
        dialogBuilder.setMessage(gerarFalenciaMensagem());
        dialogBuilder.setPositiveButton("Ok!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MainActivity.this, NovoJogoActivity.class);
                noAgenteViewsSet();
            }
        });
        dialogBuilder.create();
        dialogBuilder.show();
    }
    private void tratarConclusao(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Você conseguiu!");
        dialogBuilder.setMessage(gerarConclusaoMensagem());
        dialogBuilder.setPositiveButton("Ok!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                noCasoViewsSet();
            }
        });
        dialogBuilder.create();
        dialogBuilder.show();
    }

    //--Seta o enables dos botões dependendo do status do jogo
    private void noAgenteViewsSet(){
        this.btnNovoCaso.setEnabled(false);
        noCasoViewsSet();
    }
    private void noCasoViewsSet(){
        this.btnFazerViagem.setEnabled(false);
        this.btnVisitarLocal.setEnabled(false);
    }
    private void hasCasoViewSet(){
        this.btnNovoCaso.setEnabled(false);
        this.btnVisitarLocal.setEnabled(true);
        if(this.agente.getLocalizacaoAtual().getLocal().equals("Aeroporto") || this.agente.getLocalizacaoAtual().getLocal().equals("Rodoviaria")) {
            this.btnFazerViagem.setEnabled(true);
        }else{
            this.btnFazerViagem.setEnabled(false);
        }
    }

    //Gera um novo caso
    public void gerarCaso(){
        this.caso=new Caso();
        this.caso.setStatus(Caso.EM_ANDAMENTO);
        this.caso.setCriminoso(Criminoso.gerar(rand,this.repo.getAgente(),this.localizacoes));
        this.repo.setCaso(this.caso);

        //Adicionando 1000 reais ao agente para o novo caso
        this.agente=this.repo.getAgente();
        this.agente.setDinheiro(this.agente.getDinheiro()+1000.0);
        this.agente.setLocaisVisitados(new ArrayList<Localizacao>());
        this.agente.getLocaisVisitados().add(this.agente.getBase());

        this.repo.setAgente(agente);
        this.btnNovoCaso.setEnabled(false);
        hasCasoViewSet();
    }

    //--Funções de tratamento para click nos botões
    public void clickNovoJogo(View v){
        if(this.agente.existe()) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setTitle("Novo jogo!");
            dialogBuilder.setMessage(gerarAlertaNovoCasoMensagem());
            dialogBuilder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(MainActivity.this, NovoJogoActivity.class);
                    launcher.launch(intent);
                }
            });
            dialogBuilder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dialogBuilder.create();
            dialogBuilder.show();
        }else{
            Intent intent = new Intent(MainActivity.this, NovoJogoActivity.class);
            launcher.launch(intent);
        }

    }
    public void clickNovoCaso(View v){
        gerarCaso();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Novo caso!");
        dialogBuilder.setMessage(gerarNovoCasoMensagem());
        dialogBuilder.setPositiveButton("Ok!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loadData();
            }
        });
        dialogBuilder.create();
        dialogBuilder.show();
    }
    public void clickVisitar(View v){
        Intent intent=new Intent(MainActivity.this,VisitarActivity.class);
        this.launcher.launch(intent);
    }
    public void clickViajar(View v){
        Intent intent=new Intent(MainActivity.this,ViajarActivity.class);
        this.launcher.launch(intent);
    }

    //--Geraçoes de mensagem para o AlertDialog
    private String gerarAlertaNovoCasoMensagem(){

        return "Ao iniciar um novo jogo, você deverá criar um novo Agente e todo o progresso será perdido. Continuar?";
    }
    private String gerarNovoCasoMensagem(){
        String mensagem="Você foi escalado(a) para trabalhar em um novo caso. Neste, ";
        Criminoso criminoso=this.caso.getCriminoso();
        mensagem+= criminoso.getNome() + " foi acusado(a) de " +
                criminoso.getCrime()+
                ", e precisamos pega-lo(a)! "+
                criminoso.getNome()+" foi visto(a) nos arredores de " +
                criminoso.getLocaisVisitados().get(0).getCidade()+
                ". Você tera uma jornada limitada a 16 horas por dia! "+
                ". Contamos com a sua competência, oferecendo um aporte de R$1.000,00 para deslocamentos. Bom trabalho!";

        return mensagem;
    }
    private String gerarFalenciaMensagem(){

        return "Você gastou todo o dinheiro disponível, foi destituido do caso e demitido.";
    }
    private String gerarConclusaoMensagem(){
        String mensagem="Parabens Agente "+this.agente.getNome()+
                "! "+ this.caso.getCriminoso().getCrime() +
                "foi encontrado por você em " + this.agente.getLocalizacaoAtual().toString() +
                ". Como bonificação, você ficara com o restante do dinheiro do aporte da investigação.";
        return mensagem;
    }
}