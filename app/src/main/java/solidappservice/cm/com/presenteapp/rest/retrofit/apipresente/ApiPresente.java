package solidappservice.cm.com.presenteapp.rest.retrofit.apipresente;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import solidappservice.cm.com.presenteapp.entities.bottomnavigationbar.response.ResponsePreguntasFrecuente;
import solidappservice.cm.com.presenteapp.entities.dispositivo.apiresponse.ResponseValidarDispositivo;
import solidappservice.cm.com.presenteapp.entities.login.Request.RequestForgotPassword;
import solidappservice.cm.com.presenteapp.entities.login.Request.RequestLogin;
import solidappservice.cm.com.presenteapp.entities.login.Response.Usuario;
import solidappservice.cm.com.presenteapp.entities.mensajesbanner.ResponseMensajesBanner;
import solidappservice.cm.com.presenteapp.entities.imagelogin.ResponseImageLogin;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.entities.tyc.request.RequestAceptaTyC;
import solidappservice.cm.com.presenteapp.entities.tyc.response.ReponseTyC;
import solidappservice.cm.com.presenteapp.entities.pagoobligaciones.response.ResponsePagosPendientes;
import solidappservice.cm.com.presenteapp.entities.banercomercial.response.ResponseBannerComercial;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseParametrosAPP;
import solidappservice.cm.com.presenteapp.entities.solicitudahorros.response.ResponseTiposAhorro;
import solidappservice.cm.com.presenteapp.entities.centrosvacacionales.request.RequestSolicitarCentroVacacional;
import solidappservice.cm.com.presenteapp.entities.centrosvacacionales.response.ResponseCentroVacacional;
import solidappservice.cm.com.presenteapp.entities.mensajes.request.RequestActualizarMensaje;
import solidappservice.cm.com.presenteapp.entities.mensajes.response.ResponseObtenerMensajes;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.request.RequestActualizarAdelantoNomina;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.response.ResponseConsultaAdelantoNomina;
import solidappservice.cm.com.presenteapp.entities.transferencias.response.ResponseTransferenciasPendientes;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.request.RequestConsultarAdelantoNomina;
import solidappservice.cm.com.presenteapp.entities.mensajes.request.RequestEnviarMensaje;
import solidappservice.cm.com.presenteapp.entities.pagoobligaciones.request.RequestEnviarPago;
import solidappservice.cm.com.presenteapp.entities.parametrosemail.ResponseParametrosEmail;
import solidappservice.cm.com.presenteapp.entities.solicitudahorros.request.RequestEnviarSolicitudAhorro;
import solidappservice.cm.com.presenteapp.entities.transferencias.request.RequestMakeTransfer;
import solidappservice.cm.com.presenteapp.entities.transferencias.response.ResponseBanco;
import solidappservice.cm.com.presenteapp.entities.transferencias.response.ResponseCuentasInscritas;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseMovimientoProducto;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseProducto;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.request.RequestMovimientosProducto;
import solidappservice.cm.com.presenteapp.entities.tarjetapresente.request.RequestActivarTarjeta;
import solidappservice.cm.com.presenteapp.entities.tarjetapresente.request.RequestBloquearTarjeta;
import solidappservice.cm.com.presenteapp.entities.tarjetapresente.request.RequestMensajeReposicionTarjeta;
import solidappservice.cm.com.presenteapp.entities.tarjetapresente.request.RequestReposicionTarjeta;
import solidappservice.cm.com.presenteapp.entities.tarjetapresente.response.ResponseDependenciasAsociado;
import solidappservice.cm.com.presenteapp.entities.tarjetapresente.response.ResponseTarjeta;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apiresponse.*;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apirequest.*;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.request.RequestInsertarAdelantoNomina;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.request.RequestLogs;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.request.RequestNoCumple;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.request.RequestProcesarAdelantoNomina;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.response.*;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.bottomnavigationbar.response.ResponseDirectorio;
import solidappservice.cm.com.presenteapp.entities.bottomnavigationbar.response.ResponsePortafolio;
import solidappservice.cm.com.presenteapp.entities.bottomnavigationbar.response.ResponseServicios;
import solidappservice.cm.com.presenteapp.entities.dispositivo.apirequest.Dispositivo;
import solidappservice.cm.com.presenteapp.entities.dispositivo.apiresponse.ResponseRegistrarDispositivo;
import solidappservice.cm.com.presenteapp.entities.bottomnavigationbar.response.ResponseLocationsAgencies;
import solidappservice.cm.com.presenteapp.entities.tarjetapresente.response.ResponseValorReposicionTarjeta;
import solidappservice.cm.com.presenteapp.entities.transferencias.request.RequestDeleteAccount;
import solidappservice.cm.com.presenteapp.entities.transferencias.request.RequestRegisterAccount;

