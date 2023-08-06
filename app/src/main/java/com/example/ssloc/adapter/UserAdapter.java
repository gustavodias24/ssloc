package com.example.ssloc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ssloc.R;
import com.example.ssloc.models.UsuarioCompletoModel;
import com.example.ssloc.models.UsuarioModel;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    List<UsuarioCompletoModel> lista;
    Context c;

    public UserAdapter(List<UsuarioCompletoModel> lista, Context c) {
        this.lista = lista;
        this.c = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.usuario_admin_layout, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        UsuarioCompletoModel usuarioModel = lista.get(position);

            holder.nome.setText("Usuário: " + usuarioModel.getUsuarioModel().getLogin());
            if ( usuarioModel.getPlano() != null){
                if ( usuarioModel.getPlano().getAtivo() ){
                    holder.comprovante.setText("Comprovante: Em análise");
                }else{
                    holder.comprovante.setText("Comprovante: Ativo");
                }
            }else{
                holder.comprovante.setText("Comprovante: vazio");
            }

            if ( usuarioModel.getManu() != null){
                switch (usuarioModel.getManu().getStatus()){
                    case 1:
                        holder.manutencao.setText("Manutenção: Em análise");
                        break;
                    case 2:
                        holder.manutencao.setText("Manutenção: Confirmado");
                        break;
                    default:
                        holder.manutencao.setText("Manutenção: vazio");
                }
            }else{
                holder.manutencao.setText("Manutenção: vazio");
            }


    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder {
        TextView nome, manutencao, comprovante;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.nomeText);
            manutencao = itemView.findViewById(R.id.manutecaoText);
            comprovante = itemView.findViewById(R.id.comprovanteText);
        }
    }
}
