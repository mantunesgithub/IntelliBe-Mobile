package br.com.fiap.intellibe.retrofit.callback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

import static br.com.fiap.intellibe.retrofit.callback.MensagensCallback.FALHA_DE_COMUNICAÇÃO;
import static br.com.fiap.intellibe.retrofit.callback.MensagensCallback.RESPOSTA_NÃO_BEM_SUCEDIDA;

public class CallbackSemRetorno implements Callback<Void> {
    private final RespostaCallback callback;

    public CallbackSemRetorno(RespostaCallback callback) {
        this.callback = callback;
    }
    @Override
    @EverythingIsNonNull
    public void onResponse(Call<Void> call, Response<Void> response) {

        if (response.isSuccessful()){
            callback.quandoSucesso();
        } else{
            callback.quandoFalha(RESPOSTA_NÃO_BEM_SUCEDIDA);
        }
    }
    @Override
    @EverythingIsNonNull
    public void onFailure(Call<Void> call, Throwable t) {
        callback.quandoFalha(FALHA_DE_COMUNICAÇÃO + t.getMessage());
    }
    public interface RespostaCallback {
        void quandoSucesso();
        void quandoFalha(String erro);
    }
}
