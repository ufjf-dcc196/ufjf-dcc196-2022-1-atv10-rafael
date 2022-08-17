package br.ufjf.dcc196.rafael.agente008;
//Classe da Activity Viajar
import android.content.DialogInterface;
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

import androidx.appcompat.app.AlertDialog;
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
    private CidadeAdapter cidadeAdapter;
    private RecyclerView rvCidades;
    private AppDatabase db;
    private JogoRepository repo;
    private List<Localizacao> cidades;
    private Localizacao cidadeSelecionada;
    private Integer horasViagem;
    private Double precoViagem;
    private Agente agente;
    private Caso caso;
    public static final int RESULT_VIAJAR = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viajar);

        this.db=AppDatabase.getInstance(getApplicationContext());
        this.repo=new JogoRepository(getApplicationContext());

        buildViews();
        loadData();
        buildListeners();
    }

    //Atribuição de views para variaveis
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

    //Carrega os dados do SharedPreferences
    private void loadData(){
        this.agente=this.repo.getAgente();
        this.caso=this.repo.getCaso();
        this.tvNomeAgenteViagem.setText(agente.getNome());
        this.tvDinheiroViagem.setText("R$"+agente.getDinheiro().toString()+"0");
        this.tvLocalizacaoAtualViagem.setText(agente.getLocaisVisitados().get(agente.getLocaisVisitados().size()-1).toString());
        this.tvDiaViagem.setText(caso.getDia().toString());
        this.tvHoraViagem.setText(caso.getHora().toString());
        this.tvLocalizacaoAtualViagem.setText(agente.getLocaisVisitados().get(agente.getLocaisVisitados().size()-1).toString());
        this.tvNomeAgenteViagem.setText(agente.getNome());
        buildRv();
    }

    //Constroi e popula o RecyclerView
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
                        cidades = db.localizacaoDAO().findLocaisByEstado(spUfViagem.getSelectedItem().toString(), "Delegacia");
                    }
                }else {
                    if (agente.getLocalizacaoAtual().getLocal().equals("Aeroporto")) {
                        cidades = db.localizacaoDAO().findCidadesComAeroportoByEstadoeCidade(spUfViagem.getSelectedItem().toString(),etCidade.getText().toString() );
                    } else {
                        cidades = db.localizacaoDAO().findLocaisByCidadesAndEstado(spUfViagem.getSelectedItem().toString(), etCidade.getText().toString(), "Delegacia");
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

    //Listeners necessarios para a activity
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

    //Habilita (se possivel) o botão viajar)
    public void habilitarBotaoViajar(){
        if(this.cidadeSelecionada!=null){
            this.btnViajarViagem.setEnabled(true);
        }else{
            this.btnViajarViagem.setEnabled(false);
        }
    }

    //-Tratamento dos clicks nos botoes
    public void retornarClick(View v){
        Intent resultado = new Intent();
        setResult(-1, resultado);
        finish();

    }
    public void viajarClick(View v){

        this.agente.decrDinheiro(this.precoViagem);
        this.caso.setHora(this.caso.getHora()+this.horasViagem);
        this.agente.getLocaisVisitados().add(this.cidadeSelecionada);
        this.agente.getLocalizacaoAtual().setDica("Isso é uma delegacia!");

        Boolean mudouDia=false;
        if(this.agente.getDinheiro()<0.0){
            this.caso.setStatus(Caso.FALIU);
        }else if(this.agente.getLocalizacaoAtual().equals(this.caso.getCriminoso().getLocalizacaoAtual())){
            this.caso.setStatus(Caso.CONCLUIDO);
        }else if(this.caso.getHora()>=Caso.MAX_HORAS_TRABALHADAS_POR_DIA){
            this.caso.incrDia();
            this.caso.setHora(0);
            mudouDia=true;
        }

        this.repo.setAgente(this.agente);
        this.repo.setCaso(this.caso);

        Intent resultado = new Intent();
        setResult(RESULT_VIAJAR, resultado);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Bem Vindo!!");
        dialogBuilder.setMessage(gerarViajarMensagem(mudouDia));
        dialogBuilder.setPositiveButton("Ok!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        dialogBuilder.create();
        dialogBuilder.show();


    }

    //Geração de mensagem para o click da viagem
    private String gerarViajarMensagem(Boolean mudouDia){
        String mensagem= "Bem vindo a "+this.agente.getLocalizacaoAtual()+"!";

        if(mudouDia){
            mensagem+=" Voce atingiu 16 horas de trabalho, as proximas tarefas ocorrerão no dia seguinte";
        }

        return mensagem;

    }

}