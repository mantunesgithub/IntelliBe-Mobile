package br.com.fiap.intellibe.asynctask;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.List;

import br.com.fiap.intellibe.R;
import br.com.fiap.intellibe.database.dao.ClienteDAO;
import br.com.fiap.intellibe.model.Cliente;

public class BuscaClientePorTelefoneTask extends AsyncTask<Void, Void, List<Cliente>> {

    private final ClienteDAO dao;
    private final String telefone;
    private final Context context;
    private Cliente Cliente;

    public BuscaClientePorTelefoneTask(ClienteDAO dao, String telefone, Context context) {
        this.dao = dao;
        this.telefone = telefone;
        this.context = context;
    }

    @Override
    protected List<Cliente> doInBackground(Void... voids) {
        List<Cliente> cliente = dao.buscaClientePorTelefone(telefone);
         return cliente;
    }

    @Override
    protected void onPostExecute(List<Cliente> cliente) {
        super.onPostExecute(cliente);
        if (cliente != null) {
            Toast.makeText(context, "Atenção chegou um SMS do Cliente :" ,
                    Toast.LENGTH_SHORT).show();
            MediaPlayer mp = MediaPlayer.create(context, R.raw.msg);
            mp.start();
        } else {
            Toast.makeText(context, "Cliente não encontrado", Toast.LENGTH_SHORT).show();
        }
    }
}