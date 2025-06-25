DROP DATABASE IF EXISTS ELECTRONICA_DW;
CREATE DATABASE ELECTRONICA_DW;
USE ELECTRONICA_DW;
DROP TABLE IF EXISTS FactSales;
DROP TABLE IF EXISTS ProductDimension;
DROP TABLE IF EXISTS CustomerDimension;
DROP TABLE IF EXISTS DateDimension;
DROP TABLE IF EXISTS StoreDimension;
DROP TABLE IF EXISTS SupplierDimension;



CREATE TABLE ProductDimension (
    ProductID INT PRIMARY KEY,
    productName VARCHAR(255),
    productPrice DECIMAL(10,2),
    supplierID INT
);

CREATE TABLE CustomerDimension (
    CustomerID INT PRIMARY KEY,
    CustomerName VARCHAR(255),
    Gender VARCHAR(10)
);

CREATE TABLE DateDimension (
    DateID DATE PRIMARY KEY,
    d_Year YEAR,
    d_Month INT,
    d_Day INT
);

CREATE TABLE StoreDimension (
    StoreID INT PRIMARY KEY,
    storeName VARCHAR(255)
);

CREATE TABLE SupplierDimension (
    SupplierID INT PRIMARY KEY,
    supplierName VARCHAR(255)
);
CREATE TABLE FactSales (
    OrderID INT PRIMARY KEY,
    ProductID INT,
    CustomerID INT,
    StoreID INT,
    DateID DATE,
    QuantityOrdered INT,
    TotalSale DECIMAL(10,2),  
    FOREIGN KEY (ProductID) REFERENCES ProductDimension(ProductID),
    FOREIGN KEY (CustomerID) REFERENCES CustomerDimension(CustomerID),
    FOREIGN KEY (StoreID) REFERENCES StoreDimension(StoreID),
    FOREIGN KEY (DateID) REFERENCES DateDimension(DateID)
);

