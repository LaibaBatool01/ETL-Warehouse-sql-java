package datawareproject;


//String username = "root";
//String password = "Laiba1426";
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.Scanner; 

public class MainClass {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); 

        System.out.print("Enter the database username: ");
        String username = scanner.nextLine();

        System.out.print("Enter the database password: ");
        String password = scanner.nextLine();

        String jdbcUrl = "jdbc:mysql://localhost:3306/TransactionMasterData?useSSL=false";
        String jdbcUrlDataWarehouse = "jdbc:mysql://localhost:3306/ELECTRONICA_DW?useSSL=false";

        BlockingQueue<Transaction> queue = new LinkedBlockingQueue<>();
        StreamGenerator streamGenerator = new StreamGenerator(jdbcUrl, username, password, queue);

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(jdbcUrl, username, password);
            Connection dataWarehouseConn = DriverManager.getConnection(jdbcUrlDataWarehouse, username, password);

            System.out.println("Connected to the database successfully");
            DataProcessor dataProcessor = new DataProcessor(dataWarehouseConn);

            HybridJoin hybridJoin = new HybridJoin(dataProcessor, conn, queue, streamGenerator);
            Controller controller = new Controller(streamGenerator, hybridJoin);

            Thread streamThread = new Thread(streamGenerator);
            Thread joinThread = new Thread(hybridJoin);
            Thread controllerThread = new Thread(controller);

            streamThread.start();
            joinThread.start();
            controllerThread.start();

            while (!streamGenerator.isCompleted() || !queue.isEmpty()) {
                try {
                    Thread.sleep(100); // Sleep to reduce CPU usage
                } catch (InterruptedException e) {
                    System.out.println("Main thread interrupted.");
                    break;
                }
            }

            streamThread.join();
            joinThread.join();
            controllerThread.join();

            System.out.println("All transactions processed. Program terminating.");

        } catch (SQLException e) {
            System.out.println("Error connecting to the database");
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted");
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                    System.out.println("Database connection closed");
                } catch (SQLException e) {
                    System.out.println("Error closing the database connection");
                    e.printStackTrace();
                }
            }
        }
    }
}
