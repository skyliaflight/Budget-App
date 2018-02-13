package testBudgetComponents;
import static org.junit.Assert.*;

import java.io.*;
import java.util.ArrayList;

import budgetComponents.BudgetSpreadSheet;
import budgetComponents.BudgetCategory;
import budgetComponents.BudgetItem;
import budgetComponents.Decimals;
import budgetComponents.Statistic;

public class BudgetSpreadSheetTest {
	public static void main(String [] args)
	throws IOException, FileNotFoundException, ClassNotFoundException
	{
		// Create a new spreadsheet and add some budget categories.
		BudgetSpreadSheet quarterOneBudget = new BudgetSpreadSheet();
		BudgetCategory category1 = new BudgetCategory();
		BudgetCategory category2 = new BudgetCategory();
		quarterOneBudget.addCategory(category1);
		quarterOneBudget.addCategory(category2);
		
		// Perform tests.
		assertEquals("New Category", quarterOneBudget.getCategory(0).getName());
		assertEquals("New Category", quarterOneBudget.getCategory(1).getName());		
		assertEquals(0, quarterOneBudget.getCategory(0).getBudgeted(), 0);
		assertEquals(0, quarterOneBudget.getCategory(1).getBudgeted(), 0);
		
		// Edit the categories.
		category1.setName("Food");
		category2.setName("Education");
		
		// Perform tests.
		assertEquals("Food", quarterOneBudget.getCategory(0).getName());
		assertEquals("Education", quarterOneBudget.getCategory(1).getName());
		assertEquals(0, quarterOneBudget.getCategory(0).getSpent(), 0);
		assertEquals(0, quarterOneBudget.getCategory(1).getSpent(), 0);
		
		// Budget for items.
		quarterOneBudget.getCategory(0).addItem(new BudgetItem());
		quarterOneBudget.getCategory(0).addItem(new BudgetItem());
		quarterOneBudget.getCategory(0).addItem(new BudgetItem());
		quarterOneBudget.getCategory(1).addItem(new BudgetItem());
		quarterOneBudget.getCategory(1).addItem(new BudgetItem());
		
		quarterOneBudget.getCategory(0).getItem(0).setName("Groceries");
		quarterOneBudget.getCategory(0).getItem(0).setBudgeted(1200);
		quarterOneBudget.getCategory(0).getItem(0).setSpent(339.02);
		quarterOneBudget.getCategory(0).getItem(1).setName("Vitamins");
		quarterOneBudget.getCategory(0).getItem(1).setBudgeted(20);
		quarterOneBudget.getCategory(0).getItem(1).setSpent(24.3);
		quarterOneBudget.getCategory(0).getItem(2).setName("Coconut Water");
		quarterOneBudget.getCategory(0).getItem(2).setBudgeted(21);
		quarterOneBudget.getCategory(0).getItem(2).setSpent(17.85);
		
		quarterOneBudget.getCategory(1).getItem(0).setName("Tuition");
		quarterOneBudget.getCategory(1).getItem(0).setBudgeted(5200);
		quarterOneBudget.getCategory(1).getItem(0).setSpent(5193.12);
		quarterOneBudget.getCategory(1).getItem(1).setName("Books");
		quarterOneBudget.getCategory(1).getItem(1).setBudgeted(203);
		quarterOneBudget.getCategory(1).getItem(1).setSpent(234.16);	
		
		// Check category information.
		assertEquals(1241, quarterOneBudget.getCategory(0).getBudgeted(), 0);
		assertEquals(5403, quarterOneBudget.getCategory(1).getBudgeted(), 0);
		
		assertEquals(381.17, quarterOneBudget.getCategory(0).getSpent(), 0);
		assertEquals(5427.28, quarterOneBudget.getCategory(1).getSpent(), 0);
		
		assertEquals(859.83, quarterOneBudget.getCategory(0).getRemainder(), 0);
		assertEquals(-24.28, quarterOneBudget.getCategory(1).getRemainder(), 0);
		
		assertTrue(quarterOneBudget.getCategory(0).getRemainder() >= 0);
		assertFalse(quarterOneBudget.getCategory(1).getRemainder() >= 0);
		
		// Check some items
		assertEquals("Groceries", quarterOneBudget.getCategory(0).getItem(0).getName());
		assertEquals(339.02, quarterOneBudget.getCategory(0).getItem(0).getSpent(), 0);
		assertEquals("Books", quarterOneBudget.getCategory(1).getItem(1).getName());
		assertEquals(203, quarterOneBudget.getCategory(1).getItem(1).getBudgeted(), 0);
		
		// Write the information to a file before making any further changes.
		File spreadsheetTestFile = new File("SpreadsheetTest");
		ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(spreadsheetTestFile));
		outputStream.writeObject(quarterOneBudget);
		outputStream.close();
		
