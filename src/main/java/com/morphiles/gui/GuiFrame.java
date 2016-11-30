package com.morphiles.gui;

import com.morphiles.views.JStatusBar;
import com.morphiles.views.TableAndChartsViewer;
import com.morphiles.views.TreeNavigator;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JSplitPane;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author robevans
 */
public class GuiFrame extends JFrame {

    private BorderLayout borderLayout = new BorderLayout();
    private JSplitPane splitPane;

    private JStatusBar statusBar;

    private MenuBar menubar;

    private final static int GUI_WIDTH = 1200;
    private final static int GUI_HEIGHT = 700;
    private String label;

    @Autowired
    private TreeNavigator treeNavigator;

    @Autowired
    TableAndChartsViewer datTabs;

    @Autowired
    HandHistoryTabs hhTabs;

    public GuiFrame(){
        super("PokerAnalyser");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(GUI_WIDTH, GUI_HEIGHT);
        this.setVisible(true);
    }

    /**
     * Sets up the initial display
     */
    public void setupDisplay(){
        this.treeNavigator = new TreeNavigator();
        this.setLayout(borderLayout);

        addSplitPanes(datTabs, hhTabs);
        addStatusBar(BorderLayout.SOUTH);
        addNavigationTree(BorderLayout.WEST);

        addMenuBar(); // MenuBar sits on this JFrame
    }


    public void addSplitPanes(TableAndChartsViewer datTabs, HandHistoryTabs hhTabs){
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerSize(5);
        splitPane.setResizeWeight(0.2);
        splitPane.add(datTabs); // HH data table and chart in Center.
        splitPane.add(hhTabs); // HH data as a JList - RHS
        this.add(splitPane, BorderLayout.CENTER);
    }


    public void addNavigationTree(String position){
        this.add(treeNavigator.getNavigationScrollPane(), position);
    }

    public void addNodeTo(String parent, String childNode) {
        treeNavigator.addNodeTo(parent, childNode);
    }

    public void addStatusBar(String position){
        statusBar = new JStatusBar("Poker Analyser", "File > Open > Select a file/folder of hand histories to get started!");
        this.add(statusBar, position);
    }

    /**
     * Adds the menu bar to the GUI
     */
    public void addMenuBar(){
        menubar = new MenuBar();
        this.setJMenuBar(menubar);
    }


    public JStatusBar getStatusBar(){
        return statusBar;
    }

    public JFrame getFrame(){
        return this;
    }

}
