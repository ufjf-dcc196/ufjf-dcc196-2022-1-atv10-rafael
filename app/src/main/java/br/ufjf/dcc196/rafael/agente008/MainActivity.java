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

import br.ufjf.dcc196.rafael.agente008.entities.*;

@SuppressLint({"SetTextI18n", "NonConstantResourceId"})
public class MainActivity extends AppCompatActivity {

    private TextView tvNomeAgente, tvDia, tvHora, tvDinheiro, tvNomeCriminoso, tvCrime, tvStatus, tvLocalizacaoAtual;
    private Button btnNovoJogo, btnVisitarLocal, btnFazerViagem, btnNovoCaso;
    private RecyclerView rvLocaisVisitados;
    private JogoRepository repo;
    private ActivityResultLauncher<Intent> launcher;
    public final int RESULT_NOVO_JOGO = 0;
    public final int RESULT_VISITAR = 1;
    public final int RESULT_VIAJAR = 2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.repo=new JogoRepository(getApplicationContext());

        //resetData();

        buildViews();
        buildLauncher();
        loadData();
    }

    private void loadData(){
        Caso caso=this.repo.getCaso();
        Agente agente = caso.getAgente();

        if(agente.existe()) {


            this.tvNomeAgente.setText(agente.getNome().toString());
            this.tvDinheiro.setText("R$" + caso.getAgente().getDinheiro().toString());

            if (!caso.getStatus().equals(Caso.INEXISTENTE)) {
                this.tvDia.setText(caso.getDia().toString());
                this.tvHora.setText(caso.getHora().toString());
                this.tvStatus.setText(caso.getStatus().toString());
                this.tvNomeCriminoso.setText(caso.getCriminoso().getNome().toString());
                this.tvCrime.setText(caso.getCriminoso().getCrime().toString());
                this.tvLocalizacaoAtual.setText(agente.getLocaisVisitados().get(agente.getLocaisVisitados().size() - 1).toString());
            }
        }
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

                if(result.getResultCode()==RESULT_NOVO_JOGO){
                    criarAgente(extras);
                    loadData();
                } else if(result.getResultCode()==RESULT_VISITAR){
                    criarAgente(extras);
                    loadData();
                } else if(result.getResultCode()==RESULT_VIAJAR){
                    criarAgente(extras);
                    loadData();
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

        Caso caso = new Caso();
        caso.criarCasoAutomatico(agente); //TODO apagar, só pra testar
        //caso.setAgente(agente);

        this.repo.setCaso(caso);
        loadData();

        this.btnVisitarLocal.setEnabled(true);
        this.btnFazerViagem.setEnabled(true);
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

            }

            this.launcher.launch(intent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void sendExtras(Intent intent){

    }

}