package com.morphiles.gui;

import java.awt.*;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JPanel;

public class DataBarChart extends JPanel {

	private Double[] values;
	private String[] names;
	private String title;
	
	public DataBarChart(){
		title = "Profit//Loss for Pairs";
		values = new Double[25];
		names = new String[25];
		
		
		names[0] = "22-";
		names[1] = "22+";
		names[2] = "33+";
		names[3] = "33-";
		names[4] = "44+";
		names[5] = "55-";
		names[6] = "55+";
		names[7] = "66-";
		names[8] = "66+";
		names[9] = "77-";
		names[10] = "77+";
		names[11] = "88-";
		names[12] = "88+";
		names[13] = "99-";
		names[14] = "99+";
		names[15] = "TT-";
		names[16] = "TT+";
		names[17] = "JJ-";
		names[18] = "JJ+";
		names[19] = "QQ-";
		names[20] = "QQ+";
		names[21] = "KK-";
		names[22] = "KK+";
		names[23] = "AA-";
		names[24] = "AA+";
		for (int i = 0; i<=24; i++){
			if (i % 2 ==0){
				values[i] = Math.random() * 100;
			} else {
				values[i] = (-1) *  Math.random() * 100;
			}
		}
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		if (values==null || values.length == 0)
			return;
		
		double minVal=0;
		double maxVal=0;
		
		int halfHeight = (int) getSize().getHeight();
		
		g.drawLine(10, halfHeight, (int) getSize().getWidth()-50, halfHeight);
		
		// Work out the minimum and maximum scale that needs to be represented  
		for (int i = 0; i < values.length; i++) {
			System.out.println(i+"");
			if (minVal > values[i])
				minVal = values[i];
			if (maxVal < values[i])
				maxVal = values[i];    
		}
		Dimension d = getSize();
		
		int clientWidth = d.width;    
		int clientHeight = d.height;    
		int barWidth = clientWidth / values.length;     
		
		Font titleFont = new Font("SansSerif", Font.BOLD, 16);    
		FontMetrics titleFontMetrics = g.getFontMetrics(titleFont);    
		
		Font labelFont = new Font("SansSerif", Font.PLAIN, 12);    
		FontMetrics labelFontMetrics = g.getFontMetrics(labelFont);
		
		Font valFont = new Font("SansSerif", Font.PLAIN, 9);    
		FontMetrics valFontMetrics = g.getFontMetrics(valFont);
		
		
		int titleWidth = titleFontMetrics.stringWidth(title);    
		int y = titleFontMetrics.getAscent();    
		int x = (clientWidth - titleWidth) / 2;
		
		g.setFont(titleFont);    
		g.drawString(title, x, y);
		
		int top = titleFontMetrics.getHeight();    
		int bottom = labelFontMetrics.getHeight();
		
		if (maxVal == minVal)      
			return;
		
		double scale = (clientHeight - top - bottom) / (maxVal - minVal);
		
		y = clientHeight - labelFontMetrics.getDescent();
		g.setFont(labelFont);
		
		for (int i = 0; i < values.length; i++) {
			g.setFont(labelFont);
			int valueX = i * barWidth + 1; 
			int valueY = top;      
			int height = (int) (values[i] * scale);
			if (values[i] >= 0)        
				valueY += (int) ((maxVal - values[i]) * scale);
			else {        
				valueY += (int) (maxVal * scale);
				height = -height;
			} 
			if (i % 2 == 1){
				g.setColor(Color.red);
				g.fillRect(valueX, valueY, barWidth - 2, height);
				g.setColor(Color.black);
				g.draw3DRect(valueX, valueY, barWidth - 2, height, true);
				int labelWidth = labelFontMetrics.stringWidth(names[i]);
				x = i * barWidth + (barWidth - labelWidth) / 2;
				g.drawString(names[i], x, y);
			} else {
				g.setColor(Color.green);
				g.fillRect(valueX, valueY, barWidth - 2, height);
				g.setColor(Color.black);
				g.draw3DRect(valueX, valueY, barWidth - 2, height, true);
				int labelWidth = labelFontMetrics.stringWidth(names[i]);
				x = i * barWidth + (barWidth - labelWidth) / 2;
				g.drawString(names[i], x, y);
			}
			
			// Post Values onto the bar chart as well
			g.setFont(valFont);
			String val = values[i].toString();
			val = val.substring(0,val.indexOf(".")+3);
			
			g.drawString("$" + val + "", x-5, valueY);
			
		}
	}
	
	
	
}
