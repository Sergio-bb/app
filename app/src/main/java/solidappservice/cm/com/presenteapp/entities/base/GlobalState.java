package solidappservice.cm.com.presenteapp.entities.base;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTabHost;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import solidappservice.cm.com.presenteapp.entities.bottomnavigationbar.response.ResponseLocationsAgencies;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseMovimientoProducto;
import solidappservice.cm.com.presenteapp.entities.nequi.dto.SuscriptionData;
import solidappservice.cm.com.presenteapp.entities.nequi.response.ResponseDataCommerceQR;
import solidappservice.cm.com.presenteapp.entities.parametrosgenerales.ResponseMensajesRespuesta;
import solidappservice.cm.com.presenteapp.entities.transferencias.response.ResponseBanco;
import solidappservice.cm.com.presenteapp.entities.centrosvacacionales.response.ResponseCentroVacacional;
import solidappservice.cm.com.presenteapp.entities.bottomnavigationbar.response.ResponseDirectorio;
import solidappservice.cm.com.presenteapp.entities.mensajesbanner.ResponseMensajesBanner;
import solidappservice.cm.com.presenteapp.entities.mensajes.response.ResponseObtenerMensajes;
import solidappservice.cm.com.presenteapp.entities.bottomnavigationbar.response.ResponsePortafolio;
import solidappservice.cm.com.presenteapp.entities.bottomnavigationbar.dto.PortafolioPadre;
import solidappservice.cm.com.presenteapp.entities.bottomnavigationbar.response.ResponsePreguntasFrecuente;
import solidappservice.cm.com.presenteapp.entities.estadocuenta.response.ResponseProducto;
import solidappservice.cm.com.presenteapp.entities.bottomnavigationbar.response.ResponseServicios;
import solidappservice.cm.com.presenteapp.entities.tarjetapresente.dto.ReposicionTarjeta;
import solidappservice.cm.com.presenteapp.entities.tarjetapresente.response.ResponseTarjeta;
import solidappservice.cm.com.presenteapp.entities.tyc.response.ReponseTyC;
import solidappservice.cm.com.presenteapp.entities.solicitudahorros.response.ResponseTiposAhorro;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apiresponse.ResponseFormatoDirecciones;
import solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apiresponse.ResponseUbicaciones;
import solidappservice.cm.com.presenteapp.entities.login.Response.Usuario;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.response.ResponseValidarRequisitos;
import solidappservice.cm.com.presenteapp.entities.convenios.dto.Resumen;
import solidappservice.cm.com.presenteapp.entities.adelantonomina.response.ResponseTopes;
import solidappservice.cm.com.presenteapp.rest.NetworkHelper;
import solidappservice.cm.com.presenteapp.tools.IFragmentCoordinator;
import solidappservice.cm.com.presenteapp.tools.ILocation;
import solidappservice.cm.com.presenteapp.tools.LocationHelper;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA
 */
public class GlobalState extends Application{

    //VARIABLES BASE
    public static final String UrlCentroAyuda = "https://www.presente.com.co/contactenos/?utm_source=app&utm_medium=cat-mas&utm_campaign=Centro-de-ayuda";
    public static final String UrlConoceGestor = "https://www.presente.com.co/conoce-tu-gestor/?utm_source=app&utm_medium=cat-mas&utm_campaign=Conoce-tu-gestor";

    //REQUESTCODE PERMISSIONS
    public static final int PERMISSION_CAMERA = 100;
    public static final int PERMISSION_GALLERY = 101;
    public static final int PERMISSION_LOCATION = 102;

    //RESULTCODE PERMISSIONS
    public static final int GALLERY = 200;

