package solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apiresponse;

import java.util.List;

public class ResponseUbicaciones {

    private List<Pais> paises;

    public ResponseUbicaciones() {
        super();
    }

    public ResponseUbicaciones(List<Pais> paises) {
        super();
        this.paises = paises;
    }

    public List<Pais> getPaises() {
        return paises;
    }

    public void setPaises(List<Pais> paises) {
        this.paises = paises;
    }

    public static class Pais {
        private String idPais;
        private String nombrePais;
        private List<Departamento> departamentos;

        public Pais() {
            super();
        }

        public Pais(String idPais, String nombrePais) {
            super();
            this.idPais = idPais;
            this.nombrePais = nombrePais;
        }

        public Pais(String idPais, String nombrePais, List<Departamento> departamentos) {
            super();
            this.idPais = idPais;
            this.nombrePais = nombrePais;
            this.departamentos = departamentos;
        }

        public String getIdPais() {
            return idPais;
        }
        public void setIdPais(String idPais) {
            this.idPais = idPais;
        }
        public String getNombrePais() {
            return nombrePais;
        }
        public void setNombrePais(String nombrePais) {
            this.nombrePais = nombrePais;
        }
        public List<Departamento> getDepartamentos() {
            return departamentos;
        }
        public void setDepartamentos(List<Departamento> departamentos) {
            this.departamentos = departamentos;
        }
        public String toString() {
            return nombrePais;
        }
    }

    public static class Departamento {
        private String idDepartamento;
        private String nombreDepartamento;
        private List<Ciudad> ciudades;

        public Departamento() {
            super();
        }

        public Departamento(String idDepartamento, String nombreDepartamento) {
            super();
            this.idDepartamento = idDepartamento;
            this.nombreDepartamento = nombreDepartamento;
        }

        public Departamento(String idDepartamento, String nombreDepartamento, List<Ciudad> ciudades) {
            super();
            this.idDepartamento = idDepartamento;
            this.nombreDepartamento = nombreDepartamento;
            this.ciudades = ciudades;
        }

        public String getIdDepartamento() {
            return idDepartamento;
        }
        public void setIdDepartamento(String idDepartamento) {
            this.idDepartamento = idDepartamento;
        }
        public String getNombreDepartamento() {
            return nombreDepartamento;
        }
        public void setNombreDepartamento(String nombreDepartamento) {
            this.nombreDepartamento = nombreDepartamento;
        }
        public List<Ciudad> getCiudades() {
            return ciudades;
        }
        public void setCiudades(List<Ciudad> ciudades) {
            this.ciudades = ciudades;
        }
        public String toString() {
            return nombreDepartamento;
        }
    }

    public static class Ciudad {
        private String idCiudad;
        private String nombreCiudad;

        public Ciudad() {
            super();
        }

        public Ciudad(String idCiudad, String nombreCiudad) {
            super();
            this.idCiudad = idCiudad;
            this.nombreCiudad = nombreCiudad;
        }

        public String getIdCiudad() {
            return idCiudad;
        }

        public void setIdCiudad(String idCiudad) {
            this.idCiudad = idCiudad;
        }

        public String getNombreCiudad() {
            return nombreCiudad;
        }

        public void setNombreCiudad(String nombreCiudad) {
            this.nombreCiudad = nombreCiudad;
        }

        public String toString() {
            return nombreCiudad;
        }
    }

}
