
function realizarPago(){
 
  $.ajax({
    type: "POST",
    contentType: "application/x-www-form-urlencoded",
    url: "https://apps.presente.com.co/PresenteME2/api/connect/token",
    data: { grant_type: 'client_credentials' },
    dataType: "json",
    headers: {
      "Authorization": "Basic OTU5NDUwNzMtNWJlYi00NGU0LWIyOTAtZDhiN2IwNWI0MDczOmViM2RiYmQ2LTEzOGQtNGIxZC1hNzU1LWY0ZWMwY2EzNDUzNg=="
    },
    success: function (data){

      var fechaActual = new Date();
      var dd = fechaActual.getDate();
      var mm = fechaActual.getMonth() + 1;
      var yyyy = fechaActual.getFullYear();
      var hours = fechaActual.getHours();
      var minutes = fechaActual.getMinutes();
      var seconds = fechaActual.getSeconds();
      var fechaRegistro = (dd < 10 ? "0"+dd : dd) + "/" + (mm < 10 ? "0"+mm : mm) + "/" + yyyy + " " + hours + ":" + minutes + ":" + seconds;

      $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "https://apps.presente.com.co/PresenteME2/api/recarga/iniciarSolicitudRecarga",
        headers: {
          "Authorization": "Bearer "+data.access_token
        },
        data: JSON.stringify({ 
          idTransaccionPresente: $("#IdTransaccionPresente").val(),
          cedula: '1037584383',
          idPaquete: '194',
          nombrePaquete: 'Paquete Extra 3GB',
          lineasARecargar: '3054367301', //3011011001
          origen: 'APP',
          valorPaquete: 37900,
          vigenciaPaquete: '15 días',
          detallePaquete: 'Minutos ilimitados y 3GB de navegación',
          valorFinal: 37900,
          fechaSolicitud: fechaRegistro,
          idTransaccionME: '987654321',
          tipoRecarga: 'UNICA'
        }),
        dataType: "json",
        success: function (data){
          if(data != null && data.resultado == "Exitoso"){
            closeWindow();
          }
        },
        error: function(request, status, error) {
          alert(request.responseText);
        }
      });
    },
    error: function(e) {
      alert('There was some error performing the AJAX call!');
    }
  });
}

function loadVariables(idTransaccionPresente){
  $("#IdTransaccionPresente").val(idTransaccionPresente);
}
function closeWindow() {
  window.open('','_parent','');
  window.close();
}