    //REQUESTCODE ACTIVITY
    public static final int ACTIVITY_AGREEMENTS = 900;
    public static final int TABS = 901;
    public static final int TERMS_AND_CONDITIONS = 902;
    public static final int TUTORIAL = 903;
    public static final int UPDATE_PERSONAL_DATA = 904;
    public static final int VALIDATE_CODE = 905;
    public static final int NEQUI = 906;
    public static final int QR = 908;
    public static final int QR_DETAIL = 909;
    public static final int CONFIRM_BUY = 910;
    public static final int VIEW_RESULT_AGREEMENT = 911;
    public static final int DIALOG_GET_NEQUI_BALANCE = 912;

    //RESULTCODE ACTIVIY
    public static final int CLOSED_SESSION = 400;
    public static final int REFUSED_AUTHORIZATION_NEQUI_BALANCE = 401;
    public static final int ERROR_AUTHORIZATION_NEQUI_BALANCE = 402;

    private IFragmentCoordinator.Pantalla fragmentActual;
    private IFragmentCoordinator.Pantalla fragmentAnterior;
    private FragmentTabHost mTabHost;

    public void setFragmentActual(IFragmentCoordinator.Pantalla fragmentActual) {
        this.fragmentActual = fragmentActual;
    }
    public IFragmentCoordinator.Pantalla getFragmentActual() {
        return fragmentActual;
    }

    public void setFragmentAnterior(IFragmentCoordinator.Pantalla fragmentAnterior) {
        this.fragmentAnterior = fragmentAnterior;
    }
    public IFragmentCoordinator.Pantalla getFragmentAnterior() {
        return fragmentAnterior;
    }

    public FragmentTabHost getmTabHost() {
        return mTabHost;
    }
    public void setmTabHost(FragmentTabHost mTabHost) {
        this.mTabHost = mTabHost;
    }

    public void reiniciarEstado() {
        usuario = null;
        statusSuscription = null;
        productos = null;
        datosSuscripcion = null;
        estadoAdelantoNomina = null;
        saldoNequi = null;
        tarjetas = null;
        tarjetaSeleccionada = null;

    }
    public boolean validarEstado() {
        return usuario != null && !TextUtils.isEmpty(usuario.getToken())
                && !TextUtils.isEmpty(usuario.getCedula())
                && !TextUtils.isEmpty(usuario.getClave())
                && !TextUtils.isEmpty(usuario.getNombreAsociado());
    }


    //VARIABLES SPLASH
    private boolean isHmsSystem;
    private String currentVersion = "A-1";
    private List<ResponseMensajesRespuesta> mensajesRespuesta;

    public boolean isHmsSystem() {
        return isHmsSystem;
    }
    public void setHmsSystem(boolean hmsSystem) {
        isHmsSystem = hmsSystem;
    }

    public void setCurrentVersion(String currentVersion) {
        this.currentVersion = currentVersion;
    }
    public String getCurrentVersion() {
        return currentVersion;
    }

    public List<ResponseMensajesRespuesta> getMensajesRespuesta() {
        return mensajesRespuesta;
    }
    public void setMensajesRespuesta(List<ResponseMensajesRespuesta> mensajesRespuesta) {
        this.mensajesRespuesta = mensajesRespuesta;
    }

    //VARIABLES ESTADO CUENTA
    private List<ResponseProducto> productos;
    private List<ResponseProducto> productosDetalle;
    private ResponseProducto productoSeleccionado;
    private List<ResponseMovimientoProducto> movimientos;

    public List<ResponseProducto> getProductos() {
        return productos;
    }
    public void setProductos(List<ResponseProducto> productos) {
        this.productos = productos;
    }

    public List<ResponseProducto> getProductosDetalle() {
        return productosDetalle;
    }
    public void setProductosDetalle(ArrayList<ResponseProducto> productos_ver_detalle) {
        this.productosDetalle = productos_ver_detalle;
    }

    public ResponseProducto getProductoSeleccionado() {
        return productoSeleccionado;
    }
    public void setProductoSeleccionado(ResponseProducto productoSeleccionado) {
        this.productoSeleccionado = productoSeleccionado;
    }

