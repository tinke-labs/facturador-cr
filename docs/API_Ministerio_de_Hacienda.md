# API Ministerio de Hacienda

# Introducción

Información general y de uso público para el consumo desde aplicaciones creadas por desarrolladores de soluciones.

# EndPoints
## https://api.hacienda.go.cr/fe/ae

Permite obtener el nombre, el tipo de identificación, el régimen, la situación tributaria y las actividades económicas asociadas a un contribuyente, usando como parámetro el número de identificación (sin hacer uso de guiones).  Para la consulta de identificaciones físicas nacionales no es necesario incluir el 0 como primer dígito. En el caso de las identificaciones jurídicas y NITES tributarios es necesario el uso de los primeros 10 dígitos, sin utilizar los dos últimos dígitos verificadores.

Identificaciones soportadas:

1. Físicas nacionales
2. Jurídicas nacionales
3. DIMEX
4. NITES tributarios

**Ejemplos**

- cURL


    curl --location --request GET "https://api.hacienda.go.cr/fe/ae?identificacion=2100042005"


- jQuery


    $.ajax({
      "url": "https://api.hacienda.go.cr/fe/ae?identificacion=2100042005",
      "method": "GET"
    }).done(function (response) {
      console.log(response);
    });


- PHP


    <?php
    
    $curl = curl_init();
    
    curl_setopt_array($curl, array(
      CURLOPT_URL => "https://api.hacienda.go.cr/fe/ae?identificacion=2100042005",
      CURLOPT_RETURNTRANSFER => true,
      CURLOPT_ENCODING => "",
      CURLOPT_MAXREDIRS => 10,
      CURLOPT_TIMEOUT => 0,
      CURLOPT_FOLLOWLOCATION => false,
      CURLOPT_HTTP_VERSION => CURL_HTTP_VERSION_1_1,
      CURLOPT_CUSTOMREQUEST => "GET",
    ));
    
    $response = curl_exec($curl);
    $err = curl_error($curl);
    
    curl_close($curl);
    
    if ($err) {
      echo "cURL Error #:" . $err;
    } else {
      echo $response;
    } ?>

**Respuesta esperada**


    {
      "nombre": "ESTADO-MINISTERIO DE HACIENDA",
      "tipoIdentificacion": "02",
      "regimen": {
        "codigo": 1,
        "descripcion": "Régimen Tradicional"
      },
      "situacion": {
        "moroso": "SI",
        "omiso": "NO",
        "estado": "Inscrito",
        "administracionTributaria": "San Jose"
      },
      "actividades": [
        {
          "estado": "I",
          "tipo": "P",
          "codigo": 751101,
          "descripcion": "ACTIVIDADES DE LA ADMINISTRACION PUBLICA EN GENERAL"
        }
      ]
    }

**Tipos de identificación**

- 01 = Persona física costarricense
- 02 = Persona jurídica costarricense estatal (pública) o persona jurídica costarricense privada
- 03 = Persona física extranjera (DIMEX)
- 04 = Número de identificación tributario para personas físicas (NITE)

**Tipos de regímenes**

- 0 = No aplica
- 1 = Régimen tradicional
- 2 = Régimen simplificado

**Estado de las actividades económicas**

- A = Activa
- I = Inactiva

**Tipo de actividades económicas**

- P = Primaria
- S = Secundaria


## https://api.hacienda.go.cr/indicadores/tc/dolar

Permite obtener el tipo de cambio del dólar de los Estados Unidos de América.  No requiere de ningún tipo de parámetro.

**Ejemplos**

- cURL


    curl --location --request GET "https://api.hacienda.go.cr/indicadores/tc/dolar"


- jQuery


    $.ajax({
      "url": "https://api.hacienda.go.cr/indicadores/tc/dolar",
      "method": "GET"
    }).done(function (response) {
      console.log(response);
    });

**Respuesta esperada**


    {
      "venta": {
        "fecha": "2019-10-24T00:00:00-06:00",
        "valor": 586
      },
      "compra": {
        "fecha": "2019-10-24T00:00:00-06:00",
        "valor": 579.2
      }
    }


## https://api.hacienda.go.cr/indicadores/tc/dolar/historico

Permite obtener el histórico del tipo de cambio diario del dólar de Estados Unidos de América.  Requiere de los parámetros `d` y `h` (desde y hasta), que corresponden a las fechas en formato `año-mes-día` (`2019-01-01`).

