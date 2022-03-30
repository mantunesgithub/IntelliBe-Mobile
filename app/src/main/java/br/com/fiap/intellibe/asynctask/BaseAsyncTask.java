package br.com.fiap.intellibe.asynctask;

import android.os.AsyncTask;
import android.util.Log;

public class BaseAsyncTask<T> extends AsyncTask<Void, Void, T> {

    private final ExecutaListener<T> executaListener;
    private final FinalizadaListener<T> finalizadaListener;


    public BaseAsyncTask(ExecutaListener<T> executaListener,
                         FinalizadaListener<T> finalizadaListener) {
        this.executaListener = executaListener;
        this.finalizadaListener = finalizadaListener;
        Log.d("b1", "BaseAsyncTask: construtor-executaListener " + executaListener);
        Log.d("b1.1", "BaseAsyncTask: construtor-finalizadaListener " + finalizadaListener);
    }
    @Override
    protected T doInBackground(Void... voids) {
        Log.d("b2", "doInBackground: ");
        return executaListener.quandoExecuta();
    }

    @Override
    protected void onPostExecute(T resultado) {
        super.onPostExecute(resultado);
        Log.d("b6", "onPostExecute: ");
        finalizadaListener.quandoFinalizada(resultado);
    }

    public interface ExecutaListener<T> {
        T quandoExecuta();
    }

    public interface FinalizadaListener<T> {
        void quandoFinalizada(T resultado);
    }
}
