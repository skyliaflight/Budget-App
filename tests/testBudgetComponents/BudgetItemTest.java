package testBudgetComponents;
import static org.junit.Assert.*;
import java.io.*;
//import org.junit.Test;

import budgetComponents.BudgetItem;

public class BudgetItemTest {
	public static void main(String [] args)
	throws IOException, ClassNotFoundException
	{
		// Test the budget entry created with the first constructor
		BudgetItem budgetEntry1 = new BudgetItem();
		budgetEntry1.setName("Shirt");
		assertEquals("Shirt", budgetEntry1.getName());
		assertEquals(0, budgetEntry1.getBudgeted(), 0);
		assertEquals(0, budgetEntry1.getSpent(), 0);
		assertEquals(0, budgetEntry1.getRemainder(), 0);
		assertTrue(budgetEntry1.withinBudget());
		
		budgetEntry1.setName("White Blouse");
		budgetEntry1.setBudgeted(35);
		budgetEntry1.setSpent(37.5);
		
		assertEquals("White Blouse", budgetEntry1.getName());
		assertEquals(35, budgetEntry1.getBudgeted(), 0);
		assertEquals(37.5, budgetEntry1.getSpent(), 0);
		assertEquals(-2.5, budgetEntry1.getRemainder(), 0);
		assertFalse(budgetEntry1.withinBudget());
		
		budgetEntry1.setBudgeted(38);
		
		assertEquals(38, budgetEntry1.getBudgeted(), 0);
		assertEquals(0.5, budgetEntry1.getRemainder(), 0);
		assertTrue(budgetEntry1.withinBudget());			
		
		// Test the budget entry created with the second constructor
		BudgetItem budgetEntry2 = new BudgetItem("Shoes", 40, 37.39);
		assertEquals("Shoes", budgetEntry2.getName());
		assertEquals(40, budgetEntry2.getBudgeted(), 0);
		assertEquals(37.39, budgetEntry2.getSpent(), 0);
		assertEquals(2.61, budgetEntry2.getRemainder(), 0);
		assertTrue(budgetEntry2.withinBudget());	
		
		// Store this BudgetItem's current information in a file.
		File budgetItemTestFile = new File("BudgetItemTest");
		ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(budgetItemTestFile));
		outputStream.writeObject(budgetEntry1);
		outputStream.writeObject(budgetEntry2);
		outputStream.close();
		
		ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(budgetItemTestFile));
		BudgetItem budgetEntry3 = (BudgetItem)inputStream.readObject();
		BudgetItem budgetEntry4 = (BudgetItem)inputStream.readObject();
		
		assertEquals("White Blouse", budgetEntry3.getName());
		assertEquals(38, budgetEntry3.getBudgeted(), 0);
		assertEquals(37.5, budgetEntry3.getSpent(), 0);
		assertEquals(0.5, budgetEntry3.getRemainder(), 0);
		assertTrue(budgetEntry3.withinBudget());
		
		assertEquals("Shoes", budgetEntry4.getName());
		assertEquals(40, budgetEntry4.getBudgeted(), 0);
		assertEquals(37.39, budgetEntry4.getSpent(), 0);
		assertEquals(2.61, budgetEntry4.getRemainder(), 0);
		assertTrue(budgetEntry4.withinBudget());
		
		// Inform the user of the test's completion
		System.out.println("Testing completed");
		
	}
}
