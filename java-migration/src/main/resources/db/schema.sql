CREATE TABLE IF NOT EXISTS comprobante (
    id SERIAL PRIMARY KEY,
    clave VARCHAR(50) UNIQUE NOT NULL,
    consecutivo VARCHAR(50) NOT NULL,
    tipo VARCHAR(40) NOT NULL,
    fecha_emision TIMESTAMP NOT NULL,
    emisor_nombre VARCHAR(120),
    emisor_identificacion VARCHAR(20),
    emisor_tipo_identificacion VARCHAR(5),
    emisor_email VARCHAR(120),
    receptor_nombre VARCHAR(120),
    receptor_identificacion VARCHAR(20),
    receptor_tipo_identificacion VARCHAR(5),
    receptor_email VARCHAR(120),
    total_serv_gravados NUMERIC(15,5),
    total_serv_exentos NUMERIC(15,5),
    total_mercancias_gravadas NUMERIC(15,5),
    total_mercancias_exentas NUMERIC(15,5),
    total_gravado NUMERIC(15,5),
    total_exento NUMERIC(15,5),
    total_venta NUMERIC(15,5),
    total_descuentos NUMERIC(15,5),
    total_venta_neta NUMERIC(15,5),
    total_impuesto NUMERIC(15,5),
    total_comprobante NUMERIC(15,5),
    xml_firmado TEXT,
    estado_hacienda VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS detalle_linea (
    id SERIAL PRIMARY KEY,
    comprobante_id INTEGER NOT NULL REFERENCES comprobante(id) ON DELETE CASCADE,
    numero_linea INTEGER,
    codigo VARCHAR(50),
    descripcion TEXT,
    cantidad NUMERIC(15,5),
    precio_unitario NUMERIC(15,5),
    subtotal NUMERIC(15,5),
    descuento NUMERIC(15,5),
    total_linea NUMERIC(15,5),
    codigo_impuesto VARCHAR(10),
    tarifa_impuesto NUMERIC(5,2),
    monto_impuesto NUMERIC(15,5),
    monto_exonerado NUMERIC(15,5),
    exoneracion_documento VARCHAR(40),
    exoneracion_fecha DATE,
    exoneracion_institucion VARCHAR(120),
    exoneracion_porcentaje VARCHAR(10)
);

CREATE TABLE IF NOT EXISTS envio_historial (
    id SERIAL PRIMARY KEY,
    comprobante_id INTEGER NOT NULL REFERENCES comprobante(id) ON DELETE CASCADE,
    fecha_envio TIMESTAMP NOT NULL,
    estado VARCHAR(40),
    respuesta_hacienda TEXT
);

CREATE TABLE IF NOT EXISTS estado_hacienda (
    id SERIAL PRIMARY KEY,
    comprobante_id INTEGER UNIQUE NOT NULL REFERENCES comprobante(id) ON DELETE CASCADE,
    estado VARCHAR(40),
    detalle TEXT,
    fecha_actualizacion TIMESTAMP
);

CREATE TABLE IF NOT EXISTS reintento_log (
    id SERIAL PRIMARY KEY,
    comprobante_id INTEGER NOT NULL REFERENCES comprobante(id) ON DELETE CASCADE,
    fecha_intento TIMESTAMP NOT NULL,
    resultado VARCHAR(40),
    mensaje_error TEXT
);

CREATE TABLE IF NOT EXISTS mensaje_receptor (
    id SERIAL PRIMARY KEY,
    clave_referencia VARCHAR(50) NOT NULL,
    consecutivo_referencia VARCHAR(50) NOT NULL,
    mensaje VARCHAR(20) NOT NULL,
    detalle_mensaje TEXT,
    condicion_impuesto VARCHAR(10),
    fecha_emision TIMESTAMP NOT NULL,
    xml_mensaje TEXT
);