		// Delete an item.
		quarterOneBudget.getCategory(0).deleteItem(0);
		assertEquals("Vitamins", quarterOneBudget.getCategory(0).getItem(0).getName());
		assertEquals(24.3, quarterOneBudget.getCategory(0).getItem(0).getSpent(), 0);		
		
		// Check the histograms
		ArrayList<Statistic> histogramItems1 = quarterOneBudget.getCategoryStatsByQuantity(0);
		ArrayList<Statistic> histogramItems2 = quarterOneBudget.getCategoryStatsByPercentage(1);
		
		assertEquals("Vitamins", histogramItems1.get(0).getItemName());
		assertEquals(24.3, histogramItems1.get(0).getQuantity(), 0);
		assertEquals("Coconut Water", histogramItems1.get(1).getItemName());
		assertEquals(17.85, histogramItems1.get(1).getQuantity(), 0);		
		
		assertEquals("Tuition", histogramItems2.get(0).getItemName());
		assertEquals(96, histogramItems2.get(0).getQuantity(), 0);
		assertEquals("Books", histogramItems2.get(1).getItemName());
		assertEquals(4, histogramItems2.get(1).getQuantity(), 0);			
		
		// Test the totals
		ArrayList<Statistic> histogramOfTotals1 = quarterOneBudget.getStatsByQuantity();
		ArrayList<Statistic> histogramOfTotals2 = quarterOneBudget.getStatsByPercentage();
		
		assertEquals("Food", histogramOfTotals1.get(0).getItemName());
		assertEquals(42.15, Decimals.round(histogramOfTotals1.get(0).getQuantity(), 2), 0);
		assertEquals(1, Decimals.round(histogramOfTotals2.get(0).getQuantity(), 2), 0);
		assertEquals("Education", histogramOfTotals1.get(1).getItemName());
		assertEquals(5427.28, histogramOfTotals1.get(1).getQuantity(), 0);
		assertEquals(99, Decimals.round(histogramOfTotals2.get(1).getQuantity(), 2), 0);		
		
		// Read the information stored earlier.
		ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(spreadsheetTestFile));
		BudgetSpreadSheet quarterTwoBudget = (BudgetSpreadSheet)inputStream.readObject();
		
		// Check category information.
		assertEquals(1241, quarterTwoBudget.getCategory(0).getBudgeted(), 0);
		assertEquals(5403, quarterTwoBudget.getCategory(1).getBudgeted(), 0);
		
		assertEquals(381.17, quarterTwoBudget.getCategory(0).getSpent(), 0);
		assertEquals(5427.28, quarterTwoBudget.getCategory(1).getSpent(), 0);
		
		assertEquals(859.83, quarterTwoBudget.getCategory(0).getRemainder(), 0);
		assertEquals(-24.28, quarterTwoBudget.getCategory(1).getRemainder(), 0);
		
		assertTrue(quarterTwoBudget.getCategory(0).getRemainder() >= 0);
		assertFalse(quarterTwoBudget.getCategory(1).getRemainder() >= 0);
		
		// Check some items
		assertEquals("Groceries", quarterTwoBudget.getCategory(0).getItem(0).getName());
		assertEquals(339.02, quarterTwoBudget.getCategory(0).getItem(0).getSpent(), 0);
		assertEquals("Books", quarterTwoBudget.getCategory(1).getItem(1).getName());
		assertEquals(203, quarterTwoBudget.getCategory(1).getItem(1).getBudgeted(), 0);
		
		System.out.println("Completed all tests");
	}	
}
