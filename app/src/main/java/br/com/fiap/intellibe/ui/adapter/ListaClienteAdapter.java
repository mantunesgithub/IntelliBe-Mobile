package br.com.fiap.intellibe.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.fiap.intellibe.R;
import br.com.fiap.intellibe.asynctask.BuscaPrimeiroTelefoneDoClienteTask;
import br.com.fiap.intellibe.database.ClienteDatabase;
import br.com.fiap.intellibe.database.dao.TelefoneDAO;
import br.com.fiap.intellibe.model.Cliente;
import br.com.fiap.intellibe.util.UtilCnpjCpf;

public class ListaClienteAdapter extends BaseAdapter {

    private final List<Cliente> clientes = new ArrayList<>();
    private final Context context;
    private final TelefoneDAO dao;

    public ListaClienteAdapter(Context context) {
        this.context = context;
        dao = ClienteDatabase.getInstance(context).getTelefoneDAO();
    }

    @Override
    public int getCount() {
        return clientes.size();
    }

    @Override
    public Cliente getItem(int posicao) {
        return clientes.get(posicao);
    }

    @Override
    public long getItemId(int posicao) {
        return clientes.get(posicao).getCnpjOuCpf();
    }

    @Override
    public View getView(int posicao, View view, ViewGroup viewGroup) {
        View viewCriada = criaView(viewGroup);
        Cliente clienteDevolvido = clientes.get(posicao);
        vincula(viewCriada, clienteDevolvido);
        return viewCriada;
    }

    private void vincula(View view, Cliente cliente) {
        TextView nome = view.findViewById(R.id.item_cliente_nome);
        nome.setText(cliente.getNome());

        TextView cnpjCpf = view.findViewById(R.id.item_cliente_cnpj_cpf);
        cnpjCpf.setText(UtilCnpjCpf.formatCPForCPNJ(cliente.getCnpjOuCpf(), false));

        ImageView foto = view.findViewById(R.id.item_cliente_foto);
        String caminhoFoto = cliente.getCaminhoFoto();
        if (caminhoFoto != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(caminhoFoto);
            Bitmap bitmapReduzido = Bitmap.createScaledBitmap
                    (bitmap, 100, 100, true);
            foto.setImageBitmap(bitmapReduzido);
            foto.setScaleType(ImageView.ScaleType.FIT_XY);
        }
    }

    private View criaView(ViewGroup viewGroup) {
        return LayoutInflater
                .from(context)
                .inflate(R.layout.item_cliente, viewGroup, false);
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
}