**Ejemplos**

- cURL


    curl --location --request GET "https://api.hacienda.go.cr/indicadores/tc/dolar/historico?d=2019-12-01&h=2019-12-09"


- jQuery


    $.ajax({
      "url": "https://api.hacienda.go.cr/indicadores/tc/dolar/historico?d=2019-12-01&h=2019-12-09",
      "method": "GET"
    }).done(function (response) {
      console.log(response);
    });

**Respuesta esperada**


    [
      {
        "fecha": "2019-12-01 00:00:00",
        "compra": 558.9,
        "venta": 565.89
      },
      {
        "fecha": "2019-12-02 00:00:00",
        "compra": 558.9,
        "venta": 565.89
      },
      {
        "fecha": "2019-12-03 00:00:00",
        "compra": 560.22,
        "venta": 567.65
      },
      {
        "fecha": "2019-12-04 00:00:00",
        "compra": 563.53,
        "venta": 572.01
      },
      {
        "fecha": "2019-12-05 00:00:00",
        "compra": 563.99,
        "venta": 571.81
      },
      {
        "fecha": "2019-12-06 00:00:00",
        "compra": 566.91,
        "venta": 573.19
      },
      {
        "fecha": "2019-12-07 00:00:00",
        "compra": 565.08,
        "venta": 572.22
      },
      {
        "fecha": "2019-12-08 00:00:00",
        "compra": 565.08,
        "venta": 572.22
      },
      {
        "fecha": "2019-12-09 00:00:00",
        "compra": 565.08,
        "venta": 572.22
      }
    ]


## https://api.hacienda.go.cr/indicadores/tc/euro

Permite obtener el tipo de cambio del Euro (dólares y colones).  No requiere de ningún tipo de parámetro.

**Ejemplos**

- cURL


    curl --location --request GET "https://api.hacienda.go.cr/indicadores/tc/euro"


- jQuery


    $.ajax({
      "url": "https://api.hacienda.go.cr/indicadores/tc/euro",
      "method": "GET"
    }).done(function (response) {
      console.log(response);
    });

**Respuesta esperada**


    {
      "fecha": "2019-10-24T00:00:00-06:00",
      "dolares": 1.1115,
      "colones": 651.34
    }


## https://api.hacienda.go.cr/indicadores/tc

Permite obtener el tipo de cambio del dólar de los Estados Unidos de América y del Euro (dólares y colones).  No requiere de ningún tipo de parámetro.

**Ejemplos**

- cURL


    curl --location --request GET "https://api.hacienda.go.cr/indicadores/tc"


- jQuery


    $.ajax({
      "url": "https://api.hacienda.go.cr/indicadores/tc",
      "method": "GET"
    }).done(function (response) {
      console.log(response);
    });

**Respuesta esperada**


    {
      "dolar": {
        "venta": {
          "fecha": "2019-10-24T00:00:00-06:00",
          "valor": 586
        },
        "compra": {
          "fecha": "2019-10-24T00:00:00-06:00",
          "valor": 579.2
        }
      },
      "euro": {
        "fecha": "2019-10-24T00:00:00-06:00",
        "dolares": 1.1115,
        "colones": 651.34
      }
    }


## https://api.hacienda.go.cr/fe/ex

Permite obtener la información correspondiente a una exoneración.  Requiere el parámetro `autorizacion` cuyo formato debe seguir la regla `al-00000000-00`.  Si el documento de exoneración no existe se devuelve el código `404`.  Si el formato del parámetro `autorizacion` es incorrecto o no existe como parámetro, se devuelve el código `400`.

Cuando una autorización posee CABYS asociados, el campo `poseeCabys` tendrá un valor `true` y en consecuencia aparecerá el campo `cabys` que corresponde a un array de códigos CABYS.  Si el valor de `poseeCabys` es `false`, la respuesta no incluye el arreglo de códigos.

Si el documento de exoneración no existe se devuelve el código 404.  Si el formato del parámetro autorización es incorrecto o no existe como parámetro, se devuelve el código 400.

**Ejemplos**

- cURL


    curl --location --request GET "https://api.hacienda.go.cr/fe/ex?autorizacion=al-00460853-20"


