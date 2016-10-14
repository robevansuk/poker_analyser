package com.morphiles.gui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.HashMap;

public class DataPresentationTabs extends JPanel {
	
    private boolean DEBUG = false;
	
	private HashMap<String, com.morphiles.views.DataTable> tables= new HashMap<>();
	private HashMap<String, DataBarChart> charts = new HashMap<>();
	private HashMap<String, HandHistoryTab> histories = new HashMap<>();
	private JTabbedPane tabs;
	
	/**
	 * set up DataPresentation tabs and ensure these
	 * have an interface to the hand history
	 */
	public DataPresentationTabs(){
		super();
		this.setLayout(new BorderLayout());
		
		tabs = new JTabbedPane();
        tabs.addChangeListener(event ->
			{
				if (event.getSource() instanceof JTabbedPane){
					String label = ((JTabbedPane)event.getSource()).getSelectedComponent().getName();
					processTabChange(label);
				}
			});

		this.add(tabs, BorderLayout.CENTER);
	}

    public HashMap<String, com.morphiles.views.DataTable> getTables(){
        return tables;
    }

    public String getSelectedTableName(){
        return tabs.getSelectedComponent().getName();
    }

    public com.morphiles.views.DataTable getSelectedTable(){
        String tableName = getSelectedTableName();
        return tables.get(tableName);
    }
	
	/**
	 * Creates a new HandHistoryTab DataTable
	 * which is linked directly via a reference
	 * @param name
	 * @param h
	 * @return
	 */
	public com.morphiles.views.DataTable addNewTable(String name, final HandHistoryTab h){

        tables.put(name, new com.morphiles.views.DataTable(name, GuiFrame.SINGLETON.getHandHistoryTabs(name).get(name)));

        tabs.addTab(name, tables.get(name));
        tabs.getComponentAt(tabs.getTabCount()-1).setName(name);
        tabs.setTabComponentAt(tables.size()-1, new TabCloseButton(name, tabs));
        tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        //charts.put(name + "_bar", new DataBarChart());
        //tabs.addTab(name + "_bar", charts.get(name+"_bar"));

		return tables.get(name);
	}
	
	//public Object[][] gatherData(String name){
		//Object[][] data = new Object[histories.get(name).getRows()][30];
	//}
	
	class MyTabModel extends AbstractTableModel {
		private String[] columnNames = {"First Name",
				"Last Name",
				"Sport",
				"# of Years",
				"Vegetarian"};
		
		private Object[][] data = {
			};
		public int getColumnCount() {
			return columnNames.length;
		}
		public int getRowCount() {
			return data.length;
		}
		public String getColumnName(int col) {
			return columnNames[col];
		}           
		public Object getValueAt(int row, int col) {
			return data[row][col];         
		}           
		/*          
		 * JTable uses this method to determine the default renderer
		 * editor for each cell.  If we didn't implement this method,
		 * then the last column would contain text ("true"/"false"),
		 * rather than a check box.
		 */
		public Class getColumnClass(int c) { 
			return getValueAt(0, c).getClass();
		}           
		/*          * 
		 * Don't need to implement this method unless your table's          * 
		 * editable.          *
		 */
		public boolean isCellEditable(int row, int col) {
			//Note that the data/cell address is constant,
			//no matter where the cell appears onscreen.
			if (col < 2) {
				return false;
			} else {
				return true;
			}
		} 
		/*          * 
		 * Don't need to implement this method unless your table's          * 
		 * data can change.          */        
		public void setValueAt(Object value, int row, int col) {
			data[row][col] = value;
			fireTableCellUpdated(row, col);
		}
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
}
