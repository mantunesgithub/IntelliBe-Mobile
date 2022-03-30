package br.com.fiap.intellibe.database;

import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;


class ClienteMigrations {

    private static final Migration MIGRATION_6_7 = new Migration(6, 7) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE Cliente ADD COLUMN caminhoFoto TEXT");
        }
    };
    private static final Migration MIGRATION_7_8 = new Migration(7, 8) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `Cliente_novo` (`cnpjOuCpf` INTEGER," +
                    " `tipoCliente` TEXT, `nomeCliente` TEXT, `descricaoEmail` TEXT," +
                    " `descricaoEndereco` TEXT, `complementoEndereco` TEXT, `bairro` TEXT, `cidade` TEXT," +
                    " `estado` TEXT, `pais` TEXT, `cep` TEXT, `telefoneFixo` TEXT, `telefoneCelular` TEXT," +
                    " `telefoneComercial` TEXT, `caminhoFoto` TEXT, PRIMARY KEY(`cnpjOuCpf`))");

            // Copiar dados da tabela antiga para a nova
            database.execSQL("INSERT INTO Cliente_novo (cnpjOuCpf,tipoCliente,nomeCliente," +
                    "descricaoEmail,descricaoEndereco,complementoEndereco,\n" +
                    "bairro,cidade,estado,pais,cep,telefoneFixo,telefoneCelular,telefoneComercial," +
                    "caminhoFoto) " +
                    "SELECT cnpjOuCpf,tipoPessoa,nome,site,endereco,complemento,bairro,cidade,\n" +
                    "estado,pais,cep,telefoneFixo,telefoneCelular,telefoneComercial,\n" +
                    "caminhoFoto FROM Cliente");

            // Remove tabela antiga
            database.execSQL("DROP TABLE Cliente");

            // Renomear a tabela nova com o nome da tabela antiga
            database.execSQL("ALTER TABLE Cliente_novo RENAME TO Cliente");
        }
    };
    private static final Migration MIGRATION_8_9 = new Migration(8, 9) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("DELETE FROM Cliente ");
        }
    };


    static final Migration[] TODAS_MIGRATIONS = {MIGRATION_6_7, MIGRATION_7_8, MIGRATION_8_9};

}