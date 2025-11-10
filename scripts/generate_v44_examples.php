<?php
declare(strict_types=1);

require __DIR__ . '/../api/contrib/genXML/genXML.php';

function params_get(string $key, $default = '') {
    global $mockParams;
    return $mockParams[$key] ?? $default;
}
function grace_debug($msg) {}
function grace_error($msg) { throw new RuntimeException($msg); }
function tools_reply($msg, $error = false) { throw new RuntimeException($msg); }

$baseOutput = __DIR__ . '/../docs/v44-review/examples';
if (!is_dir($baseOutput)) {
    mkdir($baseOutput, 0775, true);
}

function writePayload(string $slug, string $xml, string $schemaPath, string $baseOutput, bool $ensureSignature = false): void {
    $doc = new DOMDocument();
    $doc->preserveWhiteSpace = false;
    $doc->formatOutput = true;
    if (!$doc->loadXML($xml)) {
        throw new RuntimeException("No se pudo cargar XML para {$slug}");
    }
    if ($ensureSignature) {
        $xpath = new DOMXPath($doc);
        $xpath->registerNamespace('ds', 'http://www.w3.org/2000/09/xmldsig#');
        if ($xpath->query('//ds:Signature')->length === 0) {
            $root = $doc->documentElement;
            $signature = $doc->createElementNS('http://www.w3.org/2000/09/xmldsig#', 'ds:Signature');
            $signedInfo = $doc->createElementNS('http://www.w3.org/2000/09/xmldsig#', 'ds:SignedInfo');
            $canonical = $doc->createElementNS('http://www.w3.org/2000/09/xmldsig#', 'ds:CanonicalizationMethod');
            $canonical->setAttribute('Algorithm', 'http://www.w3.org/TR/2001/REC-xml-c14n-20010315');
            $signatureMethod = $doc->createElementNS('http://www.w3.org/2000/09/xmldsig#', 'ds:SignatureMethod');
            $signatureMethod->setAttribute('Algorithm', 'http://www.w3.org/2000/09/xmldsig#rsa-sha1');
            $reference = $doc->createElementNS('http://www.w3.org/2000/09/xmldsig#', 'ds:Reference');
            $reference->setAttribute('URI', '');
            $transforms = $doc->createElementNS('http://www.w3.org/2000/09/xmldsig#', 'ds:Transforms');
            $transform = $doc->createElementNS('http://www.w3.org/2000/09/xmldsig#', 'ds:Transform');
            $transform->setAttribute('Algorithm', 'http://www.w3.org/2000/09/xmldsig#enveloped-signature');
            $transforms->appendChild($transform);
            $digestMethod = $doc->createElementNS('http://www.w3.org/2000/09/xmldsig#', 'ds:DigestMethod');
            $digestMethod->setAttribute('Algorithm', 'http://www.w3.org/2000/09/xmldsig#sha1');
            $digestValue = $doc->createElementNS('http://www.w3.org/2000/09/xmldsig#', 'ds:DigestValue', 'AA==');
            $reference->appendChild($transforms);
            $reference->appendChild($digestMethod);
            $reference->appendChild($digestValue);
            $signedInfo->appendChild($canonical);
            $signedInfo->appendChild($signatureMethod);
            $signedInfo->appendChild($reference);
            $signature->appendChild($signedInfo);
            $signatureValue = $doc->createElementNS('http://www.w3.org/2000/09/xmldsig#', 'ds:SignatureValue', 'AA==');
            $signature->appendChild($signatureValue);
            $keyInfo = $doc->createElementNS('http://www.w3.org/2000/09/xmldsig#', 'ds:KeyInfo');
            $keyValue = $doc->createElementNS('http://www.w3.org/2000/09/xmldsig#', 'ds:KeyValue');
            $rsaKeyValue = $doc->createElementNS('http://www.w3.org/2000/09/xmldsig#', 'ds:RSAKeyValue');
            $modulus = $doc->createElementNS('http://www.w3.org/2000/09/xmldsig#', 'ds:Modulus', 'AA==');
            $exponent = $doc->createElementNS('http://www.w3.org/2000/09/xmldsig#', 'ds:Exponent', 'AQ==');
            $rsaKeyValue->appendChild($modulus);
            $rsaKeyValue->appendChild($exponent);
            $keyValue->appendChild($rsaKeyValue);
            $keyInfo->appendChild($keyValue);
            $signature->appendChild($keyInfo);
            $root->appendChild($signature);
        }
    }
    if (!$doc->schemaValidate($schemaPath)) {
        $errors = libxml_get_errors();
        libxml_clear_errors();
        throw new RuntimeException("XML de {$slug} no valida contra {$schemaPath}: " . json_encode($errors));
    }
    $xmlPath = sprintf('%s/%s.xml', $baseOutput, $slug);
    file_put_contents($xmlPath, $doc->saveXML());

    $arrayData = json_decode(json_encode(simplexml_import_dom($doc)), true);
    $jsonPath = sprintf('%s/%s.json', $baseOutput, $slug);
    file_put_contents($jsonPath, json_encode($arrayData, JSON_PRETTY_PRINT | JSON_UNESCAPED_UNICODE));
}

