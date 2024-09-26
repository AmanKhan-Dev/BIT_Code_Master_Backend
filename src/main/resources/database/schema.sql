/*
SQLyog Community v13.1.9 (64 bit)
MySQL - 8.0.38 : Database - quiz_website_database
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`quiz_website_database` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `quiz_website_database`;

/*Table structure for table `admin_details` */

DROP TABLE IF EXISTS `admin_details`;

CREATE TABLE `admin_details` (
  `admin_email` varchar(255) NOT NULL,
  `admin_full_name` varchar(255) NOT NULL,
  `admin_id` bigint DEFAULT NULL,
  `admin_password` varchar(255) NOT NULL,
  PRIMARY KEY (`admin_email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `coding_questions` */

DROP TABLE IF EXISTS `coding_questions`;

CREATE TABLE `coding_questions` (
  `question_set_id` varchar(255) NOT NULL,
  `Question_no` int NOT NULL,
  `question` text NOT NULL,
  `question_category` varchar(255) DEFAULT NULL,
  `question_description` text,
  `test_case_input` text,
  `test_case_output` text,
  PRIMARY KEY (`question_set_id`,`Question_no`),
  UNIQUE KEY `id_and_no` (`question_set_id`,`Question_no`),
  UNIQUE KEY `UKq3s8l9qewjpn1skv6chrnsgyi` (`question_set_id`,`Question_no`),
  KEY `idx_Question_no` (`Question_no`),
  KEY `available_category` (`question_category`),
  CONSTRAINT `available_category` FOREIGN KEY (`question_category`) REFERENCES `question_set_categories` (`available_category`),
  CONSTRAINT `coding_questions_ibfk_1` FOREIGN KEY (`question_set_id`) REFERENCES `question_sets` (`question_set_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `question_set_categories` */

DROP TABLE IF EXISTS `question_set_categories`;

CREATE TABLE `question_set_categories` (
  `question_set_id` varchar(255) DEFAULT NULL,
  `available_category` varchar(255) DEFAULT NULL,
  UNIQUE KEY `available_category` (`available_category`),
  KEY `question_set_id` (`question_set_id`),
  CONSTRAINT `question_set_categories_ibfk_1` FOREIGN KEY (`question_set_id`) REFERENCES `question_sets` (`question_set_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `question_sets` */

DROP TABLE IF EXISTS `question_sets`;

CREATE TABLE `question_sets` (
  `admin_email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `question_set_id` varchar(255) NOT NULL,
  `set_password` varchar(255) NOT NULL,
  `question_set_name` varchar(255) NOT NULL,
  PRIMARY KEY (`question_set_id`),
  KEY `admin_email` (`admin_email`),
  CONSTRAINT `question_sets_ibfk_1` FOREIGN KEY (`admin_email`) REFERENCES `admin_details` (`admin_email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `student_details` */

DROP TABLE IF EXISTS `student_details`;

CREATE TABLE `student_details` (
  `full_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `email` varchar(255) NOT NULL,
  `prn` varchar(255) NOT NULL,
  `roll_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`email`,`prn`),
  UNIQUE KEY `PRN` (`prn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `student_results` */

DROP TABLE IF EXISTS `student_results`;

CREATE TABLE `student_results` (
  `question_set_id` varchar(255) DEFAULT NULL,
  `Question_no` int DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `student_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `prn` varchar(255) DEFAULT NULL,
  `student_roll_no` varchar(255) DEFAULT NULL,
  `solved_counter` int DEFAULT NULL,
  UNIQUE KEY `unique_student_question` (`email`,`question_set_id`,`Question_no`),
  KEY `question_set_id` (`question_set_id`),
  KEY `Question_no` (`Question_no`),
  KEY `prn` (`prn`),
  CONSTRAINT `student_results_ibfk_1` FOREIGN KEY (`question_set_id`) REFERENCES `coding_questions` (`question_set_id`),
  CONSTRAINT `student_results_ibfk_2` FOREIGN KEY (`Question_no`) REFERENCES `coding_questions` (`Question_no`),
  CONSTRAINT `student_results_ibfk_3` FOREIGN KEY (`email`) REFERENCES `student_details` (`email`),
  CONSTRAINT `student_results_ibfk_4` FOREIGN KEY (`prn`) REFERENCES `student_details` (`prn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `test_cases` */

DROP TABLE IF EXISTS `test_cases`;

CREATE TABLE `test_cases` (
  `question_set_id` varchar(255) NOT NULL,
  `Question_no` int NOT NULL,
  `tc_id` int NOT NULL AUTO_INCREMENT,
  `test_case_input` varchar(255) DEFAULT NULL,
  `test_case_output` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`question_set_id`,`Question_no`,`tc_id`),
  KEY `fk_Question_no` (`Question_no`),
  KEY `tc_id` (`tc_id`),
  CONSTRAINT `fk_Question_no` FOREIGN KEY (`Question_no`) REFERENCES `coding_questions` (`Question_no`),
  CONSTRAINT `fk_question_set_id` FOREIGN KEY (`question_set_id`) REFERENCES `coding_questions` (`question_set_id`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
