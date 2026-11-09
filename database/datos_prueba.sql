-- ============================================================
-- DATOS DE PRUEBA - LA ESTANCIA
--
-- Ejecutar este archivo despues de LaEstancia.sql
-- sobre una base recien creada.
--
-- Todos los usuarios de prueba utilizan la contraseña: 1234
-- almacenada mediante SHA-256.
--
-- El orden de los INSERT respeta las claves foraneas.
-- ============================================================

USE La_Estancia;

START TRANSACTION;


-- ============================================================
-- USUARIOS
--
-- Primero cargo los usuarios porque la tabla partida
-- necesita un id_usuario existente.
--
-- Creo 2 administradores y 15 jugadores.
-- ============================================================

INSERT INTO usuario
(id_usuario, nombre_usuario, email, contrasenia_hash, rol, activo)
VALUES
    (1, 'admin01', 'admin01@laestancia.com', SHA2('1234', 256), 'ADMIN', TRUE),
    (2, 'admin02', 'admin02@laestancia.com', SHA2('1234', 256), 'ADMIN', TRUE),

    (3, 'jugador01', 'jugador01@laestancia.com', SHA2('1234', 256), 'JUGADOR', TRUE),
    (4, 'jugador02', 'jugador02@laestancia.com', SHA2('1234', 256), 'JUGADOR', TRUE),
    (5, 'jugador03', 'jugador03@laestancia.com', SHA2('1234', 256), 'JUGADOR', TRUE),
    (6, 'jugador04', 'jugador04@laestancia.com', SHA2('1234', 256), 'JUGADOR', TRUE),
    (7, 'jugador05', 'jugador05@laestancia.com', SHA2('1234', 256), 'JUGADOR', TRUE),
    (8, 'jugador06', 'jugador06@laestancia.com', SHA2('1234', 256), 'JUGADOR', TRUE),
    (9, 'jugador07', 'jugador07@laestancia.com', SHA2('1234', 256), 'JUGADOR', TRUE),
    (10, 'jugador08', 'jugador08@laestancia.com', SHA2('1234', 256), 'JUGADOR', TRUE),
    (11, 'jugador09', 'jugador09@laestancia.com', SHA2('1234', 256), 'JUGADOR', TRUE),
    (12, 'jugador10', 'jugador10@laestancia.com', SHA2('1234', 256), 'JUGADOR', TRUE),
    (13, 'jugador11', 'jugador11@laestancia.com', SHA2('1234', 256), 'JUGADOR', TRUE),
    (14, 'jugador12', 'jugador12@laestancia.com', SHA2('1234', 256), 'JUGADOR', TRUE),
    (15, 'jugador13', 'jugador13@laestancia.com', SHA2('1234', 256), 'JUGADOR', TRUE),
    (16, 'jugador14', 'jugador14@laestancia.com', SHA2('1234', 256), 'JUGADOR', TRUE),
    (17, 'jugador15', 'jugador15@laestancia.com', SHA2('1234', 256), 'JUGADOR', TRUE);


-- ============================================================
-- PARTIDAS
--
-- Creo una partida para cada jugador.
-- Los administradores no tienen una partida asociada.
--
-- minuto_juego_total = 480 representa Dia 1 a las 08:00.
-- ============================================================

INSERT INTO partida
(id_partida, id_usuario, nombre_granja, nivel, experiencia, monedas,
 minuto_juego_total, parcelas_desbloqueadas, nivel_terreno,
 tiene_gallinero, tiene_establo,
 rareza_hacha, rareza_pico, rareza_cania,
 dia_ultimo_evento, estado, activo)
