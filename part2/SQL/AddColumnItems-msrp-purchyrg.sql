USE FinalProject;
ALTER TABLE Items
ADD (category varchar(256),
collection varchar(256),
msrp double,
purchase_price double,
purchase_year YEAR); 