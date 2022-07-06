package solidappservice.cm.com.presenteapp.rest.retrofit.apielecciones;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import solidappservice.cm.com.presenteapp.entities.elecciones.response.ResponseCandidatos;
import solidappservice.cm.com.presenteapp.entities.elecciones.response.ResponseEstadoVotante;
import solidappservice.cm.com.presenteapp.entities.elecciones.response.ResponseRegistraVoto;

public interface ApiElecciones {

    @GET("EstadoVotante.php")
    Call<ResponseEstadoVotante> getVoterStatus(@Query("idvotante") String idVotante);

    @GET("CandidatosZona.php")
    Call<ResponseCandidatos> getCandidates(@Query("idZona") String idZonaElectoral);

    @GET("InsertVoto.php")
    Call<ResponseRegistraVoto> registerVote(
            @Query("idZona") String idZonaElectoral,
            @Query("CandidatoNo") String idCandidato,
            @Query("idVotante") String idVotante);

}