VALUES

    (1, 3, 'Los Aromos',
     1, 0, 500,
     480, 2, 1,
     FALSE, FALSE,
     'COMUN', 'COMUN', 'COMUN',
     NULL, 'ACTIVA', TRUE),

    (2, 4, 'El Trigal',
     2, 120, 750,
     870, 3, 1,
     TRUE, FALSE,
     'POCO_COMUN', 'COMUN', 'COMUN',
     NULL, 'ACTIVA', TRUE),

    (3, 5, 'La Esperanza',
     3, 350, 1100,
     1500, 4, 2,
     TRUE, FALSE,
     'POCO_COMUN', 'POCO_COMUN', 'COMUN',
     1, 'ACTIVA', TRUE),

    (4, 6, 'Campo Verde',
     2, 180, 650,
     1900, 3, 1,
     FALSE, FALSE,
     'COMUN', 'POCO_COMUN', 'COMUN',
     NULL, 'ACTIVA', TRUE),

    (5, 7, 'El Molino',
     4, 620, 1800,
     2500, 5, 2,
     TRUE, TRUE,
     'RARA', 'POCO_COMUN', 'POCO_COMUN',
     2, 'ACTIVA', TRUE),

    (6, 8, 'Las Flores',
     1, 40, 420,
     600, 2, 1,
     FALSE, FALSE,
     'COMUN', 'COMUN', 'COMUN',
     NULL, 'ACTIVA', TRUE),

    (7, 9, 'Santa Clara',
     3, 410, 1250,
     3100, 4, 2,
     TRUE, TRUE,
     'POCO_COMUN', 'RARA', 'POCO_COMUN',
     2, 'ACTIVA', TRUE),

    (8, 10, 'El Ombu',
     2, 200, 820,
     1100, 3, 1,
     FALSE, FALSE,
     'COMUN', 'POCO_COMUN', 'COMUN',
     NULL, 'ACTIVA', TRUE),

    (9, 11, 'La Arboleda',
     5, 900, 2500,
     4200, 6, 3,
     TRUE, TRUE,
     'RARA', 'RARA', 'POCO_COMUN',
     3, 'ACTIVA', TRUE),

    (10, 12, 'Sol Naciente',
     1, 70, 550,
     750, 2, 1,
     FALSE, FALSE,
     'COMUN', 'COMUN', 'COMUN',
     NULL, 'ACTIVA', TRUE),

    (11, 13, 'Los Pinos',
     3, 390, 1300,
     2250, 4, 2,
     TRUE, FALSE,
     'POCO_COMUN', 'POCO_COMUN', 'RARA',
     1, 'ACTIVA', TRUE),

    (12, 14, 'La Pradera',
     2, 240, 900,
     1750, 3, 1,
     FALSE, TRUE,
     'POCO_COMUN', 'COMUN', 'POCO_COMUN',
     NULL, 'ACTIVA', TRUE),

    (13, 15, 'El Hornero',
     4, 680, 1950,
     3400, 5, 2,
     TRUE, TRUE,
     'RARA', 'RARA', 'RARA',
     2, 'ACTIVA', TRUE),

    (14, 16, 'Las Acacias',
     2, 150, 700,
     980, 3, 1,
     TRUE, FALSE,
     'COMUN', 'POCO_COMUN', 'COMUN',
     NULL, 'ACTIVA', TRUE),

    (15, 17, 'La Colina',
     6, 1200, 3200,
     5200, 7, 3,
     TRUE, TRUE,
     'EPICA', 'RARA', 'RARA',
     3, 'ACTIVA', TRUE);


-- ============================================================
-- ITEMS
--
-- Primero creo todos los items porque las tablas cultivo,
-- tipo_animal e inventario dependen de ellos.
-- ============================================================


-- ------------------------------------------------------------
-- SEMILLAS
-- IDs 1 a 15
-- ------------------------------------------------------------

INSERT INTO item
(id_item, nombre, descripcion, tipo,
 precio_compra, precio_venta, activo)
