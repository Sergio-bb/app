package solidappservice.cm.com.presenteapp.front.convenios.ActivityAgreements.movilexito.FragmentLandingME;

import androidx.annotation.NonNull;

import retrofit2.Response;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apiresponse.ResponseConsultarDatosAsociado;
import solidappservice.cm.com.presenteapp.entities.base.BaseRequest;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponse;
import solidappservice.cm.com.presenteapp.entities.movilexito.request.RequestEmail;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseParametrosAPP;

public class FragmentLandingMEPresenter implements FragmentLandingMEContract.Presenter,
        FragmentLandingMEContract.APIListener {

    private FragmentLandingMEView view;
    private FragmentLandingMEModel model;

    public FragmentLandingMEPresenter(@NonNull FragmentLandingMEView view, @NonNull FragmentLandingMEModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void getUrlLandingMovilExito() {
        view.showCircularProgressBar("Un momento");
        model.getUrlLandingMovilExito(this);
    }
    @Override
    public <T> void onSuccessGetUrlLandingMovilExito(Response<BaseResponse<T>> response) {
        try{
            ResponseParametrosAPP params = (ResponseParametrosAPP) response.body().getResultado();
            view.resultUrlLandingMovilExito(params);
            view.fetchAssociatedData();
        }catch(Exception ex){
            view.hideCircularProgressBar();
            view.showDataFetchError("");
        }
    }
    @Override
    public <T> void onErrorGetUrlLandingMovilExito(Response<BaseResponse<T>> response) {
        view.hideCircularProgressBar();
        view.showDataFetchError("");
    }

    @Override
    public void getAssociatedData(BaseRequest request) {
        view.showCircularProgressBar("Un momento");
        model.getAssociatedData(this, request);
    }
    @Override
    public <T> void onSuccessGetAssociatedData(Response<BaseResponse<T>> response) {
        try{
            ResponseConsultarDatosAsociado datosAsociado = (ResponseConsultarDatosAsociado) response.body().getResultado();
            view.openLandingMovilExito(datosAsociado);
        }catch(Exception ex){
            view.hideCircularProgressBar();
            view.showDataFetchError("");
        }
    }
    @Override
    public <T> void onErrorGetAssociatedData(Response<BaseResponse<T>> response) {
        view.hideCircularProgressBar();
        view.showDataFetchError("");
    }

//    @Override
//    public void postEmailFallo(String contenidoEmail) {
//        model.postEmail(this, new RequestEmail("1807",
//                "Notificacion fallo en la transaccion Movil Exito",
//                true, contenidoEmail));
//    }

    @Override
    public <T> void onExpiredToken(Response<BaseResponse<T>> response) {
        view.hideCircularProgressBar();
        view.showExpiredToken("");
    }


/*    public void mostrarMensaje(){
        view.mostrarMensajeError();
    }


    public void buscarDatosAsociado(Usuario usuario, String cedula){
        BaseRequest baseRequest = new BaseRequest();
        baseRequest.setCedula(cedula);
        //baseRequest.setCedula(usuario.getCedula());
        baseRequest.setToken(usuario.getToken());
        model.recolectarDatosAsociadoWV(view, baseRequest);
    }

    public void datosAsociado(ResponseConsultarDatosAsociado datos){
        view.setControlsAsociado(datos);
        view.cargarDatosLanding();
        view.dismissMessage();
    }

    public void goToUrl(){
        view.message();
        model.consultarUrlWebView(view);
    }

    public void setRequestBodyUrl(RequestBodyUrl requestBodyUrl){
        this.requestBodyUrl = requestBodyUrl;
        view.setControlsUrl(requestBodyUrl.getValue1());
        //view.cargarDatosLanding();
        view.goToBuscarDatosAsociado();
    }*/
}
