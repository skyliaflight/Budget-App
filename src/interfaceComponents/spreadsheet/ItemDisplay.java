package interfaceComponents.spreadsheet;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import budgetComponents.BudgetItem;
import budgetComponents.Decimals;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

public class ItemDisplay
implements PropertyChangeListener, java.io.Serializable
{

	static final long serialVersionUID = 10;
	
	//static private Color itemColor = new Color(181, 230, 245);
	static private Color itemColor = new Color(158, 223, 255);
	static private DecimalFormat currencyFormatter = new DecimalFormat("#0.00");
	
	private JLabel pointerLabel;	// Just holds the space for an extra column (which in CategoryDisplay rows holds the sub-menu pointer)
	private JFormattedTextField nameField;
	private JFormattedTextField budgetField;
	private JFormattedTextField spentField;
	private JLabel remainderLabel;
	protected JCheckBox selector;
	private BudgetItem itemInfo;
	
	public ItemDisplay(){
		// This the BudgetItem from which the display gets information
		itemInfo = new BudgetItem();
		
		// Create the labels
		pointerLabel = new JLabel("", JLabel.CENTER);	
		
		nameField = new JFormattedTextField(itemInfo.getName());
		nameField.addPropertyChangeListener("value", this);
		
		budgetField = new JFormattedTextField(currencyFormatter);
		budgetField.setValue(itemInfo.getBudgeted());
		budgetField.addPropertyChangeListener("value", this);
		
		spentField = new JFormattedTextField(currencyFormatter);
		spentField.setValue(itemInfo.getSpent());
		spentField.addPropertyChangeListener("value", this);
		
		remainderLabel = new JLabel(currencyFormatter.format(itemInfo.getRemainder()), JLabel.RIGHT);		
		selector = new JCheckBox();
		
		applyStyle(itemColor, Color.WHITE, Color.WHITE, new Font(Font.SANS_SERIF, Font.BOLD, 25));
	}	
	
	public ItemDisplay(BudgetItem item){
		// This the BudgetItem from which the display gets information
		itemInfo = item;
		
		// Create the labels
		pointerLabel = new JLabel("", JLabel.CENTER);	
		
		nameField = new JFormattedTextField(itemInfo.getName());
		nameField.addPropertyChangeListener("value", this);
		
		budgetField = new JFormattedTextField(currencyFormatter);
		budgetField.setValue(itemInfo.getBudgeted());
		budgetField.addPropertyChangeListener("value", this);
		
		spentField = new JFormattedTextField(currencyFormatter);
		spentField.setValue(itemInfo.getSpent());
		spentField.addPropertyChangeListener("value", this);
		
		remainderLabel = new JLabel(currencyFormatter.format(itemInfo.getRemainder()), JLabel.RIGHT);		
		selector = new JCheckBox();
		
		applyStyle(itemColor, Color.WHITE, Color.WHITE, new Font(Font.SANS_SERIF, Font.BOLD, 25));
	}
	
	private void applyStyle(Color backgroundColor, Color borderColor, Color fontColor, Font fontStyle){
		pointerLabel.setBorder(BorderFactory.createLineBorder(borderColor));
		pointerLabel.setOpaque(true);
		pointerLabel.setBackground(backgroundColor);	
		pointerLabel.setFont(fontStyle);
		pointerLabel.setForeground(fontColor);		
		
		nameField.setBorder(BorderFactory.createLineBorder(borderColor));
		nameField.setBackground(backgroundColor);		
		nameField.setFont(fontStyle);
		nameField.setForeground(fontColor);
		
		budgetField.setHorizontalAlignment(JTextField.RIGHT);
		budgetField.setBorder(BorderFactory.createLineBorder(borderColor));
		budgetField.setBackground(backgroundColor);
		budgetField.setFont(fontStyle);
		budgetField.setForeground(fontColor);
		
		spentField.setHorizontalAlignment(JTextField.RIGHT);
		spentField.setBorder(BorderFactory.createLineBorder(borderColor));
		spentField.setBackground(backgroundColor);
		spentField.setFont(fontStyle);
		spentField.setForeground(fontColor);
		
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
	
	/* This requires a container using the GridBagLayout.
	 * The caller gives the container and row index.
	 * The row is expected to have the first six columns
	 * available.
	 */
	public void displayInContainerRow(Container display, int rowIndex){
		display.add(this.pointerLabel, new GridBagConstraints(
				0, rowIndex, 1, 1, 1, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0),
				30, 20
		));	
		
		display.add(nameField, new GridBagConstraints(
				1, rowIndex, 1, 1, 1, 0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
				30, 20
		));
		
		display.add(budgetField, new GridBagConstraints(
				2, rowIndex, 1, 1, 1, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
				30, 20
		));		
		
		display.add(spentField, new GridBagConstraints(
				3, rowIndex, 1, 1, 1, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
				30, 20
		));
		
		display.add(remainderLabel, new GridBagConstraints(
				4, rowIndex, 1, 1, 1, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
				30, 20
		));	
		
		display.add(this.selector, new GridBagConstraints(
				5, rowIndex, 1, 1, 1, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0),
				30, 20
		));
	}
	
	public void setVisible(boolean visibility){
		pointerLabel.setVisible(visibility);
		nameField.setVisible(visibility);
		budgetField.setVisible(visibility);
		spentField.setVisible(visibility);
		remainderLabel.setVisible(visibility);
		selector.setVisible(visibility);
	}
	
	public void select(boolean selected){
		this.selector.setSelected(selected);
	}
	
	public void propertyChange(PropertyChangeEvent e){
		if(e.getSource() == budgetField){			
			if(Decimals.isDouble(budgetField.getText())){
				try{
					itemInfo.setBudgeted(Double.parseDouble(budgetField.getText()));
					remainderLabel.setText(currencyFormatter.format(itemInfo.getRemainder()));
				}
				catch(IllegalArgumentException exception){
					budgetField.setValue(itemInfo.getBudgeted());
				}
			}
			else{
				budgetField.setValue(itemInfo.getBudgeted());
			}
		}
		
		if(e.getSource() == spentField){
			if(Decimals.isDouble(spentField.getText())){
				try{
					itemInfo.setSpent(Double.parseDouble(spentField.getText()));
					remainderLabel.setText(currencyFormatter.format(itemInfo.getRemainder()));
				}
				catch(IllegalArgumentException exception){
					spentField.setValue(itemInfo.getSpent());
				}
			}
			else{
				spentField.setValue(itemInfo.getSpent());
			}
		}
		
		if(e.getSource() == nameField){
			itemInfo.setName(nameField.getText());
		}
	}
	
	public boolean isSelected(){
		return this.selector.isSelected();
	}
	
	public BudgetItem getItem(){
		return this.itemInfo;
	}

}