    public List<ResponseMovimientoProducto> getMovimientos() {
        return movimientos;
    }
    public void setMovimientos(List<ResponseMovimientoProducto> movimientos) {
        this.movimientos = movimientos;
    }

    //VARIABLES MENU TRANSACCIONES
    private String estadoAdelantoNomina;
    private List<String> listaDependenciasActivas;
    private String dependenciaAsociado;

    public String getEstadoAdelantoNomina() {
        return estadoAdelantoNomina;
    }
    public void setEstadoAdelantoNomina(String estadoAdelantoNomina) {
        this.estadoAdelantoNomina = estadoAdelantoNomina;
    }

    public List<String> getListaDependenciasActivas() {
        return listaDependenciasActivas;
    }
    public void setListaDependenciasActivas(List<String> listaDependenciasActivas) {
        this.listaDependenciasActivas = listaDependenciasActivas;
    }

    public String getDependenciaAsociado() {
        return dependenciaAsociado;
    }
    public void setDependenciaAsociado(String dependenciaAsociado) {
        this.dependenciaAsociado = dependenciaAsociado;
    }

    //VARIABLES TARJETA PRESENTE
    private ReposicionTarjeta reposicionTarjeta;
    private List<ResponseTarjeta> tarjetas;
    private ResponseTarjeta tarjetaSeleccionada;
    private List<String> motivosBloqueoTarjeta;

    public ReposicionTarjeta getReposicionTarjeta() {
        return reposicionTarjeta;
    }
    public void setReposicionTarjeta(ReposicionTarjeta reposicionTarjeta) {
        this.reposicionTarjeta = reposicionTarjeta;
    }

    public List<ResponseTarjeta> getTarjetas() {
        return tarjetas;
    }
    public void setTarjetas(List<ResponseTarjeta> tarjetas) {
        this.tarjetas = tarjetas;
    }

    public List<String> getMotivosBloqueoTarjeta() {
        return motivosBloqueoTarjeta;
    }
    public void setMotivosBloqueoTarjeta(List<String> motivosBloqueoTarjeta) {
        this.motivosBloqueoTarjeta = motivosBloqueoTarjeta;
    }

    public ResponseTarjeta getTarjetaSeleccionada() {
        return tarjetaSeleccionada;
    }
    public void setTarjetaSeleccionada(ResponseTarjeta tarjetaSeleccionada) {
        this.tarjetaSeleccionada = tarjetaSeleccionada;
    }

    //VARIABLES BOTTOM NAVIGATION BAR
    private PortafolioPadre categoriaPortafolioSeleccionada;
    private ResponsePortafolio portafolioSeleccionado;
    private List<PortafolioPadre> categoriasPortafolio;
    private List<ResponsePortafolio> portafolios;
    private List<ResponsePreguntasFrecuente> preguntaFrecuentes;
    private List<ResponseDirectorio> directorios;
    private String nombreAgenciaSeleccionada;
    private List<ResponseServicios> servicios;
    private ResponseServicios servicioDestacado1;
    private ResponseServicios servicioDestacado2;
    private List<ResponseLocationsAgencies> agencias;

    public PortafolioPadre getCategoriaPortafolioSeleccionada() {
        return categoriaPortafolioSeleccionada;
    }
    public void setCategoriaPortafolioSeleccionada(PortafolioPadre categoriaPortafolioSeleccionada) {
        this.categoriaPortafolioSeleccionada = categoriaPortafolioSeleccionada;
    }

    public ResponsePortafolio getPortafolioSeleccionado() {
        return portafolioSeleccionado;
    }
    public void setPortafolioSeleccionado(ResponsePortafolio portafolioSeleccionado) {
        this.portafolioSeleccionado = portafolioSeleccionado;
    }

    public List<PortafolioPadre> getCategoriasPortafolio() {
        return categoriasPortafolio;
    }
    public void setCategoriasPortafolio(List<PortafolioPadre> categoriasPortafolio) {
        this.categoriasPortafolio = categoriasPortafolio;
    }

