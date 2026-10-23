USE La_Estancia;

SHOW COLUMNS FROM parcela;


-- 1) Probabilidad de exito de cada respuesta.
-- evento.probabilidad queda reservada para la probabilidad de aparicion del evento.
ALTER TABLE opcion_evento
    ADD COLUMN probabilidad_exito DECIMAL(5,2) NOT NULL DEFAULT 75.00
    AFTER descripcion_resultado;

-- 2) Consecuencias que pueden afectar a un tipo de animal.
-- 'ANIMAL' fue agregado a la tabla
ALTER TABLE consecuencia
    MODIFY COLUMN tipo ENUM(
        'MONEDAS',
        'EXPERIENCIA',
        'ITEM',
        'PROGRESO_CULTIVO',
        'ANIMAL',
        'OTRO'
    ) NOT NULL;

ALTER TABLE consecuencia
    ADD COLUMN id_tipo_animal INT UNSIGNED NULL
    AFTER id_item;

ALTER TABLE consecuencia
    ADD CONSTRAINT fk_consecuencia_tipo_animal
        FOREIGN KEY (id_tipo_animal)
        REFERENCES tipo_animal(id_tipo_animal);

-- 3) Reloj ficticio del juego para que los cultivos no dependan
-- de la fecha/hora real de la computadora.
-- minuto_juego_total: minutos transcurridos en el mundo del juego.
-- minuto_siembra_juego: minuto ficticio en el que se planto cada cultivo.
ALTER TABLE partida
    ADD COLUMN minuto_juego_total BIGINT UNSIGNED NOT NULL DEFAULT 0
    AFTER hora_juego;

-- Sincronizar partidas existentes con dia_juego y hora_juego.

UPDATE partida
SET minuto_juego_total =
    (CAST(dia_juego AS UNSIGNED) * 1440)
    + (CAST(hora_juego AS UNSIGNED) * 60);

ALTER TABLE plantacion
    ADD COLUMN minuto_siembra_juego BIGINT UNSIGNED NULL
    AFTER fecha_siembra;

-- Para plantaciones ya existentes, iniciar el conteo ficticio desde el
-- momento actual del juego de esa partida. No se puede reconstruir el
-- tiempo ficticio pasado a partir de fecha_siembra (que es fecha real).
UPDATE plantacion pl
JOIN parcela pa ON pa.id_parcela = pl.id_parcela
JOIN granja g ON g.id_granja = pa.id_granja
JOIN partida p ON p.id_partida = g.id_partida
SET pl.minuto_siembra_juego = p.minuto_juego_total
WHERE pl.minuto_siembra_juego IS NULL
  AND pl.estado = 'CRECIENDO';

-- Las plantaciones que ya no estan creciendo no necesitan calcular
-- tiempo restante. Se completa el campo igualmente para evitar NULL.
UPDATE plantacion
SET minuto_siembra_juego = 0
WHERE minuto_siembra_juego IS NULL;

-- Verificacion de cambios
SHOW COLUMNS FROM opcion_evento;
SHOW COLUMNS FROM consecuencia;
SHOW COLUMNS FROM partida;
SHOW COLUMNS FROM plantacion;
SHOW COLUMNS FROM parcela;


