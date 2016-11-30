package com.morphiles.gui;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 13/06/13
 * Time: 08:53
 * To change this template use File | Settings | File Templates.
 */
public class TabCloseButton extends JPanel implements ActionListener {

    private String name;
    private JTabbedPane pane;
    private JButton closeButton;

    public TabCloseButton(String name, JTabbedPane pane){
        super();
        this.name = name;
        this.pane = pane;

        byte[] image = null;
        URL imageFile = this.getClass().getResource("/close.png");

        if (imageFile!=null) {
            Icon icon = new ImageIcon(imageFile);
            closeButton = new JButton(icon);
        } else {
            closeButton = new JButton("x");
        }

        closeButton.setBorder(null);
        JLabel label = new JLabel(name);

        //closeButton.setBackground(null);
        label.setFont(new Font("Arial", Font.PLAIN, 10));


        this.setOpaque(false);
        this.add(closeButton);
        this.add(label);

        closeButton.setUI(new BasicButtonUI());
        //Make it transparent
        closeButton.setContentAreaFilled(false);
        //No need to be focusable
        closeButton.setFocusable(false);
        closeButton.setBorder(BorderFactory.createEtchedBorder());
        closeButton.setBorderPainted(false);
        //Making nice rollover effect
        //we use the same listener for all buttons
        closeButton.addMouseListener(buttonMouseListener);
        closeButton.addActionListener(this);
        closeButton.setRolloverEnabled(true);
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
        this.setName(name);

    }


     public void actionPerformed(ActionEvent e){
          if (e.getSource().equals(closeButton)){
                int i = pane.indexOfTabComponent(TabCloseButton.this);
              if (i!=-1){
//                  gui.removeTabsFor(name);
                  //pane.remove(i);
              }

          }
     }

    private final static MouseListener buttonMouseListener = new MouseAdapter() {
        public void mouseEntered(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
                button.setBorderPainted(true);
            }
        }

        public void mouseExited(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
                button.setBorderPainted(false);
            }
        }
    };

    public byte[] getImage(String ImageName) throws IOException {

        // open image
        File imgPath = new File(ImageName);
        BufferedImage bufferedImage = ImageIO.read(imgPath);

        // get DataBufferBytes from Raster
        WritableRaster raster = bufferedImage .getRaster();
        DataBufferByte data   = (DataBufferByte) raster.getDataBuffer();

        return ( data.getData() );
    }
}
