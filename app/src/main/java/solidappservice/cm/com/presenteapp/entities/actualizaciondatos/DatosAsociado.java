package solidappservice.cm.com.presenteapp.entities.actualizaciondatos;

public class DatosAsociado {

    private String idRegistroDispositivo;
    private String nombreCompleto;
    private String direccion;
    private Direccion detalleDireccion;
    private String celular;
    private String email;
    private String barrio;
    private String idCiudad;
    private String nombreCiudad;
    private String idDepartamento;
    private String nombreDepartamento;
    private String idPais;
    private String nombrePais;
    private String ip;
    private String canal;

    public DatosAsociado() {
    }

    public DatosAsociado(String idRegistroDispositivo, String nombreCompleto, String direccion, String celular, String email,
                         String barrio, String idCiudad, String nombreCiudad, String idDepartamento, String nombreDepartamento,
                         String idPais, String nombrePais, String ip, String canal) {
        this.idRegistroDispositivo = idRegistroDispositivo;
        this.nombreCompleto = nombreCompleto;
        this.direccion = direccion;
        this.celular = celular;
        this.email = email;
        this.barrio = barrio;
        this.idCiudad = idCiudad;
        this.nombreCiudad = nombreCiudad;
        this.idDepartamento = idDepartamento;
        this.nombreDepartamento = nombreDepartamento;
        this.idPais = idPais;
        this.nombrePais = nombrePais;
        this.ip = ip;
        this.canal = canal;
    }

    public DatosAsociado(String nombreCompleto, String direccion, String celular, String email, String barrio, String idCiudad,
                         String nombreCiudad, String idDepartamento, String nombreDepartamento, String idPais, String nombrePais) {
        this.nombreCompleto = nombreCompleto;
        this.direccion = direccion;
        this.celular = celular;
        this.email = email;
        this.barrio = barrio;
        this.idCiudad = idCiudad;
        this.nombreCiudad = nombreCiudad;
        this.idDepartamento = idDepartamento;
        this.nombreDepartamento = nombreDepartamento;
        this.idPais = idPais;
        this.nombrePais = nombrePais;
    }

    public DatosAsociado(Direccion detalleDireccion){
        this.detalleDireccion = detalleDireccion;
    }

    public String getIdRegistroDispositivo() {
        return idRegistroDispositivo;
    }

    public void setIdRegistroDispositivo(String idRegistroDispositivo) {
        this.idRegistroDispositivo = idRegistroDispositivo;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }

    public Direccion getDetalleDireccion() {
        return detalleDireccion;
    }

    public void setDetalleDireccion(Direccion detalleDireccion) {
        this.detalleDireccion = detalleDireccion;
    }

    public static class Direccion {
        private String tipoVia;
        private String numeroVia1;
        private String letraVia1;
        private String complementoVia1;
        private String numeroVia2;
        private String letraVia2;
        private String complementoVia2;
        private String numeroComplemento;
        private String informacionAdicional;

        public Direccion() {
        }

        public Direccion(String tipoVia, String numeroVia1, String letraVia1, String complementoVia1, String numeroVia2,
                         String letraVia2, String complementoVia2, String numeroComplemento, String informacionAdicional) {
            this.tipoVia = tipoVia;
            this.numeroVia1 = numeroVia1;
            this.letraVia1 = letraVia1;
            this.complementoVia1 = complementoVia1;
            this.numeroVia2 = numeroVia2;
            this.letraVia2 = letraVia2;
            this.complementoVia2 = complementoVia2;
            this.numeroComplemento = numeroComplemento;
            this.informacionAdicional = informacionAdicional;
        }

        public String getTipoVia() {
            return tipoVia;
        }

        public void setTipoVia(String tipoVia) {
            this.tipoVia = tipoVia;
        }

        public String getNumeroVia1() {
            return numeroVia1;
        }

        public void setNumeroVia1(String numeroVia1) {
            this.numeroVia1 = numeroVia1;
        }

        public String getLetraVia1() {
            return letraVia1;
        }

        public void setLetraVia1(String letraVia1) {
            this.letraVia1 = letraVia1;
        }

        public String getComplementoVia1() {
            return complementoVia1;
        }

        public void setComplementoVia1(String complementoVia1) {
            this.complementoVia1 = complementoVia1;
        }

        public String getNumeroVia2() {
            return numeroVia2;
        }

        public void setNumeroVia2(String numeroVia2) {
            this.numeroVia2 = numeroVia2;
        }

        public String getLetraVia2() {
            return letraVia2;
        }

        public void setLetraVia2(String letraVia2) {
            this.letraVia2 = letraVia2;
        }

        public String getComplementoVia2() {
            return complementoVia2;
        }

        public void setComplementoVia2(String complementoVia2) {
            this.complementoVia2 = complementoVia2;
        }

        public String getNumeroComplemento() {
            return numeroComplemento;
        }

        public void setNumeroComplemento(String numeroComplemento) {
            this.numeroComplemento = numeroComplemento;
        }

        public String getInformacionAdicional() {
            return informacionAdicional;
        }

        public void setInformacionAdicional(String informacionAdicional) {
            this.informacionAdicional = informacionAdicional;
        }
    }
}
