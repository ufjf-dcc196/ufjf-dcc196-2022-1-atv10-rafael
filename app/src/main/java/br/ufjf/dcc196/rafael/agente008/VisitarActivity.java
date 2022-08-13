package br.ufjf.dcc196.rafael.agente008;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import br.ufjf.dcc196.rafael.agente008.Adapters.LocalizacaoAdapter;
import br.ufjf.dcc196.rafael.agente008.entities.Agente;
import br.ufjf.dcc196.rafael.agente008.entities.Caso;
import br.ufjf.dcc196.rafael.agente008.entities.Localizacao;

public class VisitarActivity extends AppCompatActivity {

    private TextView tvNomeAgenteVisita, tvDinheiroVisita,tvDiaVisita, tvHoraVisita, tvLocalSelecionado, tvLocalizacaoAtualVisita, tvCustoVisita, tvTempoVisita;
    private Spinner spUfVisita, spMunicipioVisita;
    private Button btnVisitarVisita, btnRetornarVisita;
    private RecyclerView rvLocaisVisita;
    private JogoRepository repo;
    private LocalizacaoAdapter localizacaoAdapter;
    private List<Localizacao> localizacoes;
    private Localizacao localEscolhido;
    private AppDatabase db;
    public static final int RESULT_VISITAR = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitar);

        this.db=AppDatabase.getInstance(getApplicationContext());
        this.repo=new JogoRepository(getApplicationContext());

        buildViews();
        setViews();
        loadLocalizacoes();
    }

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

    private void loadLocalizacoes(){
        Agente agente = this.repo.getAgente();

    new Thread(new Runnable() {
        @Override
        public void run() {
            String cidade =agente.getLocaisVisitados().get(agente.getLocaisVisitados().size()-1).getCidade();
            localizacoes=db.localizacaoDAO().findbyCidades(cidade);

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
                tvTempoVisita.setText("1h");
                btnVisitarVisita.setEnabled(true);
                localEscolhido=localizacaoAdapter.getLocalizacao(position);

            }
        };
        this.localizacaoAdapter=new LocalizacaoAdapter(this.localizacoes,listener);
        this.rvLocaisVisita.setAdapter(localizacaoAdapter);
    }

    private void setViews(){
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


    public void retornarClick(View v){
        Intent resultado = new Intent();
        setResult(-1, resultado);
        finish();

    }

    public void visitarClick(View v){

        Agente agente=this.repo.getAgente();
        Caso caso=this.repo.getCaso();

        agente.setDinheiro(agente.getDinheiro()-5.0);
        agente.getLocaisVisitados().add(this.localEscolhido);

        caso.setHora(caso.getHora()+1);

        this.repo.setAgente(agente);
        this.repo.setCaso(caso);

        Intent resultado = new Intent();
        setResult(-1, resultado);
        finish();

    }

}