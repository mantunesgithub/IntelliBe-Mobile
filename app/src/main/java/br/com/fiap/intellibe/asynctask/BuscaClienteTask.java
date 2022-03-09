package br.com.fiap.intellibe.asynctask;

import android.os.AsyncTask;

import java.util.List;

import br.com.fiap.intellibe.database.dao.ClienteDAO;
import br.com.fiap.intellibe.model.Cliente;
import br.com.fiap.intellibe.ui.adapter.ListaClienteAdapter;

public class BuscaClienteTask extends AsyncTask<Void, Void, List<Cliente>> {

    private final ClienteDAO dao;
    private final ListaClienteAdapter adapter;

    public BuscaClienteTask(ClienteDAO dao, ListaClienteAdapter adapter) {
        this.dao = dao;
        this.adapter = adapter;
    }

    @Override
    protected List<Cliente> doInBackground(Void[] objects) {
        return dao.todos();
    }

    @Override
    protected void onPostExecute(List<Cliente> todosClientes) {
        super.onPostExecute(todosClientes);
        adapter.atualiza(todosClientes);
    }
}
