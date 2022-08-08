package br.ufjf.dcc196.rafael.agente008;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class VisitarActivity extends AppCompatActivity {

    private TextView tvNomeAgenteVisita, tvDiaVisita, tvHoraVisita, tvLocalizacaoAtualVisita, tvCustoVisita, tvTempoVisita;
    private Spinner spUfVisita, spMunicipioVisita;
    private Button btnVisitarVisita, btnRetornarVisita;
    public static final int RESULT_VISITAR = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitar);

        buildViews();
        setViews();
    }
    
    private void buildViews(){
        //TextViews
        this.tvNomeAgenteVisita=findViewById(R.id.tvNomeAgenteVisita);
        this.tvDiaVisita=findViewById(R.id.tvDiaVisita);
        this.tvHoraVisita=findViewById(R.id.tvHoraVisita);
        this.tvLocalizacaoAtualVisita =findViewById(R.id.tvLocalizacaoAtualVisita);
        this.tvCustoVisita=findViewById(R.id.tvCustoVisita);
        this.tvTempoVisita=findViewById(R.id.tvTempoVisita);

        //Buttons
        this.btnVisitarVisita=findViewById(R.id.btnVisitar);
        this.btnRetornarVisita=findViewById(R.id.btnRetornaVisita);
    }

    private void setViews(){
        this.tvNomeAgenteVisita.setText(R.id.tvNomeAgente);


    }


    public void retornarClick(View v){
        Intent resultado = new Intent();
        setResult(-1, resultado);
        finish();

    }
    
}