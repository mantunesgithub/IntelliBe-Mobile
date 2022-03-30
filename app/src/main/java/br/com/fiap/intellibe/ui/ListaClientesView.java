package br.com.fiap.intellibe.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.MenuItem;
import android.widget.Toast;
import java.util.List;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;
import br.com.fiap.intellibe.R;
import br.com.fiap.intellibe.asynctask.BuscaTodosTelefonesDoClienteTask;
import br.com.fiap.intellibe.database.ClienteDatabase;
import br.com.fiap.intellibe.database.dao.TelefoneDAO;
import br.com.fiap.intellibe.model.Cliente;
import br.com.fiap.intellibe.model.Telefone;
import br.com.fiap.intellibe.model.TipoTelefone;
import br.com.fiap.intellibe.repository.ClienteRepository;
import br.com.fiap.intellibe.ui.recyclerview.adapter.ListaClientesAdapter;

public class ListaClientesView {
    public static final String MSG_ERRO_BUSCA_CLIENTES =
                               "Não foi possivel carregar clientes novos";
    public static final String REMOVENDO_CLIENTE =
                               "Removendo Cliente";
    public static final String TEM_CERTEZA_QUE_QUER_REMOVER_O_CLIENTE =
                               "Tem certeza que quer remover o Cliente ";
    public static final String SIM = "Sim";
    public static final String NÃO = "Não";
    public static final String HTTPS = "https://";
    public static final String HTTPS_SITE = "https://";
    public static final String NÃO_FOI_POSSIVEL_REMOVER_CLIENTE =
                               "Não foi possivel remover cliente";
    public static final String GEO_0_0_Q = "geo:0,0?q=";
    public static final String SMS = "sms:";

    private ClienteRepository repository;
    private final ListaClientesAdapter adapter;
    private final RecyclerView listaDeCliente;
    private final Context context;

    public ListaClientesView(Context context, ListaClientesAdapter adapter,
                             RecyclerView listaDeCliente)  {
        this.context = context;
        this.adapter = adapter;
        this.listaDeCliente = listaDeCliente;
    }
    public void buscaClientes() {

        repository = new ClienteRepository(context);
        repository.buscaClientesInternos(new ClienteRepository
                                        .DadosCarregadosCallback<List<Cliente>>() {
            @Override
            public void quandoSucesso(List<Cliente> clientesNovos) {
                adapter.atualiza(clientesNovos);
            }
            @Override
            public void quandoFalha(String erro) {
                mostraErro(MSG_ERRO_BUSCA_CLIENTES);
            }
        });
    }
    public void mostraErro(String mensagem) {
        Toast.makeText(context, mensagem,
                Toast.LENGTH_SHORT).show();
    }
    private void remove(Cliente cliente) {

        repository = new ClienteRepository(context);
        repository.remove(cliente, new ClienteRepository
                .DadosCarregadosCallback<Void>() {
            @Override
            public void quandoSucesso(Void resultado) {
                adapter.remove(cliente);
            }
            @Override
            public void quandoFalha(String erro) {
                mostraErro(NÃO_FOI_POSSIVEL_REMOVER_CLIENTE);
            }
        });
    }
    public void selecionaItemMenu(MenuItem item) {

        int position = ((ListaClientesAdapter) listaDeCliente.getAdapter()).getPosicao();
        List<Cliente> cliente = adapter.getClientes();
        Cliente clienteEscolhido = cliente.get(position);

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
                .setTitle(REMOVENDO_CLIENTE)
                .setMessage(TEM_CERTEZA_QUE_QUER_REMOVER_O_CLIENTE + cliente.getNomeCliente() +
                            " ?")
                .setPositiveButton(SIM,
                        (dialogInterface, i) -> {
                            remove(cliente);
                        })
                .setNegativeButton(NÃO,
                        null).show();
    }
    private void  visitaSite(MenuItem item, Cliente clienteEscolhido) {

        Intent intentSite = new Intent(Intent.ACTION_VIEW);
        String site = clienteEscolhido.getDescricaoEmail();
        if (!site.startsWith(HTTPS)) {
            site = HTTPS_SITE + site;
        }
        intentSite.setData(Uri.parse(site));
        context.startActivity(intentSite);
    }
    private void enviaSMS(MenuItem item, Cliente clienteEscolhido) {

        TelefoneDAO daoTelefone = ClienteDatabase.getInstance(context)
                .getTelefoneDAO();
        new BuscaTodosTelefonesDoClienteTask(daoTelefone, clienteEscolhido,
                telefones -> {

                    ListaClientesView.this.vinculaClienteTelefone(telefones, clienteEscolhido);
                    Intent intentSMS = new Intent(Intent.ACTION_VIEW);
                    intentSMS.setData(Uri.parse(SMS + clienteEscolhido.getTelefoneCelular()));
                    //item.setIntent(intentSMS);
                    context.startActivity(intentSMS);
                }).execute();

    }
    private void visualizaMapa(MenuItem item, Cliente clienteEscolhido) {

        Intent intentMapa = new Intent(Intent.ACTION_VIEW);
        intentMapa.setData(Uri.parse(GEO_0_0_Q + clienteEscolhido.getDescricaoEndereco() +
                                            " " + clienteEscolhido.getCidade()));
        context.startActivity(intentMapa);
    }

    private void efetuaLigacao(Cliente clienteEscolhido) {

        TelefoneDAO daoTelefone = ClienteDatabase.getInstance(context)
                .getTelefoneDAO();
        new BuscaTodosTelefonesDoClienteTask(daoTelefone, clienteEscolhido,
                telefones -> {

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
