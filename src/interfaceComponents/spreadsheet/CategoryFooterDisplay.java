package interfaceComponents.spreadsheet;

import java.awt.*;
import javax.swing.*;

public class CategoryFooterDisplay
implements java.io.Serializable
{

	static final long serialVersionUID = 10;	
	static private Color itemColor = new Color(158, 223, 255);
	
	protected JLabel plusLabel;	// Holds the icon which the user can click to add a new item.	
	private JLabel nameLabel;
	private JLabel budgetLabel;
	private JLabel spentLabel;
	private JLabel remainderLabel;
	private JLabel rightLabel;
	
	public CategoryFooterDisplay(){
		plusLabel = new JLabel("+", JLabel.CENTER);
		plusLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		plusLabel.setOpaque(true);
		plusLabel.setBackground(itemColor);	
		plusLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
		plusLabel.setForeground(Color.WHITE);
		
		nameLabel = new JLabel();
		nameLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		nameLabel.setOpaque(true);
		nameLabel.setBackground(itemColor);		
		nameLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
		nameLabel.setForeground(Color.WHITE);
		
		budgetLabel = new JLabel();
		budgetLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		budgetLabel.setOpaque(true);
		budgetLabel.setBackground(itemColor);
		budgetLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
		budgetLabel.setForeground(Color.WHITE);
		
		spentLabel = new JLabel();
		spentLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		spentLabel.setOpaque(true);
		spentLabel.setBackground(itemColor);
		spentLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
		spentLabel.setForeground(Color.WHITE);
		
		remainderLabel = new JLabel();
		remainderLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		remainderLabel.setOpaque(true);
		remainderLabel.setBackground(itemColor);
		remainderLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
		remainderLabel.setForeground(Color.WHITE);
		
		rightLabel = new JLabel();
		rightLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		rightLabel.setOpaque(true);
		rightLabel.setBackground(itemColor);
		rightLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
		rightLabel.setForeground(Color.WHITE);		
	}
	
	/* This requires a container using the GridBagLayout.
	 * The caller gives the container and row index.
	 * The row is expected to have the first six columns
	 * available.
	 */
	public void displayInContainerRow(Container display, int rowIndex){
		display.add(this.plusLabel, new GridBagConstraints(
				0, rowIndex, 1, 1, 1, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0),
				30, 20
		));		
		
		display.add(this.nameLabel, new GridBagConstraints(
				1, rowIndex, 1, 1, 1, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0),
				30, 20
		));
		
		display.add(budgetLabel, new GridBagConstraints(
				2, rowIndex, 1, 1, 1, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0),
				30, 20
		));		
		
		display.add(spentLabel, new GridBagConstraints(
				3, rowIndex, 1, 1, 1, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0),
				30, 20
		));
		
		display.add(remainderLabel, new GridBagConstraints(
				4, rowIndex, 1, 1, 1, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0),
				30, 20
		));		
		
		display.add(rightLabel, new GridBagConstraints(
				5, rowIndex, 1, 1, 1, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0),
				30, 20
		));
	}
	
	public void setVisible(boolean visibility){
		plusLabel.setVisible(visibility);
		nameLabel.setVisible(visibility);
		budgetLabel.setVisible(visibility);
		spentLabel.setVisible(visibility);
		remainderLabel.setVisible(visibility);
		rightLabel.setVisible(visibility);
	}
	
}
