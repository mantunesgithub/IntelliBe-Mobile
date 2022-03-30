package br.com.fiap.intellibe.repository;

import android.content.Context;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import br.com.fiap.intellibe.asynctask.BaseAsyncTask;
import br.com.fiap.intellibe.database.ClienteDatabase;
import br.com.fiap.intellibe.database.dao.ClienteDAO;
import br.com.fiap.intellibe.database.dao.TelefoneDAO;
import br.com.fiap.intellibe.model.Cliente;
import br.com.fiap.intellibe.model.Telefone;
import br.com.fiap.intellibe.model.TipoTelefone;
import br.com.fiap.intellibe.retrofit.ClienteRetrofit;
import br.com.fiap.intellibe.retrofit.callback.CallbackcomRetorno;
import br.com.fiap.intellibe.retrofit.callback.CallbackSemRetorno;
import br.com.fiap.intellibe.retrofit.service.ClienteService;
import retrofit2.Call;

public class ClienteRepository {
    private final ClienteDAO clienteDAO;
    private ClienteService clienteService;
    private Telefone telefoneFixo;
    private Telefone telefoneCelular;
    private Telefone telefoneComercial;

    public ClienteRepository(Context context) {
        this.clienteDAO = ClienteDatabase.getInstance(context).getClienteDAO();
        clienteService = new ClienteRetrofit().getClienteService();
    }

    public void buscaClientesInternos(DadosCarregadosCallback<List<Cliente>> callback) {

        new BaseAsyncTask<>(clienteDAO::todos,
                resultado -> {
                    callback.quandoSucesso(resultado);

                    ClienteRepository.this.buscaClientesNaApi(callback);
                }).execute();
    }

    private void buscaClientesNaApi(DadosCarregadosCallback<List<Cliente>> callback) {

        Call<List<Cliente>> call = clienteService.buscaTodos();
        call.enqueue(new CallbackcomRetorno<>(new CallbackcomRetorno
                                                .RespostaCallBack<List<Cliente>>() {
            @Override
            public void quandoSucesso(List<Cliente> clientesNovos) {
                atualizaInterno(clientesNovos, callback);
            }

            @Override
            public void quandoFalha(String erro) {
                callback.quandoFalha(erro);
            }
        }));
    }

    public void atualizaInterno(List<Cliente> clientes,
                                DadosCarregadosCallback<List<Cliente>> callback) {
        new BaseAsyncTask<>(() -> {
            clienteDAO.salvaLista(clientes);
            return clienteDAO.todos();

        }, callback::quandoSucesso)
                .execute();
    }

    public void salvaNaApiEsalvaInterno(Cliente cliente, TelefoneDAO telefoneDAO,
                                        Telefone telefoneFixo, Telefone telefoneCelular,
                                        Telefone telefoneComercial,
                                        DadosCarregadosCallback<Cliente> callback) {
        this.telefoneFixo = telefoneFixo;
        this.telefoneCelular = telefoneCelular;
        this.telefoneComercial = telefoneComercial;
        cliente.setTelefoneFixo(telefoneFixo.getNumero());
        cliente.setTelefoneCelular(telefoneCelular.getNumero());
        cliente.setTelefoneComercial(telefoneComercial.getNumero());

        salvaNaApi(cliente, telefoneDAO, callback);
    }

    private void salvaNaApi(Cliente cliente, TelefoneDAO telefoneDAO,
                            DadosCarregadosCallback<Cliente> callback) {

        Call<Cliente> call = clienteService.salva(cliente);
        call.enqueue(new CallbackcomRetorno<>(new CallbackcomRetorno
                                                .RespostaCallBack<Cliente>() {
            @Override
            public void quandoSucesso(Cliente clienteSalvo) {
                salvaInterno(clienteSalvo, telefoneDAO, callback);
            }

            @Override
            public void quandoFalha(String erro) {
                callback.quandoFalha(erro);
            }
        }));
    }

