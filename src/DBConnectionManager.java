
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;

public class DBConnectionManager {

	// Use this url if connecting with sql server login and password
	private static final String dbURL = "jdbc:mysql://localhost:3306/testdb?useSSL=false";
	private static final String user = "root";
	private static final String pass = "calibur";
	private static DBConnectionManager dbInstance;
	protected static Connection conn = null;
	protected static Statement stmt;

	/**
	 * Singleton connection to the SQL Server database <br>
	 * and creates the ICE_CREAM_ORDERS table if it is not found in the database <br> 
	 */
	private DBConnectionManager(){

		// Execute statements if there is an instance of the DBConnectionManager
		try{
			conn = DriverManager.getConnection(dbURL, user, pass);

			if (conn != null) {
				DatabaseMetaData dm = conn.getMetaData();
				System.out.println("Driver name: " + dm.getDriverName());
				System.out.println("Driver version: " + dm.getDriverVersion());
				System.out.println("Product name: " + dm.getDatabaseProductName());
				System.out.println("Product version: " + dm.getDatabaseProductVersion());

				// Create the Statement object
				stmt = conn.createStatement();

				// Creates the ICE_CREAM_ORDERS if the table is not found
				ResultSet rs = dm.getTables(null, null, "ICE_CREAM_ORDERS", null);
				if(!rs.next()){
					stmt.executeUpdate("CREATE TABLE ICE_CREAM_ORDERS (ORDER_NUMBER INT  AUTO_INCREMENT PRIMARY KEY, FIRST_NAME VARCHAR(30), LAST_NAME VARCHAR(30), FLAVOR VARCHAR(30), TOPPING VARCHAR(30), SERVING VARCHAR(30), PRICE DECIMAL(6,2), ORDER_DATE DATE)");
					System.out.println("Table Created");
				}

			}
		} catch(SQLException error){
			error.printStackTrace();
		}
	}

	/**
	 *  Returns an instance of the DB Connection if there isn't one already
	 *  @return DBConnectionManager instance
	 */
	public static DBConnectionManager getInstance(){

		if(dbInstance == null){
			dbInstance = new DBConnectionManager();
		}
		return dbInstance;
	}

	/**
	 *  Insert an IceCreamOrder from the UI to the database
	 *  and records and formats the current time to be used in the
	 *  PreparedStatement which is executed to insert a new record
	 *  into the ICE_CREAM_ORDERS table
	 *  @param IceCreamOrder This is created from the information found in the user interface
	 *  @return void 
	 */
	public static void insertIntoTable(IceCreamOrder order){
		
		// Add a record to the table
		try{
			PreparedStatement insertOrder = conn.prepareStatement("INSERT INTO ICE_CREAM_ORDERS (FIRST_NAME, LAST_NAME, FLAVOR, TOPPING, SERVING, PRICE, ORDER_DATE) VALUES(?, ?, ?, ?, ?, ?, ?)");
			insertOrder.setString(1, order.getFirstName());
			insertOrder.setString(2, order.getLastName());
			insertOrder.setString(3, order.getFlavor());
			insertOrder.setString(4, order.getTopping());
			insertOrder.setString(5, order.getServing());
			insertOrder.setDouble(6, order.getPrice());
			insertOrder.setDate(7, new Date(System.currentTimeMillis()));
			insertOrder.execute();	
			insertOrder.clearParameters();
			
		}catch(SQLException err){
			err.printStackTrace();
		}
	}
}
