// DEBUG: We have been able to "break" the display through enough playing around.
package interfaceComponents;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.beans.*;
import java.util.ArrayList;
import java.io.*;

public class BudgetWindow
extends WindowAdapter
implements ActionListener, PropertyChangeListener
{
	public static ArrayList<BudgetWindow> openWindows = new ArrayList<BudgetWindow>();
	static private Color backgroundColor = new Color(158, 223, 255);

	final private FileDialog fileSelector;
	private ImageIcon icon;
	private File budgetFile;
	private JFrame mainWindow;
	private JPanel mainPanel;
	private JScrollPane scrollableDisplay;
	private JMenuBar menuBar;
	private JMenu fileMenu, editMenu, moveMenu;
	private JMenuItem save, saveAs, open, close, newFile, print, undo, redo,
					  tabAdder, tabDeleter, moveForward, moveBackward;
	private JTabbedPane budgetFileDisplay;
	private ArrayList<BudgetDisplay> budgets;
	
	public BudgetWindow(){
		setUpMainWindow();
		fileSelector = new FileDialog(mainWindow);
		setUpMenuBar();
		setUpTabbedBudget();
		this.userAddBudget();	// Add one budget display by default.
		openWindows.add(this);	// Add this to the list of open windows.
		mainWindow.setVisible(true);
	}
	
	public BudgetWindow(File budgetFile){
		setUpMainWindow();
		fileSelector = new FileDialog(mainWindow);
		setUpMenuBar();
		setUpTabbedBudget();
		
		// Read the budget information from the file.
		this.budgetFile = budgetFile;
		
		mainWindow.setTitle(budgetFile.getName() + " - " + mainWindow.getTitle());
		
		ObjectInputStream inputStream;
		
		try{
			inputStream = new ObjectInputStream(new FileInputStream(this.budgetFile));			
		
			try{
				while(true){
					userAddBudget((BudgetDisplay)inputStream.readObject());
				}
			}
			catch(EOFException e){
				// If we've been able to read to the end of
				// the file, display the window with the information.
				openWindows.add(this);	// Add this to the list of open windows.
				mainWindow.setVisible(true);
				this.updateTabPaneSize();
			}
			catch(ClassNotFoundException e){
				System.out.println("Error: Could not open the file");
			}
		}
		catch(FileNotFoundException e){
			System.out.println("Error: The file " + budgetFile.getName() + " was not found.");
		}
		catch(IOException e){
			System.out.println("Error: IOException");
		}
	}
	
	private void setUpMainWindow(){
		icon = new ImageIcon("images\\Shoe.PNG");
		
		mainWindow = new JFrame("Shoestring Budgeting");
		mainWindow.setIconImage(icon.getImage());
		mainWindow.setSize(1800, 1000);
		mainWindow.setLayout(new GridBagLayout());
		mainWindow.addWindowListener(this);
		
		mainPanel = new JPanel();
		mainPanel.setBackground(backgroundColor);
		mainPanel.setLayout(new GridBagLayout());
		
		scrollableDisplay = new JScrollPane(mainPanel);
		mainWindow.add(scrollableDisplay, new GridBagConstraints(
				0, 0, 1, 1, 1, 1, GridBagConstraints.NORTH,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0),
				0, 0
		));		
	}
	
	private void setUpMenuBar(){
		Font menuBarFont = new Font(Font.SANS_SERIF, Font.PLAIN, 20);
		UIManager.put("Menu.font", menuBarFont);
		UIManager.put("MenuItem.font", menuBarFont);
		
		menuBar = new JMenuBar();
		fileMenu = new JMenu("File");
		editMenu = new JMenu("Edit");
		moveMenu = new JMenu("Move Tab");
		menuBar.add(fileMenu);
		menuBar.add(editMenu);

		save = new JMenuItem("Save");
		save.addActionListener(this);
		fileMenu.add(save);
		saveAs = new JMenuItem("Save As");
		saveAs.addActionListener(this);
		fileMenu.add(saveAs);
		open = new JMenuItem("Open");
		open.addActionListener(this);
		fileMenu.add(open);
		close = new JMenuItem("Close");
		fileMenu.add(close);
		close.addActionListener(this);
		newFile = new JMenuItem("New");
		newFile.addActionListener(this);
		fileMenu.add(newFile);
		/*print = new JMenuItem("Print");
		fileMenu.add(print);*/
		
		/*undo = new JMenuItem("Undo");
		editMenu.add(undo);
		redo = new JMenuItem("Redo");
		editMenu.add(redo);*/
		tabAdder = new JMenuItem("New Budget Tab");
		tabAdder.addActionListener(this);
		editMenu.add(tabAdder);
		tabDeleter = new JMenuItem("Delete Selected Budget Tab");
		tabDeleter.addActionListener(this);
		editMenu.add(tabDeleter);
		
		editMenu.add(moveMenu);
		moveForward = new JMenuItem("Right");
		moveForward.addActionListener(this);
		moveMenu.add(moveForward);
		moveBackward = new JMenuItem("Left");
		moveBackward.addActionListener(this);
		moveMenu.add(moveBackward);
		
		mainWindow.setJMenuBar(menuBar);
	}
	
	private void setUpTabbedBudget(){
		budgetFileDisplay = new JTabbedPane();
		budgetFileDisplay.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e){
				updateTabPaneSize();
			}
		});
		
		mainPanel.add(budgetFileDisplay, new GridBagConstraints(
				0, 1, 1, 1, 1, 1, GridBagConstraints.NORTH,
				GridBagConstraints.BOTH, new Insets(20, 0, 0, 0),
				0, 0
		));
		
		budgets = new ArrayList<BudgetDisplay>();
	}
	
	// We now need this to apply every time the user adds an item.
	private void updateTabPaneSize(){
		Component selectedTab =   budgetFileDisplay.getSelectedComponent();
		Dimension newDimension = selectedTab.getPreferredSize();
		newDimension.width = newDimension.width + 5;
		newDimension.height = newDimension.height + 45;
		budgetFileDisplay.setPreferredSize(newDimension);
		
		mainPanel.revalidate();
		mainWindow.revalidate();
	}
	
	private void userAddBudget(){
		int index = budgets.size();
		
		budgets.add(new BudgetDisplay());
		budgets.get(index).addPropertyChangeListener("title", this);
		budgets.get(index).addPropertyChangeListener("spreadSheetSize", this);
		
		budgetFileDisplay.addTab("", budgets.get(index).getDisplay());
		budgetFileDisplay.setTabComponentAt(index, new JLabel("  "));
		budgetFileDisplay.getTabComponentAt(index).setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
	}
	
	private void userAddBudget(BudgetDisplay budget){
		int index = budgets.size();
		
		budgets.add(budget);
		budgets.get(index).addPropertyChangeListener("title", this);
		budgets.get(index).addPropertyChangeListener("spreadSheetSize", this);
		
		budgetFileDisplay.addTab("", budget.getDisplay());
		budgetFileDisplay.setTabComponentAt(index, new JLabel(budget.getTitle()));
		budgetFileDisplay.getTabComponentAt(index).setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
	}
	
	private void userRemoveBudget(int index){
		budgets.remove(index);
		budgetFileDisplay.remove(index);
	}
	
	private void userSwitchTabPositions(int index1, int index2){
		BudgetDisplay tempBudget1 = budgets.get(index1);
		Component tempTab1 = budgetFileDisplay.getTabComponentAt(index1);
		Component tempBudgetDisplay1 = budgetFileDisplay.getComponentAt(index1);
		BudgetDisplay tempBudget2 = budgets.get(index2);
		Component tempTab2 = budgetFileDisplay.getTabComponentAt(index2);
		Component tempBudgetDisplay2 = budgetFileDisplay.getComponentAt(index2);
		
		budgets.set(index1, tempBudget2);
		budgets.set(index2, tempBudget1);
		budgetFileDisplay.setTabComponentAt(index1, tempTab2);
		budgetFileDisplay.setTabComponentAt(index2, tempTab1);
		budgetFileDisplay.setComponentAt(index2, new Container());	// We need to temporarily hold the space.
		budgetFileDisplay.setComponentAt(index1, tempBudgetDisplay2);
		budgetFileDisplay.setComponentAt(index2, tempBudgetDisplay1);
	}
	
	// Saves the contents of this BudgetWindow to the given file.
	private void userSaveBudget(File budgetFile){
		// Add the budget displays (which include their sub-contents).
		try{
			ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(budgetFile));
			
			for(int k = 0; k < budgets.size(); k++){
				outputStream.writeObject(budgets.get(k));
			}
			
			outputStream.close();
		}
		catch(IOException e){
			System.out.println(e);
		}
	}
	
	private void userSaveAs(){
		fileSelector.setMode(FileDialog.SAVE);
		fileSelector.setVisible(true);
		
		if(this.budgetFile == null){
			this.budgetFile = new File(fileSelector.getDirectory() + fileSelector.getFile());
			userSaveBudget(this.budgetFile);
		}
		else{
			File newFileToSave = new File(fileSelector.getDirectory() + fileSelector.getFile());
			userSaveBudget(newFileToSave);
			this.budgetFile = newFileToSave;
		}		
	}
	
    @Override
    public void windowClosing(WindowEvent e) {
            this.userCloseWindow();
    }
	
	private void userCloseWindow(){
		BudgetWindow.openWindows.remove(this);
		this.mainWindow.dispose();
		this.mainWindow.dispatchEvent(new WindowEvent(this.mainWindow, WindowEvent.WINDOW_CLOSED));
		if(BudgetWindow.openWindows.isEmpty()){
			System.exit(0);
		}		
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource() instanceof JMenuItem){
			if(e.getSource() == save){
				if(this.budgetFile == null){
					userSaveAs();
				}
				else{
					userSaveBudget(this.budgetFile);
				}
			}
			if(e.getSource() == saveAs){
				userSaveAs();
			}
			if(e.getSource() == open){
				fileSelector.setMode(FileDialog.LOAD);
				fileSelector.setVisible(true);
				File fileToOpen = new File(fileSelector.getDirectory() + fileSelector.getFile());
				new BudgetWindow(fileToOpen);
			}
			if(e.getSource() == close){
				this.userCloseWindow();
			}
			if(e.getSource() == newFile){
				new BudgetWindow();
			}
			if(e.getSource() == tabAdder){
				userAddBudget();
			}
			if(e.getSource() == tabDeleter){
				userRemoveBudget(budgetFileDisplay.getSelectedIndex());
				mainWindow.revalidate();
			}
			if(e.getSource() == moveBackward && budgetFileDisplay.getSelectedIndex() > 0){
				userSwitchTabPositions(budgetFileDisplay.getSelectedIndex()-1, budgetFileDisplay.getSelectedIndex());				
			}
			if(e.getSource() == moveForward && budgetFileDisplay.getSelectedIndex() < budgetFileDisplay.getTabCount()-1){
				userSwitchTabPositions(budgetFileDisplay.getSelectedIndex(), budgetFileDisplay.getSelectedIndex()+1);				
			}
		}
	}
	
	public void propertyChange(PropertyChangeEvent e){
		if(e.getPropertyName() == "title"){
			// Rename the tab when the user changes the title of the budget.
			((JLabel)budgetFileDisplay.getTabComponentAt(budgets.indexOf(e.getSource()))).setText((String)e.getNewValue());
		}
		if(e.getPropertyName() == "spreadSheetSize"){
			this.updateTabPaneSize();
			System.out.println("The BudgetWindow knows a category or item was changed.");
		}
	}
	
	public static void main(String [] args){
		new BudgetWindow();
	}
	
}
