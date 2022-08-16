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
import br.ufjf.dcc196.rafael.agente008.DAO.DatabaseBuilder;
import br.ufjf.dcc196.rafael.agente008.entities.*;

@SuppressLint({"SetTextI18n", "NonConstantResourceId"})
public class MainActivity extends AppCompatActivity {

    private TextView tvNomeAgente, tvDia, tvHora, tvDinheiro, tvNomeCriminoso, tvCrime, tvStatus, tvLocalizacaoAtual;
    private Button btnNovoJogo, btnVisitarLocal, btnFazerViagem, btnNovoCaso;
    private RecyclerView rvLocaisVisitados;
    private JogoRepository repo;
    private AppDatabase db;
    private ActivityResultLauncher<Intent> launcher;
    private List<Localizacao> localizacoes;
    private Agente agente;
    private Caso caso;
    private LocalizacaoAdapter localizacaoAdapter;
    private Random rand;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.db=AppDatabase.getInstance(getApplicationContext());
        this.rand=new Random();
        this.repo=new JogoRepository(getApplicationContext());


        //DatabaseBuilder.popularLocalizacoes(this.db);
        this.repo.setAgente(new Agente());
        resetCaso();
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
                    }
                });
            }

        }).start();

    }

    private void loadData(){
        this.agente=this.repo.getAgente();

        this.tvNomeAgente.setText(this.agente.getNome().toString());
        this.tvDinheiro.setText("R$" + this.agente.getDinheiro().toString() + "0");
        this.tvLocalizacaoAtual.setText(this.agente.getBase().toString());
        this.btnNovoCaso.setEnabled(true);

        this.caso=this.repo.getCaso();

        this.tvDia.setText(caso.getDia().toString());
        this.tvHora.setText(caso.getHora().toString());
        this.tvStatus.setText(Caso.traduzirStatus(caso.getStatus()));
        this.tvNomeCriminoso.setText(caso.getCriminoso().getNome().toString());
        this.tvCrime.setText(caso.getCriminoso().getCrime().toString());

        if(!this.agente.existe()){
            noAgenteViewsSet();
        }else if(caso.getStatus()==Caso.INEXISTENTE){
            noCasoViewsSet();
        }else{
            hasCasoViewSet();
            buildRv();
        }

    }

    private void resetCaso(){
        //this.agente=new Agente();
        this.caso=new Caso();
        //this.repo.setAgente(this.agente);
        this.repo.setCaso(this.caso);
    }

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

    private void buildLauncher(){
        this.launcher= registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {

                if(result.getResultCode()==NovoJogoActivity.RESULT_NOVO_JOGO){
                    resetCaso();
                    btnNovoCaso.setEnabled(true);
                    loadData();
                } else if(result.getResultCode()==VisitarActivity.RESULT_VISITAR){
                    verificarJogo();
                } else if(result.getResultCode()==ViajarActivity.RESULT_VIAJAR){
                    verificarJogo();
                }

            }
        });

    }

    private void verificarJogo(){
        loadData();
        if(this.caso.getStatus().equals(Caso.FALIU)){
            System.out.println("Voce faliu!");
        }else if(this.caso.getStatus().equals(Caso.CONCLUIDO)){
            System.out.println("Parabéns! Voce conseguiu!");
        }else if(this.caso.getHora().equals(0)){
            System.out.println("Ultrapassadas as 16 horas de trabalho diario, novo dia");
        }
    }

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
        System.out.println("O bandido esta em "+this.caso.getCriminoso().getLocalizacaoAtual().toString());
        this.btnNovoCaso.setEnabled(false);
        hasCasoViewSet();
    }

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
        }
    }

//--Funções de tratamento para click nos botões

    public void clickBotoes(View v){
        try {
            Intent intent=null;

            switch (v.getId()){
                case R.id.btnVisitarLocal:
                    intent = new Intent(MainActivity.this,VisitarActivity.class);
                    break;
                case R.id.btnFazerViagem:
                    intent = new Intent(MainActivity.this,ViajarActivity.class);
                    break;
                case R.id.btnNovoCaso:
                    gerarCaso();
                    break;
            }

            loadData();
            this.launcher.launch(intent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void clickNovoJogo(View v){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Novo jogo!");
        dialogBuilder.setMessage("Ao iniciar um novo jogo, você deverá criar um novo Agente e todo o progresso será perdido. Continuar?");
        dialogBuilder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MainActivity.this,NovoJogoActivity.class);
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

    }


}