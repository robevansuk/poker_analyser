package com.morphiles.gui;

import com.morphiles.views.JStatusBar;
import com.morphiles.views.TableAndChartsViewer;
import com.morphiles.views.TreeNavigator;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JSplitPane;
import org.springframework.stereotype.Component;

/**
 * @author robevans
 */
@Component
public class GuiFrame extends JFrame {

    private BorderLayout borderLayout = new BorderLayout();
    private JSplitPane splitPane;

    private JStatusBar statusBar;

    private MenuBar menubar;

    private HandHistoryTabs hhTabs;
    private TableAndChartsViewer datTabs;

    private final static int GUI_WIDTH = 1200;
    private final static int GUI_HEIGHT = 700;
    private String label;

    public GuiFrame(){
        super("PokerAnalyser");
        setupDisplay();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(GUI_WIDTH, GUI_HEIGHT);
        this.setVisible(true);
    }

    /**
     * Sets up the initial display
     */
    public void setupDisplay(){
        this.setLayout(borderLayout);

        addSplitPanes(BorderLayout.CENTER);
        addStatusBar(BorderLayout.SOUTH);
        addNavigationTree(BorderLayout.WEST);

        addMenuBar(); // MenuBar sits on this JFrame
    }

    public void addSplitPanes(String position){
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerSize(5);
        splitPane.setResizeWeight(0.2);
        splitPane.add(getTableAndChartsViewer()); // HH data table and chart in Center.
        splitPane.add(getHandHistoryTabs("default")); // HH data as a JList - RHS
        this.add(splitPane, position);
    }

    /**
     * inits a AllDataTable
     */
    public TableAndChartsViewer getTableAndChartsViewer(){
        datTabs = new TableAndChartsViewer();
        return datTabs;
    }

    /**
     * inits a HandHistoryTabs which displays the hand history as a JLIST on the
     * right hand side of the GUI (more for debugging purposes at the moment so we
     * can compare the data obtained to the original hand history visible in the list.
     */
    public HandHistoryTabs getHandHistoryTabs(String name){
        // New HandHistory Obj
        hhTabs = new HandHistoryTabs(name);
        return hhTabs;
    }

    public void addNavigationTree(String position){
        TreeNavigator.INSTANCE.init();
        this.add(TreeNavigator.INSTANCE.getNavigationScrollPane(), position);
    }

    public void addStatusBar(String position){
        statusBar = new JStatusBar("Poker Analyser", "File > Open > Select a file/folder of hand histories to get started!");
        this.add(statusBar, position);
    }

    /**
     * Adds the menu bar to the GUI
     */
    public void addMenuBar(){
        menubar = new MenuBar(hhTabs);
        this.setJMenuBar(menubar);
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
        if(hhTabs!=null)
            hhTabs.setActiveTab(label);

        if(datTabs!=null)
            datTabs.setActiveTab(label);

        // TODO set the selected item in the TreeNavigator to be in sync too.
    }

    public TableAndChartsViewer getDataTabs(){
        return datTabs;
    }

    public JStatusBar getStatusBar(){
        return statusBar;
    }

    public JFrame getFrame(){
        return this;
    }

}
