-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: fernanshop_db
-- ------------------------------------------------------
-- Server version	9.3.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES UTF8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `fernanshop_db`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `fernanshop_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `fernanshop_db`;

--
-- Table structure for table `Admins`
--

DROP TABLE IF EXISTS `Admins`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Admins` (
  `Id_Admin` varchar(100) NOT NULL,
  PRIMARY KEY (`Id_Admin`),
  CONSTRAINT `Admins_ibfk_1` FOREIGN KEY (`Id_Admin`) REFERENCES `Usuarios` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Admins`
--

LOCK TABLES `Admins` WRITE;
/*!40000 ALTER TABLE `Admins` DISABLE KEYS */;
INSERT INTO `Admins` VALUES ('A23432'),('A34535');
/*!40000 ALTER TABLE `Admins` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb3 */ ;
/*!50003 SET character_set_results = utf8mb3 */ ;
/*!50003 SET collation_connection  = utf8mb3_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`%`*/ /*!50003 TRIGGER `validar_id_Admins` BEFORE INSERT ON `Admins` FOR EACH ROW BEGIN
        IF NEW.Id_Admin NOT REGEXP ('A[0-9]+') THEN
        SIGNAL SQLSTATE '45000';
        END IF;
    END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `Clientes`
--

DROP TABLE IF EXISTS `Clientes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Clientes` (
  `Id_Cliente` varchar(100) NOT NULL,
  `Direccion` varchar(100) DEFAULT NULL,
  `Localidad` varchar(100) DEFAULT NULL,
  `Provincia` varchar(100) DEFAULT NULL,
  `token` varchar(100) DEFAULT NULL,
  `correoValidado` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`Id_Cliente`),
  CONSTRAINT `FK_cliente_usuario` FOREIGN KEY (`Id_Cliente`) REFERENCES `Usuarios` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Clientes`
--

LOCK TABLES `Clientes` WRITE;
/*!40000 ALTER TABLE `Clientes` DISABLE KEYS */;
INSERT INTO `Clientes` VALUES ('C23432','c\\ Jurado','Castro del Rio','Cordoba','t',1),('C34535','Av. Olivares 1','Martos','Jaen','t',0),('C56433','c\\ Santo Cristo','Loja','Granada','t',0);
/*!40000 ALTER TABLE `Clientes` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb3 */ ;
/*!50003 SET character_set_results = utf8mb3 */ ;
/*!50003 SET collation_connection  = utf8mb3_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`%`*/ /*!50003 TRIGGER `validar_id_clientes` BEFORE INSERT ON `Clientes` FOR EACH ROW BEGIN
        IF NEW.Id_Cliente NOT REGEXP ('C[0-9]+') THEN
        SIGNAL SQLSTATE '45000';
        END IF;
    END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `Pedido_asignado_trabajador`
--

DROP TABLE IF EXISTS `Pedido_asignado_trabajador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Pedido_asignado_trabajador` (
  `id_Pedido` varchar(255) NOT NULL,
  `id_trabajadorAsignado` varchar(255) NOT NULL,
  PRIMARY KEY (`id_Pedido`,`id_trabajadorAsignado`),
  KEY `FK_pedido_Asignado_Trabajador` (`id_trabajadorAsignado`),
  CONSTRAINT `FK_pedido_Asignado_Trabajador` FOREIGN KEY (`id_trabajadorAsignado`) REFERENCES `trabajadores` (`Id_Trabajador`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_pedido_Asignado_trabajador_Pedidos` FOREIGN KEY (`id_Pedido`) REFERENCES `Pedidos` (`id_Pedido`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Pedido_asignado_trabajador`
--

LOCK TABLES `Pedido_asignado_trabajador` WRITE;
/*!40000 ALTER TABLE `Pedido_asignado_trabajador` DISABLE KEYS */;
INSERT INTO `Pedido_asignado_trabajador` VALUES ('P1','T23432'),('P2','T23432'),('P3','T23432'),('P49472','T34535'),('P48711','T56433'),('P48514','T5839');
/*!40000 ALTER TABLE `Pedido_asignado_trabajador` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Pedidos`
--

DROP TABLE IF EXISTS `Pedidos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Pedidos` (
  `id_Pedido` varchar(255) NOT NULL,
  `fechaPedido` datetime DEFAULT CURRENT_TIMESTAMP,
  `fechaEntrega` datetime DEFAULT NULL,
  `estado` int DEFAULT '0',
  `comentario` varchar(255) DEFAULT '',
  `id_Cliente` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_Pedido`),
  KEY `FK_pedidos_clientes` (`id_Cliente`),
  CONSTRAINT `FK_pedidos_clientes` FOREIGN KEY (`id_Cliente`) REFERENCES `Clientes` (`Id_Cliente`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ck_Pedido_id` CHECK (regexp_like(`id_Pedido`,_utf8mb3'P[0-9]+'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Pedidos`
--

LOCK TABLES `Pedidos` WRITE;
/*!40000 ALTER TABLE `Pedidos` DISABLE KEYS */;
INSERT INTO `Pedidos` VALUES ('P1','2024-11-11 00:00:00',NULL,3,'asdf','C23432'),('P2','2024-11-13 00:00:00','2024-11-22 00:00:00',1,'','C23432'),('P27890','2025-05-24 19:23:21','2025-05-31 19:23:21',0,'','C23432'),('P3','2024-11-13 00:00:00','2024-11-22 00:00:00',1,'','C23432'),('P48514','2025-05-24 19:40:45','2025-05-31 19:40:45',0,'','C23432'),('P48711','2025-05-24 08:50:48','2025-05-31 08:50:48',0,'','C23432'),('P49472','2025-05-24 19:06:39','2025-05-31 19:06:39',0,'','C23432'),('P7415','2025-05-24 19:24:52','2025-05-31 19:24:52',0,'','C23432');
/*!40000 ALTER TABLE `Pedidos` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb3 */ ;
/*!50003 SET character_set_results = utf8mb3 */ ;
/*!50003 SET collation_connection  = utf8mb3_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`%`*/ /*!50003 TRIGGER `fecha_entrega_default` BEFORE INSERT ON `Pedidos` FOR EACH ROW BEGIN
        IF New.fechaEntrega IS NULL THEN
            SET NEW.fechaEntrega = CURRENT_TIMESTAMP + INTERVAL 7 DAY;
        END IF;
    END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb3 */ ;
/*!50003 SET character_set_results = utf8mb3 */ ;
/*!50003 SET collation_connection  = utf8mb3_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`%`*/ /*!50003 TRIGGER `set_fechaEntrega_null_estado3` BEFORE UPDATE ON `Pedidos` FOR EACH ROW BEGIN
        IF NEW.estado = 3 AND OLD.estado <> 3 THEN
            SET NEW.fechaEntrega = NULL;
        END IF;
    END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `Productos`
--

DROP TABLE IF EXISTS `Productos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Productos` (
  `id_producto` int NOT NULL,
  `Marca` varchar(100) NOT NULL,
  `Modelo` varchar(100) NOT NULL,
  `Descripcion` varchar(255) NOT NULL,
  `Precio` decimal(6,2) NOT NULL,
  `Relevancia` int NOT NULL,
  PRIMARY KEY (`id_producto`),
  CONSTRAINT `CK_producto_relevancia` CHECK (((`relevancia` >= 0) and (`relevancia` < 11)))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Productos`
--

LOCK TABLES `Productos` WRITE;
/*!40000 ALTER TABLE `Productos` DISABLE KEYS */;
INSERT INTO `Productos` VALUES (5643,'Sony','WH-1000XM5','Auriculares inalámbricos con cancelación de ruido',349.99,8),(8330,'Philips','Hue Starter Kit','Kit de luces inteligentes con puente y bombillas',199.99,8),(12434,'Dyson','V15 Detect','Aspiradora inalámbrica de alta potencia',799.99,7),(14566,'Asus','ROG Zephyrus G14','Portátil gaming con procesador Ryzen 9',1799.99,9),(23432,'Samsung','Galaxy S23','Smartphone con pantalla AMOLED de 6.1 pulgadas',899.99,9),(34545,'Bosch','Serie 6 SMS68TI01E','Lavavajillas eficiente y silencioso',799.00,8),(45459,'HP','Spectre x360','Portátil 2 en 1 con pantalla táctil 4K',1599.00,9),(45632,'LG','OLED55C3','Televisor OLED 4K de 55 pulgadas',1399.00,9),(45742,'Canon','EOS R6','Cámara sin espejo de alta resolución',2499.00,9),(56323,'Levi\'s','501 Original','Vaqueros clásicos de corte recto',89.99,7),(56341,'IKEA','MALM','Cama con almacenamiento incorporado',399.00,7),(56343,'Whirlpool','W Collection W7','Horno empotrado multifunción con autolimpieza',1299.00,9),(60903,'Amazon','Echo Dot 5ª Gen','Altavoz inteligente con Alexa integrado',49.99,8),(67233,'Nike','Air Max 270','Zapatillas deportivas para correr',129.99,8),(78648,'Garmin','Fenix 7','Reloj multideporte con GPS avanzado',699.99,9),(83263,'KitchenAid','Artisan 5KSM150','Batidora de pie para repostería',499.99,8),(84635,'Xiaomi','Mi Electric Scooter 4 Pro','Patinete eléctrico con gran autonomía',849.99,8),(84675,'DeWalt','DCD796D2-QW','Taladro percutor inalámbrico compacto',199.99,8),(89073,'Microsoft','Surface Pro 9','Tablet convertible en portátil con pantalla táctil',1199.00,10),(92371,'Adidas','Ultraboost 22','Zapatillas de running con amortiguación avanzada',179.99,9),(93639,'Bose','SoundLink Revolve+','Altavoz Bluetooth portátil con sonido 360°',299.99,8),(93723,'Nintendo','Switch OLED','Consola híbrida con pantalla OLED',349.99,9),(94856,'Sony','PlayStation 5','Consola de videojuegos de última generación',499.99,10),(97463,'Fitbit','Charge 6','Pulsera de actividad física con seguimiento avanzado',129.99,8),(98475,'Samsung','Galaxy Watch 6','Reloj inteligente con monitor de salud',299.99,8),(345345,'Apple','MacBook Pro 16','Portátil de alto rendimiento con chip M2 Pro',2499.00,10),(456333,'Logitech','MX Master 3','Ratón inalámbrico de alta precisión para profesionales',99.99,9);
/*!40000 ALTER TABLE `Productos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Usuarios`
--

DROP TABLE IF EXISTS `Usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Usuarios` (
  `Id` varchar(100) NOT NULL,
  `Nombre` varchar(100) DEFAULT NULL,
  `Correo` varchar(255) DEFAULT NULL,
  `Movil` int DEFAULT NULL,
  `Pass` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `Correo` (`Correo`),
  CONSTRAINT `ck_Users_id` CHECK (regexp_like(`Id`,_utf8mb3'([ACT])[0-9]+')),
  CONSTRAINT `ck_usuarios_Correo` CHECK (regexp_like(`Correo`,_utf8mb3'[a-zA-Z0-9._%+-]{1,64}@[a-zA-Z0-9-.]{1,253}\\.[a-zA-Z]{2,63}'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Usuarios`
--

LOCK TABLES `Usuarios` WRITE;
/*!40000 ALTER TABLE `Usuarios` DISABLE KEYS */;
INSERT INTO `Usuarios` VALUES ('A23432','Paco','don.92.ald@gmail.com',657852741,'asdfASDF_4'),('A34535','Josebas','admin2@gmail.com',639852147,'asdfASDF_4'),('C23432','Javier Cli','dondcas@gmail.com',654960489,'asdfASDF_4'),('C34535','Eduardo','cliente2@gmail.com',666666666,'asdfASDF_4'),('C56433','Loles','cliente3@gmail.com',722687985,'asdfASDF_4'),('T23432','Javier Tra','fjrueda92@gmail.com',687987456,'asdfASDF_4'),('T34535','Mireya','trabajador2@gmail.com',687123456,'asdfASDF_4'),('T56433','Lolo','trabajador3@gmail.com',687987456,'asdfASDF_4'),('T5839','Nuevo','dondcas@hotmail.com',654963214,'asdfASDF_4');
/*!40000 ALTER TABLE `Usuarios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `carritos`
--

DROP TABLE IF EXISTS `carritos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `carritos` (
  `idCliente` varchar(255) NOT NULL,
  `idProducto` int NOT NULL,
  `cantidad` int DEFAULT '1',
  PRIMARY KEY (`idCliente`,`idProducto`),
  KEY `FK_Carrito_Producto` (`idProducto`),
  CONSTRAINT `FK_Carrito_Cliente` FOREIGN KEY (`idCliente`) REFERENCES `Clientes` (`Id_Cliente`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_Carrito_Producto` FOREIGN KEY (`idProducto`) REFERENCES `Productos` (`id_producto`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `CK_carritos_cantidad` CHECK ((`cantidad` >= 1))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `carritos`
--

LOCK TABLES `carritos` WRITE;
/*!40000 ALTER TABLE `carritos` DISABLE KEYS */;
/*!40000 ALTER TABLE `carritos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lineasPedido`
--

DROP TABLE IF EXISTS `lineasPedido`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lineasPedido` (
  `id_linea` int NOT NULL AUTO_INCREMENT,
  `id_pedido` varchar(255) NOT NULL,
  `id_producto` int NOT NULL,
  `Marca` varchar(100) NOT NULL,
  `Modelo` varchar(100) NOT NULL,
  `Descripcion` varchar(255) NOT NULL,
  `Precio` decimal(6,2) NOT NULL,
  `Relevancia` int NOT NULL,
  `cantidadProducto` int DEFAULT NULL,
  PRIMARY KEY (`id_linea`,`id_pedido`,`id_producto`),
  KEY `FK_Linea_pedido` (`id_pedido`),
  CONSTRAINT `FK_Linea_pedido` FOREIGN KEY (`id_pedido`) REFERENCES `Pedidos` (`id_Pedido`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lineasPedido`
--

LOCK TABLES `lineasPedido` WRITE;
/*!40000 ALTER TABLE `lineasPedido` DISABLE KEYS */;
INSERT INTO `lineasPedido` VALUES (1,'P1',23432,'Samsung','Galaxy S23','Smartphone con pantalla AMOLED de 6.1 pulgadas',899.99,9,30),(2,'P1',345345,'Apple','MacBook Pro 16','Portátil de alto rendimiento con chip M2 Pro',2499.00,10,2),(3,'P1',5643,'Sony','WH-1000XM5','Auriculares inalámbricos con cancelación de ruido',349.99,8,7),(4,'P2',23432,'Samsung','Galaxy S23','Smartphone con pantalla AMOLED de 6.1 pulgadas',899.99,9,33),(5,'P2',345345,'Apple','MacBook Pro 16','Portátil de alto rendimiento con chip M2 Pro',2499.00,10,50),(6,'P2',5643,'Sony','WH-1000XM5','Auriculares inalámbricos con cancelación de ruido',349.99,8,2),(7,'P3',5643,'Sony','WH-1000XM5','Auriculares inalámbricos con cancelación de ruido',349.99,8,2),(8,'P3',5643,'Sony','WH-1000XM5','Auriculares inalámbricos con cancelación de ruido',349.99,8,2),(9,'P48711',23432,'Samsung','Galaxy S23','Smartphone con pantalla AMOLED de 6.1 pulgadas',899.99,9,5),(10,'P49472',8330,'Philips','Hue Starter Kit','Kit de luces inteligentes con puente y bombillas',199.99,8,4),(11,'P49472',14566,'Asus','ROG Zephyrus G14','Portátil gaming con procesador Ryzen 9',1799.99,9,8),(12,'P49472',93723,'Nintendo','Switch OLED','Consola híbrida con pantalla OLED',349.99,9,2),(13,'P49472',94856,'Sony','PlayStation 5','Consola de videojuegos de última generación',499.99,10,3),(14,'P27890',8330,'Philips','Hue Starter Kit','Kit de luces inteligentes con puente y bombillas',199.99,8,2),(15,'P7415',8330,'Philips','Hue Starter Kit','Kit de luces inteligentes con puente y bombillas',199.99,8,2),(16,'P48514',12434,'Dyson','V15 Detect','Aspiradora inalámbrica de alta potencia',799.99,7,2);
/*!40000 ALTER TABLE `lineasPedido` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `trabajadores`
--

DROP TABLE IF EXISTS `trabajadores`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trabajadores` (
  `Id_Trabajador` varchar(100) NOT NULL,
  `idTelegram` int DEFAULT NULL,
  `baja` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`Id_Trabajador`),
  CONSTRAINT `FK_trabajador_usuario` FOREIGN KEY (`Id_Trabajador`) REFERENCES `Usuarios` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `trabajadores`
--

LOCK TABLES `trabajadores` WRITE;
/*!40000 ALTER TABLE `trabajadores` DISABLE KEYS */;
INSERT INTO `trabajadores` VALUES ('T23432',398745054,0),('T34535',398745054,0),('T56433',398745054,0),('T5839',398745054,0);
/*!40000 ALTER TABLE `trabajadores` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb3 */ ;
/*!50003 SET character_set_results = utf8mb3 */ ;
/*!50003 SET collation_connection  = utf8mb3_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`%`*/ /*!50003 TRIGGER `validar_id_trabajadores` BEFORE INSERT ON `trabajadores` FOR EACH ROW BEGIN
        IF NEW.Id_Trabajador NOT REGEXP ('T[0-9]+') THEN
        SIGNAL SQLSTATE '45000';
        END IF;
    END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-26 22:27:44
