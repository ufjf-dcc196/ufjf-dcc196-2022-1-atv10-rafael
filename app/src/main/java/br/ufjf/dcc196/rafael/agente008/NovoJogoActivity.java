package br.ufjf.dcc196.rafael.agente008;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

import br.ufjf.dcc196.rafael.agente008.Adapters.LocalizacaoAdapter;
import br.ufjf.dcc196.rafael.agente008.entities.Localizacao;

public class NovoJogoActivity extends AppCompatActivity {

    private EditText etNome, etCidade;
    private Spinner spUf;
    private Button btnCadastrar, btnRetornar;
    private RecyclerView rvCidades;
    private LocalizacaoAdapter localizacaoAdapter;
    private List<Localizacao> localizacoes;
    private AppDatabase db;
    public static final int RESULT_NOVO_JOGO = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_jogo_activity);

        this.db=AppDatabase.getInstance(getApplicationContext());

        buildViews();
        buildRv();
        buildListeners();



    }

    private void buildRv(){
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        this.rvCidades.setLayoutManager(layoutManager);

        LocalizacaoAdapter.OnLocalizacaoClickListener listener=new LocalizacaoAdapter.OnLocalizacaoClickListener() {
            @Override
            public void onLocalizacaoClick(View source, int position) {
                System.out.println("teste");
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                localizacoes = db.localizacaoDAO().findDistinctCidades();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        localizacaoAdapter=new LocalizacaoAdapter(localizacoes,listener);
                        rvCidades.setAdapter(localizacaoAdapter);
                    }
                });
            }
        }).start();


    }

    private void buildViews(){
        //TextViews
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
        this.spUf.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //TODO implementar listener do Spinner
                String str=spUf.getSelectedItem().toString();
                String s2tr=spUf.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void cadastrarClick(View v){
        if(!this.etNome.getText().toString().equals("")) {
            Intent resultado = new Intent();
            resultado.putExtra("nomeAgente", this.etNome.getText().toString());
            resultado.putExtra("cidadeAgente", "Juiz de Fora");
            resultado.putExtra("ufAgente", "MG");
            setResult(RESULT_NOVO_JOGO, resultado);
            finish();
        }

    }

    public void retornarClick(View v){
            Intent resultado = new Intent();
            setResult(-1, resultado);
            finish();

    }
}