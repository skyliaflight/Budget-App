package budgetComponents;

public class Statistic
implements java.io.Serializable
{
	
	static final long serialVersionUID = 10;
	
	private String itemName;
	private double quantity;
	
	public Statistic(String itemName, double quantity){
		this.itemName = itemName;
		this.quantity = quantity;
	}
	
	public String getItemName(){
		return this.itemName;
	}
	
	public double getQuantity(){
		return this.quantity;
	}
	
	public void setItemName(String itemName){
		this.itemName = itemName;
	}
	
	public void setQuantity(double quantity){
		this.quantity = quantity;
	}
	
	// This method has not been rigorously tested.
	public Statistic clone(){
		return new Statistic(this.itemName, this.quantity);
	}
	
}	// End of class
