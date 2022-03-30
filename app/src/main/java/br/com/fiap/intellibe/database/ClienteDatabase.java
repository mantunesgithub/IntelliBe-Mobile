package br.com.fiap.intellibe.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import br.com.fiap.intellibe.database.converter.ConversorCalendar;
import br.com.fiap.intellibe.database.converter.ConversorTipoTelefone;
import br.com.fiap.intellibe.database.dao.ClienteDAO;
import br.com.fiap.intellibe.database.dao.TelefoneDAO;
import br.com.fiap.intellibe.model.Cliente;
import br.com.fiap.intellibe.model.Telefone;

import static br.com.fiap.intellibe.database.ClienteMigrations.TODAS_MIGRATIONS;

@Database(entities = {Cliente.class, Telefone.class}, version = 9, exportSchema = false)
@TypeConverters({ConversorCalendar.class, ConversorTipoTelefone.class})
public abstract class ClienteDatabase extends RoomDatabase {

    private static final String NOME_BANCO_DE_DADOS = "cliente.db";
    public abstract ClienteDAO getClienteDAO();
    public abstract TelefoneDAO getTelefoneDAO();

    public static ClienteDatabase getInstance(Context context) {
        return Room
                .databaseBuilder(context, ClienteDatabase.class, NOME_BANCO_DE_DADOS)
                .addMigrations(TODAS_MIGRATIONS)
                .build();
    }
}
