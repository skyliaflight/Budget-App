package interfaceComponents.spreadsheet;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.*;
import java.util.ArrayList;
import budgetComponents.*;
import exceptions.SizeException;

public class SpreadSheetDisplay
extends MouseAdapter
implements java.io.Serializable, PropertyChangeListener
{
	
	static final long serialVersionUID = 10;
	
	private Container spreadSheetDisplay;
	private HeaderDisplay categoryHeaders;
	private ArrayList<CategoryDisplay> displayableCategories;
	private BudgetSpreadSheet spreadSheetInfo;
	private PropertyChangeSupport propertyChangeAssistant;
	
	public SpreadSheetDisplay(){
		propertyChangeAssistant = new PropertyChangeSupport(this);
		
		// Create blank spreadsheet
		spreadSheetDisplay = new Container();
		spreadSheetDisplay.setSize(800, 800);
		spreadSheetDisplay.setLayout(new GridBagLayout());
		spreadSheetDisplay.addMouseListener(this);	
		
		// Create the headers
		categoryHeaders = new HeaderDisplay();
		
		// Spreadsheet categories
		displayableCategories = new ArrayList<CategoryDisplay>();
		
		// Information for the spread sheet
		spreadSheetInfo = new BudgetSpreadSheet();
		
		// Display it
		this.updateSpreadSheet();
	}
	
	public SpreadSheetDisplay(BudgetSpreadSheet spreadSheetInfo){	
		propertyChangeAssistant = new PropertyChangeSupport(this);
		
		// Create blank spreadsheet
		spreadSheetDisplay = new Container();
		spreadSheetDisplay.setSize(800, 800);
		spreadSheetDisplay.setLayout(new GridBagLayout());
		spreadSheetDisplay.addMouseListener(this);	
		
		// Create the headers
		categoryHeaders = new HeaderDisplay();
		
		// Spreadsheet category displays
		displayableCategories = new ArrayList<CategoryDisplay>();
		
		// Information for the spread sheet.
		this.spreadSheetInfo = spreadSheetInfo;
		
		// Add appropriate categories.
		for(int i = 0; i < spreadSheetInfo.length(); i++){
			this.addCategoryDisplay(new CategoryDisplay(spreadSheetInfo.getCategory(i)));
		}
		
		// Display it
		this.updateSpreadSheet();
	}
	
	public void userAddCategory(CategoryDisplay displayableCategory){	
		// Use the user input to add the category on the back-end
		spreadSheetInfo.addCategory(displayableCategory.getCategory());
				
		// Now display the category on the front-end
		this.addCategoryDisplay(displayableCategory);
	}
	
	public void userDeleteSelected()
	throws SizeException
	{		
		// Go through the category displays
		for(int i = 0; i < this.displayableCategories.size(); i++){
			// If a category is selected, then delete it and its sub-contents
			if(this.displayableCategories.get(i).isSelected()){
				if(this.displayableCategories.size() <= 1){
					throw new SizeException("Must contain at least one category");
				}
				
				spreadSheetInfo.removeCategory(i);
				displayableCategories.remove(i);
			}
			// Otherwise, delete any selected items in that category display
			else{
				this.displayableCategories.get(i).userDeleteSelected();
			}
			
			this.updateSpreadSheet();
		}
	}
	
	private void addCategoryDisplay(CategoryDisplay displayableCategory){
		displayableCategory.addPropertyChangeListener("spreadSheetSize", this);
		displayableCategories.add(displayableCategory);
		displayableCategory.pointerLabel.addMouseListener(this);
		displayableCategory.footer.plusLabel.addMouseListener(this);	// Add an item and redraw when the user clicks the plus icon
		this.updateSpreadSheet();
	}
	
	private void updateSpreadSheet(){
		// Clear any old contents
		this.spreadSheetDisplay.removeAll();
		
		// Add the headers
		categoryHeaders.displayInContainerRow(spreadSheetDisplay, 0);

		// Track the next available row
		int nextRow = 1;
		
		// Add each category and its sub-contents
		for(int i = 0; i < this.displayableCategories.size(); i++){
			CategoryDisplay currentCategory = displayableCategories.get(i);
			currentCategory.displayInContainerRow(this.spreadSheetDisplay, nextRow);
			nextRow += currentCategory.categoryHeight();	// Next available row depends on the the height of this category
		}
		
		// Ensure that what the user sees is in accordance with the updated spreadsheet
		this.spreadSheetDisplay.revalidate();
	}
	
	public void mouseClicked(MouseEvent e){
		/*if(e.getSource() instanceof JLabel && ((JLabel)e.getSource()).getText().equals("+")){
			this.updateSpreadSheet();
		}*/
		if(e.getSource() instanceof JLabel && ( ((JLabel)e.getSource()).getText().equals(">") || ((JLabel)e.getSource()).getText().equals("v") )){
			spreadSheetDisplay.revalidate();
			propertyChangeAssistant.firePropertyChange("spreadSheetSize", null, null);
		}
	}
	
	public Container getDisplay(){
		return this.spreadSheetDisplay;
	}
	
	public void addPropertyChangeListener(String property, PropertyChangeListener listener){
		propertyChangeAssistant.addPropertyChangeListener(property, listener);
	}
	
	public void removePropertyChangeListener(String property, PropertyChangeListener listener){
		propertyChangeAssistant.removePropertyChangeListener(property, listener);
	}
	
	public void propertyChange(PropertyChangeEvent e){
		if(e.getPropertyName() == "spreadSheetSize"){
			System.out.println("The spreadsheet knows we changed its size.");
			this.updateSpreadSheet();
			propertyChangeAssistant.firePropertyChange("spreadSheetSize", null, null);
		}
	}
	
}

