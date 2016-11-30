package com.morphiles.gui;

import com.morphiles.models.PokerDataModel;
import com.morphiles.views.DataTable;
import com.morphiles.views.TableAndChartsViewer;
import java.awt.BorderLayout;
import java.util.Collection;
import java.util.Hashtable;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HandHistoryTabs extends JPanel {
	
	private BorderLayout layout = new BorderLayout();
	
	private JTabbedPane tabs;
	private Hashtable<String, HandHistoryListTabs> histories = new Hashtable<>();
	private Hashtable<String, DataTable> tables = new Hashtable<>();

    private int handId;

    @Autowired
    private TableAndChartsViewer datTabs;


	/**
	 * A hand Tabbed hand history panel that displays
     * the hand histories as a list rather than a document
     * this means individual players actions can be highlighted as
     * right/wrong at some point in the future
     * TODO add highlighting to show good/bad actions - i.e. bet when ahead, check when behind.
	 */
	@Autowired
	public HandHistoryTabs(TableAndChartsViewer datTabs){
		super();
		this.datTabs = datTabs;

        String name = "default";
        this.setLayout(layout);

        tabs = new JTabbedPane();
        tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tabs.addChangeListener(event ->
                {
                    if (event.getSource() instanceof JTabbedPane) {
                        String label = ((JTabbedPane)event.getSource()).getSelectedComponent().getName();
                        processTabChange(label);
                    }
                });

        addTabbedHistoryListPane(name);
		this.add(tabs, BorderLayout.CENTER);
	}
	
	/**
	 * Adds a JList Handhistory to a new tabbed pane
	 * @param name
	 */
	public DataTable addTabbedHistoryListPane(String name) {
        histories.put(name, new HandHistoryListTabs());

        if (!name.equals("default")){
            // Send notification to the MainGui to
            // create a new data table for the hand history
            tables.put(name, datTabs.addNewTable(name, histories.get(name)));

            tabs.add(name, histories.get(name));
            tabs.getComponentAt(tabs.getTabCount() - 1).setName(name);
            tabs.setTabComponentAt(histories.size() - 1, new TabCloseButton(name, tabs));
            return tables.get(name);
        }
        return null;
	}
	
	public HandHistoryListTabs get(String name) throws NullPointerException {
        if (histories!=null || histories.size()>0){
		    return histories.get(name);
        } else {
            throw new NullPointerException();
        }
	}

    public boolean contains(String name) throws NullPointerException {
        if (histories!=null || histories.size()>0){
            return (histories.containsKey(name) ? true : false);
        } else {
            throw new NullPointerException("Histories is either initialised or empty.");
        }
    }

    public DataTable getDataTable(String tableName) throws NullPointerException{
        if (tables!=null || tables.size()>0){
            return tables.get(tableName);
        } else {
            throw new NullPointerException("tables is either uninitialised or empty.");
        }
    }
	
	/**
	 * appends a line to the hand history specified.
	 * @param
	 */
	public void append(String name, String text){
		if (text != null && text.contains(tables.get(name).getHHProcessorFirstLine()))
		{
			histories.get(name).addLine(name, "------");
			tables.get(name).addData(text, handId, name);
            setHandId(getHandId()+1);
        } else {
			histories.get(name).addLine(name, text);
			tables.get(name).addData(text, handId, name);
		}
    }

    public void removeTabsFor(String label){
        if (!label.equals("default")) {
            datTabs.removeTab(label);
            removeTab(label);
        }
    }

    public void removeTab(String label){
        if(tabs!=null && tabs.getTabCount()!=0){
            for (int i=tabs.getTabCount()-1; i>=0; i--){
                if( tabs.getComponentAt(i).getName().equals(label)){
                    tabs.remove(i);
                    histories.remove(label);
                }
            }
        }
    }

    public void processTabChange(String label){
        if (!label.equals("default")) {
            if (this != null)
                setActiveTab(label);

            if (datTabs != null)
                datTabs.setActiveTab(label);

            // TODO set the selected item in the TreeNavigator to be in sync too.
        }
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

    public PokerDataModel getActiveTable(){
        if (tabs != null && tabs.getTabCount()>0){
            return tables.get(tabs.getTitleAt(tabs.getSelectedIndex())).getModel();
        } else {
            throw new NullPointerException();
        }
    }

    public Collection<DataTable> getTables(){
        return tables.values();
    }

    public void setHandId(int id){
        handId = id;
    }

    public int getHandId(){
        return handId;
    }
}