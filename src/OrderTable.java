
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class OrderTable {

	protected static JFrame tableFrame;
	private static JScrollPane scrollpane;
	private static JTable table;
	private static boolean tableCreated = false;
	private static Statement stmt;
	private static String[] tableHeadings = {"Order Number" , "First Name" , "Last Name" , "Flavor" , "Topping", "Serving Style", "Price", "Date"};
	private static int numberOfRows = 0;
	private static int count = 0;
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	
	/**
	 * Creates a new JFrame to display
	 * the ice cream order records from the SQL Server
	 */
	public OrderTable(){
	
		// Gets a connection from SQL Server
		DBConnectionManager.getInstance();
		
		// Setup the frame for the table
		tableFrame = new JFrame("Ice Cream Orders");
		tableFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		tableFrame.setLayout(new FlowLayout());
		tableFrame.setBounds(100, 100, 1000, 500);
		tableFrame.setResizable(false);
	}

	/**
	 * Returns the total number of records in 
	 * the ICE_CREAM_ORDERS table by executing a
	 * select statement
	 * @return void
	 */
	private static void getNumberOfRows(){
		try{
			stmt = DBConnectionManager.conn.createStatement();
			ResultSet rows = stmt.executeQuery("SELECT COUNT(*) FROM ICE_CREAM_ORDERS");
			rows.next();
			numberOfRows = rows.getInt(1);
			System.out.println("There are " + numberOfRows + " rows in table");
		}
		catch(SQLException error){
			error.printStackTrace();
		}
	}

	/**
	 * Updates the JFrame with the ice cream orders <br>
	 * Queries SQL Server to return ice cream order data
	 * and fill a 2D array with the data <br>
	 * @return void
	 */
	protected static void updateTable(){

		if(tableCreated){
				tableFrame.remove(scrollpane);
		}
		tableCreated = true;

		try{
			getNumberOfRows();
			Statement stmt = DBConnectionManager.conn.createStatement();
			ResultSet data = stmt.executeQuery("SELECT * FROM ICE_CREAM_ORDERS");

			// Fill 2D String array with data from the ResultSet returned from the SQL query
			String[][] tableData = new String[numberOfRows][tableHeadings.length];
			System.out.println(">QUERY EXECUTED: SELECT * FROM ICE_CREAM_ORDERS\n---------------------------------------------------------------------------------------------------------------------------------------------------");
			while(data.next()){
				
				int orderNum = data.getInt("ORDER_NUMBER");
				String firstName = data.getString("FIRST_NAME");
				String lastName = data.getString("LAST_NAME");
				String flavor = data.getString("FLAVOR");
				String topping = data.getString("TOPPING");
				String servingStyle = data.getString("SERVING");
				double price = data.getDouble("PRICE");
				String date = dateFormat.format(data.getDate("ORDER_DATE"));;

				System.out.println("\nOrder Number: " + orderNum + "\nFirst Name: " + firstName+ "\nLast Name: " + lastName + "\nFlavor: " + flavor + "\nTopping: " + topping + "\nServing Style: " + servingStyle + "\nPrice: " + price + "\nOrder Date: " + date + "\n\n****************************************************************************************************************************************************");

					tableData[count][0] = orderNum+"";
					tableData[count][1] = firstName;
					tableData[count][2] = lastName;
					tableData[count][3] = flavor;
					tableData[count][4] = topping;
					tableData[count][5] = servingStyle;
					tableData[count][6] = NumberFormat.getCurrencyInstance().format(price);
					tableData[count][7] = date;
				
					// Increment the row index b/c no for loop 
					count++;
			}
			
			// Create the JTable
			table = new JTable(tableData, tableHeadings);
			scrollpane = new JScrollPane(table);
			scrollpane.setPreferredSize(new Dimension(975,460));
			tableFrame.add(scrollpane);
			if(tableFrame.isVisible()){
				tableFrame.setVisible(true);
			}
			
			// Reset count so table can be opened again
			count = 0;
		}
		catch(Exception error){
			error.printStackTrace();
		}

	}
	
}
