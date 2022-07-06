package solidappservice.cm.com.presenteapp.entities.bottomnavigationbar.response;

import java.util.List;

public class ResponseLocationsAgencies {

    private String n_latitu;
    private String n_longit;
    private String n_sucurs;
    private String i_tipage;
    private String k_sucurs;
    private String d_sucurs;
    private String n_depart;
    private String n_ciudad;
    private String t_sucurs;
    public List<Contacto> contactos;

    public ResponseLocationsAgencies() {
    }

    public ResponseLocationsAgencies(String n_latitu, String n_longit, String n_sucurs, String i_tipage, String k_sucurs, String d_sucurs,
            String n_depart, String n_ciudad, String t_sucurs, List<Contacto> contactos) {
        this.n_latitu = n_latitu;
        this.n_longit = n_longit;
        this.n_sucurs = n_sucurs;
        this.i_tipage = i_tipage;
        this.k_sucurs = k_sucurs;
        this.d_sucurs = d_sucurs;
        this.n_depart = n_depart;
        this.n_ciudad = n_ciudad;
        this.t_sucurs = t_sucurs;
        this.contactos = contactos;
    }

    public String getN_latitu() {
        return n_latitu;
    }

    public void setN_latitu(String n_latitu) {
        this.n_latitu = n_latitu;
    }

    public String getN_longit() {
        return n_longit;
    }

    public void setN_longit(String n_longit) {
        this.n_longit = n_longit;
    }

    public String getN_sucurs() {
        return n_sucurs;
    }

    public void setN_sucurs(String n_sucurs) {
        this.n_sucurs = n_sucurs;
    }

    public String getI_tipage() {
        return i_tipage;
    }

    public void setI_tipage(String i_tipage) {
        this.i_tipage = i_tipage;
    }

    public String getK_sucurs() {
        return k_sucurs;
    }

    public void setK_sucurs(String k_sucurs) {
        this.k_sucurs = k_sucurs;
    }

    public String getD_sucurs() {
        return d_sucurs;
    }

    public void setD_sucurs(String d_sucurs) {
        this.d_sucurs = d_sucurs;
    }

    public String getN_depart() {
        return n_depart;
    }

    public void setN_depart(String n_depart) {
        this.n_depart = n_depart;
    }

    public String getN_ciudad() {
        return n_ciudad;
    }

    public void setN_ciudad(String n_ciudad) {
        this.n_ciudad = n_ciudad;
    }

    public String getT_sucurs() {
        return t_sucurs;
    }

    public void setT_sucurs(String t_sucurs) {
        this.t_sucurs = t_sucurs;
    }

    public List<Contacto> getContactos() {
        return contactos;
    }

    public void setContactos(List<Contacto> contactos) {
        this.contactos = contactos;
    }

    @Override
    public String toString() {
        if(contactos == null) return ".!<.!<";
        StringBuilder result = new StringBuilder();
        result.append(d_sucurs).append("!");
        for(Contacto contacto : contactos){
            result.append(contacto.toString()).append("!");
        }
        return result.toString();
    }

    public static class Contacto {

        private String i_tipage;
        private String k_sucurs;
        private String n_cargo;
        private String n_nombre;
        private String n_telefo;

        public Contacto() {
        }

        public Contacto(String i_tipage, String k_sucurs, String n_cargo, String n_nombre, String n_telefo) {
            this.i_tipage = i_tipage;
            this.k_sucurs = k_sucurs;
            this.n_cargo = n_cargo;
            this.n_nombre = n_nombre;
            this.n_telefo = n_telefo;
        }

        public String getI_tipage() {
            return i_tipage;
        }

        public void setI_tipage(String i_tipage) {
            this.i_tipage = i_tipage;
        }

        public String getK_sucurs() {
            return k_sucurs;
        }

        public void setK_sucurs(String k_sucurs) {
            this.k_sucurs = k_sucurs;
        }

        public String getN_cargo() {
            return n_cargo;
        }

        public void setN_cargo(String n_cargo) {
            this.n_cargo = n_cargo;
        }

        public String getN_nombre() {
            return n_nombre;
        }

        public void setN_nombre(String n_nombre) {
            this.n_nombre = n_nombre;
        }

        public String getN_telefo() {
            return n_telefo;
        }

        public void setN_telefo(String n_telefo) {
            this.n_telefo = n_telefo;
        }

        @Override
        public String toString() {
            return n_cargo + ": "+ (n_nombre == null || n_nombre.isEmpty() || n_nombre.equals("null")? "No hay registro" : n_nombre) +" "+ n_telefo;
        }
    }

}