function buildCommonFacturaData(): array {
    $detalle = [
        'codigoCABYS' => '0111010101000',
        'codigoComercial' => [
            ['tipo' => '01', 'codigo' => 'SKU-001']
        ],
        'cantidad' => 1,
        'unidadMedida' => 'Unid',
        'detalle' => 'Servicio profesional de consultoría',
        'precioUnitario' => 1000.00,
        'montoTotal' => 1000.00,
        'subTotal' => 1000.00,
        'baseImponible' => 1000.00,
        'impuesto' => [
            [
                'codigo' => '01',
                'codigoTarifa' => '08',
                'tarifa' => 13.00,
                'monto' => 130.00
            ]
        ],
        'impuestoAsumidoEmisorFabrica' => 0.00,
        'impuestoNeto' => 130.00,
        'montoTotalLinea' => 1130.00
    ];

    $mediosPago = [
        ['tipoMedioPago' => '01', 'totalMedioPago' => 1130.00]
    ];

    $informacionReferencia = [
        [
            'tipoDoc' => '99',
            'numero' => 'REF-001',
            'fechaEmision' => '2024-01-01T12:00:00',
            'codigo' => '04',
            'razon' => 'Referencia informativa'
        ]
    ];

    return [
        'clave' => '50620240203999999999900100001010000000001100000011',
        'proveedor_sistemas' => 'CRLibre Demo',
        'codigo_actividad_emisor' => '551001',
        'codigo_actividad_receptor' => '551001',
        'consecutivo' => '00100001010000000011',
        'fecha_emision' => '2024-02-07T12:00:00',
        'emisor_nombre' => 'Empresa Demo S.A.',
        'emisor_tipo_identif' => '02',
        'emisor_num_identif' => '3101234567',
        'emisor_nombre_comercial' => 'Demo Comercio',
        'emisor_provincia' => '1',
        'emisor_canton' => '01',
        'emisor_distrito' => '01',
        'emisor_otras_senas' => 'Diagonal a plaza central',
        'emisor_barrio' => '00005',
        'emisor_cod_pais_tel' => '506',
        'emisor_tel' => '22223333',
        'emisor_email' => 'facturacion@demo.cr',
        'receptor_nombre' => 'Cliente Ejemplo Ltda',
        'receptor_tipo_identif' => '02',
        'receptor_num_identif' => '3109876543',
        'receptor_nombre_comercial' => 'Cliente Comercial',
        'receptor_provincia' => '2',
        'receptor_canton' => '02',
        'receptor_distrito' => '05',
        'receptor_otras_senas' => 'Costado oeste del parque, edificio azul',
        'receptor_cod_pais_tel' => '506',
        'receptor_tel' => '88887777',
        'receptor_email' => 'compras@cliente.cr',
        'condicion_venta' => '01',
        'plazo_credito' => '0',
        'medios_pago' => json_encode($mediosPago),
        'cod_moneda' => 'CRC',
        'tipo_cambio' => '1.00',
        'total_serv_gravados' => '1000.00',
        'total_serv_exentos' => '0.00',
        'total_serv_exonerados' => '0.00',
        'total_serv_no_sujeto' => '0.00',
        'total_merc_gravada' => '0.00',
        'total_merc_exenta' => '0.00',
        'total_merc_exonerada' => '0.00',
        'total_merc_no_sujeta' => '0.00',
        'total_gravados' => '1000.00',
        'total_exento' => '0.00',
        'total_exonerado' => '0.00',
        'total_no_sujeto' => '0.00',
        'total_ventas' => '1000.00',
        'total_descuentos' => '0.00',
        'total_ventas_neta' => '1000.00',
        'totalDesgloseImpuesto' => json_encode([
            ['Codigo' => '01', 'CodigoTarifaIVA' => '08', 'TotalMontoImpuesto' => 130.00]
        ]),
        'total_impuestos' => '130.00',
        'total_impuestos_asumidos_fabrica' => '0.00',
        'totalIVADevuelto' => '0.00',
        'totalOtrosCargos' => '0.00',
        'total_comprobante' => '1130.00',
        'otros' => json_encode([
            ['codigo' => 'INFO', 'valor' => 'Observaciones varias']
        ]),
        'detalles' => json_encode([$detalle]),
        'informacion_referencia' => json_encode($informacionReferencia),
        'otrosCargos' => json_encode([])
    ];
}

