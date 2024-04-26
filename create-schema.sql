SET FOREIGN_KEY_CHECKS = 0;

-- Drop tables if they exist
DROP TABLE IF EXISTS orderCookies;
DROP TABLE IF EXISTS palletOrders;
DROP TABLE IF EXISTS cookiesPallet;
DROP TABLE IF EXISTS recipes;
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

CREATE TABLE cookies (
    name VARCHAR(255),
    PRIMARY KEY (name)
);

CREATE TABLE pallets (
    pallet_ID INT auto_increment,
    dateTime DATETIME,
    cookieName VARCHAR(255),
    location VARCHAR(255),
    blocked varchar(3),
    Primary key (pallet_ID),
 FOREIGN KEY (cookieName) REFERENCES cookies(name)
);


CREATE TABLE delivered (
    order_ID INT PRIMARY KEY,
    dateOfDelivery DATE,
    FOREIGN KEY (order_ID) REFERENCES orders(order_ID)
);




CREATE TABLE raw_materials (
    name VARCHAR(255) PRIMARY KEY,
    amount INT,
    dateDelivered DATE,
    unit varChar(10)
);


CREATE TABLE recipes (
    ingredient VARCHAR(255),
    quantityInRecipe INT,
    cookieName VARCHAR(255),
 FOREIGN KEY (cookieName) REFERENCES cookies(name),
 FOREIGN KEY (ingredient) REFERENCES raw_materials(name)
 );
 

CREATE TABLE orderCookies (
    order_ID INT,
    cookieName VARCHAR(255),
    FOREIGN KEY (order_ID) REFERENCES orders(order_ID),
    FOREIGN KEY (cookieName) REFERENCES cookies(name)
);