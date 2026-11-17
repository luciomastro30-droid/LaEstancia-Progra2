-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: la_estancia
-- ------------------------------------------------------
-- Server version	8.0.45

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `animal`
--

DROP TABLE IF EXISTS `animal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `animal` (
  `id_animal` int unsigned NOT NULL AUTO_INCREMENT,
  `id_partida` int unsigned NOT NULL,
  `id_tipo_animal` int unsigned NOT NULL,
  `nombre` varchar(50) DEFAULT NULL,
  `hambre` tinyint unsigned NOT NULL DEFAULT '100',
  `minuto_ultima_alimentacion` bigint unsigned DEFAULT NULL,
  `minuto_ultima_produccion` bigint unsigned DEFAULT NULL,
  `fecha_adquisicion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id_animal`),
  KEY `id_partida` (`id_partida`),
  KEY `id_tipo_animal` (`id_tipo_animal`),
  CONSTRAINT `animal_ibfk_1` FOREIGN KEY (`id_partida`) REFERENCES `partida` (`id_partida`) ON DELETE CASCADE,
  CONSTRAINT `animal_ibfk_2` FOREIGN KEY (`id_tipo_animal`) REFERENCES `tipo_animal` (`id_tipo_animal`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `animal`
--

LOCK TABLES `animal` WRITE;
/*!40000 ALTER TABLE `animal` DISABLE KEYS */;
INSERT INTO `animal` VALUES (1,1,1,'Pepita',100,NULL,NULL,'2026-11-16 11:17:07',1),(2,2,2,'Lola',85,700,650,'2026-11-16 11:17:07',1),(3,3,3,'Nube',70,1200,1100,'2026-11-16 11:17:07',1),(4,4,4,'Mora',90,1500,NULL,'2026-11-16 11:17:07',1),(5,5,5,'Pipo',60,1900,1800,'2026-11-16 11:17:07',1),(6,6,6,'Chanchi',75,550,NULL,'2026-11-16 11:17:07',1),(7,7,7,'Copito',50,2700,2600,'2026-11-16 11:17:07',1),(8,8,8,'Luna',80,900,NULL,'2026-11-16 11:17:07',1),(9,9,9,'Blanco',65,3700,3500,'2026-11-16 11:17:07',1),(10,10,10,'Mini',100,NULL,NULL,'2026-11-16 11:17:07',1),(11,11,11,'Dulce',55,1900,1800,'2026-11-16 11:17:07',1),(12,12,12,'Tito',90,1500,NULL,'2026-11-16 11:17:07',1),(13,13,13,'Mancha',45,3000,2900,'2026-11-16 11:17:07',1),(14,14,14,'Canelita',75,800,NULL,'2026-11-16 11:17:07',1),(15,15,15,'Toro',40,4700,4500,'2026-11-16 11:17:07',1),(16,1,1,'jorge',100,NULL,6120,'2026-11-16 13:19:52',1);
/*!40000 ALTER TABLE `animal` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cultivo`
--