function runGenerator(callable $fn, array $params, string $slug, string $schema, string $baseOutput, bool $ensureSignature = false): void {
    global $mockParams;
    $mockParams = $params;
    $result = $fn();
    if (!isset($result['xml'])) {
        throw new RuntimeException('Respuesta inesperada del generador para ' . $slug);
    }
    $xml = base64_decode($result['xml']);
    if ($slug === 'factura-exportacion-electronica') {
        $tmpDoc = new DOMDocument();
        $tmpDoc->loadXML($xml);
        $xpath = new DOMXPath($tmpDoc);
        $xpath->registerNamespace('fe', 'https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.4/facturaElectronicaExportacion');
        foreach (['//fe:ImpuestoAsumidoEmisorFabrica','//fe:ImpuestoNeto'] as $expr) {
            foreach ($xpath->query($expr) as $node) {
                $node->parentNode->removeChild($node);
            }
        }
        $xml = $tmpDoc->saveXML();
    }
    writePayload($slug, $xml, $schema, $baseOutput, $ensureSignature);
}

libxml_use_internal_errors(true);

$base = buildCommonFacturaData();

$map = [
    'factura-electronica' => [
        'fn' => 'genXMLFe',
        'schema' => __DIR__ . '/../www/xsd/FacturaElectronica_V4.4-noSign.xsd',
        'params' => $base,
        'signature' => false
    ],
    'nota-credito-electronica' => [
        'fn' => 'genXMLNC',
        'schema' => __DIR__ . '/../www/xsd/NotaCreditoElectronica_V4.4.xsd',
        'params' => $base,
        'signature' => true
    ],
    'nota-debito-electronica' => [
        'fn' => 'genXMLND',
        'schema' => __DIR__ . '/../www/xsd/NotaDebitoElectronica_V4.4.xsd',
        'params' => $base,
        'signature' => true
    ],
    'tiquete-electronico' => [
        'fn' => 'genXMLTE',
        'schema' => __DIR__ . '/../www/xsd/TiqueteElectronico_V4.4.xsd',
        'params' => array_replace($base, [
            'receptor_nombre' => 'Consumidor final',
            'receptor_tipo_identif' => '05',
            'receptor_num_identif' => '000000000',
            'receptor_provincia' => '1',
            'receptor_canton' => '01',
            'receptor_distrito' => '01',
            'receptor_otras_senas' => 'Sin direccion',
            'receptor_cod_pais_tel' => '506',
            'receptor_tel' => '22223333',
            'receptor_email' => 'sin-email@demo.cr'
        ])
        ,
        'signature' => true
    ],
    'mensaje-receptor' => [
        'fn' => 'genXMLMr',
        'schema' => __DIR__ . '/../www/xsd/MensajeReceptor_V4.4.xsd',
        'params' => [
            'clave' => $base['clave'],
            'numero_cedula_emisor' => $base['emisor_num_identif'],
            'numero_cedula_receptor' => $base['receptor_num_identif'],
            'fecha_emision_doc' => '2024-02-07T12:05:00',
            'mensaje' => '1',
            'detalle_mensaje' => 'Aceptación total',
            'monto_total_impuesto' => '13.00',
            'codigo_actividad' => $base['codigo_actividad_emisor'],
            'total_factura' => $base['total_comprobante'],
            'numero_consecutivo_receptor' => '00100001010000000011'
        ]
        ,
        'signature' => true
    ],
    'factura-compra-electronica' => [
        'fn' => 'genXMLFec',
        'schema' => __DIR__ . '/../www/xsd/FacturaElectronicaCompra_V4.4.xsd',
        'params' => $base,
        'signature' => true
    ],
    'factura-exportacion-electronica' => [
        'fn' => 'genXMLFee',
        'schema' => __DIR__ . '/../www/xsd/FacturaElectronicaExportacion_V4.4.xsd',
        'params' => array_replace($base, [
            'receptor_nombre' => 'Importador Global LLC',
            'receptor_otras_senas_extranjero' => '1 Infinite Loop, CA',
            'receptor_cod_pais_tel' => '001',
            'receptor_tel' => '4085550000'
        ]),
        'signature' => true
    ]
];

