package solidappservice.cm.com.presenteapp.rest.retrofit.apimovilexito;

import android.net.Network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.movilexito.request.RequestActualizarRecarga;
import solidappservice.cm.com.presenteapp.entities.movilexito.request.RequestRealizarPago;
import solidappservice.cm.com.presenteapp.entities.movilexito.request.RequestResumenTransaccion;
import solidappservice.cm.com.presenteapp.entities.movilexito.response.ResponseAccessTokenOAuthME;
import solidappservice.cm.com.presenteapp.entities.movilexito.response.ResponseAccessTokenOAuthPresenteME;
import solidappservice.cm.com.presenteapp.entities.movilexito.response.ResponseActualizarRecarga;
import solidappservice.cm.com.presenteapp.entities.movilexito.response.ResponseRealizarPago;
import solidappservice.cm.com.presenteapp.entities.movilexito.response.ResponseRealizarRecarga;
import solidappservice.cm.com.presenteapp.entities.movilexito.request.RequestEmail;
import solidappservice.cm.com.presenteapp.entities.movilexito.request.RequestRealizarRecargar;
import solidappservice.cm.com.presenteapp.entities.movilexito.response.ResponseResumenTransaccion;
import solidappservice.cm.com.presenteapp.rest.NetworkHelper;

public interface ApiMovilExito {

    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("connect/token")
    Call<ResponseAccessTokenOAuthPresenteME> getAccessTokenOAuthPresenteME(@Header("Authorization") String basicToken,
                                                            @Field("grant_type") String grantType);

    @POST("recarga/obtenerSolicitudRecarga")
    Call<BaseResponse<ResponseResumenTransaccion>> getSummaryTransaction(@Header("Authorization") String tokenBearer,
                                                                         @Body RequestResumenTransaccion body);

    @POST("recarga/iniciarSolicitudPagoUnico")
    Call<BaseResponse<ResponseRealizarPago>> makePayment(@Header("Authorization") String tokenBearer,
                                                         @Body RequestRealizarPago body);

    @POST("recarga/actualizarSolicitudRecarga")
    Call<BaseResponse<ResponseActualizarRecarga>> updatePayment(@Header("Authorization") String tokenBearer,
                                                                @Body RequestActualizarRecarga body);

    @POST("recarga/enviarCorreoFalloTransaccion")
    Call<String> emailFailedTransaction(@Header("Authorization") String tokenBearer,
                                        @Body RequestEmail email);

    @FormUrlEncoded
    @Headers({
            "Accept: application/json",
            "Content-Type: application/x-www-form-urlencoded"
    })
    @POST("identity/connect/token")
    Call<ResponseAccessTokenOAuthME> getAccessTokenOAuthME(@Field("client_id") String client_id,
                                                           @Field("client_secret") String client_secret,
                                                           @Field("grant_type") String grant_type,
                                                           @Field("scope") String scope);

    @POST(NetworkHelper.URL_ENDPOINTRECARGAS_APIMOVILEXITO)
    Call<ResponseRealizarRecarga> performLineRecharge(@Header("Authorization") String tokenBearer,
                                                       @Body RequestRealizarRecargar request);


}