public interface ApiPresente {

    //Login
    @POST("login")
    Call<BaseResponse<Usuario>> validateLogin(@Body RequestLogin body);
    @POST("olvidoClave")
    Call<BaseResponse<String>> recoverPassword(@Body RequestForgotPassword body);
    @POST("dispositivo/validarDispositivo")
    Call<BaseResponse<ResponseValidarDispositivo>> validateRegisterDevice(@Body Dispositivo body);

    //Ubicaciones oficinas
    @GET("agencias")
    Call<BaseResponse<List<ResponseLocationsAgencies>>> getLocationsAgencies();
    //Directorio
    @GET("diverseInfo/directorio")
    Call<BaseResponse<List<ResponseDirectorio>>> getDirectory();
    //Directorio
    @GET("diverseInfo/portafolio")
    Call<BaseResponse<List<ResponsePortafolio>>> getPortfolioProducts();
    //Servicios
    @GET("menu/Otrosservicios")
    Call<BaseResponse<List<ResponseServicios>>> getServices();
    //Preguntas Frecuentes
    @GET("diverseInfo/preguntasFrecuentes")
    Call<BaseResponse<List<ResponsePreguntasFrecuente>>> getFrequentQuestions();

    //Splash
    @GET("diverseInfo/mensajesRespuesta")
    Call<BaseResponse<List<ResponseMensajesRespuesta>>> getResponseMessages();
    @GET("mensajesBanner")
    Call<BaseResponse<List<ResponseMensajesBanner>>> getBannerMessages();
    @GET("bannerImage")
    Call<BaseResponse<ResponseImageLogin>> getLoginImage();
    @GET("diverseInfo/versionActual")
    Call<BaseResponse<String>> getAppVersion();

    //Terminos y condiciones
    @GET("TerminosyCondiciones/obtenerTerminos")
    Call<BaseResponse<List<ReponseTyC>>> getTermsAndConditions();
    @POST("TerminosyCondiciones/insertarTerminosAceptados")
    Call<BaseResponse<String>> registerAcceptedTermsAndConditions(@Body RequestAceptaTyC body);

    //Tarjeta Presente
    @POST("tarjetas")
    Call<BaseResponse<List<ResponseTarjeta>>> getPresenteCards(@Body BaseRequest body);
    @POST("tarjetas/motivosBloqueo")
    Call<BaseResponse<List<String>>> getReasonsBlockCard(@Body BaseRequest body);
    @POST("tarjetas/cambiarEstado")
    Call<BaseResponse<String>> blockCard(@Body RequestBloquearTarjeta body);
    @POST("tarjetas/cambiarEstado")
    Call<BaseResponse<String>> activateCard(@Body RequestActivarTarjeta body);
    @POST("tarjetas/tarjetaDatosAsosiado")
    Call<BaseResponse<ResponseDependenciasAsociado>> getAssociatedDependence(@Body BaseRequest body);
    @GET("tarjetas/valorRepoTarjeta")
    Call<BaseResponse<ResponseValorReposicionTarjeta>> getReplacementCardValue();
    @POST("tarjetas/reposicionTarjeta")
    Call<BaseResponse<String>> solicityReplacementCard(@Body RequestReposicionTarjeta body);
    @POST("notificacionesInbox/enviarNotificacionInbox")
    Call<BaseResponse<String>> sendMessageCardReplacementSuccessful(@Body RequestMensajeReposicionTarjeta body);

