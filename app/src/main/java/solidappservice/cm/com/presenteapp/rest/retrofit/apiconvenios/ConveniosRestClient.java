package solidappservice.cm.com.presenteapp.rest.retrofit.apiconvenios;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConveniosRestClient {

    public static final String TOKEN_EXCEPTION = "token_exception";
    public static final String RESULT_OK = "OK";
    public static final String SERVER_ERROR = "Respuesta inválida desde el servidor.";

    private String API_BASE_URL = "https://apps.presente.com.co/servicioconvenios/api/Convenios/";

    //private OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
            .connectTimeout(40, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build();

    private Retrofit.Builder builder = new
            Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    private Retrofit retrofit = builder.
            client(okHttpClient)
            .build();

    private ApiConvenios client =  retrofit.create(ApiConvenios.class);

    // Fetch resume.
//    public Call<ResumenResult> resumeCall(String token,
//                                          String identificacion){
//        return client.getResumen(token, identificacion);
//    }


//    public Call<ProductoResult> productsCall(String token,
//                                                                      String identificacion,
//                                                                      String idConvenio){
//        return client.listarProductos(token, identificacion, idConvenio);
//    }

//    public Call<SolicitudProductoResult> requestNewProductCall(String token,
//                                                               String identificacion,
//                                                               RequestSolicitudProducto solicitudProducto){
//
//        return client.solicitarProducto(token, identificacion, solicitudProducto);
//    }

    // Execute the call asynchronously. Get a positive or negative callback.
    /*call.enqueue(new Callback<ConveniosResult>() {

        @Override
        public void onResponse(Call<ConveniosResult> call, Response<ConveniosResult> response) {
            // The network call was a success and we got a response
            // TODO: use the repository list and display it
        }

        @Override
        public void onFailure(Call<ConveniosResult> call, Throwable t) {
            // the network call was a failure
            // TODO: handle error
        }
    });*/
}
