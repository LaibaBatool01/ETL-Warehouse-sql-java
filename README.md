
# Data Warehouse ETL Project

A comprehensive Java-based data warehousing solution that demonstrates ETL (Extract, Transform, Load) processes using hybrid join techniques for combining data from multiple sources.

## 📋 Overview

This project implements a data warehousing system that:
- Processes transaction data from multiple database sources
- Performs hybrid joins between transaction and master data
- Implements efficient data buffering and streaming mechanisms
- Provides a complete ETL pipeline for data warehouse operations

## 🏗️ Architecture

The project consists of several key components:

- **`MainClass`** - Entry point and orchestrator of the ETL process
- **`StreamGenerator`** - Generates and streams transaction data from source systems
- **`HybridJoin`** - Implements hybrid join algorithms for combining data streams
- **`DataProcessor`** - Core data processing and transformation logic
- **`Controller`** - Monitors and controls the data processing workflow
- **`DiskBuffer`** - Manages memory-efficient data buffering
- **`Transaction`** - Data model for transaction records
- **`MasterData`** - Data model for master data records

## 🛠️ Prerequisites

Before running the project, ensure you have the following installed:

- **Java Development Kit (JDK) 8 or higher**
- **MySQL Database Server 5.7 or higher**
- **MySQL Connector/J (JDBC Driver)**

## 📊 Database Setup

### 1. Create Required Databases

Create two MySQL databases:

```sql
CREATE DATABASE TransactionMasterData;
CREATE DATABASE ELECTRONICA_DW;
```

### 2. Database Structure

- **`TransactionMasterData`**: Contains source transaction data
  - `TRANSACTIONS` table
  - `MASTER_DATA` table

- **`ELECTRONICA_DW`**: Data warehouse with Star Schema
  - Fact and dimension tables
  - Optimized for OLAP operations

### 3. Import Schema and Data

1. Import the provided SQL files:
   - `datawarehouse_project.sql` - Main schema
   - `tran_new.sql` - Transaction data
   - `Queries-DW-SQL.sql` - Sample queries and additional schema

2. Execute the SQL files in your MySQL environment:
   ```bash
   mysql -u username -p TransactionMasterData < datawarehouse_project.sql
   mysql -u username -p ELECTRONICA_DW < Queries-DW-SQL.sql
   ```

## 🚀 Getting Started

### 1. Compilation

Navigate to the project directory and compile the Java files:

```bash
cd datawareproject/src
javac -d ../bin datawareproject/*.java
```

### 2. Add MySQL JDBC Driver

Ensure the MySQL Connector/J JAR file is in your classpath:
```bash
# Download MySQL Connector/J from https://dev.mysql.com/downloads/connector/j/
# Add to classpath when running
```

### 3. Run the Application

```bash
cd datawareproject/bin
java -cp .:mysql-connector-java-x.x.x.jar datawareproject.MainClass
```

## 💻 Usage

1. **Start the Application**: Run the `MainClass` to begin the ETL process
2. **Database Credentials**: Enter your MySQL username and password when prompted
3. **Processing**: The system will:
   - Connect to the specified databases
   - Stream transaction data
   - Perform hybrid joins with master data
   - Load processed data into the data warehouse

## 📁 Project Structure

```
warehouse-ETL/
├── datawareproject/
│   ├── src/datawareproject/          # Source code
│   │   ├── MainClass.java           # Application entry point
│   │   ├── StreamGenerator.java     # Data streaming component
│   │   ├── HybridJoin.java         # Join algorithms
│   │   ├── DataProcessor.java      # Core processing logic
│   │   ├── Controller.java         # Process controller
│   │   ├── DiskBuffer.java         # Memory management
│   │   ├── Transaction.java        # Transaction data model
│   │   └── MasterData.java         # Master data model
│   └── bin/                        # Compiled classes
├── datawarehouse_project.sql       # Main database schema
├── tran_new.sql                    # Transaction data
├── Queries-DW-SQL.sql             # OLAP queries and schema
└── README.md                       # This file
```

## 🔧 Configuration

The application connects to MySQL databases with default settings:
- **Host**: localhost
- **Port**: 3306 (default MySQL port)
- **Databases**: TransactionMasterData, ELECTRONICA_DW

Modify the database connection parameters in the source code if needed.

## 📈 Features

- **Hybrid Join Algorithm**: Efficient joining of large datasets
- **Stream Processing**: Real-time data streaming capabilities
- **Memory Management**: Disk-based buffering for large datasets
- **ETL Pipeline**: Complete Extract, Transform, Load workflow
- **Star Schema**: Optimized data warehouse design
- **OLAP Support**: Support for analytical queries

## 🔍 Sample Queries

The project includes sample OLAP queries in `Queries-DW-SQL.sql` for:
- Sales analysis
- Customer segmentation
- Product performance
- Time-based analytics

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## 📄 License

This project is for educational and demonstration purposes.

## 📞 Support

=======
# Data Warehouse ETL Project

A comprehensive Java-based data warehousing solution that demonstrates ETL (Extract, Transform, Load) processes using hybrid join techniques for combining data from multiple sources.

## 📋 Overview

This project implements a data warehousing system that:
- Processes transaction data from multiple database sources
- Performs hybrid joins between transaction and master data
- Implements efficient data buffering and streaming mechanisms
- Provides a complete ETL pipeline for data warehouse operations

## 🏗️ Architecture

The project consists of several key components:

- **`MainClass`** - Entry point and orchestrator of the ETL process
- **`StreamGenerator`** - Generates and streams transaction data from source systems
- **`HybridJoin`** - Implements hybrid join algorithms for combining data streams
- **`DataProcessor`** - Core data processing and transformation logic
- **`Controller`** - Monitors and controls the data processing workflow
- **`DiskBuffer`** - Manages memory-efficient data buffering
- **`Transaction`** - Data model for transaction records
- **`MasterData`** - Data model for master data records

## 🛠️ Prerequisites

Before running the project, ensure you have the following installed:

- **Java Development Kit (JDK) 8 or higher**
- **MySQL Database Server 5.7 or higher**
- **MySQL Connector/J (JDBC Driver)**

## 📊 Database Setup

### 1. Create Required Databases

Create two MySQL databases:

```sql
CREATE DATABASE TransactionMasterData;
CREATE DATABASE ELECTRONICA_DW;
```

### 2. Database Structure

- **`TransactionMasterData`**: Contains source transaction data
  - `TRANSACTIONS` table
  - `MASTER_DATA` table

- **`ELECTRONICA_DW`**: Data warehouse with Star Schema
  - Fact and dimension tables
  - Optimized for OLAP operations

### 3. Import Schema and Data

1. Import the provided SQL files:
   - `datawarehouse_project.sql` - Main schema
   - `tran_new.sql` - Transaction data
   - `Queries-DW-SQL.sql` - Sample queries and additional schema

2. Execute the SQL files in your MySQL environment:
   ```bash
   mysql -u username -p TransactionMasterData < datawarehouse_project.sql
   mysql -u username -p ELECTRONICA_DW < Queries-DW-SQL.sql
   ```

## 🚀 Getting Started

### 1. Compilation

Navigate to the project directory and compile the Java files:

```bash
cd datawareproject/src
javac -d ../bin datawareproject/*.java
```

### 2. Add MySQL JDBC Driver

Ensure the MySQL Connector/J JAR file is in your classpath:
```bash
# Download MySQL Connector/J from https://dev.mysql.com/downloads/connector/j/
# Add to classpath when running
```

### 3. Run the Application

```bash
cd datawareproject/bin
java -cp .:mysql-connector-java-x.x.x.jar datawareproject.MainClass
```

## 💻 Usage

1. **Start the Application**: Run the `MainClass` to begin the ETL process
2. **Database Credentials**: Enter your MySQL username and password when prompted
3. **Processing**: The system will:
   - Connect to the specified databases
   - Stream transaction data
   - Perform hybrid joins with master data
   - Load processed data into the data warehouse

## 📁 Project Structure

```
warehouse-ETL/
├── datawareproject/
│   ├── src/datawareproject/          # Source code
│   │   ├── MainClass.java           # Application entry point
│   │   ├── StreamGenerator.java     # Data streaming component
│   │   ├── HybridJoin.java         # Join algorithms
│   │   ├── DataProcessor.java      # Core processing logic
│   │   ├── Controller.java         # Process controller
│   │   ├── DiskBuffer.java         # Memory management
│   │   ├── Transaction.java        # Transaction data model
│   │   └── MasterData.java         # Master data model
│   └── bin/                        # Compiled classes
├── datawarehouse_project.sql       # Main database schema
├── tran_new.sql                    # Transaction data
├── Queries-DW-SQL.sql             # OLAP queries and schema
└── README.md                       # This file
```

## 🔧 Configuration

The application connects to MySQL databases with default settings:
- **Host**: localhost
- **Port**: 3306 (default MySQL port)
- **Databases**: TransactionMasterData, ELECTRONICA_DW

Modify the database connection parameters in the source code if needed.

## 📈 Features

- **Hybrid Join Algorithm**: Efficient joining of large datasets
- **Stream Processing**: Real-time data streaming capabilities
- **Memory Management**: Disk-based buffering for large datasets
- **ETL Pipeline**: Complete Extract, Transform, Load workflow
- **Star Schema**: Optimized data warehouse design
- **OLAP Support**: Support for analytical queries

## 🔍 Sample Queries

The project includes sample OLAP queries in `Queries-DW-SQL.sql` for:
- Sales analysis
- Customer segmentation
- Product performance
- Time-based analytics