- jQuery


    $.ajax({
      "url": "https://api.hacienda.go.cr/fe/ex?autorizacion=al-00460853-20",
      "method": "GET"
    }).done(function (response) {
      console.log(response);
    });

**Respuestas esperadas**


    {
      "numeroDocumento": "AL-00460853-20",
      "identificacion": "000000000",
      "codigoProyectoCFIA": 0,
      "fechaEmision": "2020-12-15T00:00:00",
      "fechaVencimiento": "2021-12-15T00:00:00",
      "porcentajeExoneracion": 13,
      "tipoDocumento": {
        "codigo": "04",
        "descripcion": "Exenciones Dirección General de Hacienda"
      },
      "nombreInstitucion": "Dirección General de Hacienda",
      "poseeCabys": true,
      "cabys": [
        "7211200000100"
      ]
    }


## https://api.hacienda.go.cr/fe/agropecuario

Permite obtener el nombre, el estado, el indicador de si está activo y la fecha de baja de los productores agropecuarios en el MAG, usando como parámetro el número de `identificaci``o``n` (sin hacer uso de guiones).  Para la consulta de identificaciones físicas nacionales es necesario incluir el 0 como primer dígito. En el caso de las identificaciones jurídicas es necesario el uso de los primeros 10 dígitos, sin utilizar los dos últimos dígitos verificadores. 

**Para mayor practicidad, se incluye también el resultado de la situación tributaria del primer endpoint (api.hacienda.go.cr/fe/ae).**

Identificaciones soportadas:

1. Físicas nacionales
2. Jurídicas nacionales
3. DIMEX


**Ejemplos**
                                                                   ****

- cURL

 

    curl --location --request GET "https://api.hacienda.go.cr/fe/agropecuario?identificacion=2100042005"

 

- jQuery

 

     $.ajax({
      "url": "https://api.hacienda.go.cr/fe/agropecuario?identificacion=2100042005",
      "method": "GET"
    }).done(function (response) {
      console.log(response);
    });

 

- PHP

 

    <?php
     
    $curl = curl_init();
     
    curl_setopt_array($curl, array(
      CURLOPT_URL => "https://api.hacienda.go.cr/fe/agropecuario?identificacion=2100042005",
      CURLOPT_RETURNTRANSFER => true,
      CURLOPT_ENCODING => "",
      CURLOPT_MAXREDIRS => 10,
      CURLOPT_TIMEOUT => 0,
      CURLOPT_FOLLOWLOCATION => false,
      CURLOPT_HTTP_VERSION => CURL_HTTP_VERSION_1_1,
      CURLOPT_CUSTOMREQUEST => "GET",
    ));
     
    $response = curl_exec($curl);
    $err = curl_error($curl);
     
    curl_close($curl);
     
    if ($err) {
      echo "cURL Error #:" . $err;
    } else {
      echo $response;
    } ?>

**Respuesta esperada**
 

    {
      "listaDatosMAG": [
        {
          "indicadorActivoMAG": true,
          "estadoMAG": "Activo",
          "fechaAltaMAG": "2020-05-18",
          "fechaBajaMAG": "2021-06-22"
          "nombreMAG": "Nombre de la empresa"
        }
      ],
      "listaDatosIncopesca": [],
      "listaDatosAcuicultores": [
        {
          "nombreAcuicultor": "Nombre de la empresa",
          "fechaVenceAcuicultor": "2023/11/15",
          "indicadorActivoAcuicultor": true
        }
      ]
    }

 
**Indicador activo**

- true = el productor se encuentra activo en el registro del MAG.
- False = el productor no se encuentra activo en el registro del MAG.

**NombreMAG**
El nombre registrado del productor.

**EstadoMAG**
Indica la vigencia del productor en el registro.

**fechaBajaMAG**
Indica el final del periodo de vigencia.

**fechaAltaMAG**
Indica la fecha en que inicia el periodo de vigencia


## https://api.hacienda.go.cr/fe/pesca

Permite obtener los siguientes datos:

- Del registro de productores agropecuarios del MAG: el nombre, el estado, el indicador de si está activo y la fecha de baja. 
- Del registro de INCOPESCA: el indicador de si está activo, el nombre del permisionario y la fecha de vencimiento. 
- Del registro de acuicultores: el indicador de si está activo, el nombre del acuicultor y la fecha de vencimiento.

 
Se usa como parámetro el número de identificación (sin hacer uso de guiones).  Para la consulta de identificaciones físicas nacionales es necesario incluir el 0 como primer dígito. En el caso de las identificaciones jurídicas es necesario el uso de los primeros 10 dígitos, sin utilizar los dos últimos dígitos verificadores.

