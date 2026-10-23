create database if not exists La_Estancia;
use La_Estancia;

-- Usuarios

CREATE TABLE usuario (
    id_usuario INT UNSIGNED AUTO_INCREMENT,
    nombre_usuario VARCHAR(50) NOT NULL,
    email VARCHAR(100),
    contrasenia_hash VARCHAR(255) NOT NULL,
    rol ENUM('JUGADOR', 'ADMIN') NOT NULL DEFAULT 'JUGADOR',
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id_usuario),
    UNIQUE (nombre_usuario),
    UNIQUE (email)
);

-- Partidas

CREATE TABLE partida (
    id_partida INT UNSIGNED AUTO_INCREMENT,
    id_usuario INT UNSIGNED NOT NULL,
    nombre VARCHAR(50) NOT NULL,
    nivel INT UNSIGNED NOT NULL DEFAULT 1,
    experiencia INT UNSIGNED NOT NULL DEFAULT 0,
    monedas INT UNSIGNED NOT NULL DEFAULT 100,
    dia_juego INT UNSIGNED NOT NULL DEFAULT 0,
    hora_juego TINYINT UNSIGNED NOT NULL DEFAULT 8,
    estado ENUM('ACTIVA', 'PAUSADA', 'FINALIZADA')
        NOT NULL DEFAULT 'ACTIVA',
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_guardado DATETIME,
    activo BOOLEAN NOT NULL DEFAULT TRUE,

    PRIMARY KEY (id_partida),

    FOREIGN KEY (id_usuario)
        REFERENCES usuario(id_usuario)
);

-- Granjas

CREATE TABLE granja (
    id_granja INT UNSIGNED AUTO_INCREMENT,
    id_partida INT UNSIGNED NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    nivel INT UNSIGNED NOT NULL DEFAULT 1,

    PRIMARY KEY (id_granja),
    UNIQUE (id_partida),

    FOREIGN KEY (id_partida)
        REFERENCES partida(id_partida)
);

-- Parcelas

CREATE TABLE parcela (
    id_parcela INT UNSIGNED AUTO_INCREMENT,
    id_granja INT UNSIGNED NOT NULL,
    numero SMALLINT UNSIGNED NOT NULL,
    tipo_zona ENUM('RANCHO', 'CULTIVO', 'PESCA')
        NOT NULL DEFAULT 'CULTIVO',
    desbloqueada BOOLEAN NOT NULL DEFAULT TRUE,

    PRIMARY KEY (id_parcela),
    UNIQUE (id_granja, numero),

    FOREIGN KEY (id_granja)
        REFERENCES granja(id_granja)
);

-- Cultivos

CREATE TABLE cultivo (
    id_cultivo INT UNSIGNED AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL,
    precio_semilla INT UNSIGNED NOT NULL,
    precio_venta INT UNSIGNED NOT NULL,
    tiempo_crecimiento_min INT UNSIGNED NOT NULL,
    experiencia_recompensa INT UNSIGNED NOT NULL DEFAULT 0,
    activo BOOLEAN NOT NULL DEFAULT TRUE,

    PRIMARY KEY (id_cultivo),
    UNIQUE (nombre)
);

-- Plantaciones

CREATE TABLE plantacion (
    id_plantacion INT UNSIGNED AUTO_INCREMENT,
    id_parcela INT UNSIGNED NOT NULL,
    id_cultivo INT UNSIGNED NOT NULL,
    fecha_siembra DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    progreso TINYINT UNSIGNED NOT NULL DEFAULT 0,
    estado ENUM('CRECIENDO', 'LISTA', 'COSECHADA')
        NOT NULL DEFAULT 'CRECIENDO',

    PRIMARY KEY (id_plantacion),

    FOREIGN KEY (id_parcela)
        REFERENCES parcela(id_parcela),

    FOREIGN KEY (id_cultivo)
        REFERENCES cultivo(id_cultivo)
);


-- Inventario e ítems

