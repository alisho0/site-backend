-- MySQL dump 10.13  Distrib 8.0.43, for Linux (x86_64)
--
-- Host: localhost    Database: site_backend
-- ------------------------------------------------------
-- Server version	8.0.43

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `audiencia`
--

DROP TABLE IF EXISTS `audiencia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `audiencia` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `fecha` datetime(6) DEFAULT NULL,
  `hora` varchar(255) DEFAULT NULL,
  `lugar` varchar(255) DEFAULT NULL,
  `persona` varchar(255) DEFAULT NULL,
  `id_expediente` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKmn0nl7q6wn3qn31hycn0jenha` (`id_expediente`),
  CONSTRAINT `FKmn0nl7q6wn3qn31hycn0jenha` FOREIGN KEY (`id_expediente`) REFERENCES `expedientes` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `audiencia`
--

LOCK TABLES `audiencia` WRITE;
/*!40000 ALTER TABLE `audiencia` DISABLE KEYS */;
INSERT INTO `audiencia` VALUES (1,'2025-11-14 14:00:00.000000','11:00','DGC',NULL,1),(2,'2025-11-27 14:00:00.000000','11:00','DGC',NULL,3);
/*!40000 ALTER TABLE `audiencia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `audiencia_persona`
--

DROP TABLE IF EXISTS `audiencia_persona`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `audiencia_persona` (
  `audiencia_id` bigint NOT NULL,
  `persona_id` bigint NOT NULL,
  KEY `FKole7agb7flo85py3ppdriftp5` (`persona_id`),
  KEY `FKnw7qy7hy2rrpvshgqsqhyjwek` (`audiencia_id`),
  CONSTRAINT `FKnw7qy7hy2rrpvshgqsqhyjwek` FOREIGN KEY (`audiencia_id`) REFERENCES `audiencia` (`id`),
  CONSTRAINT `FKole7agb7flo85py3ppdriftp5` FOREIGN KEY (`persona_id`) REFERENCES `personas` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `audiencia_persona`
--

LOCK TABLES `audiencia_persona` WRITE;
/*!40000 ALTER TABLE `audiencia_persona` DISABLE KEYS */;
INSERT INTO `audiencia_persona` VALUES (1,1),(1,2),(2,13),(2,14);
/*!40000 ALTER TABLE `audiencia_persona` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `denuncia_estado`
--

DROP TABLE IF EXISTS `denuncia_estado`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `denuncia_estado` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `estado` varchar(255) DEFAULT NULL,
  `fecha` datetime(6) DEFAULT NULL,
  `observacion` varchar(255) DEFAULT NULL,
  `denuncia_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKb3iy3l2w2xgbonyvxfq0t7ill` (`denuncia_id`),
  CONSTRAINT `FKb3iy3l2w2xgbonyvxfq0t7ill` FOREIGN KEY (`denuncia_id`) REFERENCES `denuncias` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `denuncia_estado`
--

LOCK TABLES `denuncia_estado` WRITE;
/*!40000 ALTER TABLE `denuncia_estado` DISABLE KEYS */;
INSERT INTO `denuncia_estado` VALUES (1,'ADMITIDO','2025-08-20 13:25:11.139724','Se pone en proceso',1),(2,'ASESORÍA LEGAL','2025-11-05 11:41:31.287015','Cambio a As. Legal',1),(3,'ADMITIDO','2025-11-05 11:49:49.848567','Admisión de la denuncia',4),(4,'ADMITIDO','2025-11-26 15:38:35.982777','Cambio de estado\n',5),(5,'ASESORÍA LEGAL','2025-11-26 15:38:56.305991','Abogado manda a asesoría',4),(6,'EN DIRECCIÓN','2025-11-26 15:39:17.866402','Directora verifica',1),(7,'ADMITIDO','2025-11-26 18:36:44.221476','Aceptado',2),(8,'RECHAZADO','2025-11-26 18:37:11.637403','No tiene datos',3),(9,'ADMITIDO','2025-11-26 18:39:01.578960','Aceptado',6),(10,'EN DIRECCIÓN','2025-11-26 18:39:15.788276','En dir.',5),(11,'EN SUBDIRECCIÓN','2025-11-26 18:39:32.622205','Sub',6),(12,'FINALIZADO','2025-11-26 18:39:44.684440','Se finaliza\n',6);
/*!40000 ALTER TABLE `denuncia_estado` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `denuncia_persona`
--

DROP TABLE IF EXISTS `denuncia_persona`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `denuncia_persona` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `apellido_delegado` varchar(255) DEFAULT NULL,
  `dni_delegado` varchar(255) DEFAULT NULL,
  `nombre_delegado` varchar(255) DEFAULT NULL,
  `rol` varchar(255) DEFAULT NULL,
  `denuncia_id` bigint DEFAULT NULL,
  `persona_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1qoqats8gklgosxnq3vhardqx` (`denuncia_id`),
  KEY `FKk887rjkxmyi8ywivite2eayxh` (`persona_id`),
  CONSTRAINT `FK1qoqats8gklgosxnq3vhardqx` FOREIGN KEY (`denuncia_id`) REFERENCES `denuncias` (`id`),
  CONSTRAINT `FKk887rjkxmyi8ywivite2eayxh` FOREIGN KEY (`persona_id`) REFERENCES `personas` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `denuncia_persona`
--

LOCK TABLES `denuncia_persona` WRITE;
/*!40000 ALTER TABLE `denuncia_persona` DISABLE KEYS */;
INSERT INTO `denuncia_persona` VALUES (1,'Pérez','90111222','Delegada Denunciante','denunciante',1,1),(2,NULL,NULL,NULL,'denunciado',1,2),(3,NULL,NULL,NULL,'tecnico',1,3),(4,'Rodríguez','90222333','Delegado Denunciante','denunciante',2,4),(5,NULL,NULL,NULL,'denunciado',2,5),(6,NULL,NULL,NULL,'tecnico',2,6),(7,'','','','denunciante',4,11),(8,'','','','denunciado',4,12),(9,'Ramírez','81222333','Delegado Denunciante','denunciante',5,13),(10,NULL,NULL,NULL,'denunciado',5,14),(11,NULL,NULL,NULL,'tecnico',5,15),(12,'Ramírez','81222333','Delegado Denunciante','denunciante',6,13),(13,NULL,NULL,NULL,'denunciado',6,14),(14,NULL,NULL,NULL,'tecnico',6,15),(15,'Sánchez','83445566','Delegado Denunciante','denunciante',7,16),(16,NULL,NULL,NULL,'denunciado',7,17),(17,NULL,NULL,NULL,'tecnico',7,18),(18,'Aguilar','84556677','Delegado Denunciante','denunciante',8,19),(19,NULL,NULL,NULL,'denunciado',8,20),(20,NULL,NULL,NULL,'tecnico',8,21),(21,'Pereyra','85667788','Delegado Denunciante','denunciante',9,22),(22,NULL,NULL,NULL,'denunciado',9,23),(23,NULL,NULL,NULL,'tecnico',9,24),(24,'Gutiérrez','86778899','Delegado Denunciante','denunciante',10,25),(25,NULL,NULL,NULL,'denunciado',10,26),(26,NULL,NULL,NULL,'tecnico',10,27),(27,'Domínguez','87889900','Delegado Denunciante','denunciante',11,28),(28,NULL,NULL,NULL,'denunciado',11,29),(29,NULL,NULL,NULL,'tecnico',11,24),(30,'Moreno','88990011','Delegado Denunciante','denunciante',12,30),(31,NULL,NULL,NULL,'denunciado',12,31),(32,NULL,NULL,NULL,'tecnico',12,32),(33,'López','90112233','Delegado Denunciante','denunciante',13,34),(34,NULL,NULL,NULL,'denunciado',13,35),(35,NULL,NULL,NULL,'tecnico',13,36);
/*!40000 ALTER TABLE `denuncia_persona` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `denuncias`
--

DROP TABLE IF EXISTS `denuncias`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `denuncias` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(255) DEFAULT NULL,
  `estado` varchar(255) DEFAULT NULL,
  `motivo` varbinary(255) DEFAULT NULL,
  `objeto` varbinary(255) DEFAULT NULL,
  `expediente_id` bigint DEFAULT NULL,
  `notificar` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKhi6v10f2nuxdir1xqbsfn14a8` (`expediente_id`),
  CONSTRAINT `FK7u1s1n0bmgdb1rpeanxyyoes8` FOREIGN KEY (`expediente_id`) REFERENCES `expedientes` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `denuncias`
--

LOCK TABLES `denuncias` WRITE;
/*!40000 ALTER TABLE `denuncias` DISABLE KEYS */;
INSERT INTO `denuncias` VALUES (1,'Se ofreció un servicio premium y se brindó uno básico sin previo aviso.','EN DIRECCIÓN',_binary '�\�\0sr\0java.util.ArrayListx�\��\�a�\0I\0sizexp\0\0\0w\0\0\0t\0Falla en el serviciot\0 No corresponde con lo contratadox',_binary '�\�\0sr\0java.util.ArrayListx�\��\�a�\0I\0sizexp\0\0\0w\0\0\0t\0Incumplimiento de condicionest\0Atención deficientex',1,NULL),(2,'El producto llegó después de la fecha de entrega estipulada y presenta fallas de fábrica.','ADMITIDO',_binary '�\�\0sr\0java.util.ArrayListx�\��\�a�\0I\0sizexp\0\0\0w\0\0\0t\0Retraso en la entregat\0Producto defectuosox',_binary '�\�\0sr\0java.util.ArrayListx�\��\�a�\0I\0sizexp\0\0\0w\0\0\0t\0Incumplimiento de plazost\0Calidad del productox',4,NULL),(3,'Descripción de la denuncia para ser aceptada.','RECHAZADO',_binary '�\�\0sr\0java.util.ArrayListx�\��\�a�\0I\0sizexp\0\0\0w\0\0\0t\0Problemas con el serviciot\0Publicidad engañosat\0Otrosx',_binary '�\�\0sr\0java.util.ArrayListx�\��\�a�\0I\0sizexp\0\0\0w\0\0\0t\0Cambio de productot\0Reparaciónx',NULL,_binary ''),(4,'Descripción de la denuncia para ser aceptada.','ASESORÍA LEGAL',_binary '�\�\0sr\0java.util.ArrayListx�\��\�a�\0I\0sizexp\0\0\0w\0\0\0t\0Problemas con el serviciot\0Publicidad engañosat\0Otrosx',_binary '�\�\0sr\0java.util.ArrayListx�\��\�a�\0I\0sizexp\0\0\0w\0\0\0t\0Cambio de productot\0Reparaciónx',2,_binary ''),(5,'El producto entregado presentó fallas técnicas y no se respetó la garantía ofrecida.','EN DIRECCIÓN',_binary '�\�\0sr\0java.util.ArrayListx�\��\�a�\0I\0sizexp\0\0\0w\0\0\0t\0Producto defectuosot\0Problemas con la garantiax',_binary '�\�\0sr\0java.util.ArrayListx�\��\�a�\0I\0sizexp\0\0\0w\0\0\0t\0Reparaciónt\0\rBonificaciónx',3,_binary ''),(6,'El producto entregado presentó fallas técnicas y no se respetó la garantía ofrecida.','FINALIZADO',_binary '�\�\0sr\0java.util.ArrayListx�\��\�a�\0I\0sizexp\0\0\0w\0\0\0t\0Producto defectuosot\0Problemas con la garantiax',_binary '�\�\0sr\0java.util.ArrayListx�\��\�a�\0I\0sizexp\0\0\0w\0\0\0t\0Reparaciónt\0\rBonificaciónx',5,_binary ''),(7,'Se cobró un precio distinto al publicado y no se entregó la documentación correspondiente.','EN ESPERA',_binary '�\�\0sr\0java.util.ArrayListx�\��\�a�\0I\0sizexp\0\0\0w\0\0\0t\0Diferencia de preciost\0Falta de documentaciónx',_binary '�\�\0sr\0java.util.ArrayListx�\��\�a�\0I\0sizexp\0\0\0w\0\0\0t\0\rBonificaciónt\0Cambio de productox',NULL,_binary ''),(8,'El servicio contratado presentó interrupciones constantes y se aplicaron cargos excesivos.','EN ESPERA',_binary '�\�\0sr\0java.util.ArrayListx�\��\�a�\0I\0sizexp\0\0\0w\0\0\0t\0Problemas con el serviciot\0Intereses abusivosx',_binary '�\�\0sr\0java.util.ArrayListx�\��\�a�\0I\0sizexp\0\0\0w\0\0\0t\0Anulación de Contratot\0\rBonificaciónx',NULL,_binary ''),(9,'El producto no coincidía con lo anunciado y se cobró un precio superior al publicado.','EN ESPERA',_binary '�\�\0sr\0java.util.ArrayListx�\��\�a�\0I\0sizexp\0\0\0w\0\0\0t\0Publicidad engañosat\0Diferencia de preciosx',_binary '�\�\0sr\0java.util.ArrayListx�\��\�a�\0I\0sizexp\0\0\0w\0\0\0t\0Devolución de dinerot\0Cambio de productox',NULL,_binary ''),(10,'No se entregó la documentación correspondiente y el producto no fue reparado bajo garantía.','EN ESPERA',_binary '�\�\0sr\0java.util.ArrayListx�\��\�a�\0I\0sizexp\0\0\0w\0\0\0t\0Falta de documentaciónt\0Problemas con la garantiax',_binary '�\�\0sr\0java.util.ArrayListx�\��\�a�\0I\0sizexp\0\0\0w\0\0\0t\0Reparaciónt\0\rBonificaciónx',NULL,_binary ''),(11,'Se emitieron facturas con errores y se aplicaron cargos adicionales sin justificación.','EN ESPERA',_binary '�\�\0sr\0java.util.ArrayListx�\��\�a�\0I\0sizexp\0\0\0w\0\0\0t\0Problemas con la facturaciont\0Otrosx',_binary '�\�\0sr\0java.util.ArrayListx�\��\�a�\0I\0sizexp\0\0\0w\0\0\0t\0Devolución de dinerot\0\rBonificaciónx',NULL,_binary ''),(12,'El servicio contratado no funcionó correctamente y la garantía no fue aplicada.','EN ESPERA',_binary '�\�\0sr\0java.util.ArrayListx�\��\�a�\0I\0sizexp\0\0\0w\0\0\0t\0Problemas con la garantiat\0Problemas con el serviciox',_binary '�\�\0sr\0java.util.ArrayListx�\��\�a�\0I\0sizexp\0\0\0w\0\0\0t\0Reparaciónt\0\rBonificaciónx',NULL,_binary ''),(13,'El precio cobrado fue distinto al publicado y no se entregó la documentación requerida.','EN ESPERA',_binary '�\�\0sr\0java.util.ArrayListx�\��\�a�\0I\0sizexp\0\0\0w\0\0\0t\0Diferencia de preciost\0Falta de documentaciónx',_binary '�\�\0sr\0java.util.ArrayListx�\��\�a�\0I\0sizexp\0\0\0w\0\0\0t\0\rBonificaciónt\0Cambio de productox',NULL,_binary '');
/*!40000 ALTER TABLE `denuncias` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `documentos`
--

DROP TABLE IF EXISTS `documentos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `documentos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `fecha_creacion` datetime(6) DEFAULT NULL,
  `formato` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `orden` bigint DEFAULT NULL,
  `referencia` varchar(255) DEFAULT NULL,
  `ruta` varchar(255) DEFAULT NULL,
  `tipo_documento` enum('DATOS_DENUNCIA','MEMO','NOTA','PROVIDENCIA','RESOLUCIÓN') DEFAULT NULL,
  `denuncia_id` bigint NOT NULL,
  `pase_id` bigint DEFAULT NULL,
  `nombrevisible` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK327c7r3yfluqn4wl4ksrsk74d` (`denuncia_id`),
  KEY `FKmm4hcw1sct96f7t4u5js30k59` (`pase_id`),
  CONSTRAINT `FK327c7r3yfluqn4wl4ksrsk74d` FOREIGN KEY (`denuncia_id`) REFERENCES `denuncias` (`id`),
  CONSTRAINT `FKmm4hcw1sct96f7t4u5js30k59` FOREIGN KEY (`pase_id`) REFERENCES `pases` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `documentos`
--

LOCK TABLES `documentos` WRITE;
/*!40000 ALTER TABLE `documentos` DISABLE KEYS */;
INSERT INTO `documentos` VALUES (1,'2025-08-20 13:16:59.857423','.pdf','f5e9b897-0beb-4c75-8747-ec3ee6ae6add.pdf',NULL,'Usuario Externo','src/main/resources/archivos/f5e9b897-0beb-4c75-8747-ec3ee6ae6add.pdf','DATOS_DENUNCIA',1,NULL,'doc_con_nombre_personalizado'),(2,'2025-08-20 13:18:10.812816','.pdf','641e3e08-8ac4-41af-b7da-f51e7032b039.pdf',NULL,'Usuario Externo','src/main/resources/archivos/641e3e08-8ac4-41af-b7da-f51e7032b039.pdf','DATOS_DENUNCIA',2,NULL,NULL),(3,'2025-09-03 12:16:54.332721','.pdf','cd6ea107-390f-4747-aab9-2b9ad9f1c93e.pdf',NULL,'Pase','src/main/resources/archivos/cd6ea107-390f-4747-aab9-2b9ad9f1c93e.pdf','MEMO',1,1,NULL),(4,'2025-11-05 11:48:32.323937','.pdf','ffc6de8c-3cd3-484d-8b15-b17924573f42.pdf',NULL,'Usuario Externo','src/main/resources/archivos/ffc6de8c-3cd3-484d-8b15-b17924573f42.pdf','DATOS_DENUNCIA',4,NULL,'DATOS_DENUNCIA_4_2025-11-05.pdf'),(5,'2025-11-13 11:50:29.977188','.pdf','0c392e82-4ef8-4525-ba77-8e6a0adb4097.pdf',NULL,'Pase','src/main/resources/archivos/0c392e82-4ef8-4525-ba77-8e6a0adb4097.pdf','PROVIDENCIA',1,2,'PROVIDENCIA_1_2025-11-13.pdf'),(6,'2025-11-13 11:51:20.672495','.pdf','e7ba7fca-2f1b-4a99-b165-881b2f4eb571.pdf',NULL,'EXP-2025-1','src/main/resources/archivos/e7ba7fca-2f1b-4a99-b165-881b2f4eb571.pdf','NOTA',1,NULL,'NOTA_1_2025-11-13.pdf'),(7,'2025-11-13 15:08:09.618289','.pdf','dee2441b-5ec2-46c1-be43-4acdbb8655b7.pdf',NULL,'Pase','src/main/resources/archivos/dee2441b-5ec2-46c1-be43-4acdbb8655b7.pdf','NOTA',4,3,'NOTA_4_2025-11-13.pdf'),(8,'2025-11-26 15:31:27.055162','.pdf','84a2882c-e86c-493d-8ab1-ceab3aa2d36b.pdf',NULL,'Usuario Externo','src/main/resources/archivos/84a2882c-e86c-493d-8ab1-ceab3aa2d36b.pdf','DATOS_DENUNCIA',5,NULL,'DATOS_DENUNCIA_5_2025-11-26.pdf'),(9,'2025-11-26 15:31:27.075018','.pdf','20d095b1-af29-48df-b524-41440358a324.pdf',NULL,'Usuario Externo','src/main/resources/archivos/20d095b1-af29-48df-b524-41440358a324.pdf','DATOS_DENUNCIA',5,NULL,'DATOS_DENUNCIA_5_2025-11-26.pdf'),(10,'2025-11-26 15:32:05.397545','.pdf','1d7e40bf-1732-4608-b949-df4fab4a0c81.pdf',NULL,'Usuario Externo','src/main/resources/archivos/1d7e40bf-1732-4608-b949-df4fab4a0c81.pdf','DATOS_DENUNCIA',6,NULL,'DATOS_DENUNCIA_6_2025-11-26.pdf'),(11,'2025-11-26 15:32:05.401364','.pdf','31c0e046-16f6-478c-af2c-298135b85613.pdf',NULL,'Usuario Externo','src/main/resources/archivos/31c0e046-16f6-478c-af2c-298135b85613.pdf','DATOS_DENUNCIA',6,NULL,'DATOS_DENUNCIA_6_2025-11-26.pdf'),(12,'2025-11-26 15:32:32.501889','.pdf','cf6cec2f-5ce8-4f55-8dd4-094512e23713.pdf',NULL,'Usuario Externo','src/main/resources/archivos/cf6cec2f-5ce8-4f55-8dd4-094512e23713.pdf','DATOS_DENUNCIA',7,NULL,'DATOS_DENUNCIA_7_2025-11-26.pdf'),(13,'2025-11-26 15:33:58.080148','.pdf','f5352d31-523b-4649-8044-99f484fe6d2f.pdf',NULL,'Usuario Externo','src/main/resources/archivos/f5352d31-523b-4649-8044-99f484fe6d2f.pdf','DATOS_DENUNCIA',8,NULL,'DATOS_DENUNCIA_8_2025-11-26.pdf'),(14,'2025-11-26 15:34:31.063867','.pdf','d540b414-2091-4b4e-9b5a-2d290af91497.pdf',NULL,'Usuario Externo','src/main/resources/archivos/d540b414-2091-4b4e-9b5a-2d290af91497.pdf','DATOS_DENUNCIA',9,NULL,'DATOS_DENUNCIA_9_2025-11-26.pdf'),(15,'2025-11-26 15:35:10.541241','.pdf','36ad0d35-d68b-42e4-82e2-0c591d37914c.pdf',NULL,'Usuario Externo','src/main/resources/archivos/36ad0d35-d68b-42e4-82e2-0c591d37914c.pdf','DATOS_DENUNCIA',10,NULL,'DATOS_DENUNCIA_10_2025-11-26.pdf'),(16,'2025-11-26 15:35:43.664679','.pdf','721d8e0d-8c76-49fd-a114-929604319f2f.pdf',NULL,'Usuario Externo','src/main/resources/archivos/721d8e0d-8c76-49fd-a114-929604319f2f.pdf','DATOS_DENUNCIA',11,NULL,'DATOS_DENUNCIA_11_2025-11-26.pdf'),(17,'2025-11-26 15:35:43.671741','.pdf','7ea0175f-5546-4dfe-a9e5-b31c62dfa434.pdf',NULL,'Usuario Externo','src/main/resources/archivos/7ea0175f-5546-4dfe-a9e5-b31c62dfa434.pdf','DATOS_DENUNCIA',11,NULL,'DATOS_DENUNCIA_11_2025-11-26.pdf'),(18,'2025-11-26 15:35:43.679511','.pdf','adeb1363-640b-427c-9852-5c28a7e974af.pdf',NULL,'Usuario Externo','src/main/resources/archivos/adeb1363-640b-427c-9852-5c28a7e974af.pdf','DATOS_DENUNCIA',11,NULL,'DATOS_DENUNCIA_11_2025-11-26.pdf'),(19,'2025-11-26 15:37:13.412031','.pdf','3e66a506-a7df-4052-ad08-959ae76171ac.pdf',NULL,'Usuario Externo','src/main/resources/archivos/3e66a506-a7df-4052-ad08-959ae76171ac.pdf','DATOS_DENUNCIA',12,NULL,'DATOS_DENUNCIA_12_2025-11-26.pdf'),(20,'2025-11-26 15:43:13.247858','.pdf','4ea9da22-ceb2-4fb9-b149-67aed7a891a3.pdf',NULL,'Pase','src/main/resources/archivos/4ea9da22-ceb2-4fb9-b149-67aed7a891a3.pdf','RESOLUCIÓN',5,4,'\"Cambio_nombre_ej.pdf\"'),(21,'2025-11-26 15:43:39.136108','.pdf','fe33bec8-8b0d-41f2-9d1d-764992fe0b65.pdf',NULL,'EXP-2025-DGC-2','src/main/resources/archivos/fe33bec8-8b0d-41f2-9d1d-764992fe0b65.pdf','NOTA',5,NULL,'NOTA_5_2025-11-26.pdf'),(22,'2025-11-26 15:44:53.114954','.pdf','e08f9bdd-b1a3-481f-8e4c-c03ef7dfa640.pdf',NULL,'EXP-2025-DGC-2','src/main/resources/archivos/e08f9bdd-b1a3-481f-8e4c-c03ef7dfa640.pdf','RESOLUCIÓN',5,NULL,'RESOLUCIÓN_5_2025-11-26.pdf'),(23,'2025-11-26 16:00:49.024061','.pdf','b4c47530-f77f-4c86-bf36-ca17a810573d.pdf',NULL,'Usuario Externo','src/main/resources/archivos/b4c47530-f77f-4c86-bf36-ca17a810573d.pdf','DATOS_DENUNCIA',13,NULL,'DATOS_DENUNCIA_13_2025-11-26.pdf');
/*!40000 ALTER TABLE `documentos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `expediente_usuario`
--

DROP TABLE IF EXISTS `expediente_usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `expediente_usuario` (
  `expediente_id` bigint NOT NULL,
  `usuario_id` bigint NOT NULL,
  KEY `FKmifaico3khlmsf5vipu4bk2nb` (`usuario_id`),
  KEY `FK2x62logbsasv1dm9cg2hxy80i` (`expediente_id`),
  CONSTRAINT `FK2x62logbsasv1dm9cg2hxy80i` FOREIGN KEY (`expediente_id`) REFERENCES `expedientes` (`id`),
  CONSTRAINT `FKmifaico3khlmsf5vipu4bk2nb` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `expediente_usuario`
--

LOCK TABLES `expediente_usuario` WRITE;
/*!40000 ALTER TABLE `expediente_usuario` DISABLE KEYS */;
INSERT INTO `expediente_usuario` VALUES (1,4),(2,4),(3,9);
/*!40000 ALTER TABLE `expediente_usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `expedientes`
--

DROP TABLE IF EXISTS `expedientes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `expedientes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cant_folios` varchar(255) DEFAULT NULL,
  `delegacion` varchar(255) DEFAULT NULL,
  `fecha_finalizacion` date DEFAULT NULL,
  `fecha_inicio` date DEFAULT NULL,
  `hipervulnerable` varchar(255) DEFAULT NULL,
  `nro_exp` varchar(255) DEFAULT NULL,
  `id_usuario` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKeh0uyr884fj4qi25an6i1v690` (`id_usuario`),
  CONSTRAINT `FKeh0uyr884fj4qi25an6i1v690` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `expedientes`
--

LOCK TABLES `expedientes` WRITE;
/*!40000 ALTER TABLE `expedientes` DISABLE KEYS */;
INSERT INTO `expedientes` VALUES (1,'0','DGC',NULL,'2025-08-20',NULL,'EXP-2025-1',NULL),(2,'0','DGC',NULL,'2025-11-05','no','EXP-2025-DGC-1',NULL),(3,'0','DGC',NULL,'2025-11-26','no','EXP-2025-DGC-2',NULL),(4,'0','DGC',NULL,'2025-11-26',NULL,'EXP-2025-DGC-3',NULL),(5,'0','DGC',NULL,'2025-11-26',NULL,'EXP-2025-DGC-4',NULL);
/*!40000 ALTER TABLE `expedientes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pases`
--

DROP TABLE IF EXISTS `pases`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pases` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `area_destino` enum('ABOGADOS','ASESORIA_LEGAL','DIRECCION','MESA_DE_ENTRADA') DEFAULT NULL,
  `area_origen` enum('ABOGADOS','ASESORIA_LEGAL','DIRECCION','MESA_DE_ENTRADA') DEFAULT NULL,
  `asunto` varchar(255) DEFAULT NULL,
  `cant_folios` bigint DEFAULT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `fecha_accion` date DEFAULT NULL,
  `expediente_id` bigint NOT NULL,
  `usuario_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKfhrpgphmesw6ek44ddstbr6nr` (`expediente_id`),
  KEY `FKq3i67xqvfirba4teantyxqo0k` (`usuario_id`),
  CONSTRAINT `FKfhrpgphmesw6ek44ddstbr6nr` FOREIGN KEY (`expediente_id`) REFERENCES `expedientes` (`id`),
  CONSTRAINT `FKq3i67xqvfirba4teantyxqo0k` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pases`
--

LOCK TABLES `pases` WRITE;
/*!40000 ALTER TABLE `pases` DISABLE KEYS */;
INSERT INTO `pases` VALUES (1,'MESA_DE_ENTRADA','MESA_DE_ENTRADA','Asunto pruebaaa',2,'PAse ejemploe','2025-09-03',1,1),(2,'MESA_DE_ENTRADA','DIRECCION','Firma',7,'Pase de ejemplo','2025-11-13',1,3),(3,'ABOGADOS','MESA_DE_ENTRADA','Firma',135,'texto','2025-11-13',2,3),(4,'DIRECCION','ABOGADOS','Resolución',7,'Descripción del pase','2025-11-26',3,3);
/*!40000 ALTER TABLE `pases` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `personas`
--

DROP TABLE IF EXISTS `personas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `personas` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `apellido` varchar(255) DEFAULT NULL,
  `codigo_postal` varchar(255) DEFAULT NULL,
  `documento` varchar(255) DEFAULT NULL,
  `domicilio` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `fax` varchar(255) DEFAULT NULL,
  `localidad` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `telefono` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `personas`
--

LOCK TABLES `personas` WRITE;
/*!40000 ALTER TABLE `personas` DISABLE KEYS */;
INSERT INTO `personas` VALUES (1,'López','4000','30123456','Calle Mendoza 789','carla.lopez@example.com','445566778','San Miguel de Tucumán','Carla','01199887766'),(2,'Castro','3400','40981234','Av. Italia 123','federico.castro@example.com','223344556','Formosa','Federico','03854455666'),(3,'Rivero','8300','38945678','Mitre 456','ignacio.rivero@example.com','111222333','Neuquén','Ignacio','02994562333'),(4,'Gómez','5000','28765432','Calle Colón 1500','ana.gomez@example.com','445566778','Córdoba','Ana','03511234567'),(5,'Díaz','5500','35678910','Av. San Martín 800','ricardo.diaz@example.com','223344556','Mendoza','Ricardo','02611122334'),(6,'Martínez','7600','37987654','Calle Corrientes 300','sofia.martinez@example.com','111222333','Mar del Plata','Sofía','02234567890'),(7,'Ruiz','4300','45888998','España 542','dir1@gmail.com',NULL,'Banda','Facundo','3853400999'),(8,'Hernandez','4200','22988244','Lavalle 9999','me@gmail.com',NULL,'La Banda','Adrián','2093009900'),(9,'Lopez','4300','44323111','España 542','direjemplo@gmail.com',NULL,'Banda','Mariano','222333444'),(10,'Lopez','4300','45454545','España 542','direjemplo1@gmail.com',NULL,'Banda','Mariano','222333444'),(11,'Gerez','3200','22443111','Garay 777',NULL,NULL,'La Banda','Hugo ','300999111'),(12,'Chavez','4200','99100220','Jujuy 1010',NULL,NULL,'La Banda','Julieta','110222000'),(13,'Gómez','7000','30111222','Calle Alsina 456','lucia.gomez@example.com',NULL,'Bahía Blanca','Lucía','01133445566'),(14,'Suárez','3300','40555666','Av. Libertad 789','martin.suarez@example.com',NULL,'Oberá','Martín','03762233445'),(15,'Herrera','9100','38999888','Calle Mitre 234','paula.herrera@example.com',NULL,'Trelew','Paula','02915566778'),(16,'Paz','4400','33344555','Calle Güemes 890','carolina.paz@example.com',NULL,'Salta','Carolina','01144556677'),(17,'Castro','3500','42333444','Av. Sarmiento 567','roberto.castro@example.com',NULL,'Resistencia','Roberto','03765566778'),(18,'Domínguez','9000','36667788','Calle Rivadavia 345','valeria.dominguez@example.com',NULL,'Comodoro Rivadavia','Valeria','02917788990'),(19,'Ramos','3400','33445566','Calle Catamarca 321','esteban.ramos@example.com',NULL,'Corrientes','Esteban','01155664433'),(20,'Sosa','3700','42334455','Av. 25 de Mayo 456','veronica.sosa@example.com',NULL,'Formosa','Verónica','03854445566'),(21,'Ortiz','9400','38889900','Calle Roca 789','gabriel.ortiz@example.com',NULL,'Río Gallegos','Gabriel','02994445577'),(22,'Quiroga','5000','34556677','Av. Sabattini 123','natalia.quiroga@example.com',NULL,'Córdoba','Natalia','01166778899'),(23,'Luna','3500','43334455','Calle Brown 567','federico.luna@example.com',NULL,'Resistencia','Federico','03765566778'),(24,'Salas','9000','37778899','Av. Rivadavia 345','monica.salas@example.com',NULL,'Comodoro Rivadavia','Mónica','02917788990'),(25,'Alonso','2000','35667788','Calle San Lorenzo 678','sergio.alonso@example.com',NULL,'Rosario','Sergio','01177889900'),(26,'Martínez','3600','44332211','Av. Mitre 234','patricia.martinez@example.com',NULL,'Posadas','Patricia','03856677889'),(27,'Fernández','9120','36665544','Calle Moreno 456','ricardo.fernandez@example.com',NULL,'Puerto Madryn','Ricardo','02918889900'),(28,'Cabrera','4400','36778899','Calle Balcarce 890','laura.cabrera@example.com',NULL,'Salta','Laura','01188990011'),(29,'Rey','3300','45332211','Av. San Martín 567','gustavo.rey@example.com',NULL,'Oberá','Gustavo','03768889900'),(30,'Villalba','7600','37889900','Av. Independencia 345','andres.villalba@example.com',NULL,'Mar del Plata','Andrés','01199001122'),(31,'Acosta','3700','46332211','Calle Moreno 678','silvia.acosta@example.com',NULL,'Formosa','Silvia','03859900112'),(32,'Benítez','9410','38889911','Calle San Martín 234','tomas.benitez@example.com',NULL,'Ushuaia','Tomás','02919900112'),(33,'Perez','4200','33212','Lacrose 12','jp@correo.com',NULL,'La Banda','Juan','300999231'),(34,'Reyes','4400','40112233','Calle Güemes 567','camila.reyes@example.com',NULL,'Salta','Camila','01122334455'),(35,'Ramírez','3500','48332211','Av. Sarmiento 234','jorge.ramirez@example.com',NULL,'Resistencia','Jorge','03852233445'),(36,'Martínez','9000','41122334','Calle Rivadavia 678','elena.martinez@example.com',NULL,'Comodoro Rivadavia','Elena','02912233445');
/*!40000 ALTER TABLE `personas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rol`
--

DROP TABLE IF EXISTS `rol`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rol` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rol`
--

LOCK TABLES `rol` WRITE;
/*!40000 ALTER TABLE `rol` DISABLE KEYS */;
INSERT INTO `rol` VALUES (1,'DIRECCION'),(2,'ABOGADOS'),(3,'ASESORIA_LEGAL'),(4,'MESA_DE_ENTRADA');
/*!40000 ALTER TABLE `rol` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `token`
--

DROP TABLE IF EXISTS `token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `token` (
  `id` bigint NOT NULL,
  `expired` bit(1) NOT NULL,
  `revoked` bit(1) NOT NULL,
  `token` varchar(255) DEFAULT NULL,
  `token_type` enum('BEARER') DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKpddrhgwxnms2aceeku9s2ewy5` (`token`),
  KEY `FK7tipr2wekkpu7e3xxqwmxv77k` (`user_id`),
  CONSTRAINT `FK7tipr2wekkpu7e3xxqwmxv77k` FOREIGN KEY (`user_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `token`
--

LOCK TABLES `token` WRITE;
/*!40000 ALTER TABLE `token` DISABLE KEYS */;
INSERT INTO `token` VALUES (1,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwibmFtZSI6ImRpcmVjdG9yYSIsInN1YiI6ImRpckBnbWFpbC5jb20iLCJyb2wiOiJESVJFQ0NJT04iLCJpYXQiOjE3NTU2OTU1MDIsImV4cCI6MTc1NTc4MTkwMn0.pJCALmjTNPUu9BF7xQyzDp84jE6qIXqaLkyTSoWUGI4','BEARER',1),(2,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIyIiwibmFtZSI6Im1lc2EiLCJzdWIiOiJtZXNhQGdtYWlsLmNvbSIsInJvbCI6Ik1FU0FfREVfRU5UUkFEQSIsImlhdCI6MTc1NTY5NTU0MiwiZXhwIjoxNzU1NzgxOTQyfQ.fPoNsXLIRcSWo8QQF1Kr1yKSjdbh37kL4yOhOHQ7-C4','BEARER',2),(3,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwibmFtZSI6ImRpcmVjdG9yYSIsInN1YiI6ImRpckBnbWFpbC5jb20iLCJyb2wiOiJESVJFQ0NJT04iLCJpYXQiOjE3NTU2OTYyODcsImV4cCI6MTc1NTc4MjY4N30.T5qXwhzthPOKeHxC0IRuYJB6KcPuk_4BDqdE0a_RpmM','BEARER',1),(52,_binary '\0',_binary '\0','eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwibmFtZSI6ImRpcmVjdG9yYSIsInN1YiI6ImRpckBnbWFpbC5jb20iLCJyb2wiOiJESVJFQ0NJT04iLCJpYXQiOjE3NTY5MDE2MTQsImV4cCI6MTc1Njk4ODAxNH0.BzfARdiCka7JBC703TUJljJS9ynklfKANJcV2-y3V94','BEARER',1),(102,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzIiwibmFtZSI6ImRpciIsInN1YiI6ImRpcjFAZ21haWwuY29tIiwicm9sIjoiRElSRUNDSU9OIiwiaWF0IjoxNzU5MzE4NTk2LCJleHAiOjE3NTk0MDQ5OTZ9.UCtNa8oXjoMSKZl-SRlafU6NTDvgIy0oJ1ZXrRIHKoA','BEARER',3),(152,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzIiwibmFtZSI6ImRpciIsInN1YiI6ImRpcjFAZ21haWwuY29tIiwicm9sIjoiRElSRUNDSU9OIiwiaWF0IjoxNzU5OTIzNzAyLCJleHAiOjE3NjAwMTAxMDJ9.RKBFhS0Mxrfb2jPF2KYY3Mx6MJMSD-k74zwjo6DNLpA','BEARER',3),(202,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzIiwibmFtZSI6ImRpciIsInN1YiI6ImRpcjFAZ21haWwuY29tIiwicm9sIjoiRElSRUNDSU9OIiwiaWF0IjoxNzU5OTI2MTc0LCJleHAiOjE3NjAwMTI1NzR9.QOc8r6luNAnCErjWznXkDhCi9IUm9R70RdspYDq_BfE','BEARER',3),(252,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzIiwibmFtZSI6ImRpciIsInN1YiI6ImRpcjFAZ21haWwuY29tIiwicm9sIjoiRElSRUNDSU9OIiwiaWF0IjoxNzU5OTI2NDg4LCJleHAiOjE3NTk5MjY1MTh9.2_EPg0_EdVl7lS8_8q77OTV0u0KFBk86xHA9xnpO-yc','BEARER',3),(253,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzIiwibmFtZSI6ImRpciIsInN1YiI6ImRpcjFAZ21haWwuY29tIiwicm9sIjoiRElSRUNDSU9OIiwiaWF0IjoxNzU5OTI2NTcyLCJleHAiOjE3NTk5MjY2MDJ9.NGQix98UAD_7ha_IizdESSZxVaO4sQS4HXEof5XmeVY','BEARER',3),(254,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzIiwibmFtZSI6ImRpciIsInN1YiI6ImRpcjFAZ21haWwuY29tIiwicm9sIjoiRElSRUNDSU9OIiwiaWF0IjoxNzU5OTI2NjM2LCJleHAiOjE3NTk5MjY2NjZ9.cr5QZXBxBVfTVUA0BU5vzH4pjqusTB5KksW-FBs1rvU','BEARER',3),(255,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzIiwibmFtZSI6ImRpciIsInN1YiI6ImRpcjFAZ21haWwuY29tIiwicm9sIjoiRElSRUNDSU9OIiwiaWF0IjoxNzU5OTI3MjEwLCJleHAiOjE3NTk5MjcyNDB9.rEOwm759psGSjIQ9bm4xG4zY3KTmIznfKWQL3Ac_zUc','BEARER',3),(256,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzIiwibmFtZSI6ImRpciIsInN1YiI6ImRpcjFAZ21haWwuY29tIiwicm9sIjoiRElSRUNDSU9OIiwiaWF0IjoxNzU5OTI3MjQxLCJleHAiOjE3NTk5MjcyNzF9.0sp51KjiRW2ksKcjRLjjCc28iuCpt17KI1AvQXwGt-M','BEARER',3),(257,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzIiwibmFtZSI6ImRpciIsInN1YiI6ImRpcjFAZ21haWwuY29tIiwicm9sIjoiRElSRUNDSU9OIiwiaWF0IjoxNzU5OTI3MjcyLCJleHAiOjE3NTk5MjczMDJ9.IpyxxrV87RMlHe2q7MbTyZ_HvR0Ld4bQhuyI3t2Uv2A','BEARER',3),(258,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzIiwibmFtZSI6ImRpciIsInN1YiI6ImRpcjFAZ21haWwuY29tIiwicm9sIjoiRElSRUNDSU9OIiwiaWF0IjoxNzU5OTI3MzAzLCJleHAiOjE3NTk5MjczMzN9.vosCX9jJuRRKIRmkx9CZibmgRRKp75FmUQ6yUI4WAsg','BEARER',3),(302,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIyIiwibmFtZSI6Im1lc2EiLCJzdWIiOiJtZXNhQGdtYWlsLmNvbSIsInJvbCI6Ik1FU0FfREVfRU5UUkFEQSIsImlhdCI6MTc1OTkyOTQyNiwiZXhwIjoxNzU5OTI5NDU2fQ.omDDxTOQur5S1WOC518hrJBPrLiTRIzIldW1gicA0lg','BEARER',2),(352,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzIiwibmFtZSI6ImRpciIsInN1YiI6ImRpcjFAZ21haWwuY29tIiwicm9sIjoiRElSRUNDSU9OIiwiaWF0IjoxNzYwNTI4OTAxLCJleHAiOjE3NjA1Mjg5MzF9._hx31PObKzl-6KlSPNili7f2NfO-tkg45AE9Tu5Kf8g','BEARER',3),(353,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzIiwibmFtZSI6ImRpciIsInN1YiI6ImRpcjFAZ21haWwuY29tIiwicm9sIjoiRElSRUNDSU9OIiwiaWF0IjoxNzYwNTI5MDUxLCJleHAiOjE3NjA1MjkwODF9.IvKIoHzmyJtaYHLpHpkZ9wAwrJFG_v3FyNf12jrdKhc','BEARER',3),(354,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzIiwibmFtZSI6ImRpciIsInN1YiI6ImRpcjFAZ21haWwuY29tIiwicm9sIjoiRElSRUNDSU9OIiwiaWF0IjoxNzYwNTI5MDgzLCJleHAiOjE3NjA1MjkxMTN9.6zYg567T6pzHqMn11lFgsxiP8qDUis2nahb6b2l4Ww8','BEARER',3),(355,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI0IiwibmFtZSI6Im1lc2l0YSIsInN1YiI6Im1lQGdtYWlsLmNvbSIsInJvbCI6IkFCT0dBRE9TIiwiaWF0IjoxNzYwNTI5MzI4LCJleHAiOjE3NjA1MjkzNTh9.r3dAOuUrZvU-INdWajZYREf8rFvZWbQuDl0F-MQcIgo','BEARER',4),(402,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzIiwibmFtZSI6ImRpciIsInN1YiI6ImRpcjFAZ21haWwuY29tIiwicm9sIjoiRElSRUNDSU9OIiwiaWF0IjoxNzYwNTI5NTQ4LCJleHAiOjE3NjA1Mjk1Nzh9.hdOH3aLCsq4BBVNfLdR0YbWeA7oKfi_6crbOWrsoZkQ','BEARER',3),(452,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzIiwibmFtZSI6ImRpciIsInN1YiI6ImRpcjFAZ21haWwuY29tIiwicm9sIjoiRElSRUNDSU9OIiwiaWF0IjoxNzYwNTI5NzQyLCJleHAiOjE3OTA1Mjk3NDJ9.PdgSz4Aixpso5PRBPg2Ir_rGAICGQuodnY8UwGBWIk0','BEARER',3),(453,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI0IiwibmFtZSI6Im1lc2l0YSIsInN1YiI6Im1lQGdtYWlsLmNvbSIsInJvbCI6IkFCT0dBRE9TIiwiaWF0IjoxNzYwNTI5ODUzLCJleHAiOjE3OTA1Mjk4NTN9.YFQSibFpyClwuJRCP_M5R0VuBRfzwzFKFRCXik9Jm5U','BEARER',4),(502,_binary '\0',_binary '\0','eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIyIiwibmFtZSI6Im1lc2EiLCJzdWIiOiJtZXNhQGdtYWlsLmNvbSIsInJvbCI6Ik1FU0FfREVfRU5UUkFEQSIsImlhdCI6MTc2MDUzMDE0OSwiZXhwIjoxNzkwNTMwMTQ5fQ.1-P935oog2o-DKO7w3dF_qxUih53QAs_c4J8xpp5CTc','BEARER',2),(503,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI0IiwibmFtZSI6Im1lc2l0YSIsInN1YiI6Im1lQGdtYWlsLmNvbSIsInJvbCI6IkFCT0dBRE9TIiwiaWF0IjoxNzYwNTMwMTU3LCJleHAiOjE3OTA1MzAxNTd9.SvnozqoByhgKbDjl5at0QYr8RryPovdUjyTUpf2Eg2g','BEARER',4),(504,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzIiwibmFtZSI6ImRpciIsInN1YiI6ImRpcjFAZ21haWwuY29tIiwicm9sIjoiRElSRUNDSU9OIiwiaWF0IjoxNzYwNTMwMTgzLCJleHAiOjE3OTA1MzAxODN9.UbR7gHaQxkdkNCd2YYxacJAfj5A7smULvAaGVDarkFM','BEARER',3),(505,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI0IiwibmFtZSI6Im1lc2l0YSIsInN1YiI6Im1lQGdtYWlsLmNvbSIsInJvbCI6IkFCT0dBRE9TIiwiaWF0IjoxNzYwNTMwNDc2LCJleHAiOjE3OTA1MzA0NzZ9.fqhOt4k3uTmKmPRJyqwm90tZiu3uTVg8kAXlf4XxQNQ','BEARER',4),(506,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzIiwibmFtZSI6ImRpciIsInN1YiI6ImRpcjFAZ21haWwuY29tIiwicm9sIjoiRElSRUNDSU9OIiwiaWF0IjoxNzYwNTMwNTMzLCJleHAiOjE3OTA1MzA1MzN9.DRq4xDwkp5Y38RJw4GbLYP66mDbnfz4NIQsCKG9gnsw','BEARER',3),(507,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzIiwibmFtZSI6ImRpciIsInN1YiI6ImRpcjFAZ21haWwuY29tIiwicm9sIjoiRElSRUNDSU9OIiwiaWF0IjoxNzYwNTMxMDU0LCJleHAiOjE3OTA1MzEwNTR9.GCCggWEnheWo9pFF_7Yn32PS4pNQ7lRRq753apvjwL8','BEARER',3),(508,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI0IiwibmFtZSI6Im1lc2l0YSIsInN1YiI6Im1lQGdtYWlsLmNvbSIsInJvbCI6IkFCT0dBRE9TIiwiaWF0IjoxNzYwNTMxMjc4LCJleHAiOjE3OTA1MzEyNzh9.JRDcgWbhYFR77zdCe5oEqea0vvhNTJ-gmi1TmFweFak','BEARER',4),(552,_binary '\0',_binary '\0','eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1IiwibmFtZSI6ImRpcnRlc3QiLCJzdWIiOiJkaXJlamVtcGxvQGdtYWlsLmNvbSIsInJvbCI6IkRJUkVDQ0lPTiIsImlhdCI6MTc2MDk2MjAwMywiZXhwIjoxNzYxMDQ4NDAzfQ.wvzzymBWew9XJ33qO8pzx4CY2PlSvuwPNGk3EW71DkA','BEARER',5),(602,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI4IiwibmFtZSI6ImRpcnRlc3QiLCJzdWIiOiJkaXJlamVtcGxvMUBnbWFpbC5jb20iLCJyb2wiOiJESVJFQ0NJT04iLCJpYXQiOjE3NjA5NjM5NDcsImV4cCI6MTc2MTA1MDM0N30.N1zcblVs6yKpYGbDcOLgRkX-9BLr333HHK2Bk5jHyA0','BEARER',8),(652,_binary '\0',_binary '\0','eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI4IiwibmFtZSI6ImRpcnRlc3QiLCJzdWIiOiJkaXJlamVtcGxvMUBnbWFpbC5jb20iLCJyb2wiOiJESVJFQ0NJT04iLCJpYXQiOjE3NjE1NzE5ODEsImV4cCI6MTc2MTY1ODM4MX0.5YoM_pwNBrlWqPT_saKX9gIafdgAHCZN-eYXvRGog_4','BEARER',8),(653,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzIiwibmFtZSI6ImRpciIsInN1YiI6ImRpcjFAZ21haWwuY29tIiwicm9sIjoiRElSRUNDSU9OIiwiaWF0IjoxNzYxNTcyMDc4LCJleHAiOjE3NjE2NTg0Nzh9._Abt4gU_fAlQr6QsyLRjWsU_gjzgVLieLaBwufFe2ew','BEARER',3),(702,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzIiwibmFtZSI6ImRpciIsInN1YiI6ImRpcjFAZ21haWwuY29tIiwicm9sIjoiRElSRUNDSU9OIiwiaWF0IjoxNzYyMzQxODk0LCJleHAiOjE3NjI0MjgyOTR9.pXslzVfj84rc41fKysNzxhSeYSwjcf0C8jaJTGFh-SI','BEARER',3),(703,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzIiwibmFtZSI6ImRpciIsInN1YiI6ImRpcjFAZ21haWwuY29tIiwicm9sIjoiRElSRUNDSU9OIiwiaWF0IjoxNzYyMzQyODYzLCJleHAiOjE3NjI0MjkyNjN9.6GZUkHE0NRbp8cMZKHjByhBM53oeBTQlbpHobzP6gPU','BEARER',3),(752,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzIiwibmFtZSI6ImRpciIsInN1YiI6ImRpcjFAZ21haWwuY29tIiwicm9sIjoiRElSRUNDSU9OIiwiaWF0IjoxNzYyMzQzMzMwLCJleHAiOjE3NjI0Mjk3MzB9.LwGT1IifekNqah_dauHQCXYdmaM9JtyNfzICC9Qn8Ks','BEARER',3),(753,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzIiwibmFtZSI6ImRpciIsInN1YiI6ImRpcjFAZ21haWwuY29tIiwicm9sIjoiRElSRUNDSU9OIiwiaWF0IjoxNzYyMzQ0NDA0LCJleHAiOjE3NjI0MzA4MDR9.qPHPY7MV3VsYgL_05vRO9lEVCj04QGdbVfBdeJ6_ZNQ','BEARER',3),(754,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzIiwibmFtZSI6ImRpciIsInN1YiI6ImRpcjFAZ21haWwuY29tIiwicm9sIjoiRElSRUNDSU9OIiwiaWF0IjoxNzYyMzQ0NjIzLCJleHAiOjE3NjI0MzEwMjN9.Cpku1IhrVn64dFCGJOEFrPFJvs9cYz_HTO7VknSL8As','BEARER',3),(802,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzIiwibmFtZSI6ImRpciIsInN1YiI6ImRpcjFAZ21haWwuY29tIiwicm9sIjoiRElSRUNDSU9OIiwiaWF0IjoxNzYzMDAxMjExLCJleHAiOjE3NjMwODc2MTF9.Upw1cZ9OdSs5OSHv30_xNaRyQ4Q0e7vfN5NsXhzJQyk','BEARER',3),(852,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzIiwibmFtZSI6ImRpciIsInN1YiI6ImRpcjFAZ21haWwuY29tIiwicm9sIjoiRElSRUNDSU9OIiwiaWF0IjoxNzYzMDM0NTc2LCJleHAiOjE3NjMxMjA5NzZ9.KiowEpatjLORbb5N3j2nqrfUanViz_p5aU055vmiNjM','BEARER',3),(853,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzIiwibmFtZSI6ImRpciIsInN1YiI6ImRpcjFAZ21haWwuY29tIiwicm9sIjoiRElSRUNDSU9OIiwiaWF0IjoxNzYzMDM0NzgyLCJleHAiOjE3NjMxMjExODJ9.3cNrnE1lFBOVu1JAW9BBSYajo6Ca6bnCp8AHsSGV4Wc','BEARER',3),(854,_binary '\0',_binary '\0','eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI0IiwibmFtZSI6Im1lc2l0YSIsInN1YiI6Im1lQGdtYWlsLmNvbSIsInJvbCI6IkFCT0dBRE9TIiwiaWF0IjoxNzYzMDM0ODIyLCJleHAiOjE3NjMxMjEyMjJ9.-A0H6i0hu6wkUjzw0bcufDyfki4S66ac7MvSL6iTc3c','BEARER',4),(902,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzIiwibmFtZSI6ImRpciIsInN1YiI6ImRpcjFAZ21haWwuY29tIiwicm9sIjoiRElSRUNDSU9OIiwiaWF0IjoxNzYzMDM4NjA0LCJleHAiOjE3NjMxMjUwMDR9.dn2Mcmjwd6z1YkhnYVkZuyys31LOVKWPT2kZPprMu_g','BEARER',3),(903,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzIiwibmFtZSI6ImRpciIsInN1YiI6ImRpcjFAZ21haWwuY29tIiwicm9sIjoiRElSRUNDSU9OIiwiaWF0IjoxNzYzMDQyNzY2LCJleHAiOjE3NjMxMjkxNjZ9.fsYLX4DoEIgTpMQdl1pl_zwHJrrsRzxew2Ksg-LSY84','BEARER',3),(904,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzIiwibmFtZSI6ImRpciIsInN1YiI6ImRpcjFAZ21haWwuY29tIiwicm9sIjoiRElSRUNDSU9OIiwiaWF0IjoxNzYzMDQ2MTQwLCJleHAiOjE3NjMxMzI1NDB9.veftDn2-jMWh4atlSI8QKIGLvADRt-i-fsIzx1AZIpU','BEARER',3),(905,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzIiwibmFtZSI6ImRpciIsInN1YiI6ImRpcjFAZ21haWwuY29tIiwicm9sIjoiRElSRUNDSU9OIiwiaWF0IjoxNzYzMDQ2MjgwLCJleHAiOjE3NjMxMzI2ODB9.yFudvG7HmY45ZKtJ8__YHhH1ZoVCd-V7_VxKi9cUaps','BEARER',3),(952,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzIiwibmFtZSI6ImRpciIsInN1YiI6ImRpcjFAZ21haWwuY29tIiwicm9sIjoiRElSRUNDSU9OIiwiaWF0IjoxNzY0MTcwMTU0LCJleHAiOjE3NjQyNTY1NTR9.WDQelKfgf88q7L26bdePwqi6dz_M3jBrlvPuNn_6rMI','BEARER',3),(1002,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzIiwibmFtZSI6ImRpciIsInN1YiI6ImRpcjFAZ21haWwuY29tIiwicm9sIjoiRElSRUNDSU9OIiwiaWF0IjoxNzY0MTcxNDkwLCJleHAiOjE3NjQyNTc4OTB9.9OmxnBlteEW3jYxbsuI7KgpPtK01adcN_QdPFRKt-o4','BEARER',3),(1003,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI5IiwibmFtZSI6Ikp1YW4gUGVyZXoiLCJzdWIiOiJqcEBjb3JyZW8uY29tIiwicm9sIjoiQUJPR0FET1MiLCJpYXQiOjE3NjQxNzE5OTUsImV4cCI6MTc2NDI1ODM5NX0.EMyJYnkiM49SHdAA97eyK-cw9VY_J5HoJX2xmjR3VHw','BEARER',9),(1004,_binary '\0',_binary '\0','eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI5IiwibmFtZSI6Ikp1YW4gUGVyZXoiLCJzdWIiOiJqcEBjb3JyZW8uY29tIiwicm9sIjoiQUJPR0FET1MiLCJpYXQiOjE3NjQxNzIwMjksImV4cCI6MTc2NDI1ODQyOX0.mhEZxojL9PgRR4f7LiOSTE6AVQWRkdJ8olSzgp4eWkQ','BEARER',9),(1005,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzIiwibmFtZSI6ImRpciIsInN1YiI6ImRpcjFAZ21haWwuY29tIiwicm9sIjoiRElSRUNDSU9OIiwiaWF0IjoxNzY0MTcyMDUxLCJleHAiOjE3NjQyNTg0NTF9.d5to4P2-CEsJzPVudTmw3oGf7ZniLFEF0bSmtZ_A64w','BEARER',3),(1006,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzIiwibmFtZSI6IkZhY3VuZG8gUnVpeiIsInN1YiI6ImRpcjFAZ21haWwuY29tIiwicm9sIjoiRElSRUNDSU9OIiwiaWF0IjoxNzY0MTcyMTE4LCJleHAiOjE3NjQyNTg1MTh9.4PMe1rqYacLvcu7dslx_aG9JoJwlMCzx1R-SIP_iCkU','BEARER',3),(1007,_binary '',_binary '','eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzIiwibmFtZSI6IkZhY3VuZG8gUnVpeiIsInN1YiI6ImRpcjFAZ21haWwuY29tIiwicm9sIjoiRElSRUNDSU9OIiwiaWF0IjoxNzY0MTgyMTcxLCJleHAiOjE3NjQyNjg1NzF9.oAkTeYy5l_Omv8PYslleJFSd5Mck-REMyLAxceL0OjQ','BEARER',3),(1008,_binary '\0',_binary '\0','eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzIiwibmFtZSI6IkZhY3VuZG8gUnVpeiIsInN1YiI6ImRpcjFAZ21haWwuY29tIiwicm9sIjoiRElSRUNDSU9OIiwiaWF0IjoxNzY0MTgyMjQ2LCJleHAiOjE3NjQyNjg2NDZ9.v5DGKPYvkYvlmj_mMpAIltuiTJY-DW2d50a9KAA4ZpY','BEARER',3);
/*!40000 ALTER TABLE `token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `token_seq`
--

DROP TABLE IF EXISTS `token_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `token_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `token_seq`
--

LOCK TABLES `token_seq` WRITE;
/*!40000 ALTER TABLE `token_seq` DISABLE KEYS */;
INSERT INTO `token_seq` VALUES (1101);
/*!40000 ALTER TABLE `token_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `contraseña` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `rol` enum('ABOGADOS','ASESORIA_LEGAL','DIRECCION','MESA_DE_ENTRADA') DEFAULT NULL,
  `id_persona` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKkfsp0s1tflm1cwlj8idhqsad0` (`email`),
  UNIQUE KEY `UK27s1llon5naewhedd1qfhhvce` (`id_persona`),
  CONSTRAINT `FKtmank41bd4off23q2o9dx13y9` FOREIGN KEY (`id_persona`) REFERENCES `personas` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'$2a$10$fgaXTRckykuxICbMLagIY.9.msSN09RF0z0XBWEWk10TDl96pZvia','dir@gmail.com','directora','DIRECCION',NULL),(2,'$2a$10$0f6WhIe4fn8/0NtuEE4idu4fBl52B.F42c/w9Y7DavX9tGEDl0Zii','mesa@gmail.com','mesa','MESA_DE_ENTRADA',NULL),(3,'$2a$10$eyOQIbhvhzzaIriTCZcmIuv9f4J8O873SYNZWq98Vqa4HaN/5ADb6','dir1@gmail.com','Facundo Ruiz','DIRECCION',7),(4,'$2a$10$34S9ior5W4s8/OswF.nXQOltjqOoOOLz7XXOW9p6aU9T1ksMcjG66','me@gmail.com','mesita','ABOGADOS',8),(5,'$2a$10$CxJroQIqjYrOH29Wod4j8O0rk7I2ubJs70cDvgR.cQTdlxqPrQx0.','direjemplo@gmail.com','dirtest','DIRECCION',9),(8,'$2a$10$d2Dq8BbCDcnn1vMdyA1Ru.IhZb3twG6MsH2QIpx.Nw9eSjS5kU3hi','direjemplo1@gmail.com','dirtest','DIRECCION',10),(9,'$2a$10$HSk5aRMicy7znTjI1.tVf.kxVWn4GAO7BaBBrLuOMbHI52/Czl8H2','jp@correo.com','Juan Perez','ABOGADOS',33);
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-01-06 22:25:16
