package interfaceComponents.chart;

import java.awt.*;
import java.beans.*;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.util.ArrayList;

public class ColorIndexDisplay
extends Container
implements PropertyChangeListener, java.io.Serializable
{
	
	static final long serialVersionUID = 10;

	ColorIndex colorsAndLabels;
	ArrayList<ColorIndexPairDisplay> displayableIndices;
	
	public ColorIndexDisplay(ColorIndex colorsAndLabels){
		this.colorsAndLabels = colorsAndLabels;
		displayableIndices = new ArrayList<ColorIndexPairDisplay>();
		this.setLayout(new GridBagLayout());
		
		for(int i = 0; i < colorsAndLabels.length(); i++){
			displayableIndices.add(new ColorIndexPairDisplay(colorsAndLabels.getColorIndexPair(i)));
		}
		
		colorsAndLabels.addPropertyChangeListener("colorIndexSize", this);
		colorsAndLabels.addPropertyChangeListener("colorsAndLabels", this);
		
		this.setBackground(Color.WHITE);
		
		refreshDisplay();
	}
	
	private void refreshDisplay(){
		this.removeAll();
		
		for(int i = 0; i < displayableIndices.size(); i++){
			this.add(displayableIndices.get(i), new GridBagConstraints(
					0, i, 1, 1, 0, 0, GridBagConstraints.WEST,
					GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
					0, 0
			));
		}
	}
	
	public void propertyChange(PropertyChangeEvent e){
		if(e.getSource() == colorsAndLabels && e instanceof IndexedPropertyChangeEvent){
			if(e.getPropertyName() == "colorIndexSize"){
				// If we added a new item...
				if(e.getOldValue() == null){
					displayableIndices.add(new ColorIndexPairDisplay((ColorIndex.ColorIndexPair)e.getNewValue()));
				}
				// If we removed an item...
				else{
					displayableIndices.remove(((IndexedPropertyChangeEvent)e).getIndex());
				}			
			}
			
			// If we changed an existing item...
			if(e.getPropertyName() == "colorsAndLabels"){
				ColorIndexPairDisplay temp = displayableIndices.get(((IndexedPropertyChangeEvent)e).getIndex());
				temp.setColor(((ColorIndex.ColorIndexPair)e.getNewValue()).itemColor);
				temp.setText(((ColorIndex.ColorIndexPair)e.getNewValue()).label);
			}
			
			refreshDisplay();
		}
	}
	
	private class ColorIndexPairDisplay
	extends JPanel
	{
		
		static final long serialVersionUID = 10;
		
		private ColorIndex.ColorIndexPair colorAndLabel;
		private JLabel colorLabel;
		private JLabel itemName;
		
		public ColorIndexPairDisplay(ColorIndex.ColorIndexPair colorAndLabel){
			super();	
			this.setLayout(new GridBagLayout());
			
			this.colorAndLabel = colorAndLabel;
			this.colorLabel = new JLabel();			
			this.itemName = new JLabel();
			
			this.applyStyle(Color.WHITE, Color.BLACK, new Font(Font.SANS_SERIF, Font.BOLD, 25));

			this.add(colorLabel, new GridBagConstraints(
					0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER,
					GridBagConstraints.NONE, new Insets(20, 20, 20, 20),
					20, 20
			));
			
			this.add(itemName, new GridBagConstraints(
					1, 0, 1, 1, 0, 0, GridBagConstraints.CENTER,
					GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
					20, 20
			));
			
		}
		
		private void applyStyle(Color backgroundColor, Color fontColor, Font fontStyle){	
			this.setBackground(backgroundColor);
			
			colorLabel.setBackground(colorAndLabel.itemColor);
			colorLabel.setOpaque(true);		
			
			itemName.setOpaque(true);
			itemName.setBackground(backgroundColor);
			itemName.setFont(fontStyle);
			itemName.setForeground(fontColor);
			itemName.setText(colorAndLabel.label);	
		}
		
		public void setColor(Color newColor){
			this.colorLabel.setBackground(newColor);
		}
		
		public void setText(String text){
			this.itemName.setText(text);
		}
	}
	
}
