package br.ufjf.dcc196.rafael.agente008;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class NovoJogoActivity extends AppCompatActivity {

    private EditText etNome, etUf, etCidade;
    private Button btnCadastrar, btnRetornar;
    private RecyclerView rvCidades;
    public static final int RESULT_NOVO_JOGO = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_jogo_activity);

        buildViews();
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