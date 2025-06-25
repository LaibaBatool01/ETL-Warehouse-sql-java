package datawareproject;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Transaction {
    private int orderId;
    private Date orderDate;
    private long productId;  
    private long customerId; 
    private String customerName;
    private String gender;
    private int quantityOrdered;
    private double totalSale; 


   
    public Transaction() {
    }

    
    public Transaction(int orderId, Date orderDate, long productId, long customerId,
                       String customerName, String gender, int quantityOrdered) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.productId = productId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.gender = gender;
        this.quantityOrdered = quantityOrdered;
    }

   
    public int getOrderId() {
    	return orderId; 
    	}
    public Date getOrderDate() {
    	return orderDate;
    	}
    public long getProductId() {
    	return productId; 
    	}
    public long getCustomerId() { 
    	return customerId;  
    	}
    public String getCustomerName() {
    	return customerName; 
    	}
    public String getGender() {
    	return gender; 
    	}
    public int getQuantityOrdered() {
    	return quantityOrdered;
    	}
    public double getTotalSale() {
        return this.totalSale;
    }

   
    public void setOrderId(int orderId) { 
    	this.orderId = orderId;
    	}
    public void setOrderDate(Date orderDate) {
    	this.orderDate = orderDate;
    	}
    public void setProductId(long productId) { 
    	this.productId = productId;
    	}
    public void setCustomerId(long customerId) {
    	this.customerId = customerId; 
    	}
    public void setCustomerName(String customerName) {
    	this.customerName = customerName; 
    	}
    public void setGender(String gender) {
    	this.gender = gender; 
    	}
    public void setQuantityOrdered(int quantityOrdered) {
    	this.quantityOrdered = quantityOrdered;
    	}
    public void setTotalSale(double totalSale) {
        this.totalSale = totalSale;
    }
    
    public void setFromResultSet(ResultSet rs) throws SQLException {
        this.orderId = rs.getInt("Order ID");
        this.orderDate = rs.getDate("Order Date");
        this.productId = rs.getLong("ProductID");
        this.customerId = rs.getLong("CustomerID");
        this.customerName = rs.getString("CustomerName");
        this.gender = rs.getString("Gender");
        this.quantityOrdered = rs.getInt("Quantity Ordered");
    }
    
    public String toString() {
        return "Transaction{" +
               "orderId=" + orderId +
               ", orderDate=" + orderDate +
               ", productId=" + productId +
               ", customerId=" + customerId +
               ", customerName='" + customerName + '\'' +
               ", gender='" + gender + '\'' +
               ", quantityOrdered=" + quantityOrdered +
               ", totalSale=" + totalSale +
               '}';
    }
    
    
}
