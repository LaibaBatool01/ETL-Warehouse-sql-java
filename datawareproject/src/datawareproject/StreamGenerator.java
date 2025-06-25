package datawareproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class StreamGenerator implements Runnable {
    private final String jdbcUrl;
    private final String username;
    private final String password;
    private final BlockingQueue<Transaction> queue;
    private boolean isCompleted = false;
    private final int initialChunkSize = 1000;
    private int nextChunkSize = initialChunkSize;

    public StreamGenerator(String jdbcUrl, String username, String password, BlockingQueue<Transaction> queue) {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
        this.queue = queue;
    }

    
    public void run() {
        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password)) {
            System.out.println("Connected to database");

            while (!isCompleted) {
                String sql = "SELECT `Order ID`, `Order Date`, `ProductID`, `CustomerID`, `CustomerName`, `Gender`, `Quantity Ordered` FROM transactions LIMIT ?";
                try (PreparedStatement pst = conn.prepareStatement(sql)) {
                    pst.setInt(1, nextChunkSize);
                    ResultSet rs = pst.executeQuery();

                    int count = 0;
                    while (rs.next()) {
                        Transaction transaction = new Transaction();
                        transaction.setFromResultSet(rs);
                        //System.out.println("Read transaction: " + transaction + "  " + count);
                        queue.put(transaction);
                        count++;
                        
                      //  if (count > 10) {
                        //    isCompleted = true;
                      // }
                    }

                    if (count < nextChunkSize) {
                        isCompleted = true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   

    public void adjustChunkSize(int newSize) {
      //  System.out.println("Adjusting chunk size to: " + newSize);
        if (newSize > 0) {
            this.nextChunkSize = newSize;
        } else {
          //  System.out.println("Invalid new chunk size: " + newSize);
        }
    }


    public boolean isCompleted() {
        return isCompleted;
    }
}
