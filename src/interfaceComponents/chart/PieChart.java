package interfaceComponents.chart;

import java.lang.Math;
import java.awt.*;
import java.util.ArrayList;

public class PieChart
extends Canvas
implements java.io.Serializable
{
	
   static final long serialVersionUID = 10;
	
   ArrayList<Slice> slices;
   
   PieChart(){
	   slices = new ArrayList<Slice>();
	   
	   this.setPreferredSize(new Dimension(500, 500));	// Size gets determined at lower levels before we invoke "paint".
	   this.setBackground(Color.WHITE);
   }
   
   public void addSlice(Color sliceColor, double value){
	   slices.add(new Slice(value, sliceColor));
   }
   
   public void editSlice(int index, double value){
	   slices.get(index).value = value;
   }
   
   public void removeSlice(int index){
	   slices.remove(index);
   }
   
   public void paint(Graphics g){
	  Rectangle pieChartArea = new Rectangle(10, 10, 500, 500);
      drawPie(g, pieChartArea, slices);
   }
   
   void drawPie(Graphics g, Rectangle area, ArrayList<Slice> slices){  
      double total = 0.0;
      double currentValue = 0.0;
      int startAngle = 0;
      
      for (int i = 0; i < slices.size(); i++){
         total += slices.get(i).value;
      }
      
      for (int i = 0; i < slices.size(); i++) {
         startAngle = (int)Math.round(currentValue * 360.0 / total);
         int arcAngle = (int)Math.round(slices.get(i).value * 360.0 / total);
         g.setColor(slices.get(i).color);
         g.fillArc(area.x, area.y, area.width, area.height, startAngle, arcAngle);
         currentValue += slices.get(i).value;
      }
   }
   
   private class Slice
   implements java.io.Serializable
   {
	   
	   static final long serialVersionUID = 10;
	   
	   public double value;
	   public Color color;
	   
	   public Slice(double value, Color color) {  
	      this.value = value;
	      this.color = color;
	   }
	   
	}
   
}