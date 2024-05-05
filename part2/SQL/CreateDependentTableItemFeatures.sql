USE FinalProject;
CREATE TABLE ItemFeatures(
    item_name varchar(256),
    feature_desc varchar(256),
    feature_value varchar(256),
    PRIMARY KEY (item_name, feature_desc));
    
    