CREATE TABLE item (
    id_item INT UNSIGNED AUTO_INCREMENT,
    nombre VARCHAR(80) NOT NULL UNIQUE,
    descripcion VARCHAR(255),
    tipo ENUM(
        'SEMILLA',
        'COSECHA',
        'RECURSO',
        'OTRO'
    ) NOT NULL DEFAULT 'OTRO',
    apilable BOOLEAN NOT NULL DEFAULT TRUE,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id_item)
);

CREATE TABLE inventario (
    id_inventario INT UNSIGNED AUTO_INCREMENT,
    id_partida INT UNSIGNED NOT NULL,
    id_item INT UNSIGNED NOT NULL,
    cantidad INT UNSIGNED NOT NULL DEFAULT 0,
    PRIMARY KEY (id_inventario),
    UNIQUE (id_partida, id_item),
    FOREIGN KEY (id_partida)
        REFERENCES partida(id_partida)
        ON DELETE CASCADE,
    FOREIGN KEY (id_item)
        REFERENCES item(id_item)
);

-- Herramientas

CREATE TABLE herramienta (
    id_herramienta INT UNSIGNED AUTO_INCREMENT,
    id_item INT UNSIGNED NOT NULL UNIQUE,
    tipo ENUM(
        'AZADA',
        'HACHA',
        'PICO',
        'PESCA',
        'OTRA'
    ) NOT NULL,
    nivel_requerido TINYINT UNSIGNED NOT NULL DEFAULT 1,
    poder SMALLINT UNSIGNED NOT NULL DEFAULT 1,

    PRIMARY KEY (id_herramienta),

    FOREIGN KEY (id_item)
        REFERENCES item(id_item)
);

CREATE TABLE herramienta_jugador (
    id_herramienta_jugador INT UNSIGNED AUTO_INCREMENT,
    id_partida INT UNSIGNED NOT NULL,
    id_herramienta INT UNSIGNED NOT NULL,
    nivel TINYINT UNSIGNED NOT NULL DEFAULT 1,
    equipada BOOLEAN NOT NULL DEFAULT FALSE,
    fecha_adquisicion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id_herramienta_jugador),

    FOREIGN KEY (id_partida)
        REFERENCES partida(id_partida)
        ON DELETE CASCADE,

    FOREIGN KEY (id_herramienta)
        REFERENCES herramienta(id_herramienta)
);

-- Animales

CREATE TABLE tipo_animal (
    id_tipo_animal INT UNSIGNED AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(255),
    precio_compra INT UNSIGNED NOT NULL DEFAULT 0,
    precio_venta INT UNSIGNED NOT NULL DEFAULT 0,
    id_item_producto INT UNSIGNED,
    cantidad_producto INT UNSIGNED NOT NULL DEFAULT 1,
    tiempo_produccion_min INT UNSIGNED NOT NULL DEFAULT 1440,
    activo BOOLEAN NOT NULL DEFAULT TRUE,

    PRIMARY KEY (id_tipo_animal),

    FOREIGN KEY (id_item_producto)
        REFERENCES item(id_item)
);

CREATE TABLE animal (
    id_animal INT UNSIGNED AUTO_INCREMENT,
    id_granja INT UNSIGNED NOT NULL,
    id_tipo_animal INT UNSIGNED NOT NULL,
    nombre VARCHAR(50),
    hambre TINYINT UNSIGNED NOT NULL DEFAULT 100,
    fecha_adquisicion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ultima_produccion DATETIME,
    activo BOOLEAN NOT NULL DEFAULT TRUE,

    PRIMARY KEY (id_animal),

    FOREIGN KEY (id_granja)
        REFERENCES granja(id_granja)
        ON DELETE CASCADE,

    FOREIGN KEY (id_tipo_animal)
        REFERENCES tipo_animal(id_tipo_animal)
);

-- Construcciones