    private void salvaInterno(Cliente cliente, TelefoneDAO telefoneDAO,
                                DadosCarregadosCallback<Cliente> callback) {
        new BaseAsyncTask<>(() -> {

            long cnpjOuCpf = clienteDAO.salva(cliente).longValue();

            ClienteRepository.this.vinculaClienteComTelefone(cnpjOuCpf,
                                telefoneFixo, telefoneCelular, telefoneComercial);
                                telefoneDAO.salva(telefoneFixo, telefoneCelular, telefoneComercial);

            return clienteDAO.buscaClientePorCnpjCpf(cnpjOuCpf);
        }, callback::quandoSucesso)
                .execute();
    }

    public void editaNaApiEeditaInterno(Cliente cliente, TelefoneDAO telefoneDAO,
                                Telefone telefoneFixo, Telefone telefoneCelular,
                                Telefone telefoneComercial,List<Telefone> telefonesDoCliente,
                                DadosCarregadosCallback<Cliente> callback) {

        editaNaApi(cliente, telefoneDAO, telefoneFixo, telefoneCelular,
                telefoneComercial, telefonesDoCliente, callback);
    }

    public void editaNaApi(Cliente cliente, TelefoneDAO telefoneDAO, Telefone telefoneFixo,
                               Telefone telefoneCelular, Telefone telefoneComercial,
                               List<Telefone> telefonesDoCliente,
                               DadosCarregadosCallback<Cliente> callback) {

        Call<Cliente> call = clienteService.edita(cliente.getCnpjOuCpf(), cliente);
        call.enqueue(new CallbackcomRetorno<>(new CallbackcomRetorno.RespostaCallBack<Cliente>() {
            @Override
            public void quandoSucesso(Cliente resultado) {

                editaInterno(cliente, telefoneFixo, telefoneCelular, telefoneComercial,
                               telefonesDoCliente, telefoneDAO, callback);
            }

            @Override
            public void quandoFalha(String erro) {
                callback.quandoFalha(erro);
            }
        }));
    }

    @NotNull
    private void editaInterno(Cliente cliente, Telefone telefoneFixo, Telefone telefoneCelular,
                              Telefone telefoneComercial, List<Telefone> telefonesDoCliente,
                              TelefoneDAO telefoneDAO,
                              DadosCarregadosCallback<Cliente> callback) {
        new BaseAsyncTask<>(() -> {

            clienteDAO.edita(cliente);
            vinculaClienteComTelefone(cliente.getCnpjOuCpf(), telefoneFixo, telefoneCelular,
                            telefoneComercial);
            atualizaIdsDosTelefones(telefoneFixo, telefoneCelular, telefoneComercial,
                            telefonesDoCliente);
            telefoneDAO.atualiza(telefoneFixo, telefoneCelular, telefoneComercial);
            return cliente;
        }, callback::quandoSucesso)
                .execute();
    }

    public interface DadosCarregadosCallback<T> {
        void quandoSucesso(T resutado);

        void quandoFalha(String erro);
    }

    private void vinculaClienteComTelefone(Long cnpjOuCpf, Telefone... telefones) {

        for (Telefone telefone :
                telefones) {
            telefone.setClienteCnpjOuCpf(cnpjOuCpf);
        }
    }

    private void atualizaIdsDosTelefones(Telefone telefoneFixo, Telefone telefoneCelular,
                            Telefone telefoneComercial, List<Telefone> telefonesDoCliente) {
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
    public void remove(Cliente cliente, DadosCarregadosCallback<Void> callback) {
//        removeInterno(cliente, callback);        //para remover somente interno
        removeNaApi(cliente, callback);
    }
    public void removeNaApi(Cliente cliente,
                            DadosCarregadosCallback<Void> callback) {

        Call<Void> call = clienteService.remove(cliente.getCnpjOuCpf());
        call.enqueue(new CallbackSemRetorno(
            new CallbackSemRetorno.RespostaCallback() {
                @Override
                public void quandoSucesso() {
                    removeInterno(cliente, callback);
                }
                @Override
                public void quandoFalha(String erro) {
                    callback.quandoFalha(erro);
                }
            }));
    }
    public void removeInterno(Cliente cliente,
                              DadosCarregadosCallback<Void> callback) {
        new BaseAsyncTask<>(() -> {
            clienteDAO.remove(cliente);
            return null;
        }, callback::quandoSucesso)
                .execute();
    }
}