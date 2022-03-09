package br.com.fiap.intellibe.database.dao;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import br.com.fiap.intellibe.model.Cliente;
@Dao
public interface ClienteDAO {
    @Insert
    Long salva(Cliente cliente);

    @Query("SELECT * FROM Cliente")
    List<Cliente> todos();

    @Delete
    void remove(Cliente cliente);

    @Update
    void edita(Cliente cliente);
}
