package solidappservice.cm.com.presenteapp.rest.retrofit.apiconvenios;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import solidappservice.cm.com.presenteapp.entities.base.BaseResponseConvenios;
import solidappservice.cm.com.presenteapp.entities.convenios.request.RequestSolicitudProducto;
import solidappservice.cm.com.presenteapp.entities.convenios.response.ResponseProducto;
import solidappservice.cm.com.presenteapp.entities.convenios.response.ResponseResumen;
import solidappservice.cm.com.presenteapp.entities.convenios.response.ResponseSolicitudProducto;

public interface ApiConvenios {

    @GET("listarresumen")
    Call<BaseResponseConvenios<ResponseResumen>> getResumen(@Header("Token") String token,
                                                            @Header("Identificacion") String identificacion);

    @GET("listarProductos")
    Call<BaseResponseConvenios<List<ResponseProducto>>> listarProductos(
            @Header("Token") String token,
            @Header("Identificacion") String identificacion,
            @Query("idConvenio") String idConvenio
    );

    @POST("usarProducto")
    Call<BaseResponseConvenios<ResponseSolicitudProducto>> solicitarProducto(
            @Header("Token") String token,
            @Header("Identificacion") String identificacion,
            @Body RequestSolicitudProducto solicitudProducto
    );
}
