package br.ufjf.dcc196.rafael.agente008;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.ufjf.dcc196.rafael.agente008.Adapters.CidadeAdapter;
import br.ufjf.dcc196.rafael.agente008.entities.Agente;
import br.ufjf.dcc196.rafael.agente008.entities.Caso;
import br.ufjf.dcc196.rafael.agente008.entities.Localizacao;

public class ViajarActivity extends AppCompatActivity {

    private TextView tvNomeAgenteViagem, tvDiaViagem, tvHoraViagem, tvDinheiroViagem, tvLocalizacaoAtualViagem, tvCidadeSelecionada,tvCustoViagem, tvTempoViagem;
    private EditText etCidade;
    private Spinner spUfViagem;
    private Button btnViajarViagem, btnRetornarViagem;
    private List<Localizacao> cidades;
    private Localizacao cidadeSelecionada;
    private CidadeAdapter cidadeAdapter;
    private RecyclerView rvCidades;
    private Integer horasViagem;
    private Double precoViagem;
    private Agente agente;
    private Caso caso;
    private AppDatabase db;
    private JogoRepository repo;

    public static final int RESULT_VIAJAR = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viajar);

        this.db=AppDatabase.getInstance(getApplicationContext());
        this.repo=new JogoRepository(getApplicationContext());

        buildViews();
        setViews();
        buildRv();
        buildListeners();
    }

    private void buildViews(){
        //TextViews
        this.tvNomeAgenteViagem=findViewById(R.id.tvNomeAgenteViagem);
        this.tvDiaViagem=findViewById(R.id.tvDiaViagem);
        this.tvHoraViagem=findViewById(R.id.tvHoraViagem);
        this.tvLocalizacaoAtualViagem =findViewById(R.id.tvLocalizacaoAtualViagem);
        this.tvCustoViagem=findViewById(R.id.tvCustoViagem);
        this.tvTempoViagem=findViewById(R.id.tvTempoViagem);
        this.tvDinheiroViagem =findViewById(R.id.tvDinheiroViajar);
        this.tvCidadeSelecionada=findViewById(R.id.tvCidadeSelecionada);

        //EditText
        this.etCidade=findViewById(R.id.etCidadeViajar);

        //Spinners
        this.spUfViagem =findViewById(R.id.spUfViajar);

        //Buttons
        this.btnViajarViagem=findViewById(R.id.btnFazerViagem);
        this.btnRetornarViagem=findViewById(R.id.btnRetornaViagem);

        //RecyclerView
        this.rvCidades=findViewById(R.id.rvCidadesViagem);
    }

    private void setViews(){
        this.agente=this.repo.getAgente();
        this.caso=this.repo.getCaso();
        this.tvNomeAgenteViagem.setText(agente.getNome());
        this.tvDinheiroViagem.setText("R$"+agente.getDinheiro().toString()+"0");
        this.tvLocalizacaoAtualViagem.setText(agente.getLocaisVisitados().get(agente.getLocaisVisitados().size()-1).toString());
        this.tvDiaViagem.setText(caso.getDia().toString());
        this.tvHoraViagem.setText(caso.getHora().toString());
        this.tvLocalizacaoAtualViagem.setText(agente.getLocaisVisitados().get(agente.getLocaisVisitados().size()-1).toString());
        this.tvNomeAgenteViagem.setText(agente.getNome());

    }

    private void buildRv(){
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        this.rvCidades.setLayoutManager(layoutManager);

        CidadeAdapter.OnCidadeClickListener listener=new CidadeAdapter.OnCidadeClickListener() {
            @Override
            public void onCidadeClick(View source, int position) {
                cidadeSelecionada= cidadeAdapter.getLocalizacao(position);
                precoViagem=Localizacao.precificarDeslocamento(agente.getLocaisVisitados().get(agente.getLocaisVisitados().size()-1),cidadeSelecionada);
                horasViagem=Localizacao.quantificarTempo(agente.getLocaisVisitados().get(agente.getLocaisVisitados().size()-1),cidadeSelecionada);
                tvCidadeSelecionada.setText(cidadeSelecionada.getCidade()+" - "+cidadeSelecionada.getEstado());
                tvCustoViagem.setText("R$"+String.valueOf(precoViagem)+"0");
                tvTempoViagem.setText(String.valueOf(horasViagem)+"h");
                habilitarBotaoViajar();
            }
        };


        new Thread(new Runnable() {
            @Override
            public void run() {

                if(etCidade.getText().toString().equals("")){
                    if(agente.getLocalizacaoAtual().getLocal().equals("Aeroporto")) {
                        cidades = db.localizacaoDAO().findCidadesComAeroportoByEstado(spUfViagem.getSelectedItem().toString());
                    }else {
                        cidades = db.localizacaoDAO().findDistinctCidadesByEstado(spUfViagem.getSelectedItem().toString(), "Delegacia");
                    }
                }else {
                    if (agente.getLocalizacaoAtual().getLocal().equals("Aeroporto")) {
                        cidades = db.localizacaoDAO().findCidadesComAeroportoByEstadoeCidade(spUfViagem.getSelectedItem().toString(),etCidade.getText().toString() );
                    } else {
                        cidades = db.localizacaoDAO().findDistinctCidadesByEstadoAndCidade(spUfViagem.getSelectedItem().toString(), etCidade.getText().toString(), "Delegacia");
                    }
                }


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cidadeAdapter =new CidadeAdapter(cidades,listener);
                        rvCidades.setAdapter(cidadeAdapter);
                    }
                });
            }
        }).start();


    }
    private void buildListeners(){
        //Spinner listener
        this.spUfViagem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                buildRv();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Cidade EditText listener
        this.etCidade.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                buildRv();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
    public void habilitarBotaoViajar(){
        if(this.cidadeSelecionada!=null){
            this.btnViajarViagem.setEnabled(true);
        }else{
            this.btnViajarViagem.setEnabled(false);
        }
    }

    public void retornarClick(View v){
        Intent resultado = new Intent();
        setResult(-1, resultado);
        finish();

    }

    public void viajarClick(View v){
        Intent resultado = new Intent();
        this.agente.setDinheiro(this.agente.getDinheiro()-this.precoViagem);
        this.caso.setHora(this.caso.getHora()+this.horasViagem);
        this.agente.getLocaisVisitados().add(this.cidadeSelecionada);

        if(this.agente.getDinheiro()<0.0){
            this.caso.setStatus(Caso.FALIU);
        }else if(this.agente.getLocalizacaoAtual().equals(this.caso.getCriminoso().getLocalizacaoAtual())){
            this.caso.setStatus(Caso.CONCLUIDO);
        }else if(this.caso.getHora()>=Caso.HORAS_TRABALHADAS_POR_DIA){
            this.caso.incrDia();
            this.caso.setHora(0);
        }

        this.repo.setAgente(this.agente);
        this.repo.setCaso(this.caso);

        setResult(RESULT_VIAJAR, resultado);
        finish();

    }
}