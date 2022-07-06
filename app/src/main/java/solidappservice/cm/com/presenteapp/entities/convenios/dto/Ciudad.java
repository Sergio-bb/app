package solidappservice.cm.com.presenteapp.entities.convenios.dto;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 14/08/18.
 */

public class Ciudad {

    private String nombre;

    private boolean isCurrentLocation;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCurrentLocation(boolean currentLocation) {
        isCurrentLocation = currentLocation;
    }

    public boolean isCurrentLocation() {
        return isCurrentLocation;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
