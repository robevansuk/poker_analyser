package com.morphiles.models;

import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

/**
 * @author robevans
 */
public class PokerDataModel implements TableModel, TableModelListener {

    EventListenerList listenerList = new EventListenerList();
    TableModel delegatedModel;
    int[] sortedIndicies;
    int sortColumn;
    Comparator comparator;
    Comparator[] comparators;

    // public Vector<Vector> data;

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
            "F Outs",
            "F Contribution",
            "F Mv",
            "F Pot",
            "Turn",
            "T Hand",
            "T Outs",
            "T Contribution",
            "T Mv",
            "T Pot",
            "River",
            "R Hand",
            "R Contribution",
            "R Mv",
            "R Pot",
            "Seat",
            "SB",
            "BB",
            "Ante",
            "BuyIn",
            "Currency",
            "Date",
            "Time",
            "Joined",
            "LimitType",
            "Real-Play",
            "Tbl Id",
            "Gm Type",
            "Pf Plyr Cnt",
            "F Plyr Cnt",
            "T Plyr Cnt",
            "R Plyr Cnt",
            "Total Plyr Cnt"
    };

    private static ArrayList<String> BLANK_ROW = new ArrayList<>();
    private boolean isFreeroll;
    private String playerID;
    private String gameType;
    private String stakesLevel;

    // initialise the table with a blank row to begin with.
    static {
        for (int i=0; i<columnNames.length; i++){
            BLANK_ROW.add("");
        }
    }

    public PokerDataModel() {
        delegatedModel = new DefaultTableModel(columnNames, 0);
        delegatedModel.addTableModelListener(this);
        comparators = new Comparator[columnNames.length];
        sortedIndicies = new int[]{};
        setSortColumn(0);

        //addTableModelListener (this);
    }

    public String getGameType(){
        return gameType;
    } // TRNY or CASH

    public boolean isFreeroll(){
        return isFreeroll;
    }

    public void setIsFreeroll(boolean yn){
        isFreeroll = yn;
    }

    public String getStakesLevel(){
        return stakesLevel;
    }

    public void setStakesLevel(String stakes_or_buy_in){
        stakesLevel = stakes_or_buy_in;
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
            return delegatedModel.getValueAt(0, columnIndex).getClass();
        else
            return Object.class;
    }

    public void setComparatorForColumn(Comparator c, int i){
        if(i>comparators.length){
            Comparator[] newComparators = new Comparator[i+1];
            System.arraycopy(comparators, 0, newComparators, 0, comparators.length);
            comparators = newComparators;
        }
        comparators[i]=c;
    }


    public int getColumnNamed(String colName){
        for (int i = 0; i<columnNames.length; i++){
            if(columnNames[i].equals(colName)){
                return i;
            }
        }
        return -1;
    }

    public int getColumnCount() {
        return delegatedModel.getColumnCount();
    }

    public String getColumnName (int index) {
        return delegatedModel.getColumnName(index);
    }

    public int getRowCount() {
        return delegatedModel.getRowCount();
    }

    public int getDelegatedRow(int row){
        return sortedIndicies[row];
    }

    public Object getValueAt (int rowIndex, int columnIndex) {
        return delegatedModel.getValueAt(getDelegatedRow(rowIndex), columnIndex);
    }

    public boolean isCellEditable (int rowIndex, int columnIndex) {
        return delegatedModel.isCellEditable(rowIndex, columnIndex);
    }

    public void setValueAt (Object aValue, int rowIndex, int columnIndex) {

        if (rowIndex>=getRowCount())
            ((DefaultTableModel)delegatedModel).getDataVector().add(new Vector<>(BLANK_ROW));

        if (aValue instanceof String) {
            ((Vector<String>)((DefaultTableModel)delegatedModel).getDataVector().get(rowIndex)).setElementAt(aValue.toString(), columnIndex);
            if(aValue != null && columnNames[columnIndex].equals("Gm Type")){
                gameType = (String) aValue;
            }
        }

        resort();
        fireAllChanged();
    }

    public void setPlayerID(String player){
        playerID = player;
    }

    public String getPlayerID(){
        return playerID;
    }

    public void tableChanged (TableModelEvent e) {
        switch (e.getType()) {
            case TableModelEvent.DELETE: {
                resort();
                fireAllChanged();
                break;
            }
            case TableModelEvent.INSERT: {
                resort();
                fireAllChanged();
                break;
            }
            case TableModelEvent.UPDATE: {
                resort();
                fireAllChanged();
                break;
            }
        }
    }

    protected void fireAllChanged() {
        TableModelEvent e = new TableModelEvent (this);
        fireTableModelEvent(e);
    }

    public void setSortColumn(int i) {
        sortColumn = i;

        comparator = null;
        if ((comparators !=null) && (comparators.length > 0))
            comparator = comparators[sortColumn];
        resort();
    }

    public int getSortColumn(){
        return sortColumn;
    }

    public void resort(){
        if(sortedIndicies.length != delegatedModel.getRowCount()){
            sortedIndicies = new int[delegatedModel.getRowCount()];
        }
        ArrayList sortMe = new ArrayList();
        for (int i = 0; i<delegatedModel.getRowCount(); i++){
            SortingDelegate sd = new SortingDelegate(delegatedModel.getValueAt(i, getSortColumn()), i);
            sortMe.add(sd);
        }
        SortingDelegateComparator sdc = new SortingDelegateComparator(comparator);
        Collections.sort(sortMe, sdc);

        for(int i=0; i<sortMe.size(); i++){
            sortedIndicies[i] = ((SortingDelegate)sortMe.get(i)).row;
        }
        fireAllChanged();
    }

    public class SortingDelegate extends Object{

        public Object value;
        public int row;

        public SortingDelegate (Object v, int r){
            value = v;
            row = r;
        }
    }

    class SortingDelegateComparator extends Object implements Comparator {

        Comparator comp;

        public SortingDelegateComparator(Comparator c){
            comp = c;
        }

        @Override
        public int compare(Object o1, Object o2){
            Object v1 = ((SortingDelegate)o1).value;
            Object v2 = ((SortingDelegate)o2).value;
            if(comp != null){
                return comp.compare(v1, v2);
            } else if (v1 instanceof Comparable){
                return ((Comparable)v1).compareTo(v2);
            } else {
                throw new IllegalArgumentException("Can't compare objects for sorting");
            }
        }

    }

    public void sortByProfit(){

        int i = 0;
        for (String col : columnNames){
            if (col.equals("Profit")){
                break;
            }
            i++;
        }
        setComparatorForColumn(new PotSizeComparator(), i);
        setSortColumn(i);
        fireAllChanged();
    }

    class PotSizeComparator implements Comparator {

        @Override
        public int compare(Object o1, Object o2){
            if (o1==o2){
                return 0;
            } else {
                if (((String)o1).length()>0 && ((String)o2).length()>0){
                    return (new BigDecimal(o1.toString()).compareTo(new BigDecimal(o2.toString())));
                } else if (((String)o1).length()==0){
                    return 1;
                } else if (((String)o2).length() == 0){
                    return -1;
                } else {
                    return -1;
                }
            }
        }

        public boolean equals(Object obj){
            return super.equals(obj);
        }
    }
}