SET FOREIGN_KEY_CHECKS = 0;

-- Drop tables if they exist
DROP TABLE IF EXISTS orderCookies;
DROP TABLE IF EXISTS palletOrders;
DROP TABLE IF EXISTS cookiesPallet;
DROP TABLE IF EXISTS recipies;
DROP TABLE IF EXISTS raw_materials;
DROP TABLE IF EXISTS cookies;
DROP TABLE IF EXISTS delivered;
DROP TABLE IF EXISTS pallets;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS customers;
SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE customers (
    name VARCHAR(255) PRIMARY KEY,
    address VARCHAR(255) 
);


CREATE TABLE orders (
    order_ID INT auto_increment PRIMARY KEY,
    customer_Name VARCHAR(255),
    wishedDateOfDelivery DATE,
    FOREIGN KEY (customer_Name) REFERENCES customers(name)
);


CREATE TABLE pallets (
    pallet_ID INT auto_increment PRIMARY KEY,
    dateTime DATETIME,
    location VARCHAR(255),
    blocked varchar(3),
    FOREIGN KEY (order_ID) REFERENCES orders(order_ID)
);


CREATE TABLE delivered (
    order_ID INT PRIMARY KEY,
    dateOfDelivery DATE,
    FOREIGN KEY (order_ID) REFERENCES orders(order_ID)
);


CREATE TABLE cookies (
    batchID INT auto_increment,
    name VARCHAR(255),
    productionDate DATE,
    productionTime TIME,
    PRIMARY KEY (batchID, name)
);


CREATE TABLE raw_materials (
    name VARCHAR(255) PRIMARY KEY,
    amount INT,
    dateDelivered DATE,
    unit varChar(10)
);


CREATE TABLE recipies (
    ingredient VARCHAR(255),
    quantityInRecipe INT,
    batchID INT,
    cookieName VARCHAR(255),
    PRIMARY KEY (cookieName, batchID, ingredient),
 FOREIGN KEY (batchID, cookieName) REFERENCES cookies(batchID, name)
 FOREIGN KEY (ingredient) REFERENCES raw_materials(name);
 );
CREATE TABLE cookiesPallet (
    batchID INT,
    cookieName VARCHAR(255),
    pallet_ID INT,
    FOREIGN KEY (batchID, cookieName) REFERENCES cookies(batchID, name),
    FOREIGN KEY (pallet_ID) REFERENCES pallets(pallet_ID)
);

CREATE TABLE orderCookies (
    order_ID INT,
    batchID INT,
    cookieName VARCHAR(255),
    FOREIGN KEY (order_ID) REFERENCES orders(order_ID),
    FOREIGN KEY (batchID, cookieName) REFERENCES cookies(batchID, name)
);