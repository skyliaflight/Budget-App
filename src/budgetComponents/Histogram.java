package budgetComponents;

import java.lang.Math;
import java.util.ArrayList;
import java.beans.*;

public class Histogram
implements java.io.Serializable
{
	
	static final long serialVersionUID = 10;
	
	private ArrayList<Statistic> items;
	private PropertyChangeSupport propertyChangeAssistant;
	
	public Histogram(){
		propertyChangeAssistant = new PropertyChangeSupport(this);
		this.items = new ArrayList<Statistic>();
	}
	
	public void addItem(String itemName, double quantity){
		Statistic newItem = new Statistic(itemName, quantity);
		this.items.add(newItem);
		propertyChangeAssistant.fireIndexedPropertyChange("itemsSize", items.size()-1, null, items.get(items.size()-1));
	}
	
	public void editItem(int index, String itemName, double quantity){
		Statistic item = this.items.get(index);
		Statistic oldItem = item.clone();
		
		item.setItemName(itemName);
		item.setQuantity(quantity);
		
		propertyChangeAssistant.fireIndexedPropertyChange("items", index, oldItem, item);
	}
	
	public void remove(int index){
		Statistic oldItem = this.items.get(index);
		this.items.remove(index);
		
		try{
			Statistic newItem = this.items.get(index);
			propertyChangeAssistant.fireIndexedPropertyChange("itemsSize", index, oldItem, newItem);
		}
		catch(IndexOutOfBoundsException e){
			propertyChangeAssistant.fireIndexedPropertyChange("itemsSize", index, oldItem, null);
		}
		
	}
	
	public int size(){
		return this.items.size();
	}
	
	public ArrayList<Statistic> getItemsByQuantity(){
		return this.items;
	}
	
	public ArrayList<Statistic> getItemsByPercentage(){
		ArrayList<Statistic> itemsByPercentage = new ArrayList<Statistic>();
		double sumOfStatistics = 0;
		
		for(int k = 0; k < this.items.size(); k++){
			sumOfStatistics += this.items.get(k).getQuantity();
		}
		
		for(int k = 0; k < this.items.size(); k++){
			itemsByPercentage.add(new Statistic(this.items.get(k).getItemName(),
									Math.round(this.items.get(k).getQuantity()/sumOfStatistics*100)));
		}
		
		return itemsByPercentage;
	}
	
	public void addPropertyChangeListener(String property, PropertyChangeListener listener){
		propertyChangeAssistant.addPropertyChangeListener(property, listener);
	}
	
	public void removePropertyChangeListener(String property, PropertyChangeListener listener){
		propertyChangeAssistant.removePropertyChangeListener(property, listener);
	}	
	
}	// End of class
