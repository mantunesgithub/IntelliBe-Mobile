package br.com.fiap.intellibe.database.dao;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import br.com.fiap.intellibe.model.Telefone;

@Dao
public interface TelefoneDAO {
    @Query("SELECT * FROM Telefone " +
            "WHERE clienteCnpjOuCpf = :cnpjOuCpf LIMIT 1")

    Telefone buscaPrimeiroTelefoneDoAluno(Long cnpjOuCpf);

    @Insert
    void salva(Telefone... telefones);

    @Query("SELECT * FROM Telefone " +
            "WHERE clienteCnpjOuCpf = :cnpjOuCpf")

    List<Telefone> buscaTodosTelefonesDoAluno(Long cnpjOuCpf);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void atualiza(Telefone... telefones);
}
