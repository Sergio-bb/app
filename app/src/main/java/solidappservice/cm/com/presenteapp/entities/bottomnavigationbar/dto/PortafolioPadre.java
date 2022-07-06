package solidappservice.cm.com.presenteapp.entities.bottomnavigationbar.dto;

import java.util.ArrayList;

import solidappservice.cm.com.presenteapp.entities.bottomnavigationbar.response.ResponsePortafolio;

public class PortafolioPadre {

    private String categoriaPadre;
    private ArrayList<ResponsePortafolio> portafolios;

    public PortafolioPadre() {
    }

    public PortafolioPadre(String categoriaPadre, ArrayList<ResponsePortafolio> portafolios) {
        this.categoriaPadre = categoriaPadre;
        this.portafolios = portafolios;
    }

    public String getCategoriaPadre() {
        return categoriaPadre;
    }

    public void setCategoriaPadre(String categoriaPadre) {
        this.categoriaPadre = categoriaPadre;
    }

    public ArrayList<ResponsePortafolio> getPortafolios() {
        return portafolios;
    }

    public void setPortafolios(ArrayList<ResponsePortafolio> portafolios) {
        this.portafolios = portafolios;
    }
}
