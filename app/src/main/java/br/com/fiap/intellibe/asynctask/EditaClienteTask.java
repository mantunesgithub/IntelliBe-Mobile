package br.com.fiap.intellibe.asynctask;

import java.util.List;

import br.com.fiap.intellibe.database.dao.ClienteDAO;
import br.com.fiap.intellibe.database.dao.TelefoneDAO;
import br.com.fiap.intellibe.model.Cliente;
import br.com.fiap.intellibe.model.Telefone;
import br.com.fiap.intellibe.model.TipoTelefone;

public class EditaClienteTask extends BaseClienteComTelefoneTask {

    private final ClienteDAO clienteDAO;
    private final Cliente cliente;
    private final Telefone telefoneFixo;
    private final Telefone telefoneCelular;
    private final Telefone telefoneComercial;
    private final TelefoneDAO telefoneDAO;
    private final List<Telefone> telefonesDoCliente;

    public EditaClienteTask(ClienteDAO clienteDAO,
                            Cliente cliente,
                            Telefone telefoneFixo,
                            Telefone telefoneCelular,
                            Telefone telefoneComercial,
                            TelefoneDAO telefoneDAO,
                            List<Telefone> telefonesDoCliente, FinalizadaListener listener) {
        super(listener);
        this.clienteDAO = clienteDAO;
        this.cliente = cliente;
        this.telefoneFixo = telefoneFixo;
        this.telefoneCelular = telefoneCelular;
        this.telefoneComercial = telefoneComercial;
        this.telefoneDAO = telefoneDAO;
        this.telefonesDoCliente = telefonesDoCliente;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        clienteDAO.edita(cliente);

        vinculaClienteComTelefone(cliente.getCnpjOuCpf(),
                telefoneFixo, telefoneCelular, telefoneComercial);

        atualizaIdsDosTelefones(telefoneFixo, telefoneCelular, telefoneComercial);

        telefoneDAO.atualiza(telefoneFixo, telefoneCelular, telefoneComercial);
        return null;
    }

    private void atualizaIdsDosTelefones(Telefone telefoneFixo, Telefone telefoneCelular,
                                         Telefone telefoneComercial) {
        for (Telefone telefone :
                telefonesDoCliente) {
            if (telefone.getTipo() == TipoTelefone.FIXO) {
                telefoneFixo.setId(telefone.getId());
            } else {
                if (telefone.getTipo() == TipoTelefone.CELULAR) {
                    telefoneCelular.setId(telefone.getId());
                } else {
                    telefoneComercial.setId(telefone.getId());
                }
            }
        }

    }
}