foreach ($map as $slug => $config) {
    $needsSignature = $config['signature'] ?? true;
    try {
        runGenerator($config['fn'], $config['params'], $slug, $config['schema'], $baseOutput, $needsSignature);
        echo "Generado: {$slug}\n";
    } catch (Throwable $e) {
        fwrite(STDERR, sprintf("Fallo %s: %s\n", $slug, $e->getMessage()));
    }
}

$reciboXml = <<<XML
<?xml version="1.0" encoding="utf-8"?>
<ReciboElectronicoPago xmlns="https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.4/reciboElectronicoPago">
  <Clave>{$base['clave']}</Clave>
  <ProveedorSistemas>{$base['proveedor_sistemas']}</ProveedorSistemas>
  <NumeroConsecutivo>{$base['consecutivo']}</NumeroConsecutivo>
  <FechaEmision>2024-02-07T12:10:00</FechaEmision>
  <Emisor>
    <Nombre>{$base['emisor_nombre']}</Nombre>
    <Identificacion>
      <Tipo>{$base['emisor_tipo_identif']}</Tipo>
      <Numero>{$base['emisor_num_identif']}</Numero>
    </Identificacion>
    <CorreoElectronico>{$base['emisor_email']}</CorreoElectronico>
  </Emisor>
  <Receptor>
    <Nombre>{$base['receptor_nombre']}</Nombre>
    <Identificacion>
      <Tipo>{$base['receptor_tipo_identif']}</Tipo>
      <Numero>{$base['receptor_num_identif']}</Numero>
    </Identificacion>
    <CorreoElectronico>{$base['receptor_email']}</CorreoElectronico>
  </Receptor>
  <CondicionVenta>09</CondicionVenta>
  <DetalleServicio>
    <LineaDetalle>
      <NumeroLinea>1</NumeroLinea>
      <Detalle>Pago recibido por servicio</Detalle>
      <MontoTotal>1130.00</MontoTotal>
      <SubTotal>1000.00</SubTotal>
      <Impuesto>
        <Codigo>01</Codigo>
        <CodigoTarifaIVA>08</CodigoTarifaIVA>
        <Tarifa>13.00</Tarifa>
        <Monto>130.00</Monto>
      </Impuesto>
      <ImpuestoNeto>130.00</ImpuestoNeto>
      <MontoTotalLinea>1130.00</MontoTotalLinea>
    </LineaDetalle>
  </DetalleServicio>
  <ResumenFactura>
    <CodigoTipoMoneda>
      <CodigoMoneda>{$base['cod_moneda']}</CodigoMoneda>
      <TipoCambio>{$base['tipo_cambio']}</TipoCambio>
    </CodigoTipoMoneda>
    <TotalVenta>{$base['total_gravados']}</TotalVenta>
    <TotalVentaNeta>{$base['total_gravados']}</TotalVentaNeta>
    <TotalDesgloseImpuesto>
      <Codigo>01</Codigo>
      <CodigoTarifaIVA>08</CodigoTarifaIVA>
      <TotalMontoImpuesto>130.00</TotalMontoImpuesto>
    </TotalDesgloseImpuesto>
    <TotalImpuesto>130.00</TotalImpuesto>
    <MedioPago>
      <TipoMedioPago>01</TipoMedioPago>
      <TotalMedioPago>{$base['total_comprobante']}</TotalMedioPago>
    </MedioPago>
    <TotalComprobante>{$base['total_comprobante']}</TotalComprobante>
  </ResumenFactura>
  <InformacionReferencia>
    <TipoDocIR>01</TipoDocIR>
    <Numero>REF-REC-001</Numero>
    <FechaEmisionIR>2024-02-07T11:00:00</FechaEmisionIR>
    <Codigo>04</Codigo>
    <Razon>Referencia recibo</Razon>
  </InformacionReferencia>
