package br.com.fiap.intellibe.retrofit;

import br.com.fiap.intellibe.model.Cliente;
import br.com.fiap.intellibe.retrofit.service.ClienteService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClienteRetrofit {

    public static final String BASE_URL = "http://192.168.0.12:8085/cliente/listar/";
    private final ClienteService clienteService;

    private OkHttpClient configuraClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
    }
    public ClienteRetrofit() {
        OkHttpClient client = configuraClient();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        clienteService = retrofit.create(ClienteService.class);
    }

    public ClienteService getClienteService() {
        return clienteService;
    }
}
