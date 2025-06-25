package datawareproject;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;



public class DataProcessor {
    private Map<Long, List<Transaction>> multiHashTable; 
    private DiskBuffer diskBuffer; 
    private int printedCount = 0; 
    private Connection dataWarehouseConn; 


    public DataProcessor( Connection dataWarehouseConn) {
        this.multiHashTable = new HashMap<>();
        this.diskBuffer = new DiskBuffer();
        this.dataWarehouseConn = dataWarehouseConn;

    }

    public void addTransaction(Transaction transaction) {
        multiHashTable.computeIfAbsent(transaction.getProductId(), k -> new ArrayList<>()).add(transaction);
    }

    public List<Transaction> getTransactions(Long productId) {
        return multiHashTable.getOrDefault(productId, new ArrayList<>());
    }

    public void loadSegmentIntoDiskBuffer(Connection conn, List<Long> productIds) throws SQLException {
        for (Long productId : productIds) {
            diskBuffer.loadSegment(conn, productId);
        }
    }

    public MasterData getMasterData(Long productId) {
        return diskBuffer.getMasterData(productId);
    }

    public void processAndJoin(Connection conn) throws SQLException {
      //  System.out.println("processAndJoin method called in DataProcessor");

        for (Long productId : new ArrayList<>(multiHashTable.keySet())) {
            List<Transaction> transactions = multiHashTable.get(productId);

            MasterData md = getMasterData(productId);
            if (md != null) {
                for (Transaction transaction : transactions) {
                   // System.out.println("Debug: Processing transaction with ID " + transaction.getOrderId() + " and Date " + transaction.getOrderDate());

                    joinTransactionWithMasterData(transaction, md);
                   // System.out.println("Debug: Original Transaction Date: " + transaction.getOrderDate() + transaction.getProductId() );

                    if (printedCount < 50) {
                        printJoinedTransaction(transaction);
                        System.out.println("  " + printedCount ); 
                        printedCount++;
                    }
                    loadIntoDataWarehouse(transaction);
                    System.out.println("load data in data ware house " + transaction.getOrderDate() + "  product id  " + transaction.getProductId() );

                   // else {
                    	 //System.out.println(",,,,,,,"); 
                   // }
                }
            }
            // Removing processed entries from the hash table
            multiHashTable.remove(productId);
        }
    }

    private void joinTransactionWithMasterData(Transaction transaction, MasterData masterData) {
        double price = 0.0;
        try {
            String priceStr = masterData.getProductPrice().replaceAll("[^\\d.]", ""); 
            price = Double.parseDouble(priceStr);
           // System.out.println(price); 
            
        } catch (NumberFormatException e) {
            System.err.println("Invalid price format for product ID: " + transaction.getProductId() + ", Price: " + masterData.getProductPrice());
        }

        double totalSale = transaction.getQuantityOrdered() * price;
        transaction.setTotalSale(totalSale);
    }


    private void printJoinedTransaction(Transaction transaction) {
        System.out.println(transaction); 
    }
    public void loadIntoDataWarehouse(Transaction transaction) throws SQLException {
        long productId = transaction.getProductId();
        long customerId = transaction.getCustomerId();
        Date dateId = transaction.getOrderDate();
        int quantityOrdered = transaction.getQuantityOrdered();
        double totalSale = transaction.getTotalSale();

        MasterData md = getMasterData(productId);
        if (md == null) {
            throw new SQLException("MasterData not found for product ID: " + productId);
        }

        int storeId = md.getStoreId();
        String productName = md.getProductName();
        double productPrice = Double.parseDouble(md.getProductPrice().replaceAll("[^\\d.]", ""));
        int supplierId = md.getSupplierId();
        String supplierName=  md.getSupplierName();

        checkAndUpdateProductDimension(productId, productName, productPrice, supplierId, dataWarehouseConn);
        checkAndUpdateCustomerDimension(customerId, transaction.getCustomerName(), transaction.getGender(), dataWarehouseConn);
        checkAndUpdateDateDimension(dateId, dataWarehouseConn);
        checkAndUpdateStoreDimension(storeId, md.getStoreName(), dataWarehouseConn);
        checkAndUpdateSupplierDimension(supplierId, supplierName, dataWarehouseConn);
        insertIntoFactSales(transaction.getOrderId(), productId, customerId, storeId, dateId, quantityOrdered, totalSale, dataWarehouseConn);
    }

   
    
