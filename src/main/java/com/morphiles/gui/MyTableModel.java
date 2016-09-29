package com.morphiles.gui;

import java.util.ArrayList;
import java.util.Vector;

import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class MyTableModel implements TableModel, TableModelListener {

	public Vector<Vector> data;
	
	private static String[] columnNames = {
		"Id",
		"Position",
        "Player",
        "Stack",
        "Cards",
        "Win",
        "Profit",
        "Pf Hand",
        "Pf Contribution",
        "Pf Mv",
        "Pf Pot",
        "Flop",
        "F Hand",
        "F Contribution",
        "F Mv",
        "F Pot",
        "Turn",
        "T Hand",
        "T Contribution",
        "T Mv",
        "T Pot",
        "River",
        "R Hand",
        "R Contribution",
        "R Mv",
        "R Pot",
        "Seat",
        "Joined",
        "Real-Play",
        "Tbl Id",
        "Gm Type",
        "Currency",
        "Pf Plyr Cnt",
        "F Plyr Cnt",
        "T Plyr Cnt",
        "R Plyr Cnt",
        "Total Plyr Cnt"
        };
	
	private static ArrayList<String> BLANK_ROW = new ArrayList<String>(); 
	static {
		for (int i=0; i<columnNames.length; i++){
			BLANK_ROW.add("");
		}
	}
	
	EventListenerList listenerList = new EventListenerList();
    

    public MyTableModel() {
        data = new Vector<Vector>();
        addTableModelListener (this);
    }

    
    // listener stuff
    public void addTableModelListener (TableModelListener l) {
        listenerList.add (TableModelListener.class, l);
    }
    
    public void removeTableModelListener (TableModelListener l) {
        listenerList.remove (TableModelListener.class, l);
    }

    public void fireTableModelEvent (TableModelEvent e) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length-1; i>0; i--) {
            if (listeners[i] == TableModelListener.class) {
               ((TableModelListener) listeners[i+1]).tableChanged(e);
            }
        }
    }

    // contents stuff

    public Class getColumnClass(int columnIndex) {
        if (getRowCount() > 0)
            return getValueAt(0, columnIndex).getClass();
        else
            return Object.class;
    }

    public int getColumnCount() {
        return columnNames.length;
    }
    
    public String getColumnName (int index) { 
        return columnNames[index];
    }
    
    public int getRowCount() {
        return data.size();
    }
    
    
    public Object getValueAt (int rowIndex, int columnIndex) {
    	//System.out.println("Row: " + rowIndex + ", columnIndex: " + columnIndex);
    	return data.get(rowIndex).get(columnIndex);
        //return getValueAt (getDelegatedRow(rowIndex), columnIndex);
    }
    
    public boolean isCellEditable (int rowIndex, int columnIndex) {
        return true;
    }
    
    public void setValueAt (Object aValue, int rowIndex, int columnIndex) {
    	//System.out.println(aValue+"");
    	
    	if (rowIndex>=getRowCount())
    	{
    		data.add(new Vector());
    		data.get(rowIndex).addAll(BLANK_ROW);
    	}
    	
    	if (aValue instanceof String)
    	{
    		data.get(rowIndex).set(columnIndex, (String) aValue);
    	}
    	
    	fireAllChanged();
    }

    public void tableChanged (TableModelEvent e) {
        switch (e.getType()) {
            case TableModelEvent.DELETE: {
               
                fireAllChanged();
                break;
            }
            case TableModelEvent.INSERT: {
              
                fireAllChanged();
                break;
            }
            case TableModelEvent.UPDATE: {
               
                fireAllChanged();
                break;
            }
        }
    }

    protected void fireAllChanged() {
        TableModelEvent e = new TableModelEvent (this);
        fireTableModelEvent(e);
    }  
}