package br.ufjf.dcc196.rafael.agente008.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.ufjf.dcc196.rafael.agente008.R;
import br.ufjf.dcc196.rafael.agente008.entities.Localizacao;

@SuppressLint("NotifyDataSetChanged")
public class LocalizacaoAdapter extends RecyclerView.Adapter<LocalizacaoAdapter.LocalizacaoViewHolder> {

    private List<Localizacao> localizacoes;
    private OnLocalizacaoClickListener listener;

    public interface OnLocalizacaoClickListener {
        void onLocalizacaoClick(View source, int position);
    }

    public LocalizacaoAdapter(List<Localizacao> localizacoes, OnLocalizacaoClickListener listener) {
        this.localizacoes = localizacoes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LocalizacaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context contexto = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(contexto);
        View localizacaoView = inflater.inflate(R.layout.localizacao_layout, parent, false);
        LocalizacaoViewHolder holder = new LocalizacaoViewHolder(localizacaoView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull LocalizacaoViewHolder holder, int position) {
        Localizacao localizacao = this.localizacoes.get(position);
        holder.tvLocalizacao.setText(localizacao.toString());
    }

    @Override
    public int getItemCount() {
        return this.localizacoes.size();
    }

    public class LocalizacaoViewHolder extends RecyclerView.ViewHolder {
        private TextView tvLocalizacao;

        public LocalizacaoViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvLocalizacao = itemView.findViewById(R.id.tvLocalizacao);
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    listener.onLocalizacaoClick(v, getAdapterPosition());
                }
            });
        }
    }
    public Localizacao getLocalizacao(int position){
        return this.localizacoes.get(position);
    }
}