VALUES
    (1, 'Semilla de trigo', 'Semilla utilizada para cultivar trigo.', 'SEMILLA', 20, 5, TRUE),
    (2, 'Semilla de maiz', 'Semilla utilizada para cultivar maiz.', 'SEMILLA', 25, 6, TRUE),
    (3, 'Semilla de papa', 'Semilla utilizada para cultivar papa.', 'SEMILLA', 30, 8, TRUE),
    (4, 'Semilla de zanahoria', 'Semilla utilizada para cultivar zanahoria.', 'SEMILLA', 28, 7, TRUE),
    (5, 'Semilla de tomate', 'Semilla utilizada para cultivar tomate.', 'SEMILLA', 35, 9, TRUE),
    (6, 'Semilla de lechuga', 'Semilla utilizada para cultivar lechuga.', 'SEMILLA', 22, 5, TRUE),
    (7, 'Semilla de cebolla', 'Semilla utilizada para cultivar cebolla.', 'SEMILLA', 30, 7, TRUE),
    (8, 'Semilla de calabaza', 'Semilla utilizada para cultivar calabaza.', 'SEMILLA', 45, 10, TRUE),
    (9, 'Semilla de frutilla', 'Semilla utilizada para cultivar frutilla.', 'SEMILLA', 50, 12, TRUE),
    (10, 'Semilla de girasol', 'Semilla utilizada para cultivar girasol.', 'SEMILLA', 40, 10, TRUE),
    (11, 'Semilla de soja', 'Semilla utilizada para cultivar soja.', 'SEMILLA', 32, 8, TRUE),
    (12, 'Semilla de arroz', 'Semilla utilizada para cultivar arroz.', 'SEMILLA', 35, 8, TRUE),
    (13, 'Semilla de remolacha', 'Semilla utilizada para cultivar remolacha.', 'SEMILLA', 30, 7, TRUE),
    (14, 'Semilla de morron', 'Semilla utilizada para cultivar morron.', 'SEMILLA', 42, 10, TRUE),
    (15, 'Semilla de ajo', 'Semilla utilizada para cultivar ajo.', 'SEMILLA', 38, 9, TRUE);


-- ------------------------------------------------------------
-- COSECHAS
-- IDs 16 a 30
-- ------------------------------------------------------------

INSERT INTO item
(id_item, nombre, descripcion, tipo,
 precio_compra, precio_venta, activo)
VALUES
    (16, 'Trigo', 'Trigo cosechado.', 'COSECHA', 0, 40, TRUE),
    (17, 'Maiz', 'Maiz cosechado.', 'COSECHA', 0, 50, TRUE),
    (18, 'Papa', 'Papa cosechada.', 'COSECHA', 0, 55, TRUE),
    (19, 'Zanahoria', 'Zanahoria cosechada.', 'COSECHA', 0, 52, TRUE),
    (20, 'Tomate', 'Tomate cosechado.', 'COSECHA', 0, 65, TRUE),
    (21, 'Lechuga', 'Lechuga cosechada.', 'COSECHA', 0, 42, TRUE),
    (22, 'Cebolla', 'Cebolla cosechada.', 'COSECHA', 0, 58, TRUE),
    (23, 'Calabaza', 'Calabaza cosechada.', 'COSECHA', 0, 80, TRUE),
    (24, 'Frutilla', 'Frutilla cosechada.', 'COSECHA', 0, 95, TRUE),
    (25, 'Girasol', 'Girasol cosechado.', 'COSECHA', 0, 75, TRUE),
    (26, 'Soja', 'Soja cosechada.', 'COSECHA', 0, 60, TRUE),
    (27, 'Arroz', 'Arroz cosechado.', 'COSECHA', 0, 65, TRUE),
    (28, 'Remolacha', 'Remolacha cosechada.', 'COSECHA', 0, 55, TRUE),
    (29, 'Morron', 'Morron cosechado.', 'COSECHA', 0, 85, TRUE),
    (30, 'Ajo', 'Ajo cosechado.', 'COSECHA', 0, 70, TRUE);


-- ------------------------------------------------------------
-- PRODUCTOS DE ANIMALES
-- IDs 31 a 45
-- ------------------------------------------------------------

INSERT INTO item
(id_item, nombre, descripcion, tipo,
 precio_compra, precio_venta, activo)
