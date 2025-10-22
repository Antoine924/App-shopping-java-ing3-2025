-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le : sam. 26 avr. 2025 à 17:38
-- Version du serveur : 9.1.0
-- Version de PHP : 8.3.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `shopping_db`
--

-- --------------------------------------------------------

--
-- Structure de la table `article`
--

DROP TABLE IF EXISTS `article`;
CREATE TABLE IF NOT EXISTS `article` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(100) DEFAULT NULL,
  `marque` varchar(100) DEFAULT NULL,
  `prix_unitaire` decimal(5,2) DEFAULT NULL,
  `prix_gros` decimal(5,2) DEFAULT NULL,
  `seuil_gros` int DEFAULT NULL,
  `image_path` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `article`
--

INSERT INTO `article` (`id`, `nom`, `marque`, `prix_unitaire`, `prix_gros`, `seuil_gros`, `image_path`) VALUES
(1, 'Sweat noir', 'Zara', 29.99, 25.00, 5, 'Images/pullnoir.jpg'),
(2, 'Casquette ', 'Lacoste', 14.50, 12.00, 10, 'Images/casquette.jpg'),
(3, 'maillot du psg', 'nike', 99.99, 60.00, 24, 'Images/maillotdupsg.jpg'),
(5, 'maillot portugal', 'nike', 89.00, 70.00, 10, 'Images/maillotprt.jpg\"');

-- --------------------------------------------------------

--
-- Structure de la table `client`
--

DROP TABLE IF EXISTS `client`;
CREATE TABLE IF NOT EXISTS `client` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `mot_de_passe` varchar(255) DEFAULT NULL,
  `type_client` enum('standard','admin') NOT NULL DEFAULT 'standard',
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `client`
--

INSERT INTO `client` (`id`, `nom`, `email`, `mot_de_passe`, `type_client`) VALUES
(1, 'Admin', 'admin@gmail.com', '123', 'admin'),
(2, 'Hugo', 'hugo@mail.com', '123', 'standard'),
(4, 'cantine', 'cantine@gmail.com', '123', 'standard');

-- --------------------------------------------------------

--
-- Structure de la table `commande`
--

DROP TABLE IF EXISTS `commande`;
CREATE TABLE IF NOT EXISTS `commande` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_client` int DEFAULT NULL,
  `date_commande` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `id_client` (`id_client`)
) ENGINE=MyISAM AUTO_INCREMENT=57 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `commande`
--

INSERT INTO `commande` (`id`, `id_client`, `date_commande`) VALUES
(1, 1, '2025-04-02 07:50:43'),
(2, 1, '2025-04-02 08:56:29'),
(3, 1, '2025-04-02 09:37:43'),
(51, 2, '2025-04-24 17:43:06'),
(50, 2, '2025-04-24 17:42:38'),
(53, 2, '2025-04-25 16:36:16');

-- --------------------------------------------------------

--
-- Structure de la table `ligne_commande`
--

DROP TABLE IF EXISTS `ligne_commande`;
CREATE TABLE IF NOT EXISTS `ligne_commande` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_commande` int DEFAULT NULL,
  `id_article` int DEFAULT NULL,
  `quantite` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `id_commande` (`id_commande`),
  KEY `id_article` (`id_article`)
) ENGINE=MyISAM AUTO_INCREMENT=71 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `ligne_commande`
--

INSERT INTO `ligne_commande` (`id`, `id_commande`, `id_article`, `quantite`) VALUES
(1, 1, 1, 1),
(2, 2, 2, 1),
(3, 2, 3, 1),
(4, 3, 3, 1),
(62, 51, 1, 2),
(61, 50, 1, 1),
(60, 50, 3, 1),
(59, 50, 2, 1),
(64, 53, 5, 1);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
