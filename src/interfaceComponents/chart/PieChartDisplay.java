// This should use a histogram to keep its colorIndex
// up to date. The PieChart should be repainted and the
// colorIndex redisplayed every time the colorIndex gets
// updated as a result of the histogram being updated.
// We need to be able to detect a deletion in the Histogram's
// ArrayList.

package interfaceComponents.chart;

import java.awt.*;
import java.util.Random;
import java.util.ArrayList;
import budgetComponents.Histogram;
import budgetComponents.Statistic;
import java.beans.*;

public class PieChartDisplay
extends Container
implements PropertyChangeListener, java.io.Serializable
{
	
	static final long serialVersionUID = 10;

	private PieChart chart;
	private ColorIndex colorsAndLabels;
	private Histogram stats;
	private Label chartLabel;
	
	public PieChartDisplay(Histogram stats){		
		chart = new PieChart();
		colorsAndLabels = new ColorIndex();
		chartLabel = new Label("Spending Distribution", Label.CENTER);
		chartLabel.setBackground(Color.WHITE);
		chartLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
		this.stats = stats;
		
		// Fill the ColorIndex and assign random colors.
		ArrayList<Statistic> statisticValues = this.stats.getItemsByQuantity();
		
		for(int i = 0; i < statisticValues.size(); i++){
			this.addColoredStat(statisticValues.get(i));
		}
		
		// Track changes.
		this.stats.addPropertyChangeListener("items", this);
		this.stats.addPropertyChangeListener("itemsSize", this);
		
		// Add contents.
		this.setLayout(new GridBagLayout());
		
		this.add(chartLabel, new GridBagConstraints(
				0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
				10, 10
		));
		this.add(chart, new GridBagConstraints(
				0, 1, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
				20, 20
		));
		//this.setPreferredSize(new Dimension(600, 600));	// Size gets determined at lower levels before we invoke "paint".
		//this.setBackground(Color.WHITE);
	}
	
	private void addColoredStat(Statistic stat){
		Color statColor;
		
		do{
			statColor = this.randomColor();
		}while(colorsAndLabels.contains(statColor));
		
		colorsAndLabels.addColorIndexPair(statColor, stat.getItemName());
		chart.addSlice(statColor, stat.getQuantity());
	}
	
	private void removeColoredStat(int index){
		colorsAndLabels.removeColorIndexPair(index);
		chart.removeSlice(index);
	}
	
	public ColorIndex getColorsAndLabels(){
		return this.colorsAndLabels;
	}
	
	public void paint(Graphics g){
		chart.paint(g);
	}
	
	public void propertyChange(PropertyChangeEvent e){
		if(e.getSource() == stats && e instanceof IndexedPropertyChangeEvent){
			if(e.getPropertyName() == "itemsSize"){
				// If we added a new item...
				if(e.getOldValue() == null){
					this.addColoredStat((Statistic)e.getNewValue());
				}
				// If we removed an item...
				else{
					this.removeColoredStat(((IndexedPropertyChangeEvent)e).getIndex());
				}			
			}
			
			// If we changed an existing item...
			if(e.getPropertyName() == "items"){
				int index = ((IndexedPropertyChangeEvent)e).getIndex();
				String newName = ((Statistic)e.getNewValue()).getItemName();
				double newQuantity = ((Statistic)e.getNewValue()).getQuantity();
				
				if(newName != ((Statistic)e.getOldValue()).getItemName()){
					colorsAndLabels.editColorIndexPair(index, newName);
				}
				if(newQuantity != ((Statistic)e.getOldValue()).getQuantity()){
					chart.editSlice(index, newQuantity);
				}
			}
			
			chart.repaint();
		}
	}
	
	private Color randomColor(){
		Color result;
		Random randomGenerator = new Random();
		
		float r = randomGenerator.nextFloat();
		float g = randomGenerator.nextFloat();
		float b = randomGenerator.nextFloat();
		
		result = new Color(r, g, b);
		
		return result;
	}
	
}
