package interfaceComponents;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import budgetComponents.*;
import exceptions.*;
import java.beans.*;
import java.io.IOException;
import java.io.ObjectStreamException;

import interfaceComponents.chart.ColorIndexDisplay;
import interfaceComponents.chart.PieChartDisplay;
import interfaceComponents.spreadsheet.CategoryDisplay;
import interfaceComponents.spreadsheet.SpreadSheetDisplay;

public class BudgetDisplay
extends MouseAdapter
implements PropertyChangeListener, java.io.Serializable
{
	
	static final long serialVersionUID = 10;

	// Colors
	static Color backgroundColor = new Color(209, 240, 255);
	static Color buttonColor = new Color(115, 199, 255);	
	
	private JPanel mainPanel;
	private BudgetSpreadSheet spreadSheetInfo;
	private JFormattedTextField title;
	private SpreadSheetDisplay figures;
	private PieChartDisplay pieChart;
	private ColorIndexDisplay colorIndex;
	private Button addCategoryButton;
	private Button deleteButton;
	private PropertyChangeSupport propertyChangeAssistant;
	
	public BudgetDisplay(){				
		// Set up the budget
		setUpBudget(new BudgetSpreadSheet());
		
		// One default category
		this.userAddCategory(new CategoryDisplay());
		
		// Add style
		this.applyStyle(buttonColor, Color.WHITE, new Font(Font.SANS_SERIF, Font.BOLD, 25));
	}
	
	public BudgetDisplay(BudgetSpreadSheet spreadSheet){
		// Set up the budget
		setUpBudget(spreadSheet);
		
		// Add style
		this.applyStyle(buttonColor, Color.WHITE, new Font(Font.SANS_SERIF, Font.BOLD, 25));
	}
	
	private void setUpBudget(BudgetSpreadSheet spreadSheetInfo){
		propertyChangeAssistant = new PropertyChangeSupport(this);
		
		// Set up the space in which we will display the contents
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());		
		
		// Budget title
		title = new JFormattedTextField("Enter Title Here");
		title.addPropertyChangeListener("value", this);
		mainPanel.add(title, new GridBagConstraints(
				0, 0, 8, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(40, 40, 40, 40),
				20, 20
		));
		
		// Add the spreadsheet
		this.spreadSheetInfo = spreadSheetInfo;
		figures = new SpreadSheetDisplay(spreadSheetInfo);		
		figures.addPropertyChangeListener("spreadSheetSize", this);
		mainPanel.add(figures.getDisplay(), new GridBagConstraints(
				0, 1, 6, 1, 0, 0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(10, 10, 10, 10),
				20, 20
		));		
				
		// Pie chart
		pieChart = new PieChartDisplay(spreadSheetInfo.getHistogram());
		mainPanel.add(pieChart, new GridBagConstraints(
				6, 1, 1, 1, 0, 0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(10, 20, 10, 10),
				20, 20
		));
		
		// Color index which coordinates with the pie chart
		colorIndex = new ColorIndexDisplay(pieChart.getColorsAndLabels());
		mainPanel.add(colorIndex, new GridBagConstraints(
				7, 1, 1, 1, 0, 0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(10, 10, 10, 10),
				20, 20
		));
		
		// Buttons
		addCategoryButton = new Button("Add Category");
		addCategoryButton.addMouseListener(this);
		mainPanel.add(addCategoryButton, new GridBagConstraints(
				0, 2, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(10, 10, 10, 10),
				20, 20
		));		
		
		deleteButton = new Button("Delete");
		deleteButton.addMouseListener(this);
		mainPanel.add(deleteButton, new GridBagConstraints(
				1, 2, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(10, 10, 10, 10),
				20, 20
		));
	}
	
	private void applyStyle(Color buttonColor, Color fontColor, Font fontStyle){
		mainPanel.setBackground(backgroundColor);
		
		Font titleFont = new Font(fontStyle.getFontName(), fontStyle.getStyle(), fontStyle.getSize()+15);
		title.setOpaque(false);
		title.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		title.setFont(titleFont);
		title.setHorizontalAlignment(JTextField.CENTER);
		
		addCategoryButton.setBackground(buttonColor);
		addCategoryButton.setFont(fontStyle);
		addCategoryButton.setForeground(fontColor);	
		
		deleteButton.setBackground(buttonColor);
		deleteButton.setFont(fontStyle);
		deleteButton.setForeground(fontColor);
	}
	
	private void userAddCategory(CategoryDisplay displayableCategory){
		// Add the interactivity
		//displayableCategory.addPropertyChangeListener("spreadSheetSize", this);
		figures.userAddCategory(displayableCategory);
		
		// Inform the window of this change so it revalidates itself.
		propertyChangeAssistant.firePropertyChange("spreadSheetSize", null, displayableCategory);
	}
	
	public JPanel getDisplay(){
		return this.mainPanel;
	}
	
	public String getTitle(){
		return this.title.getText();
	}
	
	public void mouseClicked(MouseEvent e){
		if(e.getSource() == addCategoryButton){
			this.userAddCategory(new CategoryDisplay());
		}
		
		if(e.getSource() == deleteButton){
			// Delete the selected items in the spread sheet of figures
			try{
				this.figures.userDeleteSelected();
				
				// Inform the window of this change so it revalidates itself.
				propertyChangeAssistant.firePropertyChange("spreadSheetSize", null, null);
			}
			catch(SizeException exception){
				System.out.println("The budget must contain at least one category.");
			}
		}
	}
	
	public void propertyChange(PropertyChangeEvent e){
		if(e.getSource() == title){
			propertyChangeAssistant.firePropertyChange("title", e.getOldValue(), e.getNewValue());
		}
		if(e.getPropertyName().equals("spreadSheetSize")){
			System.out.println("BudgetDisplay knows we changed the size.");
			mainPanel.revalidate();
			propertyChangeAssistant.firePropertyChange("spreadSheetSize", e.getOldValue(), e.getNewValue());
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
		 out.writeObject(title.getText());
		 out.writeObject(spreadSheetInfo);
	 }
	 
	 private void readObject(java.io.ObjectInputStream in)
	 throws IOException, ClassNotFoundException
	 {
		 String titleText = (String)in.readObject();
		 BudgetSpreadSheet spreadSheet = (BudgetSpreadSheet)in.readObject();
		 setUpBudget(spreadSheet);	// It is important to do this before giving the title its text.
		 title.setText(titleText);
		 this.applyStyle(buttonColor, Color.WHITE, new Font(Font.SANS_SERIF, Font.BOLD, 25));
	 }
	 
	 private void readObjectNoData()
	 throws ObjectStreamException
	 {
		 
	 }
	
}
