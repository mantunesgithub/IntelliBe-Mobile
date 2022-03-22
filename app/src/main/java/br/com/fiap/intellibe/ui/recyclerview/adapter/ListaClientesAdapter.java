package br.com.fiap.intellibe.ui.recyclerview.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import br.com.fiap.intellibe.R;
import br.com.fiap.intellibe.database.ClienteDatabase;
import br.com.fiap.intellibe.database.dao.TelefoneDAO;
import br.com.fiap.intellibe.model.Cliente;
import br.com.fiap.intellibe.ui.recyclerview.adapter.listener.OnItemClickListener;
import br.com.fiap.intellibe.util.UtilCnpjCpf;

public class ListaClientesAdapter extends
        RecyclerView.Adapter<ListaClientesAdapter.ClienteViewHolder> {

    private final List<Cliente> clientes = new ArrayList<>();
    private final Context context;
    private final TelefoneDAO dao;
    private OnItemClickListener onItemClickListener;
    private int posicao;

    public ListaClientesAdapter(Context context) {
        this.context = context;
        dao = ClienteDatabase.getInstance(context).getTelefoneDAO();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public List<Cliente> getClientes() {
        return clientes;
    }


    @NonNull
    @Override
    public ListaClientesAdapter.ClienteViewHolder onCreateViewHolder
            (@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context)
                .inflate(R.layout.item_cliente, parent, false);
        return new ClienteViewHolder(viewCriada);
    }

    @Override
    public void onBindViewHolder(ListaClientesAdapter.ClienteViewHolder holder, int position) {
        Cliente clienteDevolvido = clientes.get(position);
        holder.vincula(clienteDevolvido);
    }
    @Override
    public int getItemCount() {
        return clientes.size();
    }

    class ClienteViewHolder extends RecyclerView.ViewHolder {

        private Cliente cliente;
        private final ImageView foto;
        private final TextView cnpjCpf;
        private final TextView nome;

        public ClienteViewHolder(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.item_cliente_nome);
            cnpjCpf = itemView.findViewById(R.id.item_cliente_cnpj_cpf);
            foto = itemView.findViewById(R.id.item_cliente_foto);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(cliente,
                            ClienteViewHolder.this.getAbsoluteAdapterPosition());
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    setPosicao(ClienteViewHolder.this.getAbsoluteAdapterPosition());
                    return false;
                }
            });
        }

        public void vincula(Cliente cliente) {
            this.cliente = cliente;
            nome.setText(cliente.getNome());
            cnpjCpf.setText(UtilCnpjCpf.formatCPForCPNJ(cliente.getCnpjOuCpf(), false));

            String caminhoFoto = cliente.getCaminhoFoto();
            if (caminhoFoto != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(caminhoFoto);
                Bitmap bitmapReduzido = Bitmap.createScaledBitmap
                        (bitmap, 100, 100, true);
                foto.setImageBitmap(bitmapReduzido);
                foto.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        }
    }

    public void atualiza(List<Cliente> clientes) {
        this.clientes.clear();
        this.clientes.addAll(clientes);
        notifyDataSetChanged();
    }

    public void remove(Cliente cliente) {
        clientes.remove(cliente);
        notifyDataSetChanged();
    }

    public int getPosicao() {
        return posicao;
    }

    public void setPosicao(int posicao) {
        this.posicao = posicao;
    }
}
