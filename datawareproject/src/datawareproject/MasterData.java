package datawareproject;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MasterData {
    private int productId;
    private String productName;
    private String productPrice; 
    private int supplierId;
    private String supplierName;
    private int storeId;
    private String storeName;

    public MasterData() {
    }

    public MasterData(int productId, String productName, String productPrice, 
                      int supplierId, String supplierName, int storeId, String storeName) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.storeId = storeId;
        this.storeName = storeName;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public void setFromResultSet(ResultSet rs) throws SQLException {
        this.productId = rs.getInt("productId");
        this.productName = rs.getString("productName");
        this.productPrice = rs.getString("productPrice");
        this.supplierId = rs.getInt("supplierId");
        this.supplierName = rs.getString("supplierName");
        this.storeId = rs.getInt("storeId");
        this.storeName = rs.getString("storeName");
    }
}
