package br.ufjf.dcc196.rafael.agente008;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import br.ufjf.dcc196.rafael.agente008.Adapters.LocalizacaoAdapter;
import br.ufjf.dcc196.rafael.agente008.entities.Localizacao;

public class NovoJogoActivity extends AppCompatActivity {

    private EditText etNome, etUf, etCidade;
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

        new Thread(new Runnable() {
            @Override
            public void run() {
                localizacoes = db.localizacaoDAO().findDistinctCidades();
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
        this.rvCidades.setLayoutManager(layoutManager);

        this.localizacaoAdapter=new LocalizacaoAdapter(this.localizacoes);
        this.rvCidades.setAdapter(this.localizacaoAdapter);
    }

    private void buildViews(){
        //TextViews
        this.etNome=findViewById(R.id.etNome);
        this.etUf=findViewById(R.id.etUfBase);
        this.etCidade=findViewById(R.id.etCidadeBase);

        //Buttons
        this.btnCadastrar=findViewById(R.id.btnCadastrar);
        this.btnRetornar=findViewById(R.id.btnRetornar);

        //RecyclerView
        this.rvCidades=findViewById(R.id.rvCidadesBase);
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