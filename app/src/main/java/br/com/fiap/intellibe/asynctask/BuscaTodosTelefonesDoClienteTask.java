package br.com.fiap.intellibe.asynctask;


import android.os.AsyncTask;

import java.util.List;

import br.com.fiap.intellibe.database.dao.TelefoneDAO;
import br.com.fiap.intellibe.model.Cliente;
import br.com.fiap.intellibe.model.Telefone;

public class BuscaTodosTelefonesDoClienteTask extends AsyncTask<Void, Void, List<Telefone>> {

    private final TelefoneDAO telefoneDAO;
    private final Cliente cliente;
    private final TelefonesDoClienteEncontradosListener listener;

    public BuscaTodosTelefonesDoClienteTask(TelefoneDAO telefoneDAO,
                                            Cliente cliente,
                                            TelefonesDoClienteEncontradosListener listener) {
        this.telefoneDAO = telefoneDAO;
        this.cliente = cliente;
        this.listener = listener;
    }

    @Override
    protected List<Telefone> doInBackground(Void... voids) {
        return telefoneDAO.buscaTodosTelefonesDoAluno(cliente.getCnpjOuCpf());
    }

    @Override
    protected void onPostExecute(List<Telefone> telefones) {
        super.onPostExecute(telefones);
        listener.quandoEncontrados(telefones);
    }

    public interface TelefonesDoClienteEncontradosListener {
        void quandoEncontrados(List<Telefone> telefones);
    }

}
