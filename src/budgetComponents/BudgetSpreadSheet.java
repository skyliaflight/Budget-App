// This spreadsheet must learn to update the histogram when it
// senses a category got changed.

package budgetComponents;
import java.util.ArrayList;
import java.beans.*;

public class BudgetSpreadSheet
implements PropertyChangeListener, java.io.Serializable
{
	
	static final long serialVersionUID = 10;
	
	private String spreadSheetName;
	private double budgeted;
	private double spent;
	private double remainder;
	private Histogram distributionChart;
	private ArrayList<BudgetCategory> categories;
	
	public BudgetSpreadSheet(){
		this.categories = new ArrayList<BudgetCategory>();
		this.distributionChart = new Histogram();
		this.spreadSheetName = "New";
		this.budgeted = 0;
		this.spent = 0;
		this.remainder = 0;	
		
		this.distributionChart.addPropertyChangeListener("items", this);
		this.distributionChart.addPropertyChangeListener("itemsSize", this);
	}
	
	public void setName(String spreadSheetName){
		this.spreadSheetName = spreadSheetName;
	}
	
	public String getName(){
		return this.spreadSheetName;
	}
	
	public double getBudgeted(){
		return this.budgeted;
	}
	
	public double getSpent(){
		return this.spent;
	}
	
	public double getRemainder(){
		return this.remainder;
	}
	
	public boolean withinBudget(){
		return this.remainder >= 0;
	}
	
	public int length(){
		return this.categories.size();
	}
	
	// This method has not been rigorously tested.
	public void addCategory(BudgetCategory category){
		// Note to listen for changes in the category
		category.addPropertyChangeListener("categoryName", this);
		category.addPropertyChangeListener("spent", this);
		
		// Add it to the array list
		categories.add(category);
		
		// Update our totals
		this.budgeted += category.getBudgeted();
		this.spent += category.getBudgeted();
		this.remainder = this.budgeted - this.spent;		
		
		// Update the distribution chart
		this.distributionChart.addItem(category.getName(), category.getSpent());
	}
	
	public void removeCategory(int categoryIndex){
		BudgetCategory category = this.categories.get(categoryIndex);
		
		this.budgeted -= category.getBudgeted();
		this.spent -= category.getBudgeted();
		this.remainder = this.budgeted - this.spent;
		
		this.categories.remove(categoryIndex);
		this.distributionChart.remove(categoryIndex);
	}	
	
	public BudgetCategory getCategory(int categoryIndex){
		return this.categories.get(categoryIndex);
	}
	
	public ArrayList<Statistic> getStatsByQuantity(){
		return this.distributionChart.getItemsByQuantity();
	}
	
	public ArrayList<Statistic> getStatsByPercentage(){
		return this.distributionChart.getItemsByPercentage();
	}	
	
	public ArrayList<Statistic> getCategoryStatsByQuantity(int categoryIndex){
		return this.categories.get(categoryIndex).getStatsByQuantity();
	}
	
	public ArrayList<Statistic> getCategoryStatsByPercentage(int categoryIndex){
		return this.categories.get(categoryIndex).getStatsByPercentage();
	}	
	
	public Histogram getHistogram(){
		return this.distributionChart;
	}
	
	public void propertyChange(PropertyChangeEvent e){
		if(e.getSource() instanceof BudgetCategory){
			BudgetCategory changedCategory = (BudgetCategory)e.getSource();			
			this.distributionChart.editItem(categories.indexOf(changedCategory), changedCategory.getName(), changedCategory.getSpent());
		}
	}
	
}
