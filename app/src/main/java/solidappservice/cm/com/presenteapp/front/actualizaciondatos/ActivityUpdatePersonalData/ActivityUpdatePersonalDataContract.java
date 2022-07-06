package solidappservice.cm.com.presenteapp.front.actualizaciondatos.ActivityUpdatePersonalData;

import solidappservice.cm.com.presenteapp.tools.IFragmentCoordinator;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 11/07/2021.
 */
public interface ActivityUpdatePersonalDataContract {

    interface View{
        void showFragmentUpdatePersonalData(IFragmentCoordinator.Pantalla pantalla);
    }

    interface Presenter{
        void loadFragmentUpdatePersonalData(IFragmentCoordinator.Pantalla pantalla);
    }

    interface Model{

    }

}
