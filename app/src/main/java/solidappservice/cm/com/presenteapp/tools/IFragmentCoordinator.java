package solidappservice.cm.com.presenteapp.tools;

import solidappservice.cm.com.presenteapp.entities.login.Response.Usuario;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA 23/11/2015.
 */
public interface IFragmentCoordinator {

    enum Pantalla {
        Ingreso,
        RecuperarContrasena,
        MenuPrincipal,
        MenuFinanzas,
        Tabs,
        Candidatos,
        Votacion,
        Mapa,
        PuntoAtencion,
        Transacciones,
        Transferencias,
        InscripcionCuentas,
        BorrarCuentas,
        ConveniosMenuPrincipal,
        ConveniosLista,
        ConveniosProductos,
        ConveniosCompraProducto,
        ConveniosMEMostrarResumen,
        ConveniosMELandingME,
        ActDatosStart,
        ActDatosEditData,
        ActDatosValidateData,
        ActDatosVerifyCode,
        ActDatosFinal
    }

    //void ingresar(Usuario usuario);
    void verMenuFinanzas();
    void verEstadoCuenta();
    void verTransacciones();
    void verTarjetaPresente();
    void verMisMensajes();
    void verGeoReferenciacion();
    void verCandidatos();
    void verVotacion();
    void verMenuPrincipal();
    void verRecuperarClave();
    void setFragment(Pantalla pantalla);
}