**Para mayor practicidad, se incluye también el resultado de la situación tributaria del primer endpoint (api.hacienda.go.cr/fe/ae).**

Identificaciones soportadas:

1. Físicas nacionales
2. Jurídicas nacionales
3. DIMEX

**Ejemplos**

- cURL

 

    curl --location --request GET "https://api.hacienda.go.cr/fe/pesca?identificacion=2100042005"

 

- jQuery

 

    $.ajax({
      "url": "https://api.hacienda.go.cr/fe/pesca?identificacion=2100042005",
      "method": "GET"
    }).done(function (response) {
      console.log(response);
    });

 

- PHP

 

    <?php
     
    $curl = curl_init();
     
    curl_setopt_array($curl, array(
      CURLOPT_URL => "https://api.hacienda.go.cr/fe/pesca?identificacion=2100042005",
      CURLOPT_RETURNTRANSFER => true,
      CURLOPT_ENCODING => "",
      CURLOPT_MAXREDIRS => 10,
      CURLOPT_TIMEOUT => 0,
      CURLOPT_FOLLOWLOCATION => false,
      CURLOPT_HTTP_VERSION => CURL_HTTP_VERSION_1_1,
      CURLOPT_CUSTOMREQUEST => "GET",
    ));
     
    $response = curl_exec($curl);
    $err = curl_error($curl);
     
    curl_close($curl);
     
    if ($err) {
      echo "cURL Error #:" . $err;
    } else {
      echo $response;
    } ?>

 
**Respuesta esperada**
 

     {
      "listaDatosMAG": [
        {
          "indicadorActivoMAG": true,
          "estadoMAG": "Activo",
          "fechaAltaMAG": "2020-02-17",
          "nombreMAG": "CUBERO MORERA REYES BRAULIO",
          "fechaBajaMAG": "2021-02-17"
        }
      ],
      "listaDatosIncopesca": [
        {
          "indicadorActivoIncopesca": true,
          "nombrePermisionarioIncopesca": "REYES BRAULIO CUBERO MORALES",
          "fechaVenceIncopesca": "2021/3/15"
        }
      ],
      "listaDatosAcuicultores": []
    }

**listaDatosMAG**
**indicadorActivoMAG**:

- true: el pescador se encuentra en estado activo en el registro del MAG.
- false: el pescador se encuentra en estado inactivo en el registro del MAG.

**estadoMAG**:
Indica si la licencia del pescador se encuentra vigente.

**fechaBajaMAG**: indica el final de la vigencia de la licencia.

**nombreMAG**: es el nombre con el que el pescador aparece registrado.

**listaDatosIncopesca**
**indicadorActivoIncopesca**: incluye la fecha a partir de la que está activa la autorización.


**n****ombrePermisionarioIncopesca**: es el nombre con el que el pescador aparece en el listado de INCOPESCA.

**f****echaVenceIncopesca**: indica el final de la vigencia del registro en INCOPESCA.

**listaDatosAcuicultores**
**nombreAcuicultor**: indica el nombre con que aparece registrada la persona en la lista de acuicultores.
**fechaVenceAcuicultor**: indica la fecha de vencimiento.
**indicadorActivoAcuicultor**: indica si el acuicultor se encuentra activo.


## https://api.hacienda.go.cr/fe/cabys

