package br.com.fiap.intellibe.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import br.com.fiap.intellibe.R;
import br.com.fiap.intellibe.asynctask.BuscaTodosTelefonesDoClienteTask;
import br.com.fiap.intellibe.asynctask.EditaClienteTask;
import br.com.fiap.intellibe.asynctask.SalvaClienteTask;
import br.com.fiap.intellibe.database.ClienteDatabase;
import br.com.fiap.intellibe.database.dao.ClienteDAO;
import br.com.fiap.intellibe.database.dao.TelefoneDAO;
import br.com.fiap.intellibe.model.Cliente;
import br.com.fiap.intellibe.model.DominioException;
import br.com.fiap.intellibe.model.Telefone;
import br.com.fiap.intellibe.model.TipoTelefone;
import br.com.fiap.intellibe.util.UtilCnpjCpf;

import static br.com.fiap.intellibe.ui.activity.ConstantesActivities.CHAVE_CLIENTE;

public class FormularioClienteActivity extends AppCompatActivity {

    private static final String TITULO_APPBAR_NOVO_CLIENTE = "Novo cliente";
    private static final String TITULO_APPBAR_EDITA_CLIENTE = "Edita cliente";
    private ClienteDAO clienteDAO;
    private TelefoneDAO telefoneDAO;
    private Cliente cliente;
    private List<Telefone> telefonesDoCliente;
    private ImageView campoClienteFoto;
    private Button campoClienteBotaoFoto;
    private EditText campoNome;
    private EditText campoTelefoneFixo;
    private EditText campoTelefoneCelular;
    private EditText campoTelefoneComercial;
    private EditText campoSite;
    private EditText campoTipoPessoa;
    private EditText campoCnpjCpf;
    private EditText campoCep;
    private EditText campoEndereco;
    private EditText campoComplemento;
    private EditText campoBairro;
    private EditText campoCidade;
    private EditText campoEstado;
    private EditText campoPais;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_cliente);
        ClienteDatabase database = ClienteDatabase.getInstance(this);
        clienteDAO = database.getClienteDAO();
        telefoneDAO = database.getTelefoneDAO();
        inicializacaoDosCampos();
        carregaCliente();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater()
                .inflate(R.menu.activity_formulario_cliente_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        if (itemId == R.id.activity_formulario_cliente_menu_salvar) {
            finalizaFormulario();
        }
        return super.onOptionsItemSelected(item);
    }
    private void carregaCliente() {

        Intent dados = getIntent();
        if (dados.hasExtra(CHAVE_CLIENTE)) {
            setTitle(TITULO_APPBAR_EDITA_CLIENTE);
            cliente = (Cliente) dados.getSerializableExtra(CHAVE_CLIENTE);
            preencheCampos();
        } else {
            setTitle(TITULO_APPBAR_NOVO_CLIENTE);
            cliente = new Cliente();
        }
    }

    private void preencheCampos() {

        System.out.println("Cliente tipo de pessoa= " + cliente);
        campoTipoPessoa.setText(cliente.getTipoPessoa().equals(1)? "PF" : "PJ");

//      Verifica se cnpj ou cpf válido, se valido formata cnpj/cpf para exibir tela

        campoCnpjCpf.setText(UtilCnpjCpf.formatCPForCPNJ(cliente.getCnpjOuCpf(), false));

        campoNome.setText(cliente.getNome());
        campoSite.setText(cliente.getSite());
        campoCep.setText(cliente.getCep());
        campoEndereco.setText(cliente.getEndereco());
        campoComplemento.setText(cliente.getComplemento());
        campoBairro.setText(cliente.getBairro());
        campoCidade.setText(cliente.getCidade());
        campoEstado.setText(cliente.getEstado());
        campoPais.setText(cliente.getPais());
        preencheCamposDeTelefone();
    }

    private void preencheCamposDeTelefone() {
        new BuscaTodosTelefonesDoClienteTask(telefoneDAO, cliente, telefones -> {
            this.telefonesDoCliente = telefones;
            for (Telefone telefone :
                    telefonesDoCliente) {
                if (telefone.getTipo() == TipoTelefone.FIXO) {
                    campoTelefoneFixo.setText(telefone.getNumero());
                } else {
                    if (telefone.getTipo() == TipoTelefone.CELULAR) {
                        campoTelefoneCelular.setText(telefone.getNumero());
                    } else {
                        campoTelefoneComercial.setText(telefone.getNumero());
                    }
                }
            }
        }).execute();
    }

    private void finalizaFormulario() {

        try {
            preencheCliente();

            Telefone telefoneFixo = criaTelefone(campoTelefoneFixo, TipoTelefone.FIXO);
            Telefone telefoneCelular = criaTelefone(campoTelefoneCelular, TipoTelefone.CELULAR);
            Telefone telefoneComercial = criaTelefone(campoTelefoneComercial, TipoTelefone.COMERCIAL);

            Intent dados = getIntent();
            if (dados.hasExtra(CHAVE_CLIENTE)) {
                editaCliente(telefoneFixo, telefoneCelular, telefoneComercial);
            } else {
                salvaCliente(telefoneFixo, telefoneCelular, telefoneComercial);
            }

        } catch (DominioException e) {
            Toast.makeText(this, "Campo Invalido - " + e.getMessage() ,
                    Toast.LENGTH_SHORT).show();
        } catch (RuntimeException e) {
            Toast.makeText(this, "Erro Inesperado - " + e.getMessage() ,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private Telefone criaTelefone(EditText campoTelefoneFixo, TipoTelefone fixo) {
        String numeroFixo = campoTelefoneFixo.getText().toString();
        return new Telefone(numeroFixo,
                fixo);
    }

    private void salvaCliente(Telefone telefoneFixo, Telefone telefoneCelular,
                              Telefone telefoneComercial) {
        new SalvaClienteTask(clienteDAO, cliente, telefoneFixo, telefoneComercial,
                telefoneCelular, telefoneDAO, this::finish)
                .execute();
    }

    private void editaCliente(Telefone telefoneFixo, Telefone telefoneCelular,
                              Telefone telefoneComercial) {
        new EditaClienteTask(clienteDAO, cliente, telefoneFixo, telefoneCelular, telefoneComercial,
                telefoneDAO, telefonesDoCliente, this::finish)
                .execute();
    }

    private void inicializacaoDosCampos() {
        campoClienteFoto = findViewById(R.id.formulario_cliente_foto);
        campoClienteBotaoFoto = findViewById(R.id.formulario_cliente_botao_foto);
        campoTipoPessoa = findViewById(R.id.activity_formulario_cliente_tipo_pessoa);
        campoCnpjCpf = findViewById(R.id.activity_formulario_cliente_cnpj_cpf);
        campoNome = findViewById(R.id.activity_formulario_cliente_nome);
        campoSite = findViewById(R.id.activity_formulario_cliente_site);
        campoTelefoneFixo = findViewById(R.id.activity_formulario_cliente_telefone_fixo);
        campoTelefoneCelular = findViewById(R.id.activity_formulario_cliente_telefone_celular);
        campoTelefoneComercial=findViewById(R.id.activity_formulario_cliente_telefone_comercial);
        campoCep = findViewById(R.id.activity_formulario_cliente_cep);
        campoEndereco = findViewById(R.id.activity_formulario_cliente_endereco);
        campoComplemento = findViewById(R.id.activity_formulario_cliente_complemento);
        campoBairro = findViewById(R.id.activity_formulario_cliente_bairro);
        campoCidade = findViewById(R.id.activity_formulario_cliente_cidade);
        campoEstado = findViewById(R.id.activity_formulario_cliente_estado);
        campoPais = findViewById(R.id.activity_formulario_cliente_pais);
    }

    private void preencheCliente()  {

        String tipoPessoa = campoTipoPessoa.getText().toString();
        Integer tipoPessoaConvertido = cliente.checkTipoPessoa(tipoPessoa);
        cliente.setTipoPessoa(tipoPessoaConvertido);

        String cnpjOuCpf = campoCnpjCpf.getText().toString();
        Long cnpjCpfConvertido = cliente.checkCnpjCpf(cnpjOuCpf);
        cliente.setCnpjOuCpf(cnpjCpfConvertido);

        cliente.setNome(campoNome.getText().toString());
        cliente.setSite(campoSite.getText().toString());
        cliente.setCep(campoCep.getText().toString());
        cliente.setEndereco(campoEndereco.getText().toString());
        cliente.setComplemento(campoComplemento.getText().toString());
        cliente.setBairro(campoBairro.getText().toString());
        cliente.setCidade(campoCidade.getText().toString());
        cliente.setEstado(campoEstado.getText().toString());
        cliente.setPais(campoPais.getText().toString());
    }
}
