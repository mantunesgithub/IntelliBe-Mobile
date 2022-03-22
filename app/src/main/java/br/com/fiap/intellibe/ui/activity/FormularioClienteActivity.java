package br.com.fiap.intellibe.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import br.com.fiap.intellibe.BuildConfig;
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

    private static final String TITULO_APPBAR_NOVO_CLIENTE = "IntelliBe - Novo Cliente";
    private static final String TITULO_APPBAR_EDITA_CLIENTE = "IntelliBe - Editar Cliente";
    private static final int CODIGO_CAMERA = 567;
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
    private String caminhoFoto;
    private ImageView campoCaminhoFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_cliente);
        ClienteDatabase database = ClienteDatabase.getInstance(this);
        clienteDAO = database.getClienteDAO();
        telefoneDAO = database.getTelefoneDAO();
        inicializacaoDosCampos();
        carregaCliente();
        abrirCamera();
    }

    private void abrirCamera() {
        Button botaoFoto = (Button) findViewById(R.id.formulario_cliente_botao_foto);
        botaoFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                caminhoFoto = getExternalFilesDir(null) + "/" +
                        System.currentTimeMillis() + ".jpg";
                File arquivoFoto = new File(caminhoFoto);
                intentCamera.putExtra(MediaStore.EXTRA_OUTPUT,
                        FileProvider.getUriForFile(FormularioClienteActivity.this,
                                BuildConfig.APPLICATION_ID + ".clienteprovider", arquivoFoto));
                startActivityForResult(intentCamera, CODIGO_CAMERA);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CODIGO_CAMERA) {
                carregaImagem(caminhoFoto);
            }
        }
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

        campoTipoPessoa.setText(cliente.getTipoPessoa().equals(1) ? "PF" : "PJ");

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
        carregaImagem(cliente.getCaminhoFoto());
        preencheCamposDeTelefone();
    }
    private void carregaImagem(String caminhoFoto) {
        if (caminhoFoto != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(caminhoFoto);
            Bitmap bitmapReduzido = Bitmap.createScaledBitmap
                    (bitmap, 300, 300, true);
            campoCaminhoFoto.setImageBitmap(bitmapReduzido);
            campoCaminhoFoto.setScaleType(ImageView.ScaleType.FIT_XY);
            campoCaminhoFoto.setTag(caminhoFoto);
        }
    }

    private void preencheCamposDeTelefone() {
        new BuscaTodosTelefonesDoClienteTask(telefoneDAO, cliente,
                new BuscaTodosTelefonesDoClienteTask.TelefonesDoAlunoEncontradosListener() {
                    @Override
                    public void quandoEncontrados(List<Telefone> telefones) {
                        FormularioClienteActivity.this.telefonesDoCliente = telefones;
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
            Toast.makeText(this, "Campo Invalido - " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        } catch (RuntimeException e) {
            Toast.makeText(this, "Erro Inesperado - " + e.getMessage(),
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
        campoTelefoneComercial = findViewById(R.id.activity_formulario_cliente_telefone_comercial);
        campoCep = findViewById(R.id.activity_formulario_cliente_cep);
        campoEndereco = findViewById(R.id.activity_formulario_cliente_endereco);
        campoComplemento = findViewById(R.id.activity_formulario_cliente_complemento);
        campoBairro = findViewById(R.id.activity_formulario_cliente_bairro);
        campoCidade = findViewById(R.id.activity_formulario_cliente_cidade);
        campoEstado = findViewById(R.id.activity_formulario_cliente_estado);
        campoPais = findViewById(R.id.activity_formulario_cliente_pais);
        campoCaminhoFoto = findViewById(R.id.formulario_cliente_foto);
    }

    private void preencheCliente() {

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
        cliente.setCaminhoFoto((String) campoCaminhoFoto.getTag());
    }
}
