package br.ufjf.dcc196.rafael.agente008.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.ufjf.dcc196.rafael.agente008.AppDatabase;
import br.ufjf.dcc196.rafael.agente008.R;
import br.ufjf.dcc196.rafael.agente008.entities.Localizacao;

public class LocalizacaoAdapter extends RecyclerView.Adapter<LocalizacaoAdapter.CidadeViewHolder> {

    private List<Localizacao> localizacoes;

    public LocalizacaoAdapter(List<Localizacao> localizacoes){
        this.localizacoes=localizacoes;
    }

    @NonNull
    @Override
    public CidadeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context contexto = parent.getContext();
        LayoutInflater inflater=LayoutInflater.from(contexto);
        View cidadeView = inflater.inflate(R.layout.cidade_layout,parent,false);
        CidadeViewHolder holder = new CidadeViewHolder(cidadeView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CidadeViewHolder holder, int position) {
        Localizacao localizacao=this.localizacoes.get(position);
        holder.tvUfLayout.setText(localizacao.getEstado());
        holder.tvCidadeLayout.setText(localizacao.getCidade());

    }

    @Override
    public int getItemCount() {
        return this.localizacoes.size();
    }

    public class CidadeViewHolder extends RecyclerView.ViewHolder{
        private TextView tvUfLayout, tvCidadeLayout;

        public CidadeViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvUfLayout=itemView.findViewById(R.id.tvUfLayout);
            this.tvCidadeLayout=itemView.findViewById(R.id.tvCidadeLayout);
        }
    }
}
