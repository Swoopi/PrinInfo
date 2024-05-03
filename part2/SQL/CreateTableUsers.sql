CREATE DATABASE IF NOT EXISTS `FinalProject` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `FinalProject`;

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`(
`userid` INT AUTO_INCREMENT PRIMARY KEY,
`username` VARCHAR(256) NOT NULL UNIQUE,
`password` VARCHAR(256) NOT NULL
);
INSERT INTO users (username, password) VALUES ('testuser', 'password123');users