package br.ufjf.dcc196.rafael.agente008;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
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
    private Random rand;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.db=AppDatabase.getInstance(getApplicationContext());
        //DatabaseBuilder.popularDatabase(this.db);*/
        this.repo=new JogoRepository(getApplicationContext());
        this.rand=new Random();

        // resetData();
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


                    }
                });
            }

        }).start();

    }

    private void loadCaso(){
        Caso caso=this.repo.getCaso();

        if (!caso.getStatus().equals(Caso.INEXISTENTE)) {
            this.tvDia.setText(caso.getDia().toString());
            this.tvHora.setText(caso.getHora().toString());
            this.tvStatus.setText(caso.getStatus().toString());
            this.tvNomeCriminoso.setText(caso.getCriminoso().getNome().toString());
            this.tvCrime.setText(caso.getCriminoso().getCrime().toString());
        }

    }

    private void loadAgente(){
        Agente agente=this.repo.getAgente();

        this.tvNomeAgente.setText(agente.getNome().toString());
        this.tvDinheiro.setText("R$" + agente.getDinheiro().toString()+"0");
        this.tvLocalizacaoAtual.setText(agente.getBase().toString());

    }

    private void resetData(){
        this.repo.setCaso(new Caso());
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
                    criarAgente(extras);
                } else if(result.getResultCode()==VisitarActivity.RESULT_VISITAR){
                    //TODO implementar
                } else if(result.getResultCode()==ViajarActivity.RESULT_VIAJAR){
                    //TODO implementar
                }


            }
        });

    }


    private void criarAgente(Bundle extras){
        Agente agente = new Agente();
        agente.setNome(extras.getString("nomeAgente"));
        agente.getBase().setCidade(extras.getString("cidadeAgente"));
        agente.getBase().setEstado(extras.getString("ufAgente"));
        agente.getBase().setLocal("Delegacia");
        agente.setExiste(true);
        agente.setLocaisVisitados(new ArrayList<Localizacao>(){{add(agente.getBase().clone());}});
        this.btnNovoCaso.setEnabled(true);
        this.repo.setAgente(agente);
        loadAgente();
    }

    public void gerarCaso(){

        Caso caso=new Caso();
        caso.setStatus(Caso.EM_ANDAMENTO);
        caso.setCriminoso(Criminoso.gerar(rand,this.repo.getAgente(),this.localizacoes));

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
                    break;

            }

            this.launcher.launch(intent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}