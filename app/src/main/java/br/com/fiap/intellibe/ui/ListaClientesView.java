package br.com.fiap.intellibe.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import br.com.fiap.intellibe.asynctask.BuscaClienteTask;
import br.com.fiap.intellibe.asynctask.RemoveClienteTask;
import br.com.fiap.intellibe.database.ClienteDatabase;
import br.com.fiap.intellibe.database.dao.ClienteDAO;
import br.com.fiap.intellibe.model.Cliente;
import br.com.fiap.intellibe.ui.adapter.ListaClienteAdapter;


public class ListaClientesView {

    private final ListaClienteAdapter adapter;
    private final ClienteDAO dao;
    private final Context context;

    public ListaClientesView(Context context) {
        this.context = context;
        this.adapter = new ListaClienteAdapter(this.context);
        dao = ClienteDatabase.getInstance(context)
                .getClienteDAO();
    }

    public void confirmaRemocao(final MenuItem item) {
        new AlertDialog
                .Builder(context)
                .setTitle("Removendo Cliente")
                .setMessage("Tem certeza que quer remover o Cliente?")
                .setPositiveButton("Sim", (dialogInterface, i) -> {
                    AdapterView.AdapterContextMenuInfo menuInfo =
                            (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                    Cliente clienteEscolhido = adapter.getItem(menuInfo.position);
                    remove(clienteEscolhido);
                })
                .setNegativeButton("Não", null)
                .show();
    }

    public void atualizaClientes() {
        new BuscaClienteTask(dao, adapter).execute();
    }

    private void remove(Cliente cliente) {
        new RemoveClienteTask(dao, adapter, cliente).execute();
    }

    public void configuraAdapter(ListView listaDeClientes) {
        listaDeClientes.setAdapter(adapter);
    }
}