    public List<ResponsePortafolio> getPortafolio() {
        return portafolios;
    }
    public void setPortafolio(List<ResponsePortafolio> portafolios) {
        this.portafolios = portafolios;
    }

    public List<ResponsePreguntasFrecuente> getPreguntaFrecuentes() {
        return preguntaFrecuentes;
    }
    public void setPreguntaFrecuentes(List<ResponsePreguntasFrecuente> preguntaFrecuentes) {
        this.preguntaFrecuentes = preguntaFrecuentes;
    }

    public List<ResponseDirectorio> getDirectorios() {
        return directorios;
    }
    public void setDirectorios(List<ResponseDirectorio> directorios) {
        this.directorios = directorios;
    }

    public String getNombreAgenciaSeleccionada() {
        return nombreAgenciaSeleccionada;
    }
    public void setNombreAgenciaSeleccionada(String nombreAgenciaSeleccionada) {
        this.nombreAgenciaSeleccionada = nombreAgenciaSeleccionada;
    }

    public List<ResponseServicios> getServicios() {
        return servicios;
    }
    public void setServicios(List<ResponseServicios> servicios) {
        this.servicios = servicios;
    }

    public ResponseServicios getServicioDestacado1() {
        return servicioDestacado1;
    }
    public void setServicioDestacado1(ResponseServicios servicioDestacado1) {
        this.servicioDestacado1 = servicioDestacado1;
    }

    public ResponseServicios getServicioDestacado2() {
        return servicioDestacado2;
    }
    public void setServicioDestacado2(ResponseServicios servicioDestacado2) {
        this.servicioDestacado2 = servicioDestacado2;
    }

    public List<ResponseLocationsAgencies> getAgencias() {
        return agencias;
    }
    public void setAgencias(List<ResponseLocationsAgencies> agencias) {
        this.agencias = agencias;
    }


    //VARIABLES CONVENIOS
    private Resumen resumen;
    private boolean haveJumpedToProducts;
    private boolean haveFinishedBuy;

    public Resumen getResumen() {
        return resumen;
    }
    public void setResumen(Resumen resumen) {
        this.resumen = resumen;
    }

    public boolean haveJumpedToProducts() {
        return haveJumpedToProducts;
    }
    public void haveJumpedToProducts(boolean haveJumpedToProducts) {
        this.haveJumpedToProducts = haveJumpedToProducts;
    }

    public boolean haveFinishedBuy() {
        return haveFinishedBuy;
    }
    public void haveFinishedBuy(boolean haveFinishedBuy) {
        this.haveFinishedBuy = haveFinishedBuy;
    }


    //VARIABLES ADELANTO NOMINA
    private ResponseTopes topes;
    private String valorSolicitado;
    private ResponseValidarRequisitos requisitos;

    public ResponseTopes getTopes() {
        return topes;
    }
    public void setTopes(ResponseTopes topes) {
        this.topes = topes;
    }

    public String getValorSolicitado() {
        return valorSolicitado;
    }
    public void setValorSolicitado(String valorSolicitado) {
        this.valorSolicitado = valorSolicitado;
    }

    public ResponseValidarRequisitos getRequisitos() {
        return requisitos;
    }
    public void setRequisitos(ResponseValidarRequisitos requisitos) {
        this.requisitos = requisitos;
    }

    //VARIABLES INBOX
    private List<ResponseMensajesBanner> mensajes;
    private List<ResponseObtenerMensajes> mensajesBuzon;
    private ResponseObtenerMensajes mensajesBuzonSeleccionado;

    public List<ResponseMensajesBanner> getMensajes() {
        return mensajes;
    }
    public void setMensajes(List<ResponseMensajesBanner> mensajes) {
        this.mensajes = mensajes;
    }

    public List<ResponseObtenerMensajes> getMensajesBuzon() {
        return mensajesBuzon;
    }
    public void setMensajesBuzon(List<ResponseObtenerMensajes> mensajesBuzon) {
        this.mensajesBuzon = mensajesBuzon;
    }

