package com.morphiles.gui;

import javax.swing.*;
import java.awt.*;

public class MainGui { // extends JFrame {

	private BorderLayout borderLayout = new BorderLayout();
    private JSplitPane splitPane;
	private JList handList = new JList();
	
	private MenuBar menubar;
	private FileImporter importer;
    private TableOptionsMenuBar tableOptions;
	
	private HandHistoryTabs hhTabs;
	private DataPresentationTabs datTabs;

    private final static int GUI_WIDTH = 1200;
    private final static int GUI_HEIGHT = 700;
    private String label;


    public MainGui() {
		GuiFrame.SINGLETON.init();
//		super("PokerAnalyser");
//		setupDisplay();
//		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        this.pack();
//        this.setSize(GUI_WIDTH, GUI_HEIGHT);
//        this.setVisible(true);

	}

	/**
	 * Sets up the initial display
	 */
	public void setupDisplay(){
//		setLayout(borderLayout);
        addSplitPanes();
        addMenu();
		//addButton();
	}

    /**
     * Adds the menu bar to the GUI
     */
    public void addMenu(){
        menubar = new MenuBar(hhTabs);
//        this.setJMenuBar(menubar);
    }

    public void addSplitPanes(){
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerSize(6);
        splitPane.setResizeWeight(0.66);
//        this.add(splitPane, BorderLayout.CENTER);
        addTable();
        addHandHistoryTabs("default");
    }

    /**
     * Adds a data table to the Gui
     */
    public void addTable(){
        datTabs = new DataPresentationTabs(this);
        splitPane.add(datTabs);
    }
	
	/**
	 * Lays out and instantiates the hand history tabs
	 */
	public void addHandHistoryTabs(String name){
		// New HandHisory Obj
		hhTabs = new HandHistoryTabs(name);
        splitPane.add(hhTabs);

	}
	
	/**
	 * A test area for the button
	 */
	public void addButton(){
		JButton buttonA = new JButton("Menu Display Area");
//		add(buttonA, BorderLayout.NORTH);
	}
	
	/**
	 * Create the file importer
	 * @param importer
	 */
	public void setFileImporter(FileImporter importer){
		this.importer = importer;
	}

	/**
	 * 
	 * @param name
	 */
	public com.morphiles.views.DataTable addDataTable(String name, HandHistoryTab h){
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
	
	public static void main(String[] args){
		new MainGui();
	}

    public DataPresentationTabs getDataTabs(){
        return datTabs;
    }
}