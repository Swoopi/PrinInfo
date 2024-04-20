-- Ensure the database exists and switch to it
CREATE DATABASE IF NOT EXISTS `FinalProject` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `FinalProject`;

-- Create the users table if it does not exist
CREATE TABLE IF NOT EXISTS `users` (
    `userid` INT AUTO_INCREMENT PRIMARY KEY,
    `username` VARCHAR(256) NOT NULL UNIQUE,
    `password` VARCHAR(256) NOT NULL
);

-- Insert a sample user
INSERT INTO `users` (`username`, `password`) VALUES ('testuser', 'password123');

-- Create the items table if it does not exist
CREATE TABLE IF NOT EXISTS `items` (
    `item_id` INT AUTO_INCREMENT PRIMARY KEY,
    `title` VARCHAR(255) NOT NULL,
    `description` TEXT,
    `minimum_price` DECIMAL(10,2) NOT NULL,
    `starting_price` DECIMAL(10,2) NOT NULL,
    `current_bid` DECIMAL(10,2),
    `current_bid_user_id` INT,
    `seller_id` INT NOT NULL,
    `starting_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `closing_time` DATETIME NOT NULL,
    `bid_increment` DECIMAL(10,2) NOT NULL,
    `status` ENUM('active', 'sold') NOT NULL DEFAULT 'active',
    FOREIGN KEY (`seller_id`) REFERENCES `users`(`userid`),
    FOREIGN KEY (`current_bid_user_id`) REFERENCES `users`(`userid`)
);

-- Display items to confirm their creation
SELECT * FROM `items`;
