package com.morphiles.gui;

import com.morphiles.views.DataTable;
import com.morphiles.views.JStatusBar;
import com.morphiles.views.TableAndChartsViewer;
import com.morphiles.views.TreeNavigator;

import javax.swing.*;
import java.awt.*;

/**
 * @author robevans
 */
public enum GuiFrame {

    SINGLETON;

    private JFrame frame;

    private BorderLayout borderLayout = new BorderLayout();
    private JSplitPane splitPane;

    private JStatusBar statusBar;

    private MenuBar menubar;

    private HandHistoryTabs hhTabs;
    private TableAndChartsViewer datTabs;

    private final static int GUI_WIDTH = 1200;
    private final static int GUI_HEIGHT = 700;
    private String label;

    /**
     * Sets up the initial display
     */
    public void setupDisplay(){
        frame.setLayout(borderLayout);

        addSplitPanes(BorderLayout.CENTER);
        addStatusBar(BorderLayout.SOUTH);
        addNavigationTree(BorderLayout.WEST);

        addMenuBar(); // MenuBar sits on this JFrame
    }

    public void init(){
        frame = new JFrame("PokerAnalyser");
        setupDisplay();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(GUI_WIDTH, GUI_HEIGHT);
        frame.setVisible(true);
    }

    public void addSplitPanes(String position){
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerSize(6);
        splitPane.setResizeWeight(0.66);
        splitPane.add(getTableAndChartsViewer());
        splitPane.add(getHandHistoryTabs("default"));
        frame.add(splitPane, position);
    }

    /**
     * inits a AllDataTable
     */
    public TableAndChartsViewer getTableAndChartsViewer(){
        datTabs = new TableAndChartsViewer();
        return datTabs;
    }

    /**
     * inits a HandHistoryTabs object
     */
    public HandHistoryTabs getHandHistoryTabs(String name){
        // New HandHisory Obj
        hhTabs = new HandHistoryTabs(name);
        return hhTabs;
    }

    public void addNavigationTree(String position){
        TreeNavigator.INSTANCE.init();
        frame.add(TreeNavigator.INSTANCE.getNavigationScrollPane(), position);
    }

    public void addStatusBar(String position){
        statusBar = new JStatusBar("Poker Analyser", "File > Open > Select a file/folder of hand histories to get started!");
        frame.add(statusBar, position);
    }

    /**
     * Adds the menu bar to the GUI
     */
    public void addMenuBar(){
        menubar = new MenuBar(hhTabs);
        frame.setJMenuBar(menubar);
    }

    /**
     *
     * @param name
     */
    public com.morphiles.views.DataTable addDataTable(String name, HandHistoryListTabs h){
        return datTabs.addNewTable(name, h);
    }

    public void removeTabsFor(String name){
        datTabs.removeTab(name);
        hhTabs.removeTab(name);
    }

    public void setActiveTab(String label){

        if(hhTabs!=null){
            hhTabs.setActiveTab(label);
        }
        if(datTabs!=null){
            datTabs.setActiveTab(label);
        }
    }

    public TableAndChartsViewer getDataTabs(){
        return datTabs;
    }

    public JStatusBar getStatusBar(){
        return statusBar;
    }

    public JFrame getFrame(){
        return frame;
    }

}
