package br.com.fiap.intellibe.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import br.com.fiap.intellibe.R;
import br.com.fiap.intellibe.model.Cliente;
import br.com.fiap.intellibe.ui.ListaClientesView;
import br.com.fiap.intellibe.ui.recyclerview.adapter.ListaClientesAdapter;
import br.com.fiap.intellibe.ui.recyclerview.adapter.listener.OnItemClickListener;

public class ListaClientesActivity extends AppCompatActivity {

    private static final String TITULO_APPBAR = "IntelliBe - Lista de Clientes";
    private ListaClientesView listaClientesView;
    private ListaClientesAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_clientes);
        setTitle(TITULO_APPBAR);
        configuraFabNovoCliente();
        configuraRecyclerview();
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater()
                .inflate(R.menu.activity_lista_clientes_menu, menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {

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
        listaClientesView.buscaClientes();
    }

    private void configuraRecyclerview() {

        RecyclerView listaDeCliente = findViewById(R.id.activity_lista_clientes_recyclerview);
        adapter = new ListaClientesAdapter(this);
        listaClientesView = new ListaClientesView(this, adapter, listaDeCliente);
        listaDeCliente.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Cliente cliente, int posicao) {
                ListaClientesActivity.this
                        .abreFormularioModoEditaCliente(cliente, posicao);
            }
        });
        registerForContextMenu(listaDeCliente);
    }
    private void abreFormularioModoEditaCliente(Cliente cliente, int posicao) {

        Intent vaiParaFormularioActivity = new Intent(ListaClientesActivity.this,
                FormularioClienteActivity.class);
        vaiParaFormularioActivity.putExtra(ConstantesActivities.CHAVE_CLIENTE, cliente);
        startActivity(vaiParaFormularioActivity);
    }

}
