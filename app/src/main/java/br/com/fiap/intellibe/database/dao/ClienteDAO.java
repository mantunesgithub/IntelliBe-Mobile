package br.com.fiap.intellibe.database.dao;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import br.com.fiap.intellibe.model.Cliente;
import br.com.fiap.intellibe.model.Telefone;

@Dao
public interface ClienteDAO {
    @Query("SELECT * FROM Cliente")
    List<Cliente> todos();

    @Query("SELECT cnpjOuCpf FROM Cliente WHERE telefoneCelular = :foneCelular LIMIT 1;" )
    List<Cliente> buscaClientePorTelefone(String foneCelular);

    @Query("SELECT * FROM Cliente WHERE cnpjOuCpf = :cnpjOuCpf;" )
    Cliente buscaClientePorCnpjCpf(long cnpjOuCpf);

    @Insert
    Long salva(Cliente cliente);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void salvaLista(List<Cliente> cliente);

    @Delete
    void remove(Cliente cliente);

    @Update
    void edita(Cliente cliente);
}
