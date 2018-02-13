package interfaceComponents.spreadsheet;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class HeaderDisplay
implements java.io.Serializable
{

	static final long serialVersionUID = 10;
	static Color headerColor = new Color(56, 189, 255);
	
	private JLabel pointerHeader;	// Just holds the space for an extra column (which in CategoryDisplay rows holds the sub-menu pointer)
	private JLabel nameHeader;
	private JLabel budgetHeader;
	private JLabel spentHeader;
	private JLabel remainderHeader;
	private JLabel selecterHeader;	// Just holds the space for an extra column (which contains the checkboxes for selecting items)
	
	public HeaderDisplay(){
		pointerHeader = new JLabel("", JLabel.CENTER);
		pointerHeader.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		pointerHeader.setOpaque(true);
		pointerHeader.setBackground(headerColor);	
		pointerHeader.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
		pointerHeader.setForeground(Color.WHITE);		
		
		nameHeader = new JLabel("Category");
		nameHeader.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		nameHeader.setOpaque(true);
		nameHeader.setBackground(headerColor);		
		nameHeader.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
		nameHeader.setForeground(Color.WHITE);
		
		budgetHeader = new JLabel("Budget");
		budgetHeader.setHorizontalAlignment(JTextField.RIGHT);
		budgetHeader.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		budgetHeader.setOpaque(true);
		budgetHeader.setBackground(headerColor);
		budgetHeader.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
		budgetHeader.setForeground(Color.WHITE);
		
		spentHeader = new JLabel("Spent");
		spentHeader.setHorizontalAlignment(JTextField.RIGHT);
		spentHeader.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		spentHeader.setOpaque(true);
		spentHeader.setBackground(headerColor);
		spentHeader.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
		spentHeader.setForeground(Color.WHITE);
		
		remainderHeader = new JLabel("Remainder", JLabel.RIGHT);
		remainderHeader.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		remainderHeader.setOpaque(true);
		remainderHeader.setBackground(headerColor);
		remainderHeader.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
		remainderHeader.setForeground(Color.WHITE);
		
		selecterHeader = new JLabel();
		selecterHeader.setHorizontalAlignment(JCheckBox.CENTER);
		selecterHeader.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		selecterHeader.setOpaque(true);
		selecterHeader.setBackground(headerColor);
	}	
	
	/* This requires a container using the GridBagLayout.
	 * The caller gives the container and row index.
	 * The row is expected to have the first five columns
	 * available.
	 */
	public void displayInContainerRow(Container display, int rowIndex){
		display.add(this.pointerHeader, new GridBagConstraints(
				0, rowIndex, 1, 1, 1, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0),
				30, 20
		));		
		
		display.add(nameHeader, new GridBagConstraints(
				1, rowIndex, 1, 1, 1, 0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
				30, 20
		));
		
		display.add(budgetHeader, new GridBagConstraints(
				2, rowIndex, 1, 1, 1, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
				30, 20
		));		
		
		display.add(spentHeader, new GridBagConstraints(
				3, rowIndex, 1, 1, 1, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
				30, 20
		));
		
		display.add(remainderHeader, new GridBagConstraints(
				4, rowIndex, 1, 1, 1, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
				30, 20
		));		
		
		display.add(this.selecterHeader, new GridBagConstraints(
				5, rowIndex, 1, 1, 1, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0),
				30, 20
		));
	}
	
}
