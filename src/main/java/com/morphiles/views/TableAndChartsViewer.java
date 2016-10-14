package com.morphiles.views;

import com.morphiles.gui.GuiFrame;
import com.morphiles.gui.HandHistoryListTabs;
import com.morphiles.gui.TabCloseButton;
import com.morphiles.models.PokerDataModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TableAndChartsViewer extends JPanel {
	
	private Map<String, DataTable> tables = new HashMap<>();
	private Map<String, HandHistoryListTabs> histories = new HashMap<>();
	private JTabbedPane tabs;
	
	/**
	 * set up DataPresentation tabs and ensure these
	 * have an interface to the hand history
	 */
	public TableAndChartsViewer(){
		super();

		this.setLayout(new BorderLayout());
		
		tabs = new JTabbedPane();
        tabs.addChangeListener(event ->
        {
            if (event.getSource() instanceof JTabbedPane) {
                String label = ((JTabbedPane)event.getSource()).getSelectedComponent().getName();
                processTabChange(label);
            }
        });
				
		this.add(tabs, BorderLayout.CENTER);
	}

    public Map<String, DataTable> getTables(){
        return tables;
    }

    public String getSelectedTableName(){
        return tabs.getSelectedComponent().getName();
    }

    public DataTable getSelectedTable(){
        return tables.get(getSelectedTableName());
    }
	
	/**
	 * Creates a new HandHistoryTab DataTable
	 * which is linked directly via a reference
	 * @param name
	 * @param h
	 * @return
	 */
	public DataTable addNewTable(String name, final HandHistoryListTabs h){

        tables.put(name, new DataTable(name, h)); // h was an argument here. still trying to figure out what its doing.

        tabs.addTab(name, tables.get(name));
        tabs.getComponentAt(tabs.getTabCount()-1).setName(name);
        tabs.setTabComponentAt(tables.size()-1, new TabCloseButton(name, tabs));
        tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

		return tables.get(name);
	}
	
    public void removeTabsFor(String label){
        GuiFrame.SINGLETON.removeTabsFor(label);
    }

    public void removeTab(String label){
        if(tabs!=null && tabs.getTabCount()>0){
            for (int i=tabs.getTabCount()-1; i>=0; i--){
               if(tabs.getComponentAt(i).getName().equals(label)){
                    tabs.remove(i);
                    tables.remove(label);
                    histories.remove(label);
                }
            }
        }
    }

    public void processTabChange(String label){
        GuiFrame.SINGLETON.setActiveTab(label);
    }

    public void setActiveTab(String label){
        if(tabs!=null && tabs.getTabCount()>0 && label!=null){
            for (int i=tabs.getTabCount()-1; i>=0; i--){
                if(tabs.getComponentAt(i).getName().equals(label)){
                    tabs.setSelectedIndex(i);
                }
            }
        }
    }

    public ArrayList<PokerDataModel> getDataTables(){
        ArrayList<PokerDataModel> dataModels = new ArrayList<PokerDataModel>();
        for (DataTable table : tables.values()){
            dataModels.add(table.getModel());
        }
        return dataModels;
    }
}
