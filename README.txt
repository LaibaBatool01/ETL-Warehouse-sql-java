
## Description
This Java project demonstrates a data warehousing process that combines data from different sources using a Hybrid Join technique. It loads data from a transaction database and a data warehouse database, processes the data, and performs joins. The project includes three main components: `StreamGenerator`, `HybridJoin`, and `Controller`.

## Prerequisites
Before running the project, ensure you have the following prerequisites installed:
- Java Development Kit (JDK)
- MySQL Database Server
- MySQL Connector/J (JDBC Driver)

## Database Setup
1. Create two MySQL databases:
   - `TransactionMasterData` (Contains two tables: `TRANSACTIONS` and `MASTER_DATA`)
   - `ELECTRONICA_DW` (Data Warehouse with Star Schema)
   
2. Import the Star Schema and OLAP queries into the `ELECTRONICA_DW` database using the provided SQL files.



## Usage
1. Run the `MainClass` to start the data warehousing process.
2. Enter your database username and password when prompted.
3. The program will connect to the specified databases and perform data processing and joining operations.

## Components
- `StreamGenerator`: Generates and streams transaction data.
- `HybridJoin`: Combines data from transaction and master data sources.
- `Controller`: Monitors and adjusts the data processing.


## Dependencies
- This project requires the JDBC driver for MySQL.
- Make sure to include the JDBC driver JAR file in your project's classpath.
=======
## Description
This Java project demonstrates a data warehousing process that combines data from different sources using a Hybrid Join technique. It loads data from a transaction database and a data warehouse database, processes the data, and performs joins. The project includes three main components: `StreamGenerator`, `HybridJoin`, and `Controller`.

## Prerequisites
Before running the project, ensure you have the following prerequisites installed:
- Java Development Kit (JDK)
- MySQL Database Server
- MySQL Connector/J (JDBC Driver)

## Database Setup
1. Create two MySQL databases:
   - `TransactionMasterData` (Contains two tables: `TRANSACTIONS` and `MASTER_DATA`)
   - `ELECTRONICA_DW` (Data Warehouse with Star Schema)
   
2. Import the Star Schema and OLAP queries into the `ELECTRONICA_DW` database using the provided SQL files.



## Usage
1. Run the `MainClass` to start the data warehousing process.
2. Enter your database username and password when prompted.
3. The program will connect to the specified databases and perform data processing and joining operations.

## Components
- `StreamGenerator`: Generates and streams transaction data.
- `HybridJoin`: Combines data from transaction and master data sources.
- `Controller`: Monitors and adjusts the data processing.


## Dependencies
- This project requires the JDBC driver for MySQL.
- Make sure to include the JDBC driver JAR file in your project's classpath.
>>>>>>> fbad43f7cb31451c92594c0124b5fc51c3964158
