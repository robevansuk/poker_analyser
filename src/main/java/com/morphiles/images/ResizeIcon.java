package com.morphiles.images;

import javax.swing.*;
import java.awt.*;

public class ResizeIcon implements Icon {
    private static final int WIDTH = 12;
    private static final int HEIGHT = 12;

    private static final Color COLOUR_LEFT = new Color(184, 180, 163);
    private static final Color COLOUR_TOP_RIGHT= new Color(184, 180, 161);
    private static final Color COLOUR_BOTOM_RIGHT= new Color(184, 181, 161);

    private static final Color THREE_D_COLOUR = new Color(255, 255, 255);

    public void paintIcon(Component c, Graphics g, int x, int y){

        int firstRow=0;
        int firstColumn=0;
        int rowDiff=4;
        int colDiff=4;
        int secondRow=firstRow+rowDiff;
        int secondColumn = firstColumn + colDiff;
        int thirdRow=secondRow +rowDiff;
        int thirdColumn = secondColumn + colDiff;

        //paint the white squares
        draw3dSquare(g,firstColumn+1, thirdRow+1);

        draw3dSquare(g,secondColumn+1, secondRow+1);
        draw3dSquare(g,secondColumn+1, thirdRow+1);

        draw3dSquare(g,thirdColumn+1, firstRow+1);
        draw3dSquare(g,thirdColumn+1, secondRow+1);
        draw3dSquare(g,thirdColumn+1, thirdRow+1);

        //paint the grey squares on top of the white squares
        drawSquare(g, firstColumn,thirdRow);
        drawSquare(g,secondColumn,secondRow);
        drawSquare(g,secondColumn,thirdRow);

        drawSquare(g,thirdColumn,firstRow);
        drawSquare(g,thirdColumn,secondRow);
        drawSquare(g,thirdColumn,thirdRow);


    }


    public int getIconWidth(){
        return WIDTH;
    }

    public int getIconHeight(){
        return HEIGHT;
    }

    public void drawSquare(Graphics g, int x, int y){
        Color oldColor = g.getColor();
        g.setColor(COLOUR_LEFT);
        g.drawLine(x,y,x,y+1);
        g.setColor(COLOUR_TOP_RIGHT);
        g.drawLine(x+1,y,x+1,y);
        g.setColor(COLOUR_BOTOM_RIGHT);
        g.drawLine(x+1, y+1, x+1, y+1);
        g.setColor(oldColor);
    }

    private void draw3dSquare(Graphics g, int x, int y){
        Color oldColor=g.getColor();
        g.setColor(THREE_D_COLOUR);
        g.fillRect(x,y,2,2);
        g.setColor(oldColor);
    }

}