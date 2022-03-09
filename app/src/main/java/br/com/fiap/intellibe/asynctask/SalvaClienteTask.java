package br.com.fiap.intellibe.asynctask;

import br.com.fiap.intellibe.database.dao.ClienteDAO;
import br.com.fiap.intellibe.database.dao.TelefoneDAO;
import br.com.fiap.intellibe.model.Cliente;
import br.com.fiap.intellibe.model.Telefone;

public class SalvaClienteTask extends BaseClienteComTelefoneTask {

    private final ClienteDAO clienteDAO;
    private final Cliente cliente;
    private final Telefone telefoneFixo;
    private final Telefone telefoneCelular;
    private final Telefone telefoneComercial;
    private final TelefoneDAO telefoneDAO;

    public SalvaClienteTask(ClienteDAO clienteDAO,
                            Cliente cliente,
                            Telefone telefoneFixo,
                            Telefone telefoneCelular,
                            Telefone telefoneComercial,
                            TelefoneDAO telefoneDAO, FinalizadaListener listener) {
        super(listener);
        this.clienteDAO = clienteDAO;
        this.cliente = cliente;
        this.telefoneFixo = telefoneFixo;
        this.telefoneCelular = telefoneCelular;
        this.telefoneComercial = telefoneComercial;
        this.telefoneDAO = telefoneDAO;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        long cnpjOuCpf = clienteDAO.salva(cliente).longValue();
        vinculaClienteComTelefone(cnpjOuCpf, telefoneFixo, telefoneCelular, telefoneComercial);
        telefoneDAO.salva(telefoneFixo, telefoneCelular, telefoneComercial);
        return null;
    }

}
