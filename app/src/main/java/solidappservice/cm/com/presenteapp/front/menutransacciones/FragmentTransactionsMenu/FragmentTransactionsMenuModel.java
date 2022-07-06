package solidappservice.cm.com.presenteapp.front.menutransacciones.FragmentTransactionsMenu;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.parametrosemail.ResponseParametrosEmail;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseParametrosAPP;
import solidappservice.cm.com.presenteapp.entities.tarjetapresente.response.ResponseDependenciasAsociado;
import solidappservice.cm.com.presenteapp.rest.NetworkHelper;
import solidappservice.cm.com.presenteapp.rest.retrofit.apipresente.ApiPresente;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 17/09/2021.
 */
public class FragmentTransactionsMenuModel implements FragmentTransactionsMenuContract.Model{

    @Override
    public void getButtonStateAdvanceSalary(final FragmentTransactionsMenuContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.DIRECCION_WS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<ResponseParametrosEmail>> call = service.getButtonStateAdvanceSalary();
            call.enqueue(new Callback<BaseResponse<ResponseParametrosEmail>>() {

                @Override
                public void onResponse(Call<BaseResponse<ResponseParametrosEmail>> call, Response<BaseResponse<ResponseParametrosEmail>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredToken(response);
                        }else if(response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()){
                            listener.onErrorStateAdvanceSalary(response);
                        }else{
                            listener.onSuccessStateAdvanceSalary(response);
                        }
                    } else {
                        listener.onErrorStateAdvanceSalary(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<ResponseParametrosEmail>> call, Throwable t) {
                    if(t instanceof IOException){
                        listener.onFailureStateAdvanceSalary(t, true);
                    }else{
                        listener.onFailureStateAdvanceSalary(t, false);
                    }
                }
            });
        } catch (Exception e) {
            listener.onErrorStateAdvanceSalary(null);
        }
    }

    @Override
    public void getButtonActionAdvanceSalary(final FragmentTransactionsMenuContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.DIRECCION_WS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<ResponseParametrosEmail>> call = service.getButtonActionAdvanceSalary();
            call.enqueue(new Callback<BaseResponse<ResponseParametrosEmail>>() {

                @Override
                public void onResponse(Call<BaseResponse<ResponseParametrosEmail>> call, Response<BaseResponse<ResponseParametrosEmail>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredToken(response);
                        }else if(response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()){
                            listener.onErrorActionAdvanceSalary(response);
                        }else{
                            listener.onSuccessActionAdvanceSalary(response);
                        }
                    } else {
                        listener.onErrorActionAdvanceSalary(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<ResponseParametrosEmail>> call, Throwable t) {
                    if(t instanceof IOException){
                        listener.onFailureActionAdvanceSalary(t, true);
                    }else{
                        listener.onFailureActionAdvanceSalary(t, false);
                    }
                }
            });
        } catch (Exception e) {
            listener.onErrorActionAdvanceSalary(null);
        }
    }

    @Override
    public void getAssociatedDependency(BaseRequest body, final FragmentTransactionsMenuContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.DIRECCION_WS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<ResponseDependenciasAsociado>> call = service.getAssociatedDependence(body);
            call.enqueue(new Callback<BaseResponse<ResponseDependenciasAsociado>>() {

                @Override
                public void onResponse(Call<BaseResponse<ResponseDependenciasAsociado>> call, Response<BaseResponse<ResponseDependenciasAsociado>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredToken(response);
                        }else if(response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()){
                            listener.onErrorAssociatedDependency(response);
                        }else{
                            listener.onSuccessAssociatedDependency(response);
                        }
                    } else {
                        listener.onErrorAssociatedDependency(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<ResponseDependenciasAsociado>> call, Throwable t) {
                    if(t instanceof IOException){
                        listener.onFailureAssociatedDependency(t, true);
                    }else{
                        listener.onFailureAssociatedDependency(t, false);
                    }
                }
            });
        } catch (Exception e) {
            listener.onErrorAssociatedDependency(null);
        }
    }


    @Override
    public void getButtonStateTransfers(FragmentTransactionsMenuContract.APIListener listener) {
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
    public void getButtonStateSavings(FragmentTransactionsMenuContract.APIListener listener) {
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
    public void getButtonStatePaymentCredits(FragmentTransactionsMenuContract.APIListener listener) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkHelper.DIRECCION_WS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiPresente service = retrofit.create(ApiPresente.class);
            Call<BaseResponse<ResponseParametrosAPP>> call = service.getButtonStatePaymentCredits();
            call.enqueue(new Callback<BaseResponse<ResponseParametrosAPP>>() {

                @Override
                public void onResponse(Call<BaseResponse<ResponseParametrosAPP>> call, Response<BaseResponse<ResponseParametrosAPP>> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
                            listener.onExpiredToken(response);
                        }else if(response.body().getMensajeErrorUsuario() != null && !response.body().getMensajeErrorUsuario().isEmpty()){
                            listener.onErrorButtonStatePaymentCredits(response);
                        }else{
                            listener.onSuccessButtonStatePaymentCredits(response);
                        }
                    } else {
                        listener.onErrorButtonStatePaymentCredits(null);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<ResponseParametrosAPP>> call, Throwable t) {
                    listener.onErrorButtonStatePaymentCredits(null);
                }
            });
        } catch (Exception e) {
            listener.onErrorButtonStatePaymentCredits(null);
        }
    }

//    @Override
//    public void getButtonStateResorts(final FragmentTransactionsMenuContract.APIListener listener) {
//        try {
//            Retrofit retrofit = new Retrofit.Builder()
//                    .baseUrl(NetworkHelper.DIRECCION_WS)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//
//            ApiPresente service = retrofit.create(ApiPresente.class);
//            Call<BaseResponse<ResponseParametrosAPP>> call = service.getButtonStateResorts();
//            call.enqueue(new Callback<BaseResponse<ResponseParametrosAPP>>() {
//
//                @Override
//                public void onResponse(Call<BaseResponse<ResponseParametrosAPP>> call, Response<BaseResponse<ResponseParametrosAPP>> response) {
//                    if (response.isSuccessful()) {
//                        if(response.body().getErrorToken() != null && !response.body().getErrorToken().isEmpty()){
//                            listener.onExpiredToken(response);
//                        }else if(response.body().getDescripcionError() != null && !response.body().getDescripcionError().isEmpty()){
//                            listener.onError(response);
//                        }else{
//                            listener.onSuccessButtonStateResorts(response);
//                        }
//                    } else {
//                        response.body().setDescripcionError("Error: "+response.code());
//                        listener.onError(response);
//                    }
//                }
//                @Override
//                public void onFailure(Call<BaseResponse<ResponseParametrosAPP>> call, Throwable t) {
//                    listener.onFailure(t);
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
