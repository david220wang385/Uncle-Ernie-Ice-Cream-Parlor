import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

/**
 * Creates a new swing application <br>
 * that allows the user to insert <br>
 * information for a ice cream order <br>
 * then send that information to a SQL Server <br>
 * which can then also be queried and displayed in a table <br>
 * @author  David Wang
 * @version 1.0
 * @since 4/28/2017
 */
public class UserInterface {

	// Global Variables
	JTextField firstName; 
	JTextField lastName;
	JComboBox<String> iceCreamFlavors;
	JComboBox<String> iceCreamToppings;
	JRadioButton coneOption;
	JRadioButton cupOption;
	JLabel price;
	double[] flavorPrices = {2.50, 2.50, 3.50, 3.00,
			3.50, 3.50, 3.00, 3.50, 4.00};
	double[] toppingPrices = {0.0, 0.5, 0.1, 0.5, 0.25};


	public UserInterface(){

		// Setup main window of the user interface
		JFrame mainFrame = new JFrame("Uncle Ernie's Ice Cream Parlor");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setBounds(450,250,350,400);
		mainFrame.setVisible(true);
		mainFrame.setLayout(new GridLayout(9,2,5,2));
		mainFrame.setResizable(false);

		// Create and add Components
		JLabel firstNameLabel = new JLabel("First Name:");
		firstNameLabel.setHorizontalAlignment(SwingUtilities.RIGHT);
		mainFrame.add(firstNameLabel);

		firstName = new JTextField();
		mainFrame.add(firstName);

		JLabel lastNameLabel = new JLabel("Last Name:");
		lastNameLabel.setHorizontalAlignment(SwingUtilities.RIGHT);
		mainFrame.add(lastNameLabel);

		lastName = new JTextField();
		mainFrame.add(lastName);

		JLabel iceCreamFlavorLabel = new JLabel("Ice Cream Flavor:");
		iceCreamFlavorLabel.setHorizontalAlignment(SwingUtilities.RIGHT);
		mainFrame.add(iceCreamFlavorLabel);

		// Array of ice cream flavors
		String[] flavors = {"Chocolate", "Vanilla", "Rocky Road", "Mint Chocolate Chip",
				"Cookie Dough", "Butter Pecan", "Strawberry", "Cookies 'n' Cream", "Dulce de Leche"};
		iceCreamFlavors = new JComboBox<>(flavors);
		mainFrame.add(iceCreamFlavors);

		JLabel iceCreamToppingsLabel = new JLabel("Ice Cream Topping:");
		iceCreamToppingsLabel.setHorizontalAlignment(SwingUtilities.RIGHT);
		mainFrame.add(iceCreamToppingsLabel);

		// Array of ice cream toppings
		String[] toppings = {"No topping", "Chocolate Syrup", "Whipped Cream", "Pineapple", "Sprinkles"};
		iceCreamToppings= new JComboBox<>(toppings);
		mainFrame.add(iceCreamToppings);

		JLabel servingLabel = new JLabel("Serving Style");
		servingLabel.setHorizontalAlignment(SwingUtilities.RIGHT);
		mainFrame.add(servingLabel);

		coneOption = new JRadioButton("Cone");
		mainFrame.add(coneOption);
		mainFrame.add(new JLabel());

		cupOption = new JRadioButton("Cup");
		mainFrame.add(cupOption);

		ButtonGroup servingOptions = new ButtonGroup();
		servingOptions.add(coneOption);
		servingOptions.add(cupOption);

		JLabel priceLabel = new JLabel("Price: ");
		priceLabel.setHorizontalAlignment(SwingUtilities.RIGHT);
		mainFrame.add(priceLabel);

		price = new JLabel(" $2.50");
		price.setHorizontalAlignment(SwingUtilities.LEFT);
		mainFrame.add(price);

		JLabel errorLabel = new JLabel();
		errorLabel.setHorizontalAlignment(SwingUtilities.CENTER);
		mainFrame.add(errorLabel);

		JButton addOrder = new JButton("Add Order");
		mainFrame.add(addOrder);

		JLabel errorMessages = new JLabel();
		errorMessages.setHorizontalAlignment(SwingUtilities.CENTER);
		mainFrame.add(errorMessages);

		JButton showTable = new JButton("Show Table");
		mainFrame.add(showTable);

		// Create a red border for warning label
		Border warningOutline = BorderFactory.createLineBorder(Color.RED);
		Border defaultOutline = BorderFactory.createLineBorder(Color.BLACK);

		// Attempt to create a new IceCreamOrder object
		addOrder.addActionListener(new ActionListener() {

			/**
			 * Attempts to create a new IceCreanOrder <br>
			 * with the data in the user interface. <br>
			 * If some fields are not filled, <br>
			 * tell the user that fields must be completed <br>
			 * @param ActionEvent e This ActionEvent is generated when the associated button is pressed
			 * @return void
			 */
			@Override
			public void actionPerformed(ActionEvent e) {

				boolean firstNameFilled = firstName.getText().length() > 0;
				boolean lastNameFilled = lastName.getText().length() > 0;
				boolean servingSelected = (servingOptions.getSelection() != null);

				// Ensure all fields are filled
				if(firstName.getText().equals("") || lastName.getText().equals("") || servingOptions.getSelection() == null){

					errorLabel.setForeground(Color.RED);
					errorLabel.setText("Error");
					errorMessages.setForeground(Color.RED);
					errorMessages.setText("Please fill in all the fields");

				}

				// If all fields are filled in create a new IceCreamOrder object
				else{

					// Insert a new record into the ICE_CREAM_ORDERS table
					IceCreamOrder order = new IceCreamOrder(firstName.getText(), lastName.getText(), flavors[iceCreamFlavors.getSelectedIndex()],  toppings[iceCreamToppings.getSelectedIndex()], ((cupOption.isSelected()) ? "Cup": "Cone") , (flavorPrices[iceCreamFlavors.getSelectedIndex()]+toppingPrices[iceCreamToppings.getSelectedIndex()]) );
					System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------");
					System.out.println(">>Order Added:\n" + order);
					System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------");
					clearAllFields();
					JOptionPane.showMessageDialog(null, order.toString());
					DBConnectionManager.insertIntoTable(order);

					// Set borders back to normal
					errorLabel.setText("");
					errorMessages.setText("");

					// Update the table to include the newly placed order
					OrderTable.updateTable();
				}

				firstName.setBorder(firstNameFilled ? defaultOutline : warningOutline);
				lastName.setBorder(lastNameFilled ? defaultOutline : warningOutline);
				coneOption.setForeground(servingSelected ? Color.BLACK : Color.RED);
				cupOption.setForeground(servingSelected ?  Color.BLACK : Color.RED);
			}
		});

		// Calculates price of the order using ItemListeners
		ActionListener priceUpdater = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				updatePrice();
			}
		};

		iceCreamFlavors.addActionListener(priceUpdater);
		iceCreamToppings.addActionListener(priceUpdater);

		// Shows table when showTable button is pressed
		showTable.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				OrderTable.updateTable();
				OrderTable.tableFrame.setVisible(true);
			}
		});

		// Initialize the OrderTable class to allow a new 
		// window with placed orders to be opened 
		new OrderTable();
	}

	/**
	 * Clear all the text fields and set ComboBox  <br>
	 * and RadioButton objects to their default values <br>
	 * @return void  
	 */
	private void clearAllFields(){
		firstName.setText("");
		lastName.setText("");
		iceCreamFlavors.setSelectedIndex(0);
		iceCreamToppings.setSelectedIndex(0);
		coneOption.setSelected(true);
		cupOption.setSelected(false);
	}

	/**
	 * Updates the price of the order by adding the double values <br>
	 * of the flavor and topping ComboBoxes and then formating <br>
	 * the value to include a dollar sign and exactly 2 decimals <br>
	 * after the decimal point <br>
	 * @return void
	 */
	private void updatePrice(){
		price.setText(NumberFormat.getCurrencyInstance().format(flavorPrices[iceCreamFlavors.getSelectedIndex()]+toppingPrices[iceCreamToppings.getSelectedIndex()]));
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				new UserInterface();
				DBConnectionManager.getInstance();
			}
		});
	}
}
