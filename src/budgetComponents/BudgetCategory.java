package budgetComponents;

import java.util.ArrayList;
import java.beans.*;
import java.io.IOException;
import java.io.ObjectStreamException;

public class BudgetCategory
implements PropertyChangeListener, java.io.Serializable
{
	
	static final long serialVersionUID = 10;
	
	private String categoryName;
	private double budgeted;
	private double spent;
	private double remainder;
	private ArrayList<BudgetItem> items;
	private Histogram distributionChart;
	private PropertyChangeSupport propertyChangeAssistant;
	
	public BudgetCategory(){
		propertyChangeAssistant = new PropertyChangeSupport(this);
		this.items = new ArrayList<BudgetItem>();
		this.distributionChart = new Histogram();
		this.categoryName = "New Category";
		this.budgeted = 0;
		this.spent = 0;
		this.updateRemainder();
	}

	public void setName(String categoryName){
		String oldCategoryName = this.categoryName;
		this.categoryName = categoryName;
		propertyChangeAssistant.firePropertyChange("categoryName", oldCategoryName, categoryName);
	}
	
	private void updateRemainder(){
		double oldRemainder = this.remainder;
		this.remainder = this.budgeted - this.spent;
		propertyChangeAssistant.firePropertyChange("remainder", oldRemainder, this.remainder);
	}
	
	public void addItem(BudgetItem item){
		// Track when the item's properties get changed.
		item.addPropertyChangeListener("budgeted", this);
		item.addPropertyChangeListener("spent", this);
		item.addPropertyChangeListener("itemName", this);
		
		// Update our totals.
		double oldSpent = this.spent;	// We will need this down below.
		this.budgeted += item.getBudgeted();
		this.spent += item.getSpent();
		this.updateRemainder();
		
		// Add the item.
		this.items.add(item);
		this.distributionChart.addItem(item.getName(), item.getSpent());
		
		// Let others know that we have changed things.
		propertyChangeAssistant.firePropertyChange("spent", oldSpent, this.spent);
	}
	
	public void deleteItem(int index){
		BudgetItem item = this.items.get(index);
		
		double oldSpent = this.spent;	// We will need this down below.
		this.spent -= item.getSpent();
		this.budgeted -= item.getBudgeted();
		this.updateRemainder();
		
		this.items.remove(index);
		this.distributionChart.remove(index);
		
		// Let others know that we have changed the amount spent.
		propertyChangeAssistant.firePropertyChange("spent", oldSpent, this.spent);		
	}
	
	public String getName(){
		return this.categoryName;
	}
	
	public double getBudgeted(){
		return this.budgeted;
	}
	
	public double getSpent(){
		return this.spent;
	}
	
	public double getRemainder(){
		return Decimals.round(this.remainder, 2);
	}
	
	public BudgetItem getItem(int index){
		return this.items.get(index);
	}
	
	public int length(){
		return this.items.size();
	}
	
	public boolean withinBudget(){
		return this.remainder >= 0;
	}	
	
	public ArrayList<Statistic> getStatsByQuantity(){
		return this.distributionChart.getItemsByQuantity();
	}
	
	public ArrayList<Statistic> getStatsByPercentage(){
		return this.distributionChart.getItemsByPercentage();
	}
	
	// This will take appropriate actions when any of the items
	// within this category get changed.
	public void propertyChange(PropertyChangeEvent e){
		if(e.getSource() instanceof BudgetItem){
			if(e.getPropertyName() == "itemName"){				
				// Changing an item's "itemName" requires we update
				// the distribution chart.
				BudgetItem changedItem = (BudgetItem)e.getSource();
				this.distributionChart.editItem(this.items.indexOf(changedItem), (String)e.getNewValue(), changedItem.getSpent());
			}
			if(e.getPropertyName() == "budgeted"){
				this.budgeted -= (double)e.getOldValue();
				this.budgeted += (double)e.getNewValue();
				this.updateRemainder();
			}
			if(e.getPropertyName() == "spent"){
				double oldSpent = this.spent;	// We will need this old amount below.
				this.spent -= (double)e.getOldValue();
				this.spent += (double)e.getNewValue();
				this.updateRemainder();
				
				// Changing an item's "spent" requires we update the distribution chart.
				BudgetItem changedItem = (BudgetItem)e.getSource();
				this.distributionChart.editItem(this.items.indexOf(changedItem), changedItem.getName(), (double)e.getNewValue());
			
				// Let others know that we have changed the amount spent.
				propertyChangeAssistant.firePropertyChange("spent", oldSpent, this.spent);
			}			
		}
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
		 out.writeObject(this.categoryName);
		 out.writeObject(this.items);
	}
	 
	 private void readObject(java.io.ObjectInputStream in)
	 throws IOException, ClassNotFoundException
	 {
		this.propertyChangeAssistant = new PropertyChangeSupport(this);
		this.categoryName = (String)in.readObject();
		this.budgeted = 0;
		this.spent = 0;
		this.updateRemainder();
		this.items = new ArrayList<BudgetItem>();
		this.distributionChart = new Histogram();
		ArrayList<BudgetItem> savedItems = (ArrayList<BudgetItem>)in.readObject();
		 
		for(int i = 0; i < savedItems.size(); i++){
			this.addItem(savedItems.get(i));
		}
	 }
	 
	 private void readObjectNoData()
	 throws ObjectStreamException
	 {
		 
	 }
	 
}
