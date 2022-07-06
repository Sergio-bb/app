package solidappservice.cm.com.presenteapp.entities.convenios.mockup;

import java.util.ArrayList;

import solidappservice.cm.com.presenteapp.R;
import solidappservice.cm.com.presenteapp.entities.convenios.dto.Categoria;
import solidappservice.cm.com.presenteapp.entities.convenios.dto.Ciudad;
import solidappservice.cm.com.presenteapp.entities.convenios.dto.Convenio;
import solidappservice.cm.com.presenteapp.entities.convenios.FormaPago;
import solidappservice.cm.com.presenteapp.entities.convenios.dto.Producto;
import solidappservice.cm.com.presenteapp.entities.convenios.dto.Resumen;

public class MockupConvenios {

    public static Resumen getResumen(){
        Resumen resumen = new Resumen();
        resumen.setCategorias(getCategoriasMockUp());
        resumen.setCiudades(getCiudadesMockUp());
        resumen.setConvenios(getConveniosMockUp());
        return resumen;
    }

    private static ArrayList<Categoria> getCategoriasMockUp(){

        ArrayList<Categoria> categorias = new ArrayList<>();

        Categoria bienestar = new Categoria();
        bienestar.setId(String.valueOf(1));
        bienestar.setNombre("Bienestar");
        bienestar.setImagen(null);
        bienestar.setImagenId(R.drawable.ic_cat_conv_bienes);
        categorias.add(bienestar);

        Categoria educacion = new Categoria();
        educacion.setId(String.valueOf(2));
        educacion.setNombre("Educación");
        educacion.setImagen(null);
        educacion.setImagenId(R.drawable.ic_cat_conv_educa);
        categorias.add(educacion);

        Categoria recreacion = new Categoria();
        recreacion.setId(String.valueOf(3));
        recreacion.setNombre("Recreación");
        recreacion.setImagen(null);
        recreacion.setImagenId(R.drawable.ic_cat_conv_recrea);
        categorias.add(recreacion);

        Categoria salud = new Categoria();
        salud.setId(String.valueOf(4));
        salud.setNombre("Salud");
        salud.setImagen(null);
        salud.setImagenId(R.drawable.ic_cat_conv_salud);
        categorias.add(salud);

        return categorias;
    }

    private static ArrayList<Ciudad> getCiudadesMockUp(){

        ArrayList<Ciudad> ciudades = new ArrayList<>();

        Ciudad med = new Ciudad();
        med.setNombre("Medellín");

        Ciudad cal = new Ciudad();
        cal.setNombre("Cali");

        Ciudad bog = new Ciudad();
        bog.setNombre("Bogotá");

        ciudades.add(med);
        ciudades.add(cal);
        ciudades.add(bog);
        return ciudades;
    }

    private static ArrayList<Convenio> getConveniosMockUp(){

        ArrayList<Convenio> convenios = new ArrayList<>();

        Convenio convenio = new Convenio();
        convenio.setId("1");
        convenio.setNombre("Móvil Éxito");
        convenio.setImagen(null);
        convenio.setDetalle("For an enterprise build, we recommend defining the lint rules in a init.gradle script or in a gradle script that is included via the Gradle apply from mechanism.");
        convenio.setOrden(1);
        //convenio.setDestacado(new Random().nextBoolean());
        convenio.setDestacado(true);
        convenio.setDescripcionCorta("Móvil Éxito - Paquetes exclusivos");
        convenio.setBeneficio("Adquiere planes exclusivos y recarga tu celular");
        convenio.setIdCategoria(String.valueOf(1));
        convenios.add(convenio);

        Convenio bodyTech = new Convenio();
        bodyTech.setId("2");
        bodyTech.setNombre("Body Tech");
        bodyTech.setImagen(null);
        bodyTech.setDetalle("For an enterprise build, we recommend defining the lint rules in a init.gradle script or in a gradle script that is included via the Gradle apply from mechanism.");
        bodyTech.setOrden(2);
        //bodyTech.setDestacado(new Random().nextBoolean());
        bodyTech.setDestacado(true);
        bodyTech.setDescripcionCorta("Body Tech - Hasta el 25% de dcto");
        bodyTech.setBeneficio("Descuentos en cada uno de los planes que adquieras");
        bodyTech.setIdCategoria(String.valueOf(1));
        convenios.add(bodyTech);

        return convenios;
    }

