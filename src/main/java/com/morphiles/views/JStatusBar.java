package com.morphiles.views;

import com.morphiles.images.ResizeIcon;
import com.morphiles.images.SeparatorPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;

import javax.swing.*;

public class JStatusBar extends JPanel {

    private static final long serialVersionUID = 1L;

    protected JPanel leftPanel;
    protected JPanel rightPanel;
    private JPanel progressPanel;
    private JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.TRAILING, 5, 0));
    private JLabel timeLabel;
    private JProgressBar progressBar;

    public JStatusBar(String str1, String str2) {
        JLabel leftLabel = new JLabel(str1);
        JLabel rightLabel = new JLabel(str2);
        createPartControl();
        setLeftComponent(leftLabel);
        addRightComponent(rightLabel);
        addRightComponent(addProgressBar());
        timePanel.add(new SeparatorPanel(Color.GRAY, Color.WHITE));
        timeLabel = new JLabel("");
        timePanel.add(timeLabel);
        timePanel.setOpaque(false);
        leftPanel.add(timePanel);
        setVisible(true);
    }

    protected void createPartControl() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(getWidth(), 23));
        add(initLeftStatusPanel(), BorderLayout.WEST);
        add(getResizeIconPanel(), BorderLayout.EAST);
    }

    /**
     * SETS the JComponents (JPanel) for the first/left-most panel in the status bar.
     * @param component
     */
    public void setLeftComponent(JComponent component) {
        leftPanel.add(component);
    }

    /**
     * adds JComponents (JLabels,Icons,JPanels,etc) to the left JPanel
     * @param component
     */
    public void addRightComponent(JComponent component) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.TRAILING, 5, 0));
        panel.add(new SeparatorPanel(Color.GRAY, Color.WHITE));
        panel.add(component);
        panel.setOpaque(false);
        leftPanel.add(panel);
    }

    public void timeToProcess(String text) {
        timeLabel.setText(text);
    }

    public JPanel addProgressBar(){
        progressPanel = new JPanel();
        progressPanel.setLayout(new BorderLayout());
        progressPanel.setOpaque(false);
        progressPanel.setVisible(true);
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setValue(0);
        progressPanel.add(progressBar, BorderLayout.CENTER);
        return progressPanel;
    }

    public JPanel initLeftStatusPanel(){
        leftPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 1, 1));
        leftPanel.setOpaque(false);
        return leftPanel;
    }

    public JPanel getResizeIconPanel(){
        rightPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING, 2, 10));

        JLabel resizeIcon = new JLabel(new ResizeIcon());
        resizeIcon.setOpaque(false);
        rightPanel.setOpaque(false);
        rightPanel.add(resizeIcon);
        return rightPanel;
    }

    public JProgressBar getProgressBar(){
        return progressBar;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int y = 0;
        g.setColor(new Color(156, 154, 140));
        g.drawLine(0, y, getWidth(), y);
        y++;

        g.setColor(new Color(196, 194, 183));
        g.drawLine(0, y, getWidth(), y);
        y++;

        g.setColor(new Color(218, 215, 201));
        g.drawLine(0, y, getWidth(), y);
        y++;

        g.setColor(new Color(233, 231, 217));
        g.drawLine(0, y, getWidth(), y);

        y = getHeight() - 3;

        g.setColor(new Color(233, 232, 218));
        g.drawLine(0, y, getWidth(), y);
        y++;

        g.setColor(new Color(233, 231, 216));
        g.drawLine(0, y, getWidth(), y);
        y++;

        g.setColor(new Color(221, 221, 220));
        g.drawLine(0, y, getWidth(), y);
    }
}
