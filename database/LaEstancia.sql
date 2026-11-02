
DROP DATABASE IF EXISTS La_Estancia;
CREATE DATABASE La_Estancia;
USE La_Estancia;

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


/* Partida (cada partida representa una granja)
 minuto_juego_total: reloj ficticio del juego.
 dia = minuto_juego_total / 1440 ; hora = (minuto_juego_total % 1440) / 60
*/

CREATE TABLE partida (
    id_partida INT UNSIGNED AUTO_INCREMENT,
    id_usuario INT UNSIGNED NOT NULL,
    nombre_granja VARCHAR(100) NOT NULL,
    nivel INT UNSIGNED NOT NULL DEFAULT 1,
    experiencia INT UNSIGNED NOT NULL DEFAULT 0,
    monedas INT UNSIGNED NOT NULL DEFAULT 100,
    minuto_juego_total BIGINT UNSIGNED NOT NULL DEFAULT 480, -- dia 0, 08:00
    estado ENUM('ACTIVA', 'PAUSADA', 'FINALIZADA') NOT NULL DEFAULT 'ACTIVA',
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_guardado DATETIME,
    activo BOOLEAN NOT NULL DEFAULT TRUE,

    PRIMARY KEY (id_partida),

    FOREIGN KEY (id_usuario)
        REFERENCES usuario(id_usuario)
);

CREATE TABLE item (
    id_item INT UNSIGNED AUTO_INCREMENT,
    nombre VARCHAR(80) NOT NULL,
    descripcion VARCHAR(255),
    tipo ENUM('SEMILLA', 'COSECHA', 'PRODUCTO_ANIMAL', 'OTRO') NOT NULL DEFAULT 'OTRO',
    precio_venta INT UNSIGNED NOT NULL DEFAULT 0,
    activo BOOLEAN NOT NULL DEFAULT TRUE,

    PRIMARY KEY (id_item),
    UNIQUE (nombre)
);

CREATE TABLE cultivo (
    id_cultivo INT UNSIGNED AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL,
    precio_semilla INT UNSIGNED NOT NULL,
    precio_venta INT UNSIGNED NOT NULL,
    tiempo_crecimiento_min INT UNSIGNED NOT NULL,
    experiencia_recompensa INT UNSIGNED NOT NULL DEFAULT 0,
    id_item_semilla INT UNSIGNED,
    id_item_cosecha INT UNSIGNED,
    activo BOOLEAN NOT NULL DEFAULT TRUE,

    PRIMARY KEY (id_cultivo),
    UNIQUE (nombre),

    FOREIGN KEY (id_item_semilla)
        REFERENCES item(id_item),

    FOREIGN KEY (id_item_cosecha)
        REFERENCES item(id_item)
);

CREATE TABLE plantacion (
    id_plantacion INT UNSIGNED AUTO_INCREMENT,
    id_partida INT UNSIGNED NOT NULL,
    id_cultivo INT UNSIGNED NOT NULL,
    numero_parcela SMALLINT UNSIGNED NOT NULL,
    minuto_siembra_juego BIGINT UNSIGNED NOT NULL,
    estado ENUM('CRECIENDO', 'LISTA', 'COSECHADA') NOT NULL DEFAULT 'CRECIENDO',
    activo BOOLEAN NOT NULL DEFAULT TRUE,

    PRIMARY KEY (id_plantacion),

    FOREIGN KEY (id_partida)
        REFERENCES partida(id_partida)
        ON DELETE CASCADE,

    FOREIGN KEY (id_cultivo)
        REFERENCES cultivo(id_cultivo)
);

CREATE TABLE tipo_animal (
    id_tipo_animal INT UNSIGNED AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL,
    descripcion VARCHAR(255),
    precio_compra INT UNSIGNED NOT NULL DEFAULT 0,
    precio_venta INT UNSIGNED NOT NULL DEFAULT 0,
    id_item_producto INT UNSIGNED,
    cantidad_producto INT UNSIGNED NOT NULL DEFAULT 1,
    tiempo_produccion_min INT UNSIGNED NOT NULL DEFAULT 1440,
    activo BOOLEAN NOT NULL DEFAULT TRUE,

    PRIMARY KEY (id_tipo_animal),
    UNIQUE (nombre),

    FOREIGN KEY (id_item_producto)
        REFERENCES item(id_item)
);

CREATE TABLE animal (
    id_animal INT UNSIGNED AUTO_INCREMENT,
    id_partida INT UNSIGNED NOT NULL,
    id_tipo_animal INT UNSIGNED NOT NULL,
    nombre VARCHAR(50),
    hambre TINYINT UNSIGNED NOT NULL DEFAULT 100,
    minuto_ultima_produccion BIGINT UNSIGNED,
    fecha_adquisicion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN NOT NULL DEFAULT TRUE,

    PRIMARY KEY (id_animal),

    FOREIGN KEY (id_partida)
        REFERENCES partida(id_partida)
        ON DELETE CASCADE,

    FOREIGN KEY (id_tipo_animal)
        REFERENCES tipo_animal(id_tipo_animal)
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
