package br.ufjf.dcc196.rafael.agente008;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView tvNomeAgente, tvDia, tvHora, tvDinheiro, tvNomeCriminoso, tvCrime, tvLocalizacaoAtual;
    Button btnNovoJogo, btnVisitarLocal, btnViajar, btnNovoCaso;
    RecyclerView rvLocaisVisitados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buildViews();

    }

    private void buildViews(){
        //TextViews
        this.tvDia=findViewById(R.id.tvDia);
        this.tvHora=findViewById(R.id.tvHora);
        this.tvDinheiro=findViewById(R.id.tvDinheiro);
        this.tvNomeCriminoso=findViewById(R.id.tvNomeCriminoso);
        this.tvCrime=findViewById(R.id.tvCrime);
        this.tvLocalizacaoAtual=findViewById(R.id.tvLocalizacaoAtual);
        this.tvNomeAgente=findViewById(R.id.tvNomeAgente);

        //Buttons
        this.btnNovoJogo=findViewById(R.id.btnNovoJogo);
        this.btnVisitarLocal=findViewById(R.id.btnVisitarLocal);
        this.btnViajar=findViewById(R.id.btnViajar);
        this.btnNovoCaso=findViewById(R.id.btnNovoCaso);

        //RecyclerView
        this.rvLocaisVisitados=findViewById(R.id.rvLocaisVisitados);
    }
}