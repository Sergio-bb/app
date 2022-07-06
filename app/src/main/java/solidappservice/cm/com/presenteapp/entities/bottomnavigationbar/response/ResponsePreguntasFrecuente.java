package solidappservice.cm.com.presenteapp.entities.bottomnavigationbar.response;

import java.util.List;

/**
 * CREADO POR JORGE ANDRÉS DAVID CARDONA EL 20/07/2016.
 */
public class ResponsePreguntasFrecuente {

    private String grupo;
    private List<Pregunta> preguntas;

    public ResponsePreguntasFrecuente() {
    }

    public ResponsePreguntasFrecuente(String grupo, List<Pregunta> preguntas) {
        this.grupo = grupo;
        this.preguntas = preguntas;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public List<Pregunta> getPreguntas() {
        return preguntas;
    }

    public void setPreguntas(List<Pregunta> preguntas) {
        this.preguntas = preguntas;
    }

    public static class Pregunta {

        private String pregunta;
        private String respuesta;

        public Pregunta() {
        }

        public Pregunta(String pregunta, String respuesta) {
            this.pregunta = pregunta;
            this.respuesta = respuesta;
        }

        public String getPregunta() {
            return pregunta;
        }

        public void setPregunta(String pregunta) {
            this.pregunta = pregunta;
        }

        public String getRespuesta() {
            return respuesta;
        }

        public void setRespuesta(String respuesta) {
            this.respuesta = respuesta;
        }
    }


}