CREATE TABLE tipo_construccion (
    id_tipo_construccion INT UNSIGNED AUTO_INCREMENT,
    nombre VARCHAR(80) NOT NULL UNIQUE,
    descripcion VARCHAR(255),
    costo_monedas INT UNSIGNED NOT NULL DEFAULT 0,
    nivel_requerido INT UNSIGNED NOT NULL DEFAULT 1,
    nivel_maximo TINYINT UNSIGNED NOT NULL DEFAULT 3,
    activo BOOLEAN NOT NULL DEFAULT TRUE,

    PRIMARY KEY (id_tipo_construccion)
);

CREATE TABLE construccion (
    id_construccion INT UNSIGNED AUTO_INCREMENT,
    id_granja INT UNSIGNED NOT NULL,
    id_tipo_construccion INT UNSIGNED NOT NULL,
    nivel TINYINT UNSIGNED NOT NULL DEFAULT 1,
    estado ENUM(
        'ACTIVA',
        'EN_CONSTRUCCION',
        'MEJORANDO'
    ) NOT NULL DEFAULT 'ACTIVA',
    fecha_construccion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id_construccion),
    UNIQUE (id_granja, id_tipo_construccion),

    FOREIGN KEY (id_granja)
        REFERENCES granja(id_granja)
        ON DELETE CASCADE,

    FOREIGN KEY (id_tipo_construccion)
        REFERENCES tipo_construccion(id_tipo_construccion)
);

-- Comercios y ofertas

CREATE TABLE local (
    id_local INT UNSIGNED AUTO_INCREMENT,
    nombre VARCHAR(80) NOT NULL UNIQUE,
    descripcion VARCHAR(255),
    tipo ENUM(
        'TIENDA',
        'MERCADO',
        'HERRERIA',
        'OTRO'
    ) NOT NULL DEFAULT 'TIENDA',
    activo BOOLEAN NOT NULL DEFAULT TRUE,

    PRIMARY KEY (id_local)
);

CREATE TABLE oferta_local (
    id_oferta INT UNSIGNED AUTO_INCREMENT,
    id_local INT UNSIGNED NOT NULL,
    id_item INT UNSIGNED NOT NULL,
    operacion ENUM(
        'JUGADOR_COMPRA',
        'JUGADOR_VENDE'
    ) NOT NULL,
    precio INT UNSIGNED NOT NULL DEFAULT 0,
    stock INT UNSIGNED DEFAULT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,

    PRIMARY KEY (id_oferta),
    UNIQUE (id_local, id_item, operacion),

    FOREIGN KEY (id_local)
        REFERENCES local(id_local)
        ON DELETE CASCADE,

    FOREIGN KEY (id_item)
        REFERENCES item(id_item)
);

-- NPC y misiones

CREATE TABLE npc (
    id_npc INT UNSIGNED AUTO_INCREMENT,
    nombre VARCHAR(80) NOT NULL,
    descripcion VARCHAR(255),
    ubicacion VARCHAR(80),
    activo BOOLEAN NOT NULL DEFAULT TRUE,

    PRIMARY KEY (id_npc)
);

CREATE TABLE mision (
    id_mision INT UNSIGNED AUTO_INCREMENT,
    id_npc INT UNSIGNED,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(500),
    tipo_objetivo ENUM(
        'COSECHAR',
        'VENDER',
        'RECOLECTAR',
        'COMPRAR',
        'CUIDAR_ANIMAL',
        'CONSTRUIR',
        'OTRO'
    ) NOT NULL DEFAULT 'OTRO',
    id_item_objetivo INT UNSIGNED,
    cantidad_objetivo INT UNSIGNED NOT NULL DEFAULT 1,
    recompensa_monedas INT UNSIGNED NOT NULL DEFAULT 0,
    recompensa_experiencia INT UNSIGNED NOT NULL DEFAULT 0,
    nivel_requerido INT UNSIGNED NOT NULL DEFAULT 1,
    activa BOOLEAN NOT NULL DEFAULT TRUE,

    PRIMARY KEY (id_mision),

    FOREIGN KEY (id_npc)
        REFERENCES npc(id_npc),

    FOREIGN KEY (id_item_objetivo)
        REFERENCES item(id_item)
);

