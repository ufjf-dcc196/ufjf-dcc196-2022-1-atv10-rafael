package br.ufjf.dcc196.rafael.agente008;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ViajarActivity extends AppCompatActivity {

    private TextView tvNomeAgenteViagem, tvDiaViagem, tvHoraViagem, tvLocalizacaoAtualViagem, tvCustoViagem, tvTempoViagem;
    private Spinner spUfViagem, spMunicipioViagem;
    private Button btnViajarViagem, btnRetornarViagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitar);
        buildViews();
    }

    private void buildViews(){
        //TextViews
        this.tvNomeAgenteViagem=findViewById(R.id.tvNomeAgenteViagem);
        this.tvDiaViagem=findViewById(R.id.tvDiaViagem);
        this.tvHoraViagem=findViewById(R.id.tvHoraViagem);
        this.tvLocalizacaoAtualViagem =findViewById(R.id.tvLocalizacaoAtualViagem);
        this.tvCustoViagem=findViewById(R.id.tvCustoViagem);
        this.tvTempoViagem=findViewById(R.id.tvTempoViagem);

        //Spinners
        this.spUfViagem=findViewById(R.id.spUfViagem);
        this.spMunicipioViagem=findViewById(R.id.spMunicipioViagem);

        //Buttons
        this.btnViajarViagem=findViewById(R.id.btnViajar);
        this.btnRetornarViagem=findViewById(R.id.btnRetornaViagem);
    }
}