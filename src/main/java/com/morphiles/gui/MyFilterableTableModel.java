    package com.morphiles.gui;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

/**
 * Copyright (c) 2002-2013 EAN.com, L.P. All rights reserved.
 *
 * @author robevans
 */
public class MyFilterableTableModel extends JTable {
    private FilterableField filterableField;
    private FilterableColumn filterableColumn;
    private int DEFAULT_FIELD_WIDTH=20;

    public MyFilterableTableModel(){
        super();
        setModel(new FilterableColumn());
        filterableColumn = new FilterableColumn();
    }

    public void setModel(AbstractTableModel model){
        if(!(model instanceof FilterableColumn)){
            throw new IllegalArgumentException();
        }
        super.setModel(model);
    }

    public void addItem(Object o, int row, int column){
        ((FilterableColumn)getModel()).setValueAt(0, row, column);
    }

//    public JTextField getFilterField(){
//        return filterableColumn;
//    }

    class FilterableField extends JTextField{
        public FilterableField(){
            super();
        }
    }

    class FilterableColumn extends AbstractTableModel {
       ArrayList<ArrayList> items;
       ArrayList<ArrayList> filterItems;


       @Override
       public int getRowCount() {
           return filterItems.size();
       }

       @Override
       public int getColumnCount() {
           return items.get(0).size();
       }

       @Override
       public Object getValueAt(int rowIndex, int columnIndex) {
           if (rowIndex < filterItems.size()){
            return filterItems.get(rowIndex).get(columnIndex);
           } else {
               return null;
           }
       }

        public int getSize(){
            return filterItems.size();
        }
    }
}
