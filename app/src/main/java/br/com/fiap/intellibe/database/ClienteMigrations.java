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
    static final Migration[] TODAS_MIGRATIONS = {MIGRATION_6_7};
}