    public ResponseObtenerMensajes getMensajesBuzonSeleccionado() {
        return mensajesBuzonSeleccionado;
    }
    public void setMensajesBuzonSeleccionado(ResponseObtenerMensajes mensajesBuzonSeleccionado) {
        this.mensajesBuzonSeleccionado = mensajesBuzonSeleccionado;
    }

    //VARIABLES LOGIN
    private Usuario usuario;
    private String topeTransacciones;
    private Bitmap bitmapImgBanner;
    private Drawable drawableImgBanner;

    public Usuario getUsuario() {
        return usuario;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Bitmap getBitmapImgBanner() {
        return bitmapImgBanner;
    }
    public void setBitmapImgBanner(Bitmap bitmapImgBanner) {
        this.bitmapImgBanner = bitmapImgBanner;
    }

    public Drawable getDrawableImgBanner() {
        return drawableImgBanner;
    }
    public void setDrawableImgBanner(Drawable drawableImgBanner) {
        this.drawableImgBanner = drawableImgBanner;
    }

    public String getTopeTransacciones() {
        return topeTransacciones;
    }
    public void setTopeTransacciones(String topeTransacciones) {
        this.topeTransacciones = topeTransacciones;
    }

    //VARIABLES ACTUALIZACION DATOS
    private ResponseUbicaciones ubicaciones;
    private ResponseFormatoDirecciones formatoDirecciones;

    public ResponseUbicaciones getUbicaciones() {
        return ubicaciones;
    }
    public void setUbicaciones(ResponseUbicaciones ubicaciones) {
        this.ubicaciones = ubicaciones;
    }

    public ResponseFormatoDirecciones getFormatoDirecciones() {
        return formatoDirecciones;
    }
    public void setFormatoDirecciones(ResponseFormatoDirecciones formatoDirecciones) {
        this.formatoDirecciones = formatoDirecciones;
    }

    //VARIABLES DISPOSITIVO
    private String idDispositivoRegistrado;

    public String getIdDispositivoRegistrado() {
        return idDispositivoRegistrado;
    }
    public void setIdDispositivoRegistrado(String idDispositivoRegistrado) {
        this.idDispositivoRegistrado = idDispositivoRegistrado;
    }

    //VARIABLES TERMINOS Y CONDICIONES
    private ReponseTyC terminos;

    public ReponseTyC getTerminos() {
        return terminos;
    }
    public void setTerminos(ReponseTyC terminos) {
        this.terminos = terminos;
    }


    //VARIABLES AHORROS
    private String fechaSeleccionada;
    private List<ResponseTiposAhorro> tiposAhorros;

    public String getFechaSeleccionada() {
        return fechaSeleccionada;
    }
    public void setFechaSeleccionada(String fechaSeleccionada) {
        this.fechaSeleccionada = fechaSeleccionada;
    }

    public List<ResponseTiposAhorro> getTiposAhorros() {
        return tiposAhorros;
    }
    public void setTiposAhorros(List<ResponseTiposAhorro> tiposAhorros) {
        this.tiposAhorros = tiposAhorros;
    }

    //VARIABLES CENTROS VACACIONALES
    private List<ResponseCentroVacacional> centrosVacacionales;

    public List<ResponseCentroVacacional> getCentrosVacacionales() {
        return centrosVacacionales;
    }
    public void setCentrosVacacionales(List<ResponseCentroVacacional> centrosVacacionales) {
        this.centrosVacacionales = centrosVacacionales;
    }

    //VARIABLES TRANSFERENCIAS
    private List<ResponseBanco> responseBancos;

    public List<ResponseBanco> getBancos() {
        return responseBancos;
    }
    public void setBancos(List<ResponseBanco> responseBancos) {
        this.responseBancos = responseBancos;
    }

    //VARIABLES NEQUI
    private SuscriptionData datosSuscripcion;
    private String statusSuscription;
    private String textQR;
    private String saldoNequi;
    private ResponseDataCommerceQR.DataReceivedQR dataCommerceQR;
    private boolean alreadyOpenDialogNequiBalance;
    private boolean refusedAuthorizationNequiBalance;
    private boolean isActiveButtonPaymentQR;
    private boolean isActiveButtonPaymentDispersiones;
    private boolean isActiveButtonPaymentSuscriptions;
    private boolean isActiveStateNequiBalance;
    private boolean isActiveStateSuscriptions;
    private Integer tiempoExpiracionAutorizacion;

    public SuscriptionData getDatosSuscripcion() {
        return datosSuscripcion;
    }
    public void setDatosSuscripcion(SuscriptionData datosSuscripcion) {
        this.datosSuscripcion = datosSuscripcion;
    }

    public String getStatusSuscription() {
        return statusSuscription;
    }
    public void setStatusSuscription(String statusSuscription) {
        this.statusSuscription = statusSuscription;
    }

    public String getTextQR() {
        return textQR;
    }
    public void setTextQR(String textQR) {
        this.textQR = textQR;
    }

    public String getSaldoNequi() {
        return saldoNequi;
    }
    public void setSaldoNequi(String saldoNequi) {
        this.saldoNequi = saldoNequi;
    }

    public ResponseDataCommerceQR.DataReceivedQR getDataCommerceQR() {
        return dataCommerceQR;
    }
    public void setDataCommerceQR(ResponseDataCommerceQR.DataReceivedQR dataCommerceQR) {
        this.dataCommerceQR = dataCommerceQR;
    }

    public boolean isAlreadyOpenDialogNequiBalance() {
        return alreadyOpenDialogNequiBalance;
    }
    public void setAlreadyOpenDialogNequiBalance(boolean alreadyOpenDialogNequiBalance) {
        this.alreadyOpenDialogNequiBalance = alreadyOpenDialogNequiBalance;
    }

    public boolean isRefusedAuthorizationNequiBalance() {
        return refusedAuthorizationNequiBalance;
    }
    public void setRefusedAuthorizationNequiBalance(boolean refusedAuthorizationNequiBalance) {
        this.refusedAuthorizationNequiBalance = refusedAuthorizationNequiBalance;
    }

    public boolean isActiveButtonPaymentQR() {
        return isActiveButtonPaymentQR;
    }
    public void setActiveButtonPaymentQR(boolean activeButtonPaymentQR) {
        isActiveButtonPaymentQR = activeButtonPaymentQR;
    }

    public boolean isActiveButtonPaymentDispersiones() {
        return isActiveButtonPaymentDispersiones;
    }
    public void setActiveButtonPaymentDispersiones(boolean activeButtonPaymentDispersiones) {
        isActiveButtonPaymentDispersiones = activeButtonPaymentDispersiones;
    }

    public boolean isActiveButtonPaymentSuscriptions() {
        return isActiveButtonPaymentSuscriptions;
    }
    public void setActiveButtonPaymentSuscriptions(boolean activeButtonPaymentSuscriptions) {
        isActiveButtonPaymentSuscriptions = activeButtonPaymentSuscriptions;
    }

    public boolean isActiveStateNequiBalance() {
        return isActiveStateNequiBalance;
    }
    public void setActiveStateNequiBalance(boolean activeStateNequiBalance) {
        isActiveStateNequiBalance = activeStateNequiBalance;
    }

    public boolean isActiveStateSuscriptions() {
        return isActiveStateSuscriptions;
    }
    public void setActiveStateSuscriptions(boolean activeStateSuscriptions) {
        isActiveStateSuscriptions = activeStateSuscriptions;
    }

    public Integer getTiempoExpiracionAutorizacion() {
        return tiempoExpiracionAutorizacion;
    }
    public void setTiempoExpiracionAutorizacion(Integer tiempoExpiracionAutorizacion) {
        this.tiempoExpiracionAutorizacion = tiempoExpiracionAutorizacion;
    }
}
