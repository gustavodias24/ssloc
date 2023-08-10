package com.example.ssloc.adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ssloc.R;
import com.example.ssloc.databinding.VeiculoAdminLayoutBinding;
import com.example.ssloc.models.VeiculoModel;

import java.util.List;

public class VeiculoAdapter  extends RecyclerView.Adapter<VeiculoAdapter.MyViewHolder>{

    List<VeiculoModel> lista;
    Context c;

    public VeiculoAdapter(List<VeiculoModel> lista, Context c) {
        this.lista = lista;
        this.c = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.veiculo_admin_layout,parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        VeiculoModel veiculoModel = lista.get(position);

        if ( veiculoModel.disponivel ){
            holder.dispon.setText("Diponível: sim");
        }else{
            holder.dispon.setText("Diponível: não");
        }

        holder.descricao.setText(veiculoModel.descricao);

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder {

        TextView descricao, dispon;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            descricao = itemView.findViewById(R.id.textViewDescricao);
            dispon = itemView.findViewById(R.id.textViewDisponibilidade);
        }
    }
}