    //Transferencias
    @POST("transferencias/cuentasInscritas")
    Call<BaseResponse<List<ResponseCuentasInscritas>>> getRegisteredAccounts(@Body BaseRequest body);
    @POST("transferencias/eliminarCuenta")
    Call<BaseResponse<String>> deleteSelectedAccounts(@Body RequestDeleteAccount body);
    @GET("transferencias/bancos")
    Call<BaseResponse<List<ResponseBanco>>> getBanks();
    @POST("transferencias/solicitarInscribirCuenta")
    Call<BaseResponse<String>> registerAccount(@Body RequestRegisterAccount body);
    @POST("transferencias/consultarTransferencias")
    Call<BaseResponse<List<ResponseTransferenciasPendientes>>> getIncompleteTransfers(@Body BaseRequest body);
    @POST("transferencias/realizarTransferencia")
    Call<BaseResponse<String>> makeTransfer(@Body RequestMakeTransfer body);

    //Pago obligaciones
    @POST("abonos/consultarPagoObligacion")
    Call<BaseResponse<List<ResponsePagosPendientes>>> getPendingPayments(@Body BaseRequest body);
    @POST("abonos/crearPagoObligacion")
    Call<BaseResponse<String>> makePayment(@Body RequestEnviarPago body);

    //Estado de cuenta
    @POST("estadoCuenta/consultarCuentas")
    Call<BaseResponse<List<ResponseProducto>>> getAccounts(@Body BaseRequest body);
    @POST("estadoCuenta/consultarMovimientosCuenta")
    Call<BaseResponse<List<ResponseMovimientoProducto>>> getMovementsAccounts(@Body RequestMovimientosProducto body);
    @GET("estadoCuenta/estadoMensajeMisAportes")
    Call<BaseResponse<ResponseParametrosEmail>> getStatusMessageMisAportes();

    //Solicitud de ahorros
    @POST("tipoAhorroDisponible")
    Call<BaseResponse<List<ResponseTiposAhorro>>> getTypesOfSavings(@Body BaseRequest body);
    @POST("crearSolicitudAhorro")
    Call<BaseResponse<String>> solicitySavings(@Body RequestEnviarSolicitudAhorro body);

    //Adelanto de Nomina
    @POST("AdelantoDeNomina/validarRequisitos")
    Call<BaseResponse<ResponseValidarRequisitos>> getValidateRequirements(@Body BaseRequest body);
    @POST("AdelantoDeNomina/obtenerTopes")
    Call<BaseResponse<ResponseTopes>> getDebtCapacity(@Body BaseRequest body);
    @POST("AdelantoDeNomina/obtenerNoCumple")
    Call<BaseResponse<List<ResponseNoCumple>>> validateReasonsNotMeetsRequirements(@Body RequestNoCumple body);
    @GET("AdelantoDeNomina/obtenerTips")
    Call<BaseResponse<List<ResponseTips>>> getTips();
    @POST("AdelantoDeNomina/registroAdelantoNomina")
    Call<BaseResponse<String>> sendLogs(@Body RequestLogs body);
    @POST("AdelantoDeNomina/obtenerMovimientos")
    Call<BaseResponse<List<ResponseMovimientos>>> getMoves(@Body BaseRequest body);
    @GET("AdelantoDeNomina/obtenerValorComision")
    Call<BaseResponse<ResponseValorComision>> getCommissionValue();
    @POST("AdelantoDeNomina/insertarAdelanto")
    Call<BaseResponse<String>> registerSalaryAdvance(@Body RequestInsertarAdelantoNomina body);
    @POST("AdelantoDeNomina/adelantar")
    Call<BaseResponse<ResponserSolicitarAdelantoNomina>> processSalaryAdvance(@Body RequestProcesarAdelantoNomina body);
    @POST("AdelantoDeNomina/adelantarConsulta")
    Call<BaseResponse<ResponseConsultaAdelantoNomina>> checkSalaryAdvance(@Body RequestConsultarAdelantoNomina body);
    @POST("AdelantoDeNomina/actualizarAdelanto")
    Call<BaseResponse<String>> updateSalaryAdvance(@Body RequestActualizarAdelantoNomina body);

