package br.com.fiap.intellibe.asynctask;

import android.os.AsyncTask;

import br.com.fiap.intellibe.model.Telefone;


abstract class BaseClienteComTelefoneTask extends AsyncTask<Void, Void, Void> {

    private final FinalizadaListener listener;

    BaseClienteComTelefoneTask(FinalizadaListener listener) {
        this.listener = listener;
    }

    void vinculaClienteComTelefone(Long cnpjOuCpf, Telefone... telefones) {
        for (Telefone telefone :
                telefones) {
            telefone.setClienteCnpjOuCpf(cnpjOuCpf);
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        listener.quandoFinalizada();
    }

    public interface FinalizadaListener {
        void quandoFinalizada();
    }

}
