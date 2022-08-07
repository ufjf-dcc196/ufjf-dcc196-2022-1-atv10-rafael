package br.ufjf.dcc196.rafael.agente008;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class NovoJogoActivity extends AppCompatActivity {

    private EditText etNome;
    private Spinner spUf, spMunicipio;
    private Button btnCadastrar, btnRetornar;
    public final int RESULT_NOVO_JOGO = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_jogo_activity);

        buildViews();
    }

    private void buildViews(){
        //TextViews
        this.etNome=findViewById(R.id.etNome);

        //Spinners
        this.spUf=findViewById(R.id.spUf);
        this.spMunicipio=findViewById(R.id.spMunicipio);

        //Buttons
        this.btnCadastrar=findViewById(R.id.btnCadastrar);
        this.btnRetornar=findViewById(R.id.btnRetornar);
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