    //Mensajes
    @POST("notificacionesInbox/enviarNotificacionInbox")
    Call<BaseResponse<String>> sendNotification(@Body RequestEnviarMensaje body);
    @POST("notificacionesInbox")
    Call<BaseResponse<List<ResponseObtenerMensajes>>> getMessages(@Body BaseRequest body);
    @POST("actualizarNotificacionUsuario")
    Call<BaseResponse<String>> updateStatusMessage(@Body RequestActualizarMensaje body);

    //BannerComercial Menu Principal
    @POST("mensajes/BannerMP")
    Call<BaseResponse<List<ResponseBannerComercial>>> getCommercialBanner(@Body BaseRequest body);

    //Centros vacacionales
    @POST("centrosVacacionales")
    Call<BaseResponse<List<ResponseCentroVacacional>>> getResorts(@Body BaseRequest body);
    @POST("centrosVacacionales/crearSolicitud")
    Call<BaseResponse<String>> solicityResort(@Body RequestSolicitarCentroVacacional body);

    //Estados botones transacciones
    @GET("AdelantoDeNomina/estadoAdelantoNomina")
    Call<BaseResponse<ResponseParametrosEmail>> getButtonStateAdvanceSalary();
    @GET("AdelantoDeNomina/dependenciasActivas")
    Call<BaseResponse<ResponseParametrosEmail>> getButtonActionAdvanceSalary();
    @GET("parametrosApp/obtenerParametro/"+12)
    Call<BaseResponse<ResponseParametrosAPP>> getButtonStateResorts();
    @GET("parametrosApp/obtenerParametro/"+14)
    Call<BaseResponse<ResponseParametrosAPP>> getButtonStateAgreements();
    @GET("parametrosApp/obtenerParametro/"+15)
    Call<BaseResponse<ResponseParametrosAPP>> getButtonStateSavings();
    @GET("parametrosApp/obtenerParametro/"+16)
    Call<BaseResponse<ResponseParametrosAPP>> getButtonStatePaymentCredits();
    @GET("parametrosApp/obtenerParametro/"+17)
    Call<BaseResponse<ResponseParametrosAPP>> getButtonStateReposicionTarjeta();
    @GET("parametrosApp/obtenerParametro/"+18)
    Call<BaseResponse<ResponseParametrosAPP>> getButtonStateTransfers();

    //Actualizacion de datos
    @POST("datosAsociado/consultarDatos")
    Call<BaseResponse<ResponseConsultarDatosAsociado>> getPersonalData(@Body BaseRequest body);
    @GET("creditos/obtenerUbicaciones")
    Call<BaseResponse<ResponseUbicaciones>> getLocations();
    @GET("datosAsociado/formatoDirecciones")
    Call<BaseResponse<ResponseFormatoDirecciones>> getAddressFormat();
    @POST("datosAsociado/actualizarDatos")
    Call<BaseResponse<String>> updatePersonalData(@Body RequestActualizarDatos body);
    @POST("codigo/enviarCodigo")
    Call<BaseResponse<ResponseEnviarCodigo>> sendVerificationCodeEmail(@Body RequestEnviarCodigo body);
    @POST("codigo/enviarCodigo")
    Call<BaseResponse<ResponseEnviarCodigo>> sendVerificationCodePhone(@Body RequestEnviarCodigo body);
    @POST("codigo/validarCodigo")
    Call<BaseResponse<ResponseValidarCodigo>> validateVerificationCode(@Body RequestValidarCodigo body);
    @POST("dispositivo/registrarDispositivo")
    Call<BaseResponse<ResponseRegistrarDispositivo>> registerDevice(@Body Dispositivo body);

    @Headers("Content-Type: application/json")
    @POST("MTMessage")
    Call<BaseResponse<ResponseEnviarCodigoPhone>> sendSms(@Body RequestEnviarCodigoPhone enviarCodigoPhone, @Header("Authorization") String authHeader);

}
