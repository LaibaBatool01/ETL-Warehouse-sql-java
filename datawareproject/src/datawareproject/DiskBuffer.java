package datawareproject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DiskBuffer {
    private Map<Long, MasterData> buffer;

    public DiskBuffer() {
        this.buffer = new HashMap<>();
        System.out.println("DiskBuffer initialized.");
    }

    public void loadSegment(Connection conn, Long productId) throws SQLException {
       // System.out.println("Loading segment for Product ID: " + productId);
        String sql = "SELECT * FROM master_data WHERE productID = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setLong(1, productId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    MasterData md = new MasterData();
                    md.setFromResultSet(resultSet);
                    buffer.put(productId, md);
                  //  System.out.println("Loaded MasterData for Product ID: " + productId);
                } else {
                  //  System.out.println("No MasterData found for Product ID: " + productId);
                }
            }
        }
    }

    public MasterData getMasterData(Long productId) {
        MasterData md = buffer.get(productId);
        if (md != null) {
          //  System.out.println("Retrieved MasterData for Product ID: " + productId);
        } else {
           // System.out.println("No MasterData in buffer for Product ID: " + productId);
        }
        return md;
    }
}
