package solidappservice.cm.com.presenteapp.front.actualizaciondatos.ActivityUpdatePersonalData;

import solidappservice.cm.com.presenteapp.tools.IFragmentCoordinator;

/**
 * CREADO POR MIGUEL DAVID CABEZAS EL 11/07/2021.
 */
public class ActivityUpdatePersonalDataPresenter implements ActivityUpdatePersonalDataContract.Presenter{

    ActivityUpdatePersonalDataView view;

    public ActivityUpdatePersonalDataPresenter(ActivityUpdatePersonalDataView view) {
        this.view = view;
    }

    @Override
    public void loadFragmentUpdatePersonalData(IFragmentCoordinator.Pantalla pantalla) {
        view.showFragmentUpdatePersonalData(pantalla);
    }
}
