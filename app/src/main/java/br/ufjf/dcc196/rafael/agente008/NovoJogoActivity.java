package br.ufjf.dcc196.rafael.agente008;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.List;

import br.ufjf.dcc196.rafael.agente008.Adapters.CidadeAdapter;
import br.ufjf.dcc196.rafael.agente008.entities.Agente;
import br.ufjf.dcc196.rafael.agente008.entities.Localizacao;

public class NovoJogoActivity extends AppCompatActivity {

    private TextView tvBase;
    private EditText etNome, etCidade;
    private Spinner spUf;
    private Button btnCadastrar, btnRetornar;
    private RecyclerView rvCidades;
    private CidadeAdapter cidadeAdapter;
    private Localizacao baseSelecionada;
    private AppDatabase db;
    private List<Localizacao> localizacoes;
    private Agente agente;
    private JogoRepository repo;
    public static final int RESULT_NOVO_JOGO = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_jogo_activity);

        this.repo=new JogoRepository(getApplicationContext());
        this.db=AppDatabase.getInstance(getApplicationContext());
        this.baseSelecionada=null;

        buildViews();
        buildRv();
        buildListeners();

    }

    private void buildRv(){
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        this.rvCidades.setLayoutManager(layoutManager);

        CidadeAdapter.OnCidadeClickListener listener=new CidadeAdapter.OnCidadeClickListener() {
            @Override
            public void onCidadeClick(View source, int position) {
                baseSelecionada= cidadeAdapter.getLocalizacao(position);
                tvBase.setText(baseSelecionada.getCidade()+" - "+baseSelecionada.getEstado());
                habilitarBotaoCadastrar();
            }
        };


        new Thread(new Runnable() {
            @Override
            public void run() {

                if(etCidade.getText().toString().equals("")){
                    localizacoes=db.localizacaoDAO().findDistinctCidadesByEstado(spUf.getSelectedItem().toString(),"Delegacia");
                }else{
                    localizacoes=db.localizacaoDAO().findDistinctCidadesByEstadoAndCidade(spUf.getSelectedItem().toString(),etCidade.getText().toString(),"Delegacia");
                }
                cidadeAdapter =new CidadeAdapter(localizacoes,listener);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rvCidades.setAdapter(cidadeAdapter);
                    }
                });
            }
        }).start();


    }

    private void buildViews(){
        //TextViews
        this.tvBase=findViewById(R.id.tvBase);

        //Edit Text
        this.etNome=findViewById(R.id.etNome);
        this.spUf=findViewById(R.id.spUfBase);
        this.etCidade=findViewById(R.id.etCidadeBase);

        //Buttons
        this.btnCadastrar=findViewById(R.id.btnCadastrar);
        this.btnRetornar=findViewById(R.id.btnRetornar);

        //RecyclerView
        this.rvCidades=findViewById(R.id.rvCidadesBase);
    }

    private void buildListeners(){
        //Spinner listener
        this.spUf.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        //Nome EditText Listener
        this.etNome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                habilitarBotaoCadastrar();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void habilitarBotaoCadastrar(){
        if(this.baseSelecionada!=null && !this.etNome.getText().toString().equals("")){
            this.btnCadastrar.setEnabled(true);
        }else{
            this.btnCadastrar.setEnabled(false);
        }
    }


    public void cadastrarClick(View v){
        this.agente=new Agente();
        this.agente.setNome(this.etNome.getText().toString());
        this.agente.setBase(baseSelecionada);
        this.agente.setLocaisVisitados(new ArrayList<Localizacao>(){{add(agente.getBase());}});
        this.agente.setExiste(true);
        this.repo.setAgente(this.agente);

        Intent resultado = new Intent();
        setResult(RESULT_NOVO_JOGO, resultado);
        finish();

    }

    public void retornarClick(View v){
        Intent resultado = new Intent();
        setResult(-1, resultado);
        finish();

    }
}
