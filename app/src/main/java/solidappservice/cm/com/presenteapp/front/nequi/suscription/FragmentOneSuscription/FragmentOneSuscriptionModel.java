package solidappservice.cm.com.presenteapp.front.nequi.suscription.FragmentOneSuscription;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apiresponse.ResponseConsultarDatosAsociado;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.rest.NetworkHelper;
import solidappservice.cm.com.presenteapp.rest.retrofit.apipresente.ApiPresente;

public class FragmentOneSuscriptionModel implements FragmentOneSuscriptionContract.Model {


    @Override
    public void getPersonalData(BaseRequest body, FragmentOneSuscriptionContract.APIListener listener) {
            try {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(NetworkHelper.DIRECCION_WS)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ApiPresente service = retrofit.create(ApiPresente.class);
                Call<BaseResponse<ResponseConsultarDatosAsociado>> call = service.getPersonalData(body);
                call.enqueue(new Callback<BaseResponse<ResponseConsultarDatosAsociado>>() {

                    @Override
                    public void onResponse(Call<BaseResponse<ResponseConsultarDatosAsociado>> call, Response<BaseResponse<ResponseConsultarDatosAsociado>> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()) {
                                listener.onExpiredToken(response);
                            } else if (response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()) {
                                listener.onError(response);
                            } else {
                                listener.onSuccess(response);
                            }
                        } else {
                            listener.onError(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse<ResponseConsultarDatosAsociado>> call, Throwable t) {
                        if(t instanceof IOException){
                            listener.onFailure(t, true);
                        }else{
                            listener.onFailure(t, false);
                        }
                    }
                });

            } catch (Exception e) {
                listener.onError(null);
            }
        }
    }

