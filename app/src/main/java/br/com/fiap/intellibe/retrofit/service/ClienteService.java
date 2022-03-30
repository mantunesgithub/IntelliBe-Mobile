package br.com.fiap.intellibe.retrofit.service;

import java.util.List;

import androidx.room.Delete;
import br.com.fiap.intellibe.model.Cliente;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ClienteService {
    @GET("/cliente/listar/")
    Call <List<Cliente>> buscaTodos() ;

    @POST("/cliente/cadastrar/")
    Call<Cliente>  salva(@Body Cliente cliente) ;

    @PUT("/cliente/atualizar/{id}")
    Call<Cliente> edita(@Path("id") Long cnpjOuCpf, @Body Cliente cliente);

    @DELETE("/cliente/deletar/{id}")
    Call<Void> remove(@Path ("id") Long cnpjOuCpf);
}
