package testBudgetComponents;
import static org.junit.Assert.*;

import java.io.*;
import java.util.ArrayList;

import budgetComponents.BudgetCategory;
import budgetComponents.BudgetItem;
import budgetComponents.Statistic;

public class BudgetCategoryTest {
	public static void main(String [] args)
	throws IOException, ClassNotFoundException
	{
		// Create some budget categories.
		BudgetCategory food = new BudgetCategory();
		BudgetCategory education = new BudgetCategory();
		education.setName("Education");
		
		// Add some items and give them values
		BudgetItem item1 = new BudgetItem();
		BudgetItem item2 = new BudgetItem();
		BudgetItem item3 = new BudgetItem();
		food.addItem(item1);
		food.addItem(item2);
		food.addItem(item3);
		item1.setName("Groceries");
		item1.setBudgeted(1200);
		item1.setSpent(339.02);
		item2.setName("Vitamins");
		item2.setBudgeted(20);
		item2.setSpent(24.3);
		item3.setName("Coconut Water");
		item3.setBudgeted(21);
		item3.setSpent(17.85);	

		BudgetItem item4 = new BudgetItem();
		BudgetItem item5 = new BudgetItem();
		education.addItem(item4);
		education.addItem(item5);
		item4.setName("Tuition");
		item4.setBudgeted(5200);
		item4.setSpent(5193.12);
		item5.setName("Books");
		item5.setBudgeted(203);
		item5.setSpent(234.16);
		
		// Check the information on the categories
		assertEquals("New Category", food.getName());
		assertEquals("Education", education.getName());
		food.setName("Food");
		assertEquals("Food", food.getName());
		
		assertEquals(3, food.length(), 0);
		assertEquals(2, education.length(), 0);
		
		assertEquals(1241, food.getBudgeted(), 0);
		assertEquals(5403, education.getBudgeted(), 0);
		
		assertEquals(381.17, food.getSpent(), 0);
		assertEquals(5427.28, education.getSpent(), 0);
		
		assertEquals(859.83, food.getRemainder(), 0);
		assertEquals(-24.28, education.getRemainder(), 0);
		
		assertTrue(food.withinBudget());
		assertFalse(education.withinBudget());
		
		// Check some items
		assertEquals("Groceries", food.getItem(0).getName());
		assertEquals(339.02, food.getItem(0).getSpent(), 0);
		assertEquals("Books", education.getItem(1).getName());
		assertEquals(203, education.getItem(1).getBudgeted(), 0);
		
		// Write the information to a file before making any further changes.
		File budgetCategoryTestFile = new File("BudgetCategoryTest");
		ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(budgetCategoryTestFile));
		outputStream.writeObject(food);
		outputStream.writeObject(education);
		outputStream.close();
		
		// Delete an item.
		food.deleteItem(0);
		assertEquals("Vitamins", food.getItem(0).getName());
		assertEquals(24.3, food.getItem(0).getSpent(), 0);
		
		// Check the histograms
		ArrayList<Statistic> histogramItems1 = food.getStatsByQuantity();
		ArrayList<Statistic> histogramItems2 = education.getStatsByPercentage();
		
		assertEquals("Vitamins", histogramItems1.get(0).getItemName());
		assertEquals(24.3, histogramItems1.get(0).getQuantity(), 0);
		assertEquals("Coconut Water", histogramItems1.get(1).getItemName());
		assertEquals(17.85, histogramItems1.get(1).getQuantity(), 0);		
		
		assertEquals("Tuition", histogramItems2.get(0).getItemName());
		assertEquals(96, histogramItems2.get(0).getQuantity(), 0);
		assertEquals("Books", histogramItems2.get(1).getItemName());
		assertEquals(4, histogramItems2.get(1).getQuantity(), 0);	
		
		// Read the information stored earlier.
		ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(budgetCategoryTestFile));
		BudgetCategory food2 = (BudgetCategory)inputStream.readObject();
		BudgetCategory education2 = (BudgetCategory)inputStream.readObject();
		
		// Check the information on the categories
		assertEquals("Food", food2.getName());
		assertEquals("Education", education2.getName());
		
		assertEquals(3, food2.length(), 0);
		assertEquals(2, education2.length(), 0);
		
		assertEquals(1241, food2.getBudgeted(), 0);
		assertEquals(5403, education2.getBudgeted(), 0);
		
		assertEquals(381.17, food2.getSpent(), 0);
		assertEquals(5427.28, education2.getSpent(), 0);
		
		assertEquals(859.83, food2.getRemainder(), 0);
		assertEquals(-24.28, education2.getRemainder(), 0);
		
		assertTrue(food2.withinBudget());
		assertFalse(education2.withinBudget());
		
		// Check some items
		assertEquals("Groceries", food2.getItem(0).getName());
		assertEquals(339.02, food2.getItem(0).getSpent(), 0);
		assertEquals("Books", education2.getItem(1).getName());
		assertEquals(203, education2.getItem(1).getBudgeted(), 0);
		
		System.out.println("Completed all tests");
	}
}