Permite obtener la información correspondiente al [Catalago de Bienes y Servicios](https://www.hacienda.go.cr/contenido/15808-catalogo-de-bienes-y-servicios-cabys) (CABYS), a partir de la descripción de los bienes y servicios o su número de código.  Puede utilizar los parámetros `q` o `codigo` de la siguiente manera:

**Por descripción del bien o servicio**
https://api.hacienda.go.cr/fe/cabys?q=jugo%20de%20tomate

![](https://paper-attachments.dropbox.com/s_A029D78A5A7794415E4633BC0FE576463B3FDE32328E8125919BF80E6AC047E7_1600967605188_image.png)


**Por el código del bien o servicio**
https://api.hacienda.go.cr/fe/cabys?codigo=2132100000100

![](https://paper-attachments.dropbox.com/s_A029D78A5A7794415E4633BC0FE576463B3FDE32328E8125919BF80E6AC047E7_1600990901763_image.png)


El parámetro `q` puede ser usado en combinación con el parámetro `top` para hacer una búsqueda limitada de bienes y servicios de la siguiente forma:

https://api.hacienda.go.cr/fe/cabys?q=jugo%20de%20tomate&top=2

![](https://paper-attachments.dropbox.com/s_A029D78A5A7794415E4633BC0FE576463B3FDE32328E8125919BF80E6AC047E7_1600967806654_image.png)


**Ejemplos**


- cURL


    curl --location --request GET "https://api.hacienda.go.cr/fe/cabys?q=jugo%20de%20tomate&top=2"


    curl --location --request GET "https://api.hacienda.go.cr/fe/cabys?codigo=2132100000100"


- jQuery


    $.ajax({
      "url": "https://api.hacienda.go.cr/fe/cabys?q=jugo%20de%20tomate&top=2",
      "method": "GET"
    }).done(function (response) {
      console.log(response);
    });


    $.ajax({
      "url": "https://api.hacienda.go.cr/fe/cabys?codigo=2132100000100",
      "method": "GET"
    }).done(function (response) {
      console.log(response);
    });

**Respuestas esperadas**


    {
      "total": 71,
      "cantidad": 2,
      "cabys": [
        {
          "codigo": "2132100000100",
          "descripcion": "Jugo de tomate concentrado",
          "categorias": [
            "Productos alimenticios, bebidas y tabaco; textiles, prendas de vestir y productos de cuero",
            "Carnes, pescados, frutas, verduras, aceites y grasas",
            "Vegetales, legumbres y papas, preparados y conservados",
            "Jugos de vegetales",
            "Jugo de tomate",
            "Jugo de tomate",
            "Jugo de tomate",
            "Jugo de tomate concentrado"
          ],
          "impuesto": 13,
          "uri": "https://api.hacienda.go.cr/fe/cabys?codigo=2132100000100"
        },
        {
          "codigo": "2132100000200",
          "descripcion": "Jugo de tomate natural",
          "categorias": [
            "Productos alimenticios, bebidas y tabaco; textiles, prendas de vestir y productos de cuero",
            "Carnes, pescados, frutas, verduras, aceites y grasas",
            "Vegetales, legumbres y papas, preparados y conservados",
            "Jugos de vegetales",
            "Jugo de tomate",
            "Jugo de tomate",
            "Jugo de tomate",
            "Jugo de tomate natural"
          ],
          "impuesto": 13,
          "uri": "https://api.hacienda.go.cr/fe/cabys?codigo=2132100000200"
        }
      ]
    }


    [
      {
        "codigo": "2132100000100",
        "categorias": [
          "Productos alimenticios, bebidas y tabaco; textiles, prendas de vestir y productos de cuero",
          "Carnes, pescados, frutas, verduras, aceites y grasas",
          "Vegetales, legumbres y papas, preparados y conservados",
          "Jugos de vegetales",
          "Jugo de tomate",
          "Jugo de tomate",
          "Jugo de tomate",
          "Jugo de tomate concentrado"
        ],
        "descripcion": "Jugo de tomate concentrado",
        "impuesto": 13
      }
    ]

Existe un recurso de ayuda para realizar las consultas por descripción publicado en la url https://api.hacienda.go.cr/fe/cabys/test

![](https://paper-attachments.dropbox.com/s_A029D78A5A7794415E4633BC0FE576463B3FDE32328E8125919BF80E6AC047E7_1600998065695_image.png)


**¿Cómo convertir el archivo Excel de Cabys a un archivo JSON?**

El ejemplo en lenguaje Python:

https://gist.github.com/melendezgg/ac482c4ffcbb421fa76590f2d917f470


[https://gist.github.com/melendezgg/ac482c4ffcbb421fa76590f2d917f470](https://gist.github.com/melendezgg/ac482c4ffcbb421fa76590f2d917f470)


## Códigos de estado de respuesta HTTP
- Cualquier estado distinto al 200, puede considerarse como un error de la solicitud:
- 404 cuando una identificación no aparece registrada
- 400 cuando el número de identificación enviado en el request es considerado como un dato no válido.

