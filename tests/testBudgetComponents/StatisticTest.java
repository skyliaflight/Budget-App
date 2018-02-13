package testBudgetComponents;

import java.io.*;
import budgetComponents.Statistic;
import static org.junit.Assert.assertEquals;

public class StatisticTest {
	public static void testStatistic()
	throws IOException, ClassNotFoundException
	{
		// Create the statistics.
		Statistic stat1 = new Statistic("Worked", 7);
		Statistic stat2 = new Statistic("Failed", 13);
		Statistic stat3, stat4;
		
		// Create a file.
		File statisticTestFile = new File("StatisticTest.ht");
		
		// Write to the file.
		ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(statisticTestFile));
		outputStream.writeObject(stat1);
		outputStream.writeObject(stat2);
		outputStream.close();
		
		// Read the file information.
		ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(statisticTestFile));
		stat3 = (Statistic)inputStream.readObject();
		stat4 = (Statistic)inputStream.readObject();
		
		// Perform tests.
		assertEquals("Worked", stat3.getItemName());
		assertEquals(7, stat3.getQuantity(), 0);
		assertEquals("Failed", stat4.getItemName());
		assertEquals(13, stat4.getQuantity(), 0);
		
		// Try editing the read information.
		stat3.setItemName("Win");
		stat3.setQuantity(7.21);
		stat4.setItemName("Lose");
		stat4.setQuantity(13.47);
		
		// Perform tests.
		assertEquals("Win", stat3.getItemName());
		assertEquals(7.21, stat3.getQuantity(), 0);
		assertEquals("Lose", stat4.getItemName());
		assertEquals(13.47, stat4.getQuantity(), 0);
		
		System.out.println("Completed all tests");		
	}
	
	public static void main(String [] args)
	throws IOException, ClassNotFoundException
	{
		testStatistic();
	}	
}
