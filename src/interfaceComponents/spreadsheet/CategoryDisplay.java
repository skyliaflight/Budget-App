// We need to track property changes from the CategoryDisplay
// so we can accordingly update its totals. Try adding
// property change listeners to the category from which
// we get the data.

package interfaceComponents.spreadsheet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeEvent;
import budgetComponents.*;

public class CategoryDisplay
extends MouseAdapter
implements PropertyChangeListener, java.io.Serializable
{
	
	static final long serialVersionUID = 10;
	static private Color itemColor = new Color(115, 199, 255);
	static private DecimalFormat currencyFormatter = new DecimalFormat("#0.00");
	
	protected JLabel pointerLabel;	// Holds the pointer indicating when the submenu is open.
	private JFormattedTextField nameField;
	private JLabel budgetLabel;
	private JLabel spentLabel;
	private JLabel remainderLabel;
	private JCheckBox selector;
	protected CategoryFooterDisplay footer;
	private boolean displayItems;
	private ArrayList<ItemDisplay> displayableItems;
	private BudgetCategory categoryInfo;
	private PropertyChangeSupport propertyChangeAssistant;
	
	public CategoryDisplay(){
		// This is the category from which the display will get information.
		// When we create a new ItemDisplay, we must add its item to this
		// category.
		categoryInfo = new BudgetCategory();
		categoryInfo.addPropertyChangeListener("itemName", this);
		categoryInfo.addPropertyChangeListener("remainder", this);
		categoryInfo.addPropertyChangeListener("numberOfBudgetedItems", this);
		
		// Create the list of ItemDisplays.
		displayableItems = new ArrayList<ItemDisplay>();
		
		// Create the labels.
		pointerLabel = new JLabel("v", JLabel.CENTER);
		pointerLabel.addMouseListener(this);
		
		nameField = new JFormattedTextField("New Category");
		nameField.addPropertyChangeListener("value", this);
		
		budgetLabel = new JLabel("$0.00");		
		spentLabel = new JLabel("$0.00");	
		remainderLabel = new JLabel("$0.00", JLabel.RIGHT);
		
		selector = new JCheckBox();
		selector.addMouseListener(this);
		
		// Create the propertyChangeAssistant
		this.propertyChangeAssistant = new PropertyChangeSupport(this);
		
		// Apply the style
		this.applyStyle(itemColor, Color.WHITE, Color.WHITE, new Font(Font.SANS_SERIF, Font.BOLD, 25));
		
		// Contains one ItemDisplay by default
		this.userAddItem(new ItemDisplay());
		
		// The bottom row containing the icon which the user
		// can click to add a new item.
		footer = new CategoryFooterDisplay();
		footer.plusLabel.addMouseListener(this);
		
		// We will have it visible by default
		displayItems = true;
	}
	
	// This constructor is incomplete and is a part of the plan to
	// break down the budget into its components and match each with
	// a display, starting from the SpreadSheetDisplay at the top.
	public CategoryDisplay(BudgetCategory category){
		// This is the category from which the display will get information.
		// When we create a new ItemDisplay, we must add its item to this
		// category.
		categoryInfo = category;
		categoryInfo.addPropertyChangeListener("itemName", this);
		categoryInfo.addPropertyChangeListener("remainder", this);
		categoryInfo.addPropertyChangeListener("numberOfBudgetedItems", this);
		
		// Create the list of ItemDisplays.
		displayableItems = new ArrayList<ItemDisplay>();
		
		// Create the labels.
		pointerLabel = new JLabel("v", JLabel.CENTER);
		pointerLabel.addMouseListener(this);
		
		nameField = new JFormattedTextField(category.getName());
		nameField.addPropertyChangeListener("value", this);
		
		budgetLabel = new JLabel(currencyFormatter.format(categoryInfo.getBudgeted()));		
		spentLabel = new JLabel(currencyFormatter.format(categoryInfo.getSpent()));	
		remainderLabel = new JLabel(currencyFormatter.format(categoryInfo.getRemainder()), JLabel.RIGHT);
		
		selector = new JCheckBox();
		selector.addMouseListener(this);
		
		// Create the propertyChangeAssistant
		this.propertyChangeAssistant = new PropertyChangeSupport(this);
		
		// Apply the style
		this.applyStyle(itemColor, Color.WHITE, Color.WHITE, new Font(Font.SANS_SERIF, Font.BOLD, 25));
		
		// Add the ItemDisplays
		for(int i = 0; i < category.length(); i++){
			this.addItemDisplay(new ItemDisplay(category.getItem(i)));
		}
		
		// The bottom row containing the icon which the user
		// can click to add a new item.
		footer = new CategoryFooterDisplay();
		footer.plusLabel.addMouseListener(this);
		
		// We will have it visible by default
		displayItems = true;
	}
	
	private void applyStyle(Color backgroundColor, Color borderColor, Color fontColor, Font fontStyle){
		// Style the labels and text fields
		pointerLabel.setBorder(BorderFactory.createLineBorder(borderColor));
		pointerLabel.setOpaque(true);
		pointerLabel.setBackground(backgroundColor);	
		pointerLabel.setFont(fontStyle);
		pointerLabel.setForeground(fontColor);
		
		nameField.setBorder(BorderFactory.createLineBorder(borderColor));
		nameField.setBackground(backgroundColor);		
		nameField.setFont(fontStyle);
		nameField.setForeground(fontColor);
		
		budgetLabel.setHorizontalAlignment(JTextField.RIGHT);
		budgetLabel.setBorder(BorderFactory.createLineBorder(borderColor));
		budgetLabel.setOpaque(true);
		budgetLabel.setBackground(backgroundColor);
		budgetLabel.setFont(fontStyle);
		budgetLabel.setForeground(fontColor);
		
		spentLabel.setHorizontalAlignment(JTextField.RIGHT);
		spentLabel.setBorder(BorderFactory.createLineBorder(borderColor));
		spentLabel.setOpaque(true);
		spentLabel.setBackground(backgroundColor);
		spentLabel.setFont(fontStyle);
		spentLabel.setForeground(fontColor);
		
		remainderLabel.setBorder(BorderFactory.createLineBorder(borderColor));
		remainderLabel.setOpaque(true);
		remainderLabel.setBackground(backgroundColor);
		remainderLabel.setFont(fontStyle);
		remainderLabel.setForeground(fontColor);
		
		selector.setHorizontalAlignment(JCheckBox.CENTER);
		selector.setBorder(BorderFactory.createLineBorder(borderColor));
		selector.setBorderPainted(true);
		selector.setBackground(backgroundColor);		
	}
	
	/*The container in which we display this category
	 *is assumed to have the given row available and to
	 *also have enough rows for the full height (determined
	 *by the categoryHeight method) of this category.
	 *The container should also have columns 0-6 available.*/
	public void displayInContainerRow(Container spreadSheetDisplay, int rowIndex){			
		// Display the category in the container.
		spreadSheetDisplay.add(this.pointerLabel, new GridBagConstraints(
				0, rowIndex, 1, 1, 1, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0),
				30, 20
		));
		
		spreadSheetDisplay.add(this.nameField, new GridBagConstraints(
				1, rowIndex, 1, 1, 1, 0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
				30, 20
		));
		
		spreadSheetDisplay.add(budgetLabel, new GridBagConstraints(
				2, rowIndex, 1, 1, 1, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
				30, 20
		));		
		
		spreadSheetDisplay.add(spentLabel, new GridBagConstraints(
				3, rowIndex, 1, 1, 1, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
				30, 20
		));
		
		spreadSheetDisplay.add(remainderLabel, new GridBagConstraints(
				4, rowIndex, 1, 1, 1, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
				30, 20
		));		
		
		spreadSheetDisplay.add(this.selector, new GridBagConstraints(
				5, rowIndex, 1, 1, 1, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0),
				30, 20
		));
		
		// Display the items in the container (if they are visible).
		for(int k = 0; k < this.displayableItems.size(); k++){
			this.displayableItems.get(k).displayInContainerRow(spreadSheetDisplay, rowIndex+k+1);
		}
		
		footer.displayInContainerRow(spreadSheetDisplay, rowIndex + this.displayableItems.size() + 1);
	
	}
	
	public void toggleItemDisplay(){	
		if(this.displayItems == true){
			for(int k = 0; k < this.displayableItems.size(); k++){
				this.displayableItems.get(k).setVisible(false);
			}		
			
			this.footer.setVisible(false);
			
			this.pointerLabel.setText(">");
			this.displayItems = false;
		}
		else{
			for(int k = 0; k < this.displayableItems.size(); k++){
				this.displayableItems.get(k).setVisible(true);
			}				
			
			this.footer.setVisible(true);
			
			this.pointerLabel.setText("v");
			this.displayItems = true;
		}		
	}
	
	private void userAddItem(ItemDisplay displayableItem){
		// Use the user input to add the item on the back end.
		categoryInfo.addItem(displayableItem.getItem());
		
		// Now display the item on the front end.
		this.addItemDisplay(displayableItem);
	}
	
	public void userDeleteSelected(){
		// Go through the item displays.
		for(int i = 0; i < this.displayableItems.size(); i++){
			// If an item is selected, then delete it.
			if(this.displayableItems.get(i).isSelected()){
				categoryInfo.deleteItem(i);
				displayableItems.remove(i);
			}
		}
	}
	
	private void addItemDisplay(ItemDisplay displayableItem){
		// Display the item on the front end.
		displayableItem.selector.addMouseListener(this);
		displayableItems.add(displayableItem);
		
		// Update the category's labels.
		this.updateCategoryDisplay();
		
		// Inform others of the change.
		propertyChangeAssistant.firePropertyChange("spreadSheetSize", displayableItems.size()-1, displayableItems.size());
	}
	
	public void mouseClicked(MouseEvent e){
		if(e.getSource() == this.pointerLabel){
			this.toggleItemDisplay();
		}
		
		if(e.getSource() == this.footer.plusLabel){
			this.userAddItem(new ItemDisplay());
		}
		
		if(e.getSource() == this.selector){
			for(int i = 0; i < displayableItems.size(); i++){
				displayableItems.get(i).select(selector.isSelected());
			}
		}
		// If it is not the category selector  but is checkbox,
		// then it is the checkbox of one of the submenu items.
		// Uncheck the category checkbox the moment the user
		// starts selecting or deselecting individual items.
		else if(e.getSource() instanceof JCheckBox){
			this.selector.setSelected(false);
		}
		
	}
	
	public void propertyChange(PropertyChangeEvent e){
		if(e.getSource() == categoryInfo && e.getPropertyName() == "remainder"){
			this.updateCategoryDisplay();
		}
		if(e.getSource() == nameField){
			this.categoryInfo.setName((String)e.getNewValue());
		}
		
		this.propertyChangeAssistant.firePropertyChange("spreadSheetSize", e.getOldValue(), e.getNewValue());
	}
	
	public void addPropertyChangeListener(String property, PropertyChangeListener listener){
		propertyChangeAssistant.addPropertyChangeListener(property, listener);
	}
	
	public void removePropertyChangeListener(String property, PropertyChangeListener listener){
		propertyChangeAssistant.removePropertyChangeListener(property, listener);
	}	
	
	private void updateCategoryDisplay(){
		this.budgetLabel.setText(currencyFormatter.format(categoryInfo.getBudgeted()));
		this.spentLabel.setText(currencyFormatter.format(categoryInfo.getSpent()));
		this.remainderLabel.setText(currencyFormatter.format(categoryInfo.getRemainder()));
	}
	
	/*The height includes the number of rows which is the
	 *number of item displays plus 2 for the category header
	 *and footer.*/
	public int categoryHeight(){
		return displayableItems.size() + 2;
	}
	
	public boolean isSelected(){
		return selector.isSelected();
	}
	
	// None of the below methods have been rigorously tested.
	public BudgetCategory getCategory(){
		return this.categoryInfo;
	}

}

