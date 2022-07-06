package solidappservice.cm.com.presenteapp.entities.actualizaciondatos.apiresponse;

import java.util.List;

public class ResponseFormatoDirecciones {

    private List<ItemsDirecciones> tiposVia;
    private List<ItemsDirecciones> letrasVia;
    private List<ItemsDirecciones> complementosVia;

    public ResponseFormatoDirecciones() {
    }

    public ResponseFormatoDirecciones(List<ItemsDirecciones> tiposVia, List<ItemsDirecciones> letrasVia,
                                      List<ItemsDirecciones> complementosVia) {
        this.tiposVia = tiposVia;
        this.letrasVia = letrasVia;
        this.complementosVia = complementosVia;
    }

    public List<ItemsDirecciones> getTiposVia() {
        return tiposVia;
    }
    public void setTiposVia(List<ItemsDirecciones> tiposVia) {
        this.tiposVia = tiposVia;
    }
    public List<ItemsDirecciones> getLetrasVia() {
        return letrasVia;
    }
    public void setLetrasVia(List<ItemsDirecciones> letrasVia) {
        this.letrasVia = letrasVia;
    }
    public List<ItemsDirecciones> getComplementosVia() {
        return complementosVia;
    }
    public void setComplementosVia(List<ItemsDirecciones> complementosVia) {
        this.complementosVia = complementosVia;
    }

    public static class ItemsDirecciones{

        private String id;
        private String value;

        public ItemsDirecciones() {
        }

        public ItemsDirecciones(String id, String value) {
            this.id = id;
            this.value = value;
        }

        public String getId() {
            return this.id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getValue() {
            return this.value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String toString() {
            return this.value;
        }
    }
}
