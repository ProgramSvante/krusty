Drop Table if exists Rawmaterials, Cookies, Ingredients;
Create table Rawmaterials(
RawMaterialName varchar(30) Primary Key,
Quantity INT,
Date_delivered date
);

Create table Cookies(
BatchID INT,
CookieName varchar(30),
Production_date date,
Production_time timestamp,
Primary key (BatchID,CookieName)
);

Create table Ingredients(
QuantityInRecipie INT,
MaterialName varchar(30),
CookieName varchar(30),
Foreign Key (MaterialName) References Rawmaterials (RawMaterialName),
Foreign Key (CookieName) References Cookies (CookieName)
);