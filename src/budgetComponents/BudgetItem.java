package budgetComponents;

import java.lang.IllegalArgumentException;
import java.util.ArrayList;
import java.beans.*;
import java.io.IOException;
import java.io.ObjectStreamException;

public class BudgetItem
implements java.io.Serializable
{
	
	static final long serialVersionUID = 10;
	
	private String itemName;
	private double budgeted;
	private double spent;
	private double remainder;
	private PropertyChangeSupport propertyChangeAssistant;
	
	public BudgetItem(){
		this.itemName = "New Item";
		this.budgeted = 0.0;
		this.spent = 0.0;
		this.remainder = 0.0;
		propertyChangeAssistant = new PropertyChangeSupport(this);
	}
	
	public BudgetItem(String itemName, double budgeted, double spent){
		this.itemName = itemName;
		this.budgeted = budgeted;
		this.spent = spent;
		this.remainder = Decimals.round(this.budgeted - this.spent, 2);
		propertyChangeAssistant = new PropertyChangeSupport(this);
	}
	
	public String getName(){
		return this.itemName;
	}
	
	public void setName(String itemName){
		String oldItemName = this.itemName;
		this.itemName = itemName;
		propertyChangeAssistant.firePropertyChange("itemName", oldItemName, itemName);
	}
	
	public double getBudgeted(){
		return this.budgeted;
	}
	
	public void setBudgeted(double budgeted){
		if(budgeted >= 0){
			double oldBudgeted = this.budgeted;
			this.budgeted = budgeted;
			this.remainder = Decimals.round(this.budgeted - this.spent, 2);
			propertyChangeAssistant.firePropertyChange("budgeted", oldBudgeted, budgeted);
		}
		else{
			throw new IllegalArgumentException("Cannot budget a negative amount");
		}
	}
	
	public double getSpent(){
		return this.spent;
	}
	
	public void setSpent(double spent){
		if(spent >= 0){
			double oldSpent = this.spent;
			this.spent = spent;
			this.remainder = Decimals.round(this.budgeted - this.spent, 2);
			propertyChangeAssistant.firePropertyChange("spent", oldSpent, spent);
		}
		else{
			throw new IllegalArgumentException("Cannot spend a negative amount");
		}
	}
	
	public double getRemainder(){
		return this.remainder;
	}
	
	public boolean withinBudget(){
		return this.remainder >= 0;
	}
	
	public void addPropertyChangeListener(String property, PropertyChangeListener listener){
		propertyChangeAssistant.addPropertyChangeListener(property, listener);
	}
	
	public void removePropertyChangeListener(String property, PropertyChangeListener listener){
		propertyChangeAssistant.removePropertyChangeListener(property, listener);
	}
	
	private void writeObject(java.io.ObjectOutputStream out)
	throws IOException
	{ 
		 out.writeObject(this.itemName);
		 out.writeObject(this.budgeted);
		 out.writeObject(this.spent);
	}
	 
	 private void readObject(java.io.ObjectInputStream in)
	 throws IOException, ClassNotFoundException
	 {
		this.propertyChangeAssistant = new PropertyChangeSupport(this);
		this.itemName = (String)in.readObject();
		this.budgeted = (double)in.readObject();
		this.spent = (double)in.readObject();
		this.remainder = Decimals.round(this.budgeted - this.spent, 2);
	 }
	 
	 private void readObjectNoData()
	 throws ObjectStreamException
	 {
		 
	 }
	
}	// End of class
