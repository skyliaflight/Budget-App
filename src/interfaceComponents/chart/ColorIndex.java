package interfaceComponents.chart;

import java.awt.Color;
import java.util.ArrayList;
import java.beans.*;

public class ColorIndex
implements java.io.Serializable
{
	
	static final long serialVersionUID = 10;
	
	ArrayList<ColorIndexPair> colorsAndLabels;
	PropertyChangeSupport propertyChangeAssistant;
	
	public ColorIndex(){
		colorsAndLabels = new ArrayList<ColorIndexPair>();
		propertyChangeAssistant = new PropertyChangeSupport(this);
	}
	
	public void addColorIndexPair(Color itemColor, String label){
		if(!this.contains(itemColor)){
			ColorIndexPair temp = new ColorIndexPair(itemColor, label);
			colorsAndLabels.add(temp);
			propertyChangeAssistant.fireIndexedPropertyChange("colorIndexSize", colorsAndLabels.size()-1, null, temp);
		}
		else{
			throw new IllegalArgumentException("ColorIndex cannot contain the same color twice");
		}
	}
	
	public void editColorIndexPair(int index, String label){
		ColorIndexPair colorIndexPair = colorsAndLabels.get(index);
		ColorIndexPair oldColorIndexPair = colorIndexPair.clone();
		colorIndexPair.label = label;
		propertyChangeAssistant.fireIndexedPropertyChange("colorsAndLabels", index, oldColorIndexPair, colorIndexPair);
	}
	
	public void removeColorIndexPair(int index){
		ColorIndexPair removedPair = colorsAndLabels.get(index);
		colorsAndLabels.remove(index);
		
		if(index < this.length()){
			propertyChangeAssistant.fireIndexedPropertyChange("colorIndexSize", index, removedPair, colorsAndLabels.get(index));
		}
		else{
			propertyChangeAssistant.fireIndexedPropertyChange("colorIndexSize", index, removedPair, null);			
		}
	}
	
	public ColorIndexPair getColorIndexPair(int index){
		return colorsAndLabels.get(index);
	}
	
	public int length(){
		return colorsAndLabels.size();
	}
	
	// This will return true if this color index contains
	// the following color at least once.
	public boolean contains(Color itemColor){
		for(int i = 0; i < colorsAndLabels.size(); i++){
			ColorIndexPair currentPair = colorsAndLabels.get(i);
			
			if(currentPair.itemColor == itemColor){
				return true;
			}
		}
		
		return false;
	}
	
	public void addPropertyChangeListener(String property, PropertyChangeListener listener){
		propertyChangeAssistant.addPropertyChangeListener(property, listener);
	}
	
	public void removePropertyChangeListener(String property, PropertyChangeListener listener){
		propertyChangeAssistant.removePropertyChangeListener(property, listener);
	}
	
	public class ColorIndexPair
	implements java.io.Serializable
	{
		
		static final long serialVersionUID = 10;
		
		public Color itemColor;
		public String label;
		
		public ColorIndexPair(Color itemColor, String label){
			this.itemColor = itemColor;
			this.label = label;
		}
		
		public ColorIndexPair clone(){
			return new ColorIndexPair(itemColor, label);
		}
		
	}
	
}