VALUES
    (31, 'Huevo', 'Huevo producido por una gallina.', 'PRODUCTO_ANIMAL', 0, 40, TRUE),
    (32, 'Leche', 'Leche fresca producida por una vaca.', 'PRODUCTO_ANIMAL', 0, 70, TRUE),
    (33, 'Lana', 'Lana obtenida de una oveja.', 'PRODUCTO_ANIMAL', 0, 85, TRUE),
    (34, 'Leche de cabra', 'Leche producida por una cabra.', 'PRODUCTO_ANIMAL', 0, 75, TRUE),
    (35, 'Huevo de pato', 'Huevo producido por un pato.', 'PRODUCTO_ANIMAL', 0, 55, TRUE),
    (36, 'Trufa', 'Trufa encontrada por un cerdo.', 'PRODUCTO_ANIMAL', 0, 140, TRUE),
    (37, 'Pelo de conejo', 'Fibra obtenida de un conejo.', 'PRODUCTO_ANIMAL', 0, 60, TRUE),
    (38, 'Lana de alpaca', 'Lana obtenida de una alpaca.', 'PRODUCTO_ANIMAL', 0, 110, TRUE),
    (39, 'Huevo de ganso', 'Huevo producido por un ganso.', 'PRODUCTO_ANIMAL', 0, 65, TRUE),
    (40, 'Huevo de codorniz', 'Huevo pequeño producido por una codorniz.', 'PRODUCTO_ANIMAL', 0, 35, TRUE),
    (41, 'Miel', 'Miel producida en la granja.', 'PRODUCTO_ANIMAL', 0, 100, TRUE),
    (42, 'Pluma', 'Pluma obtenida de un pavo.', 'PRODUCTO_ANIMAL', 0, 45, TRUE),
    (43, 'Leche de oveja', 'Leche producida por una oveja lechera.', 'PRODUCTO_ANIMAL', 0, 80, TRUE),
    (44, 'Fibra animal', 'Fibra utilizada para fabricar productos.', 'PRODUCTO_ANIMAL', 0, 90, TRUE),
    (45, 'Cuero', 'Material obtenido de animales de granja.', 'PRODUCTO_ANIMAL', 0, 120, TRUE);


-- ------------------------------------------------------------
-- MATERIALES, PESCADOS Y ALIMENTOS
-- ------------------------------------------------------------

INSERT INTO item
(id_item, nombre, descripcion, tipo,
 precio_compra, precio_venta, activo)
VALUES
    (46, 'Madera', 'Material recolectado con el hacha.', 'MATERIAL', 0, 25, TRUE),
    (47, 'Piedra', 'Material recolectado con el pico.', 'MATERIAL', 0, 30, TRUE),

    (48, 'Pejerrey', 'Pez obtenido mediante pesca.', 'PESCADO', 0, 70, TRUE),
    (49, 'Corvina', 'Pez obtenido mediante pesca.', 'PESCADO', 0, 85, TRUE),
    (50, 'Lenguado', 'Pez obtenido mediante pesca.', 'PESCADO', 0, 100, TRUE),
    (51, 'Trucha', 'Pez obtenido mediante pesca.', 'PESCADO', 0, 95, TRUE),
    (52, 'Bagre', 'Pez obtenido mediante pesca.', 'PESCADO', 0, 65, TRUE),

    (53, 'Alimento para animales', 'Alimento general para los animales.', 'ALIMENTO', 30, 10, TRUE),
    (54, 'Heno', 'Alimento utilizado para animales de granja.', 'ALIMENTO', 25, 8, TRUE),
    (55, 'Balanceado', 'Alimento nutritivo para animales.', 'ALIMENTO', 40, 12, TRUE);


-- ============================================================
-- CULTIVOS
--
-- Cada cultivo referencia:
-- id_item_semilla -> el Item de tipo SEMILLA
-- id_item_cosecha -> el Item de tipo COSECHA
-- ============================================================

