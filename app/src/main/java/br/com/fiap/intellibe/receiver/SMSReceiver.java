package br.com.fiap.intellibe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telephony.SmsMessage;

import androidx.annotation.RequiresApi;
import br.com.fiap.intellibe.asynctask.BuscaClientePorTelefoneTask;
import br.com.fiap.intellibe.database.ClienteDatabase;
import br.com.fiap.intellibe.database.dao.ClienteDAO;

public class SMSReceiver extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        Object[] pdus = (Object[]) intent.getSerializableExtra("pdus");
        byte[] pdu = (byte[]) pdus[0];
        String formato = (String) intent.getSerializableExtra("format");
        SmsMessage sms = SmsMessage.createFromPdu(pdu, formato);
        String telefone = sms.getDisplayOriginatingAddress();
        ClienteDatabase database = ClienteDatabase.getInstance(context);
        ClienteDAO clienteDAO = database.getClienteDAO();

        new BuscaClientePorTelefoneTask(clienteDAO, telefone, context).execute();
    }
}
