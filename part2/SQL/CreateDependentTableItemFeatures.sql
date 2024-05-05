USE FinalProject;
CREATE TABLE ItemFeatures(
	itemID int,
    feature_desc varchar(256),
    feature_value varchar(256),
    item_name varchar(256),
    brand varchar (256),
    year_manufactured year,
    PRIMARY KEY (itemID, feature_desc),
    FOREIGN KEY (itemID) REFERENCES Items (itemID) ON DELETE CASCADE);
    
    