    private void checkAndUpdateProductDimension(long productId, String productName, double productPrice, int supplierId, Connection conn) throws SQLException {
        String checkQuery = "SELECT COUNT(*) FROM ProductDimension WHERE ProductID = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
            checkStmt.setLong(1, productId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                String insertQuery = "INSERT INTO ProductDimension (ProductID, productName, productPrice, supplierID) VALUES (?, ?, ?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                    insertStmt.setLong(1, productId);
                    insertStmt.setString(2, productName);
                    insertStmt.setDouble(3, productPrice);
                    insertStmt.setInt(4, supplierId);
                    insertStmt.executeUpdate();
                }
            }
        }
    }
    
    private void checkAndUpdateSupplierDimension(int supplierId, String supplierName, Connection conn) throws SQLException {
        String checkQuery = "SELECT COUNT(*) FROM SupplierDimension WHERE SupplierID = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
            checkStmt.setInt(1, supplierId);
            ResultSet rs = checkStmt.executeQuery();
            if (!rs.next() || rs.getInt(1) == 0) {
                String insertQuery = "INSERT INTO SupplierDimension (SupplierID, SupplierName) VALUES (?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                    insertStmt.setInt(1, supplierId);
                    insertStmt.setString(2, supplierName);
                    insertStmt.executeUpdate();
                }
            }
        }
    }


    private void checkAndUpdateCustomerDimension(long customerId, String customerName, String gender, Connection conn) throws SQLException {
        String checkQuery = "SELECT COUNT(*) FROM CustomerDimension WHERE CustomerID = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
            checkStmt.setLong(1, customerId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                String insertQuery = "INSERT INTO CustomerDimension (CustomerID, CustomerName, Gender) VALUES (?, ?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                    insertStmt.setLong(1, customerId);
                    insertStmt.setString(2, customerName);
                    insertStmt.setString(3, gender);
                    insertStmt.executeUpdate();
                }
            }
        }
    }

    private void checkAndUpdateDateDimension(Date dateId, Connection conn) throws SQLException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateId);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1; 
        int day = cal.get(Calendar.DAY_OF_MONTH);

      //  System.out.println("Debug: Inserting into DateDimension - Year: " + year + ", Month: " + month + ", Day: " + day);

        if (year < 1900) {
           // System.out.println("Debug: Skipping record with year < 1900");
            return;
        }

        if (year == 1819) {
            
            year = 2019;
        }

        if (year > java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)) {
            throw new SQLException("Year value out of range: " + year);
        }

     
        String checkQuery = "SELECT COUNT(*) FROM DateDimension WHERE DateID = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
            checkStmt.setDate(1, dateId);
            ResultSet rs = checkStmt.executeQuery();
            if (!rs.next() || rs.getInt(1) == 0) {
                String insertQuery = "INSERT INTO DateDimension (DateID, d_Year, d_Month, d_Day) VALUES (?, ?, ?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                    insertStmt.setDate(1, dateId);
                    insertStmt.setInt(2, year);
                    insertStmt.setInt(3, month);
                    insertStmt.setInt(4, day);
                    insertStmt.executeUpdate();
                }
            }
        }
    }



    private void checkAndUpdateStoreDimension(int storeId, String storeName, Connection conn) throws SQLException {
        String checkQuery = "SELECT COUNT(*) FROM StoreDimension WHERE StoreID = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
            checkStmt.setInt(1, storeId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                String insertQuery = "INSERT INTO StoreDimension (StoreID, storeName) VALUES (?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                    insertStmt.setInt(1, storeId);
                    insertStmt.setString(2, storeName);
                    insertStmt.executeUpdate();
                }
            }
        }
    }


    private void insertIntoFactSales(int orderId, long productId, long customerId, int storeId, Date dateId, int quantityOrdered, double totalSale, Connection conn) throws SQLException {
        String checkQuery = "SELECT COUNT(*) FROM FactSales WHERE OrderID = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
            checkStmt.setInt(1, orderId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                String insertQuery = "INSERT INTO FactSales (OrderID, ProductID, CustomerID, StoreID, DateID, QuantityOrdered, TotalSale) VALUES (?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                    insertStmt.setInt(1, orderId);
                    insertStmt.setLong(2, productId);
                    insertStmt.setLong(3, customerId);
                    insertStmt.setInt(4, storeId);
                    insertStmt.setDate(5, dateId);
                    insertStmt.setInt(6, quantityOrdered);
                    insertStmt.setDouble(7, totalSale);
                    insertStmt.executeUpdate();
                }
            }
           
        }
}
    
}