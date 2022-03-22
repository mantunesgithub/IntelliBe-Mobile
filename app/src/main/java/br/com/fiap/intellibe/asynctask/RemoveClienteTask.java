package br.com.fiap.intellibe.asynctask;

import android.os.AsyncTask;

import br.com.fiap.intellibe.database.dao.ClienteDAO;
import br.com.fiap.intellibe.model.Cliente;
import br.com.fiap.intellibe.ui.recyclerview.adapter.ListaClientesAdapter;

public class RemoveClienteTask extends AsyncTask<Void, Void, Void> {

    private final ClienteDAO dao;
    private final ListaClientesAdapter adapter;
    private final Cliente cliente;

    public RemoveClienteTask(ClienteDAO dao,
                             ListaClientesAdapter adapter,
                             Cliente cliente) {
        this.dao = dao;
        this.adapter = adapter;
        this.cliente = cliente;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        dao.remove(cliente);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        adapter.remove(cliente);
    }

}
