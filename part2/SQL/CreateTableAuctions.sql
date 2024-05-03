
CREATE TABLE `FinalProject`.`Auctions`(
	`auctionID` INT NOT NULL auto_increment,
    `sellerID` INT NOT NULL,
    `title` VARCHAR(256),
    `description` VARCHAR(256), 
    `starting_price` DOUBLE,
    `bid_increment` DOUBLE,
    `minimum_price` DOUBLE,
    `starting_time` DATETIME,
    `closing_time` DATETIME,
    `auction_status` VARCHAR(20),
    PRIMARY KEY (`auctionID`),
    FOREIGN KEY (`sellerID`) references `users`(userID) 
	);
    
#CREATE TABLE `FinalProject`.`auct` (
#  `aucID` INT NOT NULL,
# PRIMARY KEY (`aucID`));