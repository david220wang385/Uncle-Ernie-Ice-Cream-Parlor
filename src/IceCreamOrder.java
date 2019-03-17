
import java.text.NumberFormat;

public class IceCreamOrder {

	private String firstName;
	private String lastName;
	private String flavor;
	private String topping;
	private String servingStyle;
	private double price;
	
	/**
	 * Public constructor for the IceCreamOrder class 
	 */
	public IceCreamOrder(String pFName, String pLName, String pFlavor, String pTopping, String pServing, double pPrice){
		firstName = pFName;
		lastName = pLName;
		flavor = pFlavor;
		topping = pTopping;
		servingStyle = pServing;
		price = pPrice;
	}
	
	/**
	 * Getter and setter methods for the IceCreamOrder class
	 * which is used to pass a new record into the SQL Server 
	 */
	public String getFirstName(){
		return this.firstName;
	}
	
	public String getLastName(){
		return this.lastName;
	}
	
	public String getFlavor(){
		return this.flavor;
	}
	public String getTopping(){
		return this.topping;
	}
	public String getServing(){
		return this.servingStyle;
	}
	
	public double getPrice(){
		return this.price;
	}
	
	/**
	 * Overrides the toString method from the Object class
	 * to customize the console output
	 */
	@Override
	public String toString(){
		return ("Name: " + firstName + " " + lastName + "\nFlavor: " + flavor + "\nTopping: " + topping + "\nServing Style: " + servingStyle + "\nPrice: " + NumberFormat.getCurrencyInstance().format(price) + "\n");
	}
}