INSERT INTO cultivo
(id_cultivo, nombre, precio_semilla, precio_venta,
 tiempo_crecimiento_min, experiencia_recompensa,
 id_item_semilla, id_item_cosecha, activo)
VALUES
    (1, 'Trigo', 20, 40, 720, 10, 1, 16, TRUE),
    (2, 'Maiz', 25, 50, 900, 12, 2, 17, TRUE),
    (3, 'Papa', 30, 55, 1080, 14, 3, 18, TRUE),
    (4, 'Zanahoria', 28, 52, 960, 12, 4, 19, TRUE),
    (5, 'Tomate', 35, 65, 1440, 18, 5, 20, TRUE),
    (6, 'Lechuga', 22, 42, 600, 8, 6, 21, TRUE),
    (7, 'Cebolla', 30, 58, 1080, 14, 7, 22, TRUE),
    (8, 'Calabaza', 45, 80, 1800, 22, 8, 23, TRUE),
    (9, 'Frutilla', 50, 95, 2160, 25, 9, 24, TRUE),
    (10, 'Girasol', 40, 75, 1440, 18, 10, 25, TRUE),
    (11, 'Soja', 32, 60, 1200, 15, 11, 26, TRUE),
    (12, 'Arroz', 35, 65, 1320, 16, 12, 27, TRUE),
    (13, 'Remolacha', 30, 55, 960, 13, 13, 28, TRUE),
    (14, 'Morron', 42, 85, 1800, 21, 14, 29, TRUE),
    (15, 'Ajo', 38, 70, 1500, 18, 15, 30, TRUE);


-- ============================================================
-- TIPOS DE ANIMAL
--
-- Cada tipo de animal puede tener asociado un producto.
-- Los productos ya existen en Item.
-- ============================================================

INSERT INTO tipo_animal
(id_tipo_animal, nombre, descripcion,
 precio_compra, precio_venta,
 id_item_producto, cantidad_producto,
 tiempo_produccion_min, activo)
VALUES
    (1, 'Gallina', 'Animal que produce huevos.', 300, 150, 31, 1, 720, TRUE),
    (2, 'Vaca', 'Animal que produce leche.', 1200, 700, 32, 2, 1440, TRUE),
    (3, 'Oveja', 'Animal que produce lana.', 800, 450, 33, 1, 1440, TRUE),
    (4, 'Cabra', 'Animal que produce leche de cabra.', 900, 500, 34, 1, 1200, TRUE),
    (5, 'Pato', 'Animal que produce huevos de pato.', 450, 250, 35, 1, 900, TRUE),
    (6, 'Cerdo', 'Animal capaz de encontrar trufas.', 1000, 550, 36, 1, 1800, TRUE),
    (7, 'Conejo', 'Animal que produce fibra.', 350, 180, 37, 1, 1200, TRUE),
    (8, 'Alpaca', 'Animal que produce lana de alpaca.', 1300, 750, 38, 1, 1800, TRUE),
    (9, 'Ganso', 'Animal que produce huevos de ganso.', 500, 280, 39, 1, 1080, TRUE),
    (10, 'Codorniz', 'Animal que produce huevos pequeños.', 250, 120, 40, 2, 720, TRUE),
    (11, 'Abeja', 'Produce miel para vender.', 600, 300, 41, 1, 1440, TRUE),
    (12, 'Pavo', 'Animal que produce plumas.', 550, 300, 42, 1, 1200, TRUE),
    (13, 'Oveja lechera', 'Oveja especializada en producir leche.', 950, 520, 43, 1, 1440, TRUE),
    (14, 'Llama', 'Animal que produce fibra.', 1100, 600, 44, 1, 1800, TRUE),
    (15, 'Buey', 'Animal de granja del que puede obtenerse cuero.', 1500, 850, 45, 1, 2160, TRUE);


-- ============================================================
-- PLANTACIONES
--
-- Incluyo distintos estados para poder probar PlantacionDao.
--
-- Algunas plantaciones COSECHADAS siguen activas a proposito.
-- Esto sirve para comprobar que listarPorPartida()
-- no devuelva las plantaciones cosechadas.
-- ============================================================

