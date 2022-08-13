package br.ufjf.dcc196.rafael.agente008;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
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
    private List<Localizacao> localizacoes;
    private LocalizacaoAdapter localizacaoAdapter;
    private Random rand;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.rand=new Random();
        this.db=AppDatabase.getInstance(getApplicationContext());
        //DatabaseBuilder.popularDatabase(this.db);*/
        this.repo=new JogoRepository(getApplicationContext());

        resetData();
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
        Agente agente=this.repo.getAgente();

        if(agente.existe()) {
            this.tvNomeAgente.setText(agente.getNome().toString());
            this.tvDinheiro.setText("R$" + agente.getDinheiro().toString() + "0");
            this.tvLocalizacaoAtual.setText(agente.getBase().toString());
            this.btnNovoCaso.setEnabled(true);
        }else{
            noAgenteViewsSet();
        }

        Caso caso=this.repo.getCaso();

        this.tvDia.setText(caso.getDia().toString());
        this.tvHora.setText(caso.getHora().toString());
        this.tvStatus.setText(Caso.traduzirStatus(caso.getStatus()));
        this.tvNomeCriminoso.setText(caso.getCriminoso().getNome().toString());
        this.tvCrime.setText(caso.getCriminoso().getCrime().toString());

        if(caso.getStatus()==Caso.INEXISTENTE){
            noCasoViewsSet();
        }else{
            buildRv();
        }

    }

    private void resetData(){
        this.repo.setAgente(new Agente());
        this.repo.setCaso(new Caso());
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
                assert result.getData() != null;
                Bundle extras = result.getData().getExtras();

                if(result.getResultCode()==NovoJogoActivity.RESULT_NOVO_JOGO){
                    resetData();
                    criarAgente(extras);

                } else if(result.getResultCode()==VisitarActivity.RESULT_VISITAR){
                    //TODO implementar
                } else if(result.getResultCode()==ViajarActivity.RESULT_VIAJAR){
                    //TODO implementar
                }

                loadData();


            }
        });

    }


    private void criarAgente(Bundle extras){
        Agente agente = new Agente();

        agente.setNome(extras.getString("nomeAgente"));


        for(Localizacao l: this.localizacoes){
            if(l.getCidade().equals(extras.getString("cidadeAgente")) &&
                    l.getEstado().equals(extras.getString("ufAgente"))&&
                    l.getLocal().equals("Delegacia")){
                agente.setBase(l);
                break;
            }
        }

        agente.setExiste(true);
        agente.setLocaisVisitados(new ArrayList<Localizacao>(){{add(agente.getBase().clone());}});
        this.btnNovoCaso.setEnabled(true);
        this.repo.setAgente(agente);
    }

    public void gerarCaso(){
        Caso caso=new Caso();
        caso.setStatus(Caso.EM_ANDAMENTO);
        caso.setCriminoso(Criminoso.gerar(rand,this.repo.getAgente(),this.localizacoes));
        this.repo.setCaso(caso);

        //Adicionando 1000 reais ao agente para o novo caso
        Agente agente=this.repo.getAgente();
        agente.setDinheiro(agente.getDinheiro()+1000.0);
        agente.setLocaisVisitados(new ArrayList<Localizacao>());
        agente.getLocaisVisitados().add(agente.getBase());

        this.repo.setAgente(agente);

        this.btnNovoCaso.setEnabled(false);
        hasCasoViewSet();
    }

    public void clickBotoes(View v){
        try {
            Intent intent=null;

            switch (v.getId()){
                case R.id.btnNovoJogo:
                    intent = new Intent(MainActivity.this,NovoJogoActivity.class);
                    break;
                case R.id.btnVisitarLocal:
                    intent = new Intent(MainActivity.this,VisitarActivity.class);
                    break;
                case R.id.btnFazerViagem:
                    intent = new Intent(MainActivity.this,ViajarActivity.class);
                    break;
                case R.id.btnNovoCaso:
                    gerarCaso();
                    loadData();
                    break;

            }

            this.launcher.launch(intent);
        }catch (Exception e){
            e.printStackTrace();
        }
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
        this.btnFazerViagem.setEnabled(true);
        this.btnVisitarLocal.setEnabled(true);
    }


}