package testBudgetComponents;
import java.io.*;
import static org.junit.Assert.assertEquals;
//import org.junit.Test;
import java.util.ArrayList;

import budgetComponents.Histogram;
import budgetComponents.Statistic;

public class HistogramTest {
	public static void testHistogram()
	throws IOException, ClassNotFoundException
	{
		Histogram histogram1 = new Histogram();
		histogram1.addItem("Food", 300);
		histogram1.addItem("Clothes", 37.50);
		histogram1.addItem("Books", 203.07);
		
		ArrayList<Statistic> histogramItems1 = histogram1.getItemsByQuantity();
		assertEquals("Food", histogramItems1.get(0).getItemName());
		assertEquals(300, histogramItems1.get(0).getQuantity(), 0);
		assertEquals("Clothes", histogramItems1.get(1).getItemName());
		assertEquals(37.5, histogramItems1.get(1).getQuantity(), 0);
		assertEquals("Books", histogramItems1.get(2).getItemName());
		assertEquals(203.07, histogramItems1.get(2).getQuantity(), 0);
		
		histogramItems1 = histogram1.getItemsByPercentage();
		assertEquals("Food", histogramItems1.get(0).getItemName());
		assertEquals(55, histogramItems1.get(0).getQuantity(), 0);
		assertEquals("Clothes", histogramItems1.get(1).getItemName());
		assertEquals(7, histogramItems1.get(1).getQuantity(), 0);
		assertEquals("Books", histogramItems1.get(2).getItemName());
		assertEquals(38, histogramItems1.get(2).getQuantity(), 0);	
		
		// Store this Histograms current information in a file.
		File histogramTestFile = new File("HistogramTest.ht");
		ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(histogramTestFile));
		outputStream.writeObject(histogram1);
		outputStream.close();
		
		histogram1.editItem(0, "Life", 7);
		histogram1.editItem(1, "Energy", 21);
		histogram1.editItem(2, "Theta", 1);
		histogramItems1 = histogram1.getItemsByQuantity();
		assertEquals("Life", histogramItems1.get(0).getItemName());
		assertEquals(7, histogramItems1.get(0).getQuantity(), 0);
		assertEquals("Energy", histogramItems1.get(1).getItemName());
		assertEquals(21, histogramItems1.get(1).getQuantity(), 0);
		assertEquals("Theta", histogramItems1.get(2).getItemName());
		assertEquals(1, histogramItems1.get(2).getQuantity(), 0);	
		
		// Read the stored data (from before the histogram was edited) into
		// a new histogram.
		ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(histogramTestFile));
		Histogram histogram2 = (Histogram)inputStream.readObject();
		//Histogram histogram3 = (Histogram)inputStream.readObject();
		
		ArrayList<Statistic> histogramItems2 = histogram2.getItemsByQuantity();
		assertEquals("Food", histogramItems2.get(0).getItemName());
		assertEquals(300, histogramItems2.get(0).getQuantity(), 0);
		assertEquals("Clothes", histogramItems2.get(1).getItemName());
		assertEquals(37.5, histogramItems2.get(1).getQuantity(), 0);
		assertEquals("Books", histogramItems2.get(2).getItemName());
		assertEquals(203.07, histogramItems2.get(2).getQuantity(), 0);
		
		histogramItems2 = histogram2.getItemsByPercentage();
		assertEquals("Food", histogramItems2.get(0).getItemName());
		assertEquals(55, histogramItems2.get(0).getQuantity(), 0);
		assertEquals("Clothes", histogramItems2.get(1).getItemName());
		assertEquals(7, histogramItems2.get(1).getQuantity(), 0);
		assertEquals("Books", histogramItems2.get(2).getItemName());
		assertEquals(38, histogramItems2.get(2).getQuantity(), 0);	
		
		histogram2.editItem(0, "Life", 7);
		histogram2.editItem(1, "Energy", 21);
		histogram2.editItem(2, "Theta", 1);
		histogramItems2 = histogram2.getItemsByQuantity();
		assertEquals("Life", histogramItems2.get(0).getItemName());
		assertEquals(7, histogramItems2.get(0).getQuantity(), 0);
		assertEquals("Energy", histogramItems2.get(1).getItemName());
		assertEquals(21, histogramItems2.get(1).getQuantity(), 0);
		assertEquals("Theta", histogramItems2.get(2).getItemName());
		assertEquals(1, histogramItems2.get(2).getQuantity(), 0);	
		
		System.out.println("Completed all tests");
	}
	
	public static void main(String [] args)
	throws IOException, ClassNotFoundException
	{
		testHistogram();
	}
}