    public static ArrayList<Producto> getProductosPorConvenioMockUp(int idConvenio){
        ArrayList<Producto> productos = new ArrayList<>();

        FormaPago formaPago1 = new FormaPago();
        formaPago1.setId(String.valueOf(1));
        formaPago1.setNombre("Forma de pago #1");
        formaPago1.setLegal("Acepto la compra y/o prestación de servicio del presente convenio, sus requisitos y condiciones de uso, así como el débito automático de mi disponible para la cancelación del mismo.");

        FormaPago formaPago2 = new FormaPago();
        formaPago2.setId(String.valueOf(2));
        formaPago2.setNombre("Forma de pago #2");
        formaPago2.setLegal("Acepto la compra y/o prestación de servicio del presente convenio, sus requisitos y condiciones de uso, así como el pago mediante Bolsillo de Nomina para la cancelación del mismo.");

        FormaPago formaPago3 = new FormaPago();
        formaPago3.setId(String.valueOf(3));
        formaPago3.setNombre("Forma de pago #3");
        formaPago3.setLegal(null);

        ArrayList<FormaPago> formaPagos = new ArrayList<>();
        formaPagos.add(formaPago1);
        formaPagos.add(formaPago2);
        formaPagos.add(formaPago3);

        Producto conversar = new Producto();
        conversar.setEtiquetaCampoCelular("Ayuda campo celular");
        conversar.setHtmlDescripcion("<h1>Descripcion</h1>");
        conversar.setHtmlRestriccion("<h1>Restricción</h1>");
        conversar.setId(1);
        conversar.setIdConvenio(String.valueOf(idConvenio));
        conversar.setImagen(null);
        conversar.setResourceImage(R.drawable.conversar5000);
        conversar.setNombre("Conversar");
        conversar.setResumen("The Gradle Lint plugin is a pluggable and configurable linter tool for identifying and reporting.");
        conversar.setValor("Valor $5.000");
        conversar.setFormasPago(formaPagos);

        Producto conversar2 = new Producto();
        conversar2.setEtiquetaCampoCelular("Ayuda campo celular");
        conversar2.setHtmlDescripcion("<h1>Descripcion</h1>");
        conversar2.setHtmlRestriccion("<h1>Restricción</h1>");
        conversar2.setId(1);
        conversar2.setIdConvenio(String.valueOf(idConvenio));
        conversar2.setImagen(null);
        conversar2.setResourceImage(R.drawable.conversar14900);
        conversar2.setNombre("Conversar");
        conversar2.setResumen("The Gradle Lint plugin is a pluggable and configurable linter tool for identifying and reporting.");
        conversar2.setValor("Valor $14.900");
        conversar2.setFormasPago(formaPagos);

        Producto conversarYnavegar = new Producto();
        conversarYnavegar.setEtiquetaCampoCelular("Ayuda campo celular");
        conversarYnavegar.setHtmlDescripcion("<h1>Descripcion</h1>");
        conversarYnavegar.setHtmlRestriccion("<h1>Restricción</h1>");
        conversarYnavegar.setId(1);
        conversarYnavegar.setIdConvenio(String.valueOf(idConvenio));
        conversarYnavegar.setImagen(null);
        conversarYnavegar.setResourceImage(R.drawable.conversarynavegar19900);
        conversarYnavegar.setNombre("Conversar");
        conversarYnavegar.setResumen("The Gradle Lint plugin is a pluggable and configurable linter tool for identifying and reporting.");
        conversarYnavegar.setValor("Valor $19.900");
        conversarYnavegar.setFormasPago(formaPagos);

        Producto conversarYnavegar2 = new Producto();
        conversarYnavegar2.setEtiquetaCampoCelular("Ayuda campo celular");
        conversarYnavegar2.setHtmlDescripcion("<h1>Descripcion</h1>");
        conversarYnavegar2.setHtmlRestriccion("<h1>Restricción</h1>");
        conversarYnavegar2.setId(1);
        conversarYnavegar2.setIdConvenio(String.valueOf(idConvenio));
        conversarYnavegar2.setImagen(null);
        conversarYnavegar2.setResourceImage(R.drawable.conversarynavegar49900);
        conversarYnavegar2.setNombre("Conversar");
        conversarYnavegar2.setResumen("The Gradle Lint plugin is a pluggable and configurable linter tool for identifying and reporting.");
        conversarYnavegar2.setValor("Valor $49.900");
        conversarYnavegar2.setFormasPago(formaPagos);

        Producto navegar = new Producto();
        navegar.setEtiquetaCampoCelular("Ayuda campo celular");
        navegar.setHtmlDescripcion("<h1>Descripcion</h1>");
        navegar.setHtmlRestriccion("<h1>Restricción</h1>");
        navegar.setId(1);
        navegar.setIdConvenio(String.valueOf(idConvenio));
        navegar.setImagen(null);
        navegar.setResourceImage(R.drawable.navegar9900);
        navegar.setNombre("Conversar");
        navegar.setResumen("The Gradle Lint plugin is a pluggable and configurable linter tool for identifying and reporting.");
        navegar.setValor("Valor $9.900");
        navegar.setFormasPago(formaPagos);

        Producto navegar2 = new Producto();
        navegar2.setEtiquetaCampoCelular("Ayuda campo celular");
        navegar2.setHtmlDescripcion("<h1>Descripcion</h1>");
        navegar2.setHtmlRestriccion("<h1>Restricción</h1>");
        navegar2.setId(1);
        navegar2.setIdConvenio(String.valueOf(idConvenio));
        navegar2.setImagen(null);
        navegar2.setResourceImage(R.drawable.navegar19900);
        navegar2.setNombre("Conversar");
        navegar2.setResumen("The Gradle Lint plugin is a pluggable and configurable linter tool for identifying and reporting.");
        navegar2.setValor("Valor $19.900");
        navegar2.setFormasPago(formaPagos);

        Producto navegar3 = new Producto();
        navegar3.setEtiquetaCampoCelular("Ayuda campo celular");
        navegar3.setHtmlDescripcion("<h1>Descripcion</h1>");
        navegar3.setHtmlRestriccion("<h1>Restricción</h1>");
        navegar3.setId(1);
        navegar3.setIdConvenio(String.valueOf(idConvenio));
        navegar3.setImagen(null);
        navegar3.setResourceImage(R.drawable.navegar24900);
        navegar3.setNombre("Conversar");
        navegar3.setResumen("The Gradle Lint plugin is a pluggable and configurable linter tool for identifying and reporting.");
        navegar3.setValor("Valor $24.900");
        navegar3.setFormasPago(formaPagos);

        Producto navegar4 = new Producto();
        navegar4.setEtiquetaCampoCelular("Ayuda campo celular");
        navegar4.setHtmlDescripcion("<h1>Descripcion</h1>");
        navegar4.setHtmlRestriccion("<h1>Restricción</h1>");
        navegar4.setId(1);
        navegar4.setIdConvenio(String.valueOf(idConvenio));
        navegar4.setImagen(null);
        navegar4.setResourceImage(R.drawable.navegar37900);
        navegar4.setNombre("Conversar");
        navegar4.setResumen("The Gradle Lint plugin is a pluggable and configurable linter tool for identifying and reporting.");
        navegar4.setValor("Valor $37.900");
        navegar4.setFormasPago(formaPagos);

        productos.add(conversar);
        productos.add(conversar2);
        productos.add(conversarYnavegar);
        productos.add(conversarYnavegar2);
        productos.add(navegar);
        productos.add(navegar2);
        productos.add(navegar3);
        productos.add(navegar4);


        return productos;
    }
}
