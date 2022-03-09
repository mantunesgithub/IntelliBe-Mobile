package br.com.fiap.intellibe.database.converter;

import java.util.Calendar;

import androidx.room.TypeConverter;

public class ConversorCalendar {

    @TypeConverter
    public Long paraLong(Calendar valor){
        if(valor != null){
            return valor.getTimeInMillis();
        }
        return null;
    }

    @TypeConverter
    public Calendar paraCalendar(Long valor){
        Calendar momentoAtual = Calendar.getInstance();
        if(valor != null){
            momentoAtual.setTimeInMillis(valor);
        }
        return momentoAtual;
    }

}