INSERT INTO plantacion
(id_plantacion, id_partida, id_cultivo,
 numero_parcela, minuto_siembra_juego,
 estado, activo)
VALUES
    (1, 1, 1, 1, 480, 'CRECIENDO', TRUE),
    (2, 2, 2, 1, 500, 'LISTA', TRUE),
    (3, 3, 3, 1, 600, 'COSECHADA', TRUE),
    (4, 4, 4, 2, 700, 'CRECIENDO', TRUE),
    (5, 5, 5, 1, 800, 'LISTA', TRUE),
    (6, 6, 6, 1, 500, 'COSECHADA', TRUE),
    (7, 7, 7, 2, 1000, 'CRECIENDO', TRUE),
    (8, 8, 8, 1, 650, 'LISTA', TRUE),
    (9, 9, 9, 3, 1300, 'COSECHADA', TRUE),
    (10, 10, 10, 1, 600, 'CRECIENDO', TRUE),
    (11, 11, 11, 2, 900, 'LISTA', TRUE),
    (12, 12, 12, 1, 800, 'CRECIENDO', TRUE),
    (13, 13, 13, 3, 1400, 'COSECHADA', TRUE),
    (14, 14, 14, 1, 700, 'LISTA', TRUE),
    (15, 15, 15, 2, 1600, 'CRECIENDO', TRUE);


-- ============================================================
-- ANIMALES
--
-- hambre:
-- 100 = completamente alimentado
-- 0   = mucha hambre
--
-- Algunos campos de tiempo pueden ser NULL porque el animal
-- puede no haber sido alimentado o no haber producido todavia.
-- ============================================================

INSERT INTO animal
(id_animal, id_partida, id_tipo_animal,
 nombre, hambre,
 minuto_ultima_alimentacion,
 minuto_ultima_produccion,
 activo)
VALUES
    (1, 1, 1, 'Pepita', 100, NULL, NULL, TRUE),
    (2, 2, 2, 'Lola', 85, 700, 650, TRUE),
    (3, 3, 3, 'Nube', 70, 1200, 1100, TRUE),
    (4, 4, 4, 'Mora', 90, 1500, NULL, TRUE),
    (5, 5, 5, 'Pipo', 60, 1900, 1800, TRUE),
    (6, 6, 6, 'Chanchi', 75, 550, NULL, TRUE),
    (7, 7, 7, 'Copito', 50, 2700, 2600, TRUE),
    (8, 8, 8, 'Luna', 80, 900, NULL, TRUE),
    (9, 9, 9, 'Blanco', 65, 3700, 3500, TRUE),
    (10, 10, 10, 'Mini', 100, NULL, NULL, TRUE),
    (11, 11, 11, 'Dulce', 55, 1900, 1800, TRUE),
    (12, 12, 12, 'Tito', 90, 1500, NULL, TRUE),
    (13, 13, 13, 'Mancha', 45, 3000, 2900, TRUE),
    (14, 14, 14, 'Canelita', 75, 800, NULL, TRUE),
    (15, 15, 15, 'Toro', 40, 4700, 4500, TRUE);


-- ============================================================
-- INVENTARIO
--
-- Cargo varios recursos diferentes para poder probar:
-- compras, ventas, cultivos, misiones y recoleccion.
--
-- La combinacion (id_partida, id_item) siempre es unica.
-- ============================================================

