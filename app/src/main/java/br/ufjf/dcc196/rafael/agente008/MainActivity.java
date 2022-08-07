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

@SuppressLint({"SetTextI18n", "NonConstantResourceId"})
public class MainActivity extends AppCompatActivity {

    private TextView tvNomeAgente, tvDia, tvHora, tvDinheiro, tvNomeCriminoso, tvCrime, tvStatus, tvLocalizacaoAtual;
    private Button btnNovoJogo, btnVisitarLocal, btnFazerViagem, btnNovoCaso;
    private RecyclerView rvLocaisVisitados;
    private ActivityResultLauncher<Intent> launcher;
    public final int RESULT_NOVO_JOGO = 0;
    public final int RESULT_VISITAR = 1;
    public final int RESULT_VIAJAR = 2;
    //TODO remover, colocado aqui só pra testar
    private Agente agente;
    private Caso caso;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buildViews();
        buildLauncher();
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
                    criarCaso();
                    montarLabels();
                } else if(result.getResultCode()==RESULT_VISITAR){
                    criarAgente(extras);
                    criarCaso();
                    montarLabels();
                } else if(result.getResultCode()==RESULT_VIAJAR){
                    criarAgente(extras);
                    criarCaso();
                    montarLabels();
                }


            }
        });

    }


    private void montarLabels(){
        this.tvNomeAgente.setText(this.agente.getNome());
        this.tvDia.setText(this.caso.getDia().toString());
        this.tvHora.setText(this.caso.getHora().toString());
        this.tvDinheiro.setText("R$"+this.caso.getAgente().getDinheiro().toString());
        this.tvStatus.setText(this.caso.getStatus().toString());
        this.tvNomeCriminoso.setText(this.caso.getCriminoso().getNome().toString());
        this.tvCrime.setText(this.caso.getCriminoso().getCrime().toString());
       this.tvLocalizacaoAtual.setText(agente.getLocaisVisitados().get(agente.getLocaisVisitados().size()-1).toString());
    }

    private void criarAgente(Bundle extras){
        agente = new Agente();
        agente.setNome(extras.getString("nomeAgente"));
        agente.getBase().setCidade(extras.getString("cidadeAgente"));
        agente.getBase().setEstado(extras.getString("ufAgente"));
        agente.getBase().setLocal("Delegacia");
        agente.setLocaisVisitados(new ArrayList<Localizacao>(){{add(agente.getBase().clone());}});

        this.btnVisitarLocal.setEnabled(true);
        this.btnFazerViagem.setEnabled(true);
    }
    private void criarCaso(){
        this.caso = new Caso();
        this.caso.criarCasoAutomatico(this.agente);
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

}