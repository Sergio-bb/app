package solidappservice.cm.com.presenteapp.rest.retrofit.apinequi;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import solidappservice.cm.com.presenteapp.entities.nequi.request.RequestPaymentDispersion;
import solidappservice.cm.com.presenteapp.entities.nequi.request.RequestReversePaymentDispersion;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequestNequi;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponseNequi;
import solidappservice.cm.com.presenteapp.entities.nequi.request.RequestCheckStatusPaymentSuscription;
import solidappservice.cm.com.presenteapp.entities.nequi.request.RequestGetDataCommerceQR;
import solidappservice.cm.com.presenteapp.entities.nequi.request.RequestPaymentQR;
import solidappservice.cm.com.presenteapp.entities.nequi.request.RequestPaymentSuscritpion;
import solidappservice.cm.com.presenteapp.entities.nequi.request.RequestReversePaymentSuscription;
import solidappservice.cm.com.presenteapp.entities.nequi.request.RequestSendSubscription;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseCheckStatusPaymentSuscription;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseConsultaSuscripcion;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseConsultarTopes;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseNequiGeneral;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseGetAuthorizacionBalance;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseMakePaymentQR;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseNequiBalance;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseDataCommerceQR;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponsePaymentSuscription;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponsePrueba;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseReversePaymentDispersion;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseReversePaymentSuscriptions;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseSendAuthorizacionBalance;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseSuscriptionStatus;

public interface ApiNequi {

    //GENERAL
    @GET("nequiGeneral/16")
    Call<ResponseNequiGeneral> getAccountsAvailable();
    @GET("nequiGeneral/34")
    Call<ResponseNequiGeneral> getStatusSectionQR();
    @GET("nequiGeneral/35")
    Call<ResponseNequiGeneral> getStatusSectionPaymentDispersiones();
    @GET("nequiGeneral/36")
    Call<ResponseNequiGeneral> getStatusSectionPaymentSuscriptions();
    @GET("nequiGeneral/37")
    Call<ResponseNequiGeneral> getStatusSectionNequiBalance();
    @GET("nequiGeneral/38")
    Call<ResponseNequiGeneral> getStatusSectionSuscription();

    @GET("nequiGeneral/17")
    Call<ResponseNequiGeneral> getMaximumTranferValuesGeneral();

    @GET("nequiGeneral/46")
    Call<ResponseNequiGeneral> getTimeExpiration();

    @POST("consumoNequi/consultaSuscripcion")
    Call<BaseResponseNequi<ResponseConsultaSuscripcion>> consultarSuscripcion(@Body BaseRequest body);

    @POST("consumoNequi/suscripcion")
    Call<BaseResponseNequi<ResponseConsultaSuscripcion>> vincularCuenta(@Body RequestSendSubscription body);

    @POST("consumoNequi/envioNequi")
    Call<BaseResponseNequi<Object>> tranferCuentanequi(@Body RequestPaymentDispersion body);

    @POST("consumoNequi/reversionNequi")
    Call<BaseResponseNequi<ResponseReversePaymentDispersion>> reverseNequiDispersion(@Body RequestReversePaymentDispersion body);

    @POST("consumoNequi/consultaDataQR")
    Call<BaseResponseNequi<ResponseDataCommerceQR>> datospagoQR(@Body RequestGetDataCommerceQR requestBase);

    //Consultar toppes
    @POST("consumoNequi/topesTransferencias")
    Call<BaseResponseNequi<ResponseConsultarTopes>> getMaximumTranferValues(@Body BaseRequest body);

    @POST("nequiSuscription/makeTransfer")
    Call<BaseResponseNequi<ResponsePaymentSuscription>> makePaymentSuscription(@Body RequestPaymentSuscritpion body);

    @POST("nequiSuscription/reverseTransfer")
    Call<BaseResponseNequi<ResponseReversePaymentSuscriptions>> reverseNequiSubscriptions(@Body RequestReversePaymentSuscription body);

    @POST("nequiSuscription/checkTransferStatus")
    Call<BaseResponseNequi<ResponseCheckStatusPaymentSuscription>> checkStatusPaymentSubscription(@Body RequestCheckStatusPaymentSuscription body);

    @POST("nequiSuscription/validateUncompletedTransfers")
    Call<BaseResponseNequi<String>> getIncompleteSubscriptionPayments(@Body BaseRequest body);

    @POST("consumoNequi/solAutoConsultaSaldo")
    Call<BaseResponseNequi<ResponseSendAuthorizacionBalance>> sendAuthorizationNequiBalance(@Body BaseRequestNequi body);

    @POST("consumoNequi/consAutoConsultaSaldo")
    Call<BaseResponseNequi<ResponseGetAuthorizacionBalance>> getAuthorizationNequiBalance(@Body BaseRequestNequi body);

    @POST("consumoNequi/consultaSaldoNequi")
    Call<BaseResponseNequi<ResponseNequiBalance>> getNequiBalance(@Body BaseRequestNequi body);

    @POST("consumoNequi/pagoQR")
    Call<BaseResponseNequi<ResponseMakePaymentQR>> pagoQR(@Body RequestPaymentQR requestBase);

    @POST("nequiRegistro/Externo")
    Call<BaseResponseNequi<ResponseSuscriptionStatus>>getSuscriptionDate(@Body BaseRequestNequi body);

    @GET("nequiCobroT/NequiCobroTInt/Dispersion")
    Call<Integer> getCostoPorOperacion();

    @POST("consumoNequi/consultaSuscripcion")
    Call<BaseResponseNequi<ResponseConsultaSuscripcion>>getSuscription(@Body BaseRequest body);
}
