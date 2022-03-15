package br.com.fiap.intellibe.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import br.com.fiap.intellibe.R;
import br.com.fiap.intellibe.model.Cliente;
import br.com.fiap.intellibe.ui.ListaClientesView;

public class ListaClientesActivity extends AppCompatActivity {

    private static final String TITULO_APPBAR = "Lista de clientes";
    private ListaClientesView listaClientesView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_clientes);
        setTitle(TITULO_APPBAR);
        listaClientesView = new ListaClientesView(this);
        configuraFabNovoCliente();
        configuraLista();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater()
                .inflate(R.menu.activity_lista_clientes_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {

        listaClientesView.selecionaItemMenu(item);
        return super.onOptionsItemSelected(item);
    }
    private void configuraFabNovoCliente() {

        FloatingActionButton botaoNovoCliente =
                findViewById(R.id.activity_lista_clientes_fab_novo_cliente);
        botaoNovoCliente.setOnClickListener(view -> abreFormularioModoInsereCliente());
    }

    private void abreFormularioModoInsereCliente() {
        startActivity(new Intent(this, FormularioClienteActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        listaClientesView.atualizaClientes();
    }

    private void configuraLista() {

        ListView listaDeCliente = findViewById(R.id.activity_lista_clientes_listview);
        listaClientesView.configuraAdapter(listaDeCliente);
        configuraListenerDeCliquePorItem(listaDeCliente);
        registerForContextMenu(listaDeCliente);
    }

    private void configuraListenerDeCliquePorItem(ListView listaDeClientes) {
        listaDeClientes.setOnItemClickListener((adapterView, view, posicao, id) -> {
            Cliente clienteEscolhido = (Cliente) adapterView.getItemAtPosition(posicao);
            abreFormularioModoEditaCliente(clienteEscolhido);
        });
    }

    private void abreFormularioModoEditaCliente(Cliente cliente) {
        Intent vaiParaFormularioActivity = new Intent(ListaClientesActivity.this,
                FormularioClienteActivity.class);
        vaiParaFormularioActivity.putExtra(ConstantesActivities.CHAVE_CLIENTE, cliente);
        startActivity(vaiParaFormularioActivity);
    }

}