DROP TABLE IF EXISTS `cultivo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cultivo` (
  `id_cultivo` int unsigned NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `precio_semilla` int unsigned NOT NULL,
  `precio_venta` int unsigned NOT NULL,
  `tiempo_crecimiento_min` int unsigned NOT NULL,
  `experiencia_recompensa` int unsigned NOT NULL DEFAULT '0',
  `id_item_semilla` int unsigned DEFAULT NULL,
  `id_item_cosecha` int unsigned DEFAULT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id_cultivo`),
  UNIQUE KEY `nombre` (`nombre`),
  KEY `id_item_semilla` (`id_item_semilla`),
  KEY `id_item_cosecha` (`id_item_cosecha`),
  CONSTRAINT `cultivo_ibfk_1` FOREIGN KEY (`id_item_semilla`) REFERENCES `item` (`id_item`),
  CONSTRAINT `cultivo_ibfk_2` FOREIGN KEY (`id_item_cosecha`) REFERENCES `item` (`id_item`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cultivo`
--

LOCK TABLES `cultivo` WRITE;
/*!40000 ALTER TABLE `cultivo` DISABLE KEYS */;
INSERT INTO `cultivo` VALUES (1,'Trigo',20,40,720,10,1,16,1),(2,'Maiz',25,50,900,12,2,17,1),(3,'Papa',30,55,1080,14,3,18,1),(4,'Zanahoria',28,52,960,12,4,19,1),(5,'Tomate',35,65,1440,18,5,20,1),(6,'Lechuga',22,42,600,8,6,21,1),(7,'Cebolla',30,58,1080,14,7,22,1),(8,'Calabaza',45,80,1800,22,8,23,1),(9,'Frutilla',50,95,2160,25,9,24,1),(10,'Girasol',40,75,1440,18,10,25,1),(11,'Soja',32,60,1200,15,11,26,1),(12,'Arroz',35,65,1320,16,12,27,1),(13,'Remolacha',30,55,960,13,13,28,1),(14,'Morron',42,85,1800,21,14,29,1),(15,'Ajo',38,70,1500,18,15,30,1);
/*!40000 ALTER TABLE `cultivo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inventario`
--

DROP TABLE IF EXISTS `inventario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inventario` (
  `id_inventario` int unsigned NOT NULL AUTO_INCREMENT,
  `id_partida` int unsigned NOT NULL,
  `id_item` int unsigned NOT NULL,
  `cantidad` int unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id_inventario`),
  UNIQUE KEY `id_partida` (`id_partida`,`id_item`),
  KEY `id_item` (`id_item`),
  CONSTRAINT `inventario_ibfk_1` FOREIGN KEY (`id_partida`) REFERENCES `partida` (`id_partida`) ON DELETE CASCADE,
  CONSTRAINT `inventario_ibfk_2` FOREIGN KEY (`id_item`) REFERENCES `item` (`id_item`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventario`
--

LOCK TABLES `inventario` WRITE;
/*!40000 ALTER TABLE `inventario` DISABLE KEYS */;
INSERT INTO `inventario` VALUES (1,1,1,2),(2,1,46,20),(3,2,2,8),(4,2,47,6),(5,3,16,10),(6,3,46,12),(7,4,4,6),(8,4,48,2),(9,5,20,15),(10,5,47,20),(11,6,6,3),(12,6,53,8),(13,7,22,9),(14,7,46,16),(15,8,8,4),(16,8,49,3),(17,9,24,18),(18,9,47,25),(19,10,10,5),(20,10,54,6),(21,11,26,12),(22,11,50,4),(23,12,12,7),(24,12,46,11),(25,13,28,14),(26,13,47,18),(27,14,14,6),(28,14,31,2),(29,15,30,20),(30,15,32,5),(31,1,16,3),(32,1,47,8),(33,1,49,1),(34,1,52,1);
/*!40000 ALTER TABLE `inventario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `item`
--

DROP TABLE IF EXISTS `item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `item` (
  `id_item` int unsigned NOT NULL AUTO_INCREMENT,
  `nombre` varchar(80) NOT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `tipo` enum('SEMILLA','COSECHA','PRODUCTO_ANIMAL','MATERIAL','PESCADO','ALIMENTO','OTRO') NOT NULL DEFAULT 'OTRO',
  `precio_compra` int unsigned NOT NULL DEFAULT '0',
  `precio_venta` int unsigned NOT NULL DEFAULT '0',
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id_item`),
  UNIQUE KEY `nombre` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item`
--

LOCK TABLES `item` WRITE;
/*!40000 ALTER TABLE `item` DISABLE KEYS */;
INSERT INTO `item` VALUES (1,'Semilla de trigo','Semilla utilizada para cultivar trigo.','SEMILLA',20,5,1),(2,'Semilla de maiz','Semilla utilizada para cultivar maiz.','SEMILLA',25,6,1),(3,'Semilla de papa','Semilla utilizada para cultivar papa.','SEMILLA',30,8,1),(4,'Semilla de zanahoria','Semilla utilizada para cultivar zanahoria.','SEMILLA',28,7,1),(5,'Semilla de tomate','Semilla utilizada para cultivar tomate.','SEMILLA',35,9,1),(6,'Semilla de lechuga','Semilla utilizada para cultivar lechuga.','SEMILLA',22,5,1),(7,'Semilla de cebolla','Semilla utilizada para cultivar cebolla.','SEMILLA',30,7,1),(8,'Semilla de calabaza','Semilla utilizada para cultivar calabaza.','SEMILLA',45,10,1),(9,'Semilla de frutilla','Semilla utilizada para cultivar frutilla.','SEMILLA',50,12,1),(10,'Semilla de girasol','Semilla utilizada para cultivar girasol.','SEMILLA',40,10,1),(11,'Semilla de soja','Semilla utilizada para cultivar soja.','SEMILLA',32,8,1),(12,'Semilla de arroz','Semilla utilizada para cultivar arroz.','SEMILLA',35,8,1),(13,'Semilla de remolacha','Semilla utilizada para cultivar remolacha.','SEMILLA',30,7,1),(14,'Semilla de morron','Semilla utilizada para cultivar morron.','SEMILLA',42,10,1),(15,'Semilla de ajo','Semilla utilizada para cultivar ajo.','SEMILLA',38,9,1),(16,'Trigo','Trigo cosechado.','COSECHA',0,40,1),(17,'Maiz','Maiz cosechado.','COSECHA',0,50,1),(18,'Papa','Papa cosechada.','COSECHA',0,55,1),(19,'Zanahoria','Zanahoria cosechada.','COSECHA',0,52,1),(20,'Tomate','Tomate cosechado.','COSECHA',0,65,1),(21,'Lechuga','Lechuga cosechada.','COSECHA',0,42,1),(22,'Cebolla','Cebolla cosechada.','COSECHA',0,58,1),(23,'Calabaza','Calabaza cosechada.','COSECHA',0,80,1),(24,'Frutilla','Frutilla cosechada.','COSECHA',0,95,1),(25,'Girasol','Girasol cosechado.','COSECHA',0,75,1),(26,'Soja','Soja cosechada.','COSECHA',0,60,1),(27,'Arroz','Arroz cosechado.','COSECHA',0,65,1),(28,'Remolacha','Remolacha cosechada.','COSECHA',0,55,1),(29,'Morron','Morron cosechado.','COSECHA',0,85,1),(30,'Ajo','Ajo cosechado.','COSECHA',0,70,1),(31,'Huevo','Huevo producido por una gallina.','PRODUCTO_ANIMAL',0,40,1),(32,'Leche','Leche fresca producida por una vaca.','PRODUCTO_ANIMAL',0,70,1),(33,'Lana','Lana obtenida de una oveja.','PRODUCTO_ANIMAL',0,85,1),(34,'Leche de cabra','Leche producida por una cabra.','PRODUCTO_ANIMAL',0,75,1),(35,'Huevo de pato','Huevo producido por un pato.','PRODUCTO_ANIMAL',0,55,1),(36,'Trufa','Trufa encontrada por un cerdo.','PRODUCTO_ANIMAL',0,140,1),(37,'Pelo de conejo','Fibra obtenida de un conejo.','PRODUCTO_ANIMAL',0,60,1),(38,'Lana de alpaca','Lana obtenida de una alpaca.','PRODUCTO_ANIMAL',0,110,1),(39,'Huevo de ganso','Huevo producido por un ganso.','PRODUCTO_ANIMAL',0,65,1),(40,'Huevo de codorniz','Huevo pequeño producido por una codorniz.','PRODUCTO_ANIMAL',0,35,1),(41,'Miel','Miel producida en la granja.','PRODUCTO_ANIMAL',0,100,1),(42,'Pluma','Pluma obtenida de un pavo.','PRODUCTO_ANIMAL',0,45,1),(43,'Leche de oveja','Leche producida por una oveja lechera.','PRODUCTO_ANIMAL',0,80,1),(44,'Fibra animal','Fibra utilizada para fabricar productos.','PRODUCTO_ANIMAL',0,90,1),(45,'Cuero','Material obtenido de animales de granja.','PRODUCTO_ANIMAL',0,120,1),(46,'Madera','Material recolectado con el hacha.','MATERIAL',0,25,1),(47,'Piedra','Material recolectado con el pico.','MATERIAL',0,30,1),(48,'Pejerrey','Pez obtenido mediante pesca.','PESCADO',0,70,1),(49,'Corvina','Pez obtenido mediante pesca.','PESCADO',0,85,1),(50,'Lenguado','Pez obtenido mediante pesca.','PESCADO',0,100,1),(51,'Trucha','Pez obtenido mediante pesca.','PESCADO',0,95,1),(52,'Bagre','Pez obtenido mediante pesca.','PESCADO',0,65,1),(53,'Alimento para animales','Alimento general para los animales.','ALIMENTO',30,10,1),(54,'Heno','Alimento utilizado para animales de granja.','ALIMENTO',25,8,1),(55,'Balanceado','Alimento nutritivo para animales.','ALIMENTO',40,12,1);
/*!40000 ALTER TABLE `item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `partida`
--

DROP TABLE IF EXISTS `partida`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `partida` (
  `id_partida` int unsigned NOT NULL AUTO_INCREMENT,
  `id_usuario` int unsigned NOT NULL,
  `nombre_granja` varchar(100) NOT NULL,
  `nivel` int unsigned NOT NULL DEFAULT '1',
  `experiencia` int unsigned NOT NULL DEFAULT '0',
  `monedas` int unsigned NOT NULL DEFAULT '500',
  `minuto_juego_total` bigint unsigned NOT NULL DEFAULT '480',
  `parcelas_desbloqueadas` tinyint unsigned NOT NULL DEFAULT '2',
  `nivel_terreno` tinyint unsigned NOT NULL DEFAULT '1',
  `tiene_gallinero` tinyint(1) NOT NULL DEFAULT '0',
  `tiene_establo` tinyint(1) NOT NULL DEFAULT '0',
  `rareza_hacha` enum('COMUN','POCO_COMUN','RARA','EPICA') NOT NULL DEFAULT 'COMUN',
  `rareza_pico` enum('COMUN','POCO_COMUN','RARA','EPICA') NOT NULL DEFAULT 'COMUN',
  `rareza_cania` enum('COMUN','POCO_COMUN','RARA','EPICA') NOT NULL DEFAULT 'COMUN',
  `dia_ultimo_evento` int unsigned DEFAULT NULL,
  `estado` enum('ACTIVA','PAUSADA','FINALIZADA') NOT NULL DEFAULT 'ACTIVA',
  `fecha_creacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_guardado` datetime DEFAULT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id_partida`),
  KEY `id_usuario` (`id_usuario`),
  CONSTRAINT `partida_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `partida`
--

LOCK TABLES `partida` WRITE;
/*!40000 ALTER TABLE `partida` DISABLE KEYS */;
INSERT INTO `partida` VALUES (1,3,'Los Aromos',2,50,680,11880,3,1,1,0,'COMUN','COMUN','COMUN',9,'ACTIVA','2026-11-16 11:17:07','2026-11-16 17:39:08',1),(2,4,'El Trigal',2,120,750,870,3,1,1,1,'POCO_COMUN','COMUN','COMUN',NULL,'ACTIVA','2026-11-16 11:17:07',NULL,1),(3,5,'La Esperanza',3,50,1100,1500,4,2,1,1,'POCO_COMUN','POCO_COMUN','COMUN',1,'ACTIVA','2026-11-16 11:17:07',NULL,1),(4,6,'Campo Verde',2,180,650,1900,3,1,0,1,'COMUN','POCO_COMUN','COMUN',NULL,'ACTIVA','2026-11-16 11:17:07',NULL,1),(5,7,'El Molino',4,220,1800,2500,5,2,1,1,'RARA','POCO_COMUN','POCO_COMUN',2,'ACTIVA','2026-11-16 11:17:07',NULL,1),(6,8,'Las Flores',1,40,420,600,2,1,0,1,'COMUN','COMUN','COMUN',NULL,'ACTIVA','2026-11-16 11:17:07',NULL,1),(7,9,'Santa Clara',3,110,1250,3100,4,2,1,1,'POCO_COMUN','RARA','POCO_COMUN',2,'ACTIVA','2026-11-16 11:17:07',NULL,1),(8,10,'El Ombu',2,0,820,1100,3,1,0,1,'COMUN','POCO_COMUN','COMUN',NULL,'ACTIVA','2026-11-16 11:17:07',NULL,1),(9,11,'La Arboleda',5,400,2500,4200,6,2,1,1,'RARA','RARA','POCO_COMUN',3,'ACTIVA','2026-11-16 11:17:07',NULL,1),(10,12,'Sol Naciente',1,70,550,750,2,1,1,0,'COMUN','COMUN','COMUN',NULL,'ACTIVA','2026-11-16 11:17:07',NULL,1),(11,13,'Los Pinos',3,90,1300,2250,4,2,1,1,'POCO_COMUN','POCO_COMUN','RARA',1,'ACTIVA','2026-11-16 11:17:07',NULL,1),(12,14,'La Pradera',2,40,900,1750,3,1,1,1,'POCO_COMUN','COMUN','POCO_COMUN',NULL,'ACTIVA','2026-11-16 11:17:07',NULL,1),(13,15,'El Hornero',4,280,1950,3400,5,2,1,1,'RARA','RARA','RARA',2,'ACTIVA','2026-11-16 11:17:07',NULL,1),(14,16,'Las Acacias',2,150,700,980,3,1,1,1,'COMUN','POCO_COMUN','COMUN',NULL,'ACTIVA','2026-11-16 11:17:07',NULL,1),(15,17,'La Colina',6,0,3200,5200,6,2,1,1,'EPICA','RARA','RARA',3,'ACTIVA','2026-11-16 11:17:07',NULL,1);
/*!40000 ALTER TABLE `partida` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `plantacion`
--

DROP TABLE IF EXISTS `plantacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `plantacion` (
  `id_plantacion` int unsigned NOT NULL AUTO_INCREMENT,
  `id_partida` int unsigned NOT NULL,
  `id_cultivo` int unsigned NOT NULL,
  `numero_parcela` smallint unsigned NOT NULL,
  `minuto_siembra_juego` bigint unsigned NOT NULL,
  `estado` enum('CRECIENDO','LISTA','COSECHADA') NOT NULL DEFAULT 'CRECIENDO',
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id_plantacion`),
  KEY `id_partida` (`id_partida`),
  KEY `id_cultivo` (`id_cultivo`),
  CONSTRAINT `plantacion_ibfk_1` FOREIGN KEY (`id_partida`) REFERENCES `partida` (`id_partida`) ON DELETE CASCADE,
  CONSTRAINT `plantacion_ibfk_2` FOREIGN KEY (`id_cultivo`) REFERENCES `cultivo` (`id_cultivo`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `plantacion`
--

LOCK TABLES `plantacion` WRITE;
/*!40000 ALTER TABLE `plantacion` DISABLE KEYS */;
INSERT INTO `plantacion` VALUES (1,1,1,1,480,'COSECHADA',1),(2,2,2,1,500,'CRECIENDO',1),(3,3,3,1,600,'CRECIENDO',1),(4,4,4,2,700,'LISTA',1),(5,5,5,1,800,'LISTA',1),(6,6,6,1,500,'CRECIENDO',1),(7,7,7,2,1000,'LISTA',1),(8,8,8,1,650,'CRECIENDO',1),(9,9,9,3,1300,'COSECHADA',1),(10,10,10,1,600,'CRECIENDO',1),(11,11,11,2,900,'LISTA',1),(12,12,12,1,800,'CRECIENDO',1),(13,13,13,3,1400,'COSECHADA',1),(14,14,14,1,700,'CRECIENDO',1),(15,15,15,2,1600,'LISTA',1),(16,1,1,1,1815,'COSECHADA',1),(17,1,1,2,2460,'COSECHADA',1),(18,1,1,1,6120,'CRECIENDO',1);
/*!40000 ALTER TABLE `plantacion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `progreso_mision`
--

DROP TABLE IF EXISTS `progreso_mision`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `progreso_mision` (
  `id_progreso_mision` int unsigned NOT NULL AUTO_INCREMENT,
  `id_partida` int unsigned NOT NULL,
  `codigo_mision` varchar(50) NOT NULL,
  `progreso` int unsigned NOT NULL DEFAULT '0',
  `estado` enum('ACTIVA','COMPLETADA','RECLAMADA') NOT NULL DEFAULT 'ACTIVA',
  PRIMARY KEY (`id_progreso_mision`),
  UNIQUE KEY `id_partida` (`id_partida`,`codigo_mision`),
  CONSTRAINT `progreso_mision_ibfk_1` FOREIGN KEY (`id_partida`) REFERENCES `partida` (`id_partida`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `progreso_mision`
--

LOCK TABLES `progreso_mision` WRITE;
/*!40000 ALTER TABLE `progreso_mision` DISABLE KEYS */;
INSERT INTO `progreso_mision` VALUES (1,1,'PRIMERA_COSECHA',1,'RECLAMADA'),(2,2,'PRIMERA_COSECHA',1,'COMPLETADA'),(3,3,'PRIMERA_COSECHA',1,'RECLAMADA'),(4,4,'AYUDANDO_CARPINTERO',4,'ACTIVA'),(5,5,'AYUDANDO_CARPINTERO',10,'COMPLETADA'),(6,6,'AYUDANDO_CARPINTERO',10,'RECLAMADA'),(7,7,'PEDIDO_PESCADOR',2,'ACTIVA'),(8,8,'PEDIDO_PESCADOR',5,'COMPLETADA'),(9,9,'PEDIDO_PESCADOR',5,'RECLAMADA'),(10,10,'PIEDRA_PARA_EL_HERRERO',3,'ACTIVA'),(11,11,'PIEDRA_PARA_EL_HERRERO',8,'COMPLETADA'),(12,12,'PRIMER_HUEVO',1,'COMPLETADA'),(13,13,'PRIMER_HUEVO',1,'RECLAMADA'),(14,14,'LECHE_FRESCA',0,'ACTIVA'),(15,15,'LECHE_FRESCA',1,'COMPLETADA'),(16,1,'PIEDRA_PARA_EL_HERRERO',6,'ACTIVA'),(17,1,'AYUDANDO_CARPINTERO',4,'ACTIVA'),(18,1,'PEDIDO_PESCADOR',0,'ACTIVA');
/*!40000 ALTER TABLE `progreso_mision` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tipo_animal`
--

DROP TABLE IF EXISTS `tipo_animal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tipo_animal` (
  `id_tipo_animal` int unsigned NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `precio_compra` int unsigned NOT NULL DEFAULT '0',
  `precio_venta` int unsigned NOT NULL DEFAULT '0',
  `id_item_producto` int unsigned DEFAULT NULL,
  `cantidad_producto` int unsigned NOT NULL DEFAULT '1',
  `tiempo_produccion_min` int unsigned NOT NULL DEFAULT '1440',
  `alojamiento` enum('GALLINERO','ESTABLO') NOT NULL DEFAULT 'ESTABLO',
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id_tipo_animal`),
  UNIQUE KEY `nombre` (`nombre`),
  KEY `id_item_producto` (`id_item_producto`),
  CONSTRAINT `tipo_animal_ibfk_1` FOREIGN KEY (`id_item_producto`) REFERENCES `item` (`id_item`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipo_animal`
--

LOCK TABLES `tipo_animal` WRITE;
/*!40000 ALTER TABLE `tipo_animal` DISABLE KEYS */;
INSERT INTO `tipo_animal` VALUES (1,'Gallina','Animal que produce huevos.',300,150,31,1,720,'GALLINERO',1),(2,'Vaca','Animal que produce leche.',1200,700,32,2,1440,'ESTABLO',1),(3,'Oveja','Animal que produce lana.',800,450,33,1,1440,'ESTABLO',1),(4,'Cabra','Animal que produce leche de cabra.',900,500,34,1,1200,'ESTABLO',1),(5,'Pato','Animal que produce huevos de pato.',450,250,35,1,900,'GALLINERO',1),(6,'Cerdo','Animal capaz de encontrar trufas.',1000,550,36,1,1800,'ESTABLO',1),(7,'Conejo','Animal que produce fibra.',350,180,37,1,1200,'ESTABLO',1),(8,'Alpaca','Animal que produce lana de alpaca.',1300,750,38,1,1800,'ESTABLO',1),(9,'Ganso','Animal que produce huevos de ganso.',500,280,39,1,1080,'GALLINERO',1),(10,'Codorniz','Animal que produce huevos pequeños.',250,120,40,2,720,'GALLINERO',1),(11,'Abeja','Produce miel para vender.',600,300,41,1,1440,'ESTABLO',1),(12,'Pavo','Animal que produce plumas.',550,300,42,1,1200,'GALLINERO',1),(13,'Oveja lechera','Oveja especializada en producir leche.',950,520,43,1,1440,'ESTABLO',1),(14,'Llama','Animal que produce fibra.',1100,600,44,1,1800,'ESTABLO',1),(15,'Buey','Animal de granja del que puede obtenerse cuero.',1500,850,45,1,2160,'ESTABLO',1);
/*!40000 ALTER TABLE `tipo_animal` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `id_usuario` int unsigned NOT NULL AUTO_INCREMENT,
  `nombre_usuario` varchar(50) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `contrasenia_hash` varchar(255) NOT NULL,
  `rol` enum('JUGADOR','ADMIN') NOT NULL DEFAULT 'JUGADOR',
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  `fecha_creacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `nombre_usuario` (`nombre_usuario`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (1,'admin01','admin01@laestancia.com','03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4','ADMIN',1,'2026-11-16 11:17:07'),(2,'admin02','admin02@laestancia.com','03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4','ADMIN',1,'2026-11-16 11:17:07'),(3,'jugador01','jugador01@laestancia.com','03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4','JUGADOR',1,'2026-11-16 11:17:07'),(4,'jugador02','jugador02@laestancia.com','03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4','JUGADOR',1,'2026-11-16 11:17:07'),(5,'jugador03','jugador03@laestancia.com','03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4','JUGADOR',1,'2026-11-16 11:17:07'),(6,'jugador04','jugador04@laestancia.com','03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4','JUGADOR',1,'2026-11-16 11:17:07'),(7,'jugador05','jugador05@laestancia.com','03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4','JUGADOR',1,'2026-11-16 11:17:07'),(8,'jugador06','jugador06@laestancia.com','03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4','JUGADOR',1,'2026-11-16 11:17:07'),(9,'jugador07','jugador07@laestancia.com','03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4','JUGADOR',1,'2026-11-16 11:17:07'),(10,'jugador08','jugador08@laestancia.com','03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4','JUGADOR',1,'2026-11-16 11:17:07'),(11,'jugador09','jugador09@laestancia.com','03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4','JUGADOR',1,'2026-11-16 11:17:07'),(12,'jugador10','jugador10@laestancia.com','03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4','JUGADOR',1,'2026-11-16 11:17:07'),(13,'jugador11','jugador11@laestancia.com','03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4','JUGADOR',1,'2026-11-16 11:17:07'),(14,'jugador12','jugador12@laestancia.com','03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4','JUGADOR',1,'2026-11-16 11:17:07'),(15,'jugador13','jugador13@laestancia.com','03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4','JUGADOR',1,'2026-11-16 11:17:07'),(16,'jugador14','jugador14@laestancia.com','03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4','JUGADOR',1,'2026-11-16 11:17:07'),(17,'jugador15','jugador15@laestancia.com','03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4','JUGADOR',1,'2026-11-16 11:17:07');
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-11-17 19:29:48
