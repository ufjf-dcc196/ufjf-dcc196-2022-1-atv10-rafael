package br.ufjf.dcc196.rafael.agente008.Adapters;

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

public class LocalAdapter extends RecyclerView.Adapter<LocalAdapter.LocalViewHolder>{
    private List<Localizacao> localizacoes;
    private LocalAdapter.OnLocalClickListener listener;

    public interface OnLocalClickListener {
        void onLocalClick(View source, int position);
    }

    public LocalAdapter(List<Localizacao> localizacoes, LocalAdapter.OnLocalClickListener listener) {
        this.localizacoes = localizacoes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LocalAdapter.LocalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context contexto = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(contexto);
        View localView = inflater.inflate(R.layout.local_layout, parent, false);
        LocalAdapter.LocalViewHolder holder = new LocalAdapter.LocalViewHolder(localView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull LocalAdapter.LocalViewHolder holder, int position) {
        Localizacao localizacao = this.localizacoes.get(position);
        holder.tvLocal.setText(localizacao.getLocal());
    }

    @Override
    public int getItemCount() {
        return this.localizacoes.size();
    }

    public class LocalViewHolder extends RecyclerView.ViewHolder {
        private TextView tvLocal;

        public LocalViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvLocal = itemView.findViewById(R.id.tvLocal);
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    listener.onLocalClick(v, getAdapterPosition());
                }
            });
        }
    }
    public Localizacao getLocalizacao(int position){
        return this.localizacoes.get(position);
    }
}
