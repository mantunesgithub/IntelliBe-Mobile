package br.com.fiap.intellibe.asynctask;

import android.os.AsyncTask;

import br.com.fiap.intellibe.database.dao.TelefoneDAO;
import br.com.fiap.intellibe.model.Telefone;

public class BuscaPrimeiroTelefoneDoClienteTask extends AsyncTask<Void, Void, Telefone> {

    private final TelefoneDAO dao;
    private final Long cnpjOuCpf;
    private final PrimeiroTelefoneEncontradoListener listener;

    public BuscaPrimeiroTelefoneDoClienteTask(TelefoneDAO dao,
                                              Long cnpjOuCpf,
                                              PrimeiroTelefoneEncontradoListener listener) {
        this.dao = dao;
        this.cnpjOuCpf = cnpjOuCpf;
        this.listener = listener;
    }

    @Override
    protected Telefone doInBackground(Void... voids) {
        return dao.buscaPrimeiroTelefoneDoAluno(cnpjOuCpf);
    }

    @Override
    protected void onPostExecute(Telefone primeiroTelefone) {
        super.onPostExecute(primeiroTelefone);
        listener.quandoEncontrado(primeiroTelefone);
    }

    public interface PrimeiroTelefoneEncontradoListener {
        void quandoEncontrado(Telefone telefoneEncontrado);
    }

}
