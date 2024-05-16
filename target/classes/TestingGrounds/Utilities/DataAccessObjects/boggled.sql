-- MySQL dump 10.13  Distrib 8.3.0, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: boggled
-- ------------------------------------------------------
-- Server version	8.3.0

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
-- Table structure for table `admins`
--

DROP TABLE IF EXISTS `admins`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admins` (
  `adminid` int NOT NULL,
  `username` varchar(45) DEFAULT NULL,
  `password` varchar(45) DEFAULT NULL,
  `sessionToken` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`adminid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admins`
--

LOCK TABLES `admins` WRITE;
/*!40000 ALTER TABLE `admins` DISABLE KEYS */;
INSERT INTO `admins` VALUES (1,'admin','admin123','7da8d7dd-1175-4112-b937-1949f59fe758');
/*!40000 ALTER TABLE `admins` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `game_sessions`
--

DROP TABLE IF EXISTS `game_sessions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `game_sessions` (
  `game_token` varchar(255) NOT NULL,
  `max_players` int DEFAULT '4',
  `winning_rounds` int DEFAULT NULL,
  `lobby_waiting_time` int DEFAULT NULL,
  `duration_per_round` int DEFAULT NULL,
  `delay_per_round` int DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `player_count` int DEFAULT NULL,
  PRIMARY KEY (`game_token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `game_sessions`
--

LOCK TABLES `game_sessions` WRITE;
/*!40000 ALTER TABLE `game_sessions` DISABLE KEYS */;
INSERT INTO `game_sessions` VALUES ('09eb977c-f24e-4d7f-bb2f-8d22844f5589',4,3,15,30,5,'WAITING',1),('0OQ-1MIG',4,3,10,30,5,'ACTIVE',2),('1AH-DAAR',4,3,15,30,5,'WAITING',1),('2J7-XGLP',4,3,10,30,5,'ACTIVE',2),('3dbf90f4-fe42-48fc-8cdb-e6e48548180e',4,3,15,30,5,'WAITING',1),('44fcfbb6-2887-47e0-9adf-d67dac80cdc4',4,3,15,30,5,'WAITING',1),('4B8-QTXO',4,3,10,30,5,'ACTIVE',2),('4G9-EF96',4,3,10,30,5,'ACTIVE',2),('4ZE-I0PI',4,3,10,30,5,'ACTIVE',2),('739-VO54',4,3,10,30,5,'ACTIVE',2),('7MP-G9QO',4,3,10,30,5,'ACTIVE',2),('7XR-SP32',4,3,10,30,5,'ACTIVE',2),('8A0-YRDS',4,3,15,30,5,'WAITING',1),('9IV-QXFV',4,3,10,30,5,'ACTIVE',2),('B5R-RFML',4,3,10,30,5,'ACTIVE',2),('E3I-BT8O',4,3,10,30,5,'ACTIVE',2),('EDA-5QHM',4,3,10,30,5,'ACTIVE',2),('FY2-TDCV',4,3,10,30,5,'ACTIVE',2),('IK4-XT1D',4,3,10,30,5,'ACTIVE',2),('KH7-KX1J',4,3,10,30,5,'ACTIVE',2),('LBO-F9Z7',4,3,10,30,5,'ACTIVE',2),('LHZ-SA5S',4,3,10,30,5,'ACTIVE',2),('LQC-ELOZ',4,3,10,30,5,'ACTIVE',2),('ME1-FF07',4,3,15,30,5,'WAITING',1),('O00-EJL8',4,3,10,30,5,'ACTIVE',2),('O4J-4E7B',4,3,10,30,5,'ACTIVE',2),('QPF-NSFB',4,3,10,30,5,'ACTIVE',2),('SLQ-9TSX',4,3,15,30,5,'WAITING',1),('TN2-3NOP',4,3,10,30,5,'ACTIVE',2),('TP2-6C5K',4,3,15,30,5,'WAITING',2),('WQN-9AYF',4,3,15,30,5,'WAITING',1),('X9Y-A8M8',4,3,10,30,5,'ACTIVE',2),('YS2-KZCJ',4,3,15,30,5,'WAITING',1),('ZMF-28OF',4,3,10,30,5,'ACTIVE',2);
/*!40000 ALTER TABLE `game_sessions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `game_settings`
--

DROP TABLE IF EXISTS `game_settings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `game_settings` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `rounds_to_win` int NOT NULL,
  `seconds_per_round` int NOT NULL,
  `seconds_per_waiting` int DEFAULT '5',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `game_settings`
--

LOCK TABLES `game_settings` WRITE;
/*!40000 ALTER TABLE `game_settings` DISABLE KEYS */;
INSERT INTO `game_settings` VALUES (1,4,30,14);
/*!40000 ALTER TABLE `game_settings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `playerId` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `username` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `sessionToken` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `inGame` tinyint(1) DEFAULT NULL,
  `overall_rounds_won` int DEFAULT NULL,
  `currentGameToken` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`playerId`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('0','dolbus','123',NULL,NULL,10,NULL),('1','fresk','password',NULL,NULL,15,NULL),('2','ju','admin',NULL,NULL,30,NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-05-16 18:58:47
