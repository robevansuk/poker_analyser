package com.morphiles.images;

import javax.swing.*;
import java.awt.*;

/**
 * @author robevans
 */
public class SeparatorPanel extends JPanel {

    private Color leftColour;
    private Color rightColour;

    public SeparatorPanel(Color left, Color right){
        leftColour = left;
        rightColour = right;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g){
        g.setColor(leftColour);
        g.drawLine(0,0,0,getHeight());
        g.setColor(rightColour);
        g.drawLine(1,0,1,getHeight());
    }

}
