package solidappservice.cm.com.presenteapp.front.menuprincipal.FragmentHome;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import solidappservice.cm.com.presenteapp.entities.banercomercial.response.ResponseBannerComercial;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequestNequi;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponseNequi;
import solidappservice.cm.com.presenteapp.entities.mensajes.response.ResponseObtenerMensajes;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseConsultaSuscripcion;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseNequiGeneral;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseSuscriptionStatus;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseParametrosAPP;
import solidappservice.cm.com.presenteapp.front.base.main.ActivityMainContract;
import solidappservice.cm.com.presenteapp.front.menutransacciones.FragmentTransactionsMenu.FragmentTransactionsMenuContract;
import solidappservice.cm.com.presenteapp.rest.NetworkHelper;
import solidappservice.cm.com.presenteapp.rest.retrofit.apinequi.ApiNequi;
import solidappservice.cm.com.presenteapp.rest.retrofit.apipresente.ApiPresente;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 17/09/2021.
 */
public class FragmentHomeModel implements FragmentHomeContract.Model{

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(NetworkHelper.DIRECCION_WS)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private ApiPresente service = retrofit.create(ApiPresente.class);

    private Retrofit retrofitNequi = new Retrofit.Builder()
            .baseUrl(NetworkHelper.NEQUI_WS)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private ApiNequi serviceNequi = retrofitNequi.create(ApiNequi.class);

    @Override
    public void getButtonStateAgreements(FragmentHomeContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.DIRECCION_WS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<ResponseParametrosAPP>> call = service.getButtonStateAgreements();
            call.enqueue(new Callback<BaseResponse<ResponseParametrosAPP>>() {

                @Override
                public void onResponse(Call<BaseResponse<ResponseParametrosAPP>> call, Response<BaseResponse<ResponseParametrosAPP>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredToken(response);
                        }else if(response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()){
                            listener.onErrorButtonStateAgreements(response);
                        }else{
                            listener.onSuccessButtonStateAgreements(response);
                        }
                    } else {
                        listener.onErrorButtonStateAgreements(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<ResponseParametrosAPP>> call, Throwable t) {
                    listener.onErrorButtonStateAgreements(null);
                }
            });
        } catch (Exception e) {
            listener.onErrorButtonStateAgreements(null);
        }
    }

    @Override
    public void getButtonStateResorts(final FragmentHomeContract.APIListener listener) {
        try {
            Call<BaseResponse<ResponseParametrosAPP>> call = service.getButtonStateResorts();
            call.enqueue(new Callback<BaseResponse<ResponseParametrosAPP>>() {

                @Override
                public void onResponse(Call<BaseResponse<ResponseParametrosAPP>> call, Response<BaseResponse<ResponseParametrosAPP>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredToken(response);
                        }else if(response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()){
                            listener.onErrorButtonStateResorts(response);
                        }else{
                            listener.onSuccessButtonStateResorts(response);
                        }
                    } else {
                        listener.onErrorButtonStateResorts(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<ResponseParametrosAPP>> call, Throwable t) {
                    listener.onErrorButtonStateResorts(null);
                }
            });
        } catch (Exception e) {
            listener.onErrorButtonStateResorts(null);
        }
    }

    @Override
    public void getButtonStateTransfers(FragmentHomeContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.DIRECCION_WS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<ResponseParametrosAPP>> call = service.getButtonStateTransfers();
            call.enqueue(new Callback<BaseResponse<ResponseParametrosAPP>>() {

                @Override
                public void onResponse(Call<BaseResponse<ResponseParametrosAPP>> call, Response<BaseResponse<ResponseParametrosAPP>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredToken(response);
                        }else if(response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()){
                            listener.onErrorButtonStateTransfers(response);
                        }else{
                            listener.onSuccessButtonStateTransfers(response);

                        }
                    } else {
                        listener.onErrorButtonStateTransfers(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<ResponseParametrosAPP>> call, Throwable t) {
                    listener.onErrorButtonStateTransfers(null);
                }
            });
        } catch (Exception e) {
            listener.onErrorButtonStateTransfers(null);
        }
    }

    @Override
    public void getButtonStateSavings(FragmentHomeContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.DIRECCION_WS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<ResponseParametrosAPP>> call = service.getButtonStateSavings();
            call.enqueue(new Callback<BaseResponse<ResponseParametrosAPP>>() {

                @Override
                public void onResponse(Call<BaseResponse<ResponseParametrosAPP>> call, Response<BaseResponse<ResponseParametrosAPP>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredToken(response);
                        }else if(response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()){
                            listener.onErrorButtonStateSavings(response);
                        }else{
                            listener.onSuccessButtonStateSavings(response);
                        }
                    } else {
                        listener.onErrorButtonStateSavings(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<ResponseParametrosAPP>> call, Throwable t) {
                    listener.onErrorButtonStateSavings(null);
                }
            });
        } catch (Exception e) {
            listener.onErrorButtonStateSavings(null);
        }
    }

    @Override
    public void getMessageInbox(BaseRequest body, final FragmentHomeContract.APIListener listener) {
        try {
            Call<BaseResponse<List<ResponseObtenerMensajes>>> call = service.getMessages(body);
            call.enqueue(new Callback<BaseResponse<List<ResponseObtenerMensajes>>>() {

                @Override
                public void onResponse(Call<BaseResponse<List<ResponseObtenerMensajes>>> call, Response<BaseResponse<List<ResponseObtenerMensajes>>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredToken(response);
                        }else if(response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()){
                            listener.onErrorMessageInbox(response);
                        }else{
                            listener.onSuccessMessageInbox(response);
                        }
                    } else {
                        listener.onErrorMessageInbox(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<List<ResponseObtenerMensajes>>> call, Throwable t) {
                    listener.onErrorMessageInbox(null);
                }
            });
        } catch (Exception e) {
            listener.onErrorMessageInbox(null);
        }
    }

    @Override
    public void getCommercialBanner(BaseRequest body, final FragmentHomeContract.APIListener listener) {
        try {
            Call<BaseResponse<List<ResponseBannerComercial>>> call = service.getCommercialBanner(body);
            call.enqueue(new Callback<BaseResponse<List<ResponseBannerComercial>>>() {

                @Override
                public void onResponse(Call<BaseResponse<List<ResponseBannerComercial>>> call, Response<BaseResponse<List<ResponseBannerComercial>>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredToken(response);
                        }else if(response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()){
                            listener.onErrorCommercialBanner(response);
                        }else{
                            listener.onSuccessCommercialBanner(response);
                        }
                    } else {
                        listener.onErrorCommercialBanner(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<List<ResponseBannerComercial>>> call, Throwable t) {
                    listener.onErrorCommercialBanner(null);
                }
            });
        } catch (Exception e) {
            listener.onErrorCommercialBanner(null);
        }
    }

    @Override
    public void getSuscriptionNequi(BaseRequest body, final FragmentHomeContract.APIListener listener) {

        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.NEQUI_WS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();

            ApiNequi service = retrofit.create(ApiNequi.class);
            Call<BaseResponseNequi<ResponseConsultaSuscripcion>> call = service.consultarSuscripcion(body);
            call.enqueue(new Callback<BaseResponseNequi<ResponseConsultaSuscripcion>>() {

                @Override
                public void onResponse(Call<BaseResponseNequi<ResponseConsultaSuscripcion>> call, Response<BaseResponseNequi<ResponseConsultaSuscripcion>> response) {
                    if(response.isSuccessful()){
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredTokenNequi(response);
                        }else if(response.isSuccessful()){
                            listener.onSuccessGetSuscriptionNequi(response);
                        }else {
                            listener.onErrorGetSuscriptionNequi(response);
                        }
                    }else {
                        listener.onErrorGetSuscriptionNequi(response);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponseNequi<ResponseConsultaSuscripcion>> call, Throwable t) {
                    listener.onFailureGetSuscriptionNequi(t);

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getTimeExpiration(final FragmentHomeContract.APIListener listener ){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NetworkHelper.NEQUI_WS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiNequi service = retrofit.create(ApiNequi.class);
        try{
            Call<ResponseNequiGeneral> call = service.getTimeExpiration();
            call.enqueue(new Callback<ResponseNequiGeneral>(){
                @Override
                public void  onResponse(Call<ResponseNequiGeneral> call, Response<ResponseNequiGeneral> response) {
                    if (response.isSuccessful()) {
                        if(response.body() != null){
                            listener.onSuccessGetTimeExpiration(response);
                        }else{
                            listener.onErrorGetTimeExpiration(response);
                        }
                    }
                }
                @Override
                public void onFailure(Call<ResponseNequiGeneral> call, Throwable t) {
                    listener.onErrorGetTimeExpiration(null);
                }
            });
        }catch(Exception e){
            listener.onErrorGetTimeExpiration(null);
        }
    }

    @Override
    public void getMinutesOfSuscription(BaseRequestNequi body, final FragmentHomeContract.APIListener listener ){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NetworkHelper.NEQUI_WS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiNequi service = retrofit.create(ApiNequi.class);
        try{
            Call<BaseResponseNequi<ResponseSuscriptionStatus>> call = service.getSuscriptionDate(body);
            call.enqueue(new Callback<BaseResponseNequi<ResponseSuscriptionStatus>>(){
                @Override
                public void  onResponse(Call<BaseResponseNequi<ResponseSuscriptionStatus>> call, Response<BaseResponseNequi<ResponseSuscriptionStatus>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getResult() != null){
                            try {
                                listener.onSuccessGetTimeOfSuscription(response);
                            } catch (ParseException e) {
                                listener.onFailureGetTimeOfSuscription(response);
                            }
                        }else{
                            listener.onFailureGetTimeOfSuscription(response);
                        }
                    }
                }
                @Override
                public void onFailure(Call<BaseResponseNequi<ResponseSuscriptionStatus>> call, Throwable t) {
                    listener.onFailureGetTimeOfSuscription(null);
                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
