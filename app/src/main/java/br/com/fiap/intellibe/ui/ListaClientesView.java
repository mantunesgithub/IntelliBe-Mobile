package br.com.fiap.intellibe.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import androidx.core.app.ActivityCompat;
import br.com.fiap.intellibe.R;
import br.com.fiap.intellibe.asynctask.BuscaClienteTask;
import br.com.fiap.intellibe.asynctask.BuscaTodosTelefonesDoClienteTask;
import br.com.fiap.intellibe.asynctask.RemoveClienteTask;
import br.com.fiap.intellibe.database.ClienteDatabase;
import br.com.fiap.intellibe.database.dao.ClienteDAO;
import br.com.fiap.intellibe.database.dao.TelefoneDAO;
import br.com.fiap.intellibe.model.Cliente;
import br.com.fiap.intellibe.model.Telefone;
import br.com.fiap.intellibe.model.TipoTelefone;
import br.com.fiap.intellibe.ui.activity.FormularioClienteActivity;
import br.com.fiap.intellibe.ui.adapter.ListaClienteAdapter;

public class ListaClientesView {

    private final ListaClienteAdapter adapter;
    private final ClienteDAO dao;
//    private final TelefoneDAO daoTelefone;
    private final Context context;
    private List<Telefone> telefonesCliente;

    public ListaClientesView(Context context) {
        this.context = context;
        this.adapter = new ListaClienteAdapter(this.context);
        dao = ClienteDatabase.getInstance(context)
                .getClienteDAO();
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

    public void selecionaItemMenu(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Cliente clienteEscolhido = adapter.getItem(menuInfo.position);

        switch (item.getItemId()) {
            case R.id.activity_lista_clientes_menu_remover:
                confirmaRemocao(item, clienteEscolhido);
                break;
            case R.id.activity_lista_clientes_menu_site:
                visitaSite(item, clienteEscolhido);
                break;
            case R.id.activity_lista_clientes_menu_sms:
                enviaSMS(item, clienteEscolhido);
                break;
            case R.id.activity_lista_clientes_menu_mapa:
                visualizaMapa(item, clienteEscolhido);
                break;
            case R.id.activity_lista_clientes_menu_ligar:
                efetuaLigacao(clienteEscolhido);
                break;
        }
    }
    public void confirmaRemocao(final MenuItem item, final Cliente cliente) {

        new AlertDialog
                .Builder(context)
                .setTitle("Removendo Cliente")
                .setMessage("Tem certeza que quer remover o Cliente?")
                .setPositiveButton("Sim",
                        (dialogInterface, i) -> {
                            remove(cliente);
                        })
                .setNegativeButton("Não",
                        null).show();
    }

    private void  visitaSite(MenuItem item, Cliente clienteEscolhido) {

        Intent intentSite = new Intent(Intent.ACTION_VIEW);
        String site = clienteEscolhido.getSite();
        if (!site.startsWith("https://")) {
            site = "https://" + site;
        }
        intentSite.setData(Uri.parse(site));
        context.startActivity(intentSite);
    }

    private void enviaSMS(MenuItem item, Cliente clienteEscolhido) {
        TelefoneDAO daoTelefone = ClienteDatabase.getInstance(context)
                .getTelefoneDAO();
        new BuscaTodosTelefonesDoClienteTask(daoTelefone, clienteEscolhido,
                new BuscaTodosTelefonesDoClienteTask.TelefonesDoAlunoEncontradosListener() {
                    @Override
                    public void quandoEncontrados(List<Telefone> telefones) {

                        vinculaClienteTelefone(telefones, clienteEscolhido);
                        Intent intentSMS = new Intent(Intent.ACTION_VIEW);
                        intentSMS.setData(Uri.parse("sms:" + clienteEscolhido.getTelefoneCelular()));
                        //item.setIntent(intentSMS);
                        context.startActivity(intentSMS);
                    }
                }).execute();

    }

    private void visualizaMapa(MenuItem item, Cliente clienteEscolhido) {

        Intent intentMapa = new Intent(Intent.ACTION_VIEW);
        intentMapa.setData(Uri.parse("geo:0,0?q=" + clienteEscolhido.getEndereco() +
                                            " " + clienteEscolhido.getCidade()));
        //item.setIntent(intentMapa);
        context.startActivity(intentMapa);
    }

    private void efetuaLigacao(Cliente clienteEscolhido) {
        TelefoneDAO daoTelefone = ClienteDatabase.getInstance(context)
                .getTelefoneDAO();
        new BuscaTodosTelefonesDoClienteTask(daoTelefone, clienteEscolhido,
                new BuscaTodosTelefonesDoClienteTask.TelefonesDoAlunoEncontradosListener() {
                    @Override
                    public void quandoEncontrados(List<Telefone> telefones) {

                        vinculaClienteTelefone(telefones, clienteEscolhido);
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions((Activity) context,
                                    new String[]{Manifest.permission.CALL_PHONE}, 123);
                        } else {
                            Intent intentLigar = new Intent(Intent.ACTION_CALL);
                            intentLigar.setData(Uri.parse("tel:" + clienteEscolhido.getTelefoneFixo()));
                            context.startActivity(intentLigar);
                        }
                    }
                }).execute();
    }

    private void vinculaClienteTelefone(List<Telefone> telefones, Cliente clienteEscolhido) {
        for (Telefone telefone :
                telefones) {
            if (telefone.getTipo() == TipoTelefone.FIXO) {
                clienteEscolhido.setTelefoneFixo(telefone.getNumero());
            } else {
                if (telefone.getTipo() == TipoTelefone.CELULAR) {
                    clienteEscolhido.setTelefoneCelular(telefone.getNumero());
                } else {
                    clienteEscolhido.setTelefoneComercial(telefone.getNumero());
                }
            }
        }
    }
}
