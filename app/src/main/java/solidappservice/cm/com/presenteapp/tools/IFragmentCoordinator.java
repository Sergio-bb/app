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
        ActDatosStart,
        ActDatosEditData,
        ActDatosValidateData,
        ActDatosVerifyCode,
        ActDatosFinal,
        SuscNequiInicio,
        SuscNequiOne,
        SuscNequiSecond,
        SuscNequiFinal,
        NequiQRCamera,
        NequiQRDetail
    }

    void setFragment(Pantalla pantalla);
}