CREATE TABLE progreso_mision (
    id_progreso_mision INT UNSIGNED AUTO_INCREMENT,
    id_partida INT UNSIGNED NOT NULL,
    id_mision INT UNSIGNED NOT NULL,
    progreso INT UNSIGNED NOT NULL DEFAULT 0,
    estado ENUM(
        'DISPONIBLE',
        'EN_CURSO',
        'COMPLETADA',
        'RECLAMADA'
    ) NOT NULL DEFAULT 'DISPONIBLE',
    fecha_inicio DATETIME,
    fecha_completada DATETIME,

    PRIMARY KEY (id_progreso_mision),
    UNIQUE (id_partida, id_mision),

    FOREIGN KEY (id_partida)
        REFERENCES partida(id_partida)
        ON DELETE CASCADE,

    FOREIGN KEY (id_mision)
        REFERENCES mision(id_mision)
);

-- Eventos aleatorios

CREATE TABLE evento (
    id_evento INT UNSIGNED AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(500),
    probabilidad DECIMAL(5,2) NOT NULL DEFAULT 50.00,
    nivel_minimo INT UNSIGNED NOT NULL DEFAULT 1,
    activo BOOLEAN NOT NULL DEFAULT TRUE,

    PRIMARY KEY (id_evento)
);

CREATE TABLE opcion_evento (
    id_opcion_evento INT UNSIGNED AUTO_INCREMENT,
    id_evento INT UNSIGNED NOT NULL,
    texto_opcion VARCHAR(255) NOT NULL,
    descripcion_resultado VARCHAR(255),

    PRIMARY KEY (id_opcion_evento),

    FOREIGN KEY (id_evento)
        REFERENCES evento(id_evento)
        ON DELETE CASCADE
);

CREATE TABLE consecuencia (
    id_consecuencia INT UNSIGNED AUTO_INCREMENT,
    id_opcion_evento INT UNSIGNED NOT NULL,
    tipo ENUM(
        'MONEDAS',
        'EXPERIENCIA',
        'ITEM',
        'PROGRESO_CULTIVO',
        'OTRO'
    ) NOT NULL,
    resultado ENUM('EXITO', 'FRACASO') NOT NULL DEFAULT 'EXITO',
    cantidad INT NOT NULL DEFAULT 0,
    id_item INT UNSIGNED,

    PRIMARY KEY (id_consecuencia),

    FOREIGN KEY (id_opcion_evento)
        REFERENCES opcion_evento(id_opcion_evento)
        ON DELETE CASCADE,

    FOREIGN KEY (id_item)
        REFERENCES item(id_item)
);

CREATE TABLE historial_evento (
    id_historial_evento INT UNSIGNED AUTO_INCREMENT,
    id_partida INT UNSIGNED NOT NULL,
    id_evento INT UNSIGNED NOT NULL,
    id_opcion_evento INT UNSIGNED,
    fecha_evento DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    descripcion_resultado VARCHAR(500),

    PRIMARY KEY (id_historial_evento),

    FOREIGN KEY (id_partida)
        REFERENCES partida(id_partida)
        ON DELETE CASCADE,

    FOREIGN KEY (id_evento)
        REFERENCES evento(id_evento),

    FOREIGN KEY (id_opcion_evento)
        REFERENCES opcion_evento(id_opcion_evento)
);


-- Conexion de cultivos con items

ALTER TABLE cultivo
    ADD COLUMN id_item_semilla INT UNSIGNED,
    ADD COLUMN id_item_cosecha INT UNSIGNED,

    ADD CONSTRAINT fk_cultivo_item_semilla
        FOREIGN KEY (id_item_semilla)
        REFERENCES item(id_item),

    ADD CONSTRAINT fk_cultivo_item_cosecha
        FOREIGN KEY (id_item_cosecha)
        REFERENCES item(id_item);

SHOW CREATE TABLE cultivo;
SHOW CREATE TABLE plantacion;

