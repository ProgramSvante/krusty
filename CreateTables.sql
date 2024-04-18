SET FOREIGN_KEY_CHECKS = 0;

-- Drop tables if they exist
DROP TABLE IF EXISTS OrderCookies;
DROP TABLE IF EXISTS PalletOrders;
DROP TABLE IF EXISTS CookiesPallet;
DROP TABLE IF EXISTS Ingredients;
DROP TABLE IF EXISTS RawMaterials;
DROP TABLE IF EXISTS Cookies;
DROP TABLE IF EXISTS Delivered;
DROP TABLE IF EXISTS Pallet;
DROP TABLE IF EXISTS Orders;
DROP TABLE IF EXISTS Customer;
SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE Customer (
    Name VARCHAR(255) PRIMARY KEY,
    Address VARCHAR(255) 
);


CREATE TABLE Orders (
    Order_ID INT PRIMARY KEY,
    Customer_Name VARCHAR(255),
    WishedDateOfDelivery DATE,
    FOREIGN KEY (Customer_Name) REFERENCES Customer(Name)
);


CREATE TABLE Pallet (
    Pallet_ID INT PRIMARY KEY,
    Order_ID INT,
    DateTime DATETIME,
    Location VARCHAR(255),
    IsBlocked BIT,
    FOREIGN KEY (Order_ID) REFERENCES Orders(Order_ID)
);


CREATE TABLE Delivered (
    Order_ID INT PRIMARY KEY,
    DateOfDelivery DATE,
    FOREIGN KEY (Order_ID) REFERENCES Orders(Order_ID)
);


CREATE TABLE Cookies (
    BatchID INT,
    CookieName VARCHAR(255),
    ProductionDate DATE,
    ProductionTime TIME,
    PRIMARY KEY (BatchID, CookieName)
);


CREATE TABLE RawMaterials (
    Name VARCHAR(255) PRIMARY KEY,
    Quantity INT,
    DateDelivered DATE
);


CREATE TABLE Ingredients (
    MaterialName VARCHAR(255),
    QuantityInRecipe INT,
    PRIMARY KEY (MaterialName),
    
    FOREIGN KEY (MaterialName) REFERENCES RawMaterials(Name)
);
CREATE TABLE CookiesPallet (
    BatchID INT,
    CookieName VARCHAR(255),
    Pallet_ID INT,
    FOREIGN KEY (BatchID, CookieName) REFERENCES Cookies(BatchID, CookieName),
    FOREIGN KEY (Pallet_ID) REFERENCES Pallet(Pallet_ID)
);

CREATE TABLE OrderCookies (
    Order_ID INT,
    BatchID INT,
    CookieName VARCHAR(255),
    FOREIGN KEY (Order_ID) REFERENCES Orders(Order_ID),
    FOREIGN KEY (BatchID, CookieName) REFERENCES Cookies(BatchID, CookieName)
);

