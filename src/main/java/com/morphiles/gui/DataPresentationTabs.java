package com.morphiles.gui;

import com.morphiles.views.DataTable;
import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.table.AbstractTableModel;
import org.springframework.beans.factory.annotation.Autowired;

public class DataPresentationTabs extends JPanel {
	
	private Map<String, DataTable> tables = new HashMap<>();
	private Map<String, DataBarChart> charts = new HashMap<>();
	private Map<String, HandHistoryTab> histories = new HashMap<>();
	private JTabbedPane tabs;

	@Autowired
	HandHistoryListTabs hhListTabs;
	
	/**
	 * set up DataPresentation tabs and ensure these
	 * have an interface to the hand history
	 */
	@Autowired
	public DataPresentationTabs(HandHistoryListTabs hhListTabs){
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

    public Map<String, DataTable> getTables(){
        return tables;
    }

    public String getSelectedTableName(){
        return tabs.getSelectedComponent().getName();
    }

    public DataTable getSelectedTable(){
        String tableName = getSelectedTableName();
        return tables.get(tableName);
    }
	
	/**
	 * Creates a new HandHistoryTab AllDataTable
	 * which is linked directly via a reference
	 * @param name
	 * @return
	 */
	public DataTable addNewTable(String name){

		if (hhListTabs !=null) {

			tables.put(name, new DataTable(name, hhListTabs));

			tabs.addTab(name, tables.get(name));
			tabs.getComponentAt(tabs.getTabCount() - 1).setName(name);
			tabs.setTabComponentAt(tables.size() - 1, new TabCloseButton(name, tabs));
			tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

			//charts.put(name + "_bar", new DataBarChart());
			//tabs.addTab(name + "_bar", charts.get(name+"_bar"));
		}

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

//    public void removeTabsFor(String label){
//        gui.removeTabsFor(label);
//    }

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

	/**
	 * this should make the relevant tabs visible in the display
	 *
	 * @param label
	 */
	public void processTabChange(String label){

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
