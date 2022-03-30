package br.com.fiap.intellibe.retrofit.callback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

import static br.com.fiap.intellibe.retrofit.callback.MensagensCallback.FALHA_DE_COMUNICAÇÃO;
import static br.com.fiap.intellibe.retrofit.callback.MensagensCallback.RESPOSTA_NÃO_BEM_SUCEDIDA;

public class CallbackcomRetorno<T> implements Callback<T> {

    private final  RespostaCallBack<T> callBack ;

    public CallbackcomRetorno(RespostaCallBack<T> callBack) {
        this.callBack = callBack;
    }
    @Override
    @EverythingIsNonNull
    public void onResponse(Call<T> call, Response<T> response) {

        if (response.isSuccessful()) {
            T resultado = response.body();
            if (resultado != null) {
                callBack.quandoSucesso(resultado);
            }
        } else {
                callBack.quandoFalha(RESPOSTA_NÃO_BEM_SUCEDIDA);
        }
    }
    @Override
    @EverythingIsNonNull
    public void onFailure(Call<T> call, Throwable t) {

        callBack.quandoFalha(FALHA_DE_COMUNICAÇÃO + t.getMessage());
    }
    public interface RespostaCallBack <T> {

        void    quandoSucesso(T resultado);
        void    quandoFalha(String erro);
    }
}