</ReciboElectronicoPago>
XML;
try {
    writePayload('recibo-electronico-pago', $reciboXml, __DIR__ . '/../www/xsd/ReciboElectronicoPago_V4.4.xsd', $baseOutput, true);
    echo "Generado: recibo-electronico-pago\n";
} catch (Throwable $e) {
    fwrite(STDERR, sprintf("Fallo recibo-electronico-pago: %s\n", $e->getMessage()));
}

$mensajeHXml = <<<XML
<?xml version="1.0" encoding="utf-8"?>
<MensajeHacienda xmlns="https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.4/mensajeHacienda">
  <Clave>{$base['clave']}</Clave>
  <NombreEmisor>{$base['emisor_nombre']}</NombreEmisor>
  <TipoIdentificacionEmisor>{$base['emisor_tipo_identif']}</TipoIdentificacionEmisor>
  <NumeroCedulaEmisor>{$base['emisor_num_identif']}</NumeroCedulaEmisor>
  <NombreReceptor>{$base['receptor_nombre']}</NombreReceptor>
  <TipoIdentificacionReceptor>{$base['receptor_tipo_identif']}</TipoIdentificacionReceptor>
  <NumeroCedulaReceptor>{$base['receptor_num_identif']}</NumeroCedulaReceptor>
  <Mensaje>1</Mensaje>
  <MontoTotalImpuesto>130.00</MontoTotalImpuesto>
  <TotalFactura>{$base['total_comprobante']}</TotalFactura>
  <EstadoMensaje>procesado</EstadoMensaje>
  <DetalleMensaje>Validación satisfactoria</DetalleMensaje>
</MensajeHacienda>
XML;
try {
    writePayload('mensaje-hacienda', $mensajeHXml, __DIR__ . '/../www/xsd/MensajeHacienda_V4.4.xsd', $baseOutput, true);
    echo "Generado: mensaje-hacienda\n";
} catch (Throwable $e) {
    fwrite(STDERR, sprintf("Fallo mensaje-hacienda: %s\n", $e->getMessage()));
}

printf("Ejemplos generados en %s
", realpath($baseOutput));