INSERT INTO inventario
(id_inventario, id_partida, id_item, cantidad)
VALUES
    (1, 1, 1, 5),
    (2, 1, 46, 4),

    (3, 2, 2, 8),
    (4, 2, 47, 6),

    (5, 3, 16, 10),
    (6, 3, 46, 12),

    (7, 4, 4, 6),
    (8, 4, 48, 2),

    (9, 5, 20, 15),
    (10, 5, 47, 20),

    (11, 6, 6, 3),
    (12, 6, 53, 8),

    (13, 7, 22, 9),
    (14, 7, 46, 16),

    (15, 8, 8, 4),
    (16, 8, 49, 3),

    (17, 9, 24, 18),
    (18, 9, 47, 25),

    (19, 10, 10, 5),
    (20, 10, 54, 6),

    (21, 11, 26, 12),
    (22, 11, 50, 4),

    (23, 12, 12, 7),
    (24, 12, 46, 11),

    (25, 13, 28, 14),
    (26, 13, 47, 18),

    (27, 14, 14, 6),
    (28, 14, 31, 2),

    (29, 15, 30, 20),
    (30, 15, 32, 5);


-- ============================================================
-- PROGRESO DE MISIONES
--
-- Los codigos utilizados aca tienen que coincidir exactamente
-- con los valores definidos en DefinicionMision.java.
--
-- Una misma mision puede aparecer en distintas partidas.
-- Lo que no puede repetirse es:
--
-- (id_partida, codigo_mision)
--
-- porque esa combinacion es UNIQUE.
-- ============================================================

INSERT INTO progreso_mision
(id_progreso_mision, id_partida,
 codigo_mision, progreso, estado)
VALUES

-- Primera cosecha
(1, 1, 'PRIMERA_COSECHA', 0, 'ACTIVA'),
(2, 2, 'PRIMERA_COSECHA', 1, 'COMPLETADA'),
(3, 3, 'PRIMERA_COSECHA', 1, 'RECLAMADA'),

-- Carpintero
(4, 4, 'AYUDANDO_CARPINTERO', 4, 'ACTIVA'),
(5, 5, 'AYUDANDO_CARPINTERO', 10, 'COMPLETADA'),
(6, 6, 'AYUDANDO_CARPINTERO', 10, 'RECLAMADA'),

-- Pescador
(7, 7, 'PEDIDO_PESCADOR', 2, 'ACTIVA'),
(8, 8, 'PEDIDO_PESCADOR', 5, 'COMPLETADA'),
(9, 9, 'PEDIDO_PESCADOR', 5, 'RECLAMADA'),

-- Herrero
(10, 10, 'PIEDRA_PARA_EL_HERRERO', 3, 'ACTIVA'),
(11, 11, 'PIEDRA_PARA_EL_HERRERO', 8, 'COMPLETADA'),

-- Huevo
(12, 12, 'PRIMER_HUEVO', 1, 'COMPLETADA'),
(13, 13, 'PRIMER_HUEVO', 1, 'RECLAMADA'),

-- Leche
(14, 14, 'LECHE_FRESCA', 0, 'ACTIVA'),
(15, 15, 'LECHE_FRESCA', 1, 'COMPLETADA');


COMMIT;


-- ============================================================
-- CONSULTAS DE CONTROL
--
-- Estas consultas permiten comprobar rapidamente que los datos
-- fueron cargados correctamente.
-- ============================================================

SELECT COUNT(*) AS cantidad_usuarios
FROM usuario;

SELECT COUNT(*) AS cantidad_partidas
FROM partida;

SELECT COUNT(*) AS cantidad_items
FROM item;

SELECT COUNT(*) AS cantidad_cultivos
FROM cultivo;

SELECT COUNT(*) AS cantidad_plantaciones
FROM plantacion;

SELECT COUNT(*) AS cantidad_tipos_animal
FROM tipo_animal;

SELECT COUNT(*) AS cantidad_animales
FROM animal;

SELECT COUNT(*) AS cantidad_inventario
FROM inventario;

SELECT COUNT(*) AS cantidad_progreso_misiones
FROM progreso_mision;


-- Muestro las plantaciones cosechadas que siguen activas.
-- Sirven para probar el filtro de PlantacionDao.listarPorPartida().
SELECT *
FROM plantacion
WHERE estado = 'COSECHADA'
  AND activo = TRUE;


-- Compruebo el hash almacenado para la contraseña de prueba.
SELECT SHA2('1234', 256) AS hash_password_prueba;