USE FinalProject;
CREATE TABLE  `Bids`(
	`bidID` INT NOT NULL auto_increment,
    `auctionID` INT NOT NULL,
    `userID` INT,
	`bid_amount` DOUBLE,
    `bid_type` VARCHAR(256), 
    `post_time` DATETIME,
    PRIMARY KEY (`bidID`),
    FOREIGN KEY (`auctionID`) references `Auctions`(auctionID),
    FOREIGN KEY (`userID`) references `users`(userID